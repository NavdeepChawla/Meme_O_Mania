package com.adgvit.meme_o_mania;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailForgotEditText;
    private Button resetForgotButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailForgotEditText=findViewById(R.id.emailForgotEditText);
        resetForgotButton=findViewById(R.id.resetForgotButton);

        clickListeners();
    }
    public void clickListeners()
    {
        resetForgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                View view = getCurrentFocus();
                if (view == null) {
                    view = new View(getApplicationContext());
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                if(checkEmpty())
                {
                    if(checkMail())
                    {
                        try {
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            auth.sendPasswordResetEmail(emailForgotEditText.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(ForgotPasswordActivity.this, "Check your Email", Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(ForgotPasswordActivity.this,LoginActivity.class);
                                                startActivity(intent);
                                            }
                                            else{
                                                Toast.makeText(ForgotPasswordActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(ForgotPasswordActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        emailForgotEditText.setError("Please Enter VIT Email");
                        emailForgotEditText.requestFocus();
                    }
                }
            }
        });
    }

    public Boolean checkMail()
    {
        String tempEmail=emailForgotEditText.getText().toString().trim();
        Pattern emailPattern=Pattern.compile("^[a-z]+.[a-z]*[0-9]?201[0-9]@vitstudent.ac.in$");
        Matcher emailMatcher=emailPattern.matcher(tempEmail);
        if(emailMatcher.matches())
        {
            return true;
        }
        return false;
    }

    public Boolean checkEmpty() {
        if (emailForgotEditText.getText().length() == 0) {
            emailForgotEditText.setError("Please enter your email");
            emailForgotEditText.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ForgotPasswordActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
