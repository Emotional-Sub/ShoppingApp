package com.example.shoppingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shoppingapp.adapter.CartAdapter;
import com.example.shoppingapp.database.CartDao;
import com.example.shoppingapp.database.OrderDao;
import com.example.shoppingapp.model.CartItem;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private CartDao cartDao;
    private OrderDao orderDao;
    private List<CartItem> cartList;
    private TextView tvTotalPrice;
    private Button btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartDao = new CartDao(this);
        orderDao = new OrderDao(this);
        recyclerView = findViewById(R.id.cart_recyclerView);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        btnCheckout = findViewById(R.id.btn_checkout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadCartData();

        btnCheckout.setOnClickListener(v -> {
            if (cartList.isEmpty()) {
                Toast.makeText(this, "购物车为空", Toast.LENGTH_SHORT).show();
                return;
            }
            // 获取当前登录用户
            SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
            String username = prefs.getString("username", "");
            if (username.isEmpty()) {
                Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            // 创建订单
            orderDao.createOrderFromCart(username, cartList);
            // 清空购物车
            cartDao.clearCart();
            // 跳转到待付款订单列表
            Intent intent = new Intent(CartActivity.this, OrderListActivity.class);
            intent.putExtra("status", "pending");
            startActivity(intent);
            finish(); // 关闭购物车页面
            Toast.makeText(this, "下单成功！", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadCartData() {
        cartList = cartDao.getAllCartItems();
        adapter = new CartAdapter(cartList);
        recyclerView.setAdapter(adapter);
        double total = 0;
        for (CartItem item : cartList) {
            total += item.price * item.quantity;
        }
        tvTotalPrice.setText(String.format("总计: ¥%.2f", total));

        adapter.setOnQuantityChangeListener((item, newQuantity) -> {
            cartDao.updateQuantity(item.id, newQuantity);
            loadCartData();
        });
        adapter.setOnDeleteListener(item -> {
            cartDao.deleteCartItem(item.id);
            loadCartData();
        });
    }
}