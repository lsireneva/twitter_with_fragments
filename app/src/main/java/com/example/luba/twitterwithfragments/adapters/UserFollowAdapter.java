package com.example.luba.twitterwithfragments.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.luba.twitterwithfragments.R;
import com.example.luba.twitterwithfragments.models.User;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by luba on 10/9/17.
 */

public class UserFollowAdapter extends RecyclerView.Adapter<UserFollowAdapter.UserViewHolder> {

    public interface OnUserFollowAdapterListener {
        void selectedUser(User user);
    }

    private ArrayList<User> mUsers;
    private OnUserFollowAdapterListener mListener;

    public UserFollowAdapter(ArrayList<User> users, OnUserFollowAdapterListener listener) {
        this.mUsers = users;
        this.mListener = listener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_follow, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.setupViewWithUser(mUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return mUsers != null ? mUsers.size() : 0;
    }

    public void notifyDataSetChanged(ArrayList<User> users) {
        this.mUsers = new ArrayList<>(users);
        notifyDataSetChanged();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        protected User user;

        ImageView ivProfileImage;
        TextView tvName, tvScreenname, tvDescription;

        public UserViewHolder(View itemView) {
            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.iv_profile_image);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvScreenname = (TextView) itemView.findViewById(R.id.tv_screenname);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) mListener.selectedUser(user);
                }
            });

            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) mListener.selectedUser(user);
                }
            });
        }

        public void setupViewWithUser(User user) {
            this.user = user;
            Glide.with(ivProfileImage.getContext())
                    .load(user.getProfileImageUrl())
                    .placeholder(R.drawable.ic_twitter)
                    .bitmapTransform(new RoundedCornersTransformation(ivProfileImage.getContext(), 3, 3))
                    .into(ivProfileImage);

            tvName.setText(user.getName());
            tvScreenname.setText(user.getScreennameToShow());
            tvDescription.setText(user.getDescription());
        }
    }
}
