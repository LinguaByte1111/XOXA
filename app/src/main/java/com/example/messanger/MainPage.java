package com.example.messanger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import org.bson.Document;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class MainPage extends AppCompatActivity {
    String Appid="";
    ImageButton add;
    ListView chats;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection mongoCollection;
    MongoCollection mongoCollection2;
    String dataot;
    String chatid;
    String userIdinapp;
    public static final String USERIDINAPP="com.example.messanger.extra.userid";
    public static final String CHATID="com.example.messanger.extra.chatid";
    public static final String USEREMAILMP="com.example.messanger.extra.useremailmp";


    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        add=findViewById(R.id.button4);
        chats=findViewById(R.id.chatlist);

        App app=new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();
        userIdinapp=user.getId();

        ArrayList<String> chatidmem =new ArrayList<>();


        ArrayList<String> member =new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,member);


        Intent intent=new Intent(this,addMembers.class);
        Intent intent2=new Intent(this,chat.class);
        Intent intentlog=getIntent();
        //String userid=intentlog.getStringExtra(Login.USERIDINAPP);
        String useremail=intentlog.getStringExtra(Login.EMAIL);
        String useremailadd=intentlog.getStringExtra(addMembers.EMAILadd);
        intent.putExtra(USERIDINAPP,userIdinapp);
        intent.putExtra(USEREMAILMP,useremail);
        intent2.putExtra(USERIDINAPP,userIdinapp);
        Log.v("User","message got"+useremail+"mp");

        mongoClient=user.getMongoClient("mongodb-atlas");
        mongoDatabase=mongoClient.getDatabase("MessageUsers");
        mongoCollection=mongoDatabase.getCollection("UserName");
        mongoCollection2=mongoDatabase.getCollection("UserPair");

        Document queryFilter=new Document();
        /*RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();


        findTask.getAsync(task->{
            if (task.isSuccess()){
                MongoCursor<Document> res = task.get();
                while (res.hasNext()){
                    Document currentDoc=res.next();
                    if(currentDoc.getString("Useremail").equals(useremail)){
                        String username=currentDoc.getString("Username");
                    }
                }
            }
            else {
                Log.v("task error",task.getError().toString());
            }
        });

         */


        //Document queryFilter=new Document().append("Userid",userid);
        chats.setClickable(true);
        RealmResultTask<MongoCursor<Document>> findTask2 = mongoCollection2.find(queryFilter).iterator();


        findTask2.getAsync(task->{
            if (task.isSuccess()){
                MongoCursor<Document> res = task.get();
                while (res.hasNext()){
                    int i = 0;
                    Document currentDoc=res.next();
                    Log.v("User","message got"+useremail+"mp2");
                    if(useremail!=null){
                        if (currentDoc.getString("sender")!=null){
                            if(currentDoc.getString("sender").equals(useremail)) {

                                dataot = currentDoc.getString("reciever");
                                chatid=currentDoc.getString("chatid");
                                member.add(dataot);
                                chatidmem.add(chatid);
                                chats.setAdapter(arrayAdapter);
                                Log.v("User","message got");
                            }
                    }
                        Log.v("User","message got"+useremail+"mp");
                        if (currentDoc.getString("reciever")!=null){
                            if (currentDoc.getString("reciever").equals(useremail)){
                                dataot = currentDoc.getString("sender");
                                chatid=currentDoc.getString("chatid");
                                member.add(dataot);
                                chatidmem.add(chatid);
                                chats.setAdapter(arrayAdapter);
                                Log.v("User","message got");
                    }
                    }else {
                        Log.v("User","no chat found");
                    }
                    }else {
                        if (currentDoc.getString("sender")!=null){
                            if(currentDoc.getString("sender").equals(useremailadd)) {
                                //if (currentDoc.getString("filled data") != null) {

                                dataot = currentDoc.getString("reciever");
                                chatid=currentDoc.getString("chatid");
                                member.add(dataot);
                                chatidmem.add(chatid);
                                chats.setAdapter(arrayAdapter);

                                Log.v("User","message got");
                                //memberemail=currentDoc.getString("Useremail");


                            }
                        }
                        Log.v("User","message got"+useremailadd+"mp");
                        if (currentDoc.getString("reciever")!=null){
                            if (currentDoc.getString("reciever").equals(useremailadd)){
                                dataot = currentDoc.getString("sender");
                                chatid=currentDoc.getString("chatid");
                                member.add(dataot);
                                chatidmem.add(chatid);
                                chats.setAdapter(arrayAdapter);

                                Log.v("User","message got");
                            }
                        }else {
                            Log.v("User","no chat found");
                        }
                    }

                }
            }
            else {
                Log.v("task error",task.getError().toString());
            }
        });
        //intent.putExtra(CHATID,chatid);
        chats.setClickable(true);

        chats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String chatidnow = chatidmem.get(position);
                intent.putExtra(CHATID,chatidnow);
                intent2.putExtra(CHATID,chatidnow);
                startActivity(intent2);
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);

            }
        });

    }
}