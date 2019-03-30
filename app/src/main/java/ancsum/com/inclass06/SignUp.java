package ancsum.com.inclass06;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUp extends AppCompatActivity {

    private Handler handler;
    private UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        handler = new Handler(Looper.getMainLooper());
        final EditText firstName = findViewById(R.id.text_sign_up_first_name);
        final EditText lastName = findViewById(R.id.text_sign_up_last_name);
        final EditText email = findViewById(R.id.text_sign_up_email);
        final EditText password = findViewById(R.id.text_sign_up_choose_password);
        final EditText repeatPassword = findViewById(R.id.text_sign_up_repeat_password);

        Button signUp = findViewById(R.id.button_sign_up_sign_up);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!password.getText().toString().equals(repeatPassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Passwords don't match.", Toast.LENGTH_SHORT).show();
                } else {

                    OkHttpClient client = new OkHttpClient();

                    RequestBody formbody = new FormBody.Builder()
                            .add("email", email.getText().toString())
                            .add("password", password.getText().toString())
                            .add("fname", firstName.getText().toString())
                            .add("lname", lastName.getText().toString())
                            .build();


                    Request request = new Request.Builder()
                            .url(Constants.URL_SIGNUP)
                            .post(formbody)
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String user = response.body().string().toString();
                                try {

                                    JSONObject object = new JSONObject(user);
                                    userDetails = new UserDetails(object.getString("token"),
                                            object.getString("user_id"), object.getString("user_email"),
                                            object.getString("user_fname"), object.getString("user_lname"));

                                } catch (JSONException e) {
                                    throw new IOException(e.getMessage());
                                }
                            } else {
                                throw new IOException(response.message());
                            }

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "User Created!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUp.this, MessageThreads.class);
                                    intent.putExtra(Constants.USER_OBJECT, userDetails);
                                    Log.d("SUMUKH5", userDetails.toString());
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    });
                }

            }
        });

        Button signupCancel = findViewById(R.id.button_sign_up_cancel);
        signupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

    }
}
