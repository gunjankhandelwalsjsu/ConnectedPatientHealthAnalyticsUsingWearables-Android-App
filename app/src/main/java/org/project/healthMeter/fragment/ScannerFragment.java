package org.project.healthMeter.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.project.healthMeter.R;
import org.project.healthMeter.presenter.ScannerPresenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScannerFragment extends Fragment implements MessageDialogFragment.MessageDialogListener,
        ZBarScannerView.ResultHandler, FormatSelectorDialogFragment.FormatSelectorDialogListener,
        CameraSelectorDialogFragment.CameraSelectorDialogListener {
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";
    private ZBarScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;
    private int mCameraId = -1;
    String barcode;
    static final String RESULT_KEY = "RESULT_KEY";
    TextView tvResult;
    private static final int Simple_Scanner_Activity = 1;
    private static final int Scanner_Activity = 2;
    TextView etResponse;
    Button button;
    public static final String MyPREFERENCES = "MyPrefs" ;

    SharedPreferences sharedpreferences;
    String email;
    ScannerPresenter presenter;



    public static ScannerFragment newInstance() {
        ScannerFragment f = new ScannerFragment();
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {

        mScannerView = new ZBarScannerView(getActivity());
        if(state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
            mSelectedIndices = state.getIntegerArrayList(SELECTED_FORMATS);
            mCameraId = state.getInt(CAMERA_ID, -1);
        } else {
            mFlash = false;
            mAutoFocus = true;
            mSelectedIndices = null;
            mCameraId = -1;
        }
        setupFormats();
       // email=getArguments().getString("email");
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        email= sharedpreferences.getString("email", "NA");

        presenter =new ScannerPresenter(this);

        return mScannerView;

    }


    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItem;

        if(mFlash) {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_off);
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);


        if(mAutoFocus) {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_off);
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        menuItem = menu.add(Menu.NONE, R.id.menu_formats, 0, R.string.formats);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        menuItem = menu.add(Menu.NONE, R.id.menu_camera_selector, 0, R.string.select_camera);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_flash:
                mFlash = !mFlash;
                if(mFlash) {
                    item.setTitle(R.string.flash_on);
                } else {
                    item.setTitle(R.string.flash_off);
                }
                mScannerView.setFlash(mFlash);
                return true;
            case R.id.menu_auto_focus:
                mAutoFocus = !mAutoFocus;
                if(mAutoFocus) {
                    item.setTitle(R.string.auto_focus_on);
                } else {
                    item.setTitle(R.string.auto_focus_off);
                }
                mScannerView.setAutoFocus(mAutoFocus);
                return true;
            case R.id.menu_formats:
                DialogFragment fragment = FormatSelectorDialogFragment.newInstance(this, mSelectedIndices);
                fragment.show(getActivity().getSupportFragmentManager(), "format_selector");
                return true;
            case R.id.menu_camera_selector:
                mScannerView.stopCamera();
                DialogFragment cFragment = CameraSelectorDialogFragment.newInstance(this, mCameraId);
                cFragment.show(getActivity().getSupportFragmentManager(), "camera_selector");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
        outState.putInt(CAMERA_ID, mCameraId);
    }

    @Override
    public void handleResult(Result rawResult) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
        }
      //  showMessageDialog("Contents = " + rawResult.getContents() + ", Format = " + rawResult.getBarcodeFormat().getName());
        Log.i("Contentsssssss =", rawResult.getContents());
        barcode=rawResult.getContents();
       /* Intent intent = new Intent(getActivity(), ViewForScannerActivity.class);
        intent.putExtra("barcode", barcode);
        startActivity(intent);*/
        fetch_foodData();


       // new HttpAsyncTask().execute("http://world.openfoodfacts.org/api/v0/product/"+rawResult.getContents()+".json");

    }

    public void showMessageDialog(String message) {
        DialogFragment fragment = MessageDialogFragment.newInstance("Scan Results", message, this);
        fragment.show(getActivity().getSupportFragmentManager(), "scan_results");

    }

    public void closeMessageDialog() {
        closeDialog("scan_results");
    }

    public void closeFormatsDialog() {
        closeDialog("format_selector");
    }

    public void closeDialog(String dialogName) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DialogFragment fragment = (DialogFragment) fragmentManager.findFragmentByTag(dialogName);
        if(fragment != null) {
            fragment.dismiss();
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // Resume the camera
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onFormatsSaved(ArrayList<Integer> selectedIndices) {
        mSelectedIndices = selectedIndices;
        setupFormats();
    }

    @Override
    public void onCameraSelected(int cameraId) {
        mCameraId = cameraId;
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        if(mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<Integer>();
            for(int i = 0; i < BarcodeFormat.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for(int index : mSelectedIndices) {
            formats.add(BarcodeFormat.ALL_FORMATS.get(index));
        }
        if(mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        closeMessageDialog();
        closeFormatsDialog();
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
    public void callAllergyDialog(String Allergyresult, final String[] nut) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set title
        alertDialogBuilder.setTitle("Allergy Information");

        // set dialog message
        alertDialogBuilder
                .setMessage(Allergyresult + "! Do you want to get Nutrition Information")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        //dialog.cancel();
                        StringBuilder builder = new StringBuilder();
                        for(String s : nut) {
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

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }




    public void fetch_foodData() {

        ScannerPresenter presentert;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.43.191:8080/webapp/food/" + email + "/" + barcode, new AsyncHttpResponseHandler() {

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
                    String nutriments = obj.getString("nutriments");
                    String[] nut = nutriments.toString().split(",");
                    StringBuilder builder = new StringBuilder();
                    String sugarConsumed = "";

                    for (int i = 0; i < nut.length; i++) {
                        nut[i] = nut[i].replace("\"", " ");
                        nut[i] = nut[i].replace("{", " ");
                        nut[i] = nut[i].replace("}", " ");
                        if (nut[i].equals("sugars"))
                            sugarConsumed = nut[i];
                        builder.append(nut[i] + "\n");
                        Log.d(String.valueOf(i), nut[i]);
                    }

                    final String[] nutFinal=nut;

                    /**************************************************************************************/
                    String sugarResult = obj.getString("sugarResult");
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
                                       callAllergyDialog(Allergyresult,nutFinal);
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


                    }
                    else
                        callAllergyDialog(Allergyresult,nutFinal);










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
                    presenter.addValueTodb(productName, all.toString(), dis.toString(), Allergyresult, sugarConsumed);

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




}


