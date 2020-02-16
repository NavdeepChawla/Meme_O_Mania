package com.adgvit.meme_o_mania;

import android.content.Context;
import android.content.Intent;
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

import com.bumptech.glide.Glide;


public class ResultFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView scoreTextView = view.findViewById(R.id.scoreTextView);
        Button continueButton = view.findViewById(R.id.continueButton);
        ImageView congratsImageView = view.findViewById(R.id.congratsImageView);
        TextView congTextView = view.findViewById(R.id.congTextView);

        int s = ((Quiz)this.getActivity()).getScore();

        String score = s + "/10" + " Score";
        scoreTextView.setText(score);

        if(((Quiz)this.getActivity()).getCheat())
        {
            Glide.with(getContext())
                    .load(R.drawable.tenor)
                    .into(congratsImageView);

            congTextView.setText("Oops! you were caught cheating.");
        }else
        {
            Glide.with(getContext())
                    .load(R.drawable.congrats)
                    .into(congratsImageView);
        }

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),NavigationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

}