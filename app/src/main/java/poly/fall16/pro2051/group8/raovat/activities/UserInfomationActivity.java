package poly.fall16.pro2051.group8.raovat.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import poly.fall16.pro2051.group8.raovat.R;
import poly.fall16.pro2051.group8.raovat.networks.MySingleton;
import poly.fall16.pro2051.group8.raovat.objects.ProductUserObject;
import poly.fall16.pro2051.group8.raovat.objects.UserProfileObject;
import poly.fall16.pro2051.group8.raovat.utils.MyString;

public class UserInfomationActivity extends AppCompatActivity {
    ImageView ivAvatar, background_img;
    TextView tvUserName, tvAddress, tvPhone, tvFullName, tvEmail, ivCall;
    String username;
    UserProfileObject userProfileObject;
    ArrayList<ProductUserObject> alUserProduct;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    ProgressBar progressBar;

    int[] bg_profile = {R.drawable.bg_profile1, R.drawable.bg_profile2, R.drawable.bg_profile3, R.drawable.bg_profile4, R.drawable.bg_profile5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infomation);
        setViews();
        setUpImageLoader();

        int resBg = getRandomNumber();
        // Set background
        background_img.setImageResource(bg_profile[resBg]);
        username = getIntent().getExtras().getString("username");
        alUserProduct = new ArrayList<>();
        getInfomation(username);


        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + userProfileObject.phone));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }
        });

    }


    public void getInfomation(String username) {
        StringRequest requestInfo = new StringRequest(MyString.URL_USER_INFO + username, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(response);
                    String str = jsonObject.getString("user");

                    userProfileObject = gson.fromJson(str, UserProfileObject.class);

                    JSONArray jsonArray = jsonObject.getJSONArray("product");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String strProduct = jsonArray.getString(i);

                        // Define Response class to correspond to the JSON response returned
                        ProductUserObject productObject = gson.fromJson(strProduct, ProductUserObject.class);
                        alUserProduct.add(productObject);
                    }

                    setData();

                } catch (JSONException e) {
                    Log.e("JSONException", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(requestInfo);
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


    public void setViews() {
        ivAvatar = (ImageView) findViewById(R.id.user_avatar);
        tvUserName = (TextView) findViewById(R.id.user_name);
        tvAddress = (TextView) findViewById(R.id.user_post);
        tvPhone = (TextView) findViewById(R.id.user_book);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvFullName = (TextView) findViewById(R.id.user_fullname);
        tvEmail = (TextView) findViewById(R.id.user_email);
        background_img = (ImageView) findViewById(R.id.background_img);
        ivCall = (TextView) findViewById(R.id.attention_user);
    }

    public void setData(){
        imageLoader.displayImage(userProfileObject.profile_url, ivAvatar, options, new ImageLoadingListener() {
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
        tvUserName.setText(userProfileObject.username);
        tvAddress.setText(userProfileObject.address);
        tvPhone.setText(userProfileObject.phone);
        tvFullName.setText("Họ và tên : " + userProfileObject.fullName);
        tvEmail.setText("Email : " + userProfileObject.email);
    }

    public int getRandomNumber(){
        Random rand = new Random();
        return rand.nextInt(5);
    }
}
