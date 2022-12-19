package com.example.move_t;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
    public String color = "#000000";
    public String name;
    public String desc;
    public boolean checked = false;
    public int id;
    public ArrayList<Integer> id_selected;
    List<ListElement> elements;
    ListAdapter listAdapter;
    String[] idSplited;
    EditText t;
    TextView tw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_form);
        elements = new ArrayList<>();
        id_selected = new ArrayList<Integer>();

        if (getIntent().getExtras().getBoolean("istolistonly")) {
            t = (EditText) findViewById(R.id.eventName);
            tw = (TextView) findViewById(R.id.titleEvent);
            t.setVisibility(View.INVISIBLE);
            tw.setVisibility(View.VISIBLE);
            Button bt = findViewById(R.id.saveForm_btn);
            bt.setVisibility(View.INVISIBLE);
            readJson();
            showCardsActivity();
        } else {
            readJson();
            showCards();
        }
    }

    public void showCardsActivity() {
        EditText bgtime = findViewById(R.id.BegintTme);
        bgtime.setVisibility(View.GONE);
        bgtime = findViewById(R.id.EndTime);
        bgtime.setVisibility(View.GONE);

        showCards();
    }


    public void readJson() {
        try {
            String jsonDataString = readJsonDataFromFile();
            JSONArray jsonArray = new JSONArray(jsonDataString);
            if (getIntent().getExtras().getBoolean("istolistonly")) {
                String selectedDate = getIntent().getStringExtra("SelectedDate");
                String ids = ((DataManager) getApplication()).getByDate(selectedDate, getIntent().getStringExtra("hour1"), getIntent().getStringExtra("hour2"));
                idSplited = ids.split(",");

                tw.setText(((DataManager) getApplication()).getName(selectedDate, getIntent().getStringExtra("hour1"), getIntent().getStringExtra("hour2")));
            }
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject itemObj = jsonArray.getJSONObject(i);
                id = itemObj.getInt("id");

                if (getIntent().getExtras().getBoolean("istolistonly")) {
                    List<String> nameList = new ArrayList<>(Arrays.asList(idSplited));
                    if (nameList.contains(String.valueOf(id))) {
                        System.out.println("HOLA FROM FORMULALALAALALALALALALALAL");

                        name = itemObj.getString("title");
                        desc = itemObj.getString("desc");
                        elements.add(new ListElement(color, name, desc, checked, id, itemObj.getString("img")));
                    }
                } else {
                    name = itemObj.getString("title");
                    desc = itemObj.getString("desc");

                    elements.add(new ListElement(color, name, desc, checked, id, itemObj.getString("img")));
                }
            }

        } catch (Exception e) {
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
        RecyclerView recyclerView;
        if (getIntent().getExtras().getBoolean("istolistonly")) {
            listAdapter.shown = false;
            recyclerView = findViewById(R.id.EventsrecyclerView2);

        } else {
            recyclerView = findViewById(R.id.EventsrecyclerView);

        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
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

    public void saveSelectedEvents(View view) {
        try {
            List<Integer> list = listAdapter.getSelecteds();
            EditText hora1 = (EditText) findViewById(R.id.BegintTme);
            EditText hora2 = (EditText) findViewById(R.id.EndTime);

            String h1 = hora1.getText().toString();
            String h2 = hora2.getText().toString();

            String strDate = getIntent().getStringExtra("SelectedDate");
            int date = ((DataManager) getApplication()).date2int(strDate);
            if (!check_parameters(list, date, h1, h2)) return;


            ContentValues contentValues = new ContentValues();
            String ids = "";
            for (int i = 0; i < list.size(); i++) {
                ids += list.get(i).toString();
                ids += ",";
            }
            contentValues.put("date", date);
            EditText nameT = findViewById(R.id.eventName);
            String nameTxt = nameT.getText().toString();
            contentValues.put("name", nameTxt.equals("") ? ("Event " + getIntent().getStringExtra("SelectedDate")) : nameTxt);
            contentValues.put("ids", ids);
            contentValues.put("hour1", ((DataManager) getApplication()).hour2int(h1));
            contentValues.put("hour2", ((DataManager) getApplication()).hour2int(h2));

            ((DataManager) getApplication()).insertDB(contentValues);
            listAdapter.cleanSelecteds();

            goBack();


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        this.finish();
        startActivity(intent);
    }


    public boolean check_parameters(List<Integer> list, int date, String h1, String h2) {
      String[] checkH1 = h1.split(":");
      if ((checkH1.length == 1 && h1.length() > 2) ||
        (checkH1.length == 2 && (checkH1[0].length() > 2  || checkH1[1].length() != 2 ))){
        popup("Invalid hour format, use 13:15 format");
        return false;
      }
      String[] checkH2 = h2.split(":");
      if ((checkH2.length == 1 && h2.length() > 2) ||
        (checkH2.length == 2 && (checkH2[0].length() > 2  || checkH2[1].length() != 2 ))){
        popup("Invalid hour format, use 13:15 format");
        return false;
      }
      int hh1, hh2;
        try {
            hh1 = ((DataManager) getApplication()).hour2int(h1);
            hh2 = ((DataManager) getApplication()).hour2int(h2);
            if(hh1 == -1 || hh2 == -1){
                popup("Hours must be between 00 and 23, and minutes between 00 and 59");
                return false;
            }
        } catch (Exception e) {
            popup("Invalid hour format, use 13:15 format");
            return false;
        }
        if (hh1 >= hh2) {
            popup("Beggining hour can not be greater than end hour");
            return false;
        }
        if (list.size() == 0) {
            popup("No exercices selected");
            return false;
        }
        if (!((DataManager) getApplication()).can_insert(date, hh1, hh2)) {
            popup("Event already exists in this time, please try another");
            return false;
        }
        return true;

    }

    public void popup(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EventForm.this);
        builder.setCancelable(true);
        builder.setTitle("Something went wrong");
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }


}
