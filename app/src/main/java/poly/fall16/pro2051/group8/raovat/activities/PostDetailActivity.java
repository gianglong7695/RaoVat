package poly.fall16.pro2051.group8.raovat.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import poly.fall16.pro2051.group8.raovat.R;
import poly.fall16.pro2051.group8.raovat.adapters.ProposeAdapter;
import poly.fall16.pro2051.group8.raovat.networks.MySingleton;
import poly.fall16.pro2051.group8.raovat.objects.ProductDetailObject;
import poly.fall16.pro2051.group8.raovat.objects.ProductObject;
import poly.fall16.pro2051.group8.raovat.utils.MyString;
import poly.fall16.pro2051.group8.raovat.utils.RecyclerItemClickListener;

import static poly.fall16.pro2051.group8.raovat.utils.MyString.URL_ADD_FAVORITE_PART1;
import static poly.fall16.pro2051.group8.raovat.utils.MyString.URL_ADD_FAVORITE_PART2;
import static poly.fall16.pro2051.group8.raovat.utils.MyString.URL_REMOVE_FAVORITE_PART1;
import static poly.fall16.pro2051.group8.raovat.utils.MyString.handingPrice;

public class PostDetailActivity extends AppCompatActivity {
    SliderLayout sliderShow;
    RecyclerView rvPropose;
    String product_id = "";
    ImageView ivUserAvatar;
    TextView tvTitle, tvPrice, tvUserName, tvTime, tvContent, tvCategory, tvAddress, tvArea, tvQuality;
    ImageView ivShowListImage, ivNext, ivPrevious;

    ImageLoader imageLoader;
    DisplayImageOptions options;
    ImageView ivBackButton;
    ProgressBar progressBar;
    public static ProductDetailObject productDetailObject;
    private boolean isFavoriteChecked = false;

    ArrayList<ProductObject> alPropose;
    ProposeAdapter proposeAdapter;
    LinearLayout layoutShowAll;
    String msg_bookmark;
    LinearLayout content_main, layout_user_info;
    Menu mMenu;
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


        // Propose recyclerview
        alPropose = new ArrayList<>();
        proposeAdapter = new ProposeAdapter(alPropose);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rvPropose.setLayoutManager(mLayoutManager);
        rvPropose.setItemAnimator(new DefaultItemAnimator());
        rvPropose.setAdapter(proposeAdapter);
        getPropose();
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvPropose.smoothScrollBy(300, 0);
            }
        });

        ivPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rvPropose.smoothScrollBy(-300, 0);
            }
        });

        rvPropose.addOnItemTouchListener(new RecyclerItemClickListener(this, rvPropose, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent it = new Intent(getApplicationContext(), PostDetailActivity.class);
                it.putExtra("product_id", alPropose.get(position).pid);
                startActivity(it);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        layoutShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), PostListActivity.class);
                it.putExtra("position", 9);
                startActivity(it);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        layout_user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), UserInfomationActivity.class);
                it.putExtra("username", productDetailObject.username);
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
        tvCategory = (TextView) findViewById(R.id.tvCategory);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvQuality = (TextView) findViewById(R.id.tvQuality);
        tvArea = (TextView) findViewById(R.id.tvArea);
        ivUserAvatar = (ImageView) findViewById(R.id.ivAvatar);
        ivShowListImage = (ImageView) findViewById(R.id.ivShowListImage);
        ivBackButton = (ImageView) findViewById(R.id.ivBackButton);
        ivNext = (ImageView) findViewById(R.id.ivNext);
        ivPrevious = (ImageView) findViewById(R.id.ivPrevious);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rvPropose = (RecyclerView) findViewById(R.id.rvPropose);
        layoutShowAll = (LinearLayout) findViewById(R.id.layoutShowAll);
        content_main = (LinearLayout) findViewById(R.id.activity_post_detail);
        layout_user_info = (LinearLayout) findViewById(R.id.layout_user_info);
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
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                isFavoriteChecked = !item.isChecked();
                item.setChecked(isFavoriteChecked);
                if(isFavoriteChecked){
                    addFavorite();
                }else{
                    removeFavorite();
                }
                updateMenuIcon(item);
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

                    MenuItem itemFavorite = mMenu.findItem(R.id.action_favorite);
                    checkFavorite(itemFavorite);

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
        tvAddress.setText(object.address);
        tvQuality.setText(object.quality);
        tvCategory.setText(MyString.arrCategory[convertPositionCategory(object.category)]);


        imageLoader.displayImage(object.profile_url, ivUserAvatar, options, new ImageLoadingListener() {
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


    public int convertPositionCategory(String strItem) {
        int position = 0;
        if (strItem.equals(MyString.arrCategoryServer[1])) {
            position = 1;
        } else if (strItem.equals(MyString.arrCategoryServer[2])) {
            position = 2;
        } else if (strItem.equals(MyString.arrCategoryServer[3])) {
            position = 3;
        } else if (strItem.equals(MyString.arrCategoryServer[4])) {
            position = 4;
        } else if (strItem.equals(MyString.arrCategoryServer[5])) {
            position = 5;
        } else if (strItem.equals(MyString.arrCategoryServer[6])) {
            position = 6;
        } else if (strItem.equals(MyString.arrCategoryServer[7])) {
            position = 7;
        } else if (strItem.equals(MyString.arrCategoryServer[8])) {
            position = 8;
        }
        return position;
    }

    public void getPropose() {
        StringRequest requestProducts = new StringRequest(MyString.URL_GET_PROPOSE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("product");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String str = jsonArray.getString(i);

                        // Define Response class to correspond to the JSON response returned
                        ProductObject productObject = gson.fromJson(str, ProductObject.class);
                        alPropose.add(productObject);
                        proposeAdapter.notifyDataSetChanged();
                        //progressBar.setVisibility(View.GONE);
                    }

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

    public void checkFavorite(final MenuItem menu) {
        if (MainActivity.session.isLoggedIn()) {
            String URL = MyString.URL_CHECK_FAVORITE_PART1 + MainActivity.tvName.getText().toString() + MyString.URL_ADD_FAVORITE_PART2 + productDetailObject.pid;
            StringRequest requestBookmark = new StringRequest(URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(response);
                        String result = jsonObject.getString("check");

                        if(result.equals("true")){
                            isFavoriteChecked = true;
                            menu.setChecked(true);
                            updateMenuIcon(menu);

                        }

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
            MySingleton.getInstance(this).addToRequestQueue(requestBookmark);
        }
    }

    public void addFavorite() {
        if (MainActivity.session.isLoggedIn()) {
            String URL = URL_ADD_FAVORITE_PART1 + MainActivity.tvName.getText().toString() + URL_ADD_FAVORITE_PART2 + productDetailObject.pid;
            StringRequest requestFavorite = new StringRequest(URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(response);
                        String result = jsonObject.getString("error");

                        if (result.equals("true")) {
                            msg_bookmark = "Không thể thêm ưa thích!";
                            Snackbar.make(content_main, msg_bookmark, Snackbar.LENGTH_LONG).show();
                        } else {
                            msg_bookmark = "Ưa thích đã được thêm thành công!";
                            Snackbar.make(content_main, msg_bookmark, Snackbar.LENGTH_LONG).setAction("Hoàn tác", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(PostDetailActivity.this, "Đã hoàn tác!", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                        }

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
            MySingleton.getInstance(this).addToRequestQueue(requestFavorite);

        }

    }

    public void removeFavorite() {
        if (MainActivity.session.isLoggedIn()) {
            String URL = URL_REMOVE_FAVORITE_PART1 + MainActivity.tvName.getText().toString() + URL_ADD_FAVORITE_PART2 + productDetailObject.pid;
            StringRequest requestFavorite = new StringRequest(URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(response);
                        String result = jsonObject.getString("error");

                        if (result.equals("true")) {
                            msg_bookmark = "Có lỗi xảy ra";
                            Snackbar.make(content_main, msg_bookmark, Snackbar.LENGTH_LONG).show();
                        } else {
                            msg_bookmark = "Đã xóa ưu thích!";
                            Snackbar.make(content_main, msg_bookmark, Snackbar.LENGTH_LONG).setAction("Hoàn tác", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(PostDetailActivity.this, "Đã hoàn tác!", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                        }

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
            MySingleton.getInstance(this).addToRequestQueue(requestFavorite);

        }

    }

    public void updateMenuIcon(MenuItem item){
        if (isFavoriteChecked) {
            item.setIcon(getResources().getDrawable(R.drawable.ic_heart_50px_white_fill_padding25));
        } else {
            item.setIcon(getResources().getDrawable(R.drawable.ic_heart_50px_white_padding25));
        }
    }
}
