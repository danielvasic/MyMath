package ba.sum.fsre.mymath;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ba.sum.fsre.mymath.utils.RandomNumberGenerator;

public class GameOneActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    TextView textView;
    EditText studentInput;

    View shadowOverlay;
    Integer sum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_one);

        textView = findViewById(R.id.numbersGeneratorTxt);
        linearLayout = findViewById(R.id.studentInputLinearLayout);
        studentInput = findViewById(R.id.studentInputTxt);
        shadowOverlay = findViewById(R.id.shadowOverlay);


        Button restartButton = findViewById(R.id.inputRestartBtn);
        Button submitButton = findViewById(R.id.inputSubmitBtn);

        reStartGame();

        restartButton.setOnClickListener(v -> reStartGame());
        submitButton.setOnClickListener(v -> checkSum());

    }

    private void checkSum(){
        if (studentInput.getText().toString().equals(String.valueOf(sum))) {
            Toast.makeText(this, "Odgovor je točan!", Toast.LENGTH_SHORT).show();
            showShadowEffect(true);
        } else {
            Toast.makeText(this, "Odgovor je netočan!", Toast.LENGTH_SHORT).show();
            showShadowEffect(false);
        }
    }

    private void reStartGame (){
        sum = 0;
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
            }, 800 * i);
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