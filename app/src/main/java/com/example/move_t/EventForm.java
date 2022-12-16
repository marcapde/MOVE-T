package com.example.move_t;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EventForm extends AppCompatActivity {

  EditText editText;
  public String color = "#775447";
  public String name;
  public String desc;
  public boolean checked = false;
  public int id;
  public ArrayList<Integer> id_selected;
  List<ListElement> elements;
  ListAdapter listAdapter;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.event_form);
    elements = new ArrayList<>();
    id_selected = new ArrayList<Integer>();
    readJson();
    showCards();

  }

  public void readJson() {
    try{
      String jsonDataString = readJsonDataFromFile();
      JSONArray jsonArray = new JSONArray(jsonDataString);

      for (int i = 0; i < jsonArray.length(); ++i) {
        JSONObject itemObj = jsonArray.getJSONObject(i);
        id = itemObj.getInt("id");
        name = itemObj.getString("title");
        desc = itemObj.getString("desc");
        elements.add(new ListElement(color,name,desc,checked,id));
      }

    }catch (Exception e){
      e.printStackTrace();

    }
  }


  public void showCards() {
    listAdapter = new ListAdapter(elements, (EventForm) this, new ListAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(ListElement item) {
        item.checked = !item.checked;
        // f5 db
        id_selected.add(item.id);
        //showCards();
        //this upp commented
      }
    });
    RecyclerView recyclerView = findViewById(R.id.EventsrecyclerView);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(listAdapter);
  }

  public String readJsonDataFromFile() throws IOException {
    InputStream inputStream = null;
    StringBuilder builder = new StringBuilder();

    try{

      String jsonString = null;
      inputStream = getResources().openRawResource(R.raw.dummy);
      BufferedReader bufferedReader = new BufferedReader(
              new InputStreamReader(inputStream,"UTF-8"));

      while((jsonString = bufferedReader.readLine()) != null){
        builder.append(jsonString);
      }
    } finally {

      if(inputStream!=null){
        inputStream.close();
      }
    }
    return builder.toString();

  }
  public void saveSelectedEvents(View view){
    try {
      List<Integer> list = listAdapter.getSelecteds();
      EditText hora1 = (EditText) findViewById(R.id.BegintTme);
      EditText hora2 = (EditText) findViewById(R.id.EndTime);

      String h1 = hora1.getText().toString();
      String h2 = hora2.getText().toString();

      ContentValues contentValues = new ContentValues();
      String ids = "";
      for (int i = 0; i < list.size(); i++) {
        ids += list.get(i).toString();
        ids += ",";
      }
      contentValues.put("date", getIntent().getStringExtra("SelectedDate"));
      contentValues.put("ids", ids);
      contentValues.put("hour1", h1);
      contentValues.put("hour2", h2);

      ((DataManager) getApplication()).insertDB(contentValues);
      listAdapter.cleanSelecteds();
    }catch (Exception e){
      e.printStackTrace();

    }
  }


}
