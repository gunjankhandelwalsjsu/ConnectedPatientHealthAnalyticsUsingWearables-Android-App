package org.glucosio.android.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import org.glucosio.android.R;
import org.glucosio.android.adapter.HomePagerAdapter;
import org.glucosio.android.db.DatabaseNewHandler;
import org.glucosio.android.presenter.MainPresenter;
import org.glucosio.android.tools.LabelledSpinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity {

    LabelledSpinner spinnerReadingType;
    Dialog addDialog;

    TextView dialogCancelButton;
    TextView dialogAddButton;
    TextView dialogAddTime;
    TextView dialogAddDate;
    TextView dialogReading;
    HomePagerAdapter homePagerAdapter;
    SharedPreferences sharedpreferences;


    private MainPresenter presenter;
    static final String RESULT_KEY = "RESULT_KEY";
    TextView tvResult;
    private static final int Simple_Scanner_Activity = 1;
    private static final int Scanner_Activity = 2;
    TextView etResponse;
    String email;
    DatabaseNewHandler db;
    public static final String MyPREFERENCES = "MyPrefs" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(this);
    //    getSupportActionBar().hide();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
    //    Intent intent = getIntent();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

   //    email = intent.getStringExtra("email");
    //    Intent i=getIntent();
     //   String emailNew = i.getStringExtra("emailn");
    //    if(email.isEmpty()&&!emailNew.isEmpty())
     //       email=emailNew;

//        Log.d("email",email);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setElevation(0);
        }

        homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), getApplicationContext(), email);

        viewPager.setAdapter(homePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    hideFabAnimation();
                    LinearLayout emptyLayout = (LinearLayout) findViewById(R.id.mainactivity_empty_layout);
                    ViewPager pager = (ViewPager) findViewById(R.id.pager);
                    if (pager.getVisibility() == View.GONE) {
                        pager.setVisibility(View.VISIBLE);
                        emptyLayout.setVisibility(View.INVISIBLE);
                    }
                } else {
                    showFabAnimation();
                    checkIfEmptyLayout();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        checkIfEmptyLayout();


        // Set fonts
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/lato.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }


    public void onFabClicked(View v) {
        Intent intent = new Intent(this, SensorTagActivity.class);
        startActivity(intent);
    }

    public void startHelloActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }



    public void startGittyReporter() {
        Intent intent = new Intent(this, GittyActivity.class);
        startActivity(intent);
        finish();
    }


    public void showErrorMessage() {
        Toast.makeText(getApplicationContext(), getString(R.string.dialog_error), Toast.LENGTH_SHORT).show();
    }


    public CoordinatorLayout getFabView() {
        return (CoordinatorLayout) findViewById(R.id.coordinator_layout);
    }

    public void reloadFragmentAdapter() {
        homePagerAdapter.notifyDataSetChanged();
    }

    public Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }


    private void hideFabAnimation() {
        final View fab = (View) findViewById(R.id.main_fab);
        fab.animate()
                .translationY(-5)
                .alpha(0.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        fab.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void showFabAnimation() {
        final View fab = (View) findViewById(R.id.main_fab);
        if (fab.getVisibility() == View.INVISIBLE) {
            // Prepare the View for the animation
            fab.setVisibility(View.VISIBLE);
            fab.setAlpha(0.0f);

            fab.animate().alpha(1f).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    fab.setVisibility(View.VISIBLE);
                }
            });
        } else {
            // do nothing
            // probably swiping from OVERVIEW to HISTORY tab
        }
    }


    public void checkIfEmptyLayout(){
        LinearLayout emptyLayout = (LinearLayout) findViewById(R.id.mainactivity_empty_layout);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        if (presenter.isdbEmpty()) {
            pager.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (getResources().getConfiguration().orientation == 1) {
                    // If Portrait choose vertical curved line
                    ImageView arrow = (ImageView) findViewById(R.id.mainactivity_arrow);
                //    arrow.setBackground(getResources().getDrawable(R.drawable.curved_line_vertical));
                } else {
                    // Else choose horizontal one
                    ImageView arrow = (ImageView) findViewById(R.id.mainactivity_arrow);
                 //   arrow.setBackground((getResources().getDrawable(R.drawable.curved_line_horizontal)));
                }
            }
        } else {
            pager.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_feedback) {
            return true;
        } else if (id==R.id.action_profile)
        {
            Intent myIntent;
            myIntent = new Intent(this, ProfileActivity.class);
            myIntent.putExtra("email", email);
            startActivity(myIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}