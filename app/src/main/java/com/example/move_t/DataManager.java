package com.example.move_t;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;

public class DataManager extends Application {
    public myDBHandler dbHandler;
    private SQLiteDatabase sqLiteDatabase;

    void insertDB(ContentValues contentValues){
        sqLiteDatabase.insert("provaSaveForm",null,contentValues);
    }

    String provaGET(){
        String query = "Select * FROM provaSaveForm";
        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query,null);
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        catch (Exception e){
            return "err";
        }
    }

    String getByDate(String date){
        String query = "Select date,field1,field2 FROM provaSaveForm WHERE date='"+date+"'";

        try {
            Cursor cursor = sqLiteDatabase.rawQuery(query,null);
            cursor.moveToFirst();
            String answer = "";
            while (cursor.moveToNext()){
                for(int i = 1; i<cursor.getColumnCount();i++){
                    answer += cursor.getString(i);
                    answer += " ";
                }
                answer+="\n";
            }
            return answer;
        }
        catch (Exception e){
            return query;
        }
    }

    @Override
    public void onCreate() {

        super.onCreate();
        try{
            dbHandler = new myDBHandler(this, "provaSaveForm", null, 1);
            sqLiteDatabase = dbHandler.getWritableDatabase();
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS  provaSaveForm (date TEXT,field1 TEXT, field2 TEXT)");
        }catch(Exception e){
            e.printStackTrace();
        }
    }


}
