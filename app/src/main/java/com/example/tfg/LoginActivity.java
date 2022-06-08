package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfg.providers.AuthProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {


    TextView registro_tv;
    EditText email_et, password_et;
    Button login_btn;
    AuthProvider auth;
    FirebaseFirestore db;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();


        registro_tv = findViewById(R.id.registro_tv);

        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);

        login_btn = findViewById(R.id.login_btn);
        
        alert = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        auth = new AuthProvider();

        db = FirebaseFirestore.getInstance();



        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(); //Cuando pulsas el boton de login ejecuta el metodo login();

            }
        });

        registro_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegistro = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentRegistro);  //Cuando pulsas el boton de registro te lleva a la activity registro
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getUserSession() != null) {    //Si getUserSession es distinto de null es por que ya hay una sesion iniciada por lo cual hace login automatico
            Intent intent = new Intent(LoginActivity.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    private void login() {  //Recoge el contenido de los EditText y se ejecuta el metodo de login creado en el AuthProvider, cuando se complete el login, si sale bien te lleva a la activity home, si sale mal crea un Toast diciendo que el email o la contrasena no son correctas
        String email = email_et.getText().toString();
        String password = password_et.getText().toString();
        alert.show();
        auth.login(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                alert.dismiss();
                if (task.isSuccessful()) {
                    Intent intentHome = new Intent (LoginActivity.this, Home.class);
                    intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentHome);
                }else {
                    Toast.makeText(LoginActivity.this, "El email o la contrasena no son correctas", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}