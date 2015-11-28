package org.project.healthMeter.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.project.healthMeter.R;
import org.project.healthMeter.presenter.ScannerPresenter;
import org.project.healthMeter.tools.ProductFood;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class GetProductFragment extends Fragment {
    public static final String MyPREFERENCES = "MyPrefs";

    SharedPreferences sharedpreferences;
    String email;
    ScannerPresenter presenter;
    EditText food;
    private OnFragmentInteractionListener mListener;
    String foodString = " ";
    String appId = "cc80ff57";
    String appKey = "51f609e7bcad546f868c41258213e46d";
    ListView lv;
    List<ProductFood> products;

    public static GetProductFragment newInstance() {
        GetProductFragment fragment = new GetProductFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        email = sharedpreferences.getString("email", "NA");

        View v = inflater.inflate(R.layout.fragment_get_product, container, false);
        food = (EditText) v.findViewById(R.id.note);
        foodString = food.getText().toString();
        lv = (ListView) v.findViewById(R.id.listView);
        products = new ArrayList<ProductFood>();

        fetch_foodData();
        ArrayAdapter<ProductFood> adapter = new ArrayAdapter<ProductFood>
                (getActivity(), android.R.layout.simple_list_item_1, products);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String item = ((TextView) view).getText().toString();


                Toast.makeText(getContext(), item, Toast.LENGTH_LONG).show();

            }
        });
        return v;

    }

    public void searchButton(View view) {
        fetch_foodData();

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }


    public void fetch_foodData() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestHandle requestHandle = client.get("https://api.nutritionix.com/v1_1/search/" + foodString + "?results=0%3A20&cal_min=0&cal_max=50000&fields=item_name%2Cbrand_name%2Citem_name%2Cnf_calories%2Cnf_sugars%2Cnf_ingredient_statement&appId=" + appId + "&appKey=" + appKey
                , new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    if (responseBody != null) {
                        String responseStr = new String(responseBody);
                        Log.d("Response", responseStr);

                    }

                    JSONArray obj = new JSONArray(new String(responseBody));
                    for (int i = 0; i < obj.length(); i++) {
                        JSONObject json = obj.getJSONObject(i);
                        products.get(i).setItem_name(json.getString("item_name"));
                        json.getString("brand_name");
                        json.getString("nf_calories");
                        json.getString("nf_sugars");
                        json.getString("nf_ingredient_statement");

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
              /*  String productName = obj.getString("productName");
                String sugarConsumed = " ";

                if (productName.equals("ProductFood not found")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getActivity());

                    // set title
                    alertDialogBuilder.setTitle("ProductFood Not Found ");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("ProductFood not found in database")
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
            /*    String sugarResult = obj.getString("sugarResult");
                final String Allergyresult = obj.getString("allergyResult");


                if (sugarResult.contains("Exceeded the daily intake")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getActivity());

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
                    sugarConsumed = callAllergyDialog(Allergyresult, nutriments);
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

                JSONArray pDiseaselist;


                pDiseaselist = obj.getJSONArray("patientDisease");
                List<String> dis = new ArrayList<String>();
                if (pDiseaselist != null && pDiseaselist.length() != 0) {
                    for (int i = 0; i < pDiseaselist.length(); i++) {
                        Log.d("in converter", pDiseaselist.get(i).toString());
                        dis.add(pDiseaselist.get(i).toString());
                    }
                }
                presenter.addValueTodb(productName, all.toString(), dis.toString(), Allergyresult, sugarConsumed);

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("statusCode", String.valueOf(statusCode));
                // When Http response code is '404'
                if (statusCode == 404 || statusCode == 405) {
                    Toast.makeText(getActivity(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getActivity(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getActivity(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public String callAllergyDialog(String Allergyresult, final String nutriments) {

        final String[] sugarConsumed = {" "};

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        Log.d("allergyyyyyy", Allergyresult);

        if (Allergyresult.equals("No information available")) {

            alertDialogBuilder.setTitle("Ingredient information is missing");

            if (!nutriments.equals("No information available")) {
                alertDialogBuilder
                        .setMessage("Do you want to get Nutrition Information")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String[] nut = nutriments.toString().split(",");
                                // StringBuilder builder=new StringBuilder();

                                for (int i = 0; i < nut.length; i++) {
                                    nut[i] = nut[i].replace("\"", " ");
                                    nut[i] = nut[i].replace("{", " ");
                                    nut[i] = nut[i].replace("}", " ");
                                    if (nut[i].equals("sugars"))
                                        sugarConsumed[0] = nut[i];
                                    //  builder.append(nut[i]+"\n");
                                    Log.d(String.valueOf(i), nut[i]);
                                }

                                StringBuilder builder = new StringBuilder();
                                for (String s : nut) {
                                    builder.append(s);
                                }


                                showMessageDialog("Nutrition info = " + Arrays.deepToString(nut));
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
                        .setMessage(Allergyresult + "! Do you want to get Nutrition Information")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                //dialog.cancel();
                                String[] nut = nutriments.toString().split(",");
                                // StringBuilder builder=new StringBuilder();

                                for (int i = 0; i < nut.length; i++) {
                                    nut[i] = nut[i].replace("\"", " ");
                                    nut[i] = nut[i].replace("{", " ");
                                    nut[i] = nut[i].replace("}", " ");
                                    if (nut[i].equals("sugars"))
                                        sugarConsumed[0] = nut[i];
                                    //  builder.append(nut[i]+"\n");
                                    Log.d(String.valueOf(i), nut[i]);
                                }
                                for (String s : nut) {
                                    StringBuilder builder = new StringBuilder();

                                    builder.append(s);
                                }


                                showMessageDialog("Nutrition info = " + Arrays.deepToString(nut));
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
            } else {
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


        return sugarConsumed[0];
    }

}
*/