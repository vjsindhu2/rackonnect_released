package com.sportskonnect.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sportskonnect.R;
import com.sportskonnect.api.Constant;
import com.sportskonnect.app.WhatsCloneApplication;
import com.sportskonnect.helpers.AppConstants;
import com.sportskonnect.helpers.UtilsTime;
import com.sportskonnect.modal.MessagesModel;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmList;

import static com.sportskonnect.helpers.UtilsString.unescapeJava;


/**
 * Created by Abderrahim El imame on 20/02/2016.
 * Email : abderrahim.elimame@gmail.com
 */
public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int INCOMING_MESSAGES = 0;
    private static final int OUTGOING_MESSAGES = 1;
    private static final int LEFT_MESSAGES = 2;
    private Activity mActivity;
    private RealmList<MessagesModel> mMessagesModel;

    private Realm realm;
    private MediaPlayer mMediaPlayer;
    private Handler mHandler;
    private String SearchQuery;
    private SparseBooleanArray selectedItems;


    public MessagesAdapter(@NonNull Activity mActivity, Realm realm) {
        this.mActivity = mActivity;
        this.mMessagesModel = new RealmList<>();
        this.realm = realm;
        this.mHandler = new Handler();
        this.mMediaPlayer = new MediaPlayer();
        this.selectedItems = new SparseBooleanArray();
    }

    public void setMessages(RealmList<MessagesModel> messagesModelList) {
        this.mMessagesModel = messagesModelList;
        notifyDataSetChanged();
    }

    public void addMessage(MessagesModel messagesModel) {
        this.mMessagesModel.add(messagesModel);
        notifyItemInserted(mMessagesModel.size() - 1);
    }


    //Methods for search start
    public void setString(String SearchQuery) {
        this.SearchQuery = SearchQuery;
        notifyDataSetChanged();
    }

    public void animateTo(List<MessagesModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<MessagesModel> newModels) {
        for (int i = mMessagesModel.size() - 1; i >= 0; i--) {
            final MessagesModel model = mMessagesModel.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<MessagesModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final MessagesModel model = newModels.get(i);
            if (!mMessagesModel.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<MessagesModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final MessagesModel model = newModels.get(toPosition);
            final int fromPosition = mMessagesModel.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private MessagesModel removeItem(int position) {
        final MessagesModel model = mMessagesModel.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    private void addItem(int position, MessagesModel model) {
        mMessagesModel.add(position, model);
        notifyItemInserted(position);
    }

    private void moveItem(int fromPosition, int toPosition) {
        final MessagesModel model = mMessagesModel.remove(fromPosition);
        mMessagesModel.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
    //Methods for search end

    @Override
    public int getItemViewType(int position) {
        try {
            MessagesModel messagesModel = mMessagesModel.get(position);


            if (messagesModel.getSenderID() == Integer.parseInt(SharedPreference.getSharedPreferenceString(WhatsCloneApplication.getAppContext(), SharedprefKeys.USER_ID, ""))) {
                if (messagesModel.getMessage().equals("FK") || messagesModel.getMessage().equals("LT")) {
                    return LEFT_MESSAGES;
                } else {
                    return OUTGOING_MESSAGES;

                }
            } else {
                if (messagesModel.getMessage().equals("FK") || messagesModel.getMessage().equals("LT")) {
                    return LEFT_MESSAGES;
                } else {
                    return INCOMING_MESSAGES;
                }

            }

        } catch (Exception e) {
            Constant.LogCat("kdoub rminin Exception" + e.getMessage());
            return 0;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == INCOMING_MESSAGES) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.chat_item_other, parent, false);
            return new MessagesViewHolder(view);
        } else if (viewType == OUTGOING_MESSAGES) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.chat_item_self, parent, false);
            return new MessagesViewHolder(view);
        } else {
            view = LayoutInflater.from(mActivity).inflate(R.layout.created_group_view, parent, false);
            return new MessagesViewHolder(view);
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MessagesViewHolder) {
            MessagesViewHolder mMessagesViewHolder = (MessagesViewHolder) holder;
            MessagesModel messagesModel = this.mMessagesModel.get(position);


/*
            ContactsModel SenderInfo = realm.where(ContactsModel.class).equalTo("id", messagesModel.getSenderID()).findFirst();
            ContactsModel RecipientInfo = realm.where(ContactsModel.class).equalTo("id", messagesModel.getRecipientID()).findFirst();
*/

            boolean isGroup;
            isGroup = false;

            if (messagesModel.getMessage() != null) {
                String message = unescapeJava(messagesModel.getMessage());
                switch (messagesModel.getMessage()) {
                    case "FK":
                        if (isGroup) {
/*
                            if (messagesModel.getSenderID() == Integer.parseInt(SharedPreference.getSharedPreferenceString(mActivity, SharedprefKeys.USER_ID, ""))) {
                                mMessagesViewHolder.message.setText(mActivity.getString(R.string.you_created_this_group), TextView.BufferType.NORMAL);
                                mMessagesViewHolder.message.setTextSize(PreferenceSettingsManager.getMessage_font_size(mActivity));
                            } else {
                                String name = UtilsPhone.getContactName(mActivity, messagesModel.getPhone());
                                if (name != null) {
                                    mMessagesViewHolder.message.setText("" + name + mActivity.getString(R.string.he_created_this_group), TextView.BufferType.NORMAL);
                                    mMessagesViewHolder.message.setTextSize(PreferenceSettingsManager.getMessage_font_size(mActivity));
                                } else {
                                    mMessagesViewHolder.message.setText("" + messagesModel.getPhone() + mActivity.getString(R.string.he_created_this_group), TextView.BufferType.NORMAL);
                                    mMessagesViewHolder.message.setTextSize(PreferenceSettingsManager.getMessage_font_size(mActivity));
                                }

                            }
*/

                        }

                        break;
                    case "LT":

                        if (isGroup) {
                         /*   if (messagesModel.getSenderID() != Integer.parseInt(SharedPreference.getSharedPreferenceString(mActivity, SharedprefKeys.USER_ID, ""))))
                            {
                                mMessagesViewHolder.message.setText(mActivity.getString(R.string.you_left), TextView.BufferType.NORMAL);
                                mMessagesViewHolder.message.setTextSize(PreferenceSettingsManager.getMessage_font_size(mActivity));
                            } else{
                                String name = UtilsPhone.getContactName(mActivity, messagesModel.getPhone());
                                if (name != null) {
                                    mMessagesViewHolder.message.setText("" + name + mActivity.getString(R.string.he_left), TextView.BufferType.NORMAL);
                                    mMessagesViewHolder.message.setTextSize(PreferenceSettingsManager.getMessage_font_size(mActivity));
                                } else {
                                    mMessagesViewHolder.message.setText("" + messagesModel.getPhone() + mActivity.getString(R.string.he_left), TextView.BufferType.NORMAL);
                                    mMessagesViewHolder.message.setTextSize(PreferenceSettingsManager.getMessage_font_size(mActivity));
                                }


                            }
*/

                        }

                        break;
                    default:
                        SpannableString Message = SpannableString.valueOf(message);
                        if (SearchQuery != null) {
                            int index = TextUtils.indexOf(message.toLowerCase(), SearchQuery.toLowerCase());
                            if (index >= 0) {
//                                Message.setSpan(new ForegroundColorSpan(AppHelper.getColor(mActivity, R.color.colorAccent)), index, index + SearchQuery.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                Message.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), index, index + SearchQuery.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                            }
                            mMessagesViewHolder.message.setText(Message, TextView.BufferType.SPANNABLE);
//                            mMessagesViewHolder.message.setTextSize(PreferenceSettingsManager.getMessage_font_size(mActivity));
                        } else {
                            mMessagesViewHolder.message.setText(message, TextView.BufferType.NORMAL);
//                            mMessagesViewHolder.message.setTextSize(PreferenceSettingsManager.getMessage_font_size(mActivity));
                        }

                        break;
                }


            } else {
                mMessagesViewHolder.message.setVisibility(View.GONE);
            }


            try {

                String messageDate = UtilsTime.getTimeStamp(messagesModel.getDate());
                mMessagesViewHolder.setDate(messageDate);


//                Log.d("TIME",messageDate+"");
                if (messagesModel.getSenderID() == Integer.parseInt(SharedPreference.getSharedPreferenceString(mActivity, SharedprefKeys.USER_ID, ""))) {
//                    mMessagesViewHolder.showSent(messagesModel.getStatus());
                } else {
//                    mMessagesViewHolder.hideSent();
                }
            } catch (Exception e) {
                Constant.LogCat("Exception time " + e.getMessage());
            }


        }

        holder.itemView.setActivated(selectedItems.get(position, false));
    }

    public void remove(int position) {
        this.mMessagesModel.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {

        return mMessagesModel != null ? mMessagesModel.size() : 0;

    }


    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {

            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);

        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }


    public MessagesModel getItem(int position) {
        return mMessagesModel.get(position);
    }

    public void UpdateMessageItem(MessagesModel messagesModel) {
        for (int i = 0; i < mMessagesModel.size(); i++) {
            MessagesModel model = mMessagesModel.get(i);
            if (messagesModel == model) {
                Constant.LogCat("Message  exist");
                notifyItemChanged(i);
            }
        }
    }


    public class MessagesViewHolder extends RecyclerView.ViewHolder {

        TextView message,timestamp;


        MessagesViewHolder(View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.message);
            timestamp = itemView.findViewById(R.id.timestamp);

        }


        void setDate(String Date) {
            timestamp.setText(Date);
        }


/*        void hideSent() {
            statusMessages.setVisibility(View.GONE);
        }*/

/*        void showSent(int status) {
            statusMessages.setVisibility(View.VISIBLE);
            switch (status) {
                case AppConstants.IS_WAITING:
                    statusMessages.setImageResource(R.drawable.ic_access_time_gray_24dp);
                    break;
                case AppConstants.IS_SENT:
                    statusMessages.setImageResource(R.drawable.ic_done_gray_24dp);
                    break;
                case AppConstants.IS_DELIVERED:
                    statusMessages.setImageResource(R.drawable.ic_done_all_gray_24dp);
                    break;
                case AppConstants.IS_SEEN:
                    statusMessages.setImageResource(R.drawable.ic_done_all_blue_24dp);
                    break;

            }

        }*/

    }

}
