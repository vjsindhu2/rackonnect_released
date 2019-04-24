package com.sportskonnect.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sportskonnect.R;
import com.sportskonnect.modal.UserRankDatum;
import com.sportskonnect.sharedpref.SharedPreference;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class UserRankAdapter extends RecyclerView.Adapter<UserRankAdapter.MyViewHolder> {

    private List<UserRankDatum> userRankDatumList;
    private Context context;
    private SharedPreference sharedPreference;
    private int typeofcall = 0;
    private boolean llBad = false;

    public UserRankAdapter(Context context, List<UserRankDatum> userRankDatumList, int typeofcall) {
        this.context = context;
        this.userRankDatumList = userRankDatumList;
        this.typeofcall = typeofcall;
        sharedPreference = new SharedPreference(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ranklytrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UserRankDatum userRankDatum = userRankDatumList.get(position);

        Typeface face = Typeface.createFromAsset(context.getAssets(),
                "fonts/Calibre Black.otf");

        Typeface face1 = Typeface.createFromAsset(context.getAssets(), "fonts/" + "Calibre Bold.otf");

        Typeface face2 = Typeface.createFromAsset(context.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Semibold.otf");

        holder.tvgameName.setTypeface(face);

        holder.tvgameName.setText(userRankDatum.getCatname());

        holder.tvRate.setText(userRankDatum.getWin()+"");
        holder.tvRate.setTypeface(face1);


       /* if (userRankDatum.getCatid().equals("1")){

//            holder.ivP.setColorFilter(R.color.squash_color,android.graphics.PorterDuff.Mode.MULTIPLY);
            holder.ivP.setColorFilter(ContextCompat.getColor(context, R.color.squash_color), android.graphics.PorterDuff.Mode.SRC_IN);

            holder.tvRate.setTextColor(context.getResources().getColor(R.color.squash_color));
        }else if (userRankDatum.getCatid().equals("2")){

//            holder.ivP.setColorFilter(R.color.table_tenis_color,android.graphics.PorterDuff.Mode.MULTIPLY);
            holder.ivP.setColorFilter(ContextCompat.getColor(context, R.color.table_tenis_color), android.graphics.PorterDuff.Mode.SRC_IN);

            holder.tvRate.setTextColor(context.getResources().getColor(R.color.table_tenis_color));

        }else if (userRankDatum.getCatid().equals("3")){

//            holder.ivP.setColorFilter(R.color.bedmanten_color,android.graphics.PorterDuff.Mode.MULTIPLY);
            holder.ivP.setColorFilter(ContextCompat.getColor(context, R.color.bedmanten_color), android.graphics.PorterDuff.Mode.SRC_IN);

            holder.tvRate.setTextColor(context.getResources().getColor(R.color.bedmanten_color));

        } else if (userRankDatum.getCatid().equals("4")){

//            holder.ivP.setColorFilter(R.color.tenis_color,android.graphics.PorterDuff.Mode.MULTIPLY);

            holder.ivP.setColorFilter(ContextCompat.getColor(context, R.color.tenis_color), android.graphics.PorterDuff.Mode.SRC_IN);

            holder.tvRate.setTextColor(context.getResources().getColor(R.color.tenis_color));

        }*/

        if (!userRankDatum.getPer().equals("") && !userRankDatum.getPer().equals(null)) {
            holder.seekBar_luminosite.setProgress(userRankDatum.getPer());
        }
        holder.tv_total_lose.setText(userRankDatum.getLoss() + "");
        holder.tv_total_win.setText(userRankDatum.getWin() + "");
        holder.tv_totalgames.setText(userRankDatum.getGames() + "");
        holder.game_percent.setText(userRankDatum.getPer() + "%");
        if (!userRankDatum.getScore().equals("")){
            holder.scoreing_tv.setText(userRankDatum.getScore());

        }


        holder.llBadQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!llBad) {
                    holder.llBadAns.setVisibility(View.VISIBLE);
                    holder.ivBadTop.setVisibility(View.GONE);
                    holder.ivBadBottom.setVisibility(View.VISIBLE);
                    llBad = true;
                } else if (llBad) {

                    holder.llBadAns.setVisibility(View.GONE);
                    holder.ivBadTop.setVisibility(View.VISIBLE);
                    holder.ivBadBottom.setVisibility(View.GONE);
                    llBad = false;
                }

            }
        });


        holder.seekBar_luminosite.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return userRankDatumList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvgameName, tvRate, tv_totalgames, tv_total_win, tv_total_lose, game_percent,scoreing_tv;
        public ImageView ivP, ivBadTop, ivBadBottom;
        public RelativeLayout rlRight;
        public LinearLayout llBadAns, llBadQues;
        public SeekBar seekBar_luminosite;


        public MyViewHolder(View view) {
            super(view);
            tvgameName = (TextView) view.findViewById(R.id.tvgameName);
            tvRate = (TextView) view.findViewById(R.id.tvRate);
            tv_totalgames = (TextView) view.findViewById(R.id.tv_totalgames);
            tv_total_win = (TextView) view.findViewById(R.id.tv_total_win);
            tv_total_lose = (TextView) view.findViewById(R.id.tv_total_lose);
            game_percent = (TextView) view.findViewById(R.id.game_percent);
            scoreing_tv = (TextView) view.findViewById(R.id.scoreing_tv);

            ivP = (ImageView) view.findViewById(R.id.ivP);
            ivBadTop = (ImageView) view.findViewById(R.id.ivBadTop);
            ivBadBottom = (ImageView) view.findViewById(R.id.ivBadBottom);

            rlRight = (RelativeLayout) view.findViewById(R.id.rlRight);

            llBadAns = (LinearLayout) view.findViewById(R.id.llBadAns);
            llBadQues = (LinearLayout) view.findViewById(R.id.llBadQues);


            seekBar_luminosite = (SeekBar) view.findViewById(R.id.seekBar_luminosite);
        }
    }
}