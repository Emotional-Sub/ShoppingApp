package com.example.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shoppingapp.adapter.ProductAdapter;
import com.example.shoppingapp.database.CartDao;
import com.example.shoppingapp.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private CartDao cartDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cartDao = new CartDao(this);
        initProductData();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(productList);
        recyclerView.setAdapter(adapter);

        adapter.setOnAddToCartListener(product -> {
            cartDao.addToCart(product.id, product.name, product.price, product.imageRes);
            Toast.makeText(MainActivity.this, "已添加 " + product.name + " 到购物车", Toast.LENGTH_SHORT).show();
        });

        adapter.setOnItemClickListener(product -> {
            Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
            intent.putExtra("id", product.id);
            intent.putExtra("name", product.name);
            intent.putExtra("price", product.price);
            intent.putExtra("description", product.description);
            intent.putExtra("imageRes", product.imageRes);
            startActivity(intent);
        });

        FloatingActionButton fab = findViewById(R.id.fab_cart);
        fab.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
    }

    private void initProductData() {
        productList = new ArrayList<>();
        productList.add(new Product(1, "智能手机", 2999.0, "最新款5G手机，120Hz高刷屏", R.drawable.ic_product_phone));
        productList.add(new Product(2, "蓝牙耳机", 199.0, "降噪耳机，续航20小时", R.drawable.ic_product_earphone));
        productList.add(new Product(3, "智能手表", 899.0, "心率监测，运动追踪", R.drawable.ic_product_watch));
        productList.add(new Product(4, "笔记本电脑", 5999.0, "轻薄本，16GB内存，512GB SSD", R.drawable.ic_product_laptop));
        productList.add(new Product(5, "平板电脑", 2499.0, "10.5英寸2K屏，支持手写笔", R.drawable.ic_product_tablet));
        productList.add(new Product(6, "游戏机", 2999.0, "高性能游戏主机，4K输出", R.drawable.ic_product_game));
    }

    // ---------- 添加选项菜单 ----------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}