package com.adgvit.meme_o_mania;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameSignUp;
    private EditText emailSignUp;
    private EditText regnoSignUp;
    private EditText passwordSignUp;
    private FirebaseAuth mAuth;

    private Button signupButton;
    private LinearLayout loginLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        nameSignUp=findViewById(R.id.nameSignUpEditText);
        emailSignUp=findViewById(R.id.emailSignUpEditText);
        regnoSignUp=findViewById(R.id.regnoSignUpEditText);
        passwordSignUp=findViewById(R.id.passwordSignUpEditText);
        signupButton=findViewById(R.id.signupSignUpButton);
        loginLayout=findViewById(R.id.loginSignUpLinearLayout);

        signupButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = getCurrentFocus();
                if (view == null) {
                    view = new View(getApplicationContext());
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                if(checkEmpty())
                {
                    if(checkReg()) {
                        if (checkMail()) {
                            mAuth.createUserWithEmailAndPassword(emailSignUp.getText().toString().trim(), passwordSignUp.getText().toString())
                                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                try {

                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference myref=database.getReference().child("users").child(emailSignUp.getText().toString().trim().replace('.','_'));
                                                    myref.child("Name").setValue(nameSignUp.getText().toString());
                                                    myref.child("RegNo").setValue(regnoSignUp.getText().toString().trim());
                                                    myref.child("Count").setValue(0);
                                                    myref.child("Upload").setValue(0);

                                                    Intent intent = new Intent(SignUpActivity.this, ConfirmSignUp.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }
                                                catch (Exception e)
                                                {
                                                    Toast.makeText(SignUpActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(SignUpActivity.this, "Sign Up Failed.", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                        } else {
                            emailSignUp.setError("Please use VIT Email");
                            emailSignUp.requestFocus();
                        }
                    }
                    else
                    {
                        regnoSignUp.setError("Please check your Registration Number");
                        regnoSignUp.requestFocus();
                    }
                }
            }
        });

        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    public Boolean checkReg()
    {
        String tempReqNo=regnoSignUp.getText().toString().trim();
        Pattern regPattern=Pattern.compile("^1[0-9][A-Z][A-Z][A-Z][0-9][0-9][0-9][0-9]$");
        Matcher matcher=regPattern.matcher(tempReqNo);
        if(matcher.matches())
        {
            return true;
        }
        return false;
    }

    public Boolean checkMail()
    {
        String tempEmail=emailSignUp.getText().toString().trim();
        String tempReqNo=regnoSignUp.getText().toString().trim();
        String subreg=tempReqNo.substring(1,2);
        Pattern emailPattern=Pattern.compile("^[a-z]+.[a-z]*[0-9]?201[0-9]@vitstudent.ac.in$");
        Matcher emailMatcher=emailPattern.matcher(tempEmail);
        if(emailMatcher.matches())
        {
            return tempEmail.contains(subreg);
        }
        return false;
    }

    public Boolean checkEmpty()
    {
        if(nameSignUp.getText().length()==0)
        {
            nameSignUp.setError("Please enter your name");
            nameSignUp.requestFocus();
            return false;
        }
        else if(regnoSignUp.getText().length()==0)
        {
            regnoSignUp.setError("Please enter your registration number");
            regnoSignUp.requestFocus();
            return false;
        }
        else if(emailSignUp.getText().length()==0)
        {
            emailSignUp.setError("Please enter your email");
            emailSignUp.requestFocus();
            return false;
        }
        else if(passwordSignUp.getText().length()==0)
        {
            passwordSignUp.setError("Please enter a password");
            passwordSignUp.requestFocus();
            return false;
        }
        return true;
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
}
