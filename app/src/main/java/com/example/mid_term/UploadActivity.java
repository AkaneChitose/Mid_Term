package com.example.mid_term;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class UploadActivity extends AppCompatActivity {

    ImageView uploadImage;
    Button SaveButton;
    EditText UploadTopic, UploadDesc, UploadTag;
    Uri uri;
    String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        uploadImage = findViewById(R.id.UploadImage);
        UploadDesc = findViewById(R.id.UploadDesc);
        UploadTopic = findViewById(R.id.UploadTopic);
        UploadTag = findViewById(R.id.UploadTag);
        SaveButton = findViewById(R.id.SaveButton);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            assert data != null;
                            uri = data.getData();
                            uploadImage.setImageURI(uri);
                        }
                        else {
                            Toast.makeText(UploadActivity.this, "No image selected!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void saveData() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image").child(Objects.requireNonNull(uri.getLastPathSegment()));

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.processing_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Use Firebase Storage to upload the image.
        storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    // Once the upload is successful, get the download URL of the uploaded image.
                    Task<Uri> uriTask = task.getResult().getStorage().getDownloadUrl();

                    // Use addOnSuccessListener to handle the success of getting the download URL.
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri urlImage) {
                            imageURL = urlImage.toString();
                            dialog.dismiss();

                            // Call uploadData() method here to upload other data after getting the image URL
                            uploadData();
                        }
                    });
                } else {
                    // If the upload fails, dismiss the processing dialog.
                    dialog.dismiss();
                }
            }
        });
    }
    public void uploadData() {
        String title = UploadTopic.getText().toString();
        String desc = UploadDesc.getText().toString();
        String tag = UploadTag.getText().toString();

        DataClass dataClass = new DataClass(title, desc, tag, imageURL);

        // Use push() to generate a unique key for each data entry under the "DEMO" node.
        DatabaseReference demoRef = FirebaseDatabase.getInstance().getReference("DEMO").push();

        demoRef.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UploadActivity.this, "Saved", Toast.LENGTH_SHORT).show();

                    // Finish the current activity (no need to start MainActivity again)
                    finish();
                } else {
                    Log.e("UploadActivity", "Upload failed: " + Objects.requireNonNull(task.getException()).getMessage());
                    Toast.makeText(UploadActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("UploadActivity", "Upload failed: " + e.getMessage());
                Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}