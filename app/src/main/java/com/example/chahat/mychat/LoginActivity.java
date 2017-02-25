package com.example.chahat.mychat;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {

    TextView tv;
    EditText etemail,etpwd;

    UserLocalStore userLocalStore;
    SharedPreferences userLocalDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);



        tv= (TextView) findViewById(R.id.tvsignup);
        etemail=(EditText) findViewById(R.id.etemail);
        etpwd=(EditText) findViewById(R.id.etpwd);

        userLocalStore= new UserLocalStore(this);
        userLocalDatabase=this.getSharedPreferences("userDetails", 0);

        if (userLocalDatabase.getBoolean("loggedIn",false) == true) {

            startActivity(new Intent(getBaseContext(),ChatActivity.class));
        }


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Toast.makeText(this,"LogIn Please",Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tvsignupclicklistner(View view)
    {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    public void loginclicklistner(View view)
    {
        String email = etemail.getText().toString();
        String password = etpwd.getText().toString();

        if (!(email.isEmpty()) && !(password.isEmpty()))
        {
            User user = new User(email, password);

            authenticate(user);
        }
        else
        {
            Toast.makeText(this,"Enter your Data",Toast.LENGTH_SHORT).show();
        }
    }

    private void authenticate(User user) {
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.fetchUserDataAsyncTask(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {

                Log.v("fetch userdetail",returnedUser.name);

                if (returnedUser == null) {
                    showErrorMessage();
                } else {
                    logUserIn(returnedUser);
                }
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        dialogBuilder.setMessage("Incorrect user details");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    private void logUserIn(User returnedUser) {
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);
        startActivity(new Intent(this,ChatActivity.class));
    }
}
