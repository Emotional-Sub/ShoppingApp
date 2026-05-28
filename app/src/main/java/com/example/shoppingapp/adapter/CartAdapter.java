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
import com.example.shoppingapp.model.CartItem;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<CartItem> cartList;
    private OnQuantityChangeListener quantityChangeListener;
    private OnDeleteListener deleteListener;

    public interface OnQuantityChangeListener {
        void onQuantityChange(CartItem item, int newQuantity);
    }
    public interface OnDeleteListener {
        void onDelete(CartItem item);
    }

    public void setOnQuantityChangeListener(OnQuantityChangeListener listener) { this.quantityChangeListener = listener; }
    public void setOnDeleteListener(OnDeleteListener listener) { this.deleteListener = listener; }

    public CartAdapter(List<CartItem> cartList) { this.cartList = cartList; }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartList.get(position);
        holder.tvName.setText(item.name);
        holder.tvPrice.setText("¥" + item.price);
        holder.tvQuantity.setText(String.valueOf(item.quantity));
        holder.ivImage.setImageResource(item.imageRes);

        holder.btnMinus.setOnClickListener(v -> {
            if (quantityChangeListener != null) quantityChangeListener.onQuantityChange(item, item.quantity - 1);
        });
        holder.btnPlus.setOnClickListener(v -> {
            if (quantityChangeListener != null) quantityChangeListener.onQuantityChange(item, item.quantity + 1);
        });
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) deleteListener.onDelete(item);
        });
    }

    @Override
    public int getItemCount() { return cartList.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvPrice, tvQuantity;
        Button btnMinus, btnPlus, btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_cart_image);
            tvName = itemView.findViewById(R.id.tv_cart_name);
            tvPrice = itemView.findViewById(R.id.tv_cart_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            btnMinus = itemView.findViewById(R.id.btn_minus);
            btnPlus = itemView.findViewById(R.id.btn_plus);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}