package com.example.ready_set_go;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {

    EditText FN,PS,CPS,EA;
    Button REG,LOG;
    FirebaseAuth Fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // assigning a variable to the button/input prompt ids in xml
        FN = findViewById(R.id.FullName);
        PS = findViewById(R.id.Password_Regi);
        CPS = findViewById(R.id.ConfrimPassword);
        EA = findViewById(R.id.EmailAddress);
        REG = findViewById(R.id.Regi_button);
        LOG = findViewById(R.id.Login_Button);

        LOG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        Fauth = FirebaseAuth.getInstance();

        REG.setOnClickListener(new View.OnClickListener() { // when the user presses the register button
            @Override
            public void onClick(View v) {

                // to extract data from the regi form

                String fullname = FN.getText().toString();
                String password = PS.getText().toString();
                String confirmpass = CPS.getText().toString();
                String email = EA.getText().toString();

                // errors if there is no input in the fields of the form

                if(fullname.isEmpty())
                {
                    FN.setError("Full name is required");
                    return;
                }

                if(email.isEmpty())
                {
                    EA.setError("Email address is required");
                    return;
                }

                if(password.isEmpty())
                {
                    PS.setError("Password is required");
                    return;
                }

                if(confirmpass.isEmpty())
                {
                    CPS.setError("Password confirmation is required");
                    return;
                }

                //check wether password and confirm password correct or not

                if(!password.equals(confirmpass))
                {
                    CPS.setError("Password does not match");
                    return;
                }

                //if all are succesful, data is validated

                //Toast.makeText(Registration.this, "Registration succesful", Toast.LENGTH_SHORT).show();
                // fyi: A toast is a view containing a quick little message for the user.
                // The toast class helps you create and show those. When the view is shown to the user,
                // appears as a floating view over the application. It will never receive focus.

                //method used to indicate succesful validation (dkt mane, ape kita nk smpaikan, berapa lame).show() tunjuk ape kita nk smpaikn

                Fauth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //user is sent to the login page
                        startActivity(new Intent (getApplicationContext(), Login.class));
                        finish();
                        //make sure user x leh patah balik ke page sebelum
                        Toast.makeText(Registration.this, "Registration succesful", Toast.LENGTH_SHORT).show();
                        Toast.makeText(Registration.this, "You can now log in!", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(Registration.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });







    }
}