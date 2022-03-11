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
import androidx.recyclerview.widget.RecyclerView;

import com.hcmus.mobilappsocialnetworkingimage.R;
import com.squareup.picasso.Picasso;

import adapter.commentsAdapter;
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
