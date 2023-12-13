package com.example.mid_term;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private List<DataClass> dataList;
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo các thành phần giao diện
        initializeViews();

        // Cấu hình RecyclerView
        setupRecyclerView();

        // Khởi tạo đối tượng FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Tạo hộp thoại xử lý
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.processing_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Khởi tạo danh sách dữ liệu
        dataList = new ArrayList<>();
        MyAdapter adapter = new MyAdapter(MainActivity.this, dataList);
        recyclerView.setAdapter(adapter);

        // Tham chiếu đến node "DEMO" trên Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("DEMO");
        dialog.show();

        // Lắng nghe sự kiện thay đổi dữ liệu trên Firebase Database
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Xử lý dữ liệu từ DataSnapshot
                handleDataSnapshot(snapshot);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        // Xử lý sự kiện click nút Floating Action Button (FAB)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở màn hình UploadActivity
                startUploadActivity();
            }
        });

        // Xử lý sự kiện click nút Logout
        Button logoutBtn = findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Đăng xuất người dùng và chuyển hướng về LoginActivity
                logoutAndRedirect();
            }
        });
    }

    // Phương thức để khởi tạo các thành phần giao diện
    private void initializeViews() {
        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recycleView);
    }

    // Phương thức để cài đặt RecyclerView với GridLayoutManager
    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    // Phương thức để xử lý DataSnapshot và cập nhật danh sách dữ liệu
    private void handleDataSnapshot(DataSnapshot snapshot) {
        dataList.clear();
        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
            DataClass dataClass = itemSnapshot.getValue(DataClass.class);
            if (dataClass != null) {
                dataClass.setKey(itemSnapshot.getKey());
                dataList.add(dataClass);
            }
        }
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    // Phương thức để mở màn hình UploadActivity
    private void startUploadActivity() {
        Intent intent = new Intent(MainActivity.this, UploadActivity.class);
        startActivity(intent);
    }

    // Phương thức để đăng xuất và chuyển hướng về LoginActivity
    private void logoutAndRedirect() {
        auth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}
