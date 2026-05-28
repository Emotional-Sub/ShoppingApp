package com.example.shoppingapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shoppingapp.adapter.OrderAdapter;
import com.example.shoppingapp.database.OrderDao;
import com.example.shoppingapp.model.Order;
import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<Order> orderList = new ArrayList<>();
    private OrderDao orderDao;
    private String statusFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        // 隐藏默认 ActionBar，避免与 Toolbar 冲突
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        statusFilter = getIntent().getStringExtra("status");
        String title;
        if ("pending".equals(statusFilter)) title = "待付款订单";
        else if ("shipped".equals(statusFilter)) title = "待收货订单";
        else title = "已完成订单";
        setTitle(title);

        orderDao = new OrderDao(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadOrders();
    }

    private void loadOrders() {
        SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        String username = prefs.getString("username", "");
        if (username.isEmpty()) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        List<Order> allOrders = orderDao.getOrdersByUsername(username);
        orderList.clear();
        for (Order order : allOrders) {
            if (order.status.equals(statusFilter)) {
                orderList.add(order);
            }
        }
        adapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(adapter);

        // 设置监听器，同时处理状态变更和取消订单
        adapter.setOnStatusChangeListener(new OrderAdapter.OnStatusChangeListener() {
            @Override
            public void onStatusChange(Order order) {
                if ("pending".equals(order.status)) {
                    orderDao.updateOrderStatus(order.id, "shipped");
                    Toast.makeText(OrderListActivity.this, "付款成功，等待发货", Toast.LENGTH_SHORT).show();
                    loadOrders();
                } else if ("shipped".equals(order.status)) {
                    orderDao.updateOrderStatus(order.id, "completed");
                    Toast.makeText(OrderListActivity.this, "确认收货，订单已完成", Toast.LENGTH_SHORT).show();
                    loadOrders();
                }
            }

            @Override
            public void onCancelOrder(Order order) {
                orderDao.deleteOrder(order.id);
                Toast.makeText(OrderListActivity.this, "订单已取消", Toast.LENGTH_SHORT).show();
                loadOrders();
            }
        });
    }
}