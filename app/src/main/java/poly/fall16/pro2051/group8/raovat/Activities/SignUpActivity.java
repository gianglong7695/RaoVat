package poly.fall16.pro2051.group8.raovat.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import poly.fall16.pro2051.group8.raovat.R;

public class SignUpActivity extends AppCompatActivity {
    TextView tvLogo;
    Typeface mTypeface; // Create a font
    EditText etUser, etPass, etRePass;
    Button btSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setViews();

        mTypeface = Typeface.createFromAsset(getAssets(), "victoria.ttf");
        tvLogo.setTypeface(mTypeface);

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUser.getText().toString().equals("")) {
                    etUser.setError("Không được để trống!");
                } else if (etPass.getText().toString().equals("")) {
                    etPass.setError("Không được để trống!");
                } else if (etRePass.getText().toString().equals("")) {
                    etRePass.setError("Không được để trống!");
                }else {
                    Toast.makeText(SignUpActivity.this, "Oke!!!", Toast.LENGTH_SHORT).show();
                }
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
