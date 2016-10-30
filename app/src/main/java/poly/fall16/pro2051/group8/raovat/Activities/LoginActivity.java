package poly.fall16.pro2051.group8.raovat.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import poly.fall16.pro2051.group8.raovat.R;
import poly.fall16.pro2051.group8.raovat.helper.AppController;
import poly.fall16.pro2051.group8.raovat.helper.SQLiteHandler;
import poly.fall16.pro2051.group8.raovat.helper.SessionManager;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Button btLogin, btSkip;
    TextView tvForget, tvSignUp;
    EditText etUser, etPass;
    Dialog mDialog;
    ImageView ivFacebook, ivGoogle;

    private SessionManager session;
    private SQLiteHandler db;

    // Facebook Sdk
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    // Google Login
    private static final int REQUEST_CODE = 100;
    private SignInButton btSignIn;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions googleSignInOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setVies();
        setDialog();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();



        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());


        //printKeyhash(); // facebook keyhash

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





        eventFacebookLogin();
        eventLoginGoogle();


    }

    public void setVies(){
        btLogin = (Button) findViewById(R.id.btLogin);
        btSkip = (Button) findViewById(R.id.btSkip);
        tvForget = (TextView) findViewById(R.id.tvForget);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        etUser = (EditText) findViewById(R.id.etUser);
        etPass = (EditText) findViewById(R.id.etPassword);
        ivFacebook = (ImageView) findViewById(R.id.ivFacebook);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        ivGoogle = (ImageView) findViewById(R.id.ivGoogle);
        btSignIn = (SignInButton) findViewById(R.id.btSignInButton);
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
                            String address = user.getString("address");

                            // Inserting row in users table
                            db.addUser(name, email, fullname, phone, uid, profile_url, address);

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

    public void printKeyhash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "poly.fall16.pro2051.group8.raovat",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    public void eventFacebookLogin(){

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                loginButton.setReadPermissions(Collections.singletonList("public_profile, email, user_birthday, user_friends"));

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Bundle bFacebookData = getFacebookData(object);
                        checkReg(bFacebookData.getString("email"), bFacebookData.getString("first_name")+bFacebookData.getString("last_name"), bFacebookData.getString("email"), "", "2");
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();

                //String profileImgUrl = "https://graph.facebook.com/" + userId + "/picture?type=large";


            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Đăng nhập bị hủy!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FacebookException", error.toString());
            }

        });
        ivFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();

            }
        });

    }


    private Bundle getFacebookData(JSONObject object) {

        Bundle bundle = new Bundle();
        try {
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name")) {
                bundle.putString("first_name", object.getString("first_name"));
            }
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email")) {
                bundle.putString("email", object.getString("email"));
            }
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bundle;

    }

        @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE &&  resultCode != 0) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = result.getSignInAccount();

            Toast.makeText(this, "Xin chào " + account.getDisplayName(), Toast.LENGTH_SHORT).show();

        }
    }



    private void checkReg(final String username,final String fullname,final String email,final String phone, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        showDialog();

        String OLD_URL = "http://demophp2.esy.es/user.php";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                OLD_URL , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d("TAG", "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    Toast.makeText(getBaseContext(),response,Toast.LENGTH_LONG).show();
                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(LoginActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i);
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("txtEmail", email);
                params.put("txtPassword", password);
                params.put("txtUsername",username);
                params.put("txtPhone",phone);
                params.put("txtFullName",fullname);
                //params.put("txtImage", encodedImage);
                params.put("action","register");

                return params;
            }

        };

        // Adding request to request queue
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }


    public void eventLoginGoogle(){
        // google
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
        btSignIn.setSize(SignInButton.SIZE_WIDE);
        btSignIn.setScopes(googleSignInOptions.getScopeArray());

        ivGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, REQUEST_CODE);

                // Logout
                //Auth.GoogleSignInApi.signOut(googleApiClient);

            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFailed", Toast.LENGTH_SHORT).show();
    }
}
