package com.example.shoppingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsername, tvOrderPending, tvOrderShipped, tvOrderCompleted;
    private Button btnLogout;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvUsername = findViewById(R.id.tv_username);
        tvOrderPending = findViewById(R.id.tv_order_pending);
        tvOrderShipped = findViewById(R.id.tv_order_shipped);
        tvOrderCompleted = findViewById(R.id.tv_order_completed);
        btnLogout = findViewById(R.id.btn_logout);

        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "未登录");
        tvUsername.setText(username);

        // 跳转到待付款订单列表
        tvOrderPending.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, OrderListActivity.class);
            intent.putExtra("status", "pending");
            startActivity(intent);
        });

        // 跳转到待收货订单列表
        tvOrderShipped.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, OrderListActivity.class);
            intent.putExtra("status", "shipped");
            startActivity(intent);
        });

        // 跳转到已完成订单列表
        tvOrderCompleted.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, OrderListActivity.class);
            intent.putExtra("status", "completed");
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            // 清除登录状态
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            // 跳转到登录页并关闭当前所有活动
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}