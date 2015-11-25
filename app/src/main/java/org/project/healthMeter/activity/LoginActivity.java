package org.project.healthMeter.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.project.healthMeter.R;
import org.project.healthMeter.tools.Utility;
import org.project.healthMeter.db.User;
import org.project.healthMeter.presenter.LoginPresenter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class LoginActivity extends AppCompatActivity {

    Button b1,b2,b3;
    EditText ed1,ed2;

    TextView tx1;

    int counter = 3;
    /***********/


    View firstView;
    View EULAView;
    CheckBox EULACheckbox;
    Button startButton;
    TextView termsTextView;
    LoginPresenter presenter;

    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    // Email Edit View Object
    EditText emailET;
    // Passwprd Edit View Object
    EditText pwdET;
    String email;
    String password;
    String emailv,id;
    User user;
    public static final String MyPREFERENCES = "MyPrefs" ;


    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        presenter = new LoginPresenter(this);

        firstView = (ScrollView) findViewById(R.id.helloactivity_mainframe);
        EULAView = (ScrollView) findViewById(R.id.helloactivity_eulaframe);
        EULACheckbox = (CheckBox) findViewById(R.id.helloactivity_checkbox_eula);
        b1=(Button)findViewById(R.id.helloactivity_nextlogin);
        b3=(Button)findViewById(R.id.helloactivity_register);

        startButton = (Button) findViewById(R.id.helloactivity_start);

        b2=(Button)findViewById(R.id.button_cancel);

        errorMsg = (TextView)findViewById(R.id.login_error);
        // Find Email Edit View control by ID
        ed1 = (EditText)findViewById(R.id.editText_email);
        // Find Password Edit View control by ID
        ed2 = (EditText)findViewById(R.id.editText_password);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);



      /*  final Drawable greyArrow = getApplicationContext().getResources().getDrawable(R.drawable.ic_navigate_next_grey_24px);
        greyArrow.setBounds(0, 0, 60, 60);
        final Drawable pinkArrow = getApplicationContext().getResources().getDrawable(R.drawable.ic_navigate_next_pink_24px);
        pinkArrow.setBounds(0, 0, 60, 60);*/
        EULACheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                        if (isChecked) {
                                                            startButton.setEnabled(true);
                                                            //             startButton.setCompoundDrawables(null, null, pinkArrow, null);
                                                        } else {
                                                            startButton.setEnabled(false);
                                                            //     startButton.setCompoundDrawables(null, null, greyArrow, null);
                                                        }
                                                    }
                                                }
        );



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    loginUser();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("email", email);
                    editor.commit();



                    //   user=new User(id,emailv);
                //    presenter.loadDatabase(id, emailv);

                 //   presenter.loadDatabase(id, email);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                /*
                    Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
                    presenter.onNextClicked();

                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();

                    tx1.setVisibility(View.VISIBLE);
                    tx1.setBackgroundColor(Color.RED);
                    counter--;
                    tx1.setText(Integer.toString(counter));

                    if (counter == 0) {
                        b1.setEnabled(false);
                    }
                }*/
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //TODO: add Preferred Unit and Diabetes Type in dB
    }

    public void onNextClicked(View v){
        presenter.onNextClicked();
    }

    public void showEULA(){
        // Prepare the View for the animation
        firstView.animate()
                .alpha(0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        firstView.setVisibility(View.GONE);
                        showEULAAnimation();
                    }
                });
    }

    private void showEULAAnimation() {
        // Prepare the View for the animation
        EULAView.setVisibility(View.VISIBLE);
        EULAView.setAlpha(0.0f);

        EULAView.animate()
                .alpha(1f);
        firstView.setVisibility(View.GONE);
    }


    public void onStartClicked(View v){
        presenter.saveToDatabase();
    }
    public void closeHelloActivity(){

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("email",email);
        startActivity(intent);
        finish();
    }

    public void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    public void displayErrorMessage(){
        Toast.makeText(getApplicationContext(), getString(R.string.helloactivity_age_invalid), Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }


/********************************************************************************/
public void loginUser() throws JSONException, UnsupportedEncodingException {
    // Get Email Edit View Value
    email = ed1.getText().toString();
    Log.d("email",email);
    // Get Password Edit View Value
    password = ed2.getText().toString();
    // Instantiate Http Request Param Object
    RequestParams params = new RequestParams();
    // When Email Edit View and Password Edit View have values other than Null
    if(Utility.isNotNull(email) && Utility.isNotNull(password)){
        // When Emp
        // ail entered is Valid
        if(Utility.validate(email)){
            // Put Http parameter username with value of Email Edit View control
            JSONObject jsonParams = new JSONObject();

            jsonParams.put("email", email);
            // Put Http parameter password with value of Password Edit Value control
            jsonParams.put("password", password);
            // Invoke RESTful Web Service with Http parameters
            StringEntity entity = new StringEntity(String.valueOf(jsonParams));
            Log.d("json", String.valueOf(jsonParams));
            entity.setContentType(String.valueOf(new BasicHeader(HTTP.CONTENT_TYPE, "application/json")));
            invokeWS(entity);
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
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(this,"http://192.168.43.191:8080/webapp/login", entity,"application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(String.valueOf(responseBody));
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("status")) {
                        Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        // Navigate to Home screen
                       emailv=obj.getString("email");
                        id=obj.getString("id");
                        navigatetoMainActivity();
                    }
                    // Else display error message
                    else {
                        errorMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    navigatetoMainActivity();
                 //   Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
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
    public void navigatetoMainActivity(){
        Intent mainIntent = new Intent(getApplicationContext(),MainActivity.class);
       mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.putExtra("email", email);
        startActivity(mainIntent);
    }
    /**
     * Method gets triggered when Register button is clicked
     *
     * @param view
     */




}

