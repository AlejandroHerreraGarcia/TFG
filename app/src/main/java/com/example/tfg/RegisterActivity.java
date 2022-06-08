package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tfg.providers.AuthProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

    CircleImageView backArrow;
    EditText  registroUser_et, registroEmail_et, registroPass_et, registroConfirmPass_et;
    Button registrar_btn;
    AuthProvider auth;
    FirebaseFirestore db;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        
        backArrow = findViewById(R.id.backArrow);

        registroUser_et = findViewById(R.id.registroUser_et);
        registroEmail_et = findViewById(R.id.registroEmail_et);
        registroPass_et = findViewById(R.id.registroPass_et);
        registroConfirmPass_et = findViewById(R.id.registroConfirmPass_et);

        registrar_btn = findViewById(R.id.registrar_btn);

        alert = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        auth = new AuthProvider();

        db = FirebaseFirestore.getInstance();

        registrar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register(); //cuando pulsas el boton de registrar ejecuta el metodo register()
            }
        });
        
        
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    private void register() {   //Primero se recoge el contenido de los EditText, comprueba que todos estan completados y que cumplan con los requesitos, si esta bien se ejecuta el metodo createUser()

        String userName = registroUser_et.getText().toString();
        String email = registroEmail_et.getText().toString();
        String password = registroPass_et.getText().toString();
        String confirmPassword = registroConfirmPass_et.getText().toString();
        
        if (!userName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()){
            if (isEmailValid(email)){
                if (password.equals(confirmPassword)){
                    if (password.length() >=6) {
                        createUser(userName, email, password);
                    }else {
                        Toast.makeText(this, "La contraseña deben tener al menos 6 caracteres", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(this, "Las contraseñas deben ser iguales", Toast.LENGTH_LONG).show();
                }

            }else{
                Toast.makeText(this, "El correo no es valido", Toast.LENGTH_SHORT).show();
            }


        }else {
            Toast.makeText(this, "Para continuar completa todos los campos", Toast.LENGTH_LONG).show();
        }

    }

    private void createUser(String userName, String email, String password){    // se crea el usuario en firebase Authentication, y crea un nuevo usuario en la BBDD, se comprueba si la tarea se completo correctamento si no es asi te muestra un tosast, si se completo correctamente te lleva a otra activity para completar el perfil del ususaio
        alert.show();
        auth.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String id = auth.getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put("userName", userName);
                    map.put("email", email);
                    map.put("password", password);
                    map.put("icon", "Yoshi");
                    db.collection("Users").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            alert.dismiss();
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "El usuario se registro correctamente", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, CompleteProfileActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(RegisterActivity.this, "No se pudo almacenar el usuario en la base de datos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(RegisterActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}