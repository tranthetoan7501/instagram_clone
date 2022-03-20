package com.hcmus.mobilappsocialnetworkingimage.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.activity.secondActivity;
import com.hcmus.mobilappsocialnetworkingimage.model.userCard;
import com.hcmus.mobilappsocialnetworkingimage.model.userModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserVIewHolder> implements Filterable {

    private List<userCard> listUser;
    private List<userCard> listUserOld;
    Bundle bundle = new Bundle();
    Context context;

    public UserAdapter(List<userCard> list, Context context){
        this.listUser = list;
        this.listUserOld = list;
        this.context =context;
    }

    @NonNull
    @Override
    public UserVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new UserVIewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserVIewHolder holder, int position) {
        userCard user = listUser.get(position);
        if(user==null){
            return;
        }
        holder.username.setText(user.getUsername());
        Picasso.get().load(user.getAvatar()).into(holder.avatar);
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putSerializable("type","profile");
                bundle.putSerializable("username",holder.username.getText().toString());
                bundle.putSerializable("id",holder.username.getText().toString());
                bundle.putSerializable("avatar",user.getAvatar().toString());
                Intent intent = new Intent(context, secondActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(listUser!=null){
            return listUser.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if(strSearch.isEmpty()){
                    listUser = listUserOld;
                }else{
                    List<userCard> list = new ArrayList<>();
                    for(userCard user : listUserOld){
                        if(user.getUsername().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(user);
                        }
                    }
                    listUser = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listUser;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listUser = (List<userCard>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class UserVIewHolder extends RecyclerView.ViewHolder{

        private CircleImageView avatar;
        private TextView username;
        private RelativeLayout layoutItem;

        public UserVIewHolder(@NonNull View itemView) {
            super(itemView);
            layoutItem = itemView.findViewById(R.id.item_user_layout);
            avatar = itemView.findViewById(R.id.item_user_avatar);
            username = itemView.findViewById(R.id.item_user_username);
        }
    }
}
