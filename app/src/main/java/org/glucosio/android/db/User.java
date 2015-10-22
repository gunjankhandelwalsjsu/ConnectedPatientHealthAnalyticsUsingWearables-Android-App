package org.glucosio.android.db;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    @PrimaryKey
    private int id;
    private String name;


    public User() {

    }

    public User(int id, String name,String preferred_language, String country, int age, String gender,int dType, String pUnit, String pRange, int minRange, int maxRange) {
        this.id=id;
        this.name=name;

    }



    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name=name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}