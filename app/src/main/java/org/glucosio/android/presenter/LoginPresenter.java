package org.glucosio.android.presenter;

import android.text.TextUtils;
import android.widget.Toast;

import org.glucosio.android.activity.LoginActivity;
import org.glucosio.android.db.DatabaseHandler;
import org.glucosio.android.db.User;


public class LoginPresenter {
    DatabaseHandler dB;
    LoginActivity loginActivity;
    int id;
    int age;
    String name;
    String country;
    int gender;
    String language;

    public LoginPresenter(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
        dB = new DatabaseHandler(loginActivity);
    }

    public void loadDatabase(){
        id = 1; // Id is always 1. We don't support multi-user (for now :D).
        name = "Test Account"; //TODO: add input for name in Tips;
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
        dB.addUser(new User(id, name, language, country, age, gender));
        loginActivity.closeHelloActivity();
    }
}