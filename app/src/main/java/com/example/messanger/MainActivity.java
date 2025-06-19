package com.example.messanger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onLogin(View v){
        Intent intent=new Intent(this,Login.class);
        startActivity(intent);
    }
    public void onSignIn(View v){
        Intent intent=new Intent(this,SignIn.class);
        startActivity(intent);
    }
}