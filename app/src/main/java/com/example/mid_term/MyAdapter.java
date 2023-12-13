package com.example.mid_term;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<DataClass> dataList;

    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo một View từ layout được định nghĩa cho mỗi item trong RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Sử dụng thư viện Glide để tải ảnh từ URL và hiển thị trong ImageView
        Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recImage);

        // Đặt dữ liệu văn bản cho các TextView
        holder.recTitle.setText(dataList.get(position).getDataTitle());
        holder.recDesc.setText(dataList.get(position).getDataDesc());
        holder.recTag.setText(dataList.get(position).getDataTag());

        // Đặt nghe sự kiện khi CardView được nhấn
        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để mở DetailActivity
                Intent intent = new Intent(context, DetailActivity.class);

                // Truyền dữ liệu sang DetailActivity
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getDataImage());
                intent.putExtra("Description", dataList.get(holder.getAdapterPosition()).getDataDesc());
                intent.putExtra("Tag", dataList.get(holder.getAdapterPosition()).getDataTag());
                intent.putExtra("Key", dataList.get(holder.getAdapterPosition()).getKey());

                // Khởi chạy DetailActivity
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // Lớp MyViewHolder giữ các thành phần giao diện của mỗi item trong RecyclerView
    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView recImage;
        TextView recTitle, recDesc, recTag;
        CardView recCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các thành phần giao diện từ layout
            recImage = itemView.findViewById(R.id.recImage);
            recTitle = itemView.findViewById(R.id.recTitle);
            recDesc = itemView.findViewById(R.id.recDesc);
            recTag = itemView.findViewById(R.id.recTag);
            recCard = itemView.findViewById(R.id.recCard);
        }
    }
}
