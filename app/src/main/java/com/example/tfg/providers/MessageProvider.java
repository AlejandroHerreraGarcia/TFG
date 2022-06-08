package com.example.tfg.providers;

import com.example.tfg.poo.Message;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MessageProvider {

    CollectionReference collectionReference;

    public MessageProvider() {

        collectionReference = FirebaseFirestore.getInstance().collection("Messages");
    }

    public Task<Void> create(Message message){
        DocumentReference document = collectionReference.document();
        message.setId(document.getId());
        return collectionReference.document().set(message);
    }

    public Query getMessageByChat(String idChat){
        return collectionReference.whereEqualTo("idChat", idChat).orderBy("time", Query.Direction.ASCENDING);
    }
}
