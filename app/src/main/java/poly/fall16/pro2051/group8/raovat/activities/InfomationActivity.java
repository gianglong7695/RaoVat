package poly.fall16.pro2051.group8.raovat.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import poly.fall16.pro2051.group8.raovat.R;

public class InfomationActivity extends AppCompatActivity {
    String [] arr = {"Điều khoản sử dụng","Tính năng & Hướng dẫn", "Kiểm tra phiên bản mới", "Hỗ trợ", "Liên hệ"};
    ArrayAdapter<String> adapter;
    ListView lv;
    ImageView ivBackButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation_app);
        setViews();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arr);
        lv.setAdapter(adapter);
        setBackButton();
    }

    public void setViews(){
        lv = (ListView) findViewById(R.id.lvInfo);
        ivBackButton = (ImageView) findViewById(R.id.ivBackButton);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

    public void setBackButton(){
        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }



}
