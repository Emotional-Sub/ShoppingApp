package com.example.shoppingapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.adapter.OrderDetailAdapter;
import com.example.shoppingapp.model.Order;

public class OrderDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // 关键修复：隐藏默认的 ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        Order order = (Order) getIntent().getSerializableExtra("order");

        TextView tvOrderNo = findViewById(R.id.tv_order_no);
        TextView tvOrderTime = findViewById(R.id.tv_order_time);
        TextView tvOrderStatus = findViewById(R.id.tv_order_status);
        TextView tvTotalPrice = findViewById(R.id.tv_total_price);
        RecyclerView rvItems = findViewById(R.id.rv_order_items);

        tvOrderNo.setText("订单号：" + order.orderNo);
        tvOrderTime.setText("下单时间：" + order.createTime);
        tvOrderStatus.setText(getStatusText(order.status));
        tvTotalPrice.setText("总金额：¥" + order.totalPrice);

        rvItems.setLayoutManager(new LinearLayoutManager(this));
        OrderDetailAdapter adapter = new OrderDetailAdapter(order.items);
        rvItems.setAdapter(adapter);
    }

    private String getStatusText(String status) {
        switch (status) {
            case "pending": return "待付款";
            case "shipped": return "待收货";
            case "completed": return "已完成";
            default: return "未知";
        }
    }
}