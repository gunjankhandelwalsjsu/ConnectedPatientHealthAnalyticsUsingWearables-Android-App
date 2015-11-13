package org.project.healthMeter.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.project.healthMeter.R;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    String email;
    Intent intent;
    LocationManager locationManager;
    String provider;

    String fname = "";
    String lname = "";
    String streetAddress = "";
    String city = "";
    String phone = "";
    String picture = "";
    String state = "";
    String zipcode = "";
    String birthDate = "";
    String gender = "M";


    String d_name;
    String d_id;
    String d_mail_id;
    List<String> allergies = new ArrayList<>();
    List<String> diseases = new ArrayList<>();
    TextView fnameText, lnameText, phoneText, streetAddressText, cityText, stateText, zipcodeText, emailText, welcomeText, d_nameText, d_idText, d_mailText;
    TextView allergyText, diseaseText, genderText, birthDateText;
    public static final String MyPREFERENCES = "MyPrefs";
    ImageView profile_pic_view;

    SharedPreferences sharedpreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setProgressBarIndeterminate(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");
        actionBar.setDisplayHomeAsUpEnabled(true);

        fnameText = (TextView) findViewById(R.id.fnameT);
        lnameText = (TextView) findViewById(R.id.lnameT);
        profile_pic_view = (ImageView) findViewById(R.id.profile_pic);
        genderText = (TextView) findViewById(R.id.gender);
        birthDateText = (TextView) findViewById(R.id.birthDate);
        phoneText = (TextView) findViewById(R.id.phoneT);
        emailText = (TextView) findViewById(R.id.display_emailT);
        streetAddressText = (TextView) findViewById(R.id.streetAddressT);
        cityText = (TextView) findViewById(R.id.cityT);
        stateText = (TextView) findViewById(R.id.stateT);
        zipcodeText = (TextView) findViewById(R.id.zipcodeT);
        welcomeText = (TextView) findViewById(R.id.welcomeT);
        d_nameText = (TextView) findViewById(R.id.d_nameT);
        d_idText = (TextView) findViewById(R.id.d_idT);
        d_mailText = (TextView) findViewById(R.id.d_mailT);
        allergyText = (TextView) findViewById(R.id.allergies);
        diseaseText = (TextView) findViewById(R.id.diseases);


        // Get the message from the intent
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        email = sharedpreferences.getString("email", "NA");

        intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("email", email);
        fetch_profile();
        display_profile();

    }

    public void fetch_profile() {


        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://10.0.0.12:8080/webapp/login/profile/" + email, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    if (responseBody != null) {
                        String responseStr = new String(responseBody);
                        Log.d("Response", responseStr);

                    }
                    String json = new String(responseBody, "UTF8");

                    JSONObject obj = new JSONObject(json);
                    intent.putExtra("id", obj.getString("id"));
                    if (!obj.has("firstName")) {
                        welcomeText.setText("NA");

                        fnameText.setText("First Name: " + "NA");
                        intent.putExtra("fname", "NA");

                    } else {
                        fname = obj.getString("firstName");
                        welcomeText.setText("Welcome " + fname + " !");
                        fnameText.setText("First Name: " + fname);
                        intent.putExtra("fname", fname);

                    }

                    if (!obj.has("lastName")) {
                        lnameText.setText("Last Name: NA");
                        intent.putExtra("lname", "NA");

                    } else {
                        lname = obj.getString("lastName");
                        lnameText.setText("Last Name: " + lname);
                        intent.putExtra("lname", "lname");

                    }
                    fetch_image();
                    if (profile_pic_view == null) {
                        if (!obj.has("gender")) {
                            genderText.setText("Gender: Male");
                            intent.putExtra("gender", "M");
                        } else {
                            gender = obj.getString("gender");
                            if (gender.equals("F")) {
                                genderText.setText("Gender: Female");
                                profile_pic_view.setImageResource(R.drawable.female);
                                intent.putExtra("gender", "F");

                            } else {
                                genderText.setText("Gender: Male");
                                profile_pic_view.setImageResource(R.drawable.male);
                                intent.putExtra("gender", "M");

                            }
                        }
                    } else if (!obj.has("gender")) {
                        genderText.setText("Gender: Male");
                        intent.putExtra("gender", "M");
                    } else {
                        gender = obj.getString("gender");
                        if (gender.equals("F")) {
                            genderText.setText("Gender: Female");
                            intent.putExtra("gender", "F");

                        } else {
                            genderText.setText("Gender: Male");
                            intent.putExtra("gender", "M");

                        }
                    }


                    if (!obj.has("birthDate")) {
                        birthDateText.setText("Birth Date: NA");
                        intent.putExtra("birthDate", "NA");

                    } else {
                        birthDate = obj.getString("birthDate");
                        birthDateText.setText("Birth Date: " + birthDate);
                        intent.putExtra("birthDate", "birthDate");

                    }


                    if (!obj.has("email")) {
                        emailText.setText("Email: NA");
                        intent.putExtra("email", "NA");
                    } else {
                        email = obj.getString("email");
                        emailText.setText("Email: " + email);
                        intent.putExtra("email", email);

                    }

                    if (!obj.has("phone")) {
                        phoneText.setText("Phone: NA");
                        intent.putExtra("phone", "NA");
                    } else {
                        phone = obj.getString("phone");
                        phoneText.setText("Phone: " + phone);
                        intent.putExtra("phone", phone);


                    }

                    if (!obj.has("streetAddress")) {
                        streetAddressText.setText("Street Address: NA");
                        intent.putExtra("streetAddress", "NA");


                    } else {
                        streetAddress = obj.getString("streetAddress");
                        streetAddressText.setText("Street Address: " + streetAddress);
                        intent.putExtra("streetAddress", streetAddress);

                    }

                    if (!obj.has("city")) {
                        cityText.setText("City :NA");
                        intent.putExtra("city", "NA");
                    } else {
                        city = obj.getString("city");
                        cityText.setText("City :" + city);
                        intent.putExtra("city", city);

                    }

                    if (!obj.has("state")) {
                        stateText.setText("State: NA");
                        intent.putExtra("state", "NA");


                    } else {
                        state = obj.getString("state");
                        stateText.setText("State: " + state);
                        intent.putExtra("state", state);

                    }

                    if (!obj.has("zipcode")) {
                        zipcodeText.setText("ZipCode :NA");
                        intent.putExtra("zipcode", "NA");

                    } else {
                        zipcode = obj.getString("zipcode");
                        Log.d("zipcode", zipcode);
                        zipcodeText.setText("ZipCode :" + zipcode);
                        intent.putExtra("zipcode", zipcode);

                    }

                    if (!obj.has("d_name")) {
                        d_nameText.setText("Name: NA");
                        intent.putExtra("d_name", "NA");
                    } else {
                        d_name = obj.getString("d_name");
                        d_nameText.setText("Name: " + d_name);
                        intent.putExtra("d_name", d_name);


                    }

                    if (!obj.has("d_id")) {
                        d_idText.setText("Id: NA");
                        intent.putExtra("d_id", "NA");
                    } else {
                        d_id = obj.getString("d_id");
                        d_idText.setText("Id: " + d_id);

                        intent.putExtra("d_id", d_id);

                    }

                    if (!obj.has("d_mail_id")) {
                        d_mailText.setText("Email: NA");
                        intent.putExtra("d_mail_id", "NA");
                    } else {
                        d_mail_id = obj.getString("d_mail_id");
                        d_mailText.setText("Email: " + d_mail_id);
                        intent.putExtra("d_mail_id", d_mail_id);


                    }

                    if (obj.has("allergy")) {
                        JSONArray allergy = (JSONArray) obj.get("allergy");
                        if (allergy != null && allergy.length() > 1) {
                            for (int i = 0; i < allergy.length(); i++) {
                                allergies.add(allergy.get(i).toString());
                            }
                        }
                        if (allergies != null && allergies.size() != 0) {
                            allergyText.setText("Allergies: " + allergies.toString());
                            intent.putExtra("allergies", allergies.toString());

                        }
                    } else {
                        allergyText.setText("Allergies: " + "NA");
                        intent.putExtra("allergies", "NA");
                    }


                    if (obj.has("disease")) {

                        JSONArray disease = (JSONArray) obj.get("disease");
                        if (disease != null && disease.length() > 1) {
                            for (int i = 0; i < disease.length(); i++) {
                                diseases.add(disease.get(i).toString());
                            }
                        }
                        if (diseases != null && diseases.size() != 0) {
                            diseaseText.setText("Diseases: " + diseases.toString());
                            intent.putExtra("diseases", diseases.toString());

                        }
                    } else {
                        diseaseText.setText("Diseases: " + "NA");
                        intent.putExtra("diseases", "NA");

                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
                    error) {
                Log.d("statusCode", String.valueOf(statusCode));
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

    public void edit_profile(View view) throws MalformedURLException, JSONException {
        startActivity(intent);
    }


    public void display_profile() {
        Log.d("display", "in display profil");

    }

    public void fetch_image() {

        new DownloadImage().execute();

    }

    public class DownloadImage extends AsyncTask<Void, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(Void... params) {

            String url = "http://10.0.0.12:8080/webapp/patientImage/download/" + email;
            try {
                URLConnection connection = new URL(url).openConnection();
                connection.setConnectTimeout(1000 * 30);
                connection.setReadTimeout(1000 * 30);
                return BitmapFactory.decodeStream((InputStream) connection.getContent(), null, null);
            } catch (Exception e) {
                Log.e("Hub", "Error getting the image from server : " + e.getMessage().toString());
                return null;
            }

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                profile_pic_view.setImageBitmap(bitmap);
                intent.putExtra("image", bitmap);
            }


        }
    }
}







/*        TextView al = (TextView) findViewById(R.id.allergies);
        al.setText("Allergies: " + allergies.toString());
        intent.putStringArrayListExtra("Allergies", (ArrayList<String>) allergies);

        TextView dis = (TextView) findViewById(R.id.diseases);
        dis.setText("Diseases: " + diseases.toString());
        intent.putStringArrayListExtra("Diseases", (ArrayList<String>) diseases);

        TextView doc = (TextView) findViewById(R.id.DoctorInfo);
        doc.setText("Doctor Info: " + d_id + d_name + d_mail_id);
        intent.putExtra("DoctorInfo", d_id+d_name+d_mail_id);*/








