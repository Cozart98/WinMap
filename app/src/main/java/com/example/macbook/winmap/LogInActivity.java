package com.example.macbook.winmap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LogInActivity extends AppCompatActivity {


    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private FirebaseAuth mAuth;
    private UserModel myUser;
    private ProgressDialog mProgDial;
    private List<UserModel> mUsertab = new ArrayList<>();
    private SharedPreferences mySharedPref;
    //private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        //Get Firebase mAuth instance
        mAuth = FirebaseAuth.getInstance();
        mySharedPref = getSharedPreferences("SP", MODE_PRIVATE);

        final EditText etEmail = findViewById(R.id.et_email_login);
        final EditText etPassword = findViewById(R.id.et_password_login);
        Button btnLogin = findViewById(R.id.btn_login);
        TextView gotoSignup = findViewById(R.id.tv_login_goto_signup);
        TextView resetPassword = findViewById(R.id.tv_login_reset_password);

        gotoSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLogin = new Intent(LogInActivity.this,
                        MainActivity.class);
                startActivity(intentLogin);
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLogin = new Intent(LogInActivity.this,
                        ResetPasswordActivity.class);
                startActivity(intentLogin);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = etEmail.getText().toString().trim();
                final String password = etPassword.getText().toString().trim();

                if (etEmail.getText().toString().isEmpty()
                        || etPassword.getText().toString().isEmpty()) {
                    Toast.makeText(LogInActivity.this, R.string.champ_email_mdp_vide,
                            Toast.LENGTH_SHORT).show();
                } else {

                    mProgDial = new ProgressDialog(LogInActivity.this);
                    mProgDial.setIndeterminate(false);
                    mProgDial.setCancelable(false);
                    mProgDial.setMessage("chargement");
                    mProgDial.show();

                    mDatabase = FirebaseDatabase.getInstance();
                    mUsersDatabaseReference = mDatabase.getReference().child("users/");

                    mUsersDatabaseReference.addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (mUsertab.size() > 0) {
                                        mUsertab.clear();
                                    }
                                    for (DataSnapshot sp : dataSnapshot.getChildren()) {
                                        myUser = sp.getValue(UserModel.class);
                                        mUsertab.add(myUser);
                                        if (myUser.getEmail().equals(email)) {
                                            signInPgm(email, password);
                                            return;
                                        }
                                    }
                                    if (!myUser.getEmail().equals(email)) {
                                        mProgDial.dismiss();
                                        Toast.makeText(LogInActivity.this,
                                                R.string.auth_incorrect, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(LogInActivity.this, "utilisateur inconnu",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }

    public void signUserInSharedPref(UserModel myUser){
        mySharedPref.edit().putBoolean("isAlreadyUser", true).apply();
        mySharedPref.edit().putString("lastName", myUser.getLastname()).apply();
        mySharedPref.edit().putString("firstName", myUser.getFirstname()).apply();
        mySharedPref.edit().putString("email", myUser.getEmail()).apply();
    }

    public void signInPgm(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LogInActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // mProgressBar.setVisibility(View.GONE);

                                if (!task.isSuccessful()) {
                                    mProgDial.dismiss();
                                } else {
                                    signUserInSharedPref(myUser);
                                    Intent intent = new Intent(LogInActivity.this,
                                            MapsActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
    }
}
