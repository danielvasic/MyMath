package ba.sum.fsre.mymath.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.List;

import ba.sum.fsre.mymath.R;
import ba.sum.fsre.mymath.utils.RandomNumberGenerator;

public class GameOneFragment extends Fragment {
    LinearLayout linearLayout;
    TextView textView;
    EditText studentInput;

    TextView pointsTxt;

    View shadowOverlay;
    Integer sum;

    Integer points;

    FirebaseAuth mAuth;

    FirebaseFirestore db;

    String uuid;

    int gameCounter = 0;

    private static final int CORRECT_POINTS = 5;
    private static final int INCORRECT_POINTS = -7;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_one, container, false);
        textView = v.findViewById(R.id.numbersGeneratorTxt);
        linearLayout = v.findViewById(R.id.studentInputLinearLayout);
        studentInput = v.findViewById(R.id.studentInputTxt);
        shadowOverlay = v.findViewById(R.id.shadowOverlay);
        pointsTxt = v.findViewById(R.id.userPointsTxt);

        pointsTxt.setText(String.valueOf(this.points));

        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();

        this.uuid = mAuth.getCurrentUser().getUid();

        this.db.collection("users").document(uuid).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                points = document.getLong("points").intValue();
                pointsTxt.setText(String.valueOf(points));
            }
        });

        if (points == null) {
            points = 0;
        }

        pointsTxt.setText(String.valueOf(points));

        Button restartButton = v.findViewById(R.id.inputRestartBtn);
        Button submitButton = v.findViewById(R.id.inputSubmitBtn);

        reStartGame();

        restartButton.setOnClickListener(item -> reStartGame());
        submitButton.setOnClickListener(item -> checkSum());
        return v;
    }


    private void checkSum(){
        gameCounter++;
        if (gameCounter >= 2) {
            Toast.makeText(getContext(), "Pokušajte ponovo, ova igra je završena!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (studentInput.getText().toString().equals(String.valueOf(sum))) {
            Toast.makeText(getContext(), "Odgovor je točan!", Toast.LENGTH_SHORT).show();
            showShadowEffect(true);
            this.points += CORRECT_POINTS;
        } else {
            Toast.makeText(getContext(), "Odgovor je netočan!", Toast.LENGTH_SHORT).show();
            showShadowEffect(false);
            this.points += INCORRECT_POINTS;
        }
        pointsTxt.setText(String.valueOf(this.points));
        db.collection("users").document(uuid).update("points", this.points);
    }

    private void reStartGame (){
        sum = 0;
        gameCounter = 0;
        List<Integer> randomNumbers = new RandomNumberGenerator().generateRandomNumbers();
        linearLayout.setVisibility(LinearLayout.INVISIBLE);
        studentInput.setText("");
        Handler handler = new Handler(Looper.getMainLooper());
        for (int i = 0; i < randomNumbers.size(); i++) {
            final int index = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textView.setText(String.valueOf(randomNumbers.get(index)));
                    sum += randomNumbers.get(index);
                    if (index == 9) linearLayout.setVisibility(LinearLayout.VISIBLE);
                }
            }, 1500 * i);
        }
    }

    private void showShadowEffect(boolean isCorrect) {
        int drawable = isCorrect ? R.drawable.green_inset_shadow : R.drawable.red_inset_shadow;
        shadowOverlay.setBackgroundResource(drawable);
        shadowOverlay.setVisibility(View.VISIBLE);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> shadowOverlay.setVisibility(View.GONE), 1000);
    }
}