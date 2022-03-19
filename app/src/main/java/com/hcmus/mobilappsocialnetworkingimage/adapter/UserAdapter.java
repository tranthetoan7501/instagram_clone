package com.hcmus.mobilappsocialnetworkingimage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.model.userModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserVIewHolder> implements Filterable {

    private List<userModel> listUser;
    private List<userModel> listUserOld;

    public UserAdapter(List<userModel> list){
        this.listUser = list;
        this.listUserOld = list;
    }

    @NonNull
    @Override
    public UserVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new UserVIewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserVIewHolder holder, int position) {
        userModel user = listUser.get(position);
        if(user==null){
            return;
        }
        holder.username.setText(user.getUsername());
      //  Picasso.get().load(user.getAvatar()).into(holder.avatar);
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
                    List<userModel> list = new ArrayList<>();
                    for(userModel user : listUserOld){
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
                listUser = (List<userModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class UserVIewHolder extends RecyclerView.ViewHolder{

        private CircleImageView avatar;
        private TextView username;
        public UserVIewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.item_user_avatar);
            username = itemView.findViewById(R.id.item_user_username);
        }
    }
}
