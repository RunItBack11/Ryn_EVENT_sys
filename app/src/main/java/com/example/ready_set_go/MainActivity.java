package com.example.ready_set_go;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE =101;
    CircleImageView profile;
    EditText Fullname,ICnum,Phonenum;
    Button savebtn;
    Uri imageuri;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mref;
    StorageReference stref;
    ProgressDialog mloadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profile = findViewById(R.id.profile_image);
        Fullname = findViewById(R.id.Fullname);
        ICnum = findViewById(R.id.IC_number);
        Phonenum =findViewById(R.id.Telephone_number);
        savebtn = findViewById(R.id.Save_btn);
        mloadingbar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mref = FirebaseDatabase.getInstance().getReference().child("Users");
        stref = FirebaseStorage.getInstance().getReference().child("ProfileImages");

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE);


            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                savedata();

            }
        });


    }

    private void savedata() {

        String fullname = Fullname.getText().toString();
        String HP = Phonenum.getText().toString();
        String IC = ICnum.getText().toString();

        if(fullname.isEmpty() || fullname.length() < 3)
        {
            Fullname.setError("The name you entered is not valid");
        }
        else if(HP.isEmpty() || HP.length() < 10)
        {
            Phonenum.setError("The phone number you entered is not valid");
        }
        else if(IC.isEmpty() || IC.length() < 12)
        {
            ICnum.setError("The IC number you entered is not valid");
        }
        else if(imageuri == null)
        {
            Toast.makeText(this,"Please select an image", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mloadingbar.setTitle("Adding setup profile");
            mloadingbar.setCanceledOnTouchOutside(false);
            mloadingbar.show();
            stref.child(mUser.getUid()).putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                {
                    if(task.isSuccessful()){
                        stref.child(mUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap Hashmap = new HashMap();
                                Hashmap.put("Fullname",Fullname);
                                Hashmap.put("Phone number",Phonenum);
                                Hashmap.put("IC number",ICnum);
                                Hashmap.put("profile",uri.toString());
                                Hashmap.put("status","offline");

                                mref.child(mUser.getUid()).updateChildren(Hashmap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {

                                        startActivity(new Intent(getApplicationContext(),frontpage_community.class));
                                        mloadingbar.dismiss();
                                        Toast.makeText(MainActivity.this,"Profile setup complete",Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        mloadingbar.dismiss();
                                        Toast.makeText(MainActivity.this, e.toString(),Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                        });

                    }

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null)
        {
             imageuri=data.getData();
             profile.setImageURI(imageuri);

        }
    }
}