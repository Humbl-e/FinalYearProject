package com.realfit1;

/**
 * @author P15224747
 */

public class Progress {
    String key;
    String date;
    String calories;
    String steps;
    String sleep;

    public Progress () {
        //empty constructor needed for Firebase
    }
    public Progress(String key,String date,String steps,String calories,String sleep){
        this.key =key;
        this.date =date;
        this.steps = steps;
        this.calories = calories;
        this.sleep = sleep;

    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getSleep() {
        return sleep;
    }

    public void setSleep(String sleep) {
        this.sleep = sleep;
    }

}

