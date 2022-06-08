package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfg.adapters.CommentAdapter;
import com.example.tfg.poo.Comment;
import com.example.tfg.providers.AuthProvider;
import com.example.tfg.providers.CommentsProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import com.example.tfg.providers.PostProvider;
import com.example.tfg.providers.UsersProvider;
import com.google.firebase.firestore.Query;


import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostInfoActivity extends AppCompatActivity {


    UsersProvider mUsersProvider;
    PostProvider postProvider;
    CommentsProvider commentsProvider;
    AuthProvider auth;

    CommentAdapter commentAdapter;

    String postId;

    TextView eventTitel_tv;
    TextView desctiption_tv;
    TextView userName_tv;
    TextView emailUser_tv;
    TextView category_tv;
    TextView fecha_tv;
    ImageView caberera_img;
    ImageView perfil_iv;
    CircleImageView mCircleImageViewProfile;
    Button profile_btn;
    FloatingActionButton comment_btn;
    RecyclerView recyclerView;

    String idUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_info);
        


        eventTitel_tv = findViewById(R.id.eventTitel_tv);
        desctiption_tv = findViewById(R.id.desctiption_tv);
        userName_tv = findViewById(R.id.userName_tv);
        emailUser_tv = findViewById(R.id.emailUser_tv);
        fecha_tv = findViewById(R.id.fecha_tv);
        category_tv = findViewById(R.id.category_tv);
        caberera_img = findViewById(R.id.caberera_img);
        perfil_iv = findViewById(R.id.perfil_iv);

        profile_btn = findViewById(R.id.profile_btn);
        comment_btn = findViewById(R.id.comment_btn);

        recyclerView = findViewById(R.id.recyclerViewComment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PostInfoActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        postProvider = new PostProvider();
        mUsersProvider = new UsersProvider();
        commentsProvider = new CommentsProvider();
        auth = new AuthProvider();

        postId = getIntent().getStringExtra("id");

        getPost();

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfile();
            }
        });

        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showComment();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = commentsProvider.getCommentsByPost(postId);
        FirestoreRecyclerOptions<Comment> options = new FirestoreRecyclerOptions.Builder<Comment>().setQuery(query, Comment.class).build();
        commentAdapter = new CommentAdapter(options, PostInfoActivity.this);
        recyclerView.setAdapter(commentAdapter);
        commentAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        commentAdapter.stopListening();
    }

    private void showComment(){
        AlertDialog.Builder alert = new AlertDialog.Builder(PostInfoActivity.this);
        alert.setTitle("COMENTARIO");
        alert.setMessage("Comenta");

        EditText editText = new EditText(PostInfoActivity.this);
        alert.setView(editText);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String comentario = editText.getText().toString();
                if (!comentario.isEmpty()){
                    createComment(comentario);
                }else {
                    Toast.makeText(PostInfoActivity.this, "Es necesario ingresar un comentario", Toast.LENGTH_SHORT).show();
                }
                

            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }

    private void createComment(String comentario) {
        Comment comment = new Comment();
        comment.setComment(comentario);
        comment.setIdPost(postId);
        comment.setIdUser(auth.getUid());
        comment.setTime(new Date().getTime());
        commentsProvider.create(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(PostInfoActivity.this, "El comentario se creo correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PostInfoActivity.this, "No se pudo crear el comentario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showProfile() {
        if (!idUser.equals("")){
            Intent intent = new Intent(PostInfoActivity.this, UserProfileActivity.class);
            intent.putExtra("id", idUser);
            startActivity(intent);
        }else {
            Toast.makeText(this, "Ups... Prueba otra vez", Toast.LENGTH_SHORT).show();
        }
        
    }


    private void getPost() {
        postProvider.getPostById(postId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    if (documentSnapshot.contains("title")) {
                        String title = documentSnapshot.getString("title");
                        eventTitel_tv.setText(title.toUpperCase());
                    }
                    if (documentSnapshot.contains("description")) {
                        String description = documentSnapshot.getString("description");
                        desctiption_tv.setText(description);
                    }
                    if (documentSnapshot.contains("category")) {
                        String category = documentSnapshot.getString("category");
                        category_tv.setText(category);

                        if (category.equals("Conocer Gente")) {
                            caberera_img.setImageResource(R.drawable.ic_conocer_gente);
                        }
                        else if (category.equals("Aventura")) {
                            caberera_img.setImageResource(R.drawable.aventura);
                        }
                        else if (category.equals("Fiesta")) {
                            caberera_img.setImageResource(R.drawable.fiesta);
                        }
                        else if (category.equals("Chill")) {
                            caberera_img.setImageResource(R.drawable.chill);
                        }
                    }
                    if (documentSnapshot.contains("fecha")) {
                        String mfecha = documentSnapshot.getString("fecha");
                        fecha_tv.setText(mfecha);
                    }
                    if (documentSnapshot.contains("idUser")) {
                        idUser = documentSnapshot.getString("idUser");
                        getUserInfo(idUser);
                    }
                }
            }
        });
    }

    private void getUserInfo(String idUser) {
        mUsersProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("userName")) {
                        String username = documentSnapshot.getString("userName");
                        userName_tv.setText(username);
                    }
                    if (documentSnapshot.contains("email")) {
                        String email = documentSnapshot.getString("email");
                        emailUser_tv.setText(email);
                    }

                    if (documentSnapshot.contains("icon")) {

                        if (documentSnapshot.getString("icon").contains("Yoshi")){
                            perfil_iv.setImageResource(R.drawable.yoshi);
                        } else if (documentSnapshot.getString("icon").contains("Purshi")){
                            perfil_iv.setImageResource(R.drawable.purple_yoshi);
                        }else if (documentSnapshot.getString("icon").contains("Broshi")){
                            perfil_iv.setImageResource(R.drawable.brown_yoshi);
                        }else if (documentSnapshot.getString("icon").contains("Boshi")){
                            perfil_iv.setImageResource(R.drawable.boshi_tm_cut);
                        } else {
                            perfil_iv.setImageResource(R.drawable.ic_perfil);
                        }
                    }
                }
            }
        });
    }
}
