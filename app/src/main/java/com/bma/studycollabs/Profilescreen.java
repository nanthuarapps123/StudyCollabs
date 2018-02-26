package com.bma.studycollabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bma.boommenu.BoomButtons.OnBMClickListener;
import com.bma.boommenu.BoomMenuButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Profilescreen extends Activity implements SingleUploadBroadcastReceiver.Delegate{
    String str_username,str_img_url;
    ImageView img_pro_user;
    EditText ed_fname,ed_mname,ed_lname,ed_telephone,ed_phone,ed_screenname,ed_ttype;
    TextView txt_choose_img;
    Button btn_update_pro;
    String jsonurl2,str_token;
    private String userChoosenTask;
    Bitmap bitmap;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    public byte[] imga1=null;
    String str_mobile,str_sname;
    private Uri filePath;
    String fileoripath;
    TextView btn_ch_pw;
    String str_id,str_uname,str_fname,str_mname,str_lname,str_mail,str_telephone,str_imag_path,str_ttype;
    String get_str_id,get_str_uname,get_str_fname,get_str_mname,get_str_lname,get_str_mail,get_str_telephone,get_str_imag_path,get_str_ttype;
    Spinner spi_tu_type;
    List<String> type_of_user;
    ArrayAdapter<String> toc_dataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilescreen);


        Bundle bu_pro_screen = getIntent().getExtras();
        str_token = bu_pro_screen.getString("toksept");
        str_username = bu_pro_screen.getString("usertopro");

        btn_ch_pw = (TextView)findViewById(R.id.btn_ch_pw);


        img_pro_user = (ImageView)findViewById(R.id.pro_img_user);

        ed_fname = (EditText) findViewById(R.id.ed_pro_fname);
        ed_mname = (EditText) findViewById(R.id.ed_pro_mname);
        ed_lname = (EditText) findViewById(R.id.ed_pro_lname);
        spi_tu_type = (Spinner)findViewById(R.id.spi_tutype);

        ed_telephone = (EditText) findViewById(R.id.ed_pro_telephone);
        type_of_user = new ArrayList<String>();

        gettou();

        btn_update_pro = (Button)findViewById(R.id.btn_update_profile) ;
        getprofiledetails();

        btn_ch_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in_ch_pw = new Intent(getApplicationContext(),ChangePassword.class);
                Bundle bu_ch_pw = new Bundle();
                bu_ch_pw.putString("strtok",str_token);
                in_ch_pw.putExtras(bu_ch_pw);
                startActivity(in_ch_pw);
            }
        });

        btn_update_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_fname = ed_fname.getText().toString();
                str_mname = ed_mname.getText().toString();
                str_lname = ed_lname.getText().toString();
                str_telephone = ed_telephone.getText().toString();

                if (str_fname.equals("")){
                    Toast.makeText(getApplicationContext(),"Please Enter your first name",Toast.LENGTH_SHORT).show();
                }

//                str_ttype = ed_ttype.getText().toString();
                str_ttype = spi_tu_type.getSelectedItem().toString();
                if (new CheckNetwork(Profilescreen.this).isNetworkAvailable()) {
                    new updateprofile(str_fname, str_mname, str_lname, get_str_mail, str_telephone, get_str_imag_path, str_ttype).execute();
                } else {
                    Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                }
             }
        });


        img_pro_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                Log.d("SELECTIONCLOSE","TRUE");
//                uploadMultipart();
            }
        });

    }

    private void gettou() {
        String tag_json_arry = "json_array_req";
        JsonArrayRequest req = new JsonArrayRequest(Constant.TUTORTYPE,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.e("Tutortypegetresponse", response.toString());

                        for (int i = 0;i<response.length();i++){
                            try {
                                JSONObject catobj = response.getJSONObject(i);
                                String name = catobj.getString("name");
                                type_of_user.add(name);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        toc_dataAdapter = new ArrayAdapter<String>(Profilescreen.this, android.R.layout.simple_spinner_item, type_of_user);
                        toc_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spi_tu_type.setAdapter(toc_dataAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    message = "Please check your username / Password";
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Log.d("GDFGDNJFD","CALLED");
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", str_token);
                return headers;
            }
            @Override
            public String getBodyContentType()
            {
                return "application/json; charset=utf-8";
            }
        };
        AppData.getInstance().addToRequestQueue(req, tag_json_arry);
    }


    private void getprofiledetails() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Constant.PROFILE_SCREEN_URL, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e("Profilescreenresponse", response.toString());

                if (response != null) {
                    try {
                        get_str_id = response.getString("Id");
                        get_str_uname = response.getString("userName");
                        get_str_fname = response.getString("firstName");
                        get_str_mname = response.getString("middleName");
                        get_str_lname = response.getString("lastName");
                        get_str_mail = response.getString("emailAddress");
                        get_str_telephone = response.getString("telephone");
                        get_str_imag_path = response.getString("uploadedFile");
                        get_str_ttype = response.getString("tutorType");

//                        Log.d("BBBBB",str_fname);
//                        Log.d("BBBBB",str_mname);
//                        Log.d("BBBBBBBBBB",str_lname);
//                        Log.d("BBBBB",str_telephone);
                        Log.d("BBBBB",get_str_ttype);

                        ed_fname.setText(get_str_fname);
                        ed_mname.setText(get_str_mname);
                        ed_lname.setText(get_str_lname);
                        ed_telephone.setText(get_str_telephone);

                        LinearLayout.LayoutParams spinnerLp = (LinearLayout.LayoutParams) spi_tu_type.getLayoutParams();
//                        spinner.setSelection(selectedPositionAge, true);
//                        spinnerLp.gravity = Gravity.CENTER;
//                        spinner.setLayoutParams(spinnerLp);

                        if (get_str_ttype.equals("All")){
                            spi_tu_type.post(new Runnable() {
                                @Override
                                public void run() {
                                    spi_tu_type.setSelection(0,false);
                                }
                            });
                        }else if(get_str_ttype.equals("Not Interested")){
                            spi_tu_type.post(new Runnable() {
                                @Override
                                public void run() {
                                    spi_tu_type.setSelection(1,false);
                                }
                            });
                        }else if(get_str_ttype.equals("Teacher")){
                            spi_tu_type.post(new Runnable() {
                                @Override
                                public void run() {
                                    spi_tu_type.setSelection(2,false);
                                }
                            });
                        }else if(get_str_ttype.equals("Volunteer")){
                            spi_tu_type.post(new Runnable() {
                                @Override
                                public void run() {
                                    spi_tu_type.setSelection(3,false);
                                }
                            });
                        }else if(get_str_ttype.equals("Student")){

                                spi_tu_type.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        spi_tu_type.setSelection(4,false);
                                    }
                                });

                        }

                        RequestOptions myOptions = new RequestOptions()
                                .fitCenter()
                                .override(100, 100);

//                        Glide.with(Profilescreen.this)
//                                .load(get_str_imag_path)
//                                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(20)))
//                                .into(img_pro_user);

                        Glide.with(Profilescreen.this)
                                .load(get_str_imag_path)
                                .apply(RequestOptions.circleCropTransform())
                                .into(img_pro_user);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    message = "Please check your username / Password";
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                }
            }

        }) {
            @Override
            public Priority getPriority() {
                return Priority.HIGH;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", str_token);
                return headers;
            }
        };

        AppData.getInstance().addToRequestQueue(request);

    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {

    }

    @Override
    public void onError(Exception exception) {

    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {

        Log.e("profileimagecomleted","trueujsbgh");
        Toast.makeText(getApplicationContext(),"Completed",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCancelled() {

    }

    //update profile class
    public class updateprofile extends AsyncTask<String, Void, String> {
        String str_fname,str_mname,str_lname,str_mail,str_img_path,str_tele,str_ttype;
        public updateprofile(String str_fname, String str_mname, String str_lname, String str_mail, String str_tele, String img_path, String ttype) {

            this.str_fname = str_fname;
            this.str_mname = str_mname;
            this.str_lname = str_lname;
            this.str_mail = str_mail;
            this.str_img_path = img_path;
            this.str_tele = str_tele;
            this.str_ttype = ttype;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String json = "", jsonStr = "";

                try {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("firstName", str_fname);
                        jsonObject.put("middleName", str_mname);
                        jsonObject.put("lastName", str_lname);
                        jsonObject.put("emailAddress", str_mail);
                        jsonObject.put("telephone", str_tele);
                        jsonObject.put("uploadedFile", str_img_path);
                        jsonObject.put("tutorType",str_ttype);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String requestBody = jsonObject.toString();

                    return jsonStr = HttpUtils.makeRequest1(Constant.PROFILE_UPDATE_URL, requestBody, str_token);
                } catch (Exception e) {
                    Log.e("InputStream", e.getLocalizedMessage());
                }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("Profileimageupdate", "RESULT------------>" + s);

            try {
                JSONObject response = new JSONObject(s);

                String status = response.getString("status");

                if (status.equals("success")){
                    JSONObject response1 = response.getJSONObject("message");

                    get_str_fname = response1.getString("f_name");
                    get_str_mname = response1.getString("m_name");
                    get_str_lname = response1.getString("l_name");
//                get_str_mail = response.getString("emailAddress");
                    get_str_telephone = response1.getString("telephone");
                    get_str_imag_path = response1.getString("uploadedFile");
                    get_str_ttype = response1.getString("tutor_type");

                    ed_fname.setText(get_str_fname);
                    ed_mname.setText(get_str_mname);
                    ed_lname.setText(get_str_lname);
                    ed_telephone.setText(get_str_telephone);

//                    ed_ttype.setText(get_str_ttype);

                    if (get_str_ttype.equals("All")){
                        spi_tu_type.setSelection(0);
                    }else if(get_str_ttype.equals("Not Interested")){
                        spi_tu_type.setSelection(1);
                    }else if(get_str_ttype.equals("Teacher")){
                        spi_tu_type.setSelection(2);
                    }else if(get_str_ttype.equals("Volunteer")){
                        spi_tu_type.setSelection(3);
                    }else if(get_str_ttype.equals("Student")){
                        spi_tu_type.setSelection(4);
                    }


                    Toast.makeText(getApplicationContext(),"Profile updated successfully",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Some went wrong try again later....",Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //update profile class

    //upload image for update profile image


    private void selectImage() {

        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Profilescreen.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(Profilescreen.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        imga1=bytes.toByteArray();
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        img_user.setImageBitmap(thumbnail);
        filePath = data.getData();
        bitmap = thumbnail;
//        img_pro_user.setImageBitmap(bitmap);
        BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
        img_pro_user.setBackgroundDrawable(ob);



        Uri tempUri = getImageUri(getApplicationContext(), bitmap);
        // CALL THIS METHOD TO GET THE ACTUAL PATH
//        File finalFile = new File(getRealPathFromURI(tempUri));
        fileoripath = getRealPathFromURI(tempUri);
//        System.out.println(mImageCaptureUri);

        if (new CheckNetwork(Profilescreen.this).isNetworkAvailable()) {
            uploadMultipart();
        }else {
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }


    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                filePath = data.getData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        img_user.setImageBitmap(bm);
        bitmap = bm;
//        img_pro_user.setImageBitmap(bitmap);
        BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
        img_pro_user.setBackgroundDrawable(ob);
        Uri tempUri = getImageUri(getApplicationContext(), bitmap);
        fileoripath = getRealPathFromURI(tempUri);
//        uploadMultipart();


        if (new CheckNetwork(Profilescreen.this).isNetworkAvailable()) {
            uploadMultipart();
        }else {
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    public void uploadMultipart() {
        Log.d("FILEPATHHH",fileoripath);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
//            Log.e("EEEREEE","ENTERTTGH");
            //Creating a multi part request


            new MultipartUploadRequest(this, uploadId, Constant.IMAGE_UPLOAD_URL)
                    .addFileToUpload(fileoripath, "uploadedFile") //Adding file
                    .addHeader("Authorization",str_token)
                    .addHeader("Content-Type","application/json")
                    .setMethod("POST")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("EEEREEE",exc.getMessage());
        }
    }



    //upload image for update profile image

}
