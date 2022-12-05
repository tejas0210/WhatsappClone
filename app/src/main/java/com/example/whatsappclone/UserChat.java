package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class UserChat extends AppCompatActivity implements View.OnClickListener {

    private ListView userChat;
    private ArrayList<String> chatList;
    private ArrayAdapter arrayAdapter;
    private String selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);

        selectedUser = getIntent().getStringExtra("selectedUser");
        setTitle(selectedUser);

        findViewById(R.id.btnSend).setOnClickListener(this);

        userChat = findViewById(R.id.userChat);
        chatList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,chatList);
        userChat.setAdapter(arrayAdapter);

        try {
            ParseQuery<ParseObject> firstUserQuery = ParseQuery.getQuery("Chat");
            ParseQuery<ParseObject> secondUserQuery = ParseQuery.getQuery("Chat");

            firstUserQuery.whereEqualTo("Sender",ParseUser.getCurrentUser().getUsername());
            firstUserQuery.whereEqualTo("Receiver",selectedUser);

            secondUserQuery.whereEqualTo("Sender",selectedUser);
            secondUserQuery.whereEqualTo("Receiver",ParseUser.getCurrentUser().getUsername());

            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(firstUserQuery);
            allQueries.add(secondUserQuery);

            ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
            myQuery.orderByAscending("createdAt");

            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(objects.size()>0 && e==null){
                        for(ParseObject chatObject : objects){

                            String wMessage = chatObject.get("Message") +"";

                            if(chatObject.get("Sender").equals(ParseUser.getCurrentUser().getUsername())){
                                wMessage = ParseUser.getCurrentUser().getUsername()+" : "+wMessage;
                            }
                            if(chatObject.get("Sender").equals(selectedUser)){
                                wMessage = selectedUser+" : "+wMessage;
                            }

                            chatList.add(wMessage);
                        }
                        arrayAdapter.notifyDataSetChanged();

                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        final EditText edtMessage = findViewById(R.id.edtMessage);

        ParseObject chat = new ParseObject("Chat");
        chat.put("Sender", ParseUser.getCurrentUser().getUsername());
        chat.put("Receiver",selectedUser);
        chat.put("Message",edtMessage.getText().toString());
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText(UserChat.this, "Sent", Toast.LENGTH_SHORT).show();
                    chatList.add(ParseUser.getCurrentUser().getUsername() +" : "+edtMessage.getText().toString());
                    arrayAdapter.notifyDataSetChanged();
                    edtMessage.setText("");
                }
            }
        });
    }
}