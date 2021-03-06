package poly.fall16.pro2051.group8.raovat.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

import poly.fall16.pro2051.group8.raovat.R;
import poly.fall16.pro2051.group8.raovat.helper.SQLiteHandler;
import poly.fall16.pro2051.group8.raovat.helper.SessionManager;
import poly.fall16.pro2051.group8.raovat.objects.PostObject;
import poly.fall16.pro2051.group8.raovat.objects.ProductObject;
import poly.fall16.pro2051.group8.raovat.slidingtabs.SlidingTabLayout;
import poly.fall16.pro2051.group8.raovat.slidingtabs.TabAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener{
    SlidingTabLayout mSlidingTabLayout;
    ViewPager mViewPager;
    private boolean isCloseApp = false;
    private SQLiteHandler db;
    public static SessionManager session;
    NavigationView navigationView;
    View headerLayout;
    SearchView searchView;

    public static TextView tvName, tvMail;
    ImageView ivAvatar;

    ImageLoader imageLoader;
    DisplayImageOptions options;
    ProgressBar progressBar;
    LinearLayout content_main;

    public static ArrayList<ProductObject> alProduct;
    public static ArrayList<PostObject> alPost;
    public static String msg = "Chào mừng trở lại!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setViews();

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session.isLoggedIn()){
                    Intent it = new Intent(getApplicationContext(), PushingPostActivity.class);
                    startActivity(it);
                    overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                }else{
                    Toast.makeText(MainActivity.this, "Bạn phải đăng nhập mới có thể dùng chức năng này!", Toast.LENGTH_SHORT).show();
                }

            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerLayout = navigationView.getHeaderView(0);

        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(it);
                overridePendingTransition(R.anim.shrink_and_rotate_b, R.anim.shrink_and_rotate_a);
            }
        });

        if(session.isLoggedIn()){
            db = new SQLiteHandler(getApplicationContext());
            HashMap<String, String> user = db.getUserDetails();
            tvName = (TextView) headerLayout.findViewById(R.id.tvName);
            tvMail = (TextView) headerLayout.findViewById(R.id.tvMail);
            ivAvatar = (ImageView) headerLayout.findViewById(R.id.ivAvatar);
            progressBar = (ProgressBar) headerLayout.findViewById(R.id.progressBar);
            tvName.setText(user.get(SQLiteHandler.KEY_NAME));
            tvMail.setText(user.get(SQLiteHandler.KEY_EMAIL));
            setUpImageLoader();
            imageLoader.displayImage(user.get("profile_url"), ivAvatar, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                   progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }
        navigationView.setNavigationItemSelectedListener(this);


        // SlidingTabs
        mViewPager = (ViewPager) findViewById(R.id.vp_tab);
        mViewPager.setAdapter(new TabAdapter(getSupportFragmentManager(), this));
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tab);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.white));
        mSlidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.tv_tab);
        mSlidingTabLayout.setViewPager(mViewPager);

        Snackbar.make(content_main, msg , Snackbar.LENGTH_LONG).setAction("Action", null).show();
        setOther();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(!isCloseApp){
                Toast.makeText(this, "Ấn back 1 lần nữa để thoát", Toast.LENGTH_SHORT).show();
                isCloseApp = true;
                CountDownTimer timer = new CountDownTimer(3000, 1) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        isCloseApp = false;

                    }
                };
                timer.start();
            }else{
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem itemSearch = menu.findItem(R.id.action_search);
        searchView = (SearchView) itemSearch.getActionView();
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.teal));
        searchEditText.setBackgroundColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.teal));
        //set OnQueryTextListener cho search view để thực hiện search theo text
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_category:
            {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
                break;
            case R.id.nav_history:

                break;
            case R.id.nav_favorite:
            {
                if(session.isLoggedIn()){
                    Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                }else{
                    Toast.makeText(this, "Bạn phải đăng nhập mới có thể dùng chức năng này!", Toast.LENGTH_SHORT).show();
                }

            }
                break;
            case R.id.nav_my_post:
            {
                if(session.isLoggedIn()){
                    Intent intent = new Intent(MainActivity.this, MyPostsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                }else{
                    Toast.makeText(this, "Bạn phải đăng nhập mới có thể dùng chức năng này!", Toast.LENGTH_SHORT).show();
                }

            }
                break;
            // Other options
            case R.id.nav_share:

                break;
            case R.id.nav_help:

                break;
            case R.id.nav_setting:

                break;
            case R.id.nav_info_app:
            {
                Intent intent = new Intent(MainActivity.this, InfomationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
                break;
            case R.id.nav_logout:
                session.setLogin(false);

                db.deleteUsers();

                // Launching the login activity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setViews(){
        content_main = (LinearLayout) findViewById(R.id.content_main);
    }

    public void setUpImageLoader(){
        // ImageLoader
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.photo_default)
                .showImageOnFail(R.drawable.ic_image_fail_to_loading)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(this, "Chức năng đang được xây dựng!", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void setOther(){
        alProduct = new ArrayList<>();
        alPost = new ArrayList<>();
    }
}
