package com.sportskonnect.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sportskonnect.R;
import com.sportskonnect.activities.ExploreMatchesActivity;
import com.sportskonnect.activities.ScoringRequestsActivity;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.OpponentsResponse;
import com.sportskonnect.api.Constant;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.modal.Fidarray;
import com.sportskonnect.modal.TourDatum;
import com.sportskonnect.modal.TourFidarray;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinedOpponentsAdapter extends RecyclerView.Adapter<JoinedOpponentsAdapter.MyViewHolder> {

    private static SparseBooleanArray sSelectedItems;
    private List<TourFidarray> tourFidarrayList;
    private String winnerprize="";
    private Context context;
    private int type;

    private String selectedWinnerid="";
    private String selectedRunnerid="";


    private int isrunner_or_winner = 0;

    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int mSelectedItem=-1;
    private String runnerprize="";
    private PopupWindow profilePopupWindow;


    public JoinedOpponentsAdapter() {
    }

    public JoinedOpponentsAdapter(Context context, List<TourFidarray> tourFidarrayList,String winnerprize, int type, int isrunner_or_winner,String runnerprize) {
        this.context = context;
        this.tourFidarrayList = tourFidarrayList;
        this.winnerprize = winnerprize;
        this.type = type;
        this.isrunner_or_winner = isrunner_or_winner;
        this.runnerprize = runnerprize;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.joinedopp_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final TourFidarray tourFidarray = tourFidarrayList.get(position);


        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Semibold.otf");

        Typeface face2 = Typeface.createFromAsset(context.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");

        String gender = SharedPreference.getSharedPreferenceString(context,SharedprefKeys.GENDER,"");

        try{

            if (tourFidarray.getGender().equals("Male")){
                Picasso.get().load(tourFidarray.getFidimage()).placeholder(R.drawable.male).into(holder.profile_image);

            }else {
                Picasso.get().load(tourFidarray.getFidimage()).placeholder(R.drawable.female).into(holder.profile_image);

            }


        }catch (Exception ex){
            ex.printStackTrace();
        }

        holder.winner_name_tv.setText(tourFidarray.getFidname());


        holder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserProfilePopup(holder.profile_image,tourFidarray.getFid(),tourFidarray.getFidlevel());
            }
        });


        if (type == 0) {

            holder.name_opp_ll.setVisibility(View.GONE);
            holder.prize_opp_rl.setVisibility(View.GONE);

        } else if (type == 1) {

            holder.name_opp_ll.setVisibility(View.VISIBLE);
//            holder.prize_opp_rl.setVisibility(View.VISIBLE);

            holder.winner_amt_tv.setText("Rs. "+winnerprize);
            holder.runner_amt_tv.setText("Rs. "+runnerprize);



            holder.winerstatus_tv.setText(Constant.getGameLevelFromlevelId(tourFidarray.getFidlevel()));
            holder.winerstatus_tv.setVisibility(View.VISIBLE);


            if (isrunner_or_winner==1){

                if (position==mSelectedItem){
                    holder.prize_opp_rl.setVisibility(View.VISIBLE);
                    holder.prize_runner_rl.setVisibility(View.GONE);
                    holder.winerstatus_tv.setText("WINNER");

                }else {
                    holder.prize_opp_rl.setVisibility(View.GONE);

                    holder.winerstatus_tv.setText(Constant.getGameLevelFromlevelId(tourFidarray.getFidlevel()));


                }


            }else if (isrunner_or_winner==2){

                if (position==mSelectedItem){
                    holder.prize_runner_rl.setVisibility(View.VISIBLE);
                    holder.prize_opp_rl.setVisibility(View.GONE);

                    holder.winerstatus_tv.setText("RUNNER");

                }else {
                    holder.prize_runner_rl.setVisibility(View.GONE);

                    holder.winerstatus_tv.setText(Constant.getGameLevelFromlevelId(tourFidarray.getFidlevel()));


                }

//                selectedRunnerid =tourFidarrayList.get(position).getMemberid();

            }





        }


    }



    public String  getselectedRunner(){

        return selectedRunnerid;

    }


    public String  getselectedWinner(){

        return selectedWinnerid;

    }


    public void notifyrowremoved(){

        notifyItemRemoved(mSelectedItem);
        tourFidarrayList.remove(mSelectedItem);
    }

    @Override
    public int getItemCount() {
        return tourFidarrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {



        public CircleImageView profile_image;
        public TextView winner_name_tv, winerstatus_tv,winner_amt_tv,runner_amt_tv;

        public ConstraintLayout selected_row;
        public LinearLayout name_opp_ll;
        public RelativeLayout prize_opp_rl,prize_runner_rl;


        public MyViewHolder(View view) {
            super(view);
            profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
            name_opp_ll = (LinearLayout) view.findViewById(R.id.name_opp_ll);
            prize_opp_rl = (RelativeLayout) view.findViewById(R.id.prize_opp_rl);
            prize_runner_rl = (RelativeLayout) view.findViewById(R.id.runner_rl);
            winner_name_tv = (TextView) view.findViewById(R.id.winner_name_tv);
            winerstatus_tv = (TextView) view.findViewById(R.id.winerstatus_tv);
            winner_amt_tv = (TextView) view.findViewById(R.id.winner_amt_tv);
            runner_amt_tv = (TextView) view.findViewById(R.id.runner_amt_tv);
            selected_row = (ConstraintLayout) view.findViewById(R.id.selected_row);




            selected_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mSelectedItem = getAdapterPosition();

                    notifyDataSetChanged();

                    if (isrunner_or_winner==1){
                        selectedWinnerid =tourFidarrayList.get(mSelectedItem).getMemberid();

                    }else if (isrunner_or_winner==2){
                        selectedRunnerid =tourFidarrayList.get(mSelectedItem).getMemberid();

                    }


                }
            });

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
    private void showuserDetail(String user_id, ImageView oopimg, TextView oppname, TextView age, TextView points) {


        /*if (typeofcall==0){
            ExploreMatchesActivity.swipreferesh.setRefreshing(true);
        }else {
            ScoringRequestsActivity.swipreferesh.setRefreshing(true);

        }*/

//        loader_dialog(context);
        Apis apis = RestAdapter.createAPI();
        Call<OpponentsResponse> callbackCall = apis.getopponetinfo(user_id);
        callbackCall.enqueue(new Callback<OpponentsResponse>() {
            @Override
            public void onResponse(Call<OpponentsResponse> call, Response<OpponentsResponse> response) {
                OpponentsResponse resp = response.body();
                if (resp != null && !resp.getError()) {
                  /*  if (typeofcall==0){
                        ExploreMatchesActivity.swipreferesh.setRefreshing(false);
                    }else {
                        ScoringRequestsActivity.swipreferesh.setRefreshing(false);

                    }*/
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
                   /* if (typeofcall==0){
                        ExploreMatchesActivity.swipreferesh.setRefreshing(false);
                    }else {
                        ScoringRequestsActivity.swipreferesh.setRefreshing(false);

                    }*/


//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<OpponentsResponse> call, Throwable t) {

                /*if (typeofcall==0){
                    ExploreMatchesActivity.swipreferesh.setRefreshing(false);
                }
                */
                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
    }

}