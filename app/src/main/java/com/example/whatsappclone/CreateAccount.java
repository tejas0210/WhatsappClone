package com.example.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener {

    private EditText edtName, edtSPhone, edtSPassword;
    private TextView txtLogin;
    private Button btnConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        setTitle("Create account");

        edtName = findViewById(R.id.edtName);
        edtSPhone = findViewById(R.id.edtSPhone);
        edtSPassword = findViewById(R.id.edtSPassword);
        txtLogin = findViewById(R.id.txtLogin);
        btnConnect = findViewById(R.id.btnConnect);

        btnConnect.setOnClickListener(this);
        txtLogin.setOnClickListener(this);

        if(ParseUser.getCurrentUser()!=null){
            transitionToChat();
        }
    }

    @Override
    public void onClick(View btnPressed) {
        switch (btnPressed.getId()){
            case R.id.btnConnect:
                try {
                    final ParseUser user = new ParseUser();
                    user.setUsername(edtSPhone.getText().toString());
                    user.setPassword(edtSPassword.getText().toString());

                    final ProgressDialog dialog = new ProgressDialog(CreateAccount.this);
                    dialog.setMessage("Connecting...");
                    dialog.show();

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null){
                                transitionToChat();
                            }
                            else{
                                Toast.makeText(CreateAccount.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                catch (Exception e){
                    Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.txtLogin:
                Intent intent = new Intent(CreateAccount.this,LogIn.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void transitionToChat(){
        Intent intent = new Intent(CreateAccount.this,Chat.class);
        startActivity(intent);
        finish();
    }

}