package poly.fall16.pro2051.group8.raovat.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import poly.fall16.pro2051.group8.raovat.R;

public class PushingPost extends AppCompatActivity {
    MaterialBetterSpinner spCategory;
    String [] categoryList = {"Xe cộ", "Bất động sản", "Đồ điện tử", "Thời trang, đồ dùng cá nhân", "..."};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushing_post);
        setViews();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, categoryList);

        spCategory.setAdapter(adapter);


    }

    public void setViews(){
        spCategory = (MaterialBetterSpinner) findViewById(R.id.spCategory);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

}
