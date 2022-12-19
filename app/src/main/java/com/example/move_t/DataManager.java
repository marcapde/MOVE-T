package com.example.move_t;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class DataManager extends Application {
    public myDBHandler dbHandler;
    private SQLiteDatabase sqLiteDatabase;


    boolean can_insert(int date, int hour1, int hour2) {
        String query = "SELECT date, hour1, hour2 FROM databoard WHERE date='" + date + "' AND NOT " +
                "( hour1 >= '" + hour2 + "' OR hour2 <= '" + hour1 + "')";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        System.out.println("apsjghasñfkjghasñfkjghbasñjfgbañsjklgbnasñjklgbnasñkfjlgfbasñfkjfgbnasñjkfbgnañskfjgbasfñkjgbsafkjñbgkñs");
        System.out.println(cursor.getCount() == 0);
        return cursor.getCount() == 0;
    }

    void insertDB(ContentValues contentValues) {

        int hour1 = (int) contentValues.get("hour1");
        int hour2 = (int) contentValues.get("hour2");


        if (hour2 > hour1 && can_insert((int) contentValues.get("date"), hour1, hour2)) {
            sqLiteDatabase.insert("databoard", null, contentValues);
        }
    }

    String getByDate(String pdate, String hour1p, String hour2p) {
        int date = date2int(pdate);
        int hour1 = hour2int(hour1p);
        int hour2 = hour2int(hour2p);
        String query = "Select date,ids,hour1,hour2 FROM databoard WHERE date='" + date + "' AND hour1='" + hour1 + "' AND hour2='" + hour2 + "'";

        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            cursor.moveToFirst();
            String answer = cursor.getString(1);
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    String getName(String pdate, String hour1p, String hour2p) {
        int date = date2int(pdate);
        int hour1 = hour2int(hour1p);
        int hour2 = hour2int(hour2p);
        String query = "Select date,name,hour1,hour2 FROM databoard WHERE date='" + date + "' AND hour1='" + hour1 + "' AND hour2='" + hour2 + "'";

        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            cursor.moveToFirst();
            String answer = cursor.getString(1);

            return answer;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    List<ContentValues> getByDate2(int date) {
        String query = "Select * FROM databoard WHERE date='" + date + "'";
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            cursor.moveToFirst();
            List<ContentValues> list = new ArrayList<>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                // do what you need with the cursor here
                System.out.println("holalaaaaaa");
                ContentValues contentValues = new ContentValues();


                contentValues.put("name", cursor.getString(1));
                System.out.println(contentValues.getAsString("name"));

                contentValues.put("hour1", int2hour(cursor.getInt(3)));
                contentValues.put("hour2", int2hour(cursor.getInt(4)));
                list.add(contentValues);
            }
            cursor.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("FALLOOOOOOO");
            return null;
        }
    }


    public int hour2int(String h) { // 00:00 -> 100
        String[] hh = h.split(":");
        int res = 0;
        res = Integer.parseInt(hh[0]) * 100;

        if (res > 2300){
          return -1;
        }
        if (hh.length == 2) {
          if (Integer.parseInt(hh[1]) > 59) {
              return -1;
          }
            res = res + Integer.parseInt(hh[1]);
        }
      System.out.println("THIS SHOUD BE 00 " + res);
        return res;
    }

    public String int2hour(int h) {
        String hh = Integer.toString(h);
        String res = "";
        if (hh.length() == 1){
          res += hh;
          res += ":";
          res += "00";
        }
        else if (hh.length() == 3) {
            res += hh.substring(0, 1);
            res += ":";
            res += hh.substring(1, 3);
        } else {
            res += hh.substring(0, 2);
            res += ":";
            res += hh.substring(2, 4);
        }
        return res;
    }

    public int date2int(String date) {
        String[] date2 = date.split("-");
        int res = 0;
        res = Integer.parseInt(date2[0]);
        res = res + (Integer.parseInt(date2[1]) * 100);
        res = res + (Integer.parseInt(date2[2]) * 10000);

        return res;
    }

    public String int2date(int date) {
        String date2 = Integer.toString(date);
        String year = date2.substring(0, 4);
        System.out.println("Failed on first parse with date2= :: " + date2);

        String month = date2.substring(5, 7);
        String day = date2.substring(8, 10);
        System.out.println(day + "-" + month + "-" + year);
        return day + "-" + month + "-" + year;
    }


    @Override
    public void onCreate() {

        super.onCreate();
        try {
            dbHandler = new myDBHandler(this, "databoard", null, 1);
            sqLiteDatabase = dbHandler.getWritableDatabase();
            sqLiteDatabase.execSQL("DROP TABLE databoard");
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS  databoard (date INTEGER,name TEXT,ids TEXT,  hour1 INTEGER , hour2 INTEGER)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
