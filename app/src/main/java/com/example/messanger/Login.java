package com.example.messanger;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
public class Login extends AppCompatActivity {
    String Appid="";
    EditText email;
    EditText password;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection mongoCollection;
    User user;
    Button onstartlogin;
    public static final String EMAIL="com.example.messanger.extra.email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.EmailAddressLogin);
        password=findViewById(R.id.PasswordLogin);
        onstartlogin=findViewById(R.id.button);
        Intent intent=new Intent(this,MainPage.class);
        Intent intent2=new Intent(this,addMembers.class);
        Realm.init(this);
        onstartlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App app=new App(new AppConfiguration.Builder(Appid).build());
                Intent intentlog=getIntent();
                String usernamelog=intentlog.getStringExtra(USERNAME.USERNAME1);
                String emailval=email.getText().toString();
                String passwordval=password.getText().toString();
                Log.v("User","message got"+emailval);
                Credentials credentials=Credentials.emailPassword(emailval,passwordval);
                app.loginAsync(credentials, new App.Callback<User>() {
                    public void onResult(App.Result<User> result) {
                        if(result.isSuccess()){
                            intent.putExtra(EMAIL,emailval);
                            intent2.putExtra(EMAIL,emailval);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(Login.this, "incorrect val", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                if (usernamelog!=null) {
                    user = app.currentUser();
                    mongoClient = user.getMongoClient("mongodb-atlas");
                    mongoDatabase = mongoClient.getDatabase("MessageUsers");
                    mongoCollection = mongoDatabase.getCollection("UserName");
                    Document document = new Document("Userid", user.getId()).append("Username", usernamelog).append("Useremail",emailval);
                    mongoCollection.insertOne(document).getAsync(result -> {
                        if (result.isSuccess()) {
                            Log.v("Data", "done");
                            Log.v("msg","name"+usernamelog);
                        } else {
                            Log.v("Data", "Failed" + result.getError().toString());
                        }
                        ;
                    });
                }
            }
        });
    }
}