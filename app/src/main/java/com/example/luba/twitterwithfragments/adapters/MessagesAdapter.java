package com.example.luba.twitterwithfragments.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.luba.twitterwithfragments.R;
import com.example.luba.twitterwithfragments.models.Message;
import com.example.luba.twitterwithfragments.models.User;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by luba on 10/6/17.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    public interface OnMessagesAdapterListener {
        void selectedUser(User user);
    }

    private ArrayList<Message> mMessages;
    private OnMessagesAdapterListener mListener;

    public MessagesAdapter(ArrayList<Message> messages, OnMessagesAdapterListener listener) {
        this.mMessages = messages;
        this.mListener = listener;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.configureViewWithMessage(mMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessages != null ? mMessages.size() : 0;
    }

    public void notifyDataSetChanged(ArrayList<Message> messages) {
        this.mMessages = new ArrayList<>(messages);
        notifyDataSetChanged();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        protected Message message;

        ImageView ivProfileImage;
        TextView tvName;
        TextView tvUser;
        TextView tvText;
        TextView tvDate;

        public MessageViewHolder(View itemView) {
            super(itemView);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.iv_profile_image);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvUser = (TextView) itemView.findViewById(R.id.tv_user);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvText = (TextView) itemView.findViewById(R.id.tv_text);


            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) mListener.selectedUser(message.getSender());
                }
            });
        }

        public void configureViewWithMessage(Message message) {
            this.message = message;

            Glide.with(ivProfileImage.getContext())
                    .load(message.getSender().getProfileImageUrl())
                    .placeholder(R.drawable.ic_twitter)
                    .bitmapTransform(new CropCircleTransformation(ivProfileImage.getContext()))
                    .into(ivProfileImage);

            tvName.setText(message.getSender().getName());
            tvUser.setText(message.getSender().getScreennameToShow());
            tvText.setText(message.getText());
            tvDate.setText(message.getFormattedCreatedAtDate());
        }
    }
}
