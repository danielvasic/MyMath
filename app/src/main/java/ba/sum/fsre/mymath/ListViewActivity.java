package ba.sum.fsre.mymath;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ba.sum.fsre.mymath.adapters.LessonAdapter;
import ba.sum.fsre.mymath.models.Lesson;

public class ListViewActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private ListView listView;
    private LessonAdapter lessonAdapter;
    private List<Lesson> lessons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        listView = findViewById(R.id.listView);
        db = FirebaseFirestore.getInstance();
        lessons = new ArrayList<>();
        lessonAdapter = new LessonAdapter(this, lessons);
        listView.setAdapter(lessonAdapter);

        getLessons();

    }

    private void getLessons () {
        db.collection("lessons").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                lessons.clear();
                QuerySnapshot documents = task.getResult();
                for (DocumentSnapshot document : documents) {
                    String imageUrl = document.getString("imageUrl");
                    String title = document.getString("title");

                    Lesson lesson = new Lesson(title, imageUrl);
                    lessons.add(lesson);
                }
                lessonAdapter.notifyDataSetChanged();
            }
        });
    }
}