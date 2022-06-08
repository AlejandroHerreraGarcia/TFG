package com.example.tfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.tfg.adapters.PostAdapter;
import com.example.tfg.poo.Post;
import com.example.tfg.providers.PostProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class BuscarActivity extends AppCompatActivity {

    String extraCategory;

    RecyclerView recyclerView;
    PostProvider postProvider;
    PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        
        recyclerView = findViewById(R.id.recyclerViewResultados);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BuscarActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        
        postProvider = new PostProvider();
        
        
        extraCategory = getIntent().getStringExtra("category");

        
    }

    @Override
    public void onStart() { //Te muestra un RecyclerView con todos los post de la categoria sellecionada
        super.onStart();
        Query query = postProvider.getPostByCategory(extraCategory);
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post.class).build();
        postAdapter = new PostAdapter(options, BuscarActivity.this);
        recyclerView.setAdapter(postAdapter);
        postAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        postAdapter.stopListening();
    }
}