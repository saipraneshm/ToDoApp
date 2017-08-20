package com.codepath.preassignment.todoapp;


/**
 * Created by saip92 on 8/19/2017.
 */

public class Priority {

    public static final int HIGH = 0;
    public static final int MEDIUM = 1;
    public static final int LOW = 2;


    public static String getString(int val){

        switch(val){
            case HIGH:
                return "HIGH";
            case MEDIUM:
                return "MEDIUM";
            case LOW:
                return "LOW";
            default:
                return "LOW";

        }
    }

    public static int getInt(String str){

        switch (str){
            case "HIGH":
                return HIGH;
            case "MEDIUM":
                return MEDIUM;
            case "LOW":
                return LOW;
            default:
                return LOW;
        }
    }
}


