package org.project.healthMeter.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.project.healthMeter.R;
import org.project.healthMeter.tools.Utility;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


/**
 *
 * Register Activity Class
 */
public class RegistrationActivity extends Activity {
    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // FirstName Edit View Object
    EditText fNameET;
    // LastName Edit View Object
    EditText lNameET;
    // Email Edit View Object
    EditText emailET;
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





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        // Find Error Msg Text View control by ID
        errorMsg = (TextView) findViewById(R.id.register_error);
        // Find FirstName Edit View control by ID
        fNameET = (EditText) findViewById(R.id.registerfName);
        // Find LastName Edit View control by ID
        lNameET = (EditText) findViewById(R.id.registerlName);
        // Find Email Edit View control by ID
        emailET = (EditText) findViewById(R.id.registerEmail);
        // Find Password Edit View control by ID
        pwdET = (EditText) findViewById(R.id.registerPassword);


        phoneET = (EditText) findViewById(R.id.phone);
        streetET = (EditText) findViewById(R.id.streetAddress);
        stateET = (EditText) findViewById(R.id.state);
        cityET = (EditText) findViewById(R.id.city);
        zipcodeET = (EditText) findViewById(R.id.zipcode);

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
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

        }
    }



    /**
     * Method gets triggered when Register button is clicked
     *
     * @param view
     */
    public void registerUser(View view) throws UnsupportedEncodingException, JSONException {
        // Get FirstNAme ET control value
        String fName = fNameET.getText().toString();
        // Get NAme ET control value
        String lName = fNameET.getText().toString();
        // Get Email ET control value
        String email = emailET.getText().toString();
        // Get Password ET control value
        String password = pwdET.getText().toString();
        String phone = phoneET.getText().toString();
        String streetAddress = streetET.getText().toString();
        String city = cityET.getText().toString();
        String state = stateET.getText().toString();
        String zipcode = zipcodeET.getText().toString();


        JSONObject jsonParams = new JSONObject();


        // Instantiate Http Request Param Object
     //   RequestParams params = new RequestParams();
        // When Name Edit View, Email Edit View and Password Edit View have values other than Null
        if(Utility.isNotNull(fName) && Utility.isNotNull(lName)&&Utility.isNotNull(email) && Utility.isNotNull(password)&& Utility.isNotNull(phone)&& Utility.isNotNull(streetAddress)&& Utility.isNotNull(city)&& Utility.isNotNull(zipcode)&& Utility.isNotNull(state)){
            // When Email entered is Valid
            if(Utility.validate(email)){
                // Put Http parameter name with value of Name Edit View control
          /*      params.put("firstName", fName);
                params.put("lastName", lName);
                params.put("password", password);

                // Put Http parameter username with value of Email Edit View control
                params.put("email", email);
                // Put Http parameter password with value of Password Edit View control
                params.put("phone", phone);
                params.put("streetAddress", streetAddress);
                params.put("state", state);
                params.put("city", city);
                params.put("zipCode", zipcode);
                params.put("allergy", allergyList);*/
           //     params.put("Disease", diseaseList);


                jsonParams.put("firstName", fName);
                jsonParams.put("lastName", lName);
                jsonParams.put("password", password);

                // Put Http parameter username with value of Email Edit View control
                jsonParams.put("email", email);
                // Put Http parameter password with value of Password Edit View control
                jsonParams.put("phone", phone);
                jsonParams.put("streetAddress", streetAddress);
                jsonParams.put("state", state);
                jsonParams.put("city", city);
                jsonParams.put("zipCode", zipcode);
                JSONArray alist=new JSONArray();
                for (int i = 0; i < allergyList.size(); i++) {
                    alist.put(allergyList.get(i));
                }
                JSONArray dlist=new JSONArray();
                for (int i = 0; i < diseaseList.size(); i++) {
                    dlist.put(diseaseList.get(i));
                }
                jsonParams.put("allergy", alist);
                jsonParams.put("disease", dlist);

                //   jsonParams.put("notes", "Test api support");
                StringEntity entity = new StringEntity(jsonParams.toString());
                Log.d("json", String.valueOf(jsonParams));
                entity.setContentType(String.valueOf(new BasicHeader(HTTP.CONTENT_TYPE, "application/json")));


                // Invoke RESTful Web Service with Http parameters
                invokeWS(entity);
                Intent myIntent;
                myIntent = new Intent(this, ProfileActivity.class);
                myIntent.putExtra("email", email);
                startActivity(myIntent);
            }
            // When Email is invalid
            else{
                Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
            }
        }
        // When any of the Edit View control left blank
        else{
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param
     */
    public void invokeWS(StringEntity entity){
        // Show Progress Dialog
        prgDialog.show();
        Log.d("y so", "manyyyyyyyyy");

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(this,"http://10.0.0.12:8080/webapp/patient", entity,"application/json", new AsyncHttpResponseHandler() {
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
                        setDefaultValues();
                        // Display successfully registered message using Toast
                        Toast.makeText(getApplicationContext(), "You are successfully registered!", Toast.LENGTH_LONG).show();
                    }
                    // Else display error message
                    else {
                        errorMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
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



    /**
     * Method which navigates from Register Activity to Login Activity
     */
    public void navigatetoLoginActivity(View view){
        Intent loginIntent = new Intent(getApplicationContext(),LoginActivity.class);
        // Clears History of Activity
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    /**
     * Set degault values for Edit View controls
     */
    public void setDefaultValues(){
        fNameET.setText("");
        lNameET.setText("");
        emailET.setText("");
        pwdET.setText("");
        streetET.setText("");
        phoneET.setText("");
        stateET.setText("");
        cityET.setText("");
        zipcodeET.setText("");


    }

}
