package sample.equi.com.equinox;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import sample.equi.com.equinox.Common.ServiceHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new SampleApiCall().execute();
        // todo : add a splash screen and make it default launcher.


    }

    private class SampleApiCall extends AsyncTask<Void, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String jsonStr = "";
            RequestBody body = new FormBody.Builder()
                    .build();
            try {
                ServiceHandler sh = new ServiceHandler();
                jsonStr = sh.makeServiceCall("https://randomuser.me/api/?results=10", sh.GET);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("jsonStr", jsonStr);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }
}
