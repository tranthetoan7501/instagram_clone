package com.hcmus.mobilappsocialnetworkingimage.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.adapter.commentsAdapter;
import com.hcmus.mobilappsocialnetworkingimage.model.commentsModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class commentFragment extends Fragment implements View.OnClickListener{
    commentsAdapter  commentsAdapter;
    ImageButton previous;
    CircleImageView avatar;
    RecyclerView recyclerView;
    EditText comment;
    ImageButton sendComment;
    CircleImageView belowAvatar;
    TextView description;
    TextView date;
    Bundle bundle = new Bundle();
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    List<commentsModel> comments = new ArrayList<>();
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
        sendComment = view.findViewById(R.id.sendComment);
        sendComment.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        getData();
        handleEvent();
        return view;
    }

    void getData(){
        commentsAdapter = new commentsAdapter(getContext(),comments,bundle.getString("user_id"),bundle.getString("post_id"));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(commentsAdapter);
        database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference postDetails = database.getReference("user_photos/"+bundle.get("user_id")+"/"+bundle.get("post_id"));
        postDetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    description.setText(snapshot.child("caption").getValue().toString());
                    date.setText(snapshot.child("date_created").getValue().toString());
                    comments.clear();
                    for(DataSnapshot data : snapshot.child("comments").getChildren()){
                        commentsModel temp = data.getValue(commentsModel.class);
                        comments.add(temp);
                    }
                    commentsAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference postOwner = database.getReference("user_account_settings");
        postOwner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if(dataSnapshot.getKey().equals(bundle.getString("user_id"))){
                        Picasso.get().load(dataSnapshot.child("profile_photo").getValue().toString()).into(avatar);
                    }
                    if(dataSnapshot.getKey().equals(mAuth.getUid())){
                        Picasso.get().load(dataSnapshot.child("profile_photo").getValue().toString()).into(belowAvatar);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    void handleEvent(){
        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(comment.getText().equals("")) {
                    sendComment.setVisibility(View.GONE);
                }
                else{
                    sendComment.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.previous:
                getActivity().finish();
                break;
            case R.id.sendComment:
                if(comment.getText()!=null){
                    DatabaseReference pushComment = database.getReference("user_photos/"+bundle.get("user_id")+"/"+bundle.get("post_id")+"/"+"comments");
                    if(pushComment != null){
                        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                        commentsModel model = new commentsModel(comment.getText().toString(),null,timeStamp,mAuth.getUid(),comments.size()+"");
                        pushComment.child(model.getComment_id()).setValue(model);
                        comment.setText("");
                    }
                    else {
                        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                        commentsModel model = new commentsModel(comment.getText().toString(),null,timeStamp,mAuth.getUid(),"0");
                        pushComment.child("comments/0").setValue(model);
                        comment.setText("");
                    }
                }
                break;
        }
    }
}
