package com.example.fizz.financewizard;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private DbHelperCategory cHelper;
    private SQLiteDatabase cDataBase;
    Button signupBtn, loginBtn;
    TextInputLayout signupEmail, signupPass, signupCPass, loginEmail, loginPass;
    Cursor gCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        cHelper = new DbHelperCategory(this);
        cDataBase = cHelper.getWritableDatabase();
        gCursor = cDataBase.rawQuery("SELECT * FROM " + DbHelperCategory.TABLE_LOGIN, null);

        if (gCursor.getCount() > 0) {
            //Toast.makeText(getApplicationContext(), "Data present", Toast.LENGTH_LONG).show();
            setTitle("Login");
            setContentView(R.layout.activity_login);

            loginEmail = (TextInputLayout) findViewById(R.id.loginEmail);
            loginPass = (TextInputLayout) findViewById(R.id.loginPassword);
            loginBtn = (Button) findViewById(R.id.loginBtn);

            loginBtn.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!loginEmail.getEditText().getText().toString().equals("") && !loginPass.getEditText().getText().toString().equals("")){
                        gCursor.moveToFirst();
                        if(gCursor.getString(gCursor.getColumnIndex(DbHelperCategory.KEY_IDLOGIN)).equals(loginEmail.getEditText().getText().toString()) && gCursor.getString(gCursor.getColumnIndex(DbHelperCategory.PASSWORD_LOGIN)).equals(loginPass.getEditText().getText().toString())) {

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }else{
                            loginEmail.getEditText().setText("");
                            loginPass.getEditText().setText("");
                            Toast.makeText(getApplicationContext(),"Invalid User ID or Password",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        loginEmail.getEditText().setText("");
                        loginPass.getEditText().setText("");
                        Toast.makeText(getApplicationContext(),"Please enter all fields",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            setTitle("SignUp");
            setContentView(R.layout.activity_signup);
            signupEmail = (TextInputLayout) findViewById(R.id.signupEmail);
            signupPass = (TextInputLayout) findViewById(R.id.signupPassword);
            signupCPass = (TextInputLayout) findViewById(R.id.signupCPassword);
            signupBtn = (Button) findViewById(R.id.signupBtn);

            signupBtn.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!signupPass.getEditText().getText().toString().equals("") && signupPass.getEditText().getText().toString().equals(signupCPass.getEditText().getText().toString()) && signupPass.getEditText().getText().length() >= 6) {
                        ContentValues values = new ContentValues();
                        values.put(DbHelperCategory.KEY_IDLOGIN, signupEmail.getEditText().getText().toString());
                        values.put(DbHelperCategory.PASSWORD_LOGIN, signupPass.getEditText().getText().toString());
                        cDataBase.insert(DbHelperCategory.TABLE_LOGIN, null, values);
                        cDataBase.close();
                        Toast.makeText(getApplicationContext(), "Sign-up Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }else if (signupEmail.getEditText().getText().toString().length() <=0 && signupPass.getEditText().getText().toString().length() <=0){
                        signupEmail.getEditText().setText("");
                        signupPass.getEditText().setText("");
                        signupCPass.getEditText().setText("");
                        signupEmail.getEditText().setFocusable(true);
                        Toast.makeText(getApplicationContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                    } else if(signupPass.getEditText().getText().length() < 6) {
                        signupEmail.getEditText().setText("");
                        signupPass.getEditText().setText("");
                        signupCPass.getEditText().setText("");
                        signupEmail.getEditText().setFocusable(true);
                        Toast.makeText(getApplicationContext(), "Password length too short (At least 6 characters)", Toast.LENGTH_SHORT).show();
                    }  else{
                        signupEmail.getEditText().setText("");
                        signupPass.getEditText().setText("");
                        signupCPass.getEditText().setText("");
                        signupEmail.getEditText().setFocusable(true);
                        Toast.makeText(getApplicationContext(), "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
