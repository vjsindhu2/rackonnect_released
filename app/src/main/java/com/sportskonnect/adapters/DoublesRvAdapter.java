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
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jorgecastillo.FillableLoader;
import com.sportskonnect.Activity_Badminton;
import com.sportskonnect.Activity_ChatDoubles;
import com.sportskonnect.R;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.JoinDualMatchResponse;
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

public class DoublesRvAdapter extends RecyclerView.Adapter<DoublesRvAdapter.MyViewHolder> implements Filterable {

    private List<DualMatchListDatum> dualMatchListDatumList;
    private List<DualMatchListDatum> dualMatchListDatumListFiltered;
    private Context context;
    private SharedPreference sharedPreference;
    private boolean group_a_member_one_selected = false;
    private boolean group_a_member_two_selected = false;
    private boolean group_b_member_one_selected = false;
    private boolean group_b_member_two_selected = false;
    private String group_selected_member_id = "";
    private Dialog loader_dialog;
    private PopupWindow profilePopupWindow;

    public DoublesRvAdapter() {
    }

    public DoublesRvAdapter(Context context, List<DualMatchListDatum> dualMatchListDatumList) {
        this.context = context;
        this.dualMatchListDatumList = dualMatchListDatumList;
        this.dualMatchListDatumListFiltered = dualMatchListDatumList;
        sharedPreference = new SharedPreference(context);
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doubles_match_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DualMatchListDatum dualMatchListDatum = dualMatchListDatumList.get(position);

        holder.setIsRecyclable(false);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Semibold.otf");

        Typeface face2 = Typeface.createFromAsset(context.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");

        holder.game_name_with_game.setTypeface(face2);

        holder.tvAreaName3.setTypeface(typeface);
        holder.game_name_with_game.setText(dualMatchListDatum.getMatchname() + " - " + Constant.getGameNameFromcatId(dualMatchListDatum.getCatid()));


        int playercount = 0;

        holder.players_count.setText("PLAYERS ("+playercount +")");

        if (!dualMatchListDatum.getFidarray().get(0).getFid().equals("")){

            playercount++;
            holder.players_count.setText("PLAYERS ("+playercount +")");

        } if (!dualMatchListDatum.getFidarray().get(1).getFid().equals("")){
            playercount++;
            holder.players_count.setText("PLAYERS ("+playercount +")");

        } if (!dualMatchListDatum.getFidarray().get(2).getFid().equals("")){
            playercount++;
            holder.players_count.setText("PLAYERS ("+playercount +")");

        }




        if (dualMatchListDatum.getMid().equals(SharedPreference.getSharedPreferenceString(context, SharedprefKeys.USER_ID, ""))) {
            holder.tvJoinMa.setVisibility(View.GONE);
            holder.tvChatMa.setVisibility(View.VISIBLE);
        } else {
            holder.tvJoinMa.setVisibility(View.VISIBLE);
            holder.tvChatMa.setVisibility(View.GONE);

        }

        for (int i = 0; i < dualMatchListDatum.getFidarray().size(); i++) {

            if (SharedPreference.getSharedPreferenceString(context, SharedprefKeys.USER_ID, "").equals(dualMatchListDatum.getFidarray().get(i).getFid())) {
                holder.tvJoinMa.setVisibility(View.GONE);
                holder.tvChatMa.setVisibility(View.VISIBLE);

            } else {
//                holder.tvJoinMa.setVisibility(View.VISIBLE);

            }
        }


        holder.tvChatMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_M_ID, dualMatchListDatum.getMid());
                SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_M_ID_LEVEL, dualMatchListDatum.getMidlevel());
                SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_GROUP_ID, dualMatchListDatum.getGroupid());
                SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_USER_IMAGE, dualMatchListDatum.getMidimage());
                SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_NAME, dualMatchListDatum.getMatchname());
                SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_M_GENDER, dualMatchListDatum.getGender());

                SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_CHAT_MEMBER_COUNT, String.valueOf(dualMatchListDatumList.size()+1));

                if (dualMatchListDatum.getFidarray().size() == 1) {

                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_ID, dualMatchListDatum.getFidarray().get(0).getFid());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME, dualMatchListDatum.getFidarray().get(0).getFidname());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL, dualMatchListDatum.getFidarray().get(0).getFidlevel());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE, dualMatchListDatum.getFidarray().get(0).getFidimage());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_GENDER, dualMatchListDatum.getFidarray().get(0).getGender());

                } else {

                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_ID, dualMatchListDatum.getFidarray().get(0).getFid());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME, dualMatchListDatum.getFidarray().get(0).getFidname());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL, dualMatchListDatum.getFidarray().get(0).getFidlevel());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE, dualMatchListDatum.getFidarray().get(0).getFidimage());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_GENDER, dualMatchListDatum.getFidarray().get(0).getGender());


                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_ID_TWO, dualMatchListDatum.getFidarray().get(1).getFid());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME_TWO, dualMatchListDatum.getFidarray().get(1).getFidname());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL_TWO, dualMatchListDatum.getFidarray().get(1).getFidlevel());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE_TWO, dualMatchListDatum.getFidarray().get(1).getFidimage());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_GENDER_TWO, dualMatchListDatum.getFidarray().get(1).getGender());


                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_ID_THREE, dualMatchListDatum.getFidarray().get(2).getFid());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_NAME_THREE, dualMatchListDatum.getFidarray().get(2).getFidname());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL_THREE, dualMatchListDatum.getFidarray().get(2).getFidlevel());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE_THREE, dualMatchListDatum.getFidarray().get(2).getFidimage());
                    SharedPreference.setSharedPreferenceString(context, SharedprefKeys.CURRENT_MATCH_OPPONENT_GENDER_THREE, dualMatchListDatum.getFidarray().get(2).getGender());

                }


                Intent intent = new Intent(context, Activity_ChatDoubles.class);
                context.startActivity(intent);
            }
        });

        try {





            Picasso.get().load(Constant.getGameImageFromcatId(dualMatchListDatum.getCatid())).into(holder.game_img);

           if (dualMatchListDatum.getGender().equals("Male")){
               Picasso.get().load(dualMatchListDatum.getMidimage()).placeholder(R.drawable.male).into(holder.mid_img);

           }else {
               Picasso.get().load(dualMatchListDatum.getMidimage()).placeholder(R.drawable.female).into(holder.mid_img);

           }

            if (!dualMatchListDatum.getFidarray().get(0).getFid().equals("")) {

                if (dualMatchListDatum.getFidarray().get(0).getGender().equals("Male")){
                    Picasso.get().load(dualMatchListDatum.getFidarray().get(0).getFidimage()).placeholder(R.drawable.male).into(holder.fid_one_img);

                }else {
                    Picasso.get().load(dualMatchListDatum.getFidarray().get(0).getFidimage()).placeholder(R.drawable.female).into(holder.fid_one_img);

                }

                holder.fone_name.setText(getName(dualMatchListDatum.getFidarray().get(0).getFidname()));
                holder.fone_name.setVisibility(View.VISIBLE);



            } else {
                holder.fone_name.setVisibility(View.GONE);
                Picasso.get().load(R.drawable.circle).placeholder(R.drawable.circle).into(holder.fid_one_img);
            }


            if (!dualMatchListDatum.getFidarray().get(1).getFid().equals("")) {

                if (dualMatchListDatum.getFidarray().get(1).getGender().equals("Male")){
                    Picasso.get().load(dualMatchListDatum.getFidarray().get(1).getFidimage()).placeholder(R.drawable.male).into(holder.fid_two_img);

                }else {
                    Picasso.get().load(dualMatchListDatum.getFidarray().get(1).getFidimage()).placeholder(R.drawable.female).into(holder.fid_two_img);

                }
                holder.ftwo_name.setText(getName(dualMatchListDatum.getFidarray().get(1).getFidname()));
                holder.ftwo_name.setVisibility(View.VISIBLE);



            } else {
                Picasso.get().load(R.drawable.circle).placeholder(R.drawable.circle).into(holder.fid_two_img);
                holder.ftwo_name.setVisibility(View.GONE);

            }
            if (!dualMatchListDatum.getFidarray().get(2).getFid().equals("")) {

                if (dualMatchListDatum.getFidarray().get(2).getGender().equals("Male")){
                    Picasso.get().load(dualMatchListDatum.getFidarray().get(2).getFidimage()).placeholder(R.drawable.male).into(holder.fid_three_img);

                }else {
                    Picasso.get().load(dualMatchListDatum.getFidarray().get(2).getFidimage()).placeholder(R.drawable.female).into(holder.fid_three_img);

                }

                holder.fthree_name.setText(getName(dualMatchListDatum.getFidarray().get(2).getFidname()));
                holder.fthree_name.setVisibility(View.VISIBLE);


            } else {
                Picasso.get().load(R.drawable.circle).placeholder(R.drawable.circle).into(holder.fid_three_img);
                holder.fthree_name.setVisibility(View.GONE);

            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        holder.gametime.setText(dualMatchListDatum.getDate() + " | " + Constant.convet24hourTo12hour(dualMatchListDatum.getStartTime()) + " - " + Constant.convet24hourTo12hour(dualMatchListDatum.getEndTime()));

        holder.tvAreaName3.setText(dualMatchListDatum.getAddress());

        holder.tvV1.setText(dualMatchListDatum.getMidname());
        holder.mid_level.setText(Constant.getGameLevelFromlevelId(dualMatchListDatum.getMidlevel()));

        holder.tvJoinMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                opendialogJoinGroup(SharedPreference.getSharedPreferenceString(context, SharedprefKeys.USER_ID, ""), SharedPreference.getSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_LEVEL, ""), dualMatchListDatum.getGroupid(), dualMatchListDatum, holder.tvJoinMa, holder.tvChatMa);


            }
        });


        if (!dualMatchListDatum.getFidarray().get(0).getFid().equals("")) {
            holder.fid_one_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showUserProfilePopup(holder.fid_one_img,dualMatchListDatum.getFidarray().get(0).getFid(), dualMatchListDatum.getFidarray().get(0).getFidlevel());
                }
            });

        }

        if (!dualMatchListDatum.getFidarray().get(1).getFid().equals("")) {

            holder.fid_two_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showUserProfilePopup(holder.fid_two_img,dualMatchListDatum.getFidarray().get(1).getFid(), dualMatchListDatum.getFidarray().get(1).getFidlevel());

                }
            });
        }
        if (!dualMatchListDatum.getFidarray().get(2).getFid().equals("")) {


            holder.fid_three_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showUserProfilePopup(holder.fid_three_img,dualMatchListDatum.getFidarray().get(2).getFid(), dualMatchListDatum.getFidarray().get(2).getFidlevel());
                }
            });
        }
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

    @Override
    public int getItemCount() {
        return dualMatchListDatumListFiltered.size();
    }

    private void joindulaMatch(String user_id, String userlevel, String groupid, String memberid, TextView joinmatch, TextView chatnow) {

        Apis apis = RestAdapter.createAPI();
        Call<JoinDualMatchResponse> callbackCall = apis.joinDualmatch(user_id, userlevel, groupid, memberid);
        callbackCall.enqueue(new Callback<JoinDualMatchResponse>() {
            @Override
            public void onResponse(Call<JoinDualMatchResponse> call, Response<JoinDualMatchResponse> response) {
                JoinDualMatchResponse resp = response.body();
                if (resp != null && !resp.getError()) {

                    Toast.makeText(context, resp.getErrorMsg() + "", Toast.LENGTH_LONG).show();


                    joinmatch.setVisibility(View.GONE);
                    chatnow.setVisibility(View.VISIBLE);



                    ((Activity_Badminton) context).runFetchlocation();
//                    try{
//
//                        if (SharedPreference.getSharedPreferenceString(context,SharedprefKeys.GENDER,"").equals("Male")){
//                            Picasso.get().load(SharedPreference.getSharedPreferenceString(context,SharedprefKeys.PROFILE_PIC_URL,"")).placeholder(R.drawable.male).into(image);
//
//                        }else {
//                            Picasso.get().load(SharedPreference.getSharedPreferenceString(context,SharedprefKeys.PROFILE_PIC_URL,"")).placeholder(R.drawable.female).into(image);
//
//                        }
//
//                    }catch (Exception ex){ex.printStackTrace();}

//                    dialogServerNotConnect();



                } else {


                    joinmatch.setVisibility(View.VISIBLE);
                    chatnow.setVisibility(View.GONE);
//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<JoinDualMatchResponse> call, Throwable t) {

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
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


    private void opendialogJoinGroup(String user_id, String userlevel, String groupid, DualMatchListDatum dualMatchListDatum, TextView joinmath, TextView chatnow) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = li.inflate(R.layout.joindual_match_dialog, null, false);
        dialog.setCancelable(false);
        dialog.setContentView(v1);

        LinearLayout group_a_one_ll = (LinearLayout) v1.findViewById(R.id.group_a_one_ll);
        LinearLayout group_a_two_ll = (LinearLayout) v1.findViewById(R.id.group_a_two_ll);

        LinearLayout group_b_one_ll = (LinearLayout) v1.findViewById(R.id.group_b_one_ll);
        LinearLayout group_b_two_ll = (LinearLayout) v1.findViewById(R.id.group_b_two_ll);


        CircleImageView group_a_member_one_img = (CircleImageView) v1.findViewById(R.id.group_a_member_one_img);
        CircleImageView group_a_member_two_img = (CircleImageView) v1.findViewById(R.id.group_a_member_two_img);
        CircleImageView group_b_member_one_img = (CircleImageView) v1.findViewById(R.id.group_b_member_one_img);
        CircleImageView group_b_member_two_img = (CircleImageView) v1.findViewById(R.id.group_b_member_two_img);


        TextView group_a_member_one_name = (TextView) v1.findViewById(R.id.group_a_member_one_name);
        TextView group_a_member_one_level = (TextView) v1.findViewById(R.id.group_a_member_one_level);
        TextView group_a_member_two_name = (TextView) v1.findViewById(R.id.group_a_member_two_name);
        TextView group_a_member_two_level = (TextView) v1.findViewById(R.id.group_a_member_two_level);
        TextView group_b_member_one_name = (TextView) v1.findViewById(R.id.group_b_member_one_name);
        TextView group_b_member_one_level = (TextView) v1.findViewById(R.id.group_b_member_one_level);
        TextView group_b_member_two_name = (TextView) v1.findViewById(R.id.group_b_member_two_name);
        TextView group_b_member_two_level = (TextView) v1.findViewById(R.id.group_b_member_two_level);

        group_a_member_one_selected = true;

        /*        group A member one */
        if (group_a_member_one_selected) {
            group_a_one_ll.setClickable(false);
            group_a_one_ll.setEnabled(false);

        }


        try {

            Picasso.get().load(dualMatchListDatum.getMidimage()).placeholder(R.drawable.male).into(group_a_member_one_img);

            group_a_member_one_name.setText(dualMatchListDatum.getMidname());
            group_a_member_one_level.setText(Constant.getGameLevelFromlevelId(dualMatchListDatum.getMidlevel()));


        } catch (Exception ex) {
            ex.printStackTrace();
        }


        if (!dualMatchListDatum.getFidarray().get(0).getFid().equals("")) {
            group_a_member_two_selected = true;
            group_a_two_ll.setClickable(false);
            group_a_two_ll.setEnabled(false);

//            group_selected_member_id = dualMatchListDatum.getFidarray().get(0).getMemberid();
            Picasso.get().load(dualMatchListDatum.getFidarray().get(0).getFidimage()).placeholder(R.drawable.male).into(group_a_member_two_img);

            group_a_member_two_name.setText(dualMatchListDatum.getFidarray().get(0).getFidname());
            group_a_member_two_level.setText(Constant.getGameLevelFromlevelId(dualMatchListDatum.getFidarray().get(0).getFidlevel()));

        } else {
            group_a_member_two_selected = false;
            group_a_two_ll.setClickable(true);
            group_a_two_ll.setEnabled(true);

        }

        if (!dualMatchListDatum.getFidarray().get(1).getFid().equals("")) {

            group_b_member_one_selected = true;
            group_b_one_ll.setClickable(false);
            group_b_one_ll.setEnabled(false);

//            group_selected_member_id = dualMatchListDatum.getFidarray().get(1).getMemberid();
            Picasso.get().load(dualMatchListDatum.getFidarray().get(1).getFidimage()).placeholder(R.drawable.male).into(group_b_member_one_img);

            group_b_member_one_name.setText(dualMatchListDatum.getFidarray().get(1).getFidname());
            group_b_member_one_level.setText(Constant.getGameLevelFromlevelId(dualMatchListDatum.getFidarray().get(1).getFidlevel()));

        } else {
            group_b_member_one_selected = false;
            group_b_one_ll.setClickable(true);
            group_b_one_ll.setEnabled(true);

        }


        if (!dualMatchListDatum.getFidarray().get(2).getFid().equals("")) {

            group_b_member_two_selected = true;

            group_b_two_ll.setClickable(false);
            group_b_two_ll.setEnabled(false);
//            group_selected_member_id = dualMatchListDatum.getFidarray().get(2).getMemberid();
            Picasso.get().load(dualMatchListDatum.getFidarray().get(2).getFidimage()).placeholder(R.drawable.male).into(group_b_member_two_img);

            group_b_member_two_name.setText(dualMatchListDatum.getFidarray().get(2).getFidname());
            group_b_member_two_level.setText(Constant.getGameLevelFromlevelId(dualMatchListDatum.getFidarray().get(2).getFidlevel()));

        } else {
            group_b_member_two_selected = false;
            group_b_two_ll.setClickable(true);
            group_b_two_ll.setEnabled(true);
        }


        group_a_two_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {


                    group_selected_member_id = dualMatchListDatum.getFidarray().get(0).getMemberid();

                    Picasso.get().load(SharedPreference.getSharedPreferenceString(context, SharedprefKeys.PROFILE_PIC_URL, "")).placeholder(R.drawable.male).into(group_a_member_two_img);

                    group_a_member_two_name.setText(SharedPreference.getSharedPreferenceString(context, SharedprefKeys.USER_NAME, ""));
                    group_a_member_two_level.setText(Constant.getGameLevelFromlevelId(SharedPreference.getSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_LEVEL, "")));

                    if (dualMatchListDatum.getFidarray().get(1).getFid().equals("")) {

                        Picasso.get().load(R.drawable.circle).placeholder(R.drawable.circle).into(group_b_member_one_img);

                        group_b_member_one_name.setText("---- ----");
                        group_b_member_one_level.setText("----");

                    }

                    if (dualMatchListDatum.getFidarray().get(2).getFid().equals("")) {
                        Picasso.get().load(R.drawable.circle).placeholder(R.drawable.circle).into(group_b_member_two_img);

                        group_b_member_two_name.setText("---- ----");
                        group_b_member_two_level.setText("----");

                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });


        group_b_two_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {


                    group_selected_member_id = dualMatchListDatum.getFidarray().get(2).getMemberid();

                    Picasso.get().load(SharedPreference.getSharedPreferenceString(context, SharedprefKeys.PROFILE_PIC_URL, "")).placeholder(R.drawable.male).into(group_b_member_two_img);

                    group_b_member_two_name.setText(SharedPreference.getSharedPreferenceString(context, SharedprefKeys.USER_NAME, ""));
                    group_b_member_two_level.setText(Constant.getGameLevelFromlevelId(SharedPreference.getSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_LEVEL, "")));


                    if (dualMatchListDatum.getFidarray().get(0).getFid().equals("")) {

                        Picasso.get().load(R.drawable.circle).placeholder(R.drawable.circle).into(group_a_member_two_img);

                        group_a_member_two_name.setText("---- ----");
                        group_a_member_two_level.setText("----");
                    }

                    if (dualMatchListDatum.getFidarray().get(1).getFid().equals("")) {
                        Picasso.get().load(R.drawable.circle).placeholder(R.drawable.circle).into(group_b_member_one_img);
                        group_b_member_one_name.setText("---- ----");
                        group_b_member_one_level.setText("----");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        group_b_one_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    group_selected_member_id = dualMatchListDatum.getFidarray().get(1).getMemberid();

                    Picasso.get().load(SharedPreference.getSharedPreferenceString(context, SharedprefKeys.PROFILE_PIC_URL, "")).placeholder(R.drawable.male).into(group_b_member_one_img);

                    group_b_member_one_name.setText(SharedPreference.getSharedPreferenceString(context, SharedprefKeys.USER_NAME, ""));
                    group_b_member_one_level.setText(Constant.getGameLevelFromlevelId(SharedPreference.getSharedPreferenceString(context, SharedprefKeys.USER_SELECTED_LEVEL, "")));


                    if (dualMatchListDatum.getFidarray().get(0).getFid().equals("")) {
                        Picasso.get().load(R.drawable.circle).placeholder(R.drawable.circle).into(group_a_member_two_img);
                        group_a_member_two_name.setText("---- ----");
                        group_a_member_two_level.setText("----");
                    }

                    if (dualMatchListDatum.getFidarray().get(2).getFid().equals("")) {
                        Picasso.get().load(R.drawable.circle).placeholder(R.drawable.circle).into(group_b_member_two_img);

                        group_b_member_two_name.setText("---- ----");
                        group_b_member_two_level.setText("----");
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });

        Button btn_cancel = (Button) v1.findViewById(R.id.btn_cancel);
        Button btn_join = (Button) v1.findViewById(R.id.btn_join);


        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!group_selected_member_id.equals("")) {

                    dialog.dismiss();

                    joindulaMatch(user_id, userlevel, groupid, group_selected_member_id, joinmath, chatnow);

                } else {

                    Toast.makeText(context, "Please Select a group", Toast.LENGTH_LONG).show();

                }


            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                group_selected_member_id = "";
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView game_name_with_game, gametime, tvAreaName3, tvV1, mid_level, tvJoinMa, time_status,fone_name,ftwo_name,fthree_name;
        public CircleImageView mid_img, fid_one_img, fid_two_img, fid_three_img;
        public ImageView game_img;
        public TextView tvChatMa,players_count;


        public MyViewHolder(View view) {
            super(view);
            game_name_with_game = (TextView) view.findViewById(R.id.game_name_with_game);
            fone_name = (TextView) view.findViewById(R.id.fone_name);
            ftwo_name = (TextView) view.findViewById(R.id.ftwo_name);
            fthree_name = (TextView) view.findViewById(R.id.fthree_name);
            mid_img = (CircleImageView) view.findViewById(R.id.mid_img);
            fid_one_img = (CircleImageView) view.findViewById(R.id.fid_one_img);
            fid_two_img = (CircleImageView) view.findViewById(R.id.fid_two_img);
            fid_three_img = (CircleImageView) view.findViewById(R.id.fid_three_img);
            gametime = (TextView) view.findViewById(R.id.gametime);
            mid_level = (TextView) view.findViewById(R.id.mid_level);
            tvAreaName3 = (TextView) view.findViewById(R.id.tvAreaName3);
            tvV1 = (TextView) view.findViewById(R.id.tvV1);
            time_status = (TextView) view.findViewById(R.id.time_status);
            tvJoinMa = (TextView) view.findViewById(R.id.tvJoinMa);
            tvChatMa = (TextView) view.findViewById(R.id.tvChatMa);
            players_count = (TextView) view.findViewById(R.id.players_count);
            game_img = (ImageView) view.findViewById(R.id.game_img);

        }
    }

    public String getName(String fullName){
        return fullName.split(" (?!.* )")[0];
    }
}