package com.example.tindnet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeCompany extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 1;

    private EditText editTextCompanyName;
    private EditText editTextCompanyDescription;

    private List<Uri> selectedImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_company);

        editTextCompanyName = findViewById(R.id.editTextCompanyName);
        editTextCompanyDescription = findViewById(R.id.editTextCompanyDescription);
    }

    public void selectImages(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    public void uploadData(View view) {
        // Aquí implementar la lógica para subir los datos (incluidas las imágenes) a Firebase
        // Puedes usar los métodos de Firebase Storage y Firestore para esto
        // No olvides obtener los datos del nombre de la empresa y la descripción de los EditText
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            if (data.getData() != null) {
                // Si se selecciona una sola imagen
                Uri imageUri = data.getData();
                selectedImages.add(imageUri);
            } else if (data.getClipData() != null) {
                // Si se seleccionan múltiples imágenes
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    selectedImages.add(imageUri);
                }
            }
        }
    }
}
