package com.example.test3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class JibunImageAdapter extends RecyclerView.Adapter<JibunImageAdapter.JibunImageAdapter2> {

    //tạo danh sách người dùng
    private ArrayList<User> users;
    //cài đặt ngữ cảnh
    private Context context;

    private OnUserClickListener onUserClickListener;

    public JibunImageAdapter(ArrayList<User> users, Context context, OnUserClickListener onUserClickListener) {
        this.users = users;
        this.context = context;
        this.onUserClickListener = (OnUserClickListener) onUserClickListener;

    }

    interface OnUserClickListener{
        //vị trí người dùng được nhấp vào
        void onUserClick(int positon);
    }

    @NonNull
    @Override
    public JibunImageAdapter2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_jibun,parent,false);
        return new JibunImageAdapter2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JibunImageAdapter2 holder, int position) {
        holder.txtUsername.setText(users.get(position).getUsernameAcount());
        Glide.with(context).load(users.get(position).getProfilePicture()).error(R.drawable.account_img).placeholder(R.drawable.account_img).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    class JibunImageAdapter2 extends RecyclerView.ViewHolder{
        TextView txtUsername;
        ImageView imageView;
        public JibunImageAdapter2(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onUserClickListener.onUserClick(getAdapterPosition());
                }
            });
            txtUsername = itemView.findViewById(R.id.edtNickNameId);
            imageView = itemView.findViewById(R.id.cameraProfile);
        }
    }
}
