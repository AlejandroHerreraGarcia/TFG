package com.example.tfg.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfg.EditProfileActivity;
import com.example.tfg.LoginActivity;
import com.example.tfg.R;
import com.example.tfg.adapters.MyPostAdapter;
import com.example.tfg.poo.Post;
import com.example.tfg.providers.AuthProvider;
import com.example.tfg.providers.PostProvider;
import com.example.tfg.providers.UsersProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import static android.content.ContentValues.TAG;


public class PerfilFragment extends Fragment {

    View view;
    TextView user, email, bio_tv, postNumber_tv, postExist_tv, edad_tv;
    String aUser, aEmail, bio, edad;
    Button cerrar_sesion_btn, editar_btn;
    ImageView perfil_iv;

    RecyclerView recyclerView;

    AuthProvider auth;
    UsersProvider usersProvider;
    PostProvider postProvider;

    MyPostAdapter adapter;

    public PerfilFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_perfil, container, false);
        user = view.findViewById(R.id.user_tv);
        email = view.findViewById(R.id.email_tv);
        edad_tv = view.findViewById(R.id.edad_tv);
        bio_tv = view.findViewById(R.id.bio_tv);
        postNumber_tv = view.findViewById(R.id.postNumber_tv);
        postExist_tv = view.findViewById(R.id.postExist_tv);
        editar_btn = view.findViewById(R.id.editar_btn);
        cerrar_sesion_btn = view.findViewById(R.id.cerrar_sesion_btn);
        perfil_iv = view.findViewById(R.id.perfil_iv);

        recyclerView = view.findViewById(R.id.recyclerViewMyPost);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        auth = new AuthProvider();
        usersProvider = new UsersProvider();
        postProvider = new PostProvider();
        
        getUser();
        getPostNumber();
        checkIfExistPost();

        editar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goEdit();
            }
        });

        cerrar_sesion_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
        return view;
    }

    private void checkIfExistPost() {
        postProvider.getPostByUser(auth.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Log.d(TAG,"Error:"+e.getMessage());
                }else {
                    int numberPost = queryDocumentSnapshots.size();
                    if (numberPost > 0) {
                        postExist_tv.setText("Publicaciones");

                    }
                    else {
                        postExist_tv.setText("No hay publicaciones");

                    }
                }

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = postProvider.getPostByUser(auth.getUid());
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class)
                        .build();
        adapter = new MyPostAdapter(options, getContext());
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }



    private void goEdit() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void getPostNumber() {
        postProvider.getPostByUser(auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int numberPost = queryDocumentSnapshots.size();
                postNumber_tv.setText(String.valueOf(numberPost));
            }
        });
    }


    private void getUser() {
        usersProvider.getUser(auth.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    aUser = documentSnapshot.getString("userName");
                    aEmail = documentSnapshot.getString("alias");
                    bio = documentSnapshot.getString("biofrafia");
                    edad = documentSnapshot.getString("edad");


                    user.setText(aUser);
                    email.setText(aEmail);
                    edad_tv.setText(edad);
                    bio_tv.setText(bio);

                    
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
        });
    }

    private void logOut() {
        auth.logout();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
