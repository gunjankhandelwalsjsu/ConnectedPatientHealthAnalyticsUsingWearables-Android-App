package org.project.healthMeter.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.project.healthMeter.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by rajeshkhandelwal on 11/19/15.
 */

public class SpinnerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    JSONArray arr; SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String pEmail;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);

        addItemsOnSpinner2();
    }


    // add items into spinner dynamically
    public void addItemsOnSpinner2() {
        final List<String> list = new ArrayList<String>();
        list.add("Select one");
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        pEmail = sharedpreferences.getString("email", "NA");
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(this, "http://10.0.0.12:8080/webapp/addDoctorToPatientProfile/list", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    if (responseBody != null) {
                        String responseStr = new String(responseBody);
                        Log.d("Response", responseStr);

                    }
                    arr = new JSONArray(new String(responseBody));


                    if (arr != null && arr.length() > 0) {
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject d = (JSONObject) arr.get(i);
                            list.add(d.getString("firstName") + d.getString("lastName") + " -" + d.getString("email"));
                        }
                    } else {
                        list.add("no doctors");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        spinner = (Spinner) findViewById(R.id.spinner);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        final String emailQ = item.substring(item.indexOf("-") + 1);

        Log.d("email", emailQ);

        if (arr != null && arr.length() > 0) {
            for (int i = 0; i < arr.length(); i++) {
                try {
                    JSONObject d = (JSONObject) arr.get(i);
                    if ((d.getString("email")).equals(emailQ)) {


                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                        // set title
                        alertDialogBuilder.setTitle("Doctor " + d.getString("firstName") + " Information");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Name:" + d.getString("firstName") + d.getString("lastName") + "\nAddress:" + d.getString("streetAddress") + "\n" + d.getString("state") + "\n" + d.getString("city") + "\n" + d.getString("zipCode") + "\nPhone:" + d.getString("phone") + "\nSpecialization:" + d.getString("specialization"))
                                .setCancelable(false)
                                .setPositiveButton("Add Doctor", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        JSONObject jsonParams = new JSONObject();
                                        //       jsonParams.put("UserPicture", img_path);
                                        try {
                                            jsonParams.put("doctorEmail", emailQ);
                                            jsonParams.put("patientEmail", pEmail);


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        StringEntity entity = null;
                                        try {
                                            entity = new StringEntity(jsonParams.toString());
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("json print", String.valueOf(jsonParams));
                                      //  entity.setContentType(String.valueOf(new BasicHeader(HTTP.CONTENT_TYPE, "application/json")));

                                        entity.setContentType(String.valueOf(new BasicHeader(HTTP.CONTENT_TYPE, "application/json")));
                                        addDoctor(entity);

                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dialog.cancel();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Showing selected spinner item}
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    void addDoctor(StringEntity entity) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(this, "http://10.0.0.12:8080/webapp/addDoctorToPatientProfile", entity, "application/json", new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Context context = getApplicationContext();
                CharSequence text = "Doctor added successfully!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {


            }
        });

    }
}