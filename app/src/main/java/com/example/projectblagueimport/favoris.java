package com.example.projectblagueimport;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class favoris extends AppCompatActivity {
    ListView l;
    ArrayList<String> test = new ArrayList<String>();
    ArrayAdapter<String> arr;
    ArrayList<String> fav = new ArrayList<String>();
    String selec = "";


    //boutons de la barre d'action
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_txt:
                try {
                    String s = "";
                    for (int i = 0; i < 10; i++) {
                        s += "\n" + arr.getItem(i);
                    }
                    Context context = getApplicationContext();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("test.txt", Context.MODE_PRIVATE));
                    outputStreamWriter.write(s);
                    outputStreamWriter.close();
                } catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
                break;

            case R.id.share:
                if (selec.isEmpty()) {

                } else {
                    Intent myIntent = new Intent(Intent.ACTION_SEND);
                    myIntent.setType("text/plain");
                    String shareBody = selec;
                    String shareSub = "blagues très drôles";
                    myIntent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                    myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(myIntent, "Share using"));

                }

                break;

        }
        return true;
    }

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
        //selectionner une blague
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position % 2 == 0) {
                    String s = parent.getItemAtPosition(position).toString();
                    String t = parent.getItemAtPosition(position + 1).toString();
                    Object v = parent.getItemAtPosition(position);
                    String u = "\n" + s + "\n" + t;
                    //u est la blague qu'on veut stocker avec la réponse liée
                    selec = u;
                    //changer la couleur pour afficher la réponse
                    l.getChildAt(position+1).setBackgroundColor(Color.LTGRAY);

                }else{
                    l.getChildAt(position).setBackgroundColor(Color.LTGRAY);
                }

            }
        });



    }
}