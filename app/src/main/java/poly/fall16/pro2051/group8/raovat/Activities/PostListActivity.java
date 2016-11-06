package poly.fall16.pro2051.group8.raovat.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
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
import poly.fall16.pro2051.group8.raovat.networks.MySingleton;
import poly.fall16.pro2051.group8.raovat.objects.BookmarkObject;
import poly.fall16.pro2051.group8.raovat.objects.CityObject;
import poly.fall16.pro2051.group8.raovat.objects.PostObject;
import poly.fall16.pro2051.group8.raovat.objects.ProductObject;
import poly.fall16.pro2051.group8.raovat.utils.MyString;
import poly.fall16.pro2051.group8.raovat.utils.RecyclerItemClickListener;
import poly.fall16.pro2051.group8.raovat.utils.SimpleDividerItemDecoration;

import static poly.fall16.pro2051.group8.raovat.utils.MyString.URL_ADD_BOOKMARK_PART1;
import static poly.fall16.pro2051.group8.raovat.utils.MyString.URL_ADD_BOOKMARK_PART2;
import static poly.fall16.pro2051.group8.raovat.utils.MyString.URL_REMOVE_BOOKMARK_PART1;

public class PostListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {
    ImageView ivBackButton;
    Button btFilter;
    ProgressBar progressBar;
    RecyclerView rvPost;
    ArrayList<PostObject> alPost;
    SwipeRefreshLayout swipeRefreshLayout;
    Spinner spCity, spCategory;
    ArrayList arrCity;
    ArrayList arrCategoryTitle;
    ArrayList<ProductObject> alProduct;

    ArrayAdapter adapterCity;
    PostAdapter adapter;
    TextView tvLable;
    String URL;
    SearchView searchView;
    boolean isBookMark = false;
    int categoryChoice;
    StringRequest requestProducts;
    int position;

    ArrayList<BookmarkObject> alBookmark;
    LinearLayout content_main;
    String msg_bookmark = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setViews();
        setBackButton();
        position = Integer.parseInt(getIntent().getExtras().get("position").toString());

        URL = getURL(position);
        if (position == 9) {
            tvLable.setText("Tổng hợp");
        } else {
            tvLable.setText(MyString.arrCategory[position]);
        }

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        alPost = new ArrayList<>();
        alBookmark = new ArrayList<>();

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
                it.putExtra("product_id", alProduct.get(position).pid);
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
        if (position != 9) {
            spCategory.setSelection(position);

        }

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryChoice = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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
                    for (int i = 0; i < jsonArray.length(); i++) {
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


        MySingleton.getInstance(this).addToRequestQueue(request);
        getDataFromServer();
        filter();

    }

    public void setViews() {
        rvPost = (RecyclerView) findViewById(R.id.rvItems);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        spCity = (Spinner) findViewById(R.id.spCity);
        spCategory = (Spinner) findViewById(R.id.spCategory);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ivBackButton = (ImageView) findViewById(R.id.ivBackButton);
        tvLable = (TextView) findViewById(R.id.tvLable);
        btFilter = (Button) findViewById(R.id.btFilter);
        content_main = (LinearLayout) findViewById(R.id.activity_login);
    }

    public void setBackButton() {
        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem checkable = menu.findItem(R.id.action_bookmark);
        checkBookmark(checkable);
        if(position == 9){
            checkable.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bookmark:
                isBookMark = !item.isChecked();
                item.setChecked(isBookMark);
                if(isBookMark){
                    addBookmark();
                }else{
                    removeBookmark();
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
        getMenuInflater().inflate(R.menu.activity_post_list, menu);
        MenuItem itemSearch = menu.findItem(R.id.action_search);
        searchView = (SearchView) itemSearch.getActionView();
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setHint("Nhập để tìm kiếm ...");
        searchEditText.setTextColor(getResources().getColor(R.color.teal));
        searchEditText.setBackgroundColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.teal));
        //set OnQueryTextListener cho search view để thực hiện search theo text
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "Đang làm mới ...", Toast.LENGTH_SHORT).show();
        checkNew();
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

    public ArrayList getCategoryTitle() {
        ArrayList arrCategory = new ArrayList();
        String[] arr = MyString.arrCategory;
        for (int i = 0; i < arr.length; i++) {
            arrCategory.add(arr[i]);
        }

        return arrCategory;
    }

    public String getURL(int position) {
        if (position == MyString.arrCategory.length) {
            URL = MyString.URL_PRODUCTS;
        } else {
            URL = MyString.URL_GET_CATEGORY + MyString.arrCategoryServer[position];
        }
        return URL;

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

    public void filter() {
        btFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL = getURL(categoryChoice);
                alPost.clear();
                getDataFromServer();
            }
        });
    }

    public void getDataFromServer() {
        alProduct = new ArrayList<>();
        requestProducts = new StringRequest(URL, new Response.Listener<String>() {
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
                        alProduct.add(productObject);
                        alPost.add(new PostObject(productObject.name, productObject.price, productObject.date, productObject.image_url));
                    }

                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);


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


    public void checkNew() {
        String URL_CHECK;
        if (position != 9) {
            URL_CHECK = MyString.URL_GET_LAST_ITEM_CATEGORY + MyString.arrCategoryServer[position];
        } else {
            URL_CHECK = MyString.URL_GET_LAST_ITEM;
        }
        StringRequest requestLastItem = new StringRequest(URL_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("product");
                    JSONObject object = jsonArray.getJSONObject(0);
                    String id = object.getString("pid");

                    if (alProduct.size() > 0) {
                        if (!alProduct.get(0).pid.equals(id)) {
                            updateNews();
                        }
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
        MySingleton.getInstance(this).addToRequestQueue(requestLastItem);
    }


    public void updateNews() {
        alProduct.clear();
        alPost.clear();
        getDataFromServer();
    }

    public void checkBookmark(final MenuItem menu) {
        if (MainActivity.session.isLoggedIn()) {
            StringRequest requestBookmark = new StringRequest(MyString.URL_GET_BOOKMARK + MainActivity.tvName.getText().toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("bookmark");
                        for (int i = 0; i < jsonArray.length(); i++){
                            String str = jsonArray.getString(i);
                            BookmarkObject bookmarkObject = gson.fromJson(str, BookmarkObject.class);
                            alBookmark.add(bookmarkObject);
                        }

                        for (int i = 0; i < alBookmark.size(); i++){
                            if(position != 9) {
                                if (MyString.arrCategoryServer[position].equals(alBookmark.get(i).category)) {
                                    isBookMark = true;
                                    menu.setChecked(isBookMark);
                                    updateMenuIcon(menu);
                                }
                            }
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




    public void addBookmark() {
        if (MainActivity.session.isLoggedIn()) {
            if (position != 9) {
                String URL = URL_ADD_BOOKMARK_PART1 + MainActivity.tvName.getText().toString() + URL_ADD_BOOKMARK_PART2 + MyString.arrCategoryServer[position];
                Log.d("DG", URL);
                StringRequest requestBookmark = new StringRequest(URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Gson gson = new Gson();
                            JSONObject jsonObject = new JSONObject(response);
                            String id = jsonObject.getString("error");

                            if(id.equals("true")){
                                msg_bookmark = "Không thể thêm bookmark!";
                                Snackbar.make(content_main, msg_bookmark , Snackbar.LENGTH_LONG).show();
                            }else{
                                msg_bookmark = "Bạn đã đăng ký nhận thông báo thành công!";
                                Snackbar.make(content_main, msg_bookmark , Snackbar.LENGTH_LONG).setAction("Hoàn tác", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(PostListActivity.this, "Đã hoàn tác!", Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
                            }

                        } catch (JSONException e) {
                            Log.e("JSONException", e.toString());
                        }

                        Log.d("DG", response.toString());

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

    }

    public void removeBookmark() {
        if (MainActivity.session.isLoggedIn()) {
            if (position != 9) {
                String URL = URL_REMOVE_BOOKMARK_PART1 + MainActivity.tvName.getText().toString() + URL_ADD_BOOKMARK_PART2 + MyString.arrCategoryServer[position];
                Log.d("DG", URL);
                StringRequest requestBookmark = new StringRequest(URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Gson gson = new Gson();
                            JSONObject jsonObject = new JSONObject(response);
                            String id = jsonObject.getString("error");

                            if(id.equals("true")){
                                msg_bookmark = "Không thể xóa bookmark!";
                                Snackbar.make(content_main, msg_bookmark , Snackbar.LENGTH_LONG).show();
                            }else{
                                msg_bookmark = "Bạn đã hủy nhận thông báo!";
                                Snackbar.make(content_main, msg_bookmark , Snackbar.LENGTH_LONG).setAction("Hoàn tác", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(PostListActivity.this, "Đã hoàn tác!", Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
                            }


                        } catch (JSONException e) {
                            Log.e("JSONException", e.toString());
                        }

                        Log.d("DG", response.toString());

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

    }

    public void updateMenuIcon(MenuItem item){
        if (isBookMark) {
            item.setIcon(getResources().getDrawable(R.drawable.ic_bookmark_50px_white_fill_padding25));
        } else {
            item.setIcon(getResources().getDrawable(R.drawable.ic_bookmark_50px_white_padding25));
        }
    }
}
