package com.example.messanger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.bson.Document;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class SignIn extends AppCompatActivity {
    String Appid="";
    EditText email;
    EditText password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email=findViewById(R.id.EmailAddressSignIn);
        password=findViewById(R.id.PasswordSignIn);
        Realm.init(this);
    }
    public void onContinue(View v){
        App app=new App(new AppConfiguration.Builder(Appid).build());


        Intent intent=new Intent(this,USERNAME.class);

        String emailval=email.getText().toString();
        String passwordval=password.getText().toString();



        app.getEmailPassword().registerUserAsync(emailval,passwordval,it->{
            if (it.isSuccess()){


                startActivity(intent);
            }
            else {
                Toast.makeText(SignIn.this, "RETRY", Toast.LENGTH_SHORT).show();
                Log.v("Data", "Failed" + it.getError().toString());
            }
        });


    }
}