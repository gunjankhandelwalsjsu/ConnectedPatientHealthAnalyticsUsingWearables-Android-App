package org.project.healthMeter.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.project.healthMeter.R;
import org.project.healthMeter.presenter.ScannerPresenter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ViewForScannerActivity extends ActionBarActivity {
    TextView productNameText, foodAllergicInfoText, foodDiseaseInfoText, nutritionInfoText,patientAllergyText;
    private static final int Simple_Scanner_Activity = 1;
    private static final int Scanner_Activity = 2;
    TextView etResponse;
    Button button;
    Button button1;
    String barcode;
    String email;
    public static final String MyPREFERENCES = "MyPrefs" ;

    SharedPreferences sharedpreferences;
    ScannerPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_for_scanner);
        setProgressBarIndeterminate(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        barcode = extras.getString("barcode");
        //email = extras.getString("email");
//        Log.d(email,email);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        email= sharedpreferences.getString("email", "NA");
        Log.d("email from scannerPr",email);

        productNameText = (TextView) findViewById(R.id.productName);
        patientAllergyText=(TextView) findViewById(R.id.patientAllergy);
        foodAllergicInfoText = (TextView) findViewById(R.id.foodAllergicInfo);
        foodDiseaseInfoText = (TextView) findViewById(R.id.foodDiseaseInfo);
        nutritionInfoText = (TextView) findViewById(R.id.nutritionInfo);

        fetch_foodData();

        presenter =new ScannerPresenter(this);

        //   etResponse.setText(barcode);

    }


    public void fetch_foodData() {


        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://10.0.0.12:8080/webapp/food/" + email + "/" + barcode, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    if (responseBody != null) {
                        String responseStr = new String(responseBody);
                        Log.d("Response", responseStr);

                    }
                    String json = new String(responseBody, "UTF8");
                    Log.d("json", json);
                    JSONObject obj = new JSONObject(json);
                    String productName = obj.getString("productName");
                    String brand = obj.getString("brand");



              /**************************************************************************************/
                    String Allergyresult = obj.getString("allergyResult");
                    JSONArray pAllergylist;



                    pAllergylist = obj.getJSONArray("patientAllergy");
                    List<String> all = new ArrayList<String>();
                    if (pAllergylist != null && pAllergylist.length() != 0) {
                        for (int i = 0; i < pAllergylist.length(); i++) {
                            System.out.println("in converter" + pAllergylist.get(i).toString());
                            all.add(pAllergylist.get(i).toString());
                        }
                    }
                /******************************************************************************/

                    String nutriments = obj.getString("nutriments");
                    String[] nut = nutriments.toString().split(",");
                    StringBuilder builder = new StringBuilder();
                    String sugarConsumed = "";

                    for (int i = 0; i < nut.length; i++) {
                        nut[i] = nut[i].replace("\"", " ");
                        nut[i] = nut[i].replace("{", " ");
                        nut[i] = nut[i].replace("}", " ");
                        if(nut[i].contains("sugar"))
                            sugarConsumed=nut[i];
                        builder.append(nut[i] + "\n");
                        Log.d(String.valueOf(i), nut[i]);
                    }

                /***************************************************************************************/

                        productNameText.setText("Brand : " + brand + "  Product Name: " + productName);
//////////////////////////////////////////////////////////////////////////////////
                    if (((pAllergylist != null) && pAllergylist.length() != 0)) {
                        patientAllergyText.setText("allergy" + all.toString());
                    } else
                        patientAllergyText.setText("NA");
                    //////////////////////////////////////////////////////////////////////////////////


                    if (obj.has("allergyResult") || ((Allergyresult != null) && Allergyresult.length() != 0)) {
                        foodAllergicInfoText.setText("Food Allergy info: " + Allergyresult);
                    } else
                        foodAllergicInfoText.setText("Food Allergy info: " + "NA");
//////////////////////////////////////////////////////////////////////////////////

                    foodDiseaseInfoText.setText("Disease info: " + "need to fetch");
                    //////////////////////////////////////////////////////////////////////////////////

                    if (!obj.has("nutriments"))
                        nutritionInfoText.setText("Nutrition info: " + "NA");
                    else
                        nutritionInfoText.setText("Nutrition info: " + builder.toString());
//////////////////////////////////////////////////////////////////////////////////

                    presenter.addValueTodb(productName,all.toString(),"needToFetch",Allergyresult,sugarConsumed);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
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
}



