package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfg.adapters.MessagesAdapter;
import com.example.tfg.adapters.MyPostAdapter;
import com.example.tfg.poo.Chat;
import com.example.tfg.poo.Message;
import com.example.tfg.poo.Post;
import com.example.tfg.providers.AuthProvider;
import com.example.tfg.providers.ChatsProvider;
import com.example.tfg.providers.MessageProvider;
import com.example.tfg.providers.UsersProvider;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    String extraIdUser1;
    String extraIdUser2;
    String extraIdChat;

    String user, email;

    ImageView perfil_iv;
    TextView userName_tv, email_tv;

    ImageView send_btn;
    EditText message_et;

    RecyclerView recyclerView;

    ChatsProvider chatsProvider;
    AuthProvider auth;
    UsersProvider usersProvider;
    MessageProvider messageProvider;

    MessagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();

        extraIdUser1 = getIntent().getStringExtra("idUser1");
        extraIdUser2 = getIntent().getStringExtra("idUser2");
        extraIdChat = getIntent().getStringExtra("idChat");

        perfil_iv = findViewById(R.id.perfil_iv);
        userName_tv = findViewById(R.id.userName_tv);
        email_tv = findViewById(R.id.email_tv);

        message_et = findViewById(R.id.message_et);
        send_btn = findViewById(R.id.send_btn);
        
        recyclerView = findViewById(R.id.recyclerViewMessage);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        chatsProvider = new ChatsProvider();
        auth = new AuthProvider();
        usersProvider = new UsersProvider();
        messageProvider = new MessageProvider();

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        
        checkIfChatExist();
        getUser();
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = messageProvider.getMessageByChat(extraIdChat);
        FirestoreRecyclerOptions<Message> options =
                new FirestoreRecyclerOptions.Builder<Message>()
                        .setQuery(query, Message.class)
                        .build();
        adapter = new MessagesAdapter(options, ChatActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void sendMessage() {

        String textMessage = message_et.getText().toString();
        if (!textMessage.isEmpty()){
            Message message = new Message();
            message.setIdChat(extraIdChat);
            if (auth.getUid().equals(extraIdUser1)){
                message.setIdSend(extraIdUser1);
                message.setIdRecive(extraIdUser2);
            } else {
                message.setIdSend(extraIdUser2);
                message.setIdRecive(extraIdUser1);

            }
            message.setMessage(textMessage);

            message.setTime(new Date().getTime());
            messageProvider.create(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        message_et.setText("");
                        adapter.notifyDataSetChanged();
                        Toast.makeText(ChatActivity.this, "El mensaje se creo correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChatActivity.this, "El mensaje NO se creo correctamente", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void checkIfChatExist() {
        chatsProvider.getChatByUser1User2(extraIdUser1, extraIdUser2).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int size = queryDocumentSnapshots.size();
                if(size == 0){
                    createChat();
                }else {
                    extraIdChat = queryDocumentSnapshots.getDocuments().get(0).getId();
                }
            }
        });
    }

    private void createChat() {
        Chat chat = new Chat();
        chat.setIdUser1(extraIdUser1);
        chat.setIdUser2(extraIdUser2);
        chat.setTime(new Date().getTime());
        chat.setId(extraIdUser1 + extraIdUser2);
        ArrayList<String> ids = new ArrayList<String>();
        ids.add(extraIdUser1);
        ids.add(extraIdUser2);
        chat.setIds(ids);
        chatsProvider.create(chat);

    }

    private void getUser() {
        if (auth.getUid().equals(extraIdUser1)){
            usersProvider.getUser(extraIdUser2).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        user = documentSnapshot.getString("userName");
                        email = documentSnapshot.getString("email");


                        userName_tv.setText(user);
                        email_tv.setText(email);


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
        } else {
            usersProvider.getUser(extraIdUser1).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        user = documentSnapshot.getString("userName");
                        email = documentSnapshot.getString("email");


                        userName_tv.setText(user);
                        email_tv.setText(email);


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
}