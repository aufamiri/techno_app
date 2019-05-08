package com.svr.techno;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class FirstSignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "FirstSignInActivity";
    private String storageUrl;

    private ImageView imageView;
    private FloatingActionButton done;

    private EditText inputName;

    private Uri profilePhotoUri;

    private FirebaseStorage storageInstance;
    private FirebaseAuth authInstance;
    private FirebaseFirestore firestoreInstance;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_sign_in);

        imageView = findViewById(R.id.first_sign_in_photo);
        done = findViewById(R.id.first_sign_in_fab);
        inputName = findViewById(R.id.first_sign_in_name);

        imageView.setOnClickListener(this);
        done.setOnClickListener(this);
        inputName.setOnClickListener(this);


        /// Inisialisasi Firebase
        storageInstance = FirebaseStorage.getInstance();
        authInstance = FirebaseAuth.getInstance();
        firestoreInstance = FirebaseFirestore.getInstance();

    }

    private void firestoreOperation(Map<String, Object> input, DocumentReference documentReference, int type) {

        switch (type) {
            case 0: // Set

                documentReference
                        .set(input)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Tampilan sukses
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

                break;
            case 1: // Set + Merge

                documentReference
                        .set(input, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Tampilan sukses
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

                break;
            case 2: // Update

                documentReference
                        .update(input)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Tampilan sukses
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

                break;
            case 3: // Delete

                documentReference
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Tampilan sukses
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

                break;

        }


    }

    private void uploadWithUpdate(Uri file, final StorageReference storageReference, final DocumentReference documentReference) {
        UploadTask uploadTask = storageReference.putFile(file);

        uploadTask
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return storageReference.getDownloadUrl();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            //storageUrl = task.getResult().toString();
                            Map<String, Object> input = new HashMap<>();
                            input.put("profilePhoto", task.getResult().toString());

                            firestoreOperation(input, documentReference, 1);
                            //
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });
    }

    private void updateData() {
        String name = inputName.getText().toString().trim();

        if (name.isEmpty() || profilePhotoUri == null) {

            inputName.setError("Harap isikan nama anda");
            inputName.requestFocus();

        } else {

            String uid = authInstance.getCurrentUser().getUid();

            /// Referensi untuk upload foto profil ke Firebase Storage
            StorageReference storageReference = storageInstance.getReference().child("users").child(uid).child("profilePhoto");

            ///
            DocumentReference userReference = firestoreInstance.collection("users").document(uid);

            /// Mengupload foto profil
            uploadWithUpdate(profilePhotoUri, storageReference, userReference);

            Map<String, Object> user = new HashMap<>();
            user.put("name", name);
            firestoreOperation(user, userReference, 1);

            startActivity(new Intent(this, HomeActivity.class));
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.first_sign_in_photo:
                pickImage();
                changePhoto(profilePhotoUri, imageView);
                break;
            case R.id.first_sign_in_fab:
                updateData();
                break;


        }
    }

    private void changePhoto(StorageReference path, ImageView place) {
        Glide
                .with(this)
                .load(path)
                .apply(RequestOptions.circleCropTransform())
                .into(place);
    }

    private void changePhoto(Uri path, ImageView place) {
        Glide
                .with(this)
                .load(path)
                .apply(RequestOptions.circleCropTransform())
                .into(place);
//                .load(url)
//                .apply(RequestOptions.circleCropTransform())
//                .into(imageView);
    }

    private void changePhoto(int resourceId, ImageView place) {
        Glide
                .with(this)
                .load(resourceId)
                .apply(RequestOptions.circleCropTransform())
                .into(place);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                profilePhotoUri = result.getUri();

                changePhoto(profilePhotoUri, imageView);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void pickImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(this);



    }


    private void test(String input) {
        Toast.makeText(FirstSignInActivity.this, input,Toast.LENGTH_SHORT).show();
    }
}
