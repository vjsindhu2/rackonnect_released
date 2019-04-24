package com.sportskonnect.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sportskonnect.Activity_TournamentChat;
import com.sportskonnect.R;
import com.sportskonnect.activities.LeaderBoard;
import com.sportskonnect.api.Constant;
import com.sportskonnect.modal.GlobalRankDatum;
import com.sportskonnect.modal.OpponetsDatum;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderBoardRvAdapter extends RecyclerView.Adapter<LeaderBoardRvAdapter.MyViewHolder> implements Filterable {

    private List<GlobalRankDatum> globalRankData;
    private List<GlobalRankDatum> globalRankDataListFiltered;
    private Context context;
    private SharedPreference sharedPreference;

    int mSelectedItem = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView winner_name_tv, level, tv_totalgames, tv_total_win, tv_total_lose, game_percent, scoreing_tv, tvRate;
        public CircleImageView winner_img;
        public CardView card_cv;

        public LinearLayout parent_leader_ll;
        public SeekBar seekBar_luminosite;

        public ImageView ivP;

        public MyViewHolder(View view) {
            super(view);
            winner_name_tv = (TextView) view.findViewById(R.id.winner_name_tv);


            winner_img = (CircleImageView) view.findViewById(R.id.winner_img);
            level = (TextView) view.findViewById(R.id.level);
            card_cv = (CardView) view.findViewById(R.id.card_cv);
            ivP = (ImageView) view.findViewById(R.id.ivP);


            tv_total_lose = (TextView) view.findViewById(R.id.tv_total_lose);
            tvRate = (TextView) view.findViewById(R.id.tvRate);
            seekBar_luminosite = (SeekBar) view.findViewById(R.id.seekBar_luminosite);
            tv_totalgames = (TextView) view.findViewById(R.id.tv_totalgames);
            tv_total_win = (TextView) view.findViewById(R.id.tv_total_win);
            scoreing_tv = (TextView) view.findViewById(R.id.scoreing_tv);
            game_percent = (TextView) view.findViewById(R.id.game_percent);
            parent_leader_ll = (LinearLayout) view.findViewById(R.id.parent_leader_ll);


//            mSelectedItem = getAdapterPosition();


//            notifyDataSetChanged();


        }


    }

    public LeaderBoardRvAdapter() {
    }

    public LeaderBoardRvAdapter(Context context, List<GlobalRankDatum> globalRankDatumList) {
        this.context = context;
        this.globalRankData = globalRankDatumList;
        this.globalRankDataListFiltered = globalRankDatumList;
        sharedPreference = new SharedPreference(context);



for (int i=0;i<globalRankDatumList.size();i++){
    if (SharedPreference.getSharedPreferenceString(context,SharedprefKeys.USER_ID,"").equals(globalRankDatumList.get(i).getUserid())){

        int position_str = i+1;
        LeaderBoard.tvRate.setText(position_str+"");

        if (position_str==1){
            LeaderBoard.ivP.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_4));

            LeaderBoard.tvRate.setTextColor(context.getResources().getColor(R.color.cup_one_color));

        }else if (position_str==2){
            LeaderBoard.ivP.setImageDrawable(context.getResources().getDrawable(R.drawable.win_2));
            LeaderBoard.tvRate.setTextColor(context.getResources().getColor(R.color.cup_two_color));

        }else if (position_str==3){
            LeaderBoard.ivP.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_three_cup));
            LeaderBoard.tvRate.setTextColor(context.getResources().getColor(R.color.cup_three_color));

        }

    }


}

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboard_row_first, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final GlobalRankDatum globalRankDatum = globalRankData.get(position);


        Log.d("POSITION", holder.getLayoutPosition() + "");
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Semibold.otf");

        Typeface face2 = Typeface.createFromAsset(context.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");

        String gender = SharedPreference.getSharedPreferenceString(context, SharedprefKeys.GENDER, "");

        if (globalRankDatum.getGender_opp().equals("Male")) {
            Picasso.get().load(globalRankDatum.getProfileimage()).placeholder(R.drawable.male).into(holder.winner_img);

        } else {
            Picasso.get().load(globalRankDatum.getProfileimage()).placeholder(R.drawable.female).into(holder.winner_img);

        }

        holder.seekBar_luminosite.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });


        holder.winner_name_tv.setText(globalRankDatum.getName());

        holder.winner_name_tv.setTypeface(face2);
        holder.level.setTypeface(face2);
        holder.level.setText(Constant.getGameLevelFromlevelId(globalRankDatum.getLevel()));
        holder.tv_total_win.setText("" + globalRankDatum.getWin());
        holder.tv_total_lose.setText("" + globalRankDatum.getLoss());
        holder.tv_totalgames.setText("" + globalRankDatum.getGames());
        holder.game_percent.setText("" + globalRankDatum.getPer() + "%");
        holder.seekBar_luminosite.setProgress(globalRankDatum.getPer());
        holder.scoreing_tv.setText("" + globalRankDatum.getScoretotal());

        int serialno = position+1;

        holder.tvRate.setText(serialno+"");

        if (position % 2 != 0) {

            holder.card_cv.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

            holder.card_cv.setRadius(4);
            holder.card_cv.setCardElevation(4);
            holder.card_cv.setUseCompatPadding(true);
            holder.game_percent.setTextColor(context.getResources().getColor(R.color.white));

            holder.tv_totalgames.setTextColor(context.getResources().getColor(R.color.white));
            holder.tv_totalgames.setTextColor(context.getResources().getColor(R.color.white));
            holder.tv_total_lose.setTextColor(context.getResources().getColor(R.color.white));
            holder.tv_total_win.setTextColor(context.getResources().getColor(R.color.white));
            holder.winner_name_tv.setTextColor(context.getResources().getColor(R.color.white));
            holder.level.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvRate.setTextColor(context.getResources().getColor(R.color.white));
            holder.seekBar_luminosite.setProgressDrawable(context.getResources().getDrawable(R.drawable.seekbar_style_two));

            holder.ivP.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_white_cup));


        }


        if (position == 0) {


            holder.parent_leader_ll.setBackground(context.getResources().getDrawable(R.drawable.backleader));


        }


        if (position == 0) {
            holder.ivP.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_4));
            holder.tvRate.setTextColor(context.getResources().getColor(R.color.cup_one_color));

        } else if (position == 1) {
            holder.ivP.setImageDrawable(context.getResources().getDrawable(R.drawable.win_2));
            holder.tvRate.setTextColor(context.getResources().getColor(R.color.cup_two_color));

        } else if (position == 2) {
            holder.ivP.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_three_cup));
            holder.tvRate.setTextColor(context.getResources().getColor(R.color.cup_three_color));

        } else if (position == 3) {
            holder.ivP.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_white_cup));
            holder.tvRate.setTextColor(context.getResources().getColor(R.color.cup_four_color));

        } else if (position == 4) {
            holder.ivP.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_3));
            holder.tvRate.setTextColor(context.getResources().getColor(R.color.cup_five_color));

        }






    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
//                Log.d("CHARSTRING",charString);
                if (charString.isEmpty()) {
                    globalRankDataListFiltered = globalRankData;
                } else {
                    List<GlobalRankDatum> filteredList = new ArrayList<>();
                    for (GlobalRankDatum row : globalRankData) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {


                            filteredList.add(row);
                            Log.d("CHARSTRINGINIF", row.getName().toLowerCase() + "==" + charString.toLowerCase());

                        }
                    }

                    globalRankDataListFiltered = filteredList;

                    Log.d("CITYNAME", filteredList.get(0).getName());

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = globalRankDataListFiltered;


                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                globalRankDataListFiltered = (ArrayList<GlobalRankDatum>) filterResults.values;
//                opponetsDatumListFiltered = fil;

//                Log.d("CITYNAME_ACTUAL",opponetsDatumListFiltered.get(0).getName());

//                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return globalRankData.size();
    }




    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}