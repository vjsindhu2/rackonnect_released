package com.sportskonnect.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.sportskonnect.Activity_TournamentChat;
import com.sportskonnect.R;
import com.sportskonnect.modal.OpponetsDatum;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;

public class SinglesRvAdapter extends RecyclerView.Adapter<SinglesRvAdapter.MyViewHolder> implements Filterable {

    private List<OpponetsDatum> opponetsDatumList;
    private List<OpponetsDatum> opponetsDatumListFiltered;
    private Context context;
    private SharedPreference sharedPreference;

    public SinglesRvAdapter() {
    }

    public SinglesRvAdapter(Context context, List<OpponetsDatum> opponetsDatumList) {
        this.context = context;
        this.opponetsDatumList = opponetsDatumList;
        this.opponetsDatumListFiltered = opponetsDatumList;
        sharedPreference = new SharedPreference(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.singles_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final OpponetsDatum opponetsDatum = opponetsDatumList.get(position);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Semibold.otf");

        Typeface face2 = Typeface.createFromAsset(context.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");

        holder.tv_opname.setTypeface(face2);

        String gender = SharedPreference.getSharedPreferenceString(context, SharedprefKeys.GENDER, "");

        if (opponetsDatum.getGender().equals("Male")) {
            Picasso.get().load(opponetsDatum.getImage()).placeholder(R.drawable.male).into(holder.profile_image);

        } else {
            Picasso.get().load(opponetsDatum.getImage()).placeholder(R.drawable.female).into(holder.profile_image);

        }

        holder.tv_opname.setText(opponetsDatum.getName());

        holder.tv_opname.setTypeface(face2);
        holder.points.setTypeface(face2);
        holder.tvAreaName.setTypeface(typeface);
        holder.lvl_.setTypeface(face2);
        holder.lvl_n_cat_name.setTypeface(face2);
        holder.tvChat.setTypeface(typeface);

        holder.points.setText(opponetsDatum.getPoints());

        holder.tvAreaName.setText(opponetsDatum.getAddress());

//        holder.points.setText(opponetsDatum.get);

        if (opponetsDatum.getStatusflag().equals("online")) {
            holder.status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_flash));
            holder.time_status.setVisibility(View.GONE);
        } else {
            holder.status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_flash_offline));
            holder.time_status.setVisibility(View.VISIBLE);
            holder.time_status.setText(opponetsDatum.getTime());

        }

        if (opponetsDatum.getCatid() == 1) {


            holder.lvl_n_cat_name.setText("SQUASH");
            Picasso.get().load(R.drawable.squash2).into(holder.game_img);


        } else if (opponetsDatum.getCatid() == 2) {

            holder.lvl_n_cat_name.setText("TABLE TENNIS");
            Picasso.get().load(R.drawable.table_tennis).into(holder.game_img);


        } else if (opponetsDatum.getCatid() == 3) {

            holder.lvl_n_cat_name.setText("BADMINTON");
            Picasso.get().load(R.drawable.badminton1).into(holder.game_img);


        } else if (opponetsDatum.getCatid() == 4) {

            holder.lvl_n_cat_name.setText("TENNIS");
            Picasso.get().load(R.drawable.tennis2).into(holder.game_img);


        } else {
            holder.lvl_n_cat_name.setText("---");

        }

        if (opponetsDatum.getLevelid() == 1) {

            holder.lvl_.setText("Amateur");

        } else if (opponetsDatum.getLevelid() == 2) {

            holder.lvl_.setText("Semi Pro");

        } else if (opponetsDatum.getLevelid() == 3) {

            holder.lvl_.setText("Pro");

        } else if (opponetsDatum.getLevelid() == 4) {

            holder.lvl_.setText("Expert");

        } else {
            holder.lvl_.setText("---");

        }


        holder.tvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Activity_TournamentChat.class);

                SharedPreference.setSharedPreferenceString(context, SharedprefKeys.OPPONENT_ID, opponetsDatum.getUserid());
                SharedPreference.setSharedPreferenceString(context, SharedprefKeys.OPPONENT_NAME, opponetsDatum.getName());
                SharedPreference.setSharedPreferenceString(context, SharedprefKeys.OPPONENT_LEVEL, String.valueOf(opponetsDatum.getLevelid()));
                SharedPreference.setSharedPreferenceString(context,SharedprefKeys.CHATCOMEFROM,"0");

                context.startActivity(intent);

                ((Activity)context).finish();

            }
        });


        if (position == 0) {


            if (!SharedPreference.getSharedPreferenceBoolean(context, SharedprefKeys.CHATSHOWCASE, false)) {


                new GuideView.Builder(context)
                        .setTitle("Chat")
                        .setContentText("RackTalk with your selected player and create a " +
                                "match.")
                        .setGravity(Gravity.auto) //optional
                        .setDismissType(DismissType.anywhere) //optional - default DismissType.targetView
                        .setTargetView(holder.tvChat)
                        .setContentTextSize(12)//optional
                        .setTitleTextSize(14)//optional
                        .build()
                        .show();

                SharedPreference.setSharedPreferenceBoolean(context, SharedprefKeys.CHATSHOWCASE, true);

            }
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
                    opponetsDatumListFiltered = opponetsDatumList;
                } else {
                    List<OpponetsDatum> filteredList = new ArrayList<>();
                    for (OpponetsDatum row : opponetsDatumList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {


                            filteredList.add(row);
                            Log.d("CHARSTRINGINIF", row.getName().toLowerCase() + "==" + charString.toLowerCase());

                        }
                    }

                    opponetsDatumListFiltered = filteredList;

                    Log.d("CITYNAME", filteredList.get(0).getName());

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = opponetsDatumListFiltered;


                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                opponetsDatumListFiltered = (ArrayList<OpponetsDatum>) filterResults.values;
//                opponetsDatumListFiltered = fil;

//                Log.d("CITYNAME_ACTUAL",opponetsDatumListFiltered.get(0).getName());

//                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return opponetsDatumListFiltered.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_opname, lvl_n_cat_name, tvAreaName, points, lvl_, tvChat, time_status;
        public CircleImageView profile_image;
        public ImageView game_img, status;

        public MyViewHolder(View view) {
            super(view);
            tv_opname = (TextView) view.findViewById(R.id.tv_opname);
            profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
            lvl_n_cat_name = (TextView) view.findViewById(R.id.cat_name);
            lvl_ = (TextView) view.findViewById(R.id.lvl_);
            tvAreaName = (TextView) view.findViewById(R.id.tvAreaName);
            points = (TextView) view.findViewById(R.id.points);
            time_status = (TextView) view.findViewById(R.id.time_status);
            tvChat = (TextView) view.findViewById(R.id.tvChat);
            game_img = (ImageView) view.findViewById(R.id.game_img);
            status = (ImageView) view.findViewById(R.id.status);
        }
    }
}