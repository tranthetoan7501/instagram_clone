package fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.hcmus.mobilappsocialnetworkingimage.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class postFragment extends Fragment {
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        View view = inflater.inflate(R.layout.post, container, false);
        return view;
    }
}
