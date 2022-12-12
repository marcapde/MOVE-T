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

import org.w3c.dom.Text;

import java.util.ArrayList;
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
//  public void init(){
//    Button btnm = findViewById(R.id.addEvent);btnm.setText("works");
//    System.out.println("******************");
//
//    elements = new ArrayList<>();
//    elements.add(new ListElement("#775447","Ejercicio1","LOREM IPSUM DOLOR I MAE",true));
//    elements.add(new ListElement("#775447","Ejercicio2","LOREM IPSUM DOLOR I MAE",false));
//    elements.add(new ListElement("#775447","Ejercicio3","LOREM IPSUM DOLOR I MAE",true));
//    elements.add(new ListElement("#775447","Ejercicio4","LOREM IPSUM DOLOR I MAE",false));
//
////    ListAdapter listAdapter = new ListAdapter(elements,this);
//    RecyclerView recyclerView = findViewById(R.id.listReciclerView);
//    recyclerView.setHasFixedSize(true);
//    recyclerView.setLayoutManager(new LinearLayoutManager(this));
//    recyclerView.setAdapter(listAdapter);
//    System.out.println("******************");
//
//  }
}