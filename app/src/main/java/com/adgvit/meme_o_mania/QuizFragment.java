package com.adgvit.meme_o_mania;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class QuizFragment extends Fragment {

    private ArrayList<questionObject> questionsList = new ArrayList<>();
    private questionObject currentQues;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Random random = new Random(System.currentTimeMillis());

        int sNo = random.nextInt(5) + 1;

        final Button startQuizButton  = view.findViewById(R.id.startQuizButton);
        final DatabaseReference dataBase = FirebaseDatabase.getInstance().getReference("questions").child(Integer.toString(sNo));
        final AVLoadingIndicatorView progressRing = view.findViewById(R.id.progressRing);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("com.adgvit.meme_o_mania", Context.MODE_PRIVATE);
        final int count = sharedPref.getInt("count",0);

        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(count == 1){

                    final Dialog dialog1 = new Dialog(getActivity());
                    dialog1.setCancelable(false);
                    dialog1.setContentView(R.layout.quiz_alert);
                    dialog1.show();

                    ImageView gifImageView = dialog1.findViewById(R.id.gifImageView);

                    Glide.with(getContext())
                            .load(R.drawable.no)
                            .into(gifImageView);

                    TextView message = dialog1.findViewById(R.id.textView3);

                    message.setText(R.string.noReQuiz);

                    Button doneButton = dialog1.findViewById(R.id.doneButton);

                    doneButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                        }
                    });

                }else {
                    startQuizButton.setEnabled(false);
                    startQuizButton.setAlpha(0.3f);
                    progressRing.smoothToShow();

                    dataBase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            questionsList = new ArrayList<>();

                            for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {

                                String key = itemSnapShot.getKey();

                                currentQues = itemSnapShot.getValue(questionObject.class);
                                questionsList.add(currentQues);

                                int count = 1;
                                SharedPreferences sharedPref = getActivity().getSharedPreferences("com.adgvit.meme_o_mania", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putInt("count",count);
                                editor.apply();

                                DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("users").child(NavigationActivity.email.replace('.','_'));

                                myref.child("Count").setValue(1);


                                progressRing.smoothToHide();
                                Intent intent = new Intent(getActivity(), Quiz.class);
                                intent.putExtra("QuestionList", questionsList);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                            progressRing.smoothToHide();
                            Toast.makeText(getActivity(), "Error Loading Quiz", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }


}