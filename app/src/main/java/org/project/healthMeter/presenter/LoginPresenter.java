package org.project.healthMeter.presenter;

import android.text.TextUtils;

import org.project.healthMeter.activity.LoginActivity;
import org.project.healthMeter.db.DatabaseNewHandler;


public class LoginPresenter {
    DatabaseNewHandler dB;
    LoginActivity loginActivity;
    String id;

    String email;


    public LoginPresenter(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
        dB = new DatabaseNewHandler(loginActivity);
    }

    public void loadDatabase(String id, String email){
        this.id = id; // Id is always 1. We don't support multi-user (for now :D).
        this.email = email; //TODO: add input for name in Tips;
    }

    public void onNextClicked(){


        showEULA();

        loginActivity.displayErrorMessage();

    }

    private boolean validateAge(String age) {
        if (TextUtils.isEmpty(age)){
            return false;
        } else if (!TextUtils.isDigitsOnly(age)){
            return false;
        } else {
            int finalAge = Integer.parseInt(age);
            return finalAge > 0 && finalAge < 120;
        }
    }

    private void showEULA(){
        loginActivity.showEULA();
    }

    public void saveToDatabase(){
      //  dB.addUser(new User(id, email)); // We use ADA range by default
        loginActivity.closeHelloActivity();
    }
}
