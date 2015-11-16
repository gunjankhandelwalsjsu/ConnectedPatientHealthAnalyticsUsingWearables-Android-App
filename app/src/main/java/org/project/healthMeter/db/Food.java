package org.project.healthMeter.db;

/**
 * Created by rajeshkhandelwal on 11/14/15.
 */
public class Food {
    private String foodName;
    private String ExistingAllergy;
    private String ExistingDisease;
    private String AllergyResult;
    private String sugarsConsumed;

    public Food(String foodName, String existingAllergy, String existingDisease, String allergyResult, String sugarsConsumed) {
        this.foodName = foodName;
        ExistingAllergy = existingAllergy;
        ExistingDisease = existingDisease;
        AllergyResult = allergyResult;
        this.sugarsConsumed = sugarsConsumed;
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




}
