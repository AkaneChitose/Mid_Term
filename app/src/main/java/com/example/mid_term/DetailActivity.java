package com.example.mid_term;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {

    private TextView detailDesc, detailTitle;
    private ImageView detailImage;
    private FloatingActionButton deleteButton, editButton;
    private String key = "";
    private String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Liên kết các thành phần giao diện với mã nguồn
        detailDesc = findViewById(R.id.detailDesc);
        detailTitle = findViewById(R.id.detailTitle);
        detailImage = findViewById(R.id.detailImage);
        deleteButton = findViewById(R.id.deleteBtn);
        editButton = findViewById(R.id.editBtn);

        // Nhận dữ liệu từ Intent gửi từ Activity trước đó
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Hiển thị dữ liệu chi tiết lên giao diện
            detailDesc.setText(bundle.getString("Description"));
            detailTitle.setText(bundle.getString("Title"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }

        // Xử lý sự kiện khi người dùng nhấn nút Delete
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức xóa dữ liệu
                deleteData();
            }
        });
    }

    // Phương thức để xóa dữ liệu từ Firebase Database và Firebase Storage
    private void deleteData() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DEMO");

        // Sử dụng FirebaseStorage.getInstance() để lấy một đối tượng Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);

        // Xóa hình ảnh từ Firebase Storage
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // Xóa dữ liệu từ Firebase Database
                reference.child(key).removeValue();

                // Hiển thị thông báo xóa thành công
                Toast.makeText(DetailActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();

                // Chuyển về màn hình MainActivity
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish(); // Đóng Activity hiện tại
            }
        });
    }
}