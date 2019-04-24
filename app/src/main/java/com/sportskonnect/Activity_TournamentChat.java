package com.sportskonnect;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.sportskonnect.activities.ChatHistoryActivity;
import com.sportskonnect.activities.CreateMatch;
import com.sportskonnect.adapters.ChatRoomThreadAdapter;
import com.sportskonnect.adapters.MessagesAdapter;
import com.sportskonnect.adapters.TextWatcherAdapter;
import com.sportskonnect.api.Apis;
import com.sportskonnect.api.Callbacks.InsertChatAdapter;
import com.sportskonnect.api.Callbacks.InsertFullChat;
import com.sportskonnect.api.Constant;
import com.sportskonnect.api.RestAdapter;
import com.sportskonnect.app.WhatsCloneApplication;
import com.sportskonnect.helpers.AppConstants;
import com.sportskonnect.helpers.MessagesPresenter;
import com.sportskonnect.helpers.PreferenceSettingsManager;
import com.sportskonnect.helpers.UtilsTime;
import com.sportskonnect.interfaces.LoadingData;
import com.sportskonnect.modal.AllChatConversation;
import com.sportskonnect.modal.ChatComonModal;
import com.sportskonnect.modal.ConversationsModel;
import com.sportskonnect.modal.MessagesModel;
import com.sportskonnect.modal.Pusher;
import com.sportskonnect.services.MainService;
import com.sportskonnect.services.MessagesService;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sportskonnect.helpers.UtilsString.escapeJavaString;
import static com.sportskonnect.services.MainService.mSocket;

public class Activity_TournamentChat extends AppCompatActivity implements LoadingData, View.OnClickListener {

    public Timer timer = new Timer();
    TextView main_text_title;
    Typeface face;
    ImageView ivSend;
    RecyclerView chat_recycler;
    MessagesPresenter mMessagesPresenter = new MessagesPresenter(this);
    private String TAG = Activity_TournamentChat.class.getSimpleName();
    private String chatRoomId;
    private ChatRoomThreadAdapter mAdapter;
    private ArrayList<InsertChatAdapter> messageArrayList = new ArrayList<>();
    private ArrayList<ChatComonModal> chatComonModalArrayList = new ArrayList<>();
    private ArrayList<InsertChatAdapter> messageArrayListlocal = new ArrayList<>();
    private ArrayList<AllChatConversation> allChatConversationArrayList = new ArrayList<>();
    private ArrayList<InsertChatAdapter> opponentmessageArrayList = new ArrayList<>();
    private SharedPreference sharedprefrence;
    private String selfUserId;
    private String opponet_id;
    private EmojiconEditText et_chatmsg;
    private TextView opponet_name, create_match;
    private Realm realm;
    private int ConversationID = 65;
    private MessagesAdapter mMessagesAdapter;
    private MessagesService mMessagesService;
    private TextView lastVu;
    private ActionMode actionMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournamentchat);
        sharedprefrence = new SharedPreference(this);
        realm = Realm.getDefaultInstance();
        mMessagesService = new MessagesService(realm);

        selfUserId = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, "");
        opponet_id = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.OPPONENT_ID, "");


        connectToChatServer();


        JSONObject json = new JSONObject();
        try {
            json.put("connected", true);
            json.put("senderId", Integer.parseInt(opponet_id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit(AppConstants.SOCKET_IS_ONLINE, json);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        init();
        mMessagesPresenter.onCreate();


    }


    @Override
    public void onShowLoading() {

    }

    @Override
    public void onHideLoading() {

    }

    @Override
    public void onBackPressed() {
        if (SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CHATCOMEFROM, "0").equals("0")) {

            Intent intent = new Intent(Activity_TournamentChat.this, Activity_Badminton.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(Activity_TournamentChat.this, ChatHistoryActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    public void onErrorLoading(Throwable throwable) {
        Constant.LogCat("Messages " + throwable.getMessage());
    }


    public void finishActivity(View view) {

        if (SharedPreference.getSharedPreferenceString(this, SharedprefKeys.CHATCOMEFROM, "0").equals("0")) {

            Intent intent = new Intent(Activity_TournamentChat.this, Activity_Badminton.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(Activity_TournamentChat.this, ChatHistoryActivity.class);
            startActivity(intent);
            finish();

        }


    }

    @SuppressLint("WrongConstant")
    private void init() {

        opponet_name = (TextView) findViewById(R.id.opponet_name);
        create_match = (TextView) findViewById(R.id.create_match);
        lastVu = (TextView) findViewById(R.id.lastVu);
        et_chatmsg = (EmojiconEditText) findViewById(R.id.et_chatmsg);
        face = Typeface.createFromAsset(getAssets(),
                "fonts/Calibre Bold.otf");

        opponet_name.setTypeface(face);
        et_chatmsg.setTypeface(face);

        ivSend = (ImageView) findViewById(R.id.ivSend);
        ivSend.setOnClickListener(this);

        chat_recycler = (RecyclerView) findViewById(R.id.recycler_view);


        opponet_name.setText(sharedprefrence.getSharedPreferenceString(this, SharedprefKeys.OPPONENT_NAME, ""));


        mMessagesAdapter = new MessagesAdapter(this, realm);

        LinearLayoutManager layoutManager = new LinearLayoutManager(WhatsCloneApplication.getAppContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        chat_recycler.setLayoutManager(layoutManager);
        chat_recycler.setAdapter(mMessagesAdapter);
        chat_recycler.setItemAnimator(new DefaultItemAnimator());

        scrollToBottom();

/*        @SuppressLint("WrongConstant") LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        chat_recycler.setLayoutManager(layoutManager);
        chat_recycler.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new ChatRoomThreadAdapter(Activity_TournamentChat.this, chatComonModalArrayList, selfUserId);

        chat_recycler.setAdapter(mAdapter);*/



//        fetchAllchat(selfUserId, opponet_id);

//        fetchAllchat(selfUserId, opponet_id, true);

        create_match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreference.setSharedPreferenceString(Activity_TournamentChat.this, SharedprefKeys.MATCH_CREATED_FROM, "1");
                Intent intent = new Intent(Activity_TournamentChat.this, CreateMatch.class);
                startActivity(intent);
            }
        });

//        startTimer();
//        mMessagesPresenter.loadLocalData();


        et_chatmsg.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

        });

        et_chatmsg.addTextChangedListener(new TextWatcherAdapter() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (PreferenceSettingsManager.enter_send(Activity_TournamentChat.this)) {
                    et_chatmsg.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
                    et_chatmsg.setSingleLine(true);
                    et_chatmsg.setOnEditorActionListener((v, actionId, event) -> {
                        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_SEND)) {
                            sendMessagesocket(selfUserId, opponet_id);
                        }
                        return false;
                    });
                }

                if (et_chatmsg.getText().length()>0){

                    ivSend.setAlpha(1f);
                    ivSend.setClickable(true);
                }else {
                    ivSend.setAlpha(0.5f);
                    ivSend.setClickable(false);
                }

            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    //stop the timer
    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
//            timer.purge();

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (timer != null) {
//            startTimer();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSend:


                if (et_chatmsg.getText().length() > 0) {


                    sendMessagesocket(selfUserId, opponet_id);

                } else {

                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMessagesPresenter.onDestroy();
        realm.close();
    }


    @Override
    protected void onStop() {
        super.onStop();
//        stopTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
//            startTimer();
        }

    }


    /**
     * method to send the new message
     */
    private void sendMessagesocket(String user_id, String opponet_id) {
//        EventBus.getDefault().post(new Pusher("startConversation"));//for change viewpager current item to 0


        String messageBody = escapeJavaString(et_chatmsg.getText().toString());
        /*if (messageTransfer != null)
            messageBody = messageTransfer;*/

/*        if (FileImagePath == null && FileAudioPath == null && FileDocumentPath == null && FileVideoPath == null) {
            if (messageBody.isEmpty()) return;
        }*/
        Calendar current = Calendar.getInstance();
        String sendTime = String.valueOf(current.getTime());

        final JSONObject message = new JSONObject();
        try {
            message.put("messageBody", messageBody);
            message.put("recipientId", Integer.parseInt(opponet_id));
            message.put("senderId", Integer.parseInt(user_id));
            try {
                if (!SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_NAME, "").equals("")) {
                    message.put("senderName", SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_NAME, ""));
                } else {
                    message.put("senderName", "null");
                }
                if (!SharedPreference.getSharedPreferenceString(this, SharedprefKeys.PROFILE_PIC_URL, "").equals(""))
                    message.put("senderImage", SharedPreference.getSharedPreferenceString(this, SharedprefKeys.PROFILE_PIC_URL, ""));
                else
                    message.put("senderImage", "null");
                message.put("phone", SharedPreference.getSharedPreferenceString(this, SharedprefKeys.OPPONENT_PHONE, ""));
            } catch (Exception e) {
                Log.d("Exception", e.getMessage());
            }


            message.put("date", sendTime);
            message.put("isGroup", false);
            message.put("conversationId", 0);

        } catch (JSONException e) {
            Log.d("Exception", e.getMessage());

        }
        unSentMessagesForARecipient(Integer.parseInt(opponet_id));
        new Handler().postDelayed(() -> runOnUiThread(() -> setStatusAsWaiting(message)), 100);

        et_chatmsg.setText("");
        messageBody = null;
        /*messageTransfer = null;*/


    }


    private int getConversationId(int recipientId, int senderId, Realm realm) {
        try {
            ConversationsModel conversationsModelNew = realm.where(ConversationsModel.class)
                    .beginGroup()
                    .equalTo("RecipientID", recipientId)
                    .or()
                    .equalTo("RecipientID", senderId)
                    .endGroup().findFirst();
            return conversationsModelNew.getId();
        } catch (Exception e) {
            return 0;
        }
    }

    private void setStatusAsWaiting(JSONObject data) {

        try {
            int senderId = data.getInt("senderId");
            int recipientId = data.getInt("recipientId");
            String messageBody = data.getString("messageBody");
            String senderName = data.getString("senderName");
            String dateTmp = data.getString("date");
            String phone = data.getString("phone");

            String recipientName = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_NAME, "");
            String recipientImage = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.PROFILE_PIC_URL, "");
            String recipientPhone = SharedPreference.getSharedPreferenceString(this, SharedprefKeys.MOBILE_NO, "");
            int conversationID = getConversationId(recipientId, senderId, realm);
            if (conversationID == 0) {
                realm.executeTransactionAsync(realm1 -> {
                    int lastConversationID = 1;
                    int lastID = 1;
                    try {
                        ConversationsModel conversationsModel = realm1.where(ConversationsModel.class).findAll().last();
                        lastConversationID = conversationsModel.getId() + 1;

                        MessagesModel messagesModel1 = realm1.where(MessagesModel.class).findAll().last();
                        lastID = messagesModel1.getId() + 1;

                        Constant.LogCat("last ID  message  MessagesActivity " + lastID);

                    } catch (Exception e) {
                        Constant.LogCat("last conversation  ID  if conversation id = 0 Exception MessagesActivity " + e.getMessage());
                        lastConversationID = 1;
                    }
                    RealmList<MessagesModel> messagesModelRealmList = new RealmList<MessagesModel>();
                    MessagesModel messagesModel = new MessagesModel();
                    messagesModel.setId(lastID);
                    messagesModel.setUsername(senderName);
                    messagesModel.setRecipientID(recipientId);
                    messagesModel.setDate(dateTmp);
                    messagesModel.setStatus(AppConstants.IS_WAITING);
                    messagesModel.setGroup(false);
                    messagesModel.setSenderID(senderId);
                    messagesModel.setConversationID(lastConversationID);
                    messagesModel.setMessage(messageBody);
                    messagesModel.setPhone(phone);
                    messagesModelRealmList.add(messagesModel);
                    ConversationsModel conversationsModel1 = new ConversationsModel();
                    conversationsModel1.setRecipientID(recipientId);
                    conversationsModel1.setLastMessage(messageBody);
                    conversationsModel1.setRecipientUsername(recipientName);
                    conversationsModel1.setRecipientImage(recipientImage);
                    conversationsModel1.setMessageDate(dateTmp);
                    conversationsModel1.setId(lastConversationID);
                    conversationsModel1.setStatus(AppConstants.IS_WAITING);
                    conversationsModel1.setRecipientPhone(recipientPhone);
                    conversationsModel1.setMessages(messagesModelRealmList);
                    conversationsModel1.setUnreadMessageCounter("0");
                    conversationsModel1.setLastMessageId(lastID);
                    conversationsModel1.setCreatedOnline(true);
                    realm1.copyToRealmOrUpdate(conversationsModel1);
                    ConversationID = lastConversationID;

                    runOnUiThread(() -> addMessage(messagesModel));
                    try {
                        data.put("messageId", lastID);
                    } catch (JSONException e) {
                        Constant.LogCat("last id");
                    }
                }, () -> {

                    mSocket.emit(AppConstants.SOCKET_NEW_MESSAGE, data);
                    mSocket.emit(AppConstants.SOCKET_SAVE_NEW_MESSAGE, data);
                }, error -> Constant.LogCat("Error  conversation id MessagesActivity " + error.getMessage()));


            } else {

                realm.executeTransactionAsync(realm1 -> {
                    try {


                        MessagesModel messagesModel1 = realm1.where(MessagesModel.class).findAll().last();
                        int lastID = messagesModel1.getId() + 1;

                        Constant.LogCat("last ID  message   MessagesActivity" + lastID);
                        ConversationsModel conversationsModel;
                        RealmQuery<ConversationsModel> conversationsModelRealmQuery = realm1.where(ConversationsModel.class).equalTo("id", conversationID);
                        conversationsModel = conversationsModelRealmQuery.findAll().first();
                        MessagesModel messagesModel = new MessagesModel();
                        messagesModel.setId(lastID);
                        messagesModel.setUsername(senderName);
                        messagesModel.setRecipientID(recipientId);
                        messagesModel.setDate(dateTmp);
                        messagesModel.setStatus(AppConstants.IS_WAITING);
                        messagesModel.setGroup(false);
                        messagesModel.setSenderID(senderId);
                        messagesModel.setConversationID(conversationID);
                        messagesModel.setMessage(messageBody);
                        messagesModel.setPhone(phone);
                        conversationsModel.getMessages().add(messagesModel);
                        conversationsModel.setLastMessageId(lastID);
                        conversationsModel.setLastMessage(messageBody);
                        conversationsModel.setMessageDate(dateTmp);
                        conversationsModel.setCreatedOnline(true);
                        realm1.copyToRealmOrUpdate(conversationsModel);
                        runOnUiThread(() -> addMessage(messagesModel));
                        try {
                            data.put("messageId", lastID);
                        } catch (JSONException e) {
                            Constant.LogCat("last id");
                        }
                    } catch (Exception e) {
                        Constant.LogCat("Exception  last id message  MessagesActivity " + e.getMessage());
                    }
                }, () -> {
                    mSocket.emit(AppConstants.SOCKET_NEW_MESSAGE, data);
                    mSocket.emit(AppConstants.SOCKET_SAVE_NEW_MESSAGE, data);
                }, error -> Constant.LogCat("Error  last id  MessagesActivity " + error.getMessage()));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    /**
     * method of EventBus
     *
     * @param pusher this is parameter of onEventMainThread method
     */
    @SuppressWarnings("unused")
    public void onEventMainThread(Pusher pusher) {


        switch (pusher.getAction()) {
            case "new_message":
                MessagesModel messagesModel = pusher.getMessagesModel();
                if (messagesModel.getSenderID() == Integer.parseInt(opponet_id) && messagesModel.getRecipientID() == Integer.parseInt(selfUserId)) {
                    new Handler().postDelayed(() -> addMessage(messagesModel), 1000);
                    mMessagesPresenter.updateConversationStatus();
                }
/*                if (!checkIfUserIsExist(Integer.parseInt(opponet_id), realm)) {
                    mMessagesPresenter.loadLocalGroupData();
                }*/

                break;

            case "messages_delivered":
//                if (isGroup) {
//                    mMessagesPresenter.loadLocalGroupData();
//                } else {
                mMessagesPresenter.loadLocalData();
//                }

                break;
            case "new_message_sent":
//                if (isGroup) {
//                    mMessagesPresenter.loadLocalGroupData();
//                } else {
                mMessagesPresenter.loadLocalData();
//                }
                break;
            case "messages_seen":
//                if (isGroup) {
//                    mMessagesPresenter.loadLocalGroupData();
//                } else {
                mMessagesPresenter.loadLocalData();
//                }

                break;
            case "ItemIsActivatedMessages":
                Constant.LogCat("here it ");
                int idx = chat_recycler.getChildAdapterPosition(pusher.getView());
                if (actionMode != null) {
                    ToggleSelection(idx);
                    return;
                }
                break;


        }
    }

    /**
     * method to toggle the selection
     *
     * @param position this is parameter for  ToggleSelection method
     */
    private void ToggleSelection(int position) {
        mMessagesAdapter.toggleSelection(position);
        String title = String.format("%s selected", mMessagesAdapter.getSelectedItemCount());
        actionMode.setTitle(title);
    }

    /**
     * method to check if user is exist
     *
     * @param id    this is the first parameter for checkIfUserIsExist method
     * @param realm this is the second parameter for checkIfUserIsExist method
     * @return this is for what checkIfUserIsExist method will return
     */
    private boolean checkIfUserIsExist(int id, Realm realm) {
//        RealmQuery<ContactsModel> query = realm.where(ContactsModel.class).equalTo("id", id);
//        return query.count() == 0 ? false : true;

        return true;
    }



    private void scrollToBottom() {

        chat_recycler.scrollToPosition(mMessagesAdapter.getItemCount() - 1);

    }


    /**
     * method to show all user messages
     *
     * @param messagesModels this is parameter for ShowMessages method
     */
    public void ShowMessages(List<MessagesModel> messagesModels) {

        RealmList<MessagesModel> mMessagesList = new RealmList<MessagesModel>();
        for (MessagesModel messagesModel : messagesModels) {
            mMessagesList.add(messagesModel);
            ConversationID = messagesModel.getConversationID();
        }
        mMessagesAdapter.setMessages(mMessagesList);
    }



    /**
     * method to check for unsent user messages
     *
     * @param recipientID this is parameter of unSentMessagesForARecipient method
     */
    private void unSentMessagesForARecipient(int recipientID) {
        Realm realm = Realm.getDefaultInstance();
        List<MessagesModel> messagesModelsList = realm.where(MessagesModel.class)
                .equalTo("status", AppConstants.IS_WAITING)
                .equalTo("recipientID", recipientID)
                .equalTo("isGroup", false)
                .equalTo("senderID", Integer.parseInt(SharedPreference.getSharedPreferenceString(this, SharedprefKeys.USER_ID, "")))
                .sort("id", Sort.ASCENDING).findAll();

        for (MessagesModel messagesModel : messagesModelsList) {
            MainService.sendMessages(messagesModel);
        }
        realm.close();

    }

    private void addMessage(MessagesModel newMsg) {
        mMessagesAdapter.addMessage(newMsg);
        scrollToBottom();
    }


    /**
     * method to connect to the chat sever by socket
     */
    private void connectToChatServer() {
        mSocket = MainService.mSocket;
        if (mSocket == null) {
            MainService.connectToServer();
            mSocket = MainService.mSocket;

            Log.d("FIF", "RUN");
        }
        if (mSocket == null) {
            return;
        }
        if (!mSocket.connected()) {
            MainService.connectToServer();
            mSocket = MainService.mSocket;
        }

        setTypingEvent();
        checkIfUserIsOnline();


/*
        setTypingEvent();
        if (isGroup) {
            AppHelper.LogCat("here group seen");
        } else {
            checkIfUserIsOnline();
            emitMessageSeen();
        }
*/


    }


    /**
     * method to update user status
     *
     * @param statusUserTyping this is the first parameter for  updateUserStatus method
     * @param lastTime         this is the second parameter for  updateUserStatus method
     */
    private void updateUserStatus(int statusUserTyping, String lastTime) {


        switch (statusUserTyping) {
            case AppConstants.STATUS_USER_TYPING:
                lastVu.setVisibility(View.VISIBLE);
                lastVu.setText(getString(R.string.isTyping));
                Constant.LogCat("typing...");
                break;
            case AppConstants.STATUS_USER_DISCONNECTED:
                lastVu.setVisibility(View.VISIBLE);
                lastVu.setText(getString(R.string.isOffline));
                Constant.LogCat("Offline...");
                break;
            case AppConstants.STATUS_USER_CONNECTED:
                lastVu.setVisibility(View.VISIBLE);
                lastVu.setText(getString(R.string.isOnline));
                Constant.LogCat("Online...");
                break;
            case AppConstants.STATUS_USER_STOP_TYPING:
                break;
            case AppConstants.STATUS_USER_LAST_SEEN:
                lastVu.setVisibility(View.VISIBLE);
                lastVu.setText(getString(R.string.lastSeen) + " " + lastTime);
                break;
            default:
                lastVu.setVisibility(View.VISIBLE);
                lastVu.setText(getString(R.string.isOffline));
                break;
        }
    }


    /**
     * method to check if user is online or not
     */
    private void checkIfUserIsOnline() {
        mSocket.on(AppConstants.SOCKET_IS_ONLINE, args -> runOnUiThread(() -> {
            final JSONObject data = (JSONObject) args[0];
            try {
                int senderID = data.getInt("senderId");
                Log.d("DATA", senderID + "--CONNECTED");


                if (senderID == Integer.parseInt(opponet_id)) {
                    if (data.getBoolean("connected")) {
                        updateUserStatus(AppConstants.STATUS_USER_CONNECTED, null);
                        unSentMessagesForARecipient(Integer.parseInt(opponet_id));

                        Log.d("DATA", new Gson().toJson(data) + "--CONNECTED");
                    } else {
                        updateUserStatus(AppConstants.STATUS_USER_DISCONNECTED, null);

                        Log.d("DATA", senderID + "--DISCONNECTED");


                    }
                }
            } catch (JSONException e) {
                Constant.LogCat(e.getMessage());
            }

        }));
    }


    /**
     * method to set user typing event
     */
    private void setTypingEvent() {
        mSocket.on(AppConstants.SOCKET_IS_TYPING, args -> runOnUiThread(() -> {

            JSONObject data = (JSONObject) args[0];
            try {

                int senderID = data.getInt("senderId");
                int recipientID = data.getInt("recipientId");
                if (senderID == Integer.parseInt(opponet_id) && recipientID == Integer.parseInt(selfUserId)) {
                    updateUserStatus(AppConstants.STATUS_USER_TYPING, null);
                }

            } catch (Exception e) {
                Constant.LogCat(e.getMessage());
            }
        }));

        mSocket.on(AppConstants.SOCKET_IS_STOP_TYPING, args -> runOnUiThread(() -> {
            try {
                JSONObject json = new JSONObject();
                try {
                    json.put("connected", true);
                    json.put("senderId", Integer.parseInt(selfUserId));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSocket.emit(AppConstants.SOCKET_IS_ONLINE, json);
            } catch (Exception e) {
                Constant.LogCat(e.getMessage());
            }
        }));
        mSocket.on(AppConstants.SOCKET_IS_LAST_SEEN, args -> runOnUiThread(() -> {
            JSONObject data = (JSONObject) args[0];
            try {
                int senderID = data.getInt("senderId");
                int recipientID = data.getInt("recipientId");
                String lastSeen = data.getString("lastSeen");
                if (senderID == Integer.parseInt(opponet_id) && recipientID == Integer.parseInt(selfUserId)) {
                    String lastTime = UtilsTime.getTimeStamp(lastSeen);
                    updateUserStatus(AppConstants.STATUS_USER_LAST_SEEN, lastTime);
                }
            } catch (Exception e) {
                Constant.LogCat(e.getMessage());
            }
        }));
    }


    @Override
    protected void onPause() {
        super.onPause();
        LastSeenTimeEmit();
    }


    /**
     * method to emit last seen of conversation
     */
    private void LastSeenTimeEmit() {
        Calendar current = Calendar.getInstance();
        String lastTime = String.valueOf(current.getTime());
        JSONObject data = new JSONObject();
        try {
            data.put("senderId", Integer.parseInt(selfUserId));
            data.put("recipientId", Integer.parseInt(opponet_id));
            data.put("lastSeen", lastTime);
        } catch (JSONException e) {
            Constant.LogCat(e.getMessage());
        }
        mSocket.emit(AppConstants.SOCKET_IS_LAST_SEEN, data);
    }

}
