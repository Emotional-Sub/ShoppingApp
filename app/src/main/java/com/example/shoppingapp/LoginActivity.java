package com.example.shoppingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.shoppingapp.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            dbHelper = new DatabaseHelper(this);
            etUsername = findViewById(R.id.et_username);
            etPassword = findViewById(R.id.et_password);
            btnLogin = findViewById(R.id.btn_login);
            tvRegister = findViewById(R.id.tv_register);

            // 注册跳转
            tvRegister.setOnClickListener(v -> {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            });

            // 自动填充注册页面传递过来的账号（可选）
            Intent intent = getIntent();
            String username = intent.getStringExtra("username");
            String password = intent.getStringExtra("password");
            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                etUsername.setText(username);
                etPassword.setText(password);
                // 可选：自动点击登录按钮
                // btnLogin.performClick();
            }

            btnLogin.setOnClickListener(v -> {
                String uname = etUsername.getText().toString().trim();
                String pwd = etPassword.getText().toString().trim();
                if (uname.isEmpty() || pwd.isEmpty()) {
                    Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (checkLogin(uname, pwd)) {
                    // 保存登录信息
                    SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", uname);
                    editor.apply();

                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "初始化失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private boolean checkLogin(String username, String password) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.query(DatabaseHelper.TABLE_USER, null,
                    DatabaseHelper.COL_USERNAME + "=? AND " + DatabaseHelper.COL_PASSWORD + "=?",
                    new String[]{username, password}, null, null, null);
            return cursor.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "数据库错误：" + e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
    }
}