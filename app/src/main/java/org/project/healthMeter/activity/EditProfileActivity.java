package org.project.healthMeter.activity;

/**
 * Created by rajeshkhandelwal on 11/12/15.
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.project.healthMeter.R;
import org.project.healthMeter.tools.MultipartEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class EditProfileActivity extends AppCompatActivity implements
        OnEditorActionListener, OnItemClickListener, OnClickListener,
        android.view.View.OnClickListener{
    private static final String TAG = "upload";
    String response = "";
    Intent intent;
    String id;
    private ProgressDialog dialog;
    private EditText caption;
    private static final int GALLERY = 0;
    private static final int CAMERA = 1;
    Bitmap profile_pic;
    ImageView profile_pic_view;
    String byte_image = "";
    String username = "";
    InputStream in;
    String img_path = "";
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // FirstName Edit View Object
    EditText fNameET;
    // LastName Edit View Object
    EditText lNameET;
    // Email Edit View Object
    TextView emailET;
    // Passwprd Edit View Object
    EditText pwdET;
    EditText phoneET;
    // Passwprd Edit View Object
    EditText streetET;
    EditText stateET;
    EditText cityET;
    EditText zipcodeET;
    List<String> diseaseList=new ArrayList<>();
    List<String> allergyList=new ArrayList<>();
    String email;
    Bitmap bitmap;
    String doctorName="",dPhone="",doctorMailId="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setProgressBarIndeterminate(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String flag = intent.getStringExtra("flag");

        id = intent.getStringExtra("id");
        email = intent.getStringExtra("email");
        bitmap = (Bitmap) intent.getParcelableExtra("image");



        // Find FirstName Edit View control by ID
        fNameET = (EditText) findViewById(R.id.editfName);
        fNameET.setText(intent.getStringExtra("fname"));

        // Find LastName Edit View control by ID
        lNameET = (EditText) findViewById(R.id.editlName);
        lNameET.setText(intent.getStringExtra("lname"));

        // Find Email Edit View control by ID
        emailET = (TextView) findViewById(R.id.editEmail);
        emailET.setText(intent.getStringExtra("email"));

        // Find Password Edit View control by ID
        pwdET = (EditText) findViewById(R.id.editPassword);
        pwdET.setText(intent.getStringExtra("password"));


        phoneET = (EditText) findViewById(R.id.editPhone);
        phoneET.setText(intent.getStringExtra("phone"));

        streetET = (EditText) findViewById(R.id.editStreetAddress);
        streetET.setText(intent.getStringExtra("streetAdress"));

        stateET = (EditText) findViewById(R.id.editState);
        stateET.setText(intent.getStringExtra("state"));

        cityET = (EditText) findViewById(R.id.editCity);
        cityET.setText(intent.getStringExtra("city"));

        zipcodeET = (EditText) findViewById(R.id.editZipcode);
        zipcodeET.setText(intent.getStringExtra("zipCode"));


        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        String gender = intent.getStringExtra("gender");


        if(bitmap!=null){
            ImageView profile_pic_view = (ImageView) findViewById(R.id.profile_pic);
            profile_pic_view.setImageBitmap(bitmap);
            if (gender.equals("F")) {
                RadioButton r = (RadioButton) findViewById(R.id.female);
                r.setChecked(true);
            } else {
                RadioButton r = (RadioButton) findViewById(R.id.male);
                r.setChecked(true);
            }
        }
        else {
            profile_pic_view = (ImageView) findViewById(R.id.profile_pic);
            if (gender.equals("F")) {
                RadioButton r = (RadioButton) findViewById(R.id.female);
                profile_pic_view.setImageResource(R.drawable.female);
                r.setChecked(true);
            } else {
                RadioButton r = (RadioButton) findViewById(R.id.male);
                profile_pic_view.setImageResource(R.drawable.male);
                r.setChecked(true);
            }
        }

        String [] birthDate = intent.getStringExtra("birthDate").split("/");
        if(birthDate.length > 1)
        {
            int day = Integer.parseInt(birthDate[0]);
            int month = Integer.parseInt(birthDate[1]) - 1;
            int year = Integer.parseInt(birthDate[2]);
            DatePicker bdate = (DatePicker) findViewById(R.id.bdate);
            bdate.updateDate(year, month, day);
        }
        doctorName=intent.getStringExtra("doctorName");
        dPhone=intent.getStringExtra("dPhone");
        doctorMailId=intent.getStringExtra("doctorMailId");

        TextView dNameT = (TextView) findViewById(R.id.d_nameT);
        dNameT.setText(doctorName);

        TextView dPhoneT = (TextView) findViewById(R.id.d_phoneT);
        dPhoneT.setText(dPhone);

        TextView dMail = (TextView) findViewById(R.id.d_mailT);
        dMail.setText(doctorMailId);
    }


    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.soyabean:
                if (checked)
                    allergyList.add("Soyabean");
                break;
            case R.id.milk:
                if (checked)
                    allergyList.add("Milk");
                break;
            case R.id.gluten:
                if (checked)
                    allergyList.add("Gluten");
                break;
            case R.id.egg:
                if (checked)
                    allergyList.add("Egg");
                break;
            case R.id.mustard:
                if (checked)
                    allergyList.add("Mustard");
                break;
            case R.id.peanuts:
                if (checked)
                    allergyList.add("peanuts");
                break;
            case R.id.fish:
                if (checked)
                    allergyList.add("Fish");
                break;
            case R.id.nuts:
                if (checked)
                    allergyList.add("Nuts");
                break;
            case R.id.coconut:
                if (checked)
                    allergyList.add("Coconut");
                break;
            case R.id.seasmeSeeds:
                if (checked)
                    allergyList.add("seasmeSeeds");
                break;
            case R.id.corn:
                if (checked)
                    allergyList.add("Corn");
                break;
            case R.id.flour:
                if (checked)
                    allergyList.add("Flour");
                break;
            case R.id.wheyPowder:
                if (checked)
                    allergyList.add("Whey Powder");
                break;
            case R.id.cornStarch:
                if (checked)
                    allergyList.add("Corn Starch");
                break;
            case R.id.celery:
                if (checked)
                    allergyList.add("Celery");
                break;
            case R.id.candelnut:
                if (checked)
                    allergyList.add("Candelnut");
                break;
            default:
                allergyList.add("No Allergies");
                break;
        }
    }

    public void onDiseaseCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {

            case R.id.diabetes:
                if (checked)
                    diseaseList.add("Diabetes");
                break;
            case R.id.BloodPressure:
                if (checked)
                    diseaseList.add("Blood Pressure");
                break;
            default:
               diseaseList.add("No Diseases");
                break;

        }
    }


    public void update_profile(View view) throws MalformedURLException, JSONException, UnsupportedEncodingException {
        intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("id", id);
        Pattern pattern = Patterns.EMAIL_ADDRESS;



        EditText fname = (EditText) findViewById(R.id.editfName);
        String str_fname = fname.getText().toString();

        EditText lname = (EditText) findViewById(R.id.editlName);
        String str_lname = lname.getText().toString();

       // EditText gemail = (EditText) findViewById(R.id.editEmail);
        //String str_email = gemail.getText().toString();


        EditText password = (EditText) findViewById(R.id.editPassword);
        String str_password = password.getText().toString();

        EditText confirm_password = (EditText) findViewById(R.id.confirm_password);
        String str_confirm_password = confirm_password.getText().toString();

        EditText phone = (EditText) findViewById(R.id.editPhone);
        String str_phone = phone.getText().toString();

        EditText streetAddress = (EditText) findViewById(R.id.editStreetAddress);
        String str_streetAddress = streetAddress.getText().toString();

        EditText state = (EditText) findViewById(R.id.editState);
        String str_state = state.getText().toString();

        EditText city = (EditText) findViewById(R.id.editCity);
        String str_city = city.getText().toString();

        EditText zipcode = (EditText) findViewById(R.id.editZipcode);
        String str_zipcode = zipcode.getText().toString();



        //   EditText pic = (EditText) findViewById(R.id.picture);
        //   String str_pic = pic.getText().toString();
        String ch_gender;
        RadioButton gender = (RadioButton) findViewById(R.id.female);
        if(gender.isChecked())
            ch_gender = "F";
        else
            ch_gender = "M";

        DatePicker bdate = (DatePicker) findViewById(R.id.bdate);
        String str_bdate = bdate.getDayOfMonth()+"/"+(bdate.getMonth()+1)+"/"+bdate.getYear();

        System.out.println(bdate.toString());



        if(!str_password.equals(str_confirm_password))
        {
            runOnUiThread(new Runnable() {
                public void run() {

                    TextView err = (TextView) findViewById( R.id.update_profile_error );
                    err.setText("Passwords don't match!");
                    EditText password = (EditText) findViewById(R.id.editPassword);
                    password.setText("");
                    EditText confirm_password = (EditText) findViewById(R.id.confirm_password);
                    confirm_password.setText("");
                }
            });
        }
        else if(str_confirm_password.equals("") || str_password.equals("") || str_fname.equals("") ||str_lname.equals(""))
        {
            runOnUiThread(new Runnable() {
                public void run() {

                    TextView err = (TextView) findViewById( R.id.update_profile_error );
                    err.setText("Please fill all the fields!");
                }
            });
        }



            JSONObject jsonParams = new JSONObject();
     //       jsonParams.put("UserPicture", img_path);
            jsonParams.put("id", id);
            jsonParams.put("firstName", str_fname);
            jsonParams.put("lastName", str_lname);
            jsonParams.put("password", str_password);

            // Put Http parameter username with value of Email Edit View control
            jsonParams.put("email", email);
            // Put Http parameter password with value of Password Edit View control
            jsonParams.put("phone", str_phone);
            jsonParams.put("streetAddress", str_streetAddress);
            jsonParams.put("state", str_state);
            jsonParams.put("city", str_city);
            jsonParams.put("zipCode", str_zipcode);
            jsonParams.put("birthDate", str_bdate);
            jsonParams.put("gender", ch_gender);
            Log.d("str_bdate",str_bdate);

            JSONArray alist=new JSONArray();
            if (allergyList.size()==0)
                alist.put("No Allergies");
            else {
                for (int i = 0; i < allergyList.size(); i++) {
                    alist.put(allergyList.get(i));
                }
            }
            JSONArray dlist=new JSONArray();
            if(diseaseList.size()==0)
                dlist.put("No Diseases");
            else {
                for (int i = 0; i < diseaseList.size(); i++) {
                    dlist.put(diseaseList.get(i));
                }
            }
            jsonParams.put("allergy", alist);
            jsonParams.put("disease", dlist);
            jsonParams.put("doctorMailId",doctorMailId);
            jsonParams.put("doctorName",doctorName);
            jsonParams.put("dPhone",dPhone);
  //          jsonParams.put("UserGender", ch_gender);
   //         jsonParams.put("UserBirthDate", str_bdate);
            //   jsonParams.put("notes", "Test api support");
            StringEntity entity = new StringEntity(jsonParams.toString());
            Log.d("json print", String.valueOf(jsonParams));
            entity.setContentType(String.valueOf(new BasicHeader(HTTP.CONTENT_TYPE, "application/json")));


            // Invoke RESTful Web Service with Http parameters
            invokeWS(entity);
            Intent myIntent;
            myIntent = new Intent(this, ProfileActivity.class);
            myIntent.putExtra("email", email);
            startActivity(myIntent);


    }

    public void invokeWS(StringEntity entity){
        // Show Progress Dialog
        prgDialog.show();
        Log.d("y so", "manyyyyyyyyy");

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(this,"http://10.0.0.12:8080/webapp/login/editProfile", entity,"application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(String.valueOf(responseBody));
                    // When the JSON response has status boolean value assigned with true

                    if (obj.getBoolean("status")) {
                        // Set Default Values for Edit View controls
                        // Display successfully registered message using Toast
                        Toast.makeText(getApplicationContext(), "Profile edited!", Toast.LENGTH_LONG).show();
                    }
                    // Else display error message
                    else {
                        errorMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
             //       Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
// Hide Progress Dialog
                prgDialog.hide();
                Log.d("statusCode",String.valueOf(statusCode));
                // When Http response code is '404'
                if (statusCode == 404 || statusCode == 405) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    public void chooseImageSource(View v) {
        System.gc();
        final CharSequence[] items = {
                getString(R.string.gallery),
                getString(R.string.camera) };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose_image_source);
        builder.setItems(items, this);
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void startCamera() {
        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camIntent, CAMERA);
    }


    protected void startGallery() {
        Intent gallIntent = new Intent(Intent.ACTION_GET_CONTENT);
        gallIntent.setType("image/*");
        startActivityForResult(gallIntent, GALLERY);
    }

    private Bitmap getImageFromURI(Uri data) {
        try {
            in = getContentResolver().openInputStream(data);
            return Media.getBitmap(getContentResolver(), data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY || requestCode == CAMERA) {
            if (resultCode == RESULT_OK) {
                System.out.println("onActivityResult");
        //        profile_pic = getImageFromURI(data.getData());
                System.out.println(profile_pic);
                setPic();
                if (profile_pic != null) {
                    System.out.println("null image");
                    profile_pic_view.setImageBitmap(profile_pic);
                } else {
                    Toast.makeText(getBaseContext(),
                            getString(R.string.image_error), Toast.LENGTH_LONG)
                            .show();
                }
            } else {
                if (resultCode != RESULT_CANCELED) {
                    Toast.makeText(getBaseContext(), getString(R.string.error),
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private void sendPhoto(Bitmap bitmap) throws Exception {
        Log.d("sendinggggg....","photoooo");

        new UploadTask().execute(bitmap);
    }

    private class UploadTask extends AsyncTask<Bitmap, Void, Void> {

        protected Void doInBackground(Bitmap... bitmaps) {
            if (bitmaps[0] == null)
                return null;
            setProgress(0);

            Bitmap bitmap = bitmaps[0];
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // convert Bitmap to ByteArrayOutputStream
            InputStream in = new ByteArrayInputStream(stream.toByteArray()); // convert ByteArrayOutputStream to ByteArrayInputStream
           // intent.putExtra("image", byteArray);
            DefaultHttpClient httpclient = new DefaultHttpClient();
            try {
                Log.d("posting....","postt");
                HttpPost httppost = new HttpPost(
                        "http://10.0.0.12:8080/webapp/patientImage/upload"); // server

                MultipartEntity reqEntity = new MultipartEntity();
                reqEntity.addPart("image",
                        email + ".jpg", in);
               // reqEntity.addPart("email",email);
                httppost.setEntity(reqEntity);

                Log.i(TAG, "request " + httppost.getRequestLine());
                HttpResponse response = null;
                try {
                    response = httpclient.execute(httppost);
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    if (response != null)
                        Log.i(TAG, "response " + response.getStatusLine().toString());
                } finally {

                }
            } finally {

            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
          //  Toast.makeText(this, R.string.uploaded, Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.i(TAG, "onResume: " + this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
    }
    String mCurrentPhotoPath;

    static final int REQUEST_TAKE_PHOTO = 1;
    File photoFile = null;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String storageDir = Environment.getExternalStorageDirectory() + "/picupload";
        File dir = new File(storageDir);
        if (!dir.exists())
            dir.mkdir();

        File image = new File(storageDir + "/" + email + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i(TAG, "photo path = " + mCurrentPhotoPath);
        return image;
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = profile_pic_view.getWidth();
        int targetH = profile_pic_view.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor << 1;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        Matrix mtx = new Matrix();
        mtx.postRotate(90);
        // Rotating Bitmap
        Bitmap rotatedBMP = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, true);

        if (rotatedBMP != bitmap)
            bitmap.recycle();

        profile_pic_view.setImageBitmap(bitmap);

        try {
            sendPhoto(bitmap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(DialogInterface dialog, int item) {
        if (item == GALLERY) {
            startGallery();
        } else {
            if (item == CAMERA) {
              //  startCamera();
                dispatchTakePictureIntent();

            } else {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
        // TODO Auto-generated method stub
        return false;
    }


}

