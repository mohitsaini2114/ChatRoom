package ancsum.com.inclass06;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MessageThreads extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_threads);
        Constants.MESSAGE_THREAD_CONTEXT = getApplicationContext();
        final UserDetails userDetails = (UserDetails) getIntent().getSerializableExtra(Constants.USER_OBJECT);
        TextView userName = findViewById(R.id.text_message_threads_name);
        Constants.MESSAGE_THREAD_LIST_VIEW = findViewById(R.id.listview_message_threads_threads);
        userName.setText(userDetails.getUserFname() + " " + userDetails.getUserLname());
        new FetchMessageThreads(getApplicationContext(), userDetails).execute(userDetails);
        findViewById(R.id.img_message_threads_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDetails.setToken(null);
                userDetails.setUserEmail(null);
                userDetails.setUserFname(null);
                userDetails.setUserLname(null);
                userDetails.setUserID(null);
                startActivity(new Intent(MessageThreads.this, MainActivity.class));
                finish();
            }
        });

        findViewById(R.id.img_message_threads_add_thread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = findViewById(R.id.text_message_threads_add_new_thread);
                String newMessageThreadName = textView.getText().toString();
                if (newMessageThreadName.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Add new thread!", Toast.LENGTH_SHORT).show();
                } else {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody formBody = new FormBody.Builder()
                            .add("title", newMessageThreadName)
                            .build();
                    final Request request = new Request.Builder()
                            .url(Constants.URL_ADD_THREAD)
                            .header("Authorization", "BEARER "+ userDetails.getToken())
                            .post(formBody)
                            .build();
                    client.newCall(request).enqueue(new Callback() {

                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.body().string().toString());
                                    final JSONObject object = jsonObject.getJSONObject("thread");
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                Constants.THREADS_ADAPTER.add(new Threads(object.getString("user_fname"),
                                                        object.getString("user_lname"),
                                                        object.getString("user_id"),
                                                        object.getString("id"),
                                                        object.getString("title"),
                                                        object.getString("created_at")));
                                                TextView text = findViewById(R.id.text_message_threads_add_new_thread);
                                                text.setText("");
                                                text.clearFocus();
                                                Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                } catch (JSONException e) {
                                    throw new IOException(e.getMessage());
                                }

                            }

                            else{
                                Log.d("demo", "Response was not successful"+ response.code());
                            }
                        }
                    });
                }
            }
        });

        Constants.MESSAGE_THREAD_LIST_VIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Threads thread = (Threads) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), IndividualThreadActivity.class);
                intent.putExtra(Constants.USER_OBJECT, userDetails);
                intent.putExtra(Constants.THREAD_OBJECT, thread);
                startActivity(intent);
            }
        });


    }
}

class FetchMessageThreads extends AsyncTask<UserDetails, Void, Integer> {

    private Context context;
    public static ArrayList<Threads> threadsArrayList;
    private OkHttpClient client;
    private Request request;
    private Response response;
    private ResponseBody responseBody;
    private ListView messageThreadsListView;
    public UserDetails userDetails;

    public FetchMessageThreads(Context context, UserDetails userDetails) {
        this.context = context;
        this.userDetails = userDetails;
    }

    @Override
    protected void onPreExecute() {
        messageThreadsListView = Constants.MESSAGE_THREAD_LIST_VIEW;
    }

    @Override
    protected Integer doInBackground(UserDetails... user) {
        threadsArrayList = new ArrayList<>();
        this.userDetails = user[0];
        Log.d("sumukh", userDetails.toString());
        client = new OkHttpClient();
        request = new Request.Builder()
                .url(Constants.URL_THREADS)
                .addHeader("Authorization", "BEARER " + userDetails.getToken())
                .build();

        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                responseBody = response.body();
                String results = responseBody.string();
                JSONObject jsonObject = new JSONObject(results);
                JSONArray arrayObject = jsonObject.getJSONArray("threads");

                for (int i = 0; i < arrayObject.length(); i++) {
                    JSONObject object = arrayObject.getJSONObject(i);
                    threadsArrayList.add(new Threads(object.getString("user_fname"),
                            object.getString("user_lname"),
                            object.getString("user_id"),
                            object.getString("id"),
                            object.getString("title"),
                            object.getString("created_at")));
                }
            } else {
                throw new IOException("Error in Response");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response.code();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (integer >= 200 && integer < 300) {
            Constants.THREADS_ADAPTER = new ListViewAdapter(Constants.MESSAGE_THREAD_CONTEXT, threadsArrayList, userDetails);
            Constants.THREADS_ADAPTER.setNotifyOnChange(true);
            messageThreadsListView.setAdapter(Constants.THREADS_ADAPTER);

        }
    }


    public static class ListViewAdapter extends ArrayAdapter<Threads> {

        private ArrayList<Threads> list;
        private UserDetails userDetails;
        public Handler handler;

        public ListViewAdapter(@NonNull Context context, @NonNull ArrayList<Threads> objects, UserDetails userDetails) {
            super(context, 0, objects);
            this.list = objects;
            this.userDetails = userDetails;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            handler = new Handler(Looper.getMainLooper());
            final Threads messageThread = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_layout, parent, false);
            }

            String title = messageThread.getTitle();
            String userId = messageThread.getUserID();
            final String threadId = messageThread.getThreadID();
            TextView threadName = convertView.findViewById(R.id.text_list_view_message_thread_name);
            threadName.setText(title);
            ImageView buttonDelete = convertView.findViewById(R.id.img_list_view_message_thread_cancel);
            //buttonDelete.setEnabled(true);
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("demo","In click");
                    Log.d("demo","URL is :"+Constants.URL_THREAD_DELETE + "/" + threadId);
                    Log.d("demo","Token is :" + userDetails.getToken());
                    OkHttpClient client = new OkHttpClient();
                    final Request request = new Request.Builder()
                            .url(Constants.URL_THREAD_DELETE + "/" + threadId)
                            .header("Authorization", "BEARER "+userDetails.getToken())
                            .build();

                    client.newCall(request).enqueue(new Callback() {



                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                threadsArrayList.remove(position);
                                handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Constants.THREADS_ADAPTER.remove(messageThread);
                                    }
                                });
                            }
                            else{
                                Log.d("demo", "Response was not successful"+ response.code());
                            }
                        }
                    });



                }
            });
            Log.d("sumukh1", userDetails.toString());

            if (userDetails.getUserID().equalsIgnoreCase(userId)) {
                //Log.d("sumukh", "user id = " + userId);
                buttonDelete.setVisibility(View.VISIBLE);
                buttonDelete.setEnabled(true);
                buttonDelete.setClickable(true);
            } else {
                //Log.d("sumukh", "user id = " + userId);
                buttonDelete.setVisibility(View.GONE);
            }
            return convertView;

        }

    }
}