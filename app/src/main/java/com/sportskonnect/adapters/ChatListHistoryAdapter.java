package com.sportskonnect.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.sportskonnect.Activity_TournamentChat;
import com.sportskonnect.R;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.ChatListHistResponse;
import com.sportskonnect.api.Callbacks.OpponentsResponse;
import com.sportskonnect.api.Constant;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.modal.ChatHistDatum;
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

public class ChatListHistoryAdapter extends RecyclerView.Adapter<ChatListHistoryAdapter.MyViewHolder> {

    private List<ChatHistDatum> matchDatumList;
    private Context context;
    private SharedPreference sharedPreference;
    private int typeofcall = 0;
    private PopupWindow profilePopupWindow;

    public ChatListHistoryAdapter(Context context, List<ChatHistDatum> matchDatumList, int typeofcall) {
        this.context = context;
        this.matchDatumList = matchDatumList;
        this.typeofcall = typeofcall;
        sharedPreference = new SharedPreference(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.explore_chat_history_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ChatHistDatum matchDatum = matchDatumList.get(position);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Semibold.otf");

        Typeface face2 = Typeface.createFromAsset(context.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Alt Bold.otf");


        holder.tv_opname.setText(matchDatum.getName());
        holder.cat_name.setText(matchDatum.getLastmsg());
        holder.time_tv.setText(matchDatum.getTime());

        if (matchDatum.getGender().equals("Male")){

            Picasso.get().load(matchDatum.getImage()).placeholder(R.drawable.male).into(holder.profile_image);
        }else {
            Picasso.get().load(matchDatum.getImage()).placeholder(R.drawable.female).into(holder.profile_image);

        }


        holder.row_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Activity_TournamentChat.class);

                SharedPreference.setSharedPreferenceString(context,SharedprefKeys.OPPONENT_ID,matchDatum.getUserid());
                SharedPreference.setSharedPreferenceString(context,SharedprefKeys.OPPONENT_NAME,matchDatum.getName());
                SharedPreference.setSharedPreferenceString(context,SharedprefKeys.OPPONENT_LEVEL,String.valueOf(matchDatum.getLevelid()));
                SharedPreference.setSharedPreferenceString(context,SharedprefKeys.CHATCOMEFROM,"1");

                context.startActivity(intent);

                ((Activity)context).finish();


            }
        });


    }

    @Override
    public int getItemCount() {


        if (typeofcall==1){
            return matchDatumList.size();
        }else {
            if (matchDatumList.size()>2){
                return 2;
            }else{

                return matchDatumList.size();

            }

        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_opname, cat_name, time_tv;

        public CircleImageView profile_image;
        public LinearLayout row_ll;

        public MyViewHolder(View view) {
            super(view);
            tv_opname = (TextView) view.findViewById(R.id.tv_opname);
            cat_name = (TextView) view.findViewById(R.id.cat_name);
            time_tv = (TextView) view.findViewById(R.id.time_tv);

            profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
            row_ll = (LinearLayout) view.findViewById(R.id.row_ll);


        }
    }





}