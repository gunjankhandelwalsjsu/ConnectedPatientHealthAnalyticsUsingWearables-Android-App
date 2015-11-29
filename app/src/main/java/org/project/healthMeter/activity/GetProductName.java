package org.project.healthMeter.activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.project.healthMeter.R;
import org.project.healthMeter.presenter.ScannerPresenter;
import org.project.healthMeter.tools.ProductFood;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class GetProductName extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String email;
    ScannerPresenter presenter;
    EditText food;
    String foodString = "";
    String appId = "cc80ff57";
    String appKey = "51f609e7bcad546f868c41258213e46d";
    ListView lv;
    List<ProductFood> products=new ArrayList<ProductFood>();
    String sugarConsumed=" ";
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_product_name);
        setProgressBarIndeterminate(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        lv = (ListView) findViewById(R.id.listView);
        food = (EditText) findViewById(R.id.food);


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        email = sharedpreferences.getString("email", "NA");
        presenter=new ScannerPresenter(this);

        FloatingActionButton button = (FloatingActionButton)findViewById(R.id.SearchActButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DownloadWebPageTask task = new DownloadWebPageTask();
                foodString = food.getText().toString();
                foodString.replace(" ","");
                String URL = "https://api.nutritionix.com/v1_1/search/" + foodString + "?results=0%3A20&cal_min=0&cal_max=50000&fields=item_name%2Cbrand_name%2Citem_name%2Cnf_calories%2Cnf_sugars%2Cnf_ingredient_statement&appId=" + appId + "&appKey=" + appKey;
                task.execute(new String[]{URL});
                final List<String> list = new ArrayList<String>();
                lv.setAdapter(adapter);
                for (int i = 0; i < products.size(); ++i) {
                    list.add(products.get(i).getBrand_name());

                }

                adapter = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_list_item_1, list);
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,
                                            int position, long id) {
                        final String item = (String) parent.getItemAtPosition(position);



                        JSONObject jsonParams = new JSONObject();
                        //       jsonParams.put("UserPicture", img_path);
                        try {
                            jsonParams.put("item_name", products.get(position).getItem_name());
                            jsonParams.put("brand_name", products.get(position).getBrand_name());
                            jsonParams.put("nf_calories", products.get(position).getNf_calories());
                            jsonParams.put("nf_ingredient_statement", products.get(position).getNf_ingredient_statement());
                            sugarConsumed = " "+products.get(position).getNf_sugars()+"g";
                            jsonParams.put("nf_sugars", products.get(position).getNf_sugars());


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

                        sendFoodData(entity,getApplicationContext());
                        Log.d("item", item);
                    }

                });

            }
        });

    }





    public void sendFoodData(StringEntity entity, final Context context) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(this, "http://52.6.111.205:8080/webapp-master/nutritionixApi/" + email, entity, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseStr=" ";
                try {

                    if (responseBody != null) {
                        responseStr = new String(responseBody);
                        Log.d("Response", responseStr);

                    }


                    JSONObject obj = new JSONObject(responseStr);

                    Log.d("re","here");
                    String productName = obj.getString("productName");

                    if (productName.equals("Product not found")) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                GetProductName.this);

                        // set title
                        alertDialogBuilder.setTitle("Product Not Found ");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Product not found in database")
                                .setCancelable(false)
                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        getFragmentManager().popBackStack();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                        return;

                    }


                    final String nutriments = obj.getString("nutriments");


                    /**************************************************************************************/
                    String sugarResult = obj.getString("sugarResult");
                    final String Allergyresult = obj.getString("allergyResult");


                    if (sugarResult.contains("Exceeded the daily intake")) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GetProductName.this);

                        // set title
                        alertDialogBuilder.setTitle("Sugar Alert");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage(sugarResult)
                                .setCancelable(false)
                                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        callAllergyDialog(Allergyresult, nutriments);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, just close
                                        // the
                                        // dialog box and do nothing
                                        getFragmentManager().popBackStack();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();


                    } else {
                        callAllergyDialog(Allergyresult, nutriments);
                    }

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


                    /***************************************************************************************/

//////////////////////////////////////////////////////////////////////////////////


                    //     foodDiseaseInfoText.setText("Disease info: " + "need to fetch");
                    //////////////////////////////////////////////////////////////////////////////////

                  /*  if (!obj.has("nutriments"))
                        nutritionInfoText.setText("Nutrition info: " + "NA");
                    else
                        nutritionInfoText.setText("Nutrition info: " + builder.toString());*/

//////////////////////////////////////////////////////////////////////////////////
                    JSONArray pDiseaselist;


                    pDiseaselist = obj.getJSONArray("patientDisease");
                    List<String> dis = new ArrayList<String>();
                    if (pDiseaselist != null && pDiseaselist.length() != 0) {
                        for (int i = 0; i < pDiseaselist.length(); i++) {
                            Log.d("in converter", pDiseaselist.get(i).toString());
                            dis.add(pDiseaselist.get(i).toString());
                        }
                    }
                    if(sugarConsumed.equals("null"))
                        sugarConsumed=" Not available.";
                    presenter.addValueTodb(productName, all.toString(), dis.toString(), Allergyresult, sugarConsumed);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void callAllergyDialog(String Allergyresult, final String nutriments) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GetProductName.this);
        Log.d("allergyyyyyy", Allergyresult);

        if (Allergyresult.equals("No information available")) {

            alertDialogBuilder.setTitle("Ingredient information is missing");

            if (!nutriments.equals("No information available")) {
                alertDialogBuilder
                        .setMessage(" ")
                        .setCancelable(false)

                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the
                                // dialog box and do nothing
                                getFragmentManager().popBackStack();
                            }
                        });
            } else {
                alertDialogBuilder
                        .setMessage("Nutrition Information is not available")
                        .setCancelable(false)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the
                                // dialog box and do nothing
                                getFragmentManager().popBackStack();
                            }
                        });
            }

        } else  // set title
        {
            alertDialogBuilder.setTitle("Allergy Information");

            // set dialog message
            if (!nutriments.equals("No information available")) {

                alertDialogBuilder
                        .setMessage(Allergyresult)
                        .setCancelable(false)
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the
                                // dialog box and do nothing
                                getFragmentManager().popBackStack();
                            }
                        });
            }
        }
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }



    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            JSONObject obj = new JSONObject();
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                  Log.d("res",response);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONArray hits =jsonObj.getJSONArray("hits");
                Log.d("hitsLen",String.valueOf(hits.length()));
                for (int i = 0; i<hits.length(); i++) {
                    ProductFood product=new ProductFood();
                    JSONObject hit = hits.getJSONObject(i);
                    JSONObject field = hit.getJSONObject("fields");
                    String item=field.getString("item_name");
                 //   Log.d(item,"//");
                    product.setItem_name(field.getString("item_name"));
                   // Log.d("product", products.get(i).getBrand_name());

                    product.setBrand_name(field.getString("brand_name"));
                    product.setNf_calories(field.getString("nf_calories"));
                    product.setNf_sugars(field.getString("nf_sugars"));
                    product.setNf_ingredient_statement(field.getString("nf_ingredient_statement"));
                    products.add(product);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}


