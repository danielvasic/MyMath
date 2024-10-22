package ba.sum.fsre.mymath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.mAuth = FirebaseAuth.getInstance();

        EditText registerEmailTxt = findViewById(R.id.registerEmailTxt);
        EditText registerPasswordTxt = findViewById(R.id.registerPasswordTxt);
        EditText registerPasswordConfirmTxt = findViewById(R.id.registerConfirmPasswordTxt);

        Button registerBtn = findViewById(R.id.registerNewBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = registerEmailTxt.getText().toString();
                String password = registerPasswordTxt.getText().toString();
                String passwordConfirm = registerPasswordConfirmTxt.getText().toString();

                if (password.equals(passwordConfirm)) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                // Registration successful
                                Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                            } else {
                                // Registration failed
                                Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}