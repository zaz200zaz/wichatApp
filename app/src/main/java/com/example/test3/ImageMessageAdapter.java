package com.example.test3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ImageMessageAdapter extends RecyclerView.Adapter<ImageMessageAdapter.ImageMessageHolder>{

    private ArrayList<Message> messages;
    private String SenderImg,receverImg;
    private Context context;

    public ImageMessageAdapter(ArrayList<Message> messages, String senderImg, String receverImg, Context context) {
        this.messages = messages;
        SenderImg = senderImg;
        this.receverImg = receverImg;
        this.context = context;
    }

    public ImageMessageAdapter() {
    }

    @NonNull
    @Override
    public ImageMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_message_holder,parent,false);
        return new ImageMessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageMessageHolder holder, int position) {

//        holder.img_message_content.setImageBitmap(messages.get(position).getImageView());
        holder.txtTime.setText(messages.get(position).getTime());

        ConstraintLayout constraintLayout = holder.ccll;

        if (messages.get(position).getSend().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){

            Glide.with(context).load(SenderImg).error(R.drawable.account_img).placeholder(R.drawable.account_img).into(holder.profimage);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);

            constraintSet.clear(R.id.profile_cardView,ConstraintSet.LEFT);
            constraintSet.clear(R.id.txt_message_content,ConstraintSet.LEFT);
            constraintSet.clear(R.id.txtTime,ConstraintSet.LEFT);

            constraintSet.connect(R.id.txtTime,ConstraintSet.RIGHT,R.id.profile_cardView,ConstraintSet.RIGHT,0);
            constraintSet.connect(R.id.txtTime,ConstraintSet.RIGHT,R.id.txt_message_content,ConstraintSet.LEFT,10);

            constraintSet.connect(R.id.profile_cardView,ConstraintSet.RIGHT,R.id.ccLayout,ConstraintSet.RIGHT,0);
            constraintSet.connect(R.id.txt_message_content,ConstraintSet.RIGHT,R.id.profile_cardView,ConstraintSet.LEFT,0);
            constraintSet.applyTo(constraintLayout);
        }else{
            Glide.with(context).load(receverImg).error(R.drawable.account_img).placeholder(R.drawable.account_img).into(holder.profimage);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);

            constraintSet.clear(R.id.profile_cardView,ConstraintSet.RIGHT);
            constraintSet.clear(R.id.txt_message_content,ConstraintSet.RIGHT);
            constraintSet.clear(R.id.txtTime,ConstraintSet.RIGHT);

            constraintSet.connect(R.id.txtTime,ConstraintSet.LEFT,R.id.profile_cardView,ConstraintSet.LEFT,0);
            constraintSet.connect(R.id.txtTime,ConstraintSet.LEFT,R.id.txt_message_content,ConstraintSet.RIGHT,10);

            constraintSet.connect(R.id.profile_cardView,ConstraintSet.LEFT,R.id.ccLayout,ConstraintSet.LEFT,0);
            constraintSet.connect(R.id.txt_message_content,ConstraintSet.LEFT,R.id.profile_cardView,ConstraintSet.RIGHT,0);
            constraintSet.applyTo(constraintLayout);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ImageMessageHolder extends RecyclerView.ViewHolder{
        ConstraintLayout ccll;
        ImageView img_message_content;
        TextView txtTime;
        ImageView profimage;

        public ImageMessageHolder(@NonNull View itemView) {
            super(itemView);
            ccll = itemView.findViewById(R.id.ccLayout);
            img_message_content = itemView.findViewById(R.id.img_message_content);
            txtTime = itemView.findViewById(R.id.txtTime);
            profimage = itemView.findViewById(R.id.small_profile_img);

        }
    }
}
