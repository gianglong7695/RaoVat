package poly.fall16.pro2051.group8.raovat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import poly.fall16.pro2051.group8.raovat.R;
import poly.fall16.pro2051.group8.raovat.adapters.PostAdapter;
import poly.fall16.pro2051.group8.raovat.networks.MySingleton;
import poly.fall16.pro2051.group8.raovat.objects.PostObject;
import poly.fall16.pro2051.group8.raovat.objects.ProductObject;
import poly.fall16.pro2051.group8.raovat.utils.MyString;
import poly.fall16.pro2051.group8.raovat.utils.RecyclerItemClickListener;
import poly.fall16.pro2051.group8.raovat.utils.SimpleDividerItemDecoration;

public class MyPostsActivity extends AppCompatActivity {
    RecyclerView rvMyPost;
    ImageView ivBackButton;
    ArrayList<PostObject> alProduct;
    PostAdapter postAdapter;
    ProgressBar progressBar;
    ArrayList<ProductObject> alOriginData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);
        setViews();
        setBackButton();

        alOriginData = new ArrayList<>();
        alProduct = new ArrayList<>();
        postAdapter = new PostAdapter(alProduct);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvMyPost.setLayoutManager(mLayoutManager);
        rvMyPost.setItemAnimator(new DefaultItemAnimator());
        rvMyPost.setAdapter(postAdapter);
        rvMyPost.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));

        rvMyPost.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rvMyPost, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent it = new Intent(getApplicationContext(), PostDetailActivity.class);
                it.putExtra("product_id",alOriginData.get(position).pid);
                startActivity(it);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        getNews();
    }

    public void setViews() {
        rvMyPost = (RecyclerView) findViewById(R.id.rvMyPost);
        ivBackButton = (ImageView) findViewById(R.id.ivBackButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

    public void setBackButton() {
        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void getNews() {
        StringRequest requestProducts = new StringRequest(MyString.URL_MY_POSTS + MainActivity.tvName.getText().toString(), new Response.Listener<String>() {
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
                        alOriginData.add(productObject);
                        alProduct.add(new PostObject(productObject.name, productObject.price, productObject.date, productObject.image_url));
                        postAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
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
}
