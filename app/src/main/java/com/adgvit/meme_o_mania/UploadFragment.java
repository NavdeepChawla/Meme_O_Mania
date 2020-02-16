package com.adgvit.meme_o_mania;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

public class UploadFragment extends Fragment {

    private ImageView uploadButton, uploadedGIF;
    private final int IMAGE_CODE = 101;
    private Uri imageUri;
    private String imageName = "";
    private StorageReference storageReference;
    private AVLoadingIndicatorView progressBar;
    private LinearLayout checkUpload;
    private TextView message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uploadButton = view.findViewById(R.id.upload);
        progressBar = view.findViewById(R.id.progressRing);
        checkUpload = view.findViewById(R.id.checkUpload);
        message = view.findViewById(R.id.message);
        uploadedGIF = view.findViewById(R.id.uploadedGIF);

        storageReference = FirebaseStorage.getInstance().getReference();

        check();

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_CODE);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if(requestCode == IMAGE_CODE)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setCancelable(false)
                        .setTitle("Confirm upload")
                        .setMessage("Are you sure you want to upload the selected meme?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if(data != null) {
                                    imageUri = data.getData();
                                    Log.i("INFO", data.getData().toString());
                                    Glide
                                            .with(getContext())
                                            .load(imageUri)
                                            .into(uploadButton);
                                    uploadMeme();
                                }
                                
                                else
                                {
                                    Toast.makeText(getContext(), "No file chosen", Toast.LENGTH_SHORT).show();
                                }
                                
                            }
                        });

                AlertDialog alertDialog = builder.create();

                alertDialog.show();

                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                positiveButton.setBackground(null);
                negativeButton.setBackground(null);

                positiveButton.setTextColor(Color.BLACK);
                negativeButton.setTextColor(Color.BLACK);

            }
            
        } catch (Exception e){
            Toast.makeText(getContext(), "Error getting image", Toast.LENGTH_SHORT).show();
        }
    }

    private void check() {
        if(NavigationActivity.uploadCheck == 1)
        {
            checkUpload.setVisibility(View.VISIBLE);
            Glide
                    .with(getContext())
                    .load(R.drawable.upoaded)
                    .into(uploadedGIF);
            message.setVisibility(View.GONE);
            uploadButton.setVisibility(View.GONE);
        }
        else
        {
            checkUpload.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);
            uploadButton.setVisibility(View.VISIBLE);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadMeme() {

        if(imageUri != null) {
            byte[] bytes = null;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                bytes = stream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageName = NavigationActivity.regNo + "." + getFileExtension(imageUri);

            StorageReference storageReference1 = storageReference.child(imageName);

            UploadTask uploadTask = storageReference1.putBytes(bytes);

            uploadTask
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.INVISIBLE);
                    uploadButton.setEnabled(true);

                    NavigationActivity.uploadCheck = 1;

                    DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("users").child(NavigationActivity.email.replace('.','_'));

                    myref.child("Upload").setValue(1);

                    Gson gson = new Gson();
                    String json = gson.toJson(NavigationActivity.uploadCheck);
                    NavigationActivity.sharedPreferences.edit().putInt("uploadCheck",Integer.parseInt(json)).apply();
                    check();

                    final Dialog dialog1 = new Dialog(getActivity());
                    dialog1.setCancelable(false);
                    dialog1.setContentView(R.layout.upload_alert);
                    dialog1.show();

                    ImageView gifImageView = dialog1.findViewById(R.id.gifImageView);

                    Glide.with(getContext())
                            .load(R.drawable.done)
                            .into(gifImageView);

                    Button doneButton = dialog1.findViewById(R.id.doneButton);

                    doneButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                        }
                    });
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.VISIBLE);
                            uploadButton.setEnabled(false);
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            uploadButton.setEnabled(true);
                            Toast.makeText(getContext(), "Error occurred. Try Again", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
        else
        {
            Toast.makeText(getContext(),"No File Chosen",Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

}
