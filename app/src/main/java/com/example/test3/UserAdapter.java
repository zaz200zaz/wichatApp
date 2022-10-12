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

//bộ điều hợp người dùng
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder>  {
    //tạo danh sách người dùng
    private ArrayList<User> users;
    //cài đặt ngữ cảnh
    private Context context;
    private ArrayList<Message>message;
    private String myUsername;
    private String value;
    private String value2;

    private OnUserClickListener onUserClickListener;

        public UserAdapter(ArrayList<User> users, Context context, OnUserClickListener onUserClickListener, String myUsername) {
        this.users = users;
        this.context = context;
        this.onUserClickListener = onUserClickListener;
        this.myUsername = myUsername;

    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_holder,parent,false);
        return new UserHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        message = new ArrayList<>();
        String email = users.get(position).getEmail();
        String name = users.get(position).getUsernameAcount();
        if (name.isEmpty()){
            holder.txtUsername.setText(email);
        }else{
            holder.txtUsername.setText(name);
        }
        String usernameOfTheRoommate = catEmail(email);
        String chatRoomId;
        if (usernameOfTheRoommate.compareTo(myUsername)>0){//max compe alex
            chatRoomId = myUsername + usernameOfTheRoommate;
        }else if (usernameOfTheRoommate.compareTo(myUsername)==00){//alex alex
            chatRoomId = myUsername + usernameOfTheRoommate;
        }else{
            chatRoomId = usernameOfTheRoommate+myUsername; //alex max
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("messages/"+chatRoomId);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot) {

                message.clear();
                for (DataSnapshot dataSnapshot1hot: dataSnapshot.getChildren()){
                    message.add(dataSnapshot1hot.getValue(Message.class));//thêm tin nhắn vào trong mảng mesage
                }
                if (message.size()<=0){
                    holder.txtTime.setText("");
                    holder.txtMessage.setText("");
                }else{
                    value=message.get(message.size()-1).getTime() ;
                    value2=message.get(message.size()-1).getConten() ;
//                Log.d("121212", "Value is: " + value+"-"+value2);
                    String sdas =value;
                    String sddsas =value2;
                    holder.txtTime.setText(value);
                    holder.txtMessage.setText(value2);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("121212", "Failed to read value.", error.toException());
            }
        });

        Glide.with(context)
                .load(users.get(position)
                        .getProfilePicture())
                .error(R.drawable.account_img)
                .placeholder(R.drawable.account_img)
                .into(holder.imageView);
    }

    private String catEmail(String toString) {
            String [] list = toString.split("\\@",2);
            return list[0];
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
        TextView txtMessage;
        TextView txtTime;
        public UserHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onUserClickListener.onUserClick(getAdapterPosition());
                }
            });
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            imageView = itemView.findViewById(R.id.img_pro);
        }
    }
}
