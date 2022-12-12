package com.example.move_t;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  // Define the variable of CalendarView type
  // and TextView type;
  CalendarView calendar;
  TextView date_view;
  public String selectedDate;
  String Date;
  List<ListElement> elements;
//   public void printEvents(){
//    String str = ((DataManager)getApplication()).getByDate(Date);
//    String [] res=str.split("[,]", 0);
//    str = "";
//    for(String myStr: res) {
//      str += myStr+"\n";
//    }
//    txt.setText(str);
//  }
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    // By ID we can use each component
    // which id is assign in xml file
    // use findViewById() to get the
    // CalendarView and TextView
    calendar = (CalendarView)
      findViewById(R.id.calendar);
    date_view = (TextView)
      findViewById(R.id.date_view);
    calendar.setFirstDayOfWeek(2);
    // Add Listener in calendar
    calendar
      .setOnDateChangeListener(
        new CalendarView
          .OnDateChangeListener() {
          @Override

          // In this Listener have one method
          // and in this method we will
          // get the value of DAYS, MONTH, YEARS
          public void onSelectedDayChange(
            @NonNull CalendarView view,
            int year,
            int month,
            int dayOfMonth) {

            // Store the value of date with
            // format in String type Variable
            // Add 1 in month because month
            // index is start with 0
            Date = dayOfMonth + "-"
              + (month + 1) + "-" + year;
            // set this date in TextView for Display
//            printEvents();
            date_view.setText(Date);
            selectedDate = Date;
            showByDate();
            //init();
            Button addEvent_btn = findViewById(R.id.addEvent);
            addEvent_btn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                addEvent_btn.setText("works");
                Intent intent = new Intent(MainActivity.this,EventForm.class );
                intent.putExtra("SelectedDate", Date);
                startActivity(intent);


              }
            });
          }
        });


  }

  private void showByDate() {
    elements = new ArrayList<>();

    String ids = ((DataManager)getApplication()).getByDate(Date);
    String[] idSplited = ids.split(",");
    getFromJson(idSplited);

    ListAdapter listAdapter = new ListAdapter(elements, this, new ListAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(ListElement item) {

      }
    });
    System.out.println("idsplited()");
    System.out.println(idSplited.length);
    System.out.println("elements.size()");

    System.out.println(elements.size());
    RecyclerView recyclerView = findViewById(R.id.listRVdate);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(listAdapter);
  }
  public void getFromJson(String[] ids){
    List<String> nameList = new ArrayList<>(Arrays.asList(ids));
    try{
      String jsonDataString = readJsonDataFromFile();
      JSONArray jsonArray = new JSONArray(jsonDataString);

      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject itemObj = jsonArray.getJSONObject(i);
        int id = itemObj.getInt("id");
        if( nameList.contains(String.valueOf(id))){
          String name = itemObj.getString("title");
          String desc = itemObj.getString("desc");
          System.out.println("holafromsaving");
          elements.add(new ListElement("#775447", name, desc, true, id));
        }
      }

    }catch (Exception e){
      e.printStackTrace();

    }
  }
  public String readJsonDataFromFile() throws IOException {
    InputStream inputStream = null;
    StringBuilder builder = new StringBuilder();

    try {

      String jsonString = null;
      inputStream = getResources().openRawResource(R.raw.dummy);
      BufferedReader bufferedReader = new BufferedReader(
              new InputStreamReader(inputStream, "UTF-8"));

      while ((jsonString = bufferedReader.readLine()) != null) {
        builder.append(jsonString);
      }
    } finally {

      if (inputStream != null) {
        inputStream.close();
      }
    }
    return builder.toString();
  }
}