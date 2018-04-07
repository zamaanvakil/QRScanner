package com.example.android.qrscanner;

/**
 * Created by Zamaan on 22-02-2018.
 */

public class Scan {
    int id;
    String type;
    String date;
    String result;

    public Scan(int id, String type, String date, String result) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getResult() {
        return result;
    }
}
