package org.project.healthMeter.db;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rajeshkhandelwal on 11/13/15.
 */
public class FoodReading extends RealmObject {

    @PrimaryKey
    private long id;
    private String foodName;
    private String ExistingAllergy;
    private String ExistingDisease;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getExistingAllergy() {
        return ExistingAllergy;
    }

    public void setExistingAllergy(String existingAllergy) {
        ExistingAllergy = existingAllergy;
    }

    public String getExistingDisease() {
        return ExistingDisease;
    }

    public void setExistingDisease(String existingDisease) {
        ExistingDisease = existingDisease;
    }

    public String getAllergyResult() {
        return AllergyResult;
    }

    public void setAllergyResult(String allergyResult) {
        AllergyResult = allergyResult;
    }

    public String getSugarsConsumed() {
        return sugarsConsumed;
    }

    public void setSugarsConsumed(String sugarsConsumed) {
        this.sugarsConsumed = sugarsConsumed;
    }

    private String AllergyResult;
    private String sugarsConsumed;


    private int user_id;
    private Date created;

    public FoodReading() {
    }

    public FoodReading(String foodName,String ExistingAllergy,String ExistingDisease,String AllergyResult,String sugarsConsumed, Date created) {
         this.foodName=foodName;
         this.ExistingAllergy=ExistingAllergy;
         this.ExistingDisease=ExistingDisease;
         this.AllergyResult=AllergyResult;
         this.sugarsConsumed=sugarsConsumed;
        this.created = created;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}