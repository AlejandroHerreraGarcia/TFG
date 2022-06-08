package com.example.tfg.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.example.tfg.R;
import com.example.tfg.PostInfoActivity;
import com.example.tfg.poo.Post;
import com.example.tfg.providers.AuthProvider;
import com.example.tfg.providers.PostProvider;
import com.example.tfg.providers.UsersProvider;
import com.example.tfg.RelativeTime.RelativeTime;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPostAdapter extends FirestoreRecyclerAdapter<Post, MyPostAdapter.ViewHolder> {

    Context context;
    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;

    public MyPostAdapter(FirestoreRecyclerOptions<Post> options, Context context) {
        super(options);
        this.context = context;
        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final Post post) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String postId = document.getId();
        String relativeTime = RelativeTime.getTimeAgo(post.getTime(), context);
        holder.time_tv.setText(relativeTime);
        holder.userName_tv.setText(post.getTitle().toUpperCase());
        if (post.getIdUser().equals(mAuthProvider.getUid())) {
            holder.delete_iv.setVisibility(View.VISIBLE);
        }
        else {
            holder.delete_iv.setVisibility(View.GONE);
        }

        if (post.getCategory().contains("Conocer Gente")) {
            holder.circleImagePost.setImageResource(R.drawable.ic_conocer_gente);
        }else if (post.getCategory().contains("Aventura")) {
            holder.circleImagePost.setImageResource(R.drawable.aventura);
        }else if (post.getCategory().contains("Fiesta")) {
            holder.circleImagePost.setImageResource(R.drawable.fiesta);
        }else if (post.getCategory().contains("Chill")) {
            holder.circleImagePost.setImageResource(R.drawable.chill);
        }else {
            holder.circleImagePost.setImageResource(R.drawable.error);
        }
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostInfoActivity.class);
                intent.putExtra("id", postId);
                context.startActivity(intent);
            }
        });

        holder.delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDelete(postId);
            }
        });


    }

    private void showConfirmDelete(final String postId) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Eliminar publicacion")
                .setMessage("¿Estas seguro de realizar esta accion?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deletePost(postId);
                    }
                })
                .setNegativeButton("NO", null)
                .show();
    }

    private void deletePost(String postId) {
        mPostProvider.delete(postId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "El post se elimino correctamente", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "No se pudo eliminar el post", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_post_view, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName_tv;
        TextView time_tv;
        CircleImageView circleImagePost;
        ImageView delete_iv;
        View viewHolder;

        public ViewHolder(View view) {
            super(view);
            userName_tv = view.findViewById(R.id.userNameMyPost_tv);
            time_tv = view.findViewById(R.id.timeMyPost_tv);
            circleImagePost = view.findViewById(R.id.circleImageMyPost);
            delete_iv = view.findViewById(R.id.delete_iv);
            viewHolder = view;
        }
    }



}
