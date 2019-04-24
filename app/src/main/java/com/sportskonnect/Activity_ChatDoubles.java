package com.sportskonnect;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.gson.Gson;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.sportskonnect.adapters.DualChatRoomThreadAdapter;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.ChatStatusDoubleRespo;
import com.sportskonnect.api.Callbacks.OpponentsResponse;
import com.sportskonnect.api.Callbacks.SendMessageDualRespo;
import com.sportskonnect.api.Constant;
import com.sportskonnect.api.FetchAllDualChatResponse;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.modal.ChatDualModal;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sportskonnect.Paths.MODAL;

public class Activity_ChatDoubles extends AppCompatActivity implements View.OnClickListener {

    ImageView ivSend;
    Typeface face;
    TextView main_text_title;

    TextView count_mem_tv;
    RecyclerView chat_dual_rv;
    private DualChatRoomThreadAdapter mAdapter;
    private String selfUserId = "";
    private String opponet_id = "";
    private String opponet_id_two = "";
    private String opponet_id_three = "";

    private ArrayList<ChatDualModal> chatComonModalArrayList = new ArrayList<>();
    private String group_id = "";
    private Timer timer = new Timer();
    private EmojiconEditText etMobile;
    private String opponet_one_img = "";
    private String opponet_two_img = "";
    private String opponet_three_img = "";
    private String user_img = "";

    private CircleImageView person_one, person_two;
    private CircleImageView person_three;
    private CircleImageView person_four;
    private String current_match_name = "";
    private Dialog loader_dialog;
    private String mid = "";
    private String mGender = "";
    private String opponet_gender = "";
    private String opponet_gender_three = "";
    private String opponet_gender_two;
    private PopupWindow profilePopupWindow;
    private String chatmember_count="1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatdoubles);

        init();

        startTimer();
    }

    private void init() {

        ivSend = (ImageView) findViewById(R.id.ivSend);
        count_mem_tv = (TextView) findViewById(R.id.count_mem_tv);
        chat_dual_rv = (RecyclerView) findViewById(R.id.chat_dual_rv);
        etMobile = (EmojiconEditText) findViewById(R.id.etMobile);
        person_one = (CircleImageView) findViewById(R.id.ivv2);
        person_two = (CircleImageView) findViewById(R.id.ivv3);
        person_three = (CircleImageView) findViewById(R.id.ivv1);
        person_four = (CircleImageView) findViewById(R.id.ivv);
        ivSend.setOnClickListener(this);
        main_text_title = (TextView) findViewById(R.id.main_text_title);
        face = Typeface.createFromAsset(getAssets(),
                "fonts/Calibre Black.otf");

        main_text_title.setTypeface(face);


        selfUserId = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, "");
        chatmember_count = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_CHAT_MEMBER_COUNT, "1");

        count_mem_tv.setText(chatmember_count+"");


        mid = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_M_ID, "");

//        Log.d("ID", mid + "");
        mGender = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_M_GENDER, "");

        opponet_id = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_ID, "");
        opponet_gender = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_GENDER, "");

        opponet_id_two = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_ID_TWO, "");
        opponet_gender_two = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_GENDER_TWO, "");
        opponet_gender_three = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_GENDER_THREE, "");


        opponet_id_three = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_ID_THREE, "");
        group_id = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_GROUP_ID, "");

        user_img = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_USER_IMAGE, "");
        opponet_one_img = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE, "");
        opponet_two_img = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE_TWO, "");
        opponet_three_img = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_OPPONENT_IMAGE_THREE, "");
        current_match_name = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CURRENT_MATCH_NAME, "");



        if(!mid.equals("")){
            person_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    showUserProfilePopup(person_one,mid, SharedPreference.getSharedPreferenceString(Activity_ChatDoubles.this, SharedprefKeys.CURRENT_MATCH_M_ID_LEVEL, ""));
                }
            });

        }

        if (!opponet_id.equals("")){
            person_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showUserProfilePopup(person_two,opponet_id, SharedPreference.getSharedPreferenceString(Activity_ChatDoubles.this, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL, ""));
                }
            });

        }

        if (!opponet_id_two.equals("")){
            person_three.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showUserProfilePopup(person_three,opponet_id_two, SharedPreference.getSharedPreferenceString(Activity_ChatDoubles.this, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL_TWO, ""));
                }
            });

        }

        if (!opponet_id_three.equals("")){
            person_four.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showUserProfilePopup(person_four,opponet_id_three, SharedPreference.getSharedPreferenceString(Activity_ChatDoubles.this, SharedprefKeys.CURRENT_MATCH_OPPONENT_LEVEL_THREE, ""));
                }
            });

        }


       /* if(!opponet_id.equals("")){
              count_mem_tv.setText("2");
        }else if (!opponet_id.equals("") && !opponet_id_two.equals("")){
            count_mem_tv.setText("3");

        }else if(!opponet_id.equals("")&& !opponet_id_two.equals("")&&!opponet_id_three.equals("")){
            count_mem_tv.setText("4");

        }else if(){

        }

            else {
            count_mem_tv.setText("1");

        }*/

        main_text_title.setText(current_match_name);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        chat_dual_rv.setLayoutManager(layoutManager);
        chat_dual_rv.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new DualChatRoomThreadAdapter(Activity_ChatDoubles.this, chatComonModalArrayList, selfUserId);

        chat_dual_rv.setAdapter(mAdapter);

        try {

            if (!user_img.equals("")) {

                if (mGender.equals("Male")) {
                    Picasso.get().load(user_img).placeholder(R.drawable.male).into(person_one);

                } else {
                    Picasso.get().load(user_img).placeholder(R.drawable.female).into(person_one);

                }
            } else {

                Picasso.get().load(R.drawable.circle).placeholder(R.drawable.circle).into(person_one);

            }


            if (opponet_id.equals("")) {


                Picasso.get().load(R.drawable.circle).placeholder(R.drawable.circle).into(person_two);

            } else {

                if (opponet_gender.equals("Male")) {
                    Picasso.get().load(opponet_one_img).placeholder(R.drawable.male).into(person_two);

                } else {
                    Picasso.get().load(opponet_one_img).placeholder(R.drawable.female).into(person_two);

                }


            }
            if (opponet_id_two.equals("")) {
                Picasso.get().load(R.drawable.circle).placeholder(R.drawable.circle).into(person_three);

            } else {

                if (opponet_gender_two.equals("Male"))
                    Picasso.get().load(opponet_two_img).placeholder(R.drawable.male).into(person_three);
                else
                    Picasso.get().load(opponet_two_img).placeholder(R.drawable.female).into(person_three);

            }
            if (opponet_id_three.equals("")) {
                Picasso.get().load(R.drawable.circle).placeholder(R.drawable.circle).into(person_four);

            } else {

                if (opponet_gender_three.equals("Male"))
                    Picasso.get().load(opponet_three_img).placeholder(R.drawable.male).into(person_four);
                else
                    Picasso.get().load(opponet_three_img).placeholder(R.drawable.female).into(person_four);

            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        fetchAllchat(group_id, true);

    }

    public void startTimer() {
//        timer = new Timer();


        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {


                fetchChatThread(selfUserId, group_id);
//                fetchAllchat(selfUserId, opponet_id,false);


            }
        }, 0, 1000);//put here time 1000 milliseconds=1 second

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSend:


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());

                ChatDualModal chatComonModal = new ChatDualModal(selfUserId, "", etMobile.getText().toString().trim(), currentDateandTime);

                chatComonModalArrayList.add(chatComonModal);

//                mAdapter = new ChatRoomThreadAdapter(Activity_TournamentChat.this, chatComonModalArrayList, selfUserId);
//                chat_recycler.setAdapter(mAdapter);

                sendMessage(selfUserId, "", group_id);

                break;
        }
    }


    public void finishdoublesChat(View view) {
        finish();
    }

    private void fetchAllchat(String group_id, boolean isfirstloaded) {


        Apis apis = RestAdapter.createAPI();
        Call<FetchAllDualChatResponse> callbackCall = apis.getallchatdouble(group_id);
        callbackCall.enqueue(new Callback<FetchAllDualChatResponse>() {
            @Override
            public void onResponse(Call<FetchAllDualChatResponse> call, Response<FetchAllDualChatResponse> response) {
                FetchAllDualChatResponse resp = response.body();
                if (resp != null && !resp.getError()) {

//                    opponentmessageArrayList.clear();

//                    chatComonModalArrayList.clear();
                    Log.d("RESPOCHAT", new Gson().toJson(resp));

                    for (int i = 0; i < resp.getData().size(); i++) {
//                        AllChatConversation insertFullChat = new AllChatConversation(
//                                resp.getData().get(i).getMsg(),
//                                resp.getData().get(i).getMid(),
//                                resp.getData().get(i).getFid(),
//                                resp.getData().get(i).getTime(),
//                                resp.getData().get(i).getStatus()
//
//                        );

                        ChatDualModal chatComonModal = new ChatDualModal(
                                resp.getData().get(i).getMid(),
                                resp.getData().get(i).getName(),
                                resp.getData().get(i).getMsg(),
                                resp.getData().get(i).getTime()
                        );

                        chatComonModalArrayList.add(chatComonModal);

                    }


                    if (chatComonModalArrayList.size() > 0) {
//                        mAdapter = new ChatRoomThreadAdapter(Activity_TournamentChat.this, allChatConversationArrayList, selfUserId, 1);


                        mAdapter.notifyDataSetChanged();


                        if (isfirstloaded) {
                            if (mAdapter.getItemCount() > 1) {
                                // scrolling to bottom of the recycler view
                                chat_dual_rv.getLayoutManager().smoothScrollToPosition(chat_dual_rv, null, mAdapter.getItemCount() - 1);
                            }

                        }
                    }


//                    setingAdapter(messageArrayList,1);

//                    setingAdapter(messageArrayList, 1);


                } else {

//                    Log.d("OPPID",opponent_id);
//                    mAdapter = new ChatRoomThreadAdapter(Activity_TournamentChat.this, messageArrayList, selfUserId);
//
//                    chat_recycler.setAdapter(mAdapter);


                }
            }

            @Override
            public void onFailure(Call<FetchAllDualChatResponse> call, Throwable t) {

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });


    }


    /**
     * Posting a new message in chat room
     * will make an http call to our server. Our server again sends the message
     * to all the devices as push notification
     */


    private void sendMessage(String user_id, String msg, String group_id) {


        final String message = this.etMobile.getText().toString().trim();

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(getApplicationContext(), "Enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        Apis apis = RestAdapter.createAPI();
        Call<SendMessageDualRespo> callbackCall = apis.addchatdouble(user_id, message, group_id);
        callbackCall.enqueue(new Callback<SendMessageDualRespo>() {
            @Override
            public void onResponse(Call<SendMessageDualRespo> call, Response<SendMessageDualRespo> response) {
                SendMessageDualRespo resp = response.body();
                if (resp != null && !resp.getError()) {

                    Log.d("RESPOCHATSEND", new Gson().toJson(resp));

//                    ChatComonModal insertChatAdapter = new ChatComonModal(
//
//                            resp.getMsg(),
//                            resp.getTime(),
//                            resp.getMid()
//
//                    );
//
//
//                    chatComonModalArrayList.add(insertChatAdapter);

                    scrollToBottom();

                    mAdapter.notifyDataSetChanged();


                } else {


                }
            }

            @Override
            public void onFailure(Call<SendMessageDualRespo> call, Throwable t) {

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });

        this.etMobile.setText("");


    }


    private void scrollToBottom() {

        if (chatComonModalArrayList.size() > 0) {
            chat_dual_rv.scrollToPosition(mAdapter.getItemCount() - 1);
        }
    }

    /*    *
     * Fetching all the messages of a single chat room
     * */
    private void fetchChatThread(String user_id, final String group_id) {


        Apis apis = RestAdapter.createAPI();
        Call<ChatStatusDoubleRespo> callbackCall = apis.checkchatstatusdouble(user_id, group_id);
        callbackCall.enqueue(new Callback<ChatStatusDoubleRespo>() {
            @Override
            public void onResponse(Call<ChatStatusDoubleRespo> call, Response<ChatStatusDoubleRespo> response) {
                ChatStatusDoubleRespo resp = response.body();
                if (resp != null && !resp.getError()) {

//                    opponentmessageArrayList.clear();

                    Log.d("RESPOCHAT", new Gson().toJson(resp));

                    ChatDualModal chatDualModal = new ChatDualModal(
                            resp.getMid(),
                            resp.getName(),
                            resp.getMsg(),
                            resp.getTime()

                    );

                    chatComonModalArrayList.add(chatDualModal);

                    mAdapter.notifyDataSetChanged();
                    scrollToBottom();


                } else {


                }
            }

            @Override
            public void onFailure(Call<ChatStatusDoubleRespo> call, Throwable t) {

                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });


    }


    private void userProfiledialog(String id, String level_id) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    private void showuserDetail(String user_id, ImageView oopimg, TextView oppname, TextView age, TextView points) {

        loader_dialog(this);
        Apis apis = RestAdapter.createAPI();
        Call<OpponentsResponse> callbackCall = apis.getopponetinfo(user_id);
        callbackCall.enqueue(new Callback<OpponentsResponse>() {
            @Override
            public void onResponse(Call<OpponentsResponse> call, Response<OpponentsResponse> response) {
                OpponentsResponse resp = response.body();
                if (resp != null && !resp.getError()) {


                    loader_dialog.dismiss();
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

                    loader_dialog.dismiss();

//                    dialogServerNotConnect(Activity_Login.this,"NOT CONNECTED TO SERVER");
                }
            }

            @Override
            public void onFailure(Call<OpponentsResponse> call, Throwable t) {

                loader_dialog.dismiss();
                Log.e("onFailure", t.getMessage());
//                dialogServerNotConnect(Activity_Login.this,t.getMessage());
            }
        });
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




    public void showUserProfilePopup(View v, String id, String level_id) {


        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.dialog_users_profile_dialog, null);



        Typeface typeface = Typeface.createFromAsset(this.getAssets(),
                "fonts/Mark Simonson - Proxima Nova Semibold.otf");

        Typeface face2 = Typeface.createFromAsset(getAssets(),
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




}
