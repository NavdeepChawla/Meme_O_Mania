package com.adgvit.meme_o_mania;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class UploadRulesFragment extends Fragment {

    private Button uploadMemeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uploadMemeButton = view.findViewById(R.id.uploadButton);

        uploadMemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NavigationActivity.quizCheck != 1)
                {
                    final Dialog dialog1 = new Dialog(getActivity());
                    dialog1.setCancelable(false);
                    dialog1.setContentView(R.layout.quiz_alert);
                    dialog1.show();

                    ImageView gifImageView = dialog1.findViewById(R.id.gifImageView);

                    Glide.with(getContext())
                            .load(R.drawable.no)
                            .into(gifImageView);

                    Button doneButton = dialog1.findViewById(R.id.doneButton);

                    doneButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                        }
                    });
                }
                else {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new UploadFragment()).commit();
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_rules, container, false);
    }

}
