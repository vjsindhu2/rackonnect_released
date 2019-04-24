package com.sportskonnect.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.sportskonnect.R;
import com.sportskonnect.helpers.AppConstants;
import com.sportskonnect.modal.ConversationsModel;
import com.sportskonnect.modal.MessagesModel;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.Sort;

import static com.sportskonnect.helpers.UtilsString.escapeJavaString;


public class SingleChatWindow extends AppCompatActivity implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {

    ImageButton emoticonBtn;
    private boolean emoticonShown = false;
    FrameLayout emojicons;
    private EmojiconEditText messageWrapper;
    private ImageButton send_button;
    private String messageTransfer = null;
    private SharedPreference sharedPreference;
    private Realm realm;
    private int ConversationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat_window);
        realm = Realm.getDefaultInstance();

        sharedPreference = new SharedPreference(this);

        emoticonBtn = (ImageButton) findViewById(R.id.emoticonBtn);
        emojicons = (FrameLayout) findViewById(R.id.emojicons);
        messageWrapper = (EmojiconEditText) findViewById(R.id.messageWrapper);
        send_button = (ImageButton) findViewById(R.id.send_button);

        setEmojIconFragment();

        messageWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emoticonShown) {
                    emoticonShown = false;
                    emojicons.setVisibility(View.GONE);

                }
            }
        });


        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        emoticonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emoticonShown) {
                    emoticonShown = true;
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        final Animation animation = AnimationUtils.loadAnimation(SingleChatWindow.this, R.anim.push_up_in);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                emojicons.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        emojicons.startAnimation(animation);
                    }
                }else {

                }
            }


        });

    }


    /**
     * method to setup EmojIcon Fragment
     */
    private void setEmojIconFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, EmojiconsFragment.newInstance(false))
                .commit();
    }



    /**
     * method to send the new message
     */
    private void sendMessage() {
//        EventBus.getDefault().post(new Pusher("startConversation"));//for change viewpager current item to 0

        String messageBody = escapeJavaString(messageWrapper.getText().toString());

        if (messageTransfer != null)
            messageBody = messageTransfer;


        Calendar current = Calendar.getInstance();
        String sendTime = String.valueOf(current.getTime());


            final JSONObject message = new JSONObject();
            try {
                message.put("messageBody", messageBody);
                message.put("recipientId", sharedPreference.getSharedPreferenceString(this,SharedprefKeys.OPPONENT_ID,""));
                message.put("senderId", sharedPreference.getSharedPreferenceString(this,SharedprefKeys.USER_ID,""));
                try {
                    if (sharedPreference.getSharedPreferenceString(this,SharedprefKeys.USER_NAME,"") != null) {
                        message.put("senderName", sharedPreference.getSharedPreferenceString(this,SharedprefKeys.USER_NAME,""));
                    } else {
                        message.put("senderName", "null");
                    }

                    message.put("phone", sharedPreference.getSharedPreferenceString(this,SharedprefKeys.MOBILE_NO,""));
                } catch (Exception e) {
                    Log.d("EXCEPTION", e.getMessage());
                }


                message.put("date", sendTime);
                message.put("isGroup", false);
                message.put("conversationId", SharedPreference.getSharedPreferenceString(this,SharedprefKeys.CONVERSATION_ID,""));


            } catch (JSONException e) {
                Log.d("EXCEPTION", e.getMessage());
            }
            unSentMessagesForARecipient(sharedPreference.getSharedPreferenceString(this,SharedprefKeys.OPPONENT_ID,""));
            new Handler().postDelayed(() -> runOnUiThread(() -> setStatusAsWaiting(message, false)), 100);

        messageWrapper.setText("");
        messageBody = null;
        messageTransfer = null;

    }


    /**
     * method to get a conversation id
     *
     * @param recipientId this is the first parameter for getConversationId method
     * @param senderId    this is the second parameter for getConversationId method
     * @param realm       this is the thirded parameter for getConversationId method
     * @return conversation id
     */
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
//            AppHelper.LogCat("Get conversation id Exception MessagesActivity " + e.getMessage());
            return 0;
        }
    }



    /**
     * method to save new message as waitng messages
     *
     * @param data    this is the first parameter for setStatusAsWaiting method
     * @param isgroup this is the second parameter for setStatusAsWaiting method
     */
    @SuppressLint("LongLogTag")
    private void setStatusAsWaiting(JSONObject data, boolean isgroup) {

        try {
                int senderId = data.getInt("senderId");
                int recipientId = data.getInt("recipientId");
                String messageBody = data.getString("messageBody");
                String senderName = data.getString("senderName");
                String dateTmp = data.getString("date");
                String video = data.getString("video");
                String thumbnail = data.getString("thumbnail");
                boolean isGroup = data.getBoolean("isGroup");
                String image = data.getString("image");
                String audio = data.getString("audio");
                String document = data.getString("document");
                String phone = data.getString("phone");

                String recipientName = sharedPreference.getSharedPreferenceString(this,SharedprefKeys.OPPONENT_NAME,"");
                String recipientImage = sharedPreference.getSharedPreferenceString(this,SharedprefKeys.OPPONENT_IMAGE,"");
                String recipientPhone = sharedPreference.getSharedPreferenceString(this,SharedprefKeys.OPPONENT_PHONE,"");
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

//                            AppHelper.LogCat("last ID  message  MessagesActivity " + lastID);

                        } catch (Exception e) {
//                            AppHelper.LogCat("last conversation  ID  if conversation id = 0 Exception MessagesActivity " + e.getMessage());
                            lastConversationID = 1;
                        }
                        RealmList<MessagesModel> messagesModelRealmList = new RealmList<MessagesModel>();
                        MessagesModel messagesModel = new MessagesModel();
                        messagesModel.setId(lastID);
                        messagesModel.setUsername(senderName);
                        messagesModel.setRecipientID(recipientId);
                        messagesModel.setDate(dateTmp);
                        messagesModel.setStatus(AppConstants.IS_WAITING);
                        messagesModel.setGroup(isGroup);
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
//                            AppHelper.LogCat("last id");
                        }
                    }, () -> {
                        if (!image.equals("null") || !video.equals("null") || !audio.equals("null") || !document.equals("null") || !thumbnail.equals("null"))
                            return;
//                        mSocket.emit(AppConstants.SOCKET_NEW_MESSAGE, data);
//                        mSocket.emit(AppConstants.SOCKET_SAVE_NEW_MESSAGE, data);
                    }, error ->
                            Log.d("Error  conversation id MessagesActivity ", error.getMessage()));


                } else {

                    realm.executeTransactionAsync(realm1 -> {
                        try {


                            MessagesModel messagesModel1 = realm1.where(MessagesModel.class).findAll().last();
                            int lastID = messagesModel1.getId() + 1;

//                            AppHelper.LogCat("last ID  message   MessagesActivity" + lastID);
                            ConversationsModel conversationsModel;
                            RealmQuery<ConversationsModel> conversationsModelRealmQuery = realm1.where(ConversationsModel.class).equalTo("id", conversationID);
                            conversationsModel = conversationsModelRealmQuery.findAll().first();
                            MessagesModel messagesModel = new MessagesModel();
                            messagesModel.setId(lastID);
                            messagesModel.setUsername(senderName);
                            messagesModel.setRecipientID(recipientId);
                            messagesModel.setDate(dateTmp);
                            messagesModel.setStatus(AppConstants.IS_WAITING);
                            messagesModel.setGroup(isGroup);
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
//                                AppHelper.LogCat("last id");
                            }
                        } catch (Exception e) {
//                            AppHelper.LogCat("Exception  last id message  MessagesActivity " + e.getMessage());
                        }
                    }, () -> {
                        if (!image.equals("null") || !video.equals("null") || !audio.equals("null") || !document.equals("null") || !thumbnail.equals("null"))
                            return;
//                        mSocket.emit(AppConstants.SOCKET_NEW_MESSAGE, data);
//                        mSocket.emit(AppConstants.SOCKET_SAVE_NEW_MESSAGE, data);
                    }, error ->
                            Log.d("Error  last id  MessagesActivity " , error.getMessage()));
                }



        } catch (JSONException e) {
//            AppHelper.LogCat("JSONException  MessagesActivity " + e);
        }


    }


    /**
     * method to add a new message to list messages
     *
     * @param newMsg this is the parameter for addMessage
     */

    private void addMessage(MessagesModel newMsg) {
//        mMessagesAdapter.addMessage(newMsg);
        scrollToBottom();
    }

    /**
     * method to scroll to the bottom of list
     */
    private void scrollToBottom() {
//        messagesList.scrollToPosition(mMessagesAdapter.getItemCount() - 1);
    }



    /**
     * method to check for unsent user messages
     *
     * @param recipientID this is parameter of unSentMessagesForARecipient method
     */
    private void unSentMessagesForARecipient(String recipientID) {
        Realm realm = Realm.getDefaultInstance();
        List<MessagesModel> messagesModelsList = realm.where(MessagesModel.class)
                .equalTo("status", AppConstants.IS_WAITING)
                .equalTo("recipientID", recipientID)
                .equalTo("senderID", sharedPreference.getSharedPreferenceString(SingleChatWindow.this,SharedprefKeys.USER_ID,""))
                .sort("id", Sort.ASCENDING).findAll();
        Log.d("size ", messagesModelsList.size()+"");
        Log.d("list " , messagesModelsList.toString());
        for (MessagesModel messagesModel : messagesModelsList) {

//            MainService.sendMessages(messagesModel);
        }
        realm.close();
    }






    @Override
    public void onEmojiconClicked(Emojicon emojicon) {

        EmojiconsFragment.input(messageWrapper, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {

        EmojiconsFragment.backspace(messageWrapper);
    }





}
