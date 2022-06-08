package com.example.tfg.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tfg.R;
import com.example.tfg.adapters.ChatsAdapter;
import com.example.tfg.adapters.PostAdapter;
import com.example.tfg.poo.Chat;
import com.example.tfg.poo.Post;
import com.example.tfg.providers.AuthProvider;
import com.example.tfg.providers.ChatsProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


public class ChatFragment extends Fragment {

    ChatsAdapter chatsAdapter;
    RecyclerView recyclerView;

    ChatsProvider chatsProvider;
    AuthProvider auth;
    View view;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewChat);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        chatsProvider = new ChatsProvider();
        auth = new AuthProvider();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = chatsProvider.getAll(auth.getUid());
        FirestoreRecyclerOptions<Chat> options = new FirestoreRecyclerOptions.Builder<Chat>().setQuery(query, Chat.class).build();
        chatsAdapter = new ChatsAdapter(options, getContext());
        recyclerView.setAdapter(chatsAdapter);
        chatsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        chatsAdapter.stopListening();
    }
}