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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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
import poly.fall16.pro2051.group8.raovat.fragments.CategoryFragment;
import poly.fall16.pro2051.group8.raovat.networks.MySingleton;
import poly.fall16.pro2051.group8.raovat.objects.CategoryObject;
import poly.fall16.pro2051.group8.raovat.objects.CityObject;
import poly.fall16.pro2051.group8.raovat.objects.PostObject;
import poly.fall16.pro2051.group8.raovat.objects.ProductObject;
import poly.fall16.pro2051.group8.raovat.utils.MyString;
import poly.fall16.pro2051.group8.raovat.utils.RecyclerItemClickListener;
import poly.fall16.pro2051.group8.raovat.utils.SimpleDividerItemDecoration;

public class PostListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    RecyclerView rvPost;
    ArrayList<PostObject> alPost;
    SwipeRefreshLayout swipeRefreshLayout;
    Spinner spCity, spCategory;
    ArrayList arrCity;
    ArrayList arrCategoryTitle;
    ArrayList<ProductObject> alProduct;

    ArrayAdapter adapterCity;
    PostAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setViews();

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        alPost = new ArrayList<>();

        adapter = new PostAdapter(alPost);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvPost.setLayoutManager(mLayoutManager);
        rvPost.setItemAnimator(new DefaultItemAnimator());
        rvPost.setAdapter(adapter);
        rvPost.addItemDecoration(new SimpleDividerItemDecoration(this));

        rvPost.addOnItemTouchListener(new RecyclerItemClickListener(this, rvPost, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent it = new Intent(PostListActivity.this, PostDetailActivity.class);
                it.putExtra("product_id",alProduct.get(position).pid);
                startActivity(it);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));



        arrCategoryTitle = getCategoryTitle();
        ArrayAdapter adapterCategory = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrCategoryTitle);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapterCategory);

        arrCity = new ArrayList();
        adapterCity = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, arrCity);
        spCity.setAdapter(adapterCity);



        // API handling network
        StringRequest request = new StringRequest(MyString.URL_CITY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++){
                        String str = jsonArray.getString(i);

                        // Define Response class to correspond to the JSON response returned
                        CityObject city = gson.fromJson(str, CityObject.class);
                        arrCity.add(city.name);
                    }

                    adapterCity.notifyDataSetChanged();



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

        alProduct = new ArrayList<>();
        StringRequest requestProducts = new StringRequest(MyString.URL_PRODUCTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("product");
                    for (int i = 0; i < jsonArray.length(); i++){
                        String str = jsonArray.getString(i);

                        // Define Response class to correspond to the JSON response returned
                        ProductObject productObject = gson.fromJson(str, ProductObject.class);
                        alProduct.add(productObject);
                        alPost.add(new PostObject(productObject.name, productObject.price, productObject.date, productObject.image_url));
                    }

                    adapter.notifyDataSetChanged();


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
        MySingleton.getInstance(this).addToRequestQueue(request);
        MySingleton.getInstance(this).addToRequestQueue(requestProducts);


    }

    public void setViews(){
        rvPost = (RecyclerView) findViewById(R.id.rvItems);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        spCity = (Spinner) findViewById(R.id.spCity);
        spCategory = (Spinner) findViewById(R.id.spCategory);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.post_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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

    public ArrayList getCategoryTitle(){
        ArrayList arrCategory = new ArrayList();
        ArrayList<CategoryObject> list = CategoryFragment.list;
        for (int i = 0; i < list.size(); i++){
            arrCategory.add(list.get(i).getTitle());
        }

        return  arrCategory;
    }
}
