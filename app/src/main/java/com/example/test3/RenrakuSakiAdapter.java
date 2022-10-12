package com.example.test3;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RenrakuSakiAdapter extends RecyclerView.Adapter<RenrakuSakiAdapter.UserHolder>{
    //tạo danh sách người dùng
    private ArrayList<User> users;
    //cài đặt ngữ cảnh
    private Context context;
//    private ArrayList<Message>message;
    private String myUsername;
//    private String value;
//    private String value2;
//
//    private String Message;

    private RenrakuSakiAdapter.OnUserClickListener onUserClickListener;

    public RenrakuSakiAdapter(ArrayList<User> users, Context context, RenrakuSakiAdapter.OnUserClickListener onUserClickListener, String myUsername) {
        this.users = users;
        this.context = context;
        this.onUserClickListener = onUserClickListener;
        this.myUsername = myUsername;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.renraku_saki_holder,parent,false);
        return new RenrakuSakiAdapter.UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        String name = users.get(position).getUsernameAcount();
        String email = users.get(position).getEmail();
        if (name.isEmpty()){
            holder.txtUsername.setText(email);
        }else{
            holder.txtUsername.setText(name);
        }

        Glide.with(context).load(users.get(position).getProfilePicture()).error(R.drawable.account_img).placeholder(R.drawable.account_img).into(holder.imageView);

    }



    @Override
    public int getItemCount() {
        return users.size();
    }

    interface OnUserClickListener{
        //vị trí người dùng được nhấp vào
        void onUserClick(int positon);
    }


    class UserHolder extends RecyclerView.ViewHolder{

        TextView txtUsername;
        ImageView imageView;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onUserClickListener.onUserClick(getAdapterPosition());
                }
            });
            txtUsername = itemView.findViewById(R.id.txtUsername);

            imageView = itemView.findViewById(R.id.img_pro);
        }
    }

//    private void chatRoomId(String chatRoomId) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("messages/"+chatRoomId);
//
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange( @NonNull DataSnapshot dataSnapshot) {
//
//                message.clear();
//                for (DataSnapshot dataSnapshot1hot: dataSnapshot.getChildren()){
//                    message.add(dataSnapshot1hot.getValue(Message.class));//thêm tin nhắn vào trong mảng mesage
//                }
//                value=message.get(message.size()-1).getTime() ;
//                value2=message.get(message.size()-1).getConten() ;
////                Log.d("121212", "Value is: " + value+"-"+value2);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("121212", "Failed to read value.", error.toException());
//            }
//        });
//    }
}
