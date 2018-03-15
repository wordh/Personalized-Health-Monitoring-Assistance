package com.berry_med.monitordemo.activity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.berry_med.monitordemo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    EditText edit_username;
    EditText edit_email;
    EditText edit_pass;
    Button btn_sing;
    TextView textViewSignUp;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        edit_pass = (EditText) findViewById(R.id.password);
        edit_username = (EditText) findViewById(R.id.username);
        edit_email = (EditText) findViewById(R.id.email);

        btn_sing=(Button)findViewById(R.id.signupbtn);
        textViewSignUp=(TextView)findViewById(R.id.signinfromreg);

        progressDialog = new ProgressDialog(this);
        btn_sing.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);

    }

    private void registerUser(){
        String email=edit_email.getText().toString().trim();
        String pass=edit_pass.getText().toString().trim();
        String username=edit_username.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"Please Enter Name",Toast.LENGTH_SHORT).show();
        }

        progressDialog.setMessage("Registering Please Wait");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    // will show a msg and start profile activity here.
                    Toast.makeText(SignUp.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SignUp.this,"Could not Register. Please Try Again",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {

        if (view==btn_sing){
            registerUser();
        }

        if (view == textViewSignUp){
            //will open login activity
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
