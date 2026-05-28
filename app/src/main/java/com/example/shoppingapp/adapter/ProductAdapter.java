package com.example.shoppingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shoppingapp.R;
import com.example.shoppingapp.model.Product;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> productList;
    private OnAddToCartListener addToCartListener;
    private OnItemClickListener itemClickListener;

    public interface OnAddToCartListener {
        void onAddToCart(Product product);
    }
    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public void setOnAddToCartListener(OnAddToCartListener listener) { this.addToCartListener = listener; }
    public void setOnItemClickListener(OnItemClickListener listener) { this.itemClickListener = listener; }

    public ProductAdapter(List<Product> productList) { this.productList = productList; }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvName.setText(product.name);
        holder.tvPrice.setText("¥" + product.price);
        holder.ivImage.setImageResource(product.imageRes);
        holder.btnAdd.setOnClickListener(v -> { if (addToCartListener != null) addToCartListener.onAddToCart(product); });
        holder.itemView.setOnClickListener(v -> { if (itemClickListener != null) itemClickListener.onItemClick(product); });
    }

    @Override
    public int getItemCount() { return productList.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvPrice;
        Button btnAdd;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_product_image);
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvPrice = itemView.findViewById(R.id.tv_product_price);
            btnAdd = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }
}