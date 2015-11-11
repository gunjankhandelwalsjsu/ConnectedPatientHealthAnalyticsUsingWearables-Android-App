package org.glucosio.android.db;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    @PrimaryKey
    private String id;
    private String email;


    public User() {

    }

    public User(String id, String email) {
        this.id=id;
        this.email=email;

    }



    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email=email;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}