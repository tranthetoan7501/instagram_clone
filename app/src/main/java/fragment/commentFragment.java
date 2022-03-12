package fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.mobilappsocialnetworkingimage.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import adapter.commentsAdapter;
import adapter.postsAdapter;
import de.hdodenhof.circleimageview.CircleImageView;

public class commentFragment extends Fragment implements View.OnClickListener{
    commentsAdapter  commentsAdapter;
    ImageButton previous;
    CircleImageView avatar;
    RecyclerView recyclerView;
    EditText comment;
    CircleImageView belowAvatar;
    TextView description;
    TextView date;
    Bundle bundle = new Bundle();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        avatar = view.findViewById(R.id.avatar);
        recyclerView = view.findViewById(R.id.all_comments);
        previous = view.findViewById(R.id.previous);
        belowAvatar = view.findViewById(R.id.avatar_below);
        comment = view.findViewById(R.id.comment);
        description = view.findViewById(R.id.description);
        previous.setOnClickListener(this);
        bundle = getArguments();
        date = view.findViewById(R.id.date);
        getData();
        return view;
    }

    void getData(){
        Picasso.get().load(bundle.get("image").toString()).into(avatar);
        String str[] = bundle.get("description").toString().split("\n");
        description.setText(Html.fromHtml("<b>" + str[0]+"</b>" + " " + str[1]));
        date.setText(bundle.get("date").toString());

        List<String> image = new ArrayList<>();
        List<String> description = new ArrayList<>();
        List<String> date = new ArrayList<>();
        List<String> num_likes = new ArrayList<>();

        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        commentsAdapter = new commentsAdapter(getContext(),description,date,num_likes,image);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(commentsAdapter);

        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
        description.add("Toàn lé\nAnh em nhóm 5 thánh bú liếm");
        date.add("27/10/2001");
        num_likes.add("15 likes");

        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
        description.add("Toàn lé\nAnh em nhóm 5 thánh bú liếm");
        date.add("27/10/2001");
        num_likes.add("15 likes");

        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
        description.add("Toàn lé\nAnh em nhóm 5 thánh bú liếm");
        date.add("27/10/2001");
        num_likes.add("15 likes");

        commentsAdapter.notifyDataSetChanged();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.previous:
                getActivity().finish();
                break;
        }
    }
}
