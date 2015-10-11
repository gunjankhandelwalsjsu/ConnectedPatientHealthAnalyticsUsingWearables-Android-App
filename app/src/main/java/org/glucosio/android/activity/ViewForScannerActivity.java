package org.glucosio.android.activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.glucosio.android.R;
import org.glucosio.android.fragment.PreScannerFragment;
import org.glucosio.android.fragment.ScannerFragment;

public class ViewForScannerActivity extends FragmentActivity {
    TextView tvResult;
    private static final int Simple_Scanner_Activity = 1;
    private static final int Scanner_Activity = 2;
    TextView etResponse;
    Button button;
    Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_for_scanner);
        Bundle extras = getIntent().getExtras();
        String message = extras.getString("message");
        tvResult = (TextView) findViewById(R.id.tvResult);
        etResponse = (TextView) findViewById(R.id.etResponse);
        etResponse.setText(message);
       button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

        });





    }



}
