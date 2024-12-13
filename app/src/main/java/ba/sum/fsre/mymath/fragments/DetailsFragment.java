package ba.sum.fsre.mymath.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import ba.sum.fsre.mymath.R;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

public class DetailsFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Uri selectedImageUri;

    // MinIO Configuration
    private static final String MINIO_ENDPOINT = "https://io.sum.ba";
    private static final String MINIO_ACCESS_KEY = "dvasic";
    private static final String MINIO_SECRET_KEY = "ES#MoSt4R.";
    private static final String BUCKET_NAME = "uploads";

    private MinioClient minioClient;
    private ImageButton profileImageButton;

    public DetailsFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize MinIO client
        minioClient = MinioClient.builder()
                .endpoint(MINIO_ENDPOINT)
                .credentials(MINIO_ACCESS_KEY, MINIO_SECRET_KEY)
                .build();

        View v = inflater.inflate(R.layout.fragment_details, container, false);
        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();

        EditText firstNameTxt = v.findViewById(R.id.firstNameTxt);
        EditText lastNameTxt = v.findViewById(R.id.lastNameTxt);
        EditText dateOfBirthTxt = v.findViewById(R.id.dateOfBirthTxt);
        EditText telephoneTxt = v.findViewById(R.id.telephoneTxt);
        profileImageButton = v.findViewById(R.id.profileImageButton);

        profileImageButton.setOnClickListener(view -> selectProfileImage());

        String uid = mAuth.getCurrentUser().getUid();

        // Fetch user data from Firestore
        db.collection("users").document(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    firstNameTxt.setText(document.getString("firstName"));
                    lastNameTxt.setText(document.getString("lastName"));
                    dateOfBirthTxt.setText(document.getString("dateOfBirth"));
                    telephoneTxt.setText(document.getString("telephone"));
                    String profileImageUrl = document.getString("profileImageUrl");

                    // Load profile image from Firestore if available
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Picasso.get().load(profileImageUrl).placeholder(R.drawable.ic_default_profile).into(profileImageButton);
                    } else {
                        profileImageButton.setImageResource(R.drawable.ic_default_profile);
                    }
                }
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            try {
                uploadFile(mAuth.getCurrentUser().getUid());
                Toast.makeText(getContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void selectProfileImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    private void uploadFile(String uid) throws Exception {
        if (selectedImageUri != null) {
            // Convert URI to File
            InputStream inputStream = getActivity().getContentResolver().openInputStream(selectedImageUri);
            File tempFile = File.createTempFile("upload", ".tmp", getActivity().getCacheDir());
            FileOutputStream out = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            inputStream.close();

            String objectName = "user-files/" + uid + "/" + tempFile.getName();

            // Upload the file to MinIO bucket
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(objectName)
                            .stream(new FileInputStream(tempFile), tempFile.length(), -1)
                            .contentType("image/jpeg")
                            .build()
            );

            String fileUrl = MINIO_ENDPOINT + "/" + BUCKET_NAME + "/" + objectName;

            // Update user's profile with the image URL
            db.collection("users").document(uid).update("profileImageUrl", fileUrl)
                    .addOnSuccessListener(aVoid -> Picasso.get().load(fileUrl).placeholder(R.drawable.ic_default_profile).into(profileImageButton))
                    .addOnFailureListener(e -> e.printStackTrace());
        }
    }
}
