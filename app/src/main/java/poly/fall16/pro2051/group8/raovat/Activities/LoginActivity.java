package poly.fall16.pro2051.group8.raovat.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import poly.fall16.pro2051.group8.raovat.R;
import poly.fall16.pro2051.group8.raovat.helper.AppController;
import poly.fall16.pro2051.group8.raovat.helper.SQLiteHandler;
import poly.fall16.pro2051.group8.raovat.helper.SessionManager;

public class LoginActivity extends AppCompatActivity {
    Button btLogin, btSkip;
    TextView tvForget, tvSignUp;
    EditText etUser, etPass;
    Dialog mDialog;

    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setVies();
        setDialog();
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Underline in text
        tvForget.setPaintFlags(tvForget.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        tvSignUp.setPaintFlags(tvSignUp.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        // Set font default
        etPass.setTypeface(Typeface.DEFAULT);

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(it);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin(etUser.getText().toString(), etPass.getText().toString());
            }
        });
        btSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,
                        MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void setVies(){
        btLogin = (Button) findViewById(R.id.btLogin);
        btSkip = (Button) findViewById(R.id.btSkip);
        tvForget = (TextView) findViewById(R.id.tvForget);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        etUser = (EditText) findViewById(R.id.etUser);
        etPass = (EditText) findViewById(R.id.etPassword);
    }

    public static void underlineText(TextView textView, String text){
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
    }

    private void checkLogin(final String email, final String password) {
        if(!email.equals("") && !password.equals("")){
            // Tag used to cancel the request
            String tag_string_req = "req_login";
            showDialog();

            String mUrl = "http://demophp2.esy.es/user.php";
            String URL ="http://demophp2.esy.es/user.php?action=login&&txtUsername=" + email + "&&txtPassword="+ password;
            StringRequest strReq = new StringRequest(URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("TAG", "Response: " + response.toString());
                    hideDialog();
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");

                        // Check for error node in json
                        if (!error) {
                            // user successfully logged in
                            // Create login session
                            session.setLogin(true);

                            // Now store the user in SQLite
                            String uid = jObj.getString("uid");

                            JSONObject user = jObj.getJSONObject("user");
                            String name = user.getString("username");
                            String email = user.getString("email");
                            String phone = user.getString("phone");
                            String fullname = user.getString("fullName");
                            String profile_url= user.getString("profile_url");

                            // Inserting row in users table
                            db.addUser(name, email, fullname, phone, uid, profile_url);

                            // Launch main activity
                            Intent intent = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            finish();
                        } else {
                            // Error in login. Get the error message
                            Toast.makeText(LoginActivity.this, jObj.getString("error_msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Json error: " + e.toString(), Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", "Login Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(), "Login Error :" +
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url
                    Map<String, String> params = new HashMap<>();
                    params.put("txtUsername", email);
                    params.put("txtPassword", password);
                    params.put("action", "login");
                    return params;
                }

            };
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }else{
            Toast.makeText(this, "Hãy nhập tài khoản & mật khẩu!", Toast.LENGTH_SHORT).show();
        }
    }

    public void setDialog(){
        mDialog = new Dialog(LoginActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // No title
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); // No backfround
        mDialog.setContentView(R.layout.custom_lg_login_dialog);
        mDialog.setCancelable(false); // Disable cancel
    }

    public void showDialog(){
        mDialog.show();
    }

    public void hideDialog(){
        mDialog.hide();
    }
}
