package com.example.shoppingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.shoppingapp.database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvBackLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        tvBackLogin = findViewById(R.id.tv_back_login);

        btnRegister.setOnClickListener(v -> register());
        tvBackLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void register() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirm = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("密码不能为空");
            return;
        }
        if (!password.equals(confirm)) {
            etConfirmPassword.setError("两次输入的密码不一致");
            return;
        }

        // 检查用户名是否已存在
        if (isUsernameExists(username)) {
            Toast.makeText(this, "用户名已存在，请直接登录", Toast.LENGTH_SHORT).show();
            return;
        }

        // 插入新用户
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_USERNAME, username);
        values.put(DatabaseHelper.COL_PASSWORD, password);
        long result = db.insert(DatabaseHelper.TABLE_USER, null, values);
        db.close();

        if (result != -1) {
            Toast.makeText(this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
            // 可选：自动登录（将用户名密码传给 LoginActivity）
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("password", password);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isUsernameExists(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {DatabaseHelper.COL_USERNAME};
        String selection = DatabaseHelper.COL_USERNAME + "=?";
        String[] selectionArgs = {username};
        android.database.Cursor cursor = db.query(DatabaseHelper.TABLE_USER, columns, selection, selectionArgs, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
}