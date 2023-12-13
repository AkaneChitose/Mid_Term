package com.example.mid_term;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText loginEmail, loginPassword;
    private Button loginBtn;
    private TextView signupRedirectText;
    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static final String KEY_LOGGED_IN = "is_logged_in";


    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo đối tượng FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Liên kết các thành phần giao diện với mã nguồn
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_btn);
        signupRedirectText = findViewById(R.id.loginRedirectText);
        forgotPassword = findViewById(R.id.forgot_password);

        // Xử lý sự kiện khi nút Login được nhấn
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy giá trị từ các trường email và password
                String email = loginEmail.getText().toString();
                String pass = loginPassword.getText().toString();

                // Kiểm tra tính hợp lệ của email
                if (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    // Kiểm tra tính hợp lệ của password
                    if (!TextUtils.isEmpty(pass)) {
                        // Thực hiện đăng nhập sử dụng FirebaseAuth
                        auth.signInWithEmailAndPassword(email, pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        // Hiển thị thông báo đăng nhập thành công và chuyển đến MainActivity
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Hiển thị thông báo đăng nhập thất bại
                                        Toast.makeText(LoginActivity.this, "Failed! Check your email or password", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        loginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    }
                } else if (TextUtils.isEmpty(email)) {
                    loginEmail.setError("Email can't be empty");
                } else {
                    loginEmail.setError("Please enter a valid email");
                }
            }
        });

        // Xử lý sự kiện khi người dùng chọn chuyển hướng đăng ký
        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        // Xử lý sự kiện khi người dùng chọn quên mật khẩu
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showForgotPasswordDialog();
            }
        });
    }

    // Phương thức để hiển thị hộp thoại quên mật khẩu
    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
        EditText emailBox = dialogView.findViewById(R.id.emailBox);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy giá trị từ trường emailBox
                String userEmail = emailBox.getText().toString();
                // Kiểm tra tính hợp lệ của email
                if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    Toast.makeText(LoginActivity.this, "Enter your registered email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Gửi email đặt lại mật khẩu
                auth.sendPasswordResetEmail(userEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Hiển thị thông báo và đóng hộp thoại
                                    Toast.makeText(LoginActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Something went wrong. Double-check your email!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        // Đặt background mờ khi hiển thị hộp thoại
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        // Hiển thị hộp thoại
        dialog.show();
    }
}