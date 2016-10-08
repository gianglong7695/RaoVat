package poly.fall16.pro2051.group8.raovat.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import poly.fall16.pro2051.group8.raovat.R;
import poly.fall16.pro2051.group8.raovat.networks.MySingleton;
import poly.fall16.pro2051.group8.raovat.objects.CityObject;
import poly.fall16.pro2051.group8.raovat.utils.MyString;

public class PushingPostActivity extends AppCompatActivity {
    MaterialBetterSpinner spCategory, spArea, spStatus;
    MaterialEditText etPrice;
    String [] categoryList = {"Xe cộ", "Bất động sản", "Đồ điện tử", "Thời trang, đồ dùng cá nhân", "..."};
    String [] arrStatus = {"Mới", "99%", "90%", "80%", "75%", "Cũ"};
    ArrayList arrCity;
    ArrayList<CityObject> alCity;
    ArrayAdapter<String> adapterCity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushing_post);
        setViews();

        alCity = new ArrayList<>();
        arrCity = new ArrayList();

        // Handling Spinner
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, categoryList);

        spCategory.setAdapter(adapterCategory);

        adapterCity = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, arrCity);
        spArea.setAdapter(adapterCity);

        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, arrStatus);

        spStatus.setAdapter(adapterStatus);

        // Editext
//        etPrice.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                Toast.makeText(PushingPostActivity.this, "Edited !", Toast.LENGTH_SHORT).show();
//            }
//        });




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
                        alCity.add(city);
                    }


                    for (int j = 0; j < alCity.size(); j++){
                        arrCity.add(alCity.get(j).name);
                    }
                    adapterCity.notifyDataSetChanged();



                } catch (JSONException e) {
                    Log.e("JSONException", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PushingPostActivity.this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                Log.e("VolleyError", error.toString());
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(request);


    }

    public void setViews(){
        spCategory = (MaterialBetterSpinner) findViewById(R.id.spCategory);
        spArea = (MaterialBetterSpinner) findViewById(R.id.spArea);
        spStatus = (MaterialBetterSpinner) findViewById(R.id.spStatus);
        etPrice = (MaterialEditText) findViewById(R.id.etPrice);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

}
