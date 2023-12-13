package com.example.mid_term;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public class UpdateActivity extends AppCompatActivity {

    ImageView updateImage;
    Button updateButton;
    EditText updateDesc, upDateTitle, updateTag;
    String title, desc, tag;
    String imageUrl, key, oldImageURL;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        updateButton = findViewById(R.id.UpdateButton);
        updateDesc = findViewById(R.id.UpdateDesc);
        updateButton = findViewById(R.id.UpdateButton);
        updateButton = findViewById(R.id.UpdateButton);
        updateButton = findViewById(R.id.UpdateButton);
    }
}