package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.hcmus.mobilappsocialnetworkingimage.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class storiesAdapter extends RecyclerView.Adapter<storiesAdapter.storiesViewHolder> {
    List<String> listImage;
    List<String> listName;
    Context context;

    public storiesAdapter(List<String> listImage, List<String> listName, Context context) {
        this.listImage = listImage;
        this.listName = listName;
        this.context = context;
    }

    @NonNull
    @Override
    public storiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story,parent,false);
        return new storiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull storiesViewHolder holder, int position) {
        if(listName.isEmpty()) return;
        Picasso.get().load(listImage.get(position)).into(holder.circleImageView);
        holder.textView.setText(listName.get(position));
        holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.circleImageView.setBorderColor(context.getColor(R.color.LightGrey));
            }
        });

    }

    @Override
    public int getItemCount() {
        return listImage.size();
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.avatar:
//                Toast.makeText(view.getContext(),"abc",Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }

    class storiesViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView textView;
        public storiesViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.avatar);
            textView = itemView.findViewById(R.id.name);
        }
    }
}
