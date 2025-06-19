package com.example.messanger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

public class USERNAME extends AppCompatActivity {
    String Appid="";
    EditText usernamegiven;
    Button name;
    String usernameval;
    public static final String USERNAME1="com.example.messanger.extra.Username";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);
        Intent intent=new Intent(this,Login.class);
        App app=new App(new AppConfiguration.Builder(Appid).build());




        usernamegiven=findViewById(R.id.UserNameInput);
        name=findViewById(R.id.NameContinue);
        Realm.init(this);


        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameval=usernamegiven.getText().toString();
                Log.v("msg","name"+usernameval);
                intent.putExtra(USERNAME1,usernameval);
                startActivity(intent);
            }
        });



    }
}