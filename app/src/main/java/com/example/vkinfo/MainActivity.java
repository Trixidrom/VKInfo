package com.example.vkinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static com.example.vkinfo.utils.NetworkUtils.generateURL;
import static com.example.vkinfo.utils.NetworkUtils.getResponseFromURL;

public class MainActivity extends AppCompatActivity {
    private EditText searchField;
    private Button searchButton;
    private TextView result;
    private TextView errorMessage;
    private ProgressBar loadingIndicator;
    private String resultingString="";

    private void showTextView(boolean n){
        if(n){
            result.setVisibility(View.VISIBLE);
            errorMessage.setVisibility(View.INVISIBLE);
        }else{
            result.setVisibility(View.INVISIBLE);
            errorMessage.setVisibility(View.VISIBLE);
        }

    }

    class  VKQueryTask extends AsyncTask<URL, Void, String>{
        @Override
        protected void onPreExecute() {
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = getResponseFromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        protected  void onPostExecute(String response){
            String firstName;
            String lastName;
            if (response != null && !response.equals("")) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("response");// по ключу респонс получаем массив

                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject userInfo = jsonArray.getJSONObject(i);
                        firstName = userInfo.getString("first_name");
                        lastName = userInfo.getString("last_name");
                        resultingString += "Имя: " + firstName + "\n" + "Фамилия: " + lastName +"\n\n";
                        result.setText(resultingString);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                showTextView(true);
            } else {
                showTextView(false);
            }
            loadingIndicator.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = findViewById(R.id.et_search_field);
        searchButton = findViewById(R.id.b_search_vk);
        result = findViewById(R.id.tv_result);
        errorMessage = findViewById(R.id.tv_error_message);
        loadingIndicator = findViewById(R.id.pb_loading_indicator);

        searchButton.setOnClickListener(view -> {
            URL gereratedURL = generateURL(searchField.getText().toString());

            new VKQueryTask().execute(gereratedURL);
        });

     }

}