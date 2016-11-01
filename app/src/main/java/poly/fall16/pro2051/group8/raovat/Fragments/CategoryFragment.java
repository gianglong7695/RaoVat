package poly.fall16.pro2051.group8.raovat.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import poly.fall16.pro2051.group8.raovat.activities.MainActivity;
import poly.fall16.pro2051.group8.raovat.activities.PostDetailActivity;
import poly.fall16.pro2051.group8.raovat.adapters.PostAdapter;
import poly.fall16.pro2051.group8.raovat.networks.MySingleton;
import poly.fall16.pro2051.group8.raovat.objects.PostObject;
import poly.fall16.pro2051.group8.raovat.objects.ProductObject;
import poly.fall16.pro2051.group8.raovat.utils.MyString;
import poly.fall16.pro2051.group8.raovat.utils.RecyclerItemClickListener;
import poly.fall16.pro2051.group8.raovat.utils.SimpleDividerItemDecoration;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    RecyclerView rvNews;
    View v;
    ArrayList<PostObject> alNews;
    PostAdapter adapter;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_category, container, false);
        setViews();

        if(MainActivity.alProduct.size() == 0){
            getNews();
            addNews();
        }else{
            progressBar.setVisibility(View.GONE);
        }

        adapter = new PostAdapter(alNews);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvNews.setLayoutManager(mLayoutManager);
        rvNews.setItemAnimator(new DefaultItemAnimator());
        rvNews.setAdapter(adapter);
        rvNews.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));


        rvNews.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvNews, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent it = new Intent(getActivity(), PostDetailActivity.class);
                it.putExtra("product_id",MainActivity.alProduct.get(position).pid);
                startActivity(it);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));



        return v;
    }

    private void setViews(){
        rvNews = (RecyclerView) v.findViewById(R.id.rvNews);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
    }
    public void addNews(){
        alNews = new ArrayList<>();
        alNews = MainActivity.alPost;
    }

    public void getNews(){
        StringRequest requestProducts = new StringRequest(MyString.URL_GET_NEWS, new Response.Listener<String>() {
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
                        MainActivity.alProduct.add(productObject);
                        MainActivity.alPost.add(new PostObject(productObject.name, productObject.price, productObject.date, productObject.image_url));
                        adapter.notifyDataSetChanged();
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
        MySingleton.getInstance(getActivity()).addToRequestQueue(requestProducts);
    }

    @Override
    public void onResume() {
        autoCheckNews(MyString.TIME_CALLBACK_GETNEWS, 1);
        super.onResume();
    }

    public void updateNews(){
        MainActivity.alProduct.clear();
        MainActivity.alPost.clear();
        getNews();

    }

    public void autoCheckNews(int totalTime, int time){
        CountDownTimer timer = new CountDownTimer(totalTime, time) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                StringRequest requestLastItem = new StringRequest(MyString.URL_GET_LAST_ITEM, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Gson gson = new Gson();
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("product");
                            JSONObject object = jsonArray.getJSONObject(0);
                            String id = object.getString("pid");

                            if(MainActivity.alProduct.size() > 0){
                                if(!MainActivity.alProduct.get(0).pid.equals(id)){
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
                MySingleton.getInstance(getActivity()).addToRequestQueue(requestLastItem);
                autoCheckNews(MyString.TIME_CALLBACK_GETNEWS, 1);
            }
        };

        timer.start();
    }
}
