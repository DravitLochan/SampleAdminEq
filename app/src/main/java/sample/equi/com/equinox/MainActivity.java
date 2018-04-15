package sample.equi.com.equinox;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.jpardogo.android.googleprogressbar.library.ChromeFloatingCirclesDrawable;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import sample.equi.com.equinox.Adapters.UserAdapter;
import sample.equi.com.equinox.Common.PreferencesManager;
import sample.equi.com.equinox.Common.ServiceHandler;
import sample.equi.com.equinox.Models.DB.UserProfileDbModel;
import sample.equi.com.equinox.Models.GSON.UserProfileGsonModel;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listUsers;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<UserProfileDbModel> users;
    private UserAdapter userAdapter;
    private PreferencesManager preferencesManager;
    private GoogleProgressBar loader;
    private Drawable progressDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init(MainActivity.this);
        // new SampleApiCall().execute();
        // todo : add a splash screen and make it default launcher.

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // todo : add an option to search
        if(id == R.id.mi_search) {
            Toast.makeText(MainActivity.this, "Search will be live soon", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void init(Context context){
        Fresco.initialize(this);
        progressDrawable = new ChromeFloatingCirclesDrawable.Builder(this)
                .colors(getProgressDrawableColors())
                .build();
        preferencesManager = new PreferencesManager(context);
        loader = (GoogleProgressBar) findViewById(R.id.gp_loader);
        listUsers = findViewById(R.id.rv_list_users);
        layoutManager = new LinearLayoutManager(context);
        listUsers.setLayoutManager(layoutManager);
        listUsers.setItemAnimator(new DefaultItemAnimator());
        users = new ArrayList<>();
        userAdapter = new UserAdapter(context, users);
        listUsers.setAdapter(userAdapter);
        if(preferencesManager.getIsFirstTime()) {
            Log.d("users size ", users.size() + "");
            new SampleApiCall().execute();
            Log.d("users size ", users.size() + "");
            preferencesManager.setFirstTime(false);
        } else {
            fetchUsersFromDb();
        }
    }

    private int[] getProgressDrawableColors() {
        int[] colors = new int[4];
        colors[0] = getResources().getColor(R.color.red);
        colors[1] = getResources().getColor(R.color.blue);
        colors[2] = getResources().getColor(R.color.yellow);
        colors[3] = getResources().getColor(R.color.green);
        return colors;
    }

    private void fetchUsersFromDb() {
        users.clear();
        List<UserProfileDbModel> dbUsers = UserProfileDbModel.listAll(UserProfileDbModel.class);
        for (int i = 0; i < dbUsers.size(); i++) {
            users.add(dbUsers.get(i));
        }
        userAdapter.notifyDataSetChanged();
    }

    private class SampleApiCall extends AsyncTask<Void, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setIndeterminateDrawable(progressDrawable);
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
                    fetchUsersFromDb();
                }
                // Log.d("db size = ", UserProfileDbModel.listAll(UserProfileDbModel.class).size() + "");
                loader.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
