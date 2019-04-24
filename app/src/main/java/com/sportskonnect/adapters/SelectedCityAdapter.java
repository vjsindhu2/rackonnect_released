package com.sportskonnect.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sportskonnect.R;
import com.sportskonnect.api.Callbacks.Citynamearray;
import com.sportskonnect.modal.SelectedGameModal;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class SelectedCityAdapter extends RecyclerView.Adapter<SelectedCityAdapter.MyViewHolder> {

    private List<Citynamearray> selectedGameModalList = new ArrayList<>();

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


    public SelectedCityAdapter(Context context, List<Citynamearray> selectedGameModalList) {

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
        Citynamearray selectedGameModal = selectedGameModalList.get(position);

        holder.game_img.setVisibility(View.GONE);

        if (!selectedGameModal.getCityname().equals(""))
        {
            holder.tv_gameName.setText(selectedGameModal.getCityname());

        }


    }

    @Override
    public int getItemCount() {
        return selectedGameModalList.size();
    }
}