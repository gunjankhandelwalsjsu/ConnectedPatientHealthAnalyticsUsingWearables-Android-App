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
