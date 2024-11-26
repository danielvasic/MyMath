package ba.sum.fsre.mymath.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ba.sum.fsre.mymath.R;
import ba.sum.fsre.mymath.adapters.LeaderboardAdapter;
import ba.sum.fsre.mymath.adapters.LessonAdapter;
import ba.sum.fsre.mymath.models.Lesson;
import ba.sum.fsre.mymath.models.User;

public class LeaderboardFragment extends Fragment {
    private FirebaseFirestore db;
    private ListView listView;
    private LeaderboardAdapter usersAdapter;
    private List<User> users;

    public LeaderboardFragment() {
        super();
    }

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.leaderboard_list_view, container, false);
        listView = v.findViewById(R.id.leaderboardListView);
        db = FirebaseFirestore.getInstance();
        users = new ArrayList<>();
        usersAdapter = new LeaderboardAdapter(v.getContext(), users);
        listView.setAdapter(usersAdapter);
        getUsers();
        return v;
    }

    private void getUsers () {
        db.collection("users").orderBy("points", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                users.clear();
                QuerySnapshot documents = task.getResult();
                for (DocumentSnapshot document : documents) {
                    String firstname = document.getString("firstName");
                    String lastname = document.getString("lastName");
                    String telephone = document.getString("telephone");
                    String dateOfBirth = document.getString("dateOfBirth");
                    Integer points = document.getLong("points").intValue();

                    User user = new User(firstname, lastname, telephone, dateOfBirth, points);
                    users.add(user);
                }
                usersAdapter.notifyDataSetChanged();
            }
        });
    }
}
