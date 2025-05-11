package com.example.tunisialive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    public interface OnCommentClickListener {
        void onCommentClick(int position);
        void onProfileClick(int position);
    }

    private final Context context;
    private List<Comment> commentList;
    private OnCommentClickListener listener;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    public void setOnCommentClickListener(OnCommentClickListener listener) {
        this.listener = listener;
    }

    public void updateComments(List<Comment> newComments) {
        this.commentList = newComments;
        notifyDataSetChanged();
    }

    public void addComment(Comment comment) {
        this.commentList.add(0, comment);
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        // Set username and comment text
        holder.textUsername.setText(comment.getUsername() != null ?
                comment.getUsername() : context.getString(R.string.default_anonymous_name));
        holder.textComment.setText(comment.getComment());

        // Format timestamp
        if (comment.getTimestamp() > 0) {
            holder.textTimestamp.setText(formatTimestamp(comment.getTimestamp()));
            holder.textTimestamp.setVisibility(View.VISIBLE);
        } else {
            holder.textTimestamp.setVisibility(View.GONE);
        }

        // Load profile image
        RequestOptions requestOptions = new RequestOptions()
                .transform(new CircleCrop())
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user);

        Glide.with(context)
                .load(comment.getProfileImageUrl())
                .apply(requestOptions)
                .into(holder.imageProfile);
    }

    private String formatTimestamp(long timestamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
            return sdf.format(new Date(timestamp));
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public int getItemCount() {
        return commentList != null ? commentList.size() : 0;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProfile;
        TextView textUsername, textComment, textTimestamp;

        public CommentViewHolder(@NonNull View itemView, OnCommentClickListener listener) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            textUsername = itemView.findViewById(R.id.textUsername);
            textComment = itemView.findViewById(R.id.textComment);
            textTimestamp = itemView.findViewById(R.id.textTimestamp);

            // Set click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null && getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onCommentClick(getBindingAdapterPosition());
                }
            });

            imageProfile.setOnClickListener(v -> {
                if (listener != null && getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onProfileClick(getBindingAdapterPosition());
                }
            });
        }
    }
}