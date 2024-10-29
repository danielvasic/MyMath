package ba.sum.fsre.mymath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import ba.sum.fsre.mymath.models.User;

public class DetailsActivity extends AppCompatActivity {
    FirebaseFirestore db;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        this.db = FirebaseFirestore.getInstance();

        this.mAuth = FirebaseAuth.getInstance();

        EditText firstNameTxt = findViewById(R.id.firstNameTxt);
        EditText lastNameTxt = findViewById(R.id.lastNameTxt);
        EditText dateOfBirthTxt = findViewById(R.id.dateOfBirthTxt);
        EditText telephoneTxt = findViewById(R.id.telephoneTxt);

        Button saveProfileBtn = findViewById(R.id.saveProfileBtn);

        String uid = mAuth.getCurrentUser().getUid();

        this.db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()){
                    User user = document.toObject(User.class);
                    firstNameTxt.setText(user.getFirstName());
                    lastNameTxt.setText(user.getLastName());
                    dateOfBirthTxt.setText(user.getDateOfBirth());
                    telephoneTxt.setText(user.getTelephone());
                }
            }
        });


        saveProfileBtn.setOnClickListener(view -> {
            String firstName = firstNameTxt.getText().toString();
            String lastName = lastNameTxt.getText().toString();
            String dateOfBirth = dateOfBirthTxt.getText().toString();
            String telephone = telephoneTxt.getText().toString();

            User newUser = new User(firstName, lastName, dateOfBirth, telephone);

            db.collection("users").document(uid).set(newUser);
        });






    }
}