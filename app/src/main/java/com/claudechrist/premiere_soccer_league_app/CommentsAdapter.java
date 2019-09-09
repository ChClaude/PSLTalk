package com.claudechrist.premiere_soccer_league_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    static ArrayList<MatchComment> matchComments;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildListener;
    MatchComment mcComment;

    public CommentsAdapter() {
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        matchComments = new ArrayList<>();

        mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MatchComment mc = dataSnapshot.getValue(MatchComment.class);
                if (mc != null) {
                    mc.setId(dataSnapshot.getKey());
                }
                matchComments.add(mc);
                notifyItemInserted(matchComments.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (mcComment != null) {
                    int index = matchComments.indexOf(mcComment);
                    Log.i("TAG", index + " has been removed at that index");
                    matchComments.remove(index);
                    notifyItemRemoved(index);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseReference.addChildEventListener(mChildListener);
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.comments_layout, viewGroup, false);
        return new CommentsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder commentsViewHolder, int i) {
        MatchComment matchComment = matchComments.get(i);
        commentsViewHolder.bind(matchComment);
    }

    @Override
    public int getItemCount() {
        return matchComments.size();
    }

    // view holder class
    public class CommentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView commentTextView;
        ImageView deleteImageView;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            commentTextView = itemView.findViewById(R.id.comment_TextView);
            deleteImageView = itemView.findViewById(R.id.delete_imageView);
            deleteImageView.setOnClickListener(this);
        }

        public void bind(MatchComment matchComment) {
            commentTextView.setText(matchComment.getComment());
            mcComment = matchComment;
        }

        @Override
        public void onClick(View v) {
            Log.i("TAG", "Delete comment icon " + mcComment.getMatch().getLabel());
            mDatabaseReference.child(mcComment.getId()).removeValue();
        }
    }


}
