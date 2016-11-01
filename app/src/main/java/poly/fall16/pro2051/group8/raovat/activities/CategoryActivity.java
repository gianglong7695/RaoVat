package poly.fall16.pro2051.group8.raovat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

import poly.fall16.pro2051.group8.raovat.R;
import poly.fall16.pro2051.group8.raovat.adapters.CategoryAdapter;
import poly.fall16.pro2051.group8.raovat.objects.CategoryObject;
import poly.fall16.pro2051.group8.raovat.utils.MyString;

public class CategoryActivity extends AppCompatActivity {
    ImageView ivBackButton;
    GridView gvCategory;
    CategoryAdapter categoryAdapter;

    public static ArrayList<CategoryObject> alCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setViews();
        setBackButton();

        // Category gridview
        addCategoryItems();
        categoryAdapter = new CategoryAdapter(getApplicationContext(), alCategory);
        gvCategory.setAdapter(categoryAdapter);
        gvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(getApplicationContext(), PostListActivity.class);
                it.putExtra("position", i);
                startActivity(it);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });


    }


    public void setViews(){
        ivBackButton = (ImageView) findViewById(R.id.ivBackButton);
        gvCategory = (GridView) findViewById(R.id.gvCategory);
    }

    public void addCategoryItems(){
        alCategory = new ArrayList<>();
        alCategory.add(new CategoryObject(R.drawable.ic_thoitrangphukien, MyString.arrCategory[0]));
        alCategory.add(new CategoryObject(R.drawable.ic_noingoaithat, MyString.arrCategory[1]));
        alCategory.add(new CategoryObject(R.drawable.ic_dienmaygiadung, MyString.arrCategory[2]));
        alCategory.add(new CategoryObject(R.drawable.ic_mevabe, MyString.arrCategory[3]));
        alCategory.add(new CategoryObject(R.drawable.ic_dienthoaimaytinhbang, MyString.arrCategory[4]));
        alCategory.add(new CategoryObject(R.drawable.ic_dichvugiaitri, MyString.arrCategory[5]));
        alCategory.add(new CategoryObject(R.drawable.ic_dichoonline, MyString.arrCategory[6]));
        alCategory.add(new CategoryObject(R.drawable.ic_otoxemay, MyString.arrCategory[7]));
        alCategory.add(new CategoryObject(R.drawable.ic_nhadat, MyString.arrCategory[8]));
        alCategory.add(new CategoryObject(R.drawable.ic_tonghop, "Tổng hợp"));
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
