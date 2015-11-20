package org.project.healthMeter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.project.healthMeter.R;


public class LicenceActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licence);
        WebView webView = (WebView) findViewById(R.id.webview_licence);

        Bundle p;
        String url;

        // Check if extras are available. If not, show terms by default
        if (getIntent().getExtras() != null) {
            p = getIntent().getExtras();
            url = p.getString("key");
        } else {
            url = "terms";
        }


        if (url != null && url.equals("privacy")) {
            webView.loadUrl("http://10.0.0.12:8080/MongoDBWebapp/contact.jsp");
            getSupportActionBar().setTitle(getResources().getString(R.string.preferences_privacy));
        } else if (url != null && url.equals("contactUs")) {
            webView.loadUrl("http://10.0.0.12:8080/MongoDBWebapp/contact.jsp");
            getSupportActionBar().setTitle(getResources().getString(R.string.preferences_contactUs));
        } else {
            webView.loadUrl("http://10.0.0.12:8080/MongoDBWebapp/contact.jsp");
            getSupportActionBar().setTitle(getResources().getString(R.string.preferences_terms));
        }

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }


    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}


