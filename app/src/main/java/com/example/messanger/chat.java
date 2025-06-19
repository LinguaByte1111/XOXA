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

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmEventStreamAsyncTask;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class chat extends AppCompatActivity {
    String Appid="";
    EditText entermessage;
    ImageButton send;
    ListView chatmessage;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection mongoCollection;
    User user;
    final String[] data = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        entermessage=findViewById(R.id.entermessage);
        send=findViewById(R.id.button7);
        chatmessage=findViewById(R.id.chatmseeage);

        App app=new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();

        ArrayList<String> message =new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,message);

        Intent intentlog=getIntent();
        String userid=intentlog.getStringExtra(MainPage.USERIDINAPP);
        Intent intentlog2=getIntent();
        String chatid=intentlog2.getStringExtra(MainPage.CHATID);
        Log.v("User","message got"+userid);

        mongoClient=user.getMongoClient("mongodb-atlas");
        mongoDatabase=mongoClient.getDatabase("MessageUsers");
        mongoCollection=mongoDatabase.getCollection("Chats");

        Document queryFilter=new Document();
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();


        findTask.getAsync(task->{
            if (task.isSuccess()){
                MongoCursor<Document> res = task.get();
                while (res.hasNext()) {
                    Document currentDoc = res.next();
                    if (currentDoc.getString("chatid") != null){
                        if (currentDoc.getString("chatid").equals(chatid)) {
                            if (currentDoc.getString("message") != null) {

                                data[0] = currentDoc.getString("message");
                                message.add(data[0]);
                                chatmessage.setAdapter(arrayAdapter);
                                Log.v("User", "message got");


                            }
                        }
                    }
                }
            }
            else {
                Log.v("task error",task.getError().toString());
            }
        });
        Log.v("User","message got"+userid);

        RealmEventStreamAsyncTask<MongoCursor<Document>> watcher = mongoCollection.watchAsync();
        watcher.get(result -> {
            if (result.isSuccess()) {
                //Log.v("EXAMPLE", "Event type: " +
                //      result.get().getOperationType() + " full document: " +
                //    result.get().getFullDocument());
                if(result.get().getDocumentKey().get("chatid")!=null){
                    if(result.get().getDocumentKey().get("chatid").equals(chatid)){
                        if(result.get().getDocumentKey().get("message")!=null){
                            data[0] = String.valueOf(result.get().getDocumentKey().get("chatid"));
                            message.add(data[0]);
                            chatmessage.setAdapter(arrayAdapter);
                            Log.v("User","message got");
                        }
                    }
                }




            }
            else {
                Log.v("User","not updated"+result.getError().toString());
            }
        });



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("User","message got"+userid);

                Document document = new Document("Userid",userid).append("message",entermessage.getText().toString())
                        .append("chatid",chatid);
                mongoCollection.insertOne(document).getAsync(result -> {
                    if (result.isSuccess()) {
                        Log.v("Data", "done");
                        entermessage.setText("");

                        Document queryFilter=new Document();
                        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();


                        findTask.getAsync(task->{
                            if (task.isSuccess()){
                                MongoCursor<Document> res = task.get();
                                message.clear();
                                while (res.hasNext()){
                                    Document currentDoc=res.next();

                                    if (currentDoc.getString("chatid") != null){
                                    if(currentDoc.getString("chatid").equals(chatid)) {
                                    if (currentDoc.getString("message") != null) {



                                        data[0] = currentDoc.getString("message");
                                        message.add(data[0]);
                                        chatmessage.setAdapter(arrayAdapter);
                                        Log.v("User","message got");


                                    }
                                     }
                                    }
                                }
                            }
                            else {
                                Log.v("task error",task.getError().toString());
                            }
                        });

                    } else {
                        Log.v("Data", "Failed" + result.getError().toString());
                    }
                    ;

                });
            }
        });


    }
}