package com.example.shoppingapp.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shoppingapp.R;
import com.example.shoppingapp.model.Order;
import com.example.shoppingapp.model.OrderItem;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> orderList;
    private OnStatusChangeListener listener;

    // 接口扩展：增加订单项点击回调
    public interface OnStatusChangeListener {
        void onStatusChange(Order order);   // 付款 / 确认收货
        void onCancelOrder(Order order);    // 取消订单
        void onItemClick(Order order);      // 查看订单详情
    }

    public void setOnStatusChangeListener(OnStatusChangeListener listener) {
        this.listener = listener;
    }

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);
        Log.d("OrderStatus", "订单号：" + order.orderNo + " 状态：" + order.status);

        holder.tvOrderNo.setText("订单号：" + order.orderNo);
        holder.tvOrderTime.setText("下单时间：" + order.createTime);
        holder.tvTotalPrice.setText("总金额：¥" + order.totalPrice);
        holder.tvStatus.setText(getStatusText(order.status));

        // 显示商品名称（取第一个商品，如有多个则显示“xxx等N件商品”）
        if (order.items != null && !order.items.isEmpty()) {
            OrderItem first = order.items.get(0);
            int totalQty = order.items.size();
            if (totalQty == 1) {
                holder.tvProductNames.setText(first.productName + " x" + first.quantity);
            } else {
                int totalCount = 0;
                for (OrderItem item : order.items) totalCount += item.quantity;
                holder.tvProductNames.setText(first.productName + " 等" + totalQty + "件商品");
            }
            holder.tvProductNames.setVisibility(View.VISIBLE);
        } else {
            holder.tvProductNames.setVisibility(View.GONE);
        }

        // 根据订单状态显示不同按钮
        if ("pending".equals(order.status)) {
            holder.btnAction.setText("去付款");
            holder.btnAction.setVisibility(View.VISIBLE);
            holder.btnCancel.setVisibility(View.VISIBLE);
            holder.btnAction.setOnClickListener(v -> {
                if (listener != null) listener.onStatusChange(order);
            });
            holder.btnCancel.setOnClickListener(v -> {
                if (listener != null) listener.onCancelOrder(order);
            });
        } else if ("shipped".equals(order.status)) {
            holder.btnAction.setText("确认收货");
            holder.btnAction.setVisibility(View.VISIBLE);
            holder.btnCancel.setVisibility(View.GONE);
            holder.btnAction.setOnClickListener(v -> {
                if (listener != null) listener.onStatusChange(order);
            });
        } else {
            holder.btnAction.setVisibility(View.GONE);
            holder.btnCancel.setVisibility(View.GONE);
        }

        // 点击整个条目查看订单详情
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(order);
        });
    }

    private String getStatusText(String status) {
        switch (status) {
            case "pending": return "待付款";
            case "shipped": return "待收货";
            case "completed": return "已完成";
            default: return "未知";
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderNo, tvOrderTime, tvProductNames, tvTotalPrice, tvStatus;
        Button btnAction, btnCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderNo = itemView.findViewById(R.id.tv_order_no);
            tvOrderTime = itemView.findViewById(R.id.tv_order_time);
            tvProductNames = itemView.findViewById(R.id.tv_product_names);  // 新增商品名称
            tvTotalPrice = itemView.findViewById(R.id.tv_total_price);
            tvStatus = itemView.findViewById(R.id.tv_status);
            btnAction = itemView.findViewById(R.id.btn_action);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
        }
    }
}