package com.sportskonnect.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sportskonnect.Activity_Badminton;
import com.sportskonnect.R;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.OpponentsResponse;
import com.sportskonnect.api.Constant;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.modal.DualMatchListDatum;
import com.sportskonnect.sharedpref.SharedPreference;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchListHistoryAdapter extends RecyclerView.Adapter<MatchListHistoryAdapter.MyViewHolder> {

    private List<DualMatchListDatum> matchDatumList;
    private Context context;
    private SharedPreference sharedPreference;
    private int typeofcall = 0;
    private PopupWindow profilePopupWindow;

    public MatchListHistoryAdapter(Context context, List<DualMatchListDatum> matchDatumList, int typeofcall) {
        this.context = context;
        this.matchDatumList = matchDatumList;
        this.typeofcall = typeofcall;
        sharedPreference = new SharedPreference(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.explore_history_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DualMatchListDatum matchDatum = matchDatumList.get(position);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Semibold.otf");

        Typeface face2 = Typeface.createFromAsset(context.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");


        holder.game_name_with_game.setText(matchDatum.getMatchname() + " - " + Constant.getGameNameFromcatId(matchDatum.getCatid()));
        holder.gametime.setText(matchDatum.getDate() + " | " + matchDatum.getStartTime() + " - " + matchDatum.getEndTime());
        holder.tvAreaName3.setText(matchDatum.getAddress());
        holder.tvV1.setText(matchDatum.getMidname());
        holder.mid_level.setText(Constant.getGameLevelFromlevelId(matchDatum.getMidlevel()));

        Picasso.get().load(Constant.getGameImageFromcatId(matchDatum.getCatid())).into(holder.game_img);

        /*if (matchDatum.getMatchtype().equals("1")) {
            holder.tvJoinMa.setVisibility(View.GONE);
        } else {
            holder.tvJoinMa.setVisibility(View.VISIBLE);

        }*/
        Picasso.get().load(matchDatum.getMidimage()).placeholder(R.drawable.male).into(holder.mid_img);


        if (!matchDatum.getMatchstatus().equals("")) {
            holder.tvJoinMa.setText(matchDatum.getMatchstatus());

            holder.tvJoinMa.setVisibility(View.VISIBLE);
        } else {
//            holder.tvJoinMa.setText(matchDatum.getMatchstatus());
            holder.tvJoinMa.setVisibility(View.GONE);

        }


        holder.fid_one_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!matchDatum.getFidarray().get(0).getFid().equals(""))
                    showUserProfilePopup(holder.fid_one_img, matchDatum.getFidarray().get(0).getFid(), matchDatum.getFidarray().get(0).getFidlevel());

            }
        });

        holder.fid_two_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!matchDatum.getFidarray().get(1).getFid().equals(""))

                    showUserProfilePopup(holder.fid_two_img, matchDatum.getFidarray().get(1).getFid(), matchDatum.getFidarray().get(1).getFidlevel());

            }
        });

        holder.fid_three_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!matchDatum.getFidarray().get(2).getFid().equals(""))
                    showUserProfilePopup(holder.fid_three_img, matchDatum.getFidarray().get(2).getFid(), matchDatum.getFidarray().get(2).getFidlevel());

            }
        });


        if (matchDatum.getFidarray().size() == 1) {

            if (!matchDatum.getFidarray().get(0).getFid().equals("")) {
                Picasso.get().load(matchDatum.getFidarray().get(0).getFidimage()).placeholder(R.drawable.male).into(holder.fid_one_img);

                holder.fone_name.setText(getName(matchDatum.getFidarray().get(0).getFidname()));
            }

            holder.player_counts.setText("PLAYERS(1)");

            holder.fid_two_img.setVisibility(View.GONE);
            holder.fid_three_img.setVisibility(View.GONE);
            holder.ftwo_name.setVisibility(View.GONE);
            holder.fthree_name.setVisibility(View.GONE);
            holder.fone_name.setVisibility(View.VISIBLE);

        } else {

            if (!matchDatum.getFidarray().get(0).getFid().equals("")) {
                Picasso.get().load(matchDatum.getFidarray().get(0).getFidimage()).placeholder(R.drawable.male).into(holder.fid_one_img);

                holder.fone_name.setText(getName(matchDatum.getFidarray().get(0).getFidname()));

                holder.fone_name.setVisibility(View.VISIBLE);

            }else {
                holder.fone_name.setVisibility(View.GONE);

            }

            if (!matchDatum.getFidarray().get(1).getFid().equals("")) {
                Picasso.get().load(matchDatum.getFidarray().get(1).getFidimage()).placeholder(R.drawable.male).into(holder.fid_two_img);
                holder.ftwo_name.setText(getName(matchDatum.getFidarray().get(1).getFidname()));
                holder.ftwo_name.setVisibility(View.VISIBLE);

            }else {
                holder.ftwo_name.setVisibility(View.GONE);

            }
            if (!matchDatum.getFidarray().get(2).getFid().equals("")) {
                Picasso.get().load(matchDatum.getFidarray().get(2).getFidimage()).placeholder(R.drawable.male).into(holder.fid_three_img);
                holder.fthree_name.setText(getName(matchDatum.getFidarray().get(2).getFidname()));
                holder.fthree_name.setVisibility(View.VISIBLE);

            } else {
                holder.fthree_name.setVisibility(View.GONE);
            }

            holder.fid_two_img.setVisibility(View.VISIBLE);
            holder.fid_three_img.setVisibility(View.VISIBLE);
            holder.player_counts.setText("PLAYERS(3)");

        }


    }

    @Override
    public int getItemCount() {


        if (typeofcall == 1) {
            return matchDatumList.size();
        } else {
            if (matchDatumList.size() > 2) {
                return 2;
            } else {

                return matchDatumList.size();

            }

        }
    }

    public void showUserProfilePopup(View v, String id, String level_id) {


        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.dialog_users_profile_dialog, null);


        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Semibold.otf");

        Typeface face2 = Typeface.createFromAsset(context.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");


        ImageView oppImg = (ImageView) popupView.findViewById(R.id.opp_image);
        ImageView imageView5 = (ImageView) popupView.findViewById(R.id.imageView5);
        TextView tvusername = (TextView) popupView.findViewById(R.id.tvusername);
        TextView tvAge = (TextView) popupView.findViewById(R.id.tvAge);
        TextView points = (TextView) popupView.findViewById(R.id.points);
        TextView tvLevel = (TextView) popupView.findViewById(R.id.tvLevel);


        tvLevel.setText(Constant.getGameLevelFromlevelId(level_id));

        showuserDetail(id, oppImg, tvusername, tvAge, points);


        profilePopupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true

        );

//        profilePopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        profilePopupWindow.setBackgroundDrawable(new BitmapDrawable());
        profilePopupWindow.setOutsideTouchable(true);
        profilePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {


            }
        });

        profilePopupWindow.showAsDropDown(v);
    }

    private void showuserDetail(String user_id, ImageView oopimg, TextView oppname, TextView age, TextView points) {

//        loader_dialog(context);
        Activity_Badminton.swipreferesh.setRefreshing(true);
        Apis apis = RestAdapter.createAPI();
        Call<OpponentsResponse> callbackCall = apis.getopponetinfo(user_id);
        callbackCall.enqueue(new Callback<OpponentsResponse>() {
            @Override
            public void onResponse(Call<OpponentsResponse> call, Response<OpponentsResponse> response) {
                OpponentsResponse resp = response.body();
                if (resp != null && !resp.getError()) {

                    Activity_Badminton.swipreferesh.setRefreshing(false);

//                    loader_dialog.dismiss();
                    try {


                        if (resp.getGender().equals("Male")) {

                            Picasso.get().load(resp.getImage()).placeholder(R.drawable.male).into(oopimg);

                        } else {
                            Picasso.get().load(resp.getImage()).placeholder(R.drawable.female).into(oopimg);

                        }

                        oppname.setText(resp.getName());

                        age.setText(resp.getAge() + " Y");
                        points.setText(resp.getPoints());

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


//                    dialogServerNotConnect();
                } else {
                    Activity_Badminton.swipreferesh.setRefreshing(false);

//                    loader_dialog.dismiss();

//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<OpponentsResponse> call, Throwable t) {
                Activity_Badminton.swipreferesh.setRefreshing(false);

//                loader_dialog.dismiss();
                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }

    public String getName(String fullName) {
        return fullName.split(" (?!.* )")[0];
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView game_name_with_game, gametime, tvAreaName3, tvV1, mid_level, player_counts, fone_name, ftwo_name, fthree_name;

        public ImageView game_img;
        public CircleImageView mid_img, fid_one_img, fid_two_img, fid_three_img;
        public LinearLayout matchdetail_ll;

        public TextView tvJoinMa;

        public MyViewHolder(View view) {
            super(view);
            game_name_with_game = (TextView) view.findViewById(R.id.game_name_with_game);
            player_counts = (TextView) view.findViewById(R.id.player_counts);
            gametime = (TextView) view.findViewById(R.id.gametime);
            fone_name = (TextView) view.findViewById(R.id.fone_name);
            ftwo_name = (TextView) view.findViewById(R.id.ftwo_name);
            fthree_name = (TextView) view.findViewById(R.id.fthree_name);

            mid_img = (CircleImageView) view.findViewById(R.id.mid_img);

            fid_one_img = (CircleImageView) view.findViewById(R.id.fid_one_img);
            fid_three_img = (CircleImageView) view.findViewById(R.id.fid_three_img);
            fid_two_img = (CircleImageView) view.findViewById(R.id.fid_two_img);

            tvAreaName3 = (TextView) view.findViewById(R.id.tvAreaName3);
            tvV1 = (TextView) view.findViewById(R.id.tvV1);

            mid_level = (TextView) view.findViewById(R.id.mid_level);
            tvJoinMa = (TextView) view.findViewById(R.id.tvJoinMa);

            game_img = (ImageView) view.findViewById(R.id.game_img);
            matchdetail_ll = (LinearLayout) view.findViewById(R.id.matchdetail_ll);


        }
    }
}