package com.example.colin.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private Button SignInButton;
    private Button SignUpButton;
    private EditText Email;
    private EditText Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Context c = this;

        Email = (EditText) findViewById(R.id.Email);
        Password = (EditText) findViewById(R.id.Password);
        SignInButton = (Button) findViewById(R.id.sign_in_button);
        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebApi api = WebApi.getInstance(c);
                api.GetUserInfo(new ResponseListener() {
                    @Override
                    public void Respond(String s) {
                        try {
                            new User(new JSONObject(s));
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }catch(Exception e){

                        }
                    }
                }, Email.getText().toString(), Password.getText().toString());
            }
        });

        SignUpButton = (Button) findViewById(R.id.sign_up_button);
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebApi api = WebApi.getInstance(c);
                api.Register("DEMO",Email.getText().toString().trim(),Password.getText().toString().trim());
                //WebApi api = WebApi.getInstance(c);
                //api.CheckDryerCycle("45");
                //api.RegisterUser("test", Email.getText().toString(), Password.getText().toString());
                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent);
            }
        });
    }
}
