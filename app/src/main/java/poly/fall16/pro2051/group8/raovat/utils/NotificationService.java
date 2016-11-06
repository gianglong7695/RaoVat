package poly.fall16.pro2051.group8.raovat.utils;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import poly.fall16.pro2051.group8.raovat.activities.MainActivity;
import poly.fall16.pro2051.group8.raovat.networks.MySingleton;

/**
 * Created by Giang Long on 11/4/2016.
 */

public class NotificationService extends Service {
    StringRequest requestLastItem;
    CountDownTimer timer;
    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        requestLastItem = new StringRequest(MyString.URL_GET_LAST_ITEM, new Response.Listener<String>() {
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
                            //updateNews();
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(requestLastItem);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void autoCheckNews(int totalTime, int time){
        timer = new CountDownTimer(totalTime, time) {
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
                                    //updateNews();
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
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(requestLastItem);
                autoCheckNews(MyString.TIME_CALLBACK_GETNEWS, 1);

            }
        };

        timer.start();
    }
}
