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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class UploadActivity extends AppCompatActivity {

    ImageView uploadImage;
    Button saveButton;
    EditText uploadTopic, uploadDesc, uploadTag;
    Uri uri;
    String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // Ánh xạ các thành phần giao diện
        uploadImage = findViewById(R.id.UploadImage);
        uploadDesc = findViewById(R.id.UploadDesc);
        uploadTopic = findViewById(R.id.UploadTopic);
        uploadTag = findViewById(R.id.UploadTag);
        saveButton = findViewById(R.id.SaveButton);

        // Đăng ký sự kiện để chọn hình ảnh từ thư viện
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            assert data != null;
                            uri = data.getData();
                            uploadImage.setImageURI(uri);
                        } else {
                            Toast.makeText(UploadActivity.this, "No image selected!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Thiết lập sự kiện click cho việc chọn hình ảnh
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        // Thiết lập sự kiện click cho nút lưu dữ liệu
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void saveData() {
        // Tạo đối tượng lưu trữ trên Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image").child(Objects.requireNonNull(uri.getLastPathSegment()));

        // Hiển thị dialog xử lý
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.processing_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Upload hình ảnh lên Firebase Storage
        storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    // Lấy đường dẫn tải về của hình ảnh
                    Task<Uri> uriTask = task.getResult().getStorage().getDownloadUrl();

                    // Xử lý khi lấy đường dẫn tải về thành công
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri urlImage) {
                            imageURL = urlImage.toString();
                            dialog.dismiss();

                            // Gọi phương thức uploadData() để upload các dữ liệu khác sau khi có đường dẫn ảnh
                            uploadData();
                        }
                    });
                } else {
                    // Nếu upload thất bại, đóng dialog xử lý
                    dialog.dismiss();
                    Toast.makeText(UploadActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void uploadData() {
        // Lấy thông tin từ các trường nhập liệu
        String title = uploadTopic.getText().toString();
        String desc = uploadDesc.getText().toString();
        String tag = uploadTag.getText().toString();

        // Tạo đối tượng DataClass để lưu dữ liệu
        DataClass dataClass = new DataClass(title, desc, tag, imageURL);

        // Sử dụng push() để tạo một key duy nhất cho mỗi mục dữ liệu dưới nút "DEMO"
        DatabaseReference demoRef = FirebaseDatabase.getInstance().getReference("DEMO").push(); // Sử dụng push() để tạo key tự động

        // Thực hiện lưu dữ liệu lên Firebase Database
        demoRef.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UploadActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    finish(); // Kết thúc activity hiện tại (không cần khởi động lại MainActivity)
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
