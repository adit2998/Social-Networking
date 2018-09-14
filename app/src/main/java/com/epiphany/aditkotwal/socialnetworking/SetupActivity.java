package com.epiphany.aditkotwal.socialnetworking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SetupActivity extends AppCompatActivity {

    private ImageView setup_profile;
    private EditText setup_username, setup_fullname, setup_country;
    private Button setup_submit;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    String current_usr_id;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        setup_profile = (ImageView)findViewById(R.id.setup_profile_icon);
        setup_username = (EditText)findViewById(R.id.setup_username);
        setup_fullname = (EditText)findViewById(R.id.setup_fullname);
        setup_country = (EditText)findViewById(R.id.setup_country);
        setup_submit = (Button)findViewById(R.id.setup_submit);
        current_usr_id = mAuth.getCurrentUser().getUid();
        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_usr_id);

        setup_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveAccountSetupInfo();
            }
        });
    }

    public void SaveAccountSetupInfo(){
        String username = setup_username.getText().toString();
        String fullname = setup_fullname.getText().toString();
        String country = setup_country.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(fullname)){
            Toast.makeText(this, "Please enter full name", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(country)){
            Toast.makeText(this, "Please enter country name", Toast.LENGTH_SHORT).show();
        }
        else{

            loadingBar.setTitle("Saving information");
            loadingBar.setMessage("Please wait while information is updated");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
            userMap.put("username", username);
            userMap.put("fullname", fullname);
            userMap.put("countryname", country);
            userMap.put("gender", "none");
            userMap.put("relationship_status", "none");
            userMap.put("DOB", "none");

            UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        SendUserToMainActivity();
                        Toast.makeText(SetupActivity.this, "Your account has been created successfully", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                    else{
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error occured: "+message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    public void SendUserToMainActivity(){
        Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
