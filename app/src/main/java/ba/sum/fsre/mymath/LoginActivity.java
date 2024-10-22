package ba.sum.fsre.mymath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.mAuth = FirebaseAuth.getInstance();
        EditText emailTxt = findViewById(R.id.emailTxt);
        EditText passwordTxt = findViewById(R.id.passwordTxt);

        Button loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailTxt.getText().toString();
                String password = passwordTxt.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // Login successful
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        } else {
                            // Login failed
                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        Button registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}