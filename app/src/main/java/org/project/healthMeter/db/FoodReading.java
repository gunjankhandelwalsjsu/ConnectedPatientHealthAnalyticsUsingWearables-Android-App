package org.project.healthMeter.db;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rajeshkhandelwal on 11/13/15.
 */
public class FoodReading extends RealmObject {

    @PrimaryKey
    private String id;

    private Date created;
    private String foodName;
    private String ExistingAllergy;

    public FoodReading() {
    }

    private String ExistingDisease;
    private String AllergyResult;
    private String sugarsConsumed;

    public FoodReading(String foodName, String existingAllergy, String existingDisease, String allergyResult, String sugarsConsumed) {
        this.foodName = foodName;
        this.ExistingAllergy = existingAllergy;
        this.ExistingDisease = existingDisease;
        this.AllergyResult = allergyResult;
        this.sugarsConsumed = sugarsConsumed;
    }

    public FoodReading(String foodName, String existingAllergy, String existingDisease, String allergyResult, String sugarsConsumed, Date finalDateTime) {
        this.foodName = foodName;
        this.ExistingAllergy = existingAllergy;
        this.ExistingDisease = existingDisease;
        this.AllergyResult = allergyResult;
        this.sugarsConsumed = sugarsConsumed;
        this.created=finalDateTime;
    }

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

       public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }




    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}