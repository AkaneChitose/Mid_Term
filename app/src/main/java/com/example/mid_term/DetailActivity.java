package com.example.mid_term;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.style.BulletSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class DetailActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle;
    ImageView detailImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailDesc = findViewById(R.id.detailDesc);
        detailTitle = findViewById(R.id.detailTitle);
        detailImage = findViewById(R.id.detailImage);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            detailDesc.setText(bundle.getString("Description"));
            detailTitle.setText(bundle.getString("Title"));
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }
//        ExtendedFloatingActionButton extendedFab = findViewById(R.id.extendedFab);
//        extendedFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle the click event for the extended FAB
//                // Show additional options or perform other actions
//            }
//        });


    }


}