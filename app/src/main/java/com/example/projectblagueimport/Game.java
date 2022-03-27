package com.example.projectblagueimport;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.example.projectblagueimport.MainActivity;
import com.example.projectblagueimport.MainActivity.JsonTask;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class Game extends AppCompatActivity {
    TextView t;
    TextView y;
    EditText e;
    ArrayList<String> test;
    ArrayList<String>arr;
    //ArrayAdapter<String> arr;
    ArrayList<String> jokes;
    String guess;
    int trys=0;
    int essaisR;
    int i=0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public class JsonTask extends AsyncTask<String, String, String> {


        protected String doInBackground(String... params) {


            HttpsURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }
                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println(result);
            //analyse et recupere les objets JSON
            String question = null;
            String answer = null;
            try {
                JSONObject toDecode = new JSONObject(result);
                JSONObject joke = toDecode.getJSONObject("joke");
                question = joke.getString("question");
                answer = "rép: " + joke.getString("answer");
                result = question + '/' + answer;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //fin du parsing JSON
            arr.add(question);
            arr.add(answer);
            t.setText(arr.get(i));
            y.setText(arr.get(i+1));
            guess = arr.get(i+1).substring(5);
            System.out.println("guess "+guess);
            //mettre le texte en blanc au début pour cacher
            //System.out.println("arr: "+arr);
            //System.out.println(arr.get(i));
            //tv.setText(result);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        t = findViewById(R.id.Devinette);
        y = findViewById(R.id.Rep);
        e = findViewById(R.id.Deviner);
        arr = new ArrayList<String>();
        new JsonTask().execute("https://blague.xyz/api/joke/random");
        //arr = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, test);
        //t.setText(arr.get(i));
        //System.out.println(arr);
        //t.setText("test");
        //y.setText(arr.getItem(i+1));

    }
    public void btn(View v) {
        switch (v.getId()) {
            case R.id.CheckD:
                System.out.println("e "+e.getText().toString());
                System.out.println("guess "+guess);


                if(e.toString().isEmpty()){

                }else if(!e.getText().toString().equalsIgnoreCase(guess)){
                    //int color = Integer.parseInt("bdbdbd", 16)+0xFF000000;
                    //y.setTextColor(color);
                    y.setTextColor(Color.RED);
                    trys++;
                    essaisR=3-trys;
                    if(essaisR!=0){
                    y.setText("Plus que "+essaisR+" essais");
                    } else {
                        y.setText(arr.get(i));
                    }
                }else if(e.getText().toString().equalsIgnoreCase(guess)){
                    y.setTextColor(Color.GREEN);
                    y.setText("Bonne réponse");
                }
                break;
            case R.id.NextD:
                y.setTextColor(Color.BLACK);
                t.clearComposingText();
                y.clearComposingText();
                e.clearComposingText();
                arr.clear();
                new JsonTask().execute("https://blague.xyz/api/joke/random");
                break;
        }
    }

}
