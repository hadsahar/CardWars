package com.example.cardwars;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<String> mImageURL = new ArrayList<>();
    private ArrayList<Player_Winner> mWinner = new ArrayList<>();
    private Context mContext;
    private CallBack_top callBack_top;

    public RecyclerViewAdapter(){}
    
    public RecyclerViewAdapter(ArrayList<String> mImageURL, ArrayList<Player_Winner> mWinner, Context mContext) {
        this.mImageURL = mImageURL;
        this.mWinner = mWinner;
        this.mContext = mContext;
    }

    public void setCallBack_top(CallBack_top callBack_top) {
        this.callBack_top = callBack_top;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        return new ViewHolder(view);
    }

    // sets the view of each individual record in the List.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("pttt", "onBindViewHolder: called");
        Glide.with(mContext).asBitmap().load(mImageURL.get(position)).into(holder.image); // Set the picture of the rank
        holder.name.setText(mWinner.get(position).getWinner_name()); // Set the name of the highscorer
        holder.points.setText((CharSequence)(String.valueOf(mWinner.get(position).getMax_score()))); // set the points of the highscorer.
        Log.d("pttt", "onBindViewHolder: name: "+ mWinner.get(position).getWinner_name());
        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            // if clicked - send a callback to the activity in order to change the map's camera location to the clicked object.
            public void onClick(View v) {
                Log.d("pttt", "onClick: Clicked on " + (mWinner.get(position).getWinner_name()));

                callBack_top.displayLocationInMap(mWinner.get(position).getLat(),mWinner.get(position).getLon());
            }
        });
    }

    // sets the count of the items in the view as the minimum from the ArrayList highscorers and
    // the size of the pictures' URL ArrayList (10).
    @Override
    public int getItemCount() {
        return Math.min(mWinner.size(),mImageURL.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView points;
        TextView name;
        RelativeLayout parent_layout;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.listitem_img_Rank);
            points = itemView.findViewById(R.id.listitem_lbl_points);
            name = itemView.findViewById(R.id.listitem_lbl_name);
            parent_layout = itemView.findViewById(R.id.listitem_parent_layout);
        }
    }
}
