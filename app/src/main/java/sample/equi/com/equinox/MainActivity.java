package sample.equi.com.equinox;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView listUsers;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<UserProfileDbModel> users;
    private UserAdapter userAdapter;
    private PreferencesManager preferencesManager;
    private GoogleProgressBar loader;
    private Drawable progressDrawable;
    private SwipeRefreshLayout refreshList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init(MainActivity.this);
        setListeners();

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

    private void setListeners() {

        refreshList.setOnRefreshListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    void init(Context context){
        Fresco.initialize(this);
        progressDrawable = new ChromeFloatingCirclesDrawable.Builder(this)
                .colors(getProgressDrawableColors())
                .build();
        preferencesManager = new PreferencesManager(context);
        refreshList = findViewById(R.id.srl_refresh_user_list);
        loader = findViewById(R.id.gp_loader);
        listUsers = findViewById(R.id.rv_list_users);
        layoutManager = new LinearLayoutManager(context);
        listUsers.setLayoutManager(layoutManager);
        listUsers.setItemAnimator(new DefaultItemAnimator());
        users = new ArrayList<>();
        userAdapter = new UserAdapter(context, users);
        listUsers.setAdapter(userAdapter);
        if(preferencesManager.getIsFirstTime()) {
            Log.d("users size ", users.size() + "");
            new SampleApiCall().execute(10 + "");
            Log.d("users size ", users.size() + "");
            preferencesManager.setFirstTime(false);
        } else {
            loader.setVisibility(View.GONE);
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

    @Override
    public void onRefresh() {
        new SampleApiCall().execute(1 + "");
    }


    private class SampleApiCall extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setIndeterminateDrawable(progressDrawable);
        }

        @Override
        protected String doInBackground(String... params) {
            String jsonStr = "";
            RequestBody body = new FormBody.Builder()
                    .build();
            try {
                ServiceHandler sh = new ServiceHandler();
                jsonStr = sh.makeServiceCall("https://randomuser.me/api/?results=" + params[0], sh.GET);
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
                if(refreshList.isRefreshing()){
                    refreshList.setRefreshing(false);
                    Toast.makeText(MainActivity.this, "List updated! New user added at the end!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                if(refreshList.isRefreshing()){
                    refreshList.setRefreshing(false);
                    Toast.makeText(MainActivity.this, "Error. Check your internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}
