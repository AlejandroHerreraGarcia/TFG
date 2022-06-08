package com.example.tfg.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.ChatActivity;
import com.example.tfg.R;
import com.example.tfg.poo.Chat;
import com.example.tfg.poo.Comment;
import com.example.tfg.providers.AuthProvider;
import com.example.tfg.providers.UsersProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class ChatsAdapter extends FirestoreRecyclerAdapter<Chat, ChatsAdapter.ViewHolder> {

    Context context;
    UsersProvider usersProvider;
    AuthProvider auth;





    public ChatsAdapter(FirestoreRecyclerOptions<Chat> options, Context context){
        super(options);
        this.context = context;
        usersProvider = new UsersProvider();
        auth = new AuthProvider();
    }


    public ChatsAdapter(@NonNull FirestoreRecyclerOptions<Chat> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Chat chat) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String chatId = document.getId();
        if (auth.getUid().equals(chat.getIdUser1())) {
            getUserInfo(chat.getIdUser2(), holder);
        } else {
            getUserInfo(chat.getIdUser1(), holder);
        }
        
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChat(chatId, chat.getIdUser1(), chat.getIdUser2());
            }
        });

    }

    private void showChat(String idChat, String idUser1, String idUser2) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("idChat", idChat);
        intent.putExtra("idUser1", idUser1);
        intent.putExtra("idUser2", idUser2);
        context.startActivity(intent);
    }

    private void getUserInfo(String idUser, ViewHolder holder) {
        usersProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("userName")) {
                        String username = documentSnapshot.getString("userName");
                        holder.userName_tv.setText(username.toUpperCase());
                    }
                    if (documentSnapshot.contains("icon")) {

                        if (documentSnapshot.getString("icon").contains("Yoshi")) {
                            holder.perfil_iv.setImageResource(R.drawable.yoshi);
                        } else if (documentSnapshot.getString("icon").contains("Purshi")) {
                            holder.perfil_iv.setImageResource(R.drawable.purple_yoshi);
                        } else if (documentSnapshot.getString("icon").contains("Broshi")) {
                            holder.perfil_iv.setImageResource(R.drawable.brown_yoshi);
                        } else if (documentSnapshot.getString("icon").contains("Boshi")) {
                            holder.perfil_iv.setImageResource(R.drawable.boshi_tm_cut);
                        } else {
                            holder.perfil_iv.setImageResource(R.drawable.ic_perfil);
                        }
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_view, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName_tv;
        TextView ultimoMensaje_tv;
        ImageView perfil_iv;

        View viewHolder;

        public ViewHolder(View view){
            super(view);
            userName_tv = view.findViewById(R.id.userName_tv);
            ultimoMensaje_tv = view.findViewById(R.id.ultimoMensaje_tv);
            perfil_iv = view.findViewById(R.id.perfil_iv);
            viewHolder = view;

        }
    }
}
