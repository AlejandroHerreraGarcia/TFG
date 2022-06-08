package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class EditProfileActivity extends AppCompatActivity {

    EditText registroUser_et, registroEdad_et, registroBio_et;
    Button registrar_btn;
    TextView icon_tv;
    ImageView yoshi_iv, purple_yoshi_iv, brown_yoshi_iv, boshi_tm_cut_iv;
    String icon = "";
    FirebaseAuth auth;
    FirebaseFirestore db;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().hide();

        registroUser_et = findViewById(R.id.registroUser_et);
        registroEdad_et = findViewById(R.id.registroEdad_et);
        registroBio_et = findViewById(R.id.registroBio_et);
        icon_tv = findViewById(R.id.icon_tv);
        yoshi_iv = findViewById(R.id.yoshi_iv);
        purple_yoshi_iv = findViewById(R.id.purple_yoshi_iv);
        brown_yoshi_iv = findViewById(R.id.brown_yoshi_iv);
        boshi_tm_cut_iv = findViewById(R.id.boshi_tm_cut_iv);

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

                update();
            }
        });

        yoshi_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon = "Yoshi";
                icon_tv.setText(icon);
            }
        });

        purple_yoshi_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon = "Purshi";
                icon_tv.setText(icon);
            }
        });

        brown_yoshi_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon = "Broshi";
                icon_tv.setText(icon);
            }
        });

        boshi_tm_cut_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                icon = "Boshi";
                icon_tv.setText(icon);
            }
        });

    }

    private void update() {

        String userName = registroUser_et.getText().toString();
        String edad = registroEdad_et.getText().toString();
        String bio = registroBio_et.getText().toString();
        if (!userName.isEmpty() && !edad.isEmpty() && !bio.isEmpty()){
            updateUser(userName, edad, bio);
        }else{
            Toast.makeText(this, "Rellene todos los campos para completar la accion", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUser(String userName, String edad, String bio){
        String id = auth.getCurrentUser().getUid();
        alert.show();
        Map<String, Object> map = new HashMap<>();
        map.put("alias", userName);
        map.put("edad", edad);
        map.put("biofrafia", bio);
        map.put("icon", icon);
        db.collection("Users").document(id).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                alert.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(EditProfileActivity.this, "Se han actualizado los datos correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditProfileActivity.this, Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Toast.makeText(EditProfileActivity.this, "No se pudo almacenar el usuario en la base de datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}