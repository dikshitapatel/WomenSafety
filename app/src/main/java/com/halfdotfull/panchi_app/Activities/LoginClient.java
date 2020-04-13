package com.halfdotfull.panchi_app.Activities;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.halfdotfull.panchi_app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginClient extends AppCompatActivity {
    EditText adhaar,Name,Password;
    Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_client);

        adhaar=(EditText)findViewById(R.id.aadhar);
        Name=(EditText)findViewById(R.id.Name);
        Password=(EditText)findViewById(R.id.Password);
        submitButton=(Button)findViewById(R.id.Submit);

         final String adhaarc=adhaar.getText().toString();
        final String uname=Name.getText().toString();
        final String pwd=Password.getText().toString();

        Log.d("Adhar",adhaarc);
        Log.d("Uname",uname);
        Log.d("pwd",pwd);

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                JSONObject postData = new JSONObject();
                try {
                    postData.put("aadhar_id","123456789098" );
                    postData.put("name", "Dikshita");
                    postData.put("password","abcd");

                    new SendDeviceDetails().execute("https://d2d5a30c.ngrok.io/register", postData.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



    }
}

class SendDeviceDetails extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

        String data = "";

        HttpURLConnection httpURLConnection = null;
        try {

            httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes("PostData=" + params[1]);
            wr.flush();
            wr.close();

            InputStream in = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(in);

            int inputStreamData = inputStreamReader.read();
            while (inputStreamData != -1) {
                char current = (char) inputStreamData;
                inputStreamData = inputStreamReader.read();
                data += current;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.e("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
    }
}

