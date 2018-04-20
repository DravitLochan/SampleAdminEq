package sample.equi.com.equinox.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sample.equi.com.equinox.Animator.FlipAnimator;
import sample.equi.com.equinox.MainActivity;
import sample.equi.com.equinox.Models.DB.UserProfileDbModel;
import sample.equi.com.equinox.R;

/**
 * Created by DravitLochan on 15-04-2018.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    Context context;
    ArrayList<UserProfileDbModel> users;
    ActionMode actionMode;
    MainActivity.ActionModeCallback actionModeCallback;
    Map selectedUsers;

    public UserAdapter(Context context, ArrayList<UserProfileDbModel> users, ActionMode actionMode, MainActivity.ActionModeCallback actionModeCallback) {
        this.context = context;
        this.users = users;
        this.actionMode = actionMode;
        this.actionModeCallback = actionModeCallback;
        selectedUsers = new HashMap();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        UserProfileDbModel user = users.get(position);
        final long id = user.getId();
        holder.name.setText(user.getTitle() + "." + " " + user.getF_name() + " " + user.getL_name());
        holder.email.setText(user.getEmail());
        holder.contact_number.setText(user.getPhone());
        if(user.getViewed()){
            holder.name.setTypeface(null, Typeface.NORMAL);
            holder.email.setTypeface(null, Typeface.NORMAL);
        }
        Uri uri = Uri.parse(user.getThumbnail());
        holder.thumbnail.setImageURI(uri);
        if(user.getImportant())
            holder.mark_important.setImageResource(R.drawable.ic_star_yellow);
        else
            holder.mark_important.setImageResource(R.drawable.ic_star_border_grey_24dp);

        // Mark Important functionality
        holder.mark_important.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileDbModel tUser = UserProfileDbModel.findById(UserProfileDbModel.class, id);
                if(tUser.getImportant()){
                    holder.mark_important.setImageResource(R.drawable.ic_star_border_grey_24dp);
                    tUser.setImportant(false);
                    Toast.makeText(context, "Message marked un-important", Toast.LENGTH_LONG).show();
                    tUser.save();
                } else {
                    holder.mark_important.setImageResource(R.drawable.ic_star_yellow);
                    tUser.setImportant(true);
                    tUser.save();
                    Toast.makeText(context, "Message marked un-important", Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionModeClicked(position, holder);
            }
        });

        holder.container_user_profile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                actionModeClicked(position, holder);
                return true;
            }
        });

        if(selectedUsers.get(position) != null){
            holder.container_user_profile.setCardBackgroundColor(Color.parseColor("#c1c1c1"));
        }
        else{
            holder.container_user_profile.setCardBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    void actionModeClicked(int position, ViewHolder holder){
        Log.d("position", position + "");
        if(selectedUsers.get(position) == null){
            selectedUsers.put(position, true);
            Log.d("selected users size", selectedUsers.size() + "");
            holder.container_user_profile.setCardBackgroundColor(Color.parseColor("#c1c1c1"));
            FlipAnimator.flipView(context, holder.thumbnail, holder.doneThumbnail, false);
        } else {
            selectedUsers.remove(position);
            Log.d("selected users size", selectedUsers.size() + "");
            holder.container_user_profile.setCardBackgroundColor(Color.parseColor("#ffffff"));
            FlipAnimator.flipView(context, holder.doneThumbnail, holder.thumbnail, false);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CardView container_user_profile;
        TextView name, email, contact_number, time_fetched;
        ImageView mark_important, doneThumbnail;
        SimpleDraweeView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            container_user_profile = itemView.findViewById(R.id.cv_user);
            name = itemView.findViewById(R.id.tv_user_name);
            email = itemView.findViewById(R.id.tv_user_email_id);
            contact_number = itemView.findViewById(R.id.tv_user_contact_number);
            time_fetched = itemView.findViewById(R.id.tv_time_fetched);
            mark_important = itemView.findViewById(R.id.iv_mark_important);
            thumbnail = itemView.findViewById(R.id.sdv_user_thumbnail);
            doneThumbnail = itemView.findViewById(R.id.iv_done_thumbnail);
        }

    }
}
