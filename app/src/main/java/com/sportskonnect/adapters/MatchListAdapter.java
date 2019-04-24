package com.sportskonnect.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jorgecastillo.FillableLoader;
import com.sportskonnect.Activity_ChatDoubles;
import com.sportskonnect.Activity_MatchScoringReq;
import com.sportskonnect.Activity_StartScoring;
import com.sportskonnect.R;
import com.sportskonnect.activities.ExploreMatchesActivity;
import com.sportskonnect.activities.ScoringRequestsActivity;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.OpponentsResponse;
import com.sportskonnect.api.Constant;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.modal.DualMatchListDatum;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sportskonnect.Paths.MODAL;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.MyViewHolder> {

    private List<DualMatchListDatum> matchDatumList;
    private Context context;
    private SharedPreference sharedPreference;
    private int typeofcall = 0;
    private Dialog loader_dialog;
    private PopupWindow profilePopupWindow;

    public MatchListAdapter(Context context, List<DualMatchListDatum> matchDatumList, int typeofcall) {
        this.context = context;
        this.matchDatumList = matchDatumList;
        this.typeofcall = typeofcall;
        sharedPreference = new SharedPreference(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doubles_match_row_explore, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DualMatchListDatum matchDatum = matchDatumList.get(position);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Semibold.otf");

        Typeface face2 = Typeface.createFromAsset(context.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");


        holder.game_name_with_game.setText(matchDatum.getMatchname() + " - " + Constant.getGameNameFromcatId(matchDatum.getCatid()));
        holder.gametime.setText(matchDatum.getDate() + " | " + Constant.convet24hourTo12hour(matchDatum.getStartTime()) + " - " + Constant.convet24hourTo12hour(matchDatum.getEndTime()));
        holder.tvAreaName3.setText(matchDatum.getAddress());
        holder.tvV1.setText(matchDatum.getMidname());
        holder.mid_level.setText(Constant.getGameLevelFromlevelId(matchDatum.getMidlevel()));


        if (matchDatum.getMatchtype().equals("1")) {
            holder.tvJoinMa.setVisibility(View.GONE);
        } else {
            holder.tvJoinMa.setVisibility(View.VISIBLE);

        }


        if (matchDatum.getGender().equals("Male")) {
            Picasso.get().load(matchDatum.getMidimage()).placeholder(R.drawable.male).into(holder.mid_img);

        } else {
            Picasso.get().load(matchDatum.getMidimage()).placeholder(R.drawable.female).into(holder.mid_img);
        }


        holder.matchdetail_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (typeofcall == 0) {

                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_CAT_ID, matchDatum.getCatid());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_USER_ID, matchDatum.getMid());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_USER_NAME, matchDatum.getMidname());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_DATE, matchDatum.getDate());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_DATE, matchDatum.getDate());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_START_TIME, matchDatum.getStartTime());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_END_TIME, matchDatum.getEndTime());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_END_TIME, matchDatum.getEndTime());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_ID, matchDatum.getMatchid());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_USER_LEVEL, matchDatum.getMidlevel());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_USER_IMAGE, matchDatum.getMidimage());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.MATCH_MATCHTYPEOF_GAME, matchDatum.getMatchtype());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_GROUP_ID, matchDatum.getGroupid());


                    if (matchDatum.getFidarray().size() == 1) {
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_ID, matchDatum.getFidarray().get(0).getFid());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME, matchDatum.getFidarray().get(0).getFidname());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL, matchDatum.getFidarray().get(0).getFidlevel());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE, matchDatum.getFidarray().get(0).getFidimage());

                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_GENDER, matchDatum.getFidarray().get(0).getGender());

                    } else {

                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_ID, matchDatum.getFidarray().get(0).getFid());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME, matchDatum.getFidarray().get(0).getFidname());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL, matchDatum.getFidarray().get(0).getFidlevel());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE, matchDatum.getFidarray().get(0).getFidimage());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_MEMBERID, matchDatum.getFidarray().get(0).getMemberid());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_GENDER, matchDatum.getFidarray().get(0).getGender());


                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_ID_TWO, matchDatum.getFidarray().get(1).getFid());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME_TWO, matchDatum.getFidarray().get(1).getFidname());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL_TWO, matchDatum.getFidarray().get(1).getFidlevel());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE_TWO, matchDatum.getFidarray().get(1).getFidimage());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_MEMBERID_TWO, matchDatum.getFidarray().get(1).getMemberid());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_GENDER_TWO, matchDatum.getFidarray().get(0).getGender());


                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_ID_THREE, matchDatum.getFidarray().get(2).getFid());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME_THREE, matchDatum.getFidarray().get(2).getFidname());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL_THREE, matchDatum.getFidarray().get(2).getFidlevel());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE_THREE, matchDatum.getFidarray().get(2).getFidimage());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_MEMBERID_THREE, matchDatum.getFidarray().get(2).getMemberid());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_GENDER_THREE, matchDatum.getFidarray().get(0).getGender());

                    }


                    if (matchDatum.getMid().equals(SharedPreference.getSharedPreferenceString(context, SharedprefKeys.USER_ID, ""))) {
                        if (matchDatum.getStatus().equals("0")) {

                            Intent intent = new Intent(context, Activity_StartScoring.class);
                            context.startActivity(intent);

                        } else {


                        }

                    }
                } else {

                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_CAT_ID, matchDatum.getCatid());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_USER_ID, matchDatum.getMid());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_USER_NAME, matchDatum.getMidname());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_DATE, matchDatum.getDate());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_DATE, matchDatum.getDate());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_START_TIME, matchDatum.getStartTime());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_END_TIME, matchDatum.getEndTime());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_END_TIME, matchDatum.getEndTime());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_ID, matchDatum.getMatchid());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_USER_LEVEL, matchDatum.getMidlevel());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_USER_IMAGE, matchDatum.getMidimage());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_USER_POINTS, matchDatum.getMidpoint());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_POINTS, matchDatum.getFidpoint());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_VALIDATE_TIME, matchDatum.getValidatetime());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_STATUS, matchDatum.getStatus());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.MATCH_MATCHTYPEOF_GAME, matchDatum.getMatchtype());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_TYPE, matchDatum.getType());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_GROUP_ID, matchDatum.getGroupid());


                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_M_ID, matchDatum.getMid());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_M_ID_LEVEL, matchDatum.getMidlevel());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_M_GENDER, matchDatum.getGender());


                    if (matchDatum.getFidarray().size() == 1) {
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_ID, matchDatum.getFidarray().get(0).getFid());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME, matchDatum.getFidarray().get(0).getFidname());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL, matchDatum.getFidarray().get(0).getFidlevel());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE, matchDatum.getFidarray().get(0).getFidimage());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_VERIFY, matchDatum.getFidarray().get(0).getVerify());

                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_GENDER, matchDatum.getFidarray().get(0).getGender());


                    } else {

                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_ID, matchDatum.getFidarray().get(0).getFid());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME, matchDatum.getFidarray().get(0).getFidname());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL, matchDatum.getFidarray().get(0).getFidlevel());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE, matchDatum.getFidarray().get(0).getFidimage());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_VERIFY, matchDatum.getFidarray().get(0).getVerify());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_MEMBERID, matchDatum.getFidarray().get(0).getMemberid());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_GENDER, matchDatum.getFidarray().get(0).getGender());


                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_ID_TWO, matchDatum.getFidarray().get(1).getFid());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME_TWO, matchDatum.getFidarray().get(1).getFidname());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL_TWO, matchDatum.getFidarray().get(1).getFidlevel());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE_TWO, matchDatum.getFidarray().get(1).getFidimage());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_VERIFY_TWO, matchDatum.getFidarray().get(1).getVerify());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_MEMBERID_TWO, matchDatum.getFidarray().get(1).getMemberid());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_GENDER_TWO, matchDatum.getFidarray().get(1).getGender());


                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_ID_THREE, matchDatum.getFidarray().get(2).getFid());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME_THREE, matchDatum.getFidarray().get(2).getFidname());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL_THREE, matchDatum.getFidarray().get(2).getFidlevel());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE_THREE, matchDatum.getFidarray().get(2).getFidimage());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_VERIFY_THREE, matchDatum.getFidarray().get(2).getVerify());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_MEMBERID_THREE, matchDatum.getFidarray().get(2).getMemberid());
                        SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_GENDER_THREE, matchDatum.getFidarray().get(2).getGender());

                    }


                    if (!matchDatum.getMidpoint().equals("") && !matchDatum.getFidpoint().equals("")) {
                        Intent intent = new Intent(context, Activity_MatchScoringReq.class);
                        context.startActivity(intent);

                    } else {
                        Toast.makeText(context, "Score not Updated till now, please wait...", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        holder.tvJoinMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_GROUP_ID, matchDatum.getGroupid());
                SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_USER_IMAGE, matchDatum.getMidimage());
                SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_NAME, matchDatum.getMatchname());

                SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_M_ID, matchDatum.getMid());
                SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_M_ID_LEVEL, matchDatum.getMidlevel());
                SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_M_GENDER, matchDatum.getGender());


                if (matchDatum.getFidarray().size() == 1) {
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_ID, matchDatum.getFidarray().get(0).getFid());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME, matchDatum.getFidarray().get(0).getFidname());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL, matchDatum.getFidarray().get(0).getFidlevel());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE, matchDatum.getFidarray().get(0).getFidimage());

                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_GENDER, matchDatum.getFidarray().get(0).getGender());

                } else {

                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_ID, matchDatum.getFidarray().get(0).getFid());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME, matchDatum.getFidarray().get(0).getFidname());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL, matchDatum.getFidarray().get(0).getFidlevel());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE, matchDatum.getFidarray().get(0).getFidimage());

                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_GENDER, matchDatum.getFidarray().get(0).getGender());


                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_ID_TWO, matchDatum.getFidarray().get(1).getFid());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME_TWO, matchDatum.getFidarray().get(1).getFidname());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL_TWO, matchDatum.getFidarray().get(1).getFidlevel());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE_TWO, matchDatum.getFidarray().get(1).getFidimage());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_GENDER_TWO, matchDatum.getFidarray().get(1).getGender());


                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_ID_THREE, matchDatum.getFidarray().get(2).getFid());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME_THREE, matchDatum.getFidarray().get(2).getFidname());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL_THREE, matchDatum.getFidarray().get(2).getFidlevel());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE_THREE, matchDatum.getFidarray().get(2).getFidimage());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_GENDER_THREE, matchDatum.getFidarray().get(2).getGender());

                }


                Intent intent = new Intent(context, Activity_ChatDoubles.class);
                context.startActivity(intent);
            }
        });


        if (matchDatum.getFidarray().size() == 1) {
            if (!matchDatum.getFidarray().get(0).getFid().equals("")) {
                holder.fid_one_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        showUserProfilePopup(holder.fid_one_img,matchDatum.getFidarray().get(0).getFid(), matchDatum.getFidarray().get(0).getFidlevel());


                    }
                });

            }


        } else {

            if (!matchDatum.getFidarray().get(0).getFid().equals("")) {
                holder.fid_one_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        showUserProfilePopup(holder.fid_one_img,matchDatum.getFidarray().get(0).getFid(), matchDatum.getFidarray().get(0).getFidlevel());
                    }
                });

            }


            if (!matchDatum.getFidarray().get(1).getFid().equals("")) {

                holder.fid_two_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showUserProfilePopup(holder.fid_two_img,matchDatum.getFidarray().get(1).getFid(), matchDatum.getFidarray().get(1).getFidlevel());
                    }
                });
            }
            if (!matchDatum.getFidarray().get(2).getFid().equals("")) {


                holder.fid_three_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showUserProfilePopup(holder.fid_three_img,matchDatum.getFidarray().get(2).getFid(), matchDatum.getFidarray().get(2).getFidlevel());
                    }
                });
            }
        }


        if (matchDatum.getFidarray().size() == 1) {

            if (!matchDatum.getFidarray().get(0).getFid().equals("")) {

                if (matchDatum.getFidarray().get(0).getGender().equals("Male")) {

                    Picasso.get().load(matchDatum.getFidarray().get(0).getFidimage()).placeholder(R.drawable.male).into(holder.fid_one_img);

                } else {
                    Picasso.get().load(matchDatum.getFidarray().get(0).getFidimage()).placeholder(R.drawable.female).into(holder.fid_one_img);

                }

                holder.fone_name.setText(getName(matchDatum.getFidarray().get(0).getFidname()));


            }

            holder.player_counts.setText("PLAYERS(1)");

            holder.fid_two_img.setVisibility(View.GONE);
            holder.fid_three_img.setVisibility(View.GONE);

            holder.fone_name.setVisibility(View.VISIBLE);
            holder.ftwo_name.setVisibility(View.GONE);
            holder.fthree_name.setVisibility(View.GONE);



        } else {

            if (!matchDatum.getFidarray().get(0).getFid().equals("")) {

                if (matchDatum.getFidarray().get(0).getGender().equals("Male")) {
                    Picasso.get().load(matchDatum.getFidarray().get(0).getFidimage()).placeholder(R.drawable.male).into(holder.fid_one_img);

                } else {
                    Picasso.get().load(matchDatum.getFidarray().get(0).getFidimage()).placeholder(R.drawable.female).into(holder.fid_one_img);

                }
                holder.fone_name.setText(getName(matchDatum.getFidarray().get(0).getFidname()));

            }

            if (!matchDatum.getFidarray().get(1).getFid().equals("")) {

                if (matchDatum.getFidarray().get(1).getGender().equals("Male")) {
                    Picasso.get().load(matchDatum.getFidarray().get(1).getFidimage()).placeholder(R.drawable.male).into(holder.fid_two_img);

                } else {
                    Picasso.get().load(matchDatum.getFidarray().get(1).getFidimage()).placeholder(R.drawable.female).into(holder.fid_two_img);

                }
                holder.ftwo_name.setText(getName(matchDatum.getFidarray().get(1).getFidname()));

            }
            if (!matchDatum.getFidarray().get(2).getFid().equals("")) {
                if (matchDatum.getFidarray().get(2).getGender().equals("Male")) {
                    Picasso.get().load(matchDatum.getFidarray().get(2).getFidimage()).placeholder(R.drawable.male).into(holder.fid_three_img);

                } else {
                    Picasso.get().load(matchDatum.getFidarray().get(2).getFidimage()).placeholder(R.drawable.female).into(holder.fid_three_img);

                }

                holder.fthree_name.setText(getName(matchDatum.getFidarray().get(2).getFidname()));

            }

            holder.fid_two_img.setVisibility(View.VISIBLE);
            holder.fid_three_img.setVisibility(View.VISIBLE);
            holder.player_counts.setText("PLAYERS(3)");

            holder.fone_name.setVisibility(View.VISIBLE);
            holder.ftwo_name.setVisibility(View.VISIBLE);
            holder.fthree_name.setVisibility(View.VISIBLE);


        }


    }

    @Override
    public int getItemCount() {
        return matchDatumList.size();
    }

    private void showuserDetail(String user_id, ImageView oopimg, TextView oppname, TextView age, TextView points) {


        if (typeofcall==0){
            ExploreMatchesActivity.swipreferesh.setRefreshing(true);
        }else {
            ScoringRequestsActivity.swipreferesh.setRefreshing(true);

        }

//        loader_dialog(context);
        Apis apis = RestAdapter.createAPI();
        Call<OpponentsResponse> callbackCall = apis.getopponetinfo(user_id);
        callbackCall.enqueue(new Callback<OpponentsResponse>() {
            @Override
            public void onResponse(Call<OpponentsResponse> call, Response<OpponentsResponse> response) {
                OpponentsResponse resp = response.body();
                if (resp != null && !resp.getError()) {
                    if (typeofcall==0){
                        ExploreMatchesActivity.swipreferesh.setRefreshing(false);
                    }else {
                        ScoringRequestsActivity.swipreferesh.setRefreshing(false);

                    }
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
                    if (typeofcall==0){
                        ExploreMatchesActivity.swipreferesh.setRefreshing(false);
                    }else {
                        ScoringRequestsActivity.swipreferesh.setRefreshing(false);

                    }


//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<OpponentsResponse> call, Throwable t) {

                if (typeofcall==0){
                    ExploreMatchesActivity.swipreferesh.setRefreshing(false);
                }else{
                    ScoringRequestsActivity.swipreferesh.setRefreshing(false);

                }
                    Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }

    private void userProfiledialog(String id, String level_id) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = li.inflate(R.layout.dialog_users_profile_dialog, null, false);
        dialog.setContentView(v1);
        dialog.setCancelable(true);


        ImageView oppImg = (ImageView) v1.findViewById(R.id.opp_image);
        ImageView imageView5 = (ImageView) v1.findViewById(R.id.imageView5);
        TextView tvusername = (TextView) v1.findViewById(R.id.tvusername);
        TextView tvAge = (TextView) v1.findViewById(R.id.tvAge);
        TextView points = (TextView) v1.findViewById(R.id.points);
        TextView tvLevel = (TextView) v1.findViewById(R.id.tvLevel);


        tvLevel.setText(Constant.getGameLevelFromlevelId(level_id));

        showuserDetail(id, oppImg, tvusername, tvAge, points);


//            Picasso.get().load(dualMatchListDatum.getFidarray().get(position).getFidimage()).placeholder(R.drawable.male).into(oppImg);


        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

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
                ViewGroup.LayoutParams.WRAP_CONTENT,true

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

    public void loader_dialog(final Context context) {
        loader_dialog = new Dialog(context);
        loader_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.progress_dilog_lyt, null, false);
        /*HERE YOU CAN FIND YOU IDS AND SET TEXTS OR BUTTONS*/

        FillableLoader fillableLoader = view.findViewById(R.id.fillableLoader);

        fillableLoader.setSvgPath(MODAL);
        fillableLoader.start();

//        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        loader_dialog.setContentView(view);
        final Window window = loader_dialog.getWindow();
        loader_dialog.setCancelable(false);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER);
        loader_dialog.show();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView game_name_with_game, gametime, tvAreaName3, tvV1, mid_level, player_counts,fthree_name,ftwo_name,fone_name;

        public ImageView game_img;
        public CircleImageView mid_img, fid_one_img, fid_two_img, fid_three_img;
        public LinearLayout matchdetail_ll;

        public TextView tvJoinMa;

        public MyViewHolder(View view) {
            super(view);
            game_name_with_game = (TextView) view.findViewById(R.id.game_name_with_game);
            fthree_name = (TextView) view.findViewById(R.id.fthree_name);
            fone_name = (TextView) view.findViewById(R.id.fone_name);
            ftwo_name = (TextView) view.findViewById(R.id.ftwo_name);
            player_counts = (TextView) view.findViewById(R.id.player_counts);
            gametime = (TextView) view.findViewById(R.id.gametime);

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
    public String getName(String fullName){
        return fullName.split(" (?!.* )")[0];
    }
}