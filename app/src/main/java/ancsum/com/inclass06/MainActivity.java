package ancsum.com.inclass06;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Chat Room");
        final EditText email = findViewById(R.id.text_chat_room_email);
        final EditText password = findViewById(R.id.text_chat_room_password);
        Log.d("info", email.getText().toString() + " " + password.getText().toString());
        findViewById(R.id.btn_chat_room_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new DoLoginTask(MainActivity.this, userDetails).execute(email.getText().toString(), password.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn_chat_room_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        EditText email = findViewById(R.id.text_chat_room_email);
        email.setText("");
        EditText pass = findViewById(R.id.text_chat_room_password);
        pass.setText("");
        userDetails = null;
    }


}

class DoLoginTask extends AsyncTask<String, Void, Void> {

    private final OkHttpClient client = new OkHttpClient();
    private Activity context;
    private Response response;
    private UserDetails userDetails;

    public DoLoginTask(Activity context, UserDetails userDetails) {
        this.context = context;
        this.userDetails = userDetails;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        if (response.isSuccessful()) {
            Intent messageThreads = new Intent(context, MessageThreads.class);
            messageThreads.putExtra(Constants.USER_OBJECT, userDetails);
            context.startActivityForResult(messageThreads, 500);
            context.finish();
        } else {
            Toast.makeText(context, "Login not successful. Try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected Void doInBackground(String... strings) {
        String email = strings[0];
        String password = strings[1];

        Log.d("info", email + " " + password);

        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(Constants.URL_LOGIN)
                .post(formBody)
                .build();

        try {
            response = client.newCall(request).execute();
            String user = response.body().string().toString();
            JSONObject object = new JSONObject(user);
            userDetails = new UserDetails(object.getString("token"),
                    object.getString("user_id"), object.getString("user_email"),
                    object.getString("user_fname"), object.getString("user_lname"));


            Log.d("demo", response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
