package ba.sum.fsre.mymath.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

import ba.sum.fsre.mymath.R;
import ba.sum.fsre.mymath.models.Lesson;
import ba.sum.fsre.mymath.models.User;

public class LeaderboardAdapter extends ArrayAdapter<User> {
    Context context;
    List<User> users;
    public LeaderboardAdapter (Context context, List<User> users) {
        super(context, R.layout.leaderboard_list_item, users);
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.leaderboard_list_item, parent, false);
        }

        User user = this.users.get(position);

        TextView usernameTxt = convertView.findViewById(R.id.usernameTxt);
        TextView pointsTxt = convertView.findViewById(R.id.pointsTxt);

        usernameTxt.setText(user.getUsername());
        pointsTxt.setText(user.getPoints().toString());
        return convertView;
    }
}
