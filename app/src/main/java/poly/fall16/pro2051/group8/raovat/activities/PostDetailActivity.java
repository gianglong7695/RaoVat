package poly.fall16.pro2051.group8.raovat.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import poly.fall16.pro2051.group8.raovat.R;
import poly.fall16.pro2051.group8.raovat.networks.MySingleton;
import poly.fall16.pro2051.group8.raovat.objects.ProductDetailObject;
import poly.fall16.pro2051.group8.raovat.utils.MyString;

import static poly.fall16.pro2051.group8.raovat.utils.MyString.handingPrice;

public class PostDetailActivity extends AppCompatActivity {
    SliderLayout sliderShow;
    String product_id = "";
    ImageView ivUserAvatar;
    TextView tvTitle, tvPrice, tvUserName, tvTime, tvContent;
    ImageView ivShowListImage;

    ImageLoader imageLoader;
    DisplayImageOptions options;
    ImageView ivBackButton;
    public static ProductDetailObject productDetailObject;
    private boolean isFavoriteChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setViews();
        setIvBackButton();

        product_id = getIntent().getExtras().getString("product_id");
        handlingNetwork(product_id);
        setImageLoader();

        ivShowListImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent it = new Intent(getApplicationContext(), ShowImageActivity.class);
            startActivity(it);
            }
        });



    }

    public void setViews() {
        sliderShow = (SliderLayout) findViewById(R.id.slider);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvContent = (TextView) findViewById(R.id.tvContent);
        ivUserAvatar = (ImageView) findViewById(R.id.ivAvatar);
        ivShowListImage = (ImageView) findViewById(R.id.ivShowListImage);
        ivBackButton = (ImageView) findViewById(R.id.ivBackButton);
    }

    public void setIvBackButton() {
        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem checkable = menu.findItem(R.id.action_favorite);
        checkable.setChecked(isFavoriteChecked);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                isFavoriteChecked = !item.isChecked();
                item.setChecked(isFavoriteChecked);
                if(isFavoriteChecked){
                    item.setIcon(getResources().getDrawable(R.drawable.ic_heart_50px_white_fill_padding25));
                }else{
                    item.setIcon(getResources().getDrawable(R.drawable.ic_heart_50px_white_padding25));
                }
                return true;
            default:
                return false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_acitivty_postdetail, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void handlingNetwork(String id) {
        // API handling network
        StringRequest requestProducts = new StringRequest(MyString.URL_GET_DETAIL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(response);

                    String str = jsonObject.getString("product");
                    productDetailObject = gson.fromJson(str, ProductDetailObject.class);
                    setData(productDetailObject);


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
        MySingleton.getInstance(this).addToRequestQueue(requestProducts);
    }


    public void setData(ProductDetailObject object) {
        for (int i = 0; i < object.img_url.size(); i++) {
            sliderShow.addSlider(new TextSliderView(getApplicationContext()).image(object.img_url.get(i)));
        }
        sliderShow.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderShow.setCustomAnimation(new DescriptionAnimation());
        sliderShow.setDuration(4000);
        if (object.img_url.size() == 1) {
            sliderShow.stopAutoCycle();
        }


        tvTime.setText(object.date);
        tvContent.setText(object.description);
        tvPrice.setText(handingPrice(object.price));
        tvUserName.setText(object.username);
        tvTitle.setText(object.name);

        imageLoader.displayImage(object.profile_url, ivUserAvatar, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                //holder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                //holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                //holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    public void setImageLoader() {
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
}
