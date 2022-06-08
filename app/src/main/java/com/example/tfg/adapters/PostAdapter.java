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

import com.example.tfg.PostInfoActivity;
import com.example.tfg.R;
import com.example.tfg.poo.Post;
import com.example.tfg.providers.UsersProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class PostAdapter extends FirestoreRecyclerAdapter<Post, PostAdapter.ViewHolder> {

    Context context;
    UsersProvider usersProvider;





    public PostAdapter (FirestoreRecyclerOptions<Post> options, Context context){
        super(options);
        this.context = context;
        usersProvider = new UsersProvider();
    }


    public PostAdapter(@NonNull FirestoreRecyclerOptions<Post> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post post) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String postId = document.getId();
        holder.title.setText(post.getTitle());
        getUserInfo(post.getIdUser(), holder);
        holder.category.setText(post.getCategory());
        holder.location.setText(post.getLocation());
        holder.description.setText(post.getDescription());
        holder.fecha.setText(post.getFecha());
        if (holder.category.getText().toString().contains("Conocer Gente")) {
            holder.post.setImageResource(R.drawable.ic_conocer_gente);
        }else if (holder.category.getText().toString().contains("Aventura")){
            holder.post.setImageResource(R.drawable.aventura);
        } else if (holder.category.getText().toString().contains("Fiesta")){
            holder.post.setImageResource(R.drawable.fiesta);
        }else if (holder.category.getText().toString().contains("Chill")){
            holder.post.setImageResource(R.drawable.chill);
        }else {
            holder.post.setImageResource(R.drawable.error);

        }
        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostInfoActivity.class);
                intent.putExtra("id", postId);
                context.startActivity(intent);
            }
        });

    }

    private void getUserInfo(String idUser, ViewHolder holder) {
        usersProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("userName")) {
                        String username = documentSnapshot.getString("userName");
                        holder.userName_tv.setText(username);
                    }
                    if (documentSnapshot.contains("icon")) {

                        if (documentSnapshot.getString("icon").contains("Yoshi")) {
                            holder.icon_img.setImageResource(R.drawable.yoshi);
                        } else if (documentSnapshot.getString("icon").contains("Purshi")) {
                            holder.icon_img.setImageResource(R.drawable.purple_yoshi);
                        } else if (documentSnapshot.getString("icon").contains("Broshi")) {
                            holder.icon_img.setImageResource(R.drawable.brown_yoshi);
                        } else if (documentSnapshot.getString("icon").contains("Boshi")) {
                            holder.icon_img.setImageResource(R.drawable.boshi_tm_cut);
                        } else {
                            holder.icon_img.setImageResource(R.drawable.ic_perfil);
                        }
                    }
                }
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView userName_tv;
        TextView category;
        TextView location;
        TextView description;
        TextView fecha;
        ImageView post;
        ImageView icon_img;
        View viewHolder;

        public ViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.eventTitel_tv);
            userName_tv = view.findViewById(R.id.userName_tv);
            category = view.findViewById(R.id.category_tv);
            location = view.findViewById(R.id.location);
            description = view.findViewById(R.id.desctiption_tv);
            fecha = view.findViewById(R.id.fecha_tv);
            post = view.findViewById(R.id.img);
            icon_img = view.findViewById(R.id.icon);
            viewHolder = view;

        }
    }
}
