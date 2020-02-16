package com.adgvit.meme_o_mania;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class AboutUsFragment extends Fragment {

    private TabHost tabHost;
    private TextView nameTextView, regNoTextView, emailTextView;
    private Button logoutButton;
    private FirebaseAuth firebaseAuth;
    private ImageView imtap, impap, imtp;
    private CardView privacyPolicy, contactUs;
    private final int CALL_CODE = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    String papvit = "https://play.google.com/store/apps/details?id=com.namankhurpia.paper";
    String tapvit = "https://play.google.com/store/apps/details?id=patel.abhay.adg_tap_app";
    String privacy = "https://akshit8.github.io/privacy-policy.github.io/";
    String taskPicker = "https://taskpicker.adgvit.com/";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabHost = view.findViewById(R.id.tabHost);
        tabHost.setup();

        nameTextView = view.findViewById(R.id.nameTextView);
        regNoTextView = view.findViewById(R.id.regNoTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        logoutButton = view.findViewById(R.id.logoutButton);
        privacyPolicy = view.findViewById(R.id.privacyPolicy);
        contactUs = view.findViewById(R.id.contactUs);
        nameTextView.setText(NavigationActivity.name);
        regNoTextView.setText(NavigationActivity.regNo);
        emailTextView.setText(NavigationActivity.email);

        imtap =view.findViewById(R.id.imtap);
        impap = view.findViewById(R.id.impap);
        imtp = view.findViewById(R.id.imtp);

        firebaseAuth = FirebaseAuth.getInstance();

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog1 = new Dialog(getActivity());
                dialog1.setCancelable(false);
                dialog1.setContentView(R.layout.contact_card);
                dialog1.show();

                final Button doneButton = dialog1.findViewById(R.id.doneButton);
                Button callButton = dialog1.findViewById(R.id.callButton);

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });

                callButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                        {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},CALL_CODE);
                        }
                        else
                        {
                            callPhone();
                        }
                    }
                });
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();

                NavigationActivity.sharedPreferences.edit().clear().apply();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData((Uri.parse(privacy)));
                startActivity(intent);
            }
        });

        TabHost.TabSpec spec = tabHost.newTabSpec("TAB ONE");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Profile");
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("TAB TWO");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Team");
        tabHost.addTab(spec);

        int tab = tabHost.getCurrentTab();
        for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            // When tab is not selected
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#EEEEF0"));
            TextView tv = tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(14);
        }
        // When tab is selected
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#FFFFFF"));
        TextView tv = tabHost.getTabWidget().getChildAt(tab).findViewById(android.R.id.title);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(14);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                int tab = tabHost.getCurrentTab();
                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    // When tab is not selected
                    tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#EEEEF0"));
                    TextView tv = tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                    tv.setTextColor(Color.BLACK);
                    tv.setTextSize(14);
                }
                // When tab is selected
                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#FFFFFF"));
                TextView tv = tabHost.getTabWidget().getChildAt(tab).findViewById(android.R.id.title);
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(14);
            }
        });

        imtap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData((Uri.parse(tapvit)));
                startActivity(intent);
            }
        });
        impap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData((Uri.parse(papvit)));
                startActivity(intent);
            }
        });
        imtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData((Uri.parse(taskPicker)));
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED && requestCode == CALL_CODE) {
                callPhone();
            }
        }
    }

    public void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + "+919099946404"));
        if (getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }
}
