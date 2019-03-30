package ancsum.com.inclass06;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class IndividualThreadActivity extends AppCompatActivity {

    private ArrayList<Message> messageArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_messages);
        final UserDetails userDetails = (UserDetails) getIntent().getExtras().get(Constants.USER_OBJECT);
        final Threads thread = (Threads) getIntent().getExtras().get(Constants.THREAD_OBJECT);
        setTitle("Chatroom");
        TextView threadName = findViewById(R.id.text_individual_thread_thread_name);
        threadName.setText(thread.getTitle());

        ImageView homeButton = findViewById(R.id.img_individual_thread_home);
        homeButton.setClickable(true);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MessageThreads.class)
                        .putExtra(Constants.USER_OBJECT, userDetails));
                finish();
            }
        });

        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Constants.URL_GET_MESSAGES_ALL + "/" + thread.getThreadID())
                .addHeader("Authorization", "BEARER " + userDetails.getToken())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("IndividualThread", Thread.currentThread().getName());
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    MessagesResponse messagesResponse = gson
                            .fromJson(response.body().string().toString(),
                                    MessagesResponse.class);
                    messageArrayList = messagesResponse.messageList;
                    Log.d("IndividualThread", messageArrayList.toString());
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ListView messagesListView = findViewById(R.id.list_view_individual_thread_message);
                            Constants.MESSAGES_ADAPTER = new MessagesListViewAdapter(getApplicationContext(), messageArrayList, userDetails);
                            Constants.MESSAGES_ADAPTER.setNotifyOnChange(true);
                            messagesListView.setAdapter(Constants.MESSAGES_ADAPTER);
                        }
                    });
                }

            }
        });

        final EditText message = findViewById(R.id.text_individual_thread_add_message);
        ImageView send = findViewById(R.id.img_individual_messages_send);
        send.setClickable(true);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!message.getText().toString().matches("\\s*")) {
                    OkHttpClient httpClient = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("message", message.getText().toString())
                            .add("thread_id", thread.getThreadID())
                            .build();

                    Request req = new Request.Builder()
                            .post(body)
                            .addHeader("Authorization", "BEARER " + userDetails.getToken())
                            .url(Constants.URL_ADD_MESSAGE_TO_THREAD)
                            .build();

                    httpClient.newCall(req).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                Log.d("IndividualThreadAdd", response.message() + " " + response.code());
                                Gson gson = new Gson();
                                MessageResponseSingle responseSingle = gson
                                        .fromJson(response.body().string().toString(), MessageResponseSingle.class);
                                final Message singleMessage = responseSingle.message;
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Constants.MESSAGES_ADAPTER.insert(singleMessage, 0);
                                        TextView textView = findViewById(R.id.text_individual_thread_add_message);
                                        textView.setText("");
                                        textView.clearFocus();
                                    }
                                });

                            }
                        }
                    });

                }
            }
        });


    }
}




class MessagesListViewAdapter extends ArrayAdapter<Message> {

    private List<Message> messageList;
    private UserDetails user;
    private Context context;

    @Override
    public int getCount() {
        Log.d("IndividualThread", "" + messageList.size());
        return messageList.size();
    }

    public MessagesListViewAdapter(@NonNull Context context, @NonNull List<Message> objects, UserDetails user) {
        super(context, 0, objects);
        this.messageList = new ArrayList<>();
        this.messageList = objects;
        this.user = user;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        final Message message = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.messages_list_view_layout, parent, false);
            viewHolder.messageString = convertView.findViewById(R.id.text_messages_list_view_message);
            viewHolder.messageString.setMovementMethod(new ScrollingMovementMethod());
            viewHolder.senderName = convertView.findViewById(R.id.text_messages_list_view_sender);
            viewHolder.timestamp = convertView.findViewById(R.id.text_messages_list_view_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.messageString.setText(message.getMessageString());
        if ((message.getUserFname() + " " + message.getUserLname()).equals(user.getUserFname() + " " + user.getUserLname())) {
            viewHolder.senderName.setText("Me");
        } else {
            viewHolder.senderName.setText(message.getUserFname() + " " + message.getUserLname());
        }
        viewHolder.timestamp.setText(getPrettyTime(message.getCreatedAtTime()));
        ImageView deleteMessage = convertView.findViewById(R.id.btn_messages_list_view_delete_message);
        if (message.getUserID().equalsIgnoreCase(user.getUserID())) {
            deleteMessage.setVisibility(View.VISIBLE);
        } else {
            deleteMessage.setVisibility(View.GONE);
        }

        deleteMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .addHeader("Authorization", "BEARER " + user.getToken())
                        .url(Constants.URL_INDIVIDUAL_MESSAGE_DELETE + "/" + message.getMessageID())
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Constants.MESSAGES_ADAPTER.remove(message);
                                }
                            });
                        }
                    }
                });

            }
        });



        return convertView;
    }


    private String getPrettyTime(String time) {
        String formatedTime = null;
        PrettyTime prettyTime = new PrettyTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date date = simpleDateFormat.parse(time);
            formatedTime = prettyTime.format(date);
            return formatedTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatedTime;
    }

    private class ViewHolder {
        public TextView messageString;
        public TextView senderName;
        public TextView timestamp;
    }
}




