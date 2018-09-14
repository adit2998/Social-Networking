package com.epiphany.aditkotwal.socialnetworking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private ImageView register_logo;
    private EditText register_email, register_password, register_confirm_password;
    private Button register_create_button;
    private FirebaseAuth mAuth;
   private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_logo = (ImageView)findViewById(R.id.register_logo);
        register_email = (EditText)findViewById(R.id.register_email);
        register_password = (EditText)findViewById(R.id.register_password);
        register_confirm_password = (EditText)findViewById(R.id.register_confirm_password);
        register_create_button = (Button) findViewById(R.id.register_create_button);
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        register_create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewAccount();
            }
        });
    }

    public void CreateNewAccount(){
        String email = register_email.getText().toString();
        String password = register_password.getText().toString();
        String confirm_password = register_confirm_password.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(confirm_password)){
            Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
        }

        else if(!password.equals(confirm_password)){
            Toast.makeText(this, "Please check password", Toast.LENGTH_SHORT).show();
        }
        else{
                loadingBar.setTitle("Creating new Account");
                loadingBar.setMessage("Please wait while account is being created");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                SendUserToSetupActivty();
                                Toast.makeText(RegisterActivity.this, "You are authenticated Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }

                            else{
                                String message=task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, "Error occured"+message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            SendUserToMainActivty();
        }
    }

    public void SendUserToMainActivty(){
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();

    }


    public void SendUserToSetupActivty(){
        Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
        startActivity(setupIntent);
        finish();
    }
}
