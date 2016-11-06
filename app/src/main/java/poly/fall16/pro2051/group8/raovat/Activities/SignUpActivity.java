package poly.fall16.pro2051.group8.raovat.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import poly.fall16.pro2051.group8.raovat.R;

public class SignUpActivity extends AppCompatActivity {
    TextView tvLogo;
    Typeface mTypeface; // Create a font
    public static EditText etUser, etPass, etRePass;
    Button btSignUp;
    public static Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setViews();
        mActivity = this;

        mTypeface = Typeface.createFromAsset(getAssets(), "victoria.ttf");
        tvLogo.setTypeface(mTypeface);
        // Set font default
        etPass.setTypeface(Typeface.DEFAULT);
        etRePass.setTypeface(Typeface.DEFAULT);

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUser.getText().toString().equals("")) {
                    etUser.setError("Không được để trống!");
                    etUser.requestFocus();
                } else if (etPass.getText().toString().equals("")) {
                    etPass.setError("Không được để trống!");
                    etPass.requestFocus();
                } else if (etRePass.getText().toString().equals("")) {
                    etRePass.setError("Không được để trống!");
                    etRePass.requestFocus();
                }else {
                    if(etUser.getText().toString().trim().length() > 5 && etUser.getText().toString().trim().length() < 16){
                        if (etPass.getText().toString().trim().length() < 5 || etPass.getText().toString().trim().length() > 16) {
                            etPass.setError("Độ dài không hợp lệ!");
                            etPass.requestFocus();
                        } else if (etRePass.getText().toString().trim().length() < 5 || etRePass.getText().toString().trim().length() > 16) {
                            etRePass.setError("Độ dài không hợp lệ!");
                            etRePass.requestFocus();
                        }else{
                            if(etPass.getText().toString().equals(etRePass.getText().toString())){
                                Intent it = new Intent(getApplicationContext(), SignUpDetailActivity.class);
                                startActivity(it);
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            }else{
                                etRePass.setError("Mật khẩu không khớp!");
                                etRePass.requestFocus();
                            }
                        }
                    }else {
                        etUser.setError("Độ dài không hợp lệ!");
                        etUser.requestFocus();
                    }

                }
            }
        });

        etRePass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    btSignUp.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void setViews() {
        tvLogo = (TextView) findViewById(R.id.tvLogo);
        btSignUp = (Button) findViewById(R.id.btSignUp);
        etUser = (EditText) findViewById(R.id.etUser);
        etPass = (EditText) findViewById(R.id.etPassword);
        etRePass = (EditText) findViewById(R.id.etRePassword);
    }




}
