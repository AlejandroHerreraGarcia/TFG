package com.example.tfg.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tfg.PostActivity;
import com.example.tfg.R;
import com.example.tfg.adapters.PostAdapter;
import com.example.tfg.poo.Post;
import com.example.tfg.providers.PostProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;


public class HomeFragment extends Fragment {

    View view;
    FloatingActionButton add_btn;
    RecyclerView recyclerView;
    PostProvider postProvider;
    PostAdapter postAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);
        add_btn = view.findViewById(R.id.add_btn);

        recyclerView = view.findViewById(R.id.recyclerViewHome);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        postProvider = new PostProvider();


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });
        return view;
    }

    @Override
    public void onStart() { //Te muestra un recyclerView con todos los post
        super.onStart();
        Query query = postProvider.getAll();
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post.class).build();
        postAdapter = new PostAdapter(options, getContext());
        recyclerView.setAdapter(postAdapter);
        postAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        postAdapter.stopListening();
    }

    private void addPost() {    //Te lleva a una nueva activity para crear un post
        Intent intent = new Intent(getContext(), PostActivity.class);
        startActivity(intent);
    }
}