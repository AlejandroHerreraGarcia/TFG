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

public class CompleteProfileActivity extends AppCompatActivity {

    EditText  registroUser_et, registroEdad_et, registroBio_et;
    Button registrar_btn;
    FirebaseAuth auth;
    FirebaseFirestore db;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        getSupportActionBar().hide();

        registroUser_et = findViewById(R.id.registroUser_et);
        registroEdad_et = findViewById(R.id.registroEdad_et);
        registroBio_et = findViewById(R.id.registroBio_et);

        registrar_btn = findViewById(R.id.registrar_btn);

        alert = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();

        auth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        registrar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register(); // Cuando pulsas el botonde completar registro se ejecuta el metodo de register()
            }
        });

    }

    private void register() {   //Recoge el contenido de los EditText y comprueba si estan vacios, si estan vacios te muestra un error, si tienen contenido se ejecuta el metodo updateUser()

        String userName = registroUser_et.getText().toString();
        String edad = registroEdad_et.getText().toString();
        String bio = registroBio_et.getText().toString();

        if (!userName.isEmpty() && !edad.isEmpty() && !bio.isEmpty()){

                updateUser(userName, edad, bio);
            } else{
                Toast.makeText(this, "No se pudo completar la accion", Toast.LENGTH_SHORT).show();
            }
    }

    private void updateUser(String userName, String edad, String bio){  //Hace un update en la BBDD y si la tarea se completo correctamente te lleva a home, si salio mal te muestra un mensaje de error
        String id = auth.getCurrentUser().getUid();
        alert.show();
        Map<String, Object> map = new HashMap<>();
        map.put("alias", userName);
        map.put("edad", edad);
        map.put("biofrafia", bio);
        db.collection("Users").document(id).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                alert.dismiss();
                if (task.isSuccessful()){
                    Intent intent = new Intent(CompleteProfileActivity.this, Home.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(CompleteProfileActivity.this, "No se pudo almacenar el usuario en la base de datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}