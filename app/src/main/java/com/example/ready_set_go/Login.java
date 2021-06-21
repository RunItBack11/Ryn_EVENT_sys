package com.example.ready_set_go;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    Button createacc,forgotpass,login;
    EditText emailad,pass;
    FirebaseAuth FB;
    AlertDialog.Builder resetpass;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FB = FirebaseAuth.getInstance();
        resetpass = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
        createacc = findViewById(R.id.Createacc_button);
        createacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (getApplicationContext(), Registration.class));
            }
        });

        emailad = findViewById(R.id.Username);
        pass = findViewById(R.id.Password);
        login = findViewById(R.id.Loginbutton);
        forgotpass = findViewById(R.id.forgotpassword);

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = inflater.inflate(R.layout.activity_resetpass_popup,null);
                resetpass.setTitle("Reset password?")
                        .setMessage("Please enter your email address to get the reset link.")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                EditText email = view.findViewById(R.id.emailresetpass);
                                if(email.getText().toString().isEmpty())
                                {
                                    email.setError("Email is required!");
                                    return;
                                }

                                FB.sendPasswordResetEmail(email.getText().toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                Toast.makeText(Login.this,"Reset email sent",Toast.LENGTH_SHORT).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull  Exception e) {

                                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                        }).setNegativeButton("Cancel", null).setView(view).create().show();


            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(emailad.getText().toString().isEmpty())
                {
                    emailad.setError("Please enter an email address");
                    return;
                }

                if(pass.getText().toString().isEmpty())
                {
                    pass.setError("Please enter a password");
                    return;
                }
                //check die ade isi pape ke x
                FB.signInWithEmailAndPassword(emailad.getText().toString(), pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        Toast.makeText(Login.this,"You have succesfully logged in!",Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

    }

    /*@Override
    protected void onStart() {
        super.onStart();
        {   //check klau user dh penah login ke belom, klau dah die terus ke main page x pyh login balik
            if(FirebaseAuth.getInstance().getCurrentUser()!= null)
            {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }
    }*/


}