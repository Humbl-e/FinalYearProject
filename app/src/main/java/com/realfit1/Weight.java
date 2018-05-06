package com.realfit1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author P15224747
 */

public class Weight {

    private double weight;
    private String date;
    private String id;

    public Weight (){
        //empty constructor required

    }
    public Weight(String id,double weight, String date) {
        this.id = id;
        this.weight = weight;
        this.date = date;
    }

    public String  getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFormattedDate() {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, d MMM yyyy hh:mm a", Locale.getDefault());

        Date date;
        String formatDate = null;

        try {
            date = inputFormat.parse(this.date);
            formatDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formatDate;
    }

}
