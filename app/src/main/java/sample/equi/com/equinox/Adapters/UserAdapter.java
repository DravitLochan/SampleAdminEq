package sample.equi.com.equinox.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import sample.equi.com.equinox.Models.DB.UserProfileDbModel;
import sample.equi.com.equinox.R;

/**
 * Created by DravitLochan on 15-04-2018.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    Context context;
    ArrayList<UserProfileDbModel> users;

    public UserAdapter(Context context, ArrayList<UserProfileDbModel> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserProfileDbModel user = users.get(position);
        holder.name.setText(user.getTitle() + "." + " " + user.getF_name() + " " + user.getL_name());
        holder.email.setText(user.getEmail());
        holder.contact_number.setText(user.getPhone());
        if(user.getViewed()){
            holder.name.setTypeface(null, Typeface.NORMAL);
            holder.email.setTypeface(null, Typeface.NORMAL);
        }
        Uri uri = Uri.parse(user.getThumbnail());
        holder.thumbnail.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CardView container_user_profile;
        TextView name, email, contact_number, time_fetched;
        ImageView mark_favourite;
        SimpleDraweeView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            container_user_profile = itemView.findViewById(R.id.cv_user);
            name = itemView.findViewById(R.id.tv_user_name);
            email = itemView.findViewById(R.id.tv_user_email_id);
            contact_number = itemView.findViewById(R.id.tv_user_contact_number);
            time_fetched = itemView.findViewById(R.id.tv_time_fetched);
            mark_favourite = itemView.findViewById(R.id.iv_mark_favourite);
            thumbnail = itemView.findViewById(R.id.sdv_user_thumbnail);
        }

    }
}
