package sample.equi.com.equinox.DB;

import com.orm.SugarRecord;

/**
 * Created by DravitLochan on 15-04-2018.
 */

public class UserProfile extends SugarRecord {

    // Name
    String title;
    String f_name;
    String l_name;

    // Location
    String street;
    String city;
    String state;
    String postal_code;

    // Contact
    String email;
    String phone;
    String cell;

    // Login
    String user_name;
    String password;

    // Images
    String thumbnail;
    String medium;

    // Miscellaneous
    String dob;
    String registered;

    // much required by library
    public UserProfile() {
    }

    public UserProfile(String title, String f_name, String l_name, String street, String city, String state, String postal_code, String email, String phone, String cell, String user_name, String password, String thumbnail, String medium, String dob, String registered) {
        this.title = title;
        this.f_name = f_name;
        this.l_name = l_name;
        this.street = street;
        this.city = city;
        this.state = state;
        this.postal_code = postal_code;
        this.email = email;
        this.phone = phone;
        this.cell = cell;
        this.user_name = user_name;
        this.password = password;
        this.thumbnail = thumbnail;
        this.medium = medium;
        this.dob = dob;
        this.registered = registered;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }
}
