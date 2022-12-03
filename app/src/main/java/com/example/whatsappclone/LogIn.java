package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    private EditText edtPhone, edtPassword;
    private Button btnLogIn, btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);

        btnLogIn = findViewById(R.id.btnLogIn);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        btnLogIn.setOnClickListener(this);
        btnCreateAccount.setOnClickListener(this);

        if(ParseUser.getCurrentUser()!=null){
            transitionToChat();
        }
    }

    @Override
    public void onClick(View btnPressed) {
        switch (btnPressed.getId()){
            case R.id.btnLogIn:
                try {
                    ParseUser.logInInBackground(edtPhone.getText().toString(), edtPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if(user!=null && e==null){
                                transitionToChat();
                            }
                            else{
                                Toast.makeText(LogIn.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                catch(Exception e){
                    Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnCreateAccount:
                Intent intent = new Intent(LogIn.this,CreateAccount.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void transitionToChat(){
        Intent intent = new Intent(LogIn.this,Chat.class);
        startActivity(intent);
        finish();
    }


}