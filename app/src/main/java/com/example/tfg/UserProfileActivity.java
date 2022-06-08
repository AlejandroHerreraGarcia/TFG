package com.example.tfg;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tfg.adapters.MyPostAdapter;
import com.example.tfg.poo.Post;
import com.example.tfg.providers.AuthProvider;
import com.example.tfg.providers.PostProvider;
import com.example.tfg.providers.UsersProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class UserProfileActivity extends AppCompatActivity {

    TextView user, email, bio_tv, postExist_tv;
    String aUser, aEmail, bio;
    FloatingActionButton chat_btn;
    ImageView perfil_iv;

    RecyclerView recyclerView;

    AuthProvider auth;
    UsersProvider usersProvider;
    PostProvider postProvider;

    String idUser = "";

    MyPostAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().hide();

        user = findViewById(R.id.user_tv);
        email = findViewById(R.id.email_tv);
        bio_tv = findViewById(R.id.bio_tv);
        postExist_tv = findViewById(R.id.postExist_tv);

        chat_btn = findViewById(R.id.chat_btn);

        perfil_iv = findViewById(R.id.perfil_iv);

        recyclerView = findViewById(R.id.recyclerViewMyPost);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserProfileActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        auth = new AuthProvider();
        usersProvider = new UsersProvider();
        postProvider = new PostProvider();

        idUser = getIntent().getStringExtra("id");

        if (auth.getUid().equals(idUser)){
            chat_btn.setVisibility(View.GONE);

        }

        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChat();
            }
        });

        getUser();
        checkIfExistPost();
    }

    private void showChat() {
        Intent intent = new Intent(UserProfileActivity.this, ChatActivity.class);
        intent.putExtra("idUser1", auth.getUid());
        intent.putExtra("idUser2", idUser);
        intent.putExtra("idChat", auth.getUid() + idUser);
        startActivity(intent);
    }

    private void checkIfExistPost() {
        postProvider.getPostByUser(idUser).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                int numberPost = queryDocumentSnapshots.size();
                if (numberPost > 0) {
                    postExist_tv.setText("Publicaciones");

                }
                else {
                    postExist_tv.setText("No hay publicaciones");

                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = postProvider.getPostByUser(idUser);
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
        adapter = new MyPostAdapter(options, UserProfileActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    private void getUser() {
        usersProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    aUser = documentSnapshot.getString("userName");
                    aEmail = documentSnapshot.getString("email");
                    bio = documentSnapshot.getString("biofrafia");

                    user.setText(aUser);
                    email.setText(aEmail);
                    bio_tv.setText(bio);

                    if (documentSnapshot.contains("icon")) {

                        if (documentSnapshot.getString("icon").contains("Yoshi")) {
                            perfil_iv.setImageResource(R.drawable.yoshi);
                        } else if (documentSnapshot.getString("icon").contains("Purshi")) {
                            perfil_iv.setImageResource(R.drawable.purple_yoshi);
                        } else if (documentSnapshot.getString("icon").contains("Broshi")) {
                            perfil_iv.setImageResource(R.drawable.brown_yoshi);
                        } else if (documentSnapshot.getString("icon").contains("Boshi")) {
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