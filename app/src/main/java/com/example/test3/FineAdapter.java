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

public class FineAdapter extends RecyclerView.Adapter<FineAdapter.FineHolder> {

    private ArrayList<FineData> arrayList;
    private ChatGamen_3 ChatGamen_3;

    public FineAdapter(ArrayList<FineData> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public FineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fine_holder,parent,false);
        return new FineHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FineHolder holder, int position) {
        FineData fineData = arrayList.get(position);
        if (fineData!=null){
//            holder.imageView.setImageBitmap(fineData.getMessage());

                ImageLoadAsyncTask imageLoadAsyncTask = new ImageLoadAsyncTask(fineData.getImageView(), holder.imageView);
                imageLoadAsyncTask.execute();

//            ImageLoadAsyncTask imageLoadAsyncTask = new ImageLoadAsyncTask(fineData.getImageView(), holder.imageView);
//            imageLoadAsyncTask.execute();
            holder.txtMessage.setText(fineData.getMessage());
            holder.txtUserName.setText(fineData.getSendPerson());
        }else{
            return;
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class FineHolder extends RecyclerView.ViewHolder {
        TextView txtMessage; TextView txtUserName;
        ImageView imageView;
        public FineHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_pro);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtUserName = itemView.findViewById(R.id.txtUserName);
        }

    }
}
