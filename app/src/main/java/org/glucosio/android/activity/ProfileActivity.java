package org.glucosio.android.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.glucosio.android.R;

import cz.msebera.android.httpclient.Header;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


import android.R.layout;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

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
    String d_name;
    String d_id;
    String d_mail_id;
    List<String> allergies;
    List<String> diseases;
    TextView fnameText,lnameText,phoneText,streetAddressText,cityText,stateText,zipcodeText,emailText,welcomeText,d_nameText,d_idText,d_mailText;
TextView allergyText,diseaseText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fnameText = (TextView) findViewById(R.id.fnameT);
        lnameText = (TextView) findViewById(R.id.lnameT);
        phoneText = (TextView) findViewById(R.id.phoneT);
        emailText=(TextView)findViewById(R.id.display_emailT);
        streetAddressText = (TextView) findViewById(R.id.streetAddressT);
        cityText = (TextView) findViewById(R.id.cityT);
        stateText = (TextView) findViewById(R.id.stateT);
        zipcodeText = (TextView) findViewById(R.id.zipcodeT);
        welcomeText=(TextView) findViewById(R.id.welcomeT);
        d_nameText=(TextView) findViewById(R.id.d_nameT);
        d_idText=(TextView) findViewById(R.id.d_idT);
        d_mailText=(TextView) findViewById(R.id.d_mailT);
        allergyText = (TextView) findViewById(R.id.allergies);
        diseaseText = (TextView) findViewById(R.id.diseases);




        // Get the message from the intent
        Intent intent_old = getIntent();
        email = intent_old.getStringExtra("email");

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
                    if (!obj.has("firstName")){
                        welcomeText.setText("NA");
                    fnameText.setText("First Name: " + "NA");
                }

                    else {
                    fname = obj.getString("firstName");
                    welcomeText.setText("Welcome "+fname+" !");
                    fnameText.setText("First Name: "+fname);
                    }

                    if (!obj.has("lastName")){
                        lnameText.setText("Last Name: NA");}
                    else {
                        lname = obj.getString("lastName");
                        lnameText.setText("Last Name: " + lname);
                    }

                    if (!obj.has("email")){
                        emailText.setText("Email: NA");}
                    else {
                    email = obj.getString("email");
                    emailText.setText("Email: "+email);}

                    if (!obj.has("phone")){
                        phoneText.setText("Phone: NA");}
                    else {
                        phone = obj.getString("phone");
                        phoneText.setText("Phone: " + phone);
                    }

                    if (!obj.has("streetAddress")){
                        streetAddressText.setText("Street Address: NA");}
                    else {
                    streetAddress = obj.getString("streetAddress");
                    streetAddressText.setText("Street Address: "+streetAddress);
                    }

                    if (!obj.has("city")){
                        cityText.setText("City :NA");}
                    else {
                        city = obj.getString("city");
                        cityText.setText("City :" + city);
                    }

                    if (!obj.has("state")){
                        stateText.setText("State: NA");
                    }
                    else {
                    state = obj.getString("state");
                    stateText.setText("State: "+state);
                    }

                    if(!obj.has("zipcode"))
                        zipcodeText.setText("ZipCode :NA");

                    else {
                        zipcode = obj.getString("zipcode");
                        Log.d("zipcode", zipcode);
                        zipcodeText.setText("ZipCode :"+zipcode);
                    }

                    if(!obj.has("d_name"))
                        d_nameText.setText("Name: NA");
                    else {
                    d_name = obj.getString("d_name");
                        d_nameText.setText("Name: "+d_name);

                    }

                    if(!obj.has("d_id"))
                        d_idText.setText("Id: NA");
                    else {
                        d_id = obj.getString("d_id");
                        d_idText.setText("Id: "+d_id);

                    }

                    if(!obj.has("d_mail_id"))
                        d_mailText.setText("Email: NA");
                    else {
                        d_mail_id = obj.getString("d_mail_id");
                        d_mailText.setText("Email: "+d_mail_id);

                    }

                    if(obj.has("allergy")) {
                        JSONArray allergy = (JSONArray) obj.get("allergy");
                        if (allergy != null && allergy.length() > 1) {
                            for (int i = 0; i < allergy.length(); i++) {
                                allergies.add(allergy.get(i).toString());
                            }
                        }
                        if (allergies != null && allergies.size() != 0) {
                            allergyText.setText("Allergies: " + allergies.toString());
                        }
                    }
                    else
                        allergyText.setText("Allergies: " + "NA");



                    if(obj.has("disease")) {

                    JSONArray disease = (JSONArray) obj.get("disease");
                    if (disease != null && disease.length() > 1) {
                        for (int i = 0; i < disease.length(); i++) {
                            diseases.add(disease.get(i).toString());
                        }
                    }
                        if (diseases != null && diseases.size() != 0) {
                            diseaseText.setText("Diseases: " + diseases.toString());
                        }
                    }
                    else
                        diseaseText.setText("Diseases: " + "NA");

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


    public void display_profile() {
        Log.d("display", "in display profil");


         /*        TextView al = (TextView) findViewById(R.id.allergies);
        al.setText("Allergies: " + allergies.toString());
        intent.putStringArrayListExtra("Allergies", (ArrayList<String>) allergies);

        TextView dis = (TextView) findViewById(R.id.diseases);
        dis.setText("Diseases: " + diseases.toString());
        intent.putStringArrayListExtra("Diseases", (ArrayList<String>) diseases);

        TextView doc = (TextView) findViewById(R.id.DoctorInfo);
        doc.setText("Doctor Info: " + d_id + d_name + d_mail_id);
        intent.putExtra("DoctorInfo", d_id+d_name+d_mail_id);*/




                ImageView img = (ImageView) findViewById(R.id.profile_pic);
        /*if (gender.equals("F")) {
            g.setText("Gender: Female");
            img.setImageResource(R.drawable.female);
        } else {
            g.setText("Gender: Male");
            img.setImageResource(R.drawable.male);
        }*/
            }

        }

