package com.sportskonnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sportskonnect.Activity_JoinTour;
import com.sportskonnect.R;
import com.sportskonnect.api.Constant;
import com.sportskonnect.modal.TourDatum;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class TourMatchHistoryAdapter extends RecyclerView.Adapter<TourMatchHistoryAdapter.MyViewHolder> {

    private List<TourDatum> tourDatumList;
    private Context context;

    public TourMatchHistoryAdapter(Context context, List<TourDatum> tourDatumList) {
        this.context = context;
        this.tourDatumList = tourDatumList;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tour_matchhistory_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final TourDatum tourDatum = tourDatumList.get(position);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Semibold.otf");

        Typeface face2 = Typeface.createFromAsset(context.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");


//        if (tourDatum.getMid().equals(SharedPreference.getSharedPreferenceString(context, SharedprefKeys.USER_ID, ""))) {
//            holder.tvJoinMa.setVisibility(View.GONE);
//        } else {
//            holder.tvJoinMa.setVisibility(View.VISIBLE);
//        }


        int value =0;
        for (int i=0;i<tourDatum.getFidarray().size();i++){

            if (tourDatum.getFidarray().get(i).getFid().equals(SharedPreference.getSharedPreferenceString(context, SharedprefKeys.USER_ID, "")))
            {
                value=1;

            }
        }

        if (value==1)
        {
//            holder.tvJoinMa.setVisibility(View.GONE);

        }else {
//            holder.tvJoinMa.setVisibility(View.VISIBLE);

        }


        try {


            if (tourDatum.getGender().equals("Male")){
                Picasso.get().load(tourDatum.getMidimage()).placeholder(R.drawable.male).into(holder.host_img);

            }else {
                Picasso.get().load(tourDatum.getMidimage()).placeholder(R.drawable.female).into(holder.host_img);

            }



            Picasso.get().load(Constant.getGameImageFromcatId(tourDatum.getCatid())).placeholder(R.drawable.male).into(holder.game_img);

            holder.game_name_n_players_tv.setText(Constant.getGameNameFromcatId(tourDatum.getCatid()) + " // " + tourDatum.getGroupMember() + " PLAYERS");

            holder.tourName_tv.setText(tourDatum.getMatchname());

            holder.dateNtime_tv.setText(tourDatum.getDate() + " | " + Constant.convet24hourTo12hour(tourDatum.getTime()) + " ONWARDS");

            holder.tvAreaName3.setText(tourDatum.getAddress() + "");

            holder.tv_host_name.setText(tourDatum.getMidname());
            holder.tv_host_level.setText(Constant.getGameLevelFromlevelId(tourDatum.getMidlevel()));

            holder.regfee_vt.setText("Rs. "+tourDatum.getAmount());


            holder.tvstatus.setText(tourDatum.getMatchstatus());
        } catch (Exception ex) {
            ex.printStackTrace();
        }





    }

    @Override
    public int getItemCount() {
        return tourDatumList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView game_name_n_players_tv, regfee_vt, tourName_tv, dateNtime_tv, tvAreaName3, tv_host_name;
        public ImageView game_img;
        public CircleImageView host_img;
        public TextView tvJoinMa;
        TextView tv_host_level;
        LinearLayout basicinfo_ll;
        TextView tvstatus;

        public MyViewHolder(View view) {
            super(view);
            game_name_n_players_tv = (TextView) view.findViewById(R.id.game_name_n_players_tv);
            tv_host_name = (TextView) view.findViewById(R.id.tv_host_name);
            tv_host_level = (TextView) view.findViewById(R.id.tv_host_level);
            regfee_vt = (TextView) view.findViewById(R.id.regfee_vt);
            tvstatus = (TextView) view.findViewById(R.id.tvstatus);

            host_img = (CircleImageView) view.findViewById(R.id.host_img);


            tourName_tv = (TextView) view.findViewById(R.id.tourName_tv);
            dateNtime_tv = (TextView) view.findViewById(R.id.dateNtime_tv);

            tvAreaName3 = (TextView) view.findViewById(R.id.tvAreaName3);
            tvJoinMa = (TextView) view.findViewById(R.id.tvJoinMa);

            game_img = (ImageView) view.findViewById(R.id.game_img);
            basicinfo_ll = (LinearLayout) view.findViewById(R.id.basicinfo_ll);


        }
    }
}