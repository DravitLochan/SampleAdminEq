package sample.equi.com.equinox;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import sample.equi.com.equinox.Adapters.UserAdapter;
import sample.equi.com.equinox.Common.ServiceHandler;
import sample.equi.com.equinox.Models.DB.UserProfileDbModel;
import sample.equi.com.equinox.Models.GSON.UserProfileGsonModel;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listUsers;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<UserProfileDbModel> users;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init(MainActivity.this);
        // new SampleApiCall().execute();
        // todo : add a splash screen and make it default launcher.


    }

    void init(Context context){
        Fresco.initialize(this);
        listUsers = findViewById(R.id.rv_list_users);
        layoutManager = new LinearLayoutManager(context);
        listUsers.setLayoutManager(layoutManager);
        listUsers.setItemAnimator(new DefaultItemAnimator());
        users = new ArrayList<>();
        fetchUsersFromDb();
        userAdapter = new UserAdapter(context, users);
        listUsers.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
    }

    private void fetchUsersFromDb() {
        List<UserProfileDbModel> dbUsers = UserProfileDbModel.listAll(UserProfileDbModel.class);
        for (int i = 0; i < dbUsers.size(); i++) {
            users.add(dbUsers.get(i));
        }
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
            try {
                JSONObject obj = new JSONObject(s);
                JSONArray jsonArray = new JSONArray(obj.getString("results"));
                UserProfileGsonModel userProfileGsonModel;
                UserProfileDbModel user;
                for (int i = 0; i < jsonArray.length(); i++) {
                    userProfileGsonModel = new Gson().fromJson(jsonArray.get(i).toString(), UserProfileGsonModel.class);
                    UserProfileGsonModel.Name name = userProfileGsonModel.getName();
                    UserProfileGsonModel.Location location = userProfileGsonModel.getLocation();
                    UserProfileGsonModel.Login login = userProfileGsonModel.getLogin();
                    UserProfileGsonModel.Picture picture = userProfileGsonModel.getPicture();
                    user = new UserProfileDbModel(name.getTitle(), name.getFirst(), name.getLast(), location.getStreet(),
                            location.getCity(), location.getState(), location.getPostcode(), userProfileGsonModel.getEmail(),
                            userProfileGsonModel.getPhone(), userProfileGsonModel.getCell(), login.getUsername(), login.getPassword(),
                            picture.getThumbnail(), picture.getMedium(), userProfileGsonModel.getDob(), userProfileGsonModel.getRegistered(),
                            userProfileGsonModel.getGender());
                    user.save();
                }
                // Log.d("db size = ", UserProfileDbModel.listAll(UserProfileDbModel.class).size() + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
