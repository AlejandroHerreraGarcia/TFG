package com.example.tfg.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.R;
import com.example.tfg.poo.Comment;
import com.example.tfg.providers.UsersProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class CommentAdapter extends FirestoreRecyclerAdapter<Comment, CommentAdapter.ViewHolder> {

    Context context;
    UsersProvider usersProvider = new UsersProvider();





    public CommentAdapter (FirestoreRecyclerOptions<Comment> options, Context context){
        super(options);
        this.context = context;
    }


    public CommentAdapter(@NonNull FirestoreRecyclerOptions<Comment> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Comment comment) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String commentId = document.getId();
        String idUser = document.getString("idUser");

        holder.comment_tv.setText(comment.getComment());
        getUserInfo(idUser, holder);

    }

    private void getUserInfo(String idUser,  ViewHolder holder) {
        usersProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("userName")) {
                        String username = documentSnapshot.getString("userName");
                        holder.userName_tv.setText(username.toUpperCase());
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_view, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName_tv;
        TextView comment_tv;

        View viewHolder;

        public ViewHolder(View view){
            super(view);
            userName_tv = view.findViewById(R.id.userName_tv);
            comment_tv = view.findViewById(R.id.comment_tv);
            viewHolder = view;

        }
    }
}
