package com.example.tindnet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SingUp extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private Button buttonSingUp;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSingUp = findViewById(R.id.buttonSingUp);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión...");

        buttonSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Obtener referencia a la base de datos de Firebase
                                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

                                // Obtener el tipo de cuenta del usuario desde la base de datos
                                usersRef.child(user.getUid()).child("tipo").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String tipo = dataSnapshot.getValue(String.class);
                                        if (tipo != null) {
                                            // Verificar el tipo de cuenta y dirigir al usuario a la actividad correspondiente
                                            if (tipo.equals("cliente")) {
                                                startActivity(new Intent(SingUp.this, HomeClient.class));
                                            } else if (tipo.equals("empresa" )) {
                                                startActivity(new Intent(SingUp.this, HomeCompany.class));
                                            }
                                            finish();
                                        } else {
                                            Toast.makeText(SingUp.this, "Error: Tipo de cuenta desconocido", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(SingUp.this, "Error al obtener el tipo de cuenta", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(SingUp.this, "Error al iniciar sesión, verifica tus credenciales", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}