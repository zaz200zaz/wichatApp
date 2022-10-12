package com.example.test3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private ArrayList<Message> messages;//nội dung message
    private String SenderImg,receverImg;// ảnh người gửi và ảnh người nhận
    private Context context;//
//    private String STime ;
//    private String UserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//    private FirebaseUser userDatabase =FirebaseAuth.getInstance().getCurrentUser();

    public MessageAdapter() {
    }

    public MessageAdapter(ArrayList<Message> messages, String senderImg, String receverImg, Context context) {
        this.messages = messages;
        SenderImg = senderImg;
        this.receverImg = receverImg;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_holder,parent,false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        //chúng tôi muốn nhận thông báo
        if (messages.get(position).getConten().equals("写真")){
            holder.txtMassage.setVisibility(View.GONE);
            holder.imgMassage.setVisibility(View.VISIBLE);
//            Bitmap bitmap = getImageBitmap(messages.get(position).getImageView());
            ImageLoadAsyncTask imageLoadAsyncTask = new ImageLoadAsyncTask(messages.get(position).getImageView(), holder.imgMassage);
            imageLoadAsyncTask.execute();
//            holder.imgMassage.setImageBitmap(bitmap);
            holder.txtTime.setText(messages.get(position).getTime());
        }else{
            holder.txtMassage.setVisibility(View.VISIBLE);
            holder.imgMassage.setVisibility(View.GONE);
            holder.txtMassage.setText(messages.get(position).getConten());
            holder.txtTime.setText(messages.get(position).getTime());
        }


        ConstraintLayout constraintLayout = holder.ccll;
        if (messages.get(position).getConten().equals("写真")){
            if (messages.get(position).getSend().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){

                Glide.with(context).load(SenderImg).error(R.drawable.account_img).placeholder(R.drawable.account_img).into(holder.profimage);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);

                constraintSet.clear(R.id.profile_cardView,ConstraintSet.LEFT);
                constraintSet.clear(R.id.img_message_content,ConstraintSet.LEFT);
                constraintSet.clear(R.id.txtTime,ConstraintSet.LEFT);

                constraintSet.connect(R.id.txtTime,ConstraintSet.RIGHT,R.id.profile_cardView,ConstraintSet.RIGHT,0);
                constraintSet.connect(R.id.txtTime,ConstraintSet.RIGHT,R.id.img_message_content,ConstraintSet.LEFT,10);


                constraintSet.connect(R.id.profile_cardView,ConstraintSet.RIGHT,R.id.ccLayout,ConstraintSet.RIGHT,0);
                constraintSet.connect(R.id.img_message_content,ConstraintSet.RIGHT,R.id.profile_cardView,ConstraintSet.LEFT,0);
                constraintSet.applyTo(constraintLayout);
            }else{
                Glide.with(context).load(receverImg).error(R.drawable.account_img).placeholder(R.drawable.account_img).into(holder.profimage);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);

                constraintSet.clear(R.id.profile_cardView,ConstraintSet.RIGHT);
                constraintSet.clear(R.id.img_message_content,ConstraintSet.RIGHT);
                constraintSet.clear(R.id.txtTime,ConstraintSet.RIGHT);

                constraintSet.connect(R.id.txtTime,ConstraintSet.LEFT,R.id.profile_cardView,ConstraintSet.LEFT,0);
                constraintSet.connect(R.id.txtTime,ConstraintSet.LEFT,R.id.img_message_content,ConstraintSet.RIGHT,10);

                constraintSet.connect(R.id.profile_cardView,ConstraintSet.LEFT,R.id.ccLayout,ConstraintSet.LEFT,0);
                constraintSet.connect(R.id.img_message_content,ConstraintSet.LEFT,R.id.profile_cardView,ConstraintSet.RIGHT,0);
                constraintSet.applyTo(constraintLayout);
            }
        }else{
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


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
    //trong người giữ thư xác định
    class MessageHolder extends RecyclerView.ViewHolder{
        ConstraintLayout ccll;
        TextView txtMassage;
        TextView txtTime;
        ImageView profimage;
        ImageView imgMassage;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);

            ccll = itemView.findViewById(R.id.ccLayout);
            txtMassage = itemView.findViewById(R.id.txt_message_content);
            txtTime = itemView.findViewById(R.id.txtTime);
            profimage = itemView.findViewById(R.id.small_profile_img);
            imgMassage= itemView.findViewById(R.id.img_message_content);

        }
    }
        private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
//            cameraProfile.setImageBitmap(bm);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("tuan", "Error getting bitmap", e);
        }
        return bm;
    }
}
