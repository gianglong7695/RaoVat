package poly.fall16.pro2051.group8.raovat.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import poly.fall16.pro2051.group8.raovat.R;

public class PostListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}
