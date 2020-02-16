package com.adgvit.meme_o_mania;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText emailLogin;
    private EditText passwordLogin;

    private Button loginButton;
    private LinearLayout signupLayout;
    private TextView forgotTextView;
    private String temp;
    public static FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLogin=findViewById(R.id.emailForgotEditText);
        passwordLogin=findViewById(R.id.passwordLoginEditText);
        loginButton=findViewById(R.id.loginLoginButton);
        signupLayout=findViewById(R.id.signUpLoginLinearLayout);
        forgotTextView=findViewById(R.id.forgotLoginTextView);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null)
        {
            {
                final SharedPreferences sharedPref = getSharedPreferences("com.adgvit.meme_o_mania", Context.MODE_PRIVATE);

                final Gson gson = new Gson();
                Type type = new TypeToken<String>(){}.getType();
                final String[] json = {sharedPref.getString("email", "Email")};
                String userEmail = gson.fromJson(json[0],type);
                final String tempEmail = userEmail.replace('.', '_');

                DatabaseReference tempRef= FirebaseDatabase.getInstance().getReference().child("users").child(tempEmail);
                tempRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                            String Values = ds.getKey();
                            assert Values != null;
                            if ("Name".equalsIgnoreCase(Values)) {
                                temp = ds.getValue().toString();
                                Log.i("INFO_1","Name:"+ temp);

                                json[0] = gson.toJson(temp);
                                sharedPref.edit().putString("name", json[0]).apply();
                            } else if ("RegNo".equalsIgnoreCase(Values)) {
                                temp = ds.getValue().toString();
                                Log.i("INFO_1","RegNo:"+ temp);

                                json[0] = gson.toJson(temp);
                                sharedPref.edit().putString("regNo", temp).apply();
                            }
                            else if ("Count".equalsIgnoreCase(Values)) {
                                temp = ds.getValue().toString();
                                Log.i("INFO_1","Count:"+ temp);

                                json[0] = gson.toJson(temp);
                                sharedPref.edit().putInt("count",Integer.parseInt(temp)).apply();
                            }
                            else if("Upload".equalsIgnoreCase(Values)){
                                temp = ds.getValue().toString();
                                Log.i("INFO_1","Upload:"+ temp);

                                json[0] = gson.toJson(temp);
                                sharedPref.edit().putInt("uploadCheck",Integer.parseInt(temp)).apply();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent intent=new Intent(LoginActivity.this,NavigationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
            Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        clickListeners();

    }

    public void clickListeners()
    {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = getCurrentFocus();
                if (view == null) {
                    view = new View(getApplicationContext());
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                if(checkEmpty()){
                    if(checkMail()){
                        firebaseAuth.signInWithEmailAndPassword(emailLogin.getText().toString().trim(), passwordLogin.getText().toString())
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            final SharedPreferences sharedPref = getSharedPreferences("com.adgvit.meme_o_mania", Context.MODE_PRIVATE);
                                            String userEmail = (emailLogin.getText()).toString().trim();
                                            final String tempEmail = userEmail.replace('.', '_');

                                            final Gson gson = new Gson();
                                            final String[] json = {gson.toJson(userEmail)};
                                            sharedPref.edit().putString("email", json[0]).apply();

                                            DatabaseReference tempRef= FirebaseDatabase.getInstance().getReference().child("users").child(tempEmail);
                                            tempRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                                                        String Values = ds.getKey();
                                                        assert Values != null;
                                                        if ("Name".equalsIgnoreCase(Values)) {
                                                            temp = ds.getValue().toString();
                                                            Log.i("INFO_1","Name:"+ temp);

                                                            json[0] = gson.toJson(temp);
                                                            sharedPref.edit().putString("name", json[0]).apply();
                                                        } else if ("RegNo".equalsIgnoreCase(Values)) {
                                                            temp = ds.getValue().toString();
                                                            Log.i("INFO_1","RegNo:"+ temp);

                                                            json[0] = gson.toJson(temp);
                                                            sharedPref.edit().putString("regNo", temp).apply();
                                                        }
                                                        else if ("Count".equalsIgnoreCase(Values)) {
                                                            temp = ds.getValue().toString();
                                                            Log.i("INFO_1","Count:"+ temp);

                                                            json[0] = gson.toJson(temp);
                                                            sharedPref.edit().putInt("count",Integer.parseInt(temp)).apply();
                                                        }
                                                        else if("Upload".equalsIgnoreCase(Values)){
                                                            temp = ds.getValue().toString();
                                                            Log.i("INFO_1","Upload:"+ temp);

                                                            json[0] = gson.toJson(temp);
                                                            sharedPref.edit().putInt("uploadCheck",Integer.parseInt(temp)).apply();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                            Intent intent=new Intent(LoginActivity.this,NavigationActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);

                                        }
                                        else{
                                            Toast.makeText(LoginActivity.this, "Log In Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else
                    {
                        emailLogin.setError("Please Enter VIT Email");
                        emailLogin.requestFocus();
                    }
                }
            }
        });

        signupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        forgotTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    public Boolean checkMail()
    {
        String tempEmail=emailLogin.getText().toString().trim();
        Pattern emailPattern=Pattern.compile("^[a-z]+.[a-z]*[0-9]?201[0-9]@vitstudent.ac.in$");
        Matcher emailMatcher=emailPattern.matcher(tempEmail);
        if(emailMatcher.matches())
        {
            return true;
        }
        return false;
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public Boolean checkEmpty()
    {
        if(emailLogin.getText().length()==0)
        {
            emailLogin.setError("Please enter your email");
            emailLogin.requestFocus();
            return false;
        }
        else if(passwordLogin.getText().length()==0)
        {
            passwordLogin.setError("Please enter your password");
            passwordLogin.requestFocus();
            return false;
        }
        return true;
    }

}
