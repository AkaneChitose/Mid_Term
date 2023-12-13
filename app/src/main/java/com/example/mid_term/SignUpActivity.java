package com.example.mid_term;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword;
    private Button signupBtn;
    private TextView loginRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Khởi tạo FirebaseAuth instance
        auth = FirebaseAuth.getInstance();

        // Ánh xạ các thành phần giao diện
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupBtn = findViewById(R.id.signup_btn);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        // Thiết lập sự kiện click cho nút đăng ký
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy thông tin đăng ký từ các trường nhập liệu
                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();

                // Kiểm tra nếu có trường nào trống thì hiển thị lỗi
                if (TextUtils.isEmpty(user)) {
                    signupEmail.setError("Fill this field");
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    signupPassword.setError("Fill your password here");
                    return;
                }

                // Thực hiện đăng ký bằng email và mật khẩu
                auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Đăng ký thành công, chuyển hướng đến màn hình đăng nhập
                            Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        } else {
                            // Đăng ký thất bại, hiển thị thông báo lỗi
                            Toast.makeText(SignUpActivity.this, "Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Thiết lập sự kiện click cho văn bản chuyển hướng đến màn hình đăng nhập
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển hướng đến màn hình đăng nhập
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }
}