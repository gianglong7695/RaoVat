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
import poly.fall16.pro2051.group8.raovat.adapters.FavoriteAdapter;
import poly.fall16.pro2051.group8.raovat.networks.MySingleton;
import poly.fall16.pro2051.group8.raovat.objects.FavoriteObject;
import poly.fall16.pro2051.group8.raovat.utils.MyString;
import poly.fall16.pro2051.group8.raovat.utils.RecyclerItemClickListener;

public class FavoriteActivity extends AppCompatActivity {
    RecyclerView rvFavorite;
    ImageView ivBackButton;
    StringRequest requestFavorite;
    FavoriteAdapter favoriteAdapter;
    ArrayList<FavoriteObject> alFavorite;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        setViews();
        setBackButton();

        alFavorite = new ArrayList<>();
        favoriteAdapter = new FavoriteAdapter(alFavorite);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvFavorite.setLayoutManager(mLayoutManager);
        rvFavorite.setItemAnimator(new DefaultItemAnimator());
        rvFavorite.setAdapter(favoriteAdapter);

        rvFavorite.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rvFavorite, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent it = new Intent(getApplicationContext(), PostDetailActivity.class);
                it.putExtra("product_id",alFavorite.get(position).pid);
                startActivity(it);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        getDataFromServer();

    }


    public void setViews(){
        rvFavorite = (RecyclerView) findViewById(R.id.rvFavorite);
        ivBackButton = (ImageView) findViewById(R.id.ivBackButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void getDataFromServer() {
        requestFavorite = new StringRequest(MyString.URL_GET_FAVORITE_LIST + MainActivity.tvName.getText().toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("favorite");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String str = jsonArray.getString(i);

                        // Define Response class to correspond to the JSON response returned
                        FavoriteObject favoriteObject = gson.fromJson(str, FavoriteObject.class);
                        alFavorite.add(favoriteObject);
                    }

                    favoriteAdapter.notifyDataSetChanged();
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

        MySingleton.getInstance(this).addToRequestQueue(requestFavorite);
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
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }
}
