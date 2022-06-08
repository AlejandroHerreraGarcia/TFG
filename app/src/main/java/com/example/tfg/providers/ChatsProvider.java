package com.example.tfg.providers;

import com.example.tfg.poo.Chat;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class ChatsProvider {

    CollectionReference collectionReference;

    public ChatsProvider(){
        collectionReference = FirebaseFirestore.getInstance().collection("Chat");
    }

    public void create(Chat chat){
        collectionReference.document(chat.getIdUser1() + chat.getIdUser2()).set(chat);
    }

    public Query getAll(String idUser){
        return collectionReference.whereArrayContains("ids", idUser );
    }

    public Query getChatByUser1User2(String idUser1, String idUser2){
        ArrayList<String> ids = new ArrayList<String>();
        ids.add(idUser1 + idUser2);
        ids.add(idUser2 + idUser1);
        return collectionReference.whereIn("id", ids);
    }
}
