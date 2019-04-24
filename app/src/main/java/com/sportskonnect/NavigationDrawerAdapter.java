package com.sportskonnect;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by revinfotech on 12/19/2017.
 */

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
   // List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    public static int selected_item = 0;

    public NavigationDrawerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
//        this.data = data;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.navigation_recycle, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    //  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        holder.rlSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("settings","clicked");
            }
        });

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView,iv;
        RelativeLayout rlSetting;

        public MyViewHolder(View itemView) {
            super(itemView);
//            title = (TextView) itemView.findViewById(R.id.title);
//            imageView = (ImageView) itemView.findViewById(R.id.imageView3);
////            iv = (ImageView) itemView.findViewById(R.id.iv);
            rlSetting = (RelativeLayout) itemView.findViewById(R.id.rlSetting);
        }
    }
}