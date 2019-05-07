package com.svr.techno;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText productName, productPrice, productDesc, productVideo;
    EditText productCategory2, productCategory3;

    Spinner productCategory1;

    FloatingActionButton productUpload;
    Button productCover;

    private String category1;
    private Uri coverUri;
    private String TAG = "SellActivity";

    FirebaseAuth authInstance;
    FirebaseFirestore firestoreInstance;
    FirebaseStorage storageInstance;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        productDesc = findViewById(R.id.sell_product_desc);
        productUpload = findViewById(R.id.sell_product_done);
        productName = findViewById(R.id.sell_product_name);
        productPrice = findViewById(R.id.sell_product_price);
        productCover = findViewById(R.id.sell_product_image);
        productVideo = findViewById(R.id.sell_product_video);


        productDesc.setOnTouchListener(this);

        productCover.setOnClickListener(this);
        productUpload.setOnClickListener(this);

        authInstance = FirebaseAuth.getInstance();
        firestoreInstance = FirebaseFirestore.getInstance();
        storageInstance = FirebaseStorage.getInstance();

        initSpinner();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.getId() == R.id.sell_product_desc) {

            v.getParent().requestDisallowInterceptTouchEvent(true);
            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_UP:
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;

            }

        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.sell_product_image:
                pickImage();
                break;

            case R.id.sell_product_done:
                uploadData();
                break;

        }
    }

    private void uploadData() {

        String name = productName.getText().toString().trim();
        int price = Integer.parseInt(productPrice.getText().toString().trim());
        String desc = productDesc.getText().toString().trim();
        String tutorial = productVideo.getText().toString().trim();
        String uploadDate = getCurrentDate();

        /// Referensi untuk upload foto profil ke Firebase Storage


        ///
        DocumentReference userReference = firestoreInstance.collection("items").document();
        String key = userReference.getId();


        StorageReference storageReference = storageInstance.getReference().child("items").child(key).child("cover");
        uploadWithUpdate(coverUri, storageReference, userReference);

        String sellerId = authInstance.getCurrentUser().getUid();
        String itemId = key;

        Map<String, Object> item = new HashMap<>();
        item.put("itemId", itemId);
        item.put("sellerId", sellerId);
        item.put("name", name);
        item.put("price", price);
        item.put("description", desc);
        item.put("uploadDate", uploadDate);
        item.put("category", category1);
        item.put("itemSold", 0);

        firestoreOperation(item, userReference, 1);

    }

    public String getCurrentDate() {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        return format.format(calendar.getTime());
    }

    private void test(String input) {
        Toast.makeText(SellActivity.this, input,Toast.LENGTH_SHORT).show();
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
                            input.put("cover", task.getResult().toString());

                            firestoreOperation(input, documentReference, 1);
                            //
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                coverUri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void pickImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);


    }


    private void initSpinner() {

        productCategory1 = findViewById(R.id.sell_product_category1);

        List<String> categoryList = new ArrayList<>();
        categoryList.add("--------");
        categoryList.add("Makanan");
        categoryList.add("Minuman");

        ArrayAdapter<String> productCategory1Adapter = new ArrayAdapter<>(SellActivity.this, R.layout.support_simple_spinner_dropdown_item, categoryList);
        productCategory1Adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        productCategory1.setAdapter(productCategory1Adapter);
        productCategory1.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        category1 = (String) parent.getItemAtPosition(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
