package com.example.shoppingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.shoppingapp.R;
import com.example.shoppingapp.model.Order;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> orderList;
    private OnStatusChangeListener listener;

    public interface OnStatusChangeListener {
        void onStatusChange(Order order);   // 付款 / 确认收货
        void onCancelOrder(Order order);    // 取消订单
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
        holder.tvOrderNo.setText("订单号：" + order.orderNo);
        holder.tvOrderTime.setText("下单时间：" + order.createTime);
        holder.tvTotalPrice.setText("总金额：¥" + order.totalPrice);
        holder.tvStatus.setText(getStatusText(order.status));

        // 根据状态显示不同按钮文字和行为
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

        // 点击订单项可查看详情（可选）
        holder.itemView.setOnClickListener(v -> {
            // 跳转到订单详情页（可后续扩展）
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
        TextView tvOrderNo, tvOrderTime, tvTotalPrice, tvStatus;
        Button btnAction, btnCancel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderNo = itemView.findViewById(R.id.tv_order_no);
            tvOrderTime = itemView.findViewById(R.id.tv_order_time);
            tvTotalPrice = itemView.findViewById(R.id.tv_total_price);
            tvStatus = itemView.findViewById(R.id.tv_status);
            btnAction = itemView.findViewById(R.id.btn_action);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
        }
    }
}