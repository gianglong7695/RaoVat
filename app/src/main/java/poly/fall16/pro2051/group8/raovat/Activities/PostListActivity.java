package poly.fall16.pro2051.group8.raovat.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import poly.fall16.pro2051.group8.raovat.R;
import poly.fall16.pro2051.group8.raovat.adapters.PostAdapter;
import poly.fall16.pro2051.group8.raovat.objects.PostObject;
import poly.fall16.pro2051.group8.raovat.utils.RecyclerItemClickListener;
import poly.fall16.pro2051.group8.raovat.utils.SimpleDividerItemDecoration;

public class PostListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    RecyclerView rvPost;
    ArrayList<PostObject> alPost;
    SwipeRefreshLayout swipeRefreshLayout;
    Spinner spCity, spCategory;
    public static String [] arrCity = {"-- Khu vực --", "Hà nội", "Đà nẵng", "TP.HCM", "Vũng Tàu", "Vinh", "Sóc Trăng", "Kiên Giang", "Bình Định", "Cần Thơ", "Nha Trang", "Vĩnh Phúc", "Phú Thọ", "Huế", "Nam Định", "Hải Dương", "Hòa Bình"};
    public static String [] arrCategory = {"Xe cộ", "Đồ dùng cá nhân", "Khác"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        setViews();

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        alPost = new ArrayList<>();
        alPost.add(new PostObject("Nhà dương nội - Hà đông, 60m2 chính chủ", "2.500.000 đ/tháng", "23/9/2016", "http://3.bp.blogspot.com/-Ue1BNDfjTwE/UPFybkYOckI/AAAAAAAACB0/SfkwREI3KHM/s640/A-Beautiful-Path-Villaviciosa-Asturias-Spain-700x525.jpg"));
        alPost.add(new PostObject("Nhà dương nội - Hà đông, 60m2 chính chủ", "2.500.000 đ/tháng", "23/9/2016", "http://diendanso.net/wp-content/uploads/2015/12/anh-dep-17.jpg"));
        alPost.add(new PostObject("Nhà dương nội - Hà đông, 60m2 chính chủ", "2.500.000 đ/tháng", "23/9/2016", "http://3.bp.blogspot.com/-Ue1BNDfjTwE/UPFybkYOckI/AAAAAAAACB0/SfkwREI3KHM/s640/A-Beautiful-Path-Villaviciosa-Asturias-Spain-700x525.jpg"));
        alPost.add(new PostObject("Nhà dương nội - Hà đông, 60m2 chính chủ", "2.500.000 đ/tháng", "23/9/2016", "http://diendanso.net/wp-content/uploads/2015/12/anh-dep-17.jpg"));
        alPost.add(new PostObject("Nhà dương nội - Hà đông, 60m2 chính chủ", "2.500.000 đ/tháng", "23/9/2016", "http://3.bp.blogspot.com/-Ue1BNDfjTwE/UPFybkYOckI/AAAAAAAACB0/SfkwREI3KHM/s640/A-Beautiful-Path-Villaviciosa-Asturias-Spain-700x525.jpg"));
        alPost.add(new PostObject("Nhà dương nội - Hà đông, 60m2 chính chủ", "2.500.000 đ/tháng", "23/9/2016", "http://diendanso.net/wp-content/uploads/2015/12/anh-dep-17.jpg"));
        alPost.add(new PostObject("Nhà dương nội - Hà đông, 60m2 chính chủ", "2.500.000 đ/tháng", "23/9/2016", "http://3.bp.blogspot.com/-Ue1BNDfjTwE/UPFybkYOckI/AAAAAAAACB0/SfkwREI3KHM/s640/A-Beautiful-Path-Villaviciosa-Asturias-Spain-700x525.jpg"));
        alPost.add(new PostObject("Nhà dương nội - Hà đông, 60m2 chính chủ", "2.500.000 đ/tháng", "23/9/2016", "http://diendanso.net/wp-content/uploads/2015/12/anh-dep-17.jpg"));
        alPost.add(new PostObject("Nhà dương nội - Hà đông, 60m2 chính chủ", "2.500.000 đ/tháng", "23/9/2016", "http://3.bp.blogspot.com/-Ue1BNDfjTwE/UPFybkYOckI/AAAAAAAACB0/SfkwREI3KHM/s640/A-Beautiful-Path-Villaviciosa-Asturias-Spain-700x525.jpg"));
        alPost.add(new PostObject("Nhà dương nội - Hà đông, 60m2 chính chủ", "2.500.000 đ/tháng", "23/9/2016", "http://diendanso.net/wp-content/uploads/2015/12/anh-dep-17.jpg"));

        PostAdapter adapter = new PostAdapter(alPost);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvPost.setLayoutManager(mLayoutManager);
        rvPost.setItemAnimator(new DefaultItemAnimator());
        rvPost.setAdapter(adapter);
        rvPost.addItemDecoration(new SimpleDividerItemDecoration(this));

        rvPost.addOnItemTouchListener(new RecyclerItemClickListener(this, rvPost, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 0:
                        Intent it = new Intent(getApplicationContext(), PostDetailActivity.class);
                        startActivity(it);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        break;
                    default:
                        Toast.makeText(PostListActivity.this, "Đang xây dựng !!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        ArrayAdapter adapterCity = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrCity);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(adapterCity);

        ArrayAdapter adapterCategory = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrCategory);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapterCategory);


    }

    public void setViews(){
        rvPost = (RecyclerView) findViewById(R.id.rvItems);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        spCity = (Spinner) findViewById(R.id.spCity);
        spCategory = (Spinner) findViewById(R.id.spCategory);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "Đang làm mới ...", Toast.LENGTH_SHORT).show();

        CountDownTimer timer = new CountDownTimer(4000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(PostListActivity.this, "Hoàn thành!", Toast.LENGTH_SHORT).show();
            }
        };
        timer.start();
    }
}
