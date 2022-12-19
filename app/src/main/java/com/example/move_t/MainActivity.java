package com.example.move_t;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Switch;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Define the variable of CalendarView type
    // and TextView type;
    CalendarView calendar;
    TextView date_view;
    public String selectedDate;
    String date;
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
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        selectedDate = df.format(c);
        date_view.setText(selectedDate);
        showActivities();
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
                                selectedDate = dayOfMonth + "-"
                                        + (month + 1) + "-" + year;
                                // set this date in TextView for Display

                                date_view.setText(selectedDate);
                                showActivities();

                            }
                        });

        Button addEvent_btn = findViewById(R.id.addEvent);
        addEvent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EventForm.class);
                intent.putExtra("SelectedDate", selectedDate);

                startActivity(intent);
            }
        });
    }

    public void showActivities() {
        try {
            elements = new ArrayList<>();

            List<ContentValues> infoList = ((DataManager) getApplication()).getByDate2(((DataManager) getApplication()).date2int(selectedDate));
            for (int i = 0; i < infoList.size(); i++) {
                ContentValues info = infoList.get(i);
                String h = info.getAsString("hour1") + '-' + info.getAsString("hour2");
                ListElement le = new ListElement("#775447", info.get("name").toString(), h, true, -1, "pesa1");

                elements.add(le);
                System.out.println(i);
            }
            ListAdapter listAdapter = new ListAdapter(elements, this, new ListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ListElement item) {
                    Intent intent = new Intent(MainActivity.this, EventForm.class);
                    intent.putExtra("SelectedDate", selectedDate);
                    intent.putExtra("istolistonly", true);
                    String s = item.desc;
                    String[] ss = s.split("-");
                    intent.putExtra("hour1", ss[0]);
                    intent.putExtra("hour2", ss[1]);

                    startActivity(intent);
                }
            });
            listAdapter.shown = false;
            RecyclerView recyclerView = findViewById(R.id.listRVdate);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(listAdapter);
        } catch (Exception e) {
            System.out.println("FALOOOO");
            e.printStackTrace();
        }
    }
}