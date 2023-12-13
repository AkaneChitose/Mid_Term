package com.example.mid_term;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateActivity extends AppCompatActivity {
    private EditText updateTopic, updateDesc, updateTag;
    private Button updateButton;
    private ImageView updateImage;
    private String key, imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Ánh xạ các thành phần giao diện với mã nguồn
        updateTopic = findViewById(R.id.UpdateTopic);
        updateDesc = findViewById(R.id.UpdateDesc);
        updateTag = findViewById(R.id.UpdateTag);
        updateButton = findViewById(R.id.UpdateButton);
        updateImage = findViewById(R.id.UpdateImage);

        // Nhận dữ liệu từ Intent gửi từ DetailActivity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Hiển thị dữ liệu chi tiết lên giao diện UpdateActivity
            updateTopic.setText(bundle.getString("Title"));
            updateDesc.setText(bundle.getString("Description"));
            updateTag.setText(bundle.getString("Tag"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(imageUrl).into(updateImage);
        }

        // Xử lý sự kiện khi người dùng nhấn nút Update
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức để cập nhật dữ liệu
                updateData();
            }
        });
    }

    private void updateData() {
        // Lấy dữ liệu mới từ các trường nhập liệu
        String updatedTitle = updateTopic.getText().toString();
        String updatedDesc = updateDesc.getText().toString();
        String updatedTag = updateTag.getText().toString();

        // Cập nhật dữ liệu trong Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DEMO").child(key);

        DataClass updatedData = new DataClass(updatedTitle, updatedDesc, updatedTag, imageUrl);

        databaseReference.setValue(updatedData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Hiển thị thông báo cập nhật thành công
                    Toast.makeText(UpdateActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();

                    // Gửi dữ liệu mới về DetailActivity để cập nhật giao diện
                    Intent intent = new Intent();
                    intent.putExtra("Title", updatedTitle);
                    intent.putExtra("Description", updatedDesc);
                    intent.putExtra("Tag", updatedTag);
                    setResult(Activity.RESULT_OK, intent);
                    finish(); // Kết thúc UpdateActivity
                } else {
                    // Hiển thị thông báo cập nhật thất bại
                    Toast.makeText(UpdateActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
