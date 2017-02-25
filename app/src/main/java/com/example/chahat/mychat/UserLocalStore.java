package com.example.chahat.mychat;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tundealao on 29/03/15.
 */
public class UserLocalStore {

    public static final String SP_NAME = "userDetails";

    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putString("Name", user.name);
        userLocalDatabaseEditor.putString("Email", user.email);
        userLocalDatabaseEditor.putString("Password", user.password);
        userLocalDatabaseEditor.putLong("Phone", user.phone);
        userLocalDatabaseEditor.commit();
    }



    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putBoolean("loggedIn", loggedIn);
        userLocalDatabaseEditor.commit();
    }

    public void clearUserData() {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.clear();
        userLocalDatabaseEditor.commit();
    }

    public User getLoggedInUser() {
        if (userLocalDatabase.getBoolean("loggedIn", false) == false) {
            return null;
        }

        String name = userLocalDatabase.getString("Name", "");
        String email = userLocalDatabase.getString("Email", "");
        String password = userLocalDatabase.getString("Password", "");
        Long phone = userLocalDatabase.getLong("Phone", -1);

        User user = new User(name, phone, email, password);
        return user;
    }
}
