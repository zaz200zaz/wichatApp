//package com.example.test3;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class hakkenAdapter extends RecyclerView.Adapter<hakkenAdapter.UserHolder>{
//    private ArrayList<Hakken> hakkenArrayList;
//    private Context context;
//    private hakkenAdapter.OnUserClickListener onUserClickListener;
//
//    public hakkenAdapter(ArrayList<Hakken> hakkenArrayList, Context context, OnUserClickListener onUserClickListener) {
//        this.hakkenArrayList = hakkenArrayList;
//        this.context = context;
//        this.onUserClickListener = onUserClickListener;
//    }
//
//    @NonNull
//    @Override
//    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.renraku_saki_holder,parent,false);
//        return new hakkenAdapter.UserHolder(view);
//    }
//
//
//
//    @Override
//    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
//        Hakken hakken = hakkenArrayList.get(position);
//        holder.txtUsername.setText(hakken.getName());
//        holder.imageView.setImageResource(hakken.getResourceId());
//    }
//
//    @Override
//    public int getItemCount() {
//        if (hakkenArrayList!=null){
//            return hakkenArrayList.size();
//        }
//        return 0;
//    }
//    interface OnUserClickListener{
//        //vị trí người dùng được nhấp vào
//        void onUserClick(int positon);
//    }
//    public class UserHolder extends RecyclerView.ViewHolder {
//        TextView txtUsername;
//        ImageView imageView;
//
//        public UserHolder(@NonNull View itemView) {
//            super(itemView);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (getAdapterPosition()!=0){
//                        onUserClickListener.onUserClick(getAdapterPosition());
//                    }
//
//                }
//            });
//            txtUsername = itemView.findViewById(R.id.txtUsername);
//
//            imageView = itemView.findViewById(R.id.img_pro);
//        }
//    }
//}
