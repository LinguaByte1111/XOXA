package com.example.messanger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class addMembers extends AppCompatActivity {
    String Appid="" +
            "";

    EditText personnameentered;
    Button search;
    ListView memberlist;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection mongoCollection;
    MongoCollection mongoCollection2;
    User user;
    ImageButton add;
    final String[] data = new String[1];
    String memberemail;
    public static final String EMAILadd="com.example.messanger.extra.emailadd";
    String useremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);
        personnameentered=findViewById(R.id.PersonName);
        search=findViewById(R.id.button5);
        memberlist=findViewById(R.id.memberlist);
        add=findViewById(R.id.button6);

        App app=new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();

        ArrayList<String> member =new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,member);
        Intent intentlog=getIntent();
        //String userid=intentlog.getStringExtra(MainPage.USERIDINAPP);
        Intent intentlog2=getIntent();
        String useremailcheck=intentlog.getStringExtra(Login.EMAIL);
        if(useremailcheck!=null){
            useremail=useremailcheck;
        }else {
            useremail=intentlog.getStringExtra(MainPage.USEREMAILMP);
        }
        String useremailmp=intentlog.getStringExtra(MainPage.USEREMAILMP);
        mongoClient=user.getMongoClient("mongodb-atlas");
        mongoDatabase=mongoClient.getDatabase("MessageUsers");
        mongoCollection=mongoDatabase.getCollection("UserName");
        mongoCollection2=mongoDatabase.getCollection("UserPair");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Document queryFilter=new Document();
                RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();


                findTask.getAsync(task->{
                    if (task.isSuccess()){
                        Log.v("User","sucess search");
                        MongoCursor<Document> res = task.get();
                        while (res.hasNext()){
                            Document currentDoc=res.next();
                            if(currentDoc.getString("Username").equals(personnameentered.getText().toString())) {
                                //if (currentDoc.getString("filled data") != null) {

                                data[0] = currentDoc.getString("Username");
                                member.add(data[0]);
                                memberlist.setAdapter(arrayAdapter);
                                Log.v("User","message got");
                                memberemail=currentDoc.getString("Useremail");


                                //}
                            }
                        }
                    }
                    else {
                        Log.v("task error",task.getError().toString());
                    }
                });
            }
        });

/*
        Document queryFilter=new Document().append("Userid",userid);
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();


        findTask.getAsync(task->{
            if (task.isSuccess()){
                MongoCursor<Document> res = task.get();
                while (res.hasNext()){
                    Document currentDoc=res.next();
                    if(currentDoc.getString("Username").equals(personnameentered)) {
                    //if (currentDoc.getString("filled data") != null) {

                        data[0] = currentDoc.getString("Username");
                        member.add(data[0]);
                        memberlist.setAdapter(arrayAdapter);
                        Log.v("User","message got");
                        memberemail=currentDoc.getString("Useremail");


                    //}
                     }
                }
            }
            else {
                Log.v("task error",task.getError().toString());
            }
        });

 */
        Intent intent=new Intent(this,MainPage.class);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Document document = new Document("Userid",user.getId()).append("sender",useremail)
                        .append("reciever",memberemail)
                        .append("chatid",useremail+"x"+memberemail);
                mongoCollection2.insertOne(document).getAsync(result -> {
                    if (result.isSuccess()) {
                        Log.v("Data", "done");
                        //personnameentered.setText("");
                        intent.putExtra(EMAILadd,useremail);
                        startActivity(intent);



                    } else {
                        Log.v("Data", "Failed" + result.getError().toString());
                    }
                    ;

                });

            }
        });



    }
}