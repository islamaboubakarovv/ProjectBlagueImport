package com.example.projectblagueimport;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class favoris extends AppCompatActivity {
    ListView l;
    ArrayList<String> test = new ArrayList<String>();
    ArrayAdapter<String> arr;
    ArrayList<String> fav = new ArrayList<String>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoris);
        l = findViewById(R.id.fav);
        arr = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, test);
        Intent intent = getIntent();
        fav = intent.getStringArrayListExtra("fav");
        for(String s  : fav){
            arr.add(s);

        }
        l.setAdapter(arr);



    }
}