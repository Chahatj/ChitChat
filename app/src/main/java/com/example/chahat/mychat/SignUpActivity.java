package com.example.chahat.mychat;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends ActionBarActivity {

    EditText etname, etphone, etemail, etpassword,etconpwd;

    String acname,mobile_no,Myemail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etname = (EditText) findViewById(R.id.etname);
        etphone = (EditText) findViewById(R.id.etphone);
        etemail = (EditText) findViewById(R.id.etemail);
        etpassword = (EditText) findViewById(R.id.etpwd);
        etconpwd=(EditText) findViewById(R.id.etcpwd);

        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccounts();
        for (Account ac : accounts)
        {
            acname = ac.name;

            if (acname.startsWith("91")) {
                mobile_no = acname;
            }else if(acname.endsWith("@gmail.com")){
                Myemail = acname;
            }

            // Take your time to look at all available accounts
            Log.i("Accounts : ", "Accounts : " + Myemail);
        }

    }

    public void signinbtnclicklistner(View view)
    {
        String name = etname.getText().toString();
        String email = etemail.getText().toString();
        String password = etpassword.getText().toString();
        String conpwd=etconpwd.getText().toString();

if ( !(email.isEmpty()) && !(password.isEmpty()) && !(name.isEmpty()) && !((etphone.getText().toString()).isEmpty()))
{
    if (password.equals(conpwd))
    {

        if (etphone.getText().toString().length()==10)
        {
            if (email.equals(Myemail))
            {
                long phone = Long.parseLong(etphone.getText().toString());

                User user = new User(name, phone, email, password);
                registerUser(user);
            }
            else
            {
                Toast.makeText(this,"Invalid Email",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this,"Invalid Phone Number",Toast.LENGTH_SHORT).show();
        }
    }
    else
    {
        Toast.makeText(getBaseContext(), "Please Confirm Password", Toast.LENGTH_SHORT).show();
    }
}
else
{
    Toast.makeText(getBaseContext(), "Enter your Data", Toast.LENGTH_SHORT).show();

}


    }

    private void registerUser(User user) {
        ServerRequests serverRequest = new ServerRequests(this);
        serverRequest.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                Toast.makeText(getBaseContext(),"You Signed In",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
