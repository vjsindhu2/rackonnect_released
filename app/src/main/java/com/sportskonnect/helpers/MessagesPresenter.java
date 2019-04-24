package com.sportskonnect.helpers;


import android.annotation.SuppressLint;

import com.sportskonnect.Activity_TournamentChat;
import com.sportskonnect.api.Constant;
import com.sportskonnect.interfaces.Presenter;
import com.sportskonnect.modal.ConversationsModel;
import com.sportskonnect.modal.Pusher;
import com.sportskonnect.services.MessagesService;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;

import de.greenrobot.event.EventBus;
import io.realm.Realm;


public class MessagesPresenter implements Presenter {
    private final Activity_TournamentChat view;
    private final Realm realm;
    private int RecipientID, ConversationID, GroupID;
    private Boolean isGroup;
    private MessagesService mMessagesService;

    public MessagesPresenter(Activity_TournamentChat messagesActivity) {
        this.view = messagesActivity;
        this.realm = Realm.getDefaultInstance();
    }


    @Override
    public void onStart() {

    }

    @Override
    public void onCreate() {

        RecipientID = Integer.parseInt(SharedPreference.getSharedPreferenceString(view,SharedprefKeys.OPPONENT_ID,""));


        if (!EventBus.getDefault().isRegistered(view)) EventBus.getDefault().register(view);
        if (view.getIntent().getExtras() != null) {
            if (view.getIntent().hasExtra("conversationID")) {
                ConversationID = view.getIntent().getExtras().getInt("conversationID");
            }


            if (view.getIntent().hasExtra("recipientID")) {
//                RecipientID = view.getIntent().getExtras().getInt("recipientID");
                RecipientID = Integer.parseInt(SharedPreference.getSharedPreferenceString(view,SharedprefKeys.OPPONENT_ID,""));
            }

            if (view.getIntent().hasExtra("groupID")) {
                GroupID = view.getIntent().getExtras().getInt("groupID");
            }

            if (view.getIntent().hasExtra("isGroup")) {
                isGroup = view.getIntent().getExtras().getBoolean("isGroup");
            }

        }

//        APIService mApiService = APIService.with(view.getApplicationContext());
        mMessagesService = new MessagesService(realm);
//        ContactsService mContactsService = new ContactsService(realm, view.getApplicationContext(), mApiService);

/*
        if (isGroup) {
            mMessagesService.getContact(PreferenceManager.getID(view)).subscribe(view::updateContact, view::onErrorLoading);
            mMessagesService.getGroupInfo(GroupID).subscribe(view::updateGroupInfo, view::onErrorLoading);
            updateConversationStatus();
            loadLocalGroupData();
        } else {

*/
//////            mMessagesService.getContact(PreferenceManager.getID(view)).subscribe(view::updateContact, view::onErrorLoading);
////            try {
////                mMessagesService.getContact(RecipientID).subscribe(view::updateContactRecipient, view::onErrorLoading);
////            } catch (Exception e) {
////                AppHelper.LogCat(" " + e.getMessage());
////            }
//            mContactsService.getContactInfo(RecipientID).subscribe(view::updateContactRecipient, view::onErrorLoading);
            updateConversationStatus();
            loadLocalData();
//        }

    }

    public void updateConversationStatus() {
        try {
            realm.executeTransaction(realm1 -> {
                ConversationsModel conversationsModel1 = realm1.where(ConversationsModel.class).equalTo("id", ConversationID).findFirst();
                if (conversationsModel1 != null) {
                    conversationsModel1.setStatus(AppConstants.IS_SEEN);
                    conversationsModel1.setUnreadMessageCounter("0");
                    realm1.copyToRealmOrUpdate(conversationsModel1);
                    EventBus.getDefault().post(new Pusher("MessagesCounter"));
                }
            });
        } catch (Exception e) {
            Constant.LogCat("There is no conversation unRead MessagesPresenter ");
        }
    }

    @SuppressLint("CheckResult")
    public void loadLocalGroupData() {
//        if (NotificationsManager.getManager())
//            NotificationsManager.cancelNotification(GroupID);
        mMessagesService.getConversation(ConversationID).subscribe(view::ShowMessages, view::onErrorLoading, view::onHideLoading);
    }

    @SuppressLint("CheckResult")
    public void loadLocalData() {
//        if (NotificationsManager.getManager())
//            NotificationsManager.cancelNotification(RecipientID);
        mMessagesService.getConversation(ConversationID, RecipientID, Integer.parseInt(SharedPreference.getSharedPreferenceString(view.getApplicationContext(),SharedprefKeys.USER_ID,""))).subscribe(view::ShowMessages, view::onErrorLoading);

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(view);
        realm.close();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onStop() {

    }

}
