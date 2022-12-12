package com.example.move_t;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;

public class DataManager extends Application {
    public myDBHandler dbHandler;
    private SQLiteDatabase sqLiteDatabase;


//    boolean can_insert( String date, String hour1, String hour2){
//        String query = "SELECT date, hour1, hour2 FROM databoard WHERE date='"+date+"' AND " +
//                "( hour1 >= '"+hour2+"' OR hour2 <= '"+hour1+"')";
//        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
//        cursor.moveToFirst();
//
//        return cursor.getCount() == 0;
//    }

    void insertDB(ContentValues contentValues){
//        System.out.printf("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
//
//        String hour1 = (String) contentValues.get("hour1");
//        String hour2 = (String) contentValues.get("hour2");
//        System.out.printf("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");



//        if(hour2.compareTo(hour1)>0 && can_insert((String)contentValues.get("date"),hour1,hour2)){
            // aqui pon lo tuyo 🙂
            System.out.printf("guardaddodododododododododdododododod");
            sqLiteDatabase.insert("databoard",null,contentValues);
//        }
    }

    String getByDate(String date){
        String query = "Select date,ids FROM databoard WHERE date='"+date+"'";

        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query,null);
            cursor.moveToFirst();
            String answer = cursor.getString(1);
            return answer;
        }
        catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }


    @Override
    public void onCreate() {

        super.onCreate();
        try{
            dbHandler = new myDBHandler(this, "databoard", null, 1);
            sqLiteDatabase = dbHandler.getWritableDatabase();
            sqLiteDatabase.execSQL("DROP TABLE databoard");

            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS  databoard (date TEXT,ids TEXT)");
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
