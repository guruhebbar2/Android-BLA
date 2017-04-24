package com.example.gp.demo1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity {
    EditText urlText ;
    String text = "";
    String postRes1;
    String postRes2;

    Button urlButton;
    Button validLicBtn;
    Button invalidLicBtn;
    Button expiredLicBtn;
    String responseData;
    String result;
    String strUrl = "http://www.telusko.com/addition.htm?t1=3&t2=10";
    public static final String editTextKey = "etxtKey";
    public static final String myPref = "MyPref" ;
    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urlText = (EditText)findViewById(R.id.urlText);

        sharedPreferences = getSharedPreferences(myPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(editTextKey,strUrl);
//        editor.commit();
        urlText.setText(sharedPreferences.getString(editTextKey,strUrl));

    }

    public void onChangeUrl(View view){
        urlText = (EditText)findViewById(R.id.urlText);

        sharedPreferences = getSharedPreferences(myPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String changeUrl = urlText.getText().toString();
        editor.putString(editTextKey,changeUrl);
        editor.commit();


        Toast.makeText(this, "Url Changed to : " + sharedPreferences.getString(editTextKey,null), Toast.LENGTH_SHORT).show();

    }

    public void onValidLic(View view){
        Toast.makeText(this, "Entering Valid Lic Method .......", Toast.LENGTH_SHORT).show();

        new asyncClass().execute();

    }

    public void onInvalidLic(View view){
        Toast.makeText(this, "Entering Invalid Lic Method !!!", Toast.LENGTH_SHORT).show();
    }

    public void onExpiredLic(View view){
        Toast.makeText(this, "Entering onExpiredLic Method ++++++", Toast.LENGTH_SHORT).show();
    }

    public class asyncClass extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            sharedPreferences = getSharedPreferences(myPref,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String changeUrl = sharedPreferences.getString(editTextKey,strUrl);
            Toast.makeText(MainActivity.this, "Connecting url :" + changeUrl , Toast.LENGTH_SHORT).show();


            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(MainActivity.this, "Response Result :" + result, Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this, "Post Successful : " + postRes1, Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this, "Post Successful : " + postRes2, Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(String... params) {
            sharedPreferences = getSharedPreferences(myPref,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String changeUrl = sharedPreferences.getString(editTextKey,strUrl);
            try {
                URL url = new URL(changeUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                responseData = bufferReader.readLine();
                String line = "";
                result = "";
                while((line = bufferReader.readLine()) != null)
                    result += line;

                System.out.println(bufferReader.toString());
                Log.i("Guru",result);
                con.disconnect();
                String data = "{"+"data"+":"+"hello"+"}";
                String data1 ="{"+"data"+":"+"Blahblahblah"+"}";
                postRes1 = postCall("http://requestb.in/zveiufzv",data) + "i went 1st";
                postRes2 = postCall("http://requestb.in/1l037y71",data1)+ "i went 2nd";




            } catch (Exception e) {
                System.out.println(e);
            }

            return null;
        }

        public String postCall(String postUrl,String data){
            InputStream inputStream = null;
            String result = "";
            try {

                URL url = new URL(postUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( data );
                wr.flush();


                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String responseData = bufferReader.readLine();
                String line = "";
                while((line = bufferReader.readLine()) != null)
                    text += line;

                conn.disconnect();
            } catch (Exception e) {

                System.out.println(e.getMessage());

            }
            return text;
        }

        public void getCall(){


        }
    }


}
