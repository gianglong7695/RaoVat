package poly.fall16.pro2051.group8.raovat.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import poly.fall16.pro2051.group8.raovat.R;
import poly.fall16.pro2051.group8.raovat.helper.AppController;

import static poly.fall16.pro2051.group8.raovat.utils.MyString.URL_USER;

public class SignUpDetailActivity extends AppCompatActivity {
    public static final int RESULT_LOAD_IMAGE = 1;
    Button btBack, btFinish;
    EditText etName, etPhone, etMail, etAddress;
    Dialog mDialog;
    LinearLayout layout_avatar;
    CircleImageView ivAvatar;
    String encodedImage = "";
    boolean isSignIn = true;
    public static Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_detail);
        mActivity = this;
        setViews();
        setDialog();

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = SignUpActivity.etUser.getText().toString();
                String password = SignUpActivity.etPass.getText().toString();
                String rePassword = SignUpActivity.etRePass.getText().toString();
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                String mail = etMail.getText().toString();
                String address = etAddress.getText().toString();

                if(isSignIn){
                    checkReg(userName, name, mail, phone, password, address);
                }

            }
        });

        layout_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void setViews(){
        btBack = (Button) findViewById(R.id.btBack);
        btFinish = (Button) findViewById(R.id.btSignUp);
        etName = (EditText) findViewById(R.id.etName);
        etMail = (EditText) findViewById(R.id.etEmail);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etAddress = (EditText) findViewById(R.id.etAddress);
        layout_avatar = (LinearLayout) findViewById(R.id.layout_avatar);
        ivAvatar = (CircleImageView) findViewById(R.id.ivAvatar);
    }

    private void checkReg(final String username,final String fullname,final String email,final String phone, final String password, final String address) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_USER , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    Toast.makeText(getBaseContext(),response,Toast.LENGTH_LONG).show();
                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(SignUpDetailActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SignUpDetailActivity.this,LoginActivity.class);
                        startActivity(i);
                        finish();
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

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("txtEmail", email);
                params.put("txtPassword", password);
                params.put("txtUsername",username);
                params.put("txtPhone",phone);
                params.put("txtFullName",fullname);
                params.put("txtImage", encodedImage);
                params.put("txtAddress", address);

                params.put("action","register");
                return params;
            }

        };

        // Adding request to request queue
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }

    public void setDialog(){
        mDialog = new Dialog(SignUpDetailActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // No title
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); // No backfround
        mDialog.setContentView(R.layout.custom_lg_login_dialog);
        mDialog.setCancelable(false); // Disable cancel
    }

    public void showDialog(){
        mDialog.show();
    }

    public void hideDialog(){
        mDialog.hide();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectImg = data.getData();
            try {
                ivAvatar.setImageBitmap(decodeUri(getApplicationContext(), selectImg, 100));
                encodedImage = getStringImage(getRealPathFromURI(getApplicationContext(), selectImg));
            } catch (FileNotFoundException e) {
                Log.e("FileNotFoundException", e.toString());
            }

        }
    }

    public byte[] decodeImageToArray(String path){
        Bitmap bm = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();

        return b;
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String getStringImage(String path){
        Bitmap bmp = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }
}
