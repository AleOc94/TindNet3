package com.example.tindnet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;






public class LoginCliente extends AppCompatActivity {
    private static final String TAG="LoginCliente";
    private EditText editTextName, editTextEmail, editTextPassword;

    private Button botonRegistrarCliente;
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;




    // Constante para indicar el tipo de usuario
    private static final String USER_TYPE = "cliente";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_cliente);

        // Manejador para el botón de continuar

// Inicializar Firebase Authentication y Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("clientes");

        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Obtención de referencias a los EditTexts
        editTextName = findViewById(R.id.editTextNombre);
        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editContrasenaCliente);

        Button buttonCreateAccount = findViewById(R.id.buttonContinuar);
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            // Obtención de los datos de los EditTexts
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

            // Llamada al método createUserWithEmailAndPassword con los datos obtenidos
                createUserWithEmailAndPassword(email, password);
                guardarDatosClienteEnFirebase(name, email);


            }
        });
    }

    private void guardarDatosClienteEnFirebase(String nombre, String email) {
        // Después de registrar al cliente exitosamente
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        // Guarda los datos del cliente en la base de datos
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        usersRef.child("nombre").setValue(nombre);
        usersRef.child("email").setValue(email);
        usersRef.child("tipo").setValue("cliente"); // Añadir el tipo de usuario como "cliente"
    }
    private void createUserWithEmailAndPassword(String email, String password) {
        // Verificar si la contraseña cumple con los requisitos
        if (password.length() < 6 || password.length() > 20) {
            Toast.makeText(LoginCliente.this, "La contraseña debe tener entre 6 y 20 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si el correo electrónico es válido
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(LoginCliente.this, "El formato del correo electrónico no es válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si el correo electrónico ya está registrado en Firebase Authentication
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.isSuccessful()) {
                    SignInMethodQueryResult result = task.getResult();
                    assert result != null;
                    if (result.getSignInMethods() != null && result.getSignInMethods().size() > 0) {
                    // El correo electrónico ya está registrado
                        Toast.makeText(LoginCliente.this, "El correo electrónico ya está registrado. Por favor, inicia sesión.", Toast.LENGTH_SHORT).show();
                    } else {
                    // El correo electrónico no está registrado, procede con la creación de usuario
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            // Obtener el ID del usuario creado
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            String userId = user.getUid();


                                        // Creación de usuario exitosa
                                            Log.d(TAG, "createUserWithEmail:success");
                                            FirebaseUser currentUser = mAuth.getCurrentUser();

                                            // Verificar si el correo electrónico está verificado
                                            if (user != null && !user.isEmailVerified()) {
                                                // Enviar correo electrónico de verificación
                                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(LoginCliente.this, "Se ha enviado un correo electrónico de verificación. Por favor, verifica tu cuenta.", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Log.e(TAG, "sendEmailVerification", task.getException());
                                                            Toast.makeText(LoginCliente.this, "No se pudo enviar el correo electrónico de verificación.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }

                                            updateUI(user);
                                        } else {
                                            // Si la creación de usuario falla, muestra un mensaje al usuario
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(LoginCliente.this, "Error al crear la cuenta. Por favor, inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
                                            updateUI(null);
                                        }
                                    }
                                });
                    }
                } else {
                    // Error al verificar el correo electrónico
                    Log.e(TAG, "fetchSignInMethodsForEmail:failure", task.getException());
                    Toast.makeText(LoginCliente.this, "Error al verificar el correo electrónico.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateUI(FirebaseUser user) {
// Aquí puedes actualizar la interfaz de usuario según el estado de autenticación
// Por ejemplo, puedes redirigir al usuario a otra actividad después de que haya iniciado sesión correctamente
        if (user != null) {
            Intent intent = new Intent (this, MainActivity.class);
            //Intent intent = new Intent(LoginCliente.this, MainActivity.class); //Cambiarlo por la página siguiente
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }
}

