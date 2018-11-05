package com.lyna.www.sqlite;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{

    EditText editTextCountry, editTextCity;
    TextView textViewTotalCount;
    Button buttonSearch, buttonAddVisited;
    Button buttonInsert, buttonRead, buttonUpdate, buttonDelete;
    TextView textViewReadDB;

    MyDBOpenHelper myDBOpenHelper;
    SQLiteDatabase mdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String pkId;

        myDBOpenHelper = new MyDBOpenHelper(this, "awe.db", null, 1);
        mdb = myDBOpenHelper.getWritableDatabase();

        editTextCountry = findViewById(R.id.editTextCountry);
        editTextCity = findViewById(R.id.editTextCity);

        textViewTotalCount = findViewById(R.id.textViewVisitedTotalCount);

        buttonSearch = findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(this);

        buttonAddVisited = findViewById(R.id.buttonAddVisited);
        buttonAddVisited.setOnClickListener(this);

        buttonInsert = findViewById(R.id.buttonInsert);
        buttonInsert.setOnClickListener(this);

        buttonRead = findViewById(R.id.buttonRead);
        buttonRead.setOnClickListener(this);

        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(this);

        buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(this);

        textViewReadDB = findViewById(R.id.textViewReadDB);

        editTextCountry.setOnKeyListener(this);
        editTextCity.setOnKeyListener(this);
    }

    @Override
    public void onClick(View v) {

        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        switch(v.getId()) {
            case R.id.buttonSearch:
                Search();
                break;
            case R.id.buttonAddVisited:
                AddVisited();
                break;
            case R.id.buttonInsert:
                InsertDB();
                break;
            case R.id.buttonRead:
                readDB();
                break;
            case R.id.buttonUpdate:
                UpdateDB();
                break;
            case R.id.buttonDelete:
                DeleteDB();
                break;
        }
    }
    public void Search(){

        String pkId = get_city();

        if(pkId != null)
            get_totalcount(pkId);

    }

    public String get_city(){
        String country = editTextCountry.getText().toString();
        String query = "SELECT * FROM awe_country WHERE country= '"+country+"'";

        Cursor cursor =  mdb.rawQuery(query, null);
        if(cursor.getCount()>0){
            String pkId="";
            cursor.moveToFirst();

            pkId = cursor.getString(cursor.getColumnIndex("pkid"));

            String city = cursor.getString(cursor.getColumnIndex("capital"));
            editTextCity.setText(city);
            return pkId;
        }
        else {
            Toast.makeText(getApplicationContext(), editTextCountry.getText() + " is not existed in DB", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    public void get_totalcount(String pkId){
        String query = "SELECT count(fkid) visitedTotal FROM awe_country INNER JOIN awe_country_visited ON pkid=fkid AND pkid= '"+pkId+"'";
        Cursor cursor = mdb.rawQuery(query, null);

        int a=cursor.getCount();
        if(cursor.getCount()>0){
            cursor.moveToFirst();

            int visitedTotal = cursor.getInt(cursor.getColumnIndex("visitedTotal"));
            textViewTotalCount.setText("Visited Total Count: " + String.valueOf(visitedTotal));
        }
    }

    public void AddVisited(){

        String pkId = get_city();

        if(pkId != null) {
            String query = "INSERT INTO awe_country_visited VALUES('" + pkId + "')";
            mdb.execSQL(query);

            get_totalcount(pkId);
        }
    }

    public void InsertDB(){

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        String str = "INSERT INTO awe_country VALUES('" + currentDateTimeString + "', '" + editTextCountry.getText().toString() + "', '" + editTextCity.getText().toString() + " ');";
        mdb.execSQL(str);
    }

    public void readDB() {
        String query = "SELECT * FROM awe_country ORDER BY pkid DESC";
        Cursor cursor = mdb.rawQuery(query, null);
        String str = "";

        while(cursor.moveToNext()) {
            String id;

            id = cursor.getString(0);
            String country = cursor.getString(cursor.getColumnIndex("country"));
            String city = cursor.getString(2);
            str += (id + ":" + country + "-" + city + "\n");
        }
        if(str.length()>0) {
            textViewReadDB.setText(str);
        }
        else{
            Toast.makeText(getApplicationContext(), "Warning: Empty DB", Toast.LENGTH_SHORT).show();
            textViewReadDB.setText("");
        }
    }

    public void UpdateDB(){
        String query = "UPDATE awe_country SET capital='"+editTextCity.getText().toString()+"' WHERE country='" + editTextCountry.getText().toString() +"'";
        mdb.execSQL(query);
    }

    public void DeleteDB(){
        String query = "DELETE FROM awe_country WHERE country='"+editTextCountry.getText().toString()+"'";
        mdb.execSQL(query);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if((event.getAction()==KeyEvent.ACTION_DOWN) && (keyCode==KeyEvent.KEYCODE_ENTER)) {

            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            return true;
        }
        return false;
    }
}
