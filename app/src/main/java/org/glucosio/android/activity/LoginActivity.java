package org.glucosio.android.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.glucosio.android.R;
import org.glucosio.android.presenter.LoginPresenter;
import org.glucosio.android.tools.LabelledSpinner;


public class LoginActivity extends AppCompatActivity {

    Button b1,b2;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        presenter = new LoginPresenter(this);
        presenter.loadDatabase();

        firstView = (ScrollView) findViewById(R.id.helloactivity_mainframe);
        EULAView = (ScrollView) findViewById(R.id.helloactivity_eulaframe);
        EULACheckbox = (CheckBox) findViewById(R.id.helloactivity_checkbox_eula);
        b1=(Button)findViewById(R.id.helloactivity_nextlogin);
        ed1=(EditText)findViewById(R.id.editText_username);
        ed2=(EditText)findViewById(R.id.editText_password);
        startButton = (Button) findViewById(R.id.helloactivity_start);

        b2=(Button)findViewById(R.id.button_cancel);
        tx1=(TextView)findViewById(R.id.textView3);
        tx1.setVisibility(View.GONE);
        termsTextView = (TextView) findViewById(R.id.helloactivity_textview_terms);
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
                if (ed1.getText().toString().equals("admin") &&

                        ed2.getText().toString().equals("admin")) {
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
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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




}

