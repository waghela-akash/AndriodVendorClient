package com.example.waghela.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendGetRequest(View View) {
        new GetClass(this).execute();
    }

    private class GetClass extends AsyncTask<String, Void, Void> {

        private final Context context;

        public GetClass(Context c){
            this.context = c;
        }
        ProgressDialog progress;
        protected void onPreExecute(){
            progress= new ProgressDialog(this.context);
            progress.setMessage("Loading");
            progress.show();
        }
        ArrayAdapter<String> listAdapter;

        @Override
        protected Void doInBackground(String... params) {
            try {

                final TextView outputView = (TextView) findViewById(R.id.showOutput);
                final ListView outputView1ist = (ListView) findViewById(R.id.listview);
                URL url = new URL("http://172.24.1.17:8080/api/items");

                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");

                int responseCode = connection.getResponseCode();

                final StringBuilder output = new StringBuilder("Request URL " + url);

                output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder("{ \"value\" :");
                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                    System.out.println(line + '\n');
                }
                responseOutput.append("}");
                br.close();

                List<String> items = new ArrayList<String>();

                String strJson = responseOutput.toString();
                try{
                    JSONObject  jsonRootObject = new JSONObject(strJson);
                    JSONArray jsonArray = jsonRootObject.optJSONArray("value");
                    for(int i=0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String name = jsonObject.optString("name").toString();
                        String cost = jsonObject.optString("cost").toString();
                        String quantity = jsonObject.optString("quantity").toString();

                        items.add(name +"\n\tCost: "+ cost +"    Quantity: " + quantity);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                listAdapter = new ArrayAdapter<String>(
                        this.context,
                        android.R.layout.simple_list_item_1,
                        items);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        outputView.setText(output);
                        outputView1ist.setAdapter(listAdapter);
                        progress.dismiss();

                    }
                });

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
