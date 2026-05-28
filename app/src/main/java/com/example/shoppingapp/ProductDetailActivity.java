package com.example.shoppingapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.shoppingapp.database.CartDao;

public class ProductDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        int id = getIntent().getIntExtra("id", 0);
        String name = getIntent().getStringExtra("name");
        double price = getIntent().getDoubleExtra("price", 0);
        String description = getIntent().getStringExtra("description");
        int imageRes = getIntent().getIntExtra("imageRes", R.drawable.ic_product_phone);

        ImageView ivImage = findViewById(R.id.iv_detail_image);
        TextView tvName = findViewById(R.id.tv_detail_name);
        TextView tvPrice = findViewById(R.id.tv_detail_price);
        TextView tvDesc = findViewById(R.id.tv_detail_description);
        Button btnAdd = findViewById(R.id.btn_detail_add_cart);

        ivImage.setImageResource(imageRes);
        tvName.setText(name);
        tvPrice.setText("¥" + price);
        tvDesc.setText(description);

        CartDao cartDao = new CartDao(this);
        btnAdd.setOnClickListener(v -> {
            cartDao.addToCart(id, name, price, imageRes);
            Toast.makeText(this, "已加入购物车", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}