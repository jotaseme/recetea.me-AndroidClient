package es.upm.miw.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import me.gujun.android.taggroup.TagGroup;

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView titleTextView;
    public ImageView coverImageView;
    public TextView portionsTextView;
    public TextView durationTextView;
    public TagGroup mTagGroup;

    public MyViewHolder(View v) {
        super(v);
        titleTextView = (TextView) v.findViewById(R.id.titleTextView);
        coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
        mTagGroup = (TagGroup) v.findViewById(R.id.tag_group);
    }
}
