package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfg.providers.UsersProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import com.example.tfg.poo.Post;
import com.example.tfg.providers.AuthProvider;
import com.example.tfg.providers.PostProvider;
import com.google.firebase.firestore.DocumentSnapshot;


import java.util.Calendar;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class PostActivity extends AppCompatActivity {


    Button post_btn;
    DatePicker datePicker;
    PostProvider postProvider;
    AuthProvider auth;

    TextInputEditText eventName_et, fecha_et,  desctiption_et, location;
    ImageView conocerGente_iv, aventura_iv, fiesta_iv, chill_iv;
    TextView category_tv;
    String category = "",  title = "", fecha = "", description = "", icon = "";
    AlertDialog alert;
    private int dia, mes, ano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        getSupportActionBar().hide();


        postProvider = new PostProvider();
        auth = new AuthProvider();

        alert = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento")
                .setCancelable(false).build();


        post_btn = findViewById(R.id.post_btn);
        eventName_et = findViewById(R.id.eventName_et);
        location = findViewById(R.id.location);
        fecha_et = findViewById(R.id.fecha_et);
        desctiption_et = findViewById(R.id.desctiption_et);
        conocerGente_iv = findViewById(R.id.conocerGente_iv);
        aventura_iv = findViewById(R.id.aventura_iv);
        fiesta_iv = findViewById(R.id.fiesta_iv);
        chill_iv = findViewById(R.id.chill_iv);
        category_tv = findViewById(R.id.category_tv);




        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(); //Cuando pulsas el boton de post se ejecuta el metodo save()
            }
        });



        conocerGente_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //Cuando pulsas sobre una imagen le contenido de categoria cambia
                category = "Conocer Gente";
                category_tv.setText(category);
            }
        });

        aventura_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //Cuando pulsas sobre una imagen le contenido de categoria cambia
                category = "Aventura";
                category_tv.setText(category);
            }
        });

        fiesta_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //Cuando pulsas sobre una imagen le contenido de categoria cambia
                category = "Fiesta";
                category_tv.setText(category);
            }
        });

        chill_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //Cuando pulsas sobre una imagen le contenido de categoria cambia
                category = "Chill";
                category_tv.setText(category);
            }
        });

        fecha_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //Cuadno pulsas sobre fecha podras elegir una fecha de manera mas visual
                final Calendar calendario = Calendar.getInstance();
                dia = calendario.get(Calendar.DAY_OF_MONTH);
                mes = calendario.get(Calendar.MONTH);
                ano = calendario.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(PostActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        fecha_et.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                    }
                }
                        ,dia, mes, ano);
                datePickerDialog.show();
            }
        });
    }


    private void save() {   //Recoges el contenido de los EditText y creas un Post con esa informacion en la BBDD, si se completa correctamente te llevara a home, si sale mal te mostrara un mensaje de error
        title = eventName_et.getText().toString();
        fecha = fecha_et.getText().toString();
        description = desctiption_et.getText().toString();
        alert.show();

        Post post = new Post();
        post.setTitle(title);
        post.setLocation(location.getText().toString());
        post.setDescription(description);
        post.setFecha(fecha);
        post.setCategory(category);
        post.setIdUser(auth.getUid());
        post.setTime(new Date().getTime());
        postProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> taskSave) {
                alert.dismiss();
                if (taskSave.isSuccessful()) {
                    clearForm();
                    Toast.makeText(PostActivity.this, "La informacion se almaceno correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PostActivity.this, Home.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(PostActivity.this, "No se pudo almacenar la informacion", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void clearForm() { //Con esto limpas el contenido de los EditText
        eventName_et.setText("");
        desctiption_et.setText("");
        category_tv.setText("CATEGORIAS");
        title = "";
        description = "";
        category = "";
    }

}
