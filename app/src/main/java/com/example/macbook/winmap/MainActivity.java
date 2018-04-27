package com.example.macbook.winmap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private ProgressBar mProgressBar;
    private SharedPreferences mySharedPref;

    private EditText mEtCompanyName;
    private EditText mETAdressCompany;
    private boolean mIsSeller = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mySharedPref = getSharedPreferences("SP", MODE_PRIVATE);

        final EditText etLastname = findViewById(R.id.et_lastname);
        final EditText etFirstname = findViewById(R.id.et_firstname);
        final EditText etEmail = findViewById(R.id.et_email);
        final EditText etPassword = findViewById(R.id.et_password);
        final EditText etConfirmPassword = findViewById(R.id.et_confirm_password);
        mEtCompanyName = findViewById(R.id.et_company_name);
        mETAdressCompany = findViewById(R.id.et_adress_company);
        Button btnSignup = findViewById(R.id.btn_signup);
        TextView tvGotoLogin = findViewById(R.id.tv_go_to_login);
        mProgressBar = findViewById(R.id.signup_progress_bar);
        mProgressBar.setVisibility(View.GONE);


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String lastname = etLastname.getText().toString().trim();
                final String firstname = etFirstname.getText().toString().trim();
                final String email = etEmail.getText().toString().trim();
                final String password = etPassword.getText().toString().trim();
                final String confirmPassword = etConfirmPassword.getText().toString().trim();
                final String companyName = mEtCompanyName.getText().toString().trim();
                final String adressCompany = mETAdressCompany.getText().toString().trim();

                if (lastname.isEmpty()
                        || firstname.isEmpty()
                        || email.isEmpty()
                        || password.isEmpty()
                        || confirmPassword.isEmpty()){
                    Toast.makeText(MainActivity.this,"", Toast.LENGTH_SHORT).show();
                }else {

                    if (!password.equals(confirmPassword)) {
                        etPassword.setText("");
                        etConfirmPassword.setText("");
                        Toast.makeText(MainActivity.this, "mot de passe different",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Veuillez entrer votre e-mail",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getApplicationContext(),
                                "Veuillez entrer votre mot de passe",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (TextUtils.isEmpty(confirmPassword)) {
                        Toast.makeText(getApplicationContext(),
                                "Veuillez confirmer vitre mot de passe",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (password.length() < 6) {
                        etPassword.setText("");
                        etConfirmPassword.setText("");
                        Toast.makeText(MainActivity.this,
                                "Votre mot de passe doit contenir au moins 6 caractère",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    mProgressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    mProgressBar.setVisibility(View.GONE);

                                    if (!task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this,
                                                "erreur lors de la création du compte",
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(MainActivity.this,
                                                "Compte créé avec succès",
                                                Toast.LENGTH_LONG).show();

                                        UserModel myUser = new UserModel(firstname, lastname, email,mIsSeller,companyName,adressCompany);
                                        signupUserInDatabase(myUser);
                                        signUserInSharedPref(myUser);
                                        Intent intent = new Intent(MainActivity.this,
                                                MapsActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                }
            }
        });
        tvGotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetSharedPreferences();
                Intent intentLogin = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intentLogin);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //  mProgressBar.setVisibility(View.GONE);
    }

    public void onBackPressed() {
        Intent i = new Intent(MainActivity.this, MainActivity.class);
        startActivity(i);
    }

    private void resetSharedPreferences(){
        mySharedPref.edit().putBoolean("isAlreadyUser", false).apply();
        mySharedPref.edit().putString("lastName", "").apply();
        mySharedPref.edit().putString("firstName", "").apply();
        mySharedPref.edit().putString("email", "").apply();
    }

    public void signupUserInDatabase(UserModel myUser){
        //Initialize Firebase components
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersDatabaseReference = database.getReference().child("users");
        // On enregistre le user en DB et dans les SP
        usersDatabaseReference.push().setValue(myUser);
    }

    public void signUserInSharedPref(UserModel myUser){
        mySharedPref.edit().putBoolean("isAlreadyUser", true).apply();
        mySharedPref.edit().putString("lastName", myUser.getLastname()).apply();
        mySharedPref.edit().putString("firstName", myUser.getFirstname()).apply();
        mySharedPref.edit().putString("email", myUser.getEmail()).apply();
    }
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioBuyer:
                if (checked) {
                    mEtCompanyName.setVisibility(View.GONE);
                    mETAdressCompany.setVisibility(View.GONE);
                    mIsSeller = false;
                }
                break;
            case R.id.radioSeller:
                if (checked) {
                    mEtCompanyName.setVisibility(View.VISIBLE);
                    mETAdressCompany.setVisibility(View.VISIBLE);
                    mIsSeller = true;
                }
                break;
        }
    }
}
