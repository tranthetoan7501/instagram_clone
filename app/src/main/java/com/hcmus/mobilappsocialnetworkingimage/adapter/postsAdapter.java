package adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.secondActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class postsAdapter extends RecyclerView.Adapter<postsAdapter.postsViewHolder> implements View.OnClickListener {
    List<String> name;
    List<String> image;
    List<String> description;
    List<String> date;
    Context context;
    FragmentTransaction fragmentTransaction;
    Bundle bundle = new Bundle();

    public postsAdapter(List<String> name, List<String> image, List<String> description, List<String> date, Context context) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.date = date;
        this.context = context;
    }

    @NonNull
    @Override
    public postsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post,parent,false);
        return new postsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull postsViewHolder holder, int position) {
        if(name.isEmpty()) return;
        holder.up_name.setText(name.get(position));
        List<SlideModel> models = new ArrayList<>();
        models.add(new SlideModel(image.get(position)));
        holder.image.setImageList(models,false);
        holder.below_name.setText(name.get(position));
        holder.description.setText(description.get(position));
        holder.date.setText(date.get(position));
        holder.comment.setOnClickListener(this);
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.like.isActivated())
                    holder.like.setImageResource(R.drawable.ic_like);
                else
                holder.like.setImageResource(R.drawable.ic_like_filled);
            }
        });

        bundle.putSerializable("description",name.get(position) + "\n" + description.get(position));
        bundle.putSerializable("name",name.get(position));
        bundle.putSerializable("date",date.get(position));
        bundle.putSerializable("image",image.get(position));
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.comment:
                bundle.putSerializable("type","comment");
                Intent intent = new Intent(context, secondActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
                break;
        }
    }

    class postsViewHolder extends RecyclerView.ViewHolder{
        CircleImageView avatar;
        TextView up_name;
        Button setting;
        ImageSlider image;
        ImageButton like;
        ImageButton comment;
        ImageButton share;
        TextView num_likes;
        TextView below_name;
        TextView description;
        TextView date;
        public postsViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            up_name = itemView.findViewById(R.id.up_name);
            setting = itemView.findViewById(R.id.setting_button);
            image = itemView.findViewById(R.id.image_slider);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            share = itemView.findViewById(R.id.share);
            num_likes = itemView.findViewById(R.id.num_likes);
            below_name = itemView.findViewById(R.id.below_name);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
        }
    }
}
