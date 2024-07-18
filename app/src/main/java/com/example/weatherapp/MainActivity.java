package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {


    Button btn;
    TextView txt1,txt2,txt3,txt4,txt5;
    EditText edt;

    public class Downloadable extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                Log.i("URL", urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                reader.close();
                connection.disconnect();
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Failed";
            } catch (IOException e) {
                e.printStackTrace();
                return "Failed";
            }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("Response", s); // Log the raw response
            JSONObject jsonObject=null;
            try {
                 jsonObject = new JSONObject(s);
                JSONObject location = jsonObject.getJSONObject("location");

                JSONObject current=jsonObject.getJSONObject("current");
                JSONObject conditionObject = current.getJSONObject("condition");
                String conditionText = conditionObject.getString("text");
                double windKph = current.getDouble("wind_kph");
                String temp=current.getString("temp_c");
                String lat = location.getString("lat");
                String lon = location.getString("lon");
                txt1.setText("Latitude"+" => "+lat+" deg");
                txt2.setText("Longtitude"+" => "+lon+" deg");
                txt3.setText("Temperature"+" => "+temp+" Cel");
                txt4.setText("Weather"+" => "+conditionText);
                txt5.setText("Windspeed"+" => "+String.valueOf(windKph)+" Kph");
                txt1.setVisibility(View.VISIBLE);
                txt2.setVisibility(View.VISIBLE);
                txt3.setVisibility(View.VISIBLE);
                txt4.setVisibility(View.VISIBLE);
                txt5.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSONError", "Error parsing JSON: " + e.getMessage());
            }
        }





    }



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            btn = findViewById(R.id.btn);
            txt1 = findViewById(R.id.txt1);
            txt2=findViewById(R.id.txt2);
            txt3 = findViewById(R.id.txt3);
            txt4=findViewById(R.id.txt4);
            txt5 = findViewById(R.id.txt5);
            edt = findViewById(R.id.edt);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Downloadable downloadable=new Downloadable();
                    try {
                        String locaion=edt.getText().toString();
                        String s=downloadable.execute("http://api.weatherapi.com/v1/current.json?key=dbfcb2115a8f4eb1902183234241807&q="+locaion+"&aqi=no").get();
                    } catch (ExecutionException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        }
    public  void  func(View view){
        txt1.setVisibility(View.INVISIBLE);
        txt2.setVisibility(View.INVISIBLE);
        txt3.setVisibility(View.INVISIBLE);
        txt4.setVisibility(View.INVISIBLE);
        txt5.setVisibility(View.INVISIBLE);
    }


}
