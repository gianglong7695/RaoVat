package poly.fall16.pro2051.group8.raovat.activities;

import android.graphics.Bitmap;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.HashMap;

import poly.fall16.pro2051.group8.raovat.R;
import poly.fall16.pro2051.group8.raovat.helper.SQLiteHandler;

public class ProfileActivity extends AppCompatActivity {
    private SQLiteHandler db;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    ProgressBar progressBar;
    ImageView ivAvatar;
    TextView tvMail, tvPhoneNumber, tvAddress, tvUserName, tvPassword;
    ToggleButton chkPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setViews();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        getSupportActionBar().setTitle(user.get(SQLiteHandler.KEY_FULLNAME));
        tvMail.setText(user.get(SQLiteHandler.KEY_EMAIL));
        tvUserName.setText(user.get(SQLiteHandler.KEY_NAME));
        tvPhoneNumber.setText(user.get(SQLiteHandler.KEY_PHONE));
        tvAddress.setText(user.get(SQLiteHandler.KEY_ADDRESS));

        chkPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    tvPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }else{
                    tvPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                }

            }
        });

        setUpImageLoader();
        imageLoader.displayImage(user.get(SQLiteHandler.KEY_AVATAR), ivAvatar, options, new ImageLoadingListener() {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.shrink_and_rotate_b, R.anim.shrink_and_rotate_a);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
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

    public void setViews(){
        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        tvMail = (TextView) findViewById(R.id.tvMail);
        tvPhoneNumber = (TextView) findViewById(R.id.tvPhoneNumber);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvPassword = (TextView) findViewById(R.id.tvPassword);
        chkPassword = (ToggleButton) findViewById(R.id.chkPassword);
    }


}
