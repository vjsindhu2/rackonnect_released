package com.sportskonnect.adapters;

import android.content.Context;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sportskonnect.R;
import com.sportskonnect.api.Callbacks.InsertChatAdapter;
import com.sportskonnect.modal.AllChatConversation;
import com.sportskonnect.modal.ChatComonModal;
import com.sportskonnect.modal.ChatDualModal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.recyclerview.widget.RecyclerView;

public class DualChatRoomThreadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = DualChatRoomThreadAdapter.class.getSimpleName();
    private static String today;
    private String userId;
    private int SELF = 100;
    private int type = 0;
    private Context mContext;
    private ArrayList<InsertChatAdapter> messageArrayList;
    private ArrayList<AllChatConversation> allChatConversationArrayList;
    private ArrayList<ChatDualModal> chatComonModalArrayList;

    public DualChatRoomThreadAdapter() {
    }

    public DualChatRoomThreadAdapter(Context mContext, ArrayList<ChatDualModal> chatComonModalArrayList, String userId) {

        this.mContext = mContext;
        this.chatComonModalArrayList = chatComonModalArrayList;
        this.userId = userId;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }



    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
//            timestamp = date1.toString();
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    public static long convertTimeInMillis(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timeInMilliseconds = 0;
        Date mDate = null;
        try {
            mDate = sdf.parse(time);
            timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return timeInMilliseconds;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        // view type is to identify where to render the chat message
        // left or right
        if (viewType == SELF) {
            // self message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_self_dual, parent, false);
        } else {
            // others message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_other_dual, parent, false);
        }


        return new ViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {



        ChatDualModal chatDualModal = chatComonModalArrayList.get(position);
        if (chatDualModal.getMid().equals(userId)) {
            return SELF;
        }
        return position;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        ChatDualModal chatComonModal = chatComonModalArrayList.get(position);


        ((ViewHolder) holder).message.setText(chatComonModal.getMessage());
        ((ViewHolder) holder).name.setText(chatComonModal.getName());

        String timestamp = getTimeStamp(chatComonModal.getDate());

        ((ViewHolder) holder).timestamp.setText(timestamp);



    }

    @Override
    public int getItemCount() {



        return chatComonModalArrayList.size();
    }

    public void updateData(ArrayList<ChatDualModal> viewModels) {

        chatComonModalArrayList.addAll(viewModels);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView message, timestamp,name;

        public ViewHolder(View view) {
            super(view);
            message = (TextView) itemView.findViewById(R.id.message);
            name = (TextView) itemView.findViewById(R.id.name);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
        }
    }
}