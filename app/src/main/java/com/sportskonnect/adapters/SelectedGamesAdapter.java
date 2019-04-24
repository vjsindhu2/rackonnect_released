package com.sportskonnect.adapters;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sportskonnect.R;
import com.sportskonnect.modal.SelectedGameModal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class SelectedGamesAdapter extends RecyclerView.Adapter<SelectedGamesAdapter.MyViewHolder> {

    private List<SelectedGameModal> selectedGameModalList = new ArrayList<>();

    private Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_gameName;
        public ImageView game_img;

        public MyViewHolder(View view) {
            super(view);
            tv_gameName = (TextView) view.findViewById(R.id.tv_gameName);
            game_img = (ImageView) view.findViewById(R.id.game_img);

        }
    }


    public SelectedGamesAdapter(Context context, List<SelectedGameModal> selectedGameModalList) {

        this.context = context;
        this.selectedGameModalList = selectedGameModalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selectedgamelist_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SelectedGameModal selectedGameModal = selectedGameModalList.get(position);

        if (selectedGameModal.getCatId()==1){


            holder.tv_gameName.setText("SQUASH");
            Picasso.get().load(R.drawable.squash2).into(holder.game_img);


        }else if (selectedGameModal.getCatId()==2){

            holder.tv_gameName.setText("TABLE TENNIS");
            Picasso.get().load(R.drawable.table_tennis).into(holder.game_img);


        }else if (selectedGameModal.getCatId()==3){

            holder.tv_gameName.setText("BADMINTON");
            Picasso.get().load(R.drawable.badminton1).into(holder.game_img);


        }else if (selectedGameModal.getCatId()==4){

            holder.tv_gameName.setText("TENNIS");
            Picasso.get().load(R.drawable.tennis2).into(holder.game_img);



        }else {
            holder.tv_gameName.setText("---");

        }

    }

    @Override
    public int getItemCount() {
        return selectedGameModalList.size();
    }
}