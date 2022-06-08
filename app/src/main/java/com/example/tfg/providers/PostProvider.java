package com.example.tfg.providers;

import com.example.tfg.poo.Post;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PostProvider {
    CollectionReference collectionReference;

    public PostProvider() {
        collectionReference = FirebaseFirestore.getInstance().collection("Posts");
    }

    public Task<DocumentSnapshot> getPost(String id) {
        return collectionReference.document(id).get();
    }

    public Task<Void> save(Post post) {
        return collectionReference.document().set(post);
    }

    public Query getAll() {
        return collectionReference.orderBy("fecha", Query.Direction.ASCENDING);
    }

    public Query getPostByUser(String id) {
        return collectionReference.whereEqualTo("idUser", id);
    }

    public Task<DocumentSnapshot> getPostById(String id) {
        return collectionReference.document(id).get();
    }

    public Task<Void> delete(String id) {
        return collectionReference.document(id).delete();
    }


    public Query getPostByCategory(String category) {
        return collectionReference.whereEqualTo("category", category).orderBy("fecha", Query.Direction.ASCENDING);
    }

    public Query getPostByTitle(String title) {
        return collectionReference.orderBy("title").startAt(title).endAt(title + '\uf8ff');
    }

}
