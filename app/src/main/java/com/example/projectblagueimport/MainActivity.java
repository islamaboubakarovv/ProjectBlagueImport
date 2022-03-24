package com.example.projectblagueimport;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {
    //commentaire de test 
    ListView l;
    ArrayList<String> test = new ArrayList<String>();
    TextView tv;
    ArrayAdapter<String> arr;
    ArrayList<String> favoris = new ArrayList<String>();
    String selec = "";


    //mettre dans test valeurs du parsed JSON
//l'action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //boutons de la barre d'action
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fav:
                if (selec.isEmpty()) {

                } else {
                    favoris.add(selec);
                    System.out.println(favoris);
                }

                break;
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
            case R.id.bluetooth:
                String t = "";
                for (int i = 0; i < 10; i++) {
                    t += "\n" + arr.getItem(i);
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
//test
    //fin des boutons de la barre d'action

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l = findViewById(R.id.txt);

        //tv = (TextView)findViewById(R.id.tv);

        for (int i = 0; i < 5; i++) {
            new JsonTask().execute("https://blague.xyz/api/joke/random");

        }
        //System.out.println(test);
        arr = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, test);

        l.setAdapter(arr);
        //selectionner une blague
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position % 2 == 0) {
                    String s = parent.getItemAtPosition(position).toString();
                    String t = parent.getItemAtPosition(position + 1).toString();
                    String u = "\n" + s + "\n" + t;
                    selec = u;
                    //u est la blague qu'on veut stocker avec la réponse liée
                }

            }
        });
        //fin du code pour sélectionner une blague


    }
    //tourne en background

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
            //tv.setText(result);

        }
    }

    public void btn(View v) {
        switch (v.getId()) {
            case R.id.reload:
                arr.clear();
                for (int i = 0; i < 5; i++) {
                    new JsonTask().execute("https://blague.xyz/api/joke/random");
                }
                break;
            case R.id.fav:
                if(selec.isEmpty()){

                }else{
                    Intent int2 = new Intent(MainActivity.this,favoris.class);
                    int2.putExtra("fav",favoris);
                    startActivity(int2);
                }

                break;

        }
    }

}

//}