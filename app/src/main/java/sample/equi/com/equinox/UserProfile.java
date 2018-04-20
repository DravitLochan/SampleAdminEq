package sample.equi.com.equinox;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import sample.equi.com.equinox.Models.DB.UserProfileDbModel;

public class UserProfile extends AppCompatActivity {

    UserProfileDbModel user;
    long id;
    TextView name, fullName, emailId, userName, password, dob, phone, cell, registeredOn, gender, street, city, state, postCode;
    SimpleDraweeView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        init(UserProfile.this);
        populateData();
    }

    private void init(Context context) {
        id = Integer.parseInt(getIntent().getExtras().getString("ID"));
        Log.d("id", getIntent().getExtras().getString("ID") + "");
        user = UserProfileDbModel.findById(UserProfileDbModel.class,  id);
        image = findViewById(R.id.sdv_profile_medium_image);
        name = findViewById(R.id.tv_profile_name);
        fullName = findViewById(R.id.tv_profile_full_name);
        emailId = findViewById(R.id.tv_profile_email);
        userName = findViewById(R.id.tv_profile_user_name);
        password = findViewById(R.id.tv_profile_password);
        dob = findViewById(R.id.tv_profile_dob);
        phone = findViewById(R.id.tv_profile_phone);
        cell = findViewById(R.id.tv_profile_cell);
        registeredOn = findViewById(R.id.tv_profile_registered);
        gender = findViewById(R.id.tv_profile_gender);
        street = findViewById(R.id.tv_profile_location_street);
        city = findViewById(R.id.tv_profile_location_city);
        state = findViewById(R.id.tv_profile_location_state);
        postCode = findViewById(R.id.tv_profile_location_post_code);
    }

    private void populateData() {
        Uri uri = Uri.parse(user.getMedium());
        image.setImageURI(uri);
        name.setText(user.getF_name());
        fullName.setText(user.getTitle() + " " + user.getF_name() + " " + user.getL_name());
        emailId.setText(user.getEmail());
        userName.setText(user.getUser_name());
        password.setText(user.getPassword());
        dob.setText(user.getDob());
        phone.setText(user.getPhone());
        cell.setText(user.getCell());
        registeredOn.setText(user.getRegistered());
        gender.setText(user.getGender());
        street.setText(user.getStreet());
        city.setText(user.getCity());
        state.setText(user.getState());
        postCode.setText(user.getPostal_code());
    }
}
