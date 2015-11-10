package org.glucosio.android.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.glucosio.android.R;

import cz.msebera.android.httpclient.Header;

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

    String contactInfo = "";
    String fname = "";
    String lname = "";
    String streetAddress = "";
    String city = "";
    String phone = "";
    String picture = "";
    String state="";
    String zipcode="";
    String d_name;
    String d_id;
    String d_mail_id;
    List<String> allergies;
    List<String>  diseases;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // Get the message from the intent
        Intent intent_old = getIntent();
        email = intent_old.getStringExtra("email");

        intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("email", email);
        fetch_profile();
        display_profile();
    }

    public void fetch_profile() {
        final ProgressDialog pdia;
     /*   pdia = ProgressDialog.show(ProfileActivity.this, "",
                "Loading.....", true);*/
     //   System.out.println("in fetch profile");


        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://10.0.0.12:8080/webapp/login/profile/" + email, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(responseBody));

                    fname = obj.getString("firstName");
                    lname = obj.getString("lastName");
                    obj.getString("password");
                    streetAddress = obj.getString("streetAddress");
                    state = obj.getString("state");
                    zipcode = obj.getString("zipcode");
                    phone = obj.getString("phone");
                    email = obj.getString("email");
                    city = obj.getString("city");
                    d_name = obj.getString("d_name");
                    d_id = obj.getString("d_id");
                    JSONArray allergy = (JSONArray) obj.get("allergy");
                    List<String> all = new ArrayList<String>();
                    if (allergy != null && allergy.length() > 1) {
                        for (int i = 0; i < allergy.length(); i++) {
                            all.add(allergy.get(i).toString());
                        }
                    }
                    allergies = all;
                    JSONArray disease = (JSONArray) obj.get("disease");
                    List<String> dis = new ArrayList<String>();
                    if (disease != null && disease.length() > 1) {
                        for (int i = 0; i < disease.length(); i++) {
                            dis.add(disease.get(i).toString());
                        }
                    }
                    diseases = dis;


                    d_mail_id = obj.getString("d_mail_id");

                } catch (JSONException e) {
                    e.printStackTrace();
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
        TextView u = (TextView) findViewById(R.id.welcome);
        u.setText("Welcome " + fname + "!");
        intent.putExtra("fname", fname);

        TextView m = (TextView) findViewById(R.id.display_email);
        m.setText("Email: " + email);
        intent.putExtra("email", email);

        TextView f = (TextView) findViewById(R.id.fname);
        f.setText("First Name: " + fname);
        intent.putExtra("fname", fname);

        TextView l = (TextView) findViewById(R.id.lname);
        l.setText("Last Name: " + lname);
        intent.putExtra("lname", lname);

        TextView b = (TextView) findViewById(R.id.ContactInfo);

        TextView street = (TextView) findViewById(R.id.streetAddress);
        street.setText("Street Address : "+ streetAddress);
        intent.putExtra("streetAddress", streetAddress);

        TextView c = (TextView) findViewById(R.id.city);
        c.setText("city : "+ city);
        intent.putExtra("city", city);

        TextView st = (TextView) findViewById(R.id.state);
        st.setText("State : "+ state);
        intent.putExtra("state", state);

        TextView zip = (TextView) findViewById(R.id.zipcode);
        zip.setText("Zipcode : "+ zipcode);
        intent.putExtra("zipcode", zipcode);

        TextView al = (TextView) findViewById(R.id.allergies);
        al.setText("Allergies: " + allergies.toString());
        intent.putStringArrayListExtra("Allergies", (ArrayList<String>) allergies);

        TextView dis = (TextView) findViewById(R.id.diseases);
        dis.setText("Diseases: " + diseases.toString());
        intent.putStringArrayListExtra("Diseases", (ArrayList<String>) diseases);

        TextView doc = (TextView) findViewById(R.id.DoctorInfo);
        doc.setText("Doctor Info: " + d_id + d_name + d_mail_id);
        intent.putExtra("DoctorInfo", d_id+d_name+d_mail_id);

        TextView p = (TextView) findViewById(R.id.phone);
        p.setText("Phone: " + phone);
        intent.putExtra("phone", phone);




        ImageView img = (ImageView) findViewById(R.id.profile_pic);
        /*if (gender.equals("F")) {
            g.setText("Gender: Female");
            img.setImageResource(R.drawable.female);
        } else {
            g.setText("Gender: Male");
            img.setImageResource(R.drawable.male);
        }*/
    }


    public void edit_profile(View view) throws MalformedURLException, JSONException {
        startActivity(intent);
    }

}