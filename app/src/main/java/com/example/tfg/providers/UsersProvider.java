package com.example.tfg.providers;

import com.example.tfg.poo.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UsersProvider {

    private CollectionReference collectionReference;
    String id = "";

    public UsersProvider() {
        collectionReference = FirebaseFirestore.getInstance().collection("Users");
    }

    public Task<DocumentSnapshot> getUser(String id) {
        return collectionReference.document(id).get();
    }

    public Task<Void> create(User user) {
        return collectionReference.document(user.getId()).set(user);
    }

    public Task<Void> update(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        return collectionReference.document(user.getId()).update(map);
    }

}
