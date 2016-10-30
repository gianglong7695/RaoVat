package poly.fall16.pro2051.group8.raovat.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import poly.fall16.pro2051.group8.raovat.R;
import poly.fall16.pro2051.group8.raovat.adapters.ImageSelectAdapter;
import poly.fall16.pro2051.group8.raovat.networks.MySingleton;
import poly.fall16.pro2051.group8.raovat.objects.CityObject;
import poly.fall16.pro2051.group8.raovat.objects.ImageObject;
import poly.fall16.pro2051.group8.raovat.utils.MyString;

import static poly.fall16.pro2051.group8.raovat.activities.SignUpDetailActivity.RESULT_LOAD_IMAGE;

public class PushingPostActivity extends AppCompatActivity {
    MaterialBetterSpinner spCategory, spArea, spStatus;
    MaterialEditText etPrice, etName, etDetail, etAdress;
    String[] categoryList = {"Xe cộ", "Bất động sản", "Đồ điện tử", "Thời trang, đồ dùng cá nhân", "..."};
    String[] arrStatus = {"Mới 100%", "Like new", "Cũ"};
    ArrayList arrCity;
    ArrayList<CityObject> alCity;
    ArrayAdapter<String> adapterCity;
    RelativeLayout layoutPushImg;
    ArrayList<ImageObject> alImage;
    ArrayList<String> alBaseImage;
    ImageSelectAdapter imageSelectAdapter;
    RecyclerView rvImageSelecter;
    public static LinearLayout largeSelect, addPictureView;
    ImageView ivBackButton;
    int statusChoice, categoryChoice;
    Button btUpload;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushing_post);
        setViews();
        setDialog();

        alCity = new ArrayList<>();
        arrCity = new ArrayList();
        alImage = new ArrayList<>();
        alBaseImage = new ArrayList<>();

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

        imageSelectAdapter = new ImageSelectAdapter(alImage, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rvImageSelecter.setLayoutManager(mLayoutManager);
        rvImageSelecter.setItemAnimator(new DefaultItemAnimator());
        rvImageSelecter.setAdapter(imageSelectAdapter);

        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                statusChoice = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryChoice = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        largeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        addPictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventPushingButton();
            }
        });


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
                        alCity.add(city);
                    }


                    for (int j = 0; j < alCity.size(); j++) {
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

    public void setViews() {
        spCategory = (MaterialBetterSpinner) findViewById(R.id.spCategory);
        spArea = (MaterialBetterSpinner) findViewById(R.id.spArea);
        spStatus = (MaterialBetterSpinner) findViewById(R.id.spStatus);
        etPrice = (MaterialEditText) findViewById(R.id.etPrice);
        layoutPushImg = (RelativeLayout) findViewById(R.id.layoutPushImg);
        rvImageSelecter = (RecyclerView) findViewById(R.id.rvImgItems);
        largeSelect = (LinearLayout) findViewById(R.id.largeSelect);
        addPictureView = (LinearLayout) findViewById(R.id.addPictureView);
        addPictureView.setVisibility(View.GONE);
        ivBackButton = (ImageView) findViewById(R.id.ivBackButton);
        etName = (MaterialEditText) findViewById(R.id.etName);
        etAdress = (MaterialEditText) findViewById(R.id.etAdress);
        etDetail = (MaterialEditText) findViewById(R.id.etDetail);
        btUpload = (Button) findViewById(R.id.btUpload);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectImg = data.getData();
            alImage.add(new ImageObject(selectImg, getStringImage(getRealPathFromURI(getApplicationContext(), selectImg))));
            alBaseImage.add(getStringImage(getRealPathFromURI(getApplicationContext(), selectImg)));
            imageSelectAdapter.notifyDataSetChanged();
            if (alImage.size() > 0) {
                largeSelect.setVisibility(View.GONE);
                addPictureView.setVisibility(View.VISIBLE);
            } else {
                addPictureView.setVisibility(View.GONE);
            }
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getStringImage(String path) {
        Bitmap bmp = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public void eventPushingButton() {
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                MyString.URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                    // Check for error node in json
                    if (!error) {

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.toString(), Toast.LENGTH_LONG).show();
                }
                hideDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("txtName", etName.getText().toString());
                params.put("txtAddress", etAdress.getText().toString());
                params.put("txtDescription", etDetail.getText().toString());
                params.put("txtQuality", arrStatus[statusChoice]);
                params.put("txtCategory", categoryList[categoryChoice]);
                params.put("txtPrice", etPrice.getText().toString());
                params.put("txtUsername", MainActivity.tvName.getText().toString());
                ArrayList<String> listBaseImage = new ArrayList<>();
                for (int i = 0; i < alImage.size(); i++){
                    listBaseImage.add(alImage.get(i).baseImage);
                }

                JSONArray jsonArray = new JSONArray(alBaseImage);

                params.put("txtImage", alBaseImage.get(0).toString());
                params.put("JSON",jsonArray.toString());
                params.put("action", "insert");

                return params;
            }

        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq);

    }

    public void setDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Đang upload ...");
        pDialog.setCancelable(false);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
