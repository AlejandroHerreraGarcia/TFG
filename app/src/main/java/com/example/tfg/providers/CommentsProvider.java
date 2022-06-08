package com.example.tfg.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.example.tfg.poo.Comment;

public class CommentsProvider {

    CollectionReference collectionReference;

    public CommentsProvider() {
        collectionReference = FirebaseFirestore.getInstance().collection("Comments");
    }

    public Task<Void> create(Comment comment) {
        return collectionReference.document().set(comment);
    }

    public Query getCommentsByPost(String idPost) {
        return collectionReference.whereEqualTo("idPost", idPost);
    }


}
