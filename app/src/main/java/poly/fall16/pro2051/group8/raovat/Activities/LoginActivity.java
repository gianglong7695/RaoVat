package poly.fall16.pro2051.group8.raovat.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import poly.fall16.pro2051.group8.raovat.R;

public class LoginActivity extends AppCompatActivity {
    Button btLogin;
    TextView tvForget, tvSignUp;
    EditText etUser, etPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setVies();

        // Underline in text
        tvForget.setPaintFlags(tvForget.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        tvSignUp.setPaintFlags(tvSignUp.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        // Set font chữ về mặc định
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
                Intent it = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(it);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void setVies(){
        btLogin = (Button) findViewById(R.id.btLogin);
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
}
