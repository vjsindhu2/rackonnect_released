package com.sportskonnect.helpers;

public class AppConstants {


    public static final String DATABASE_LOCAL_NAME = "Reckonnect.realm";


    //single user socket constants:
    public static final String SOCKET_IS_MESSAGE_SENT = "send_message";
    public static final String SOCKET_IS_MESSAGE_DELIVERED = "delivered";
    public static final String SOCKET_IS_MESSAGE_SEEN = "seen";
    public static final String SOCKET_NEW_MESSAGE = "new_message";
    public static final String SOCKET_SAVE_NEW_MESSAGE = "save_new_message";
    public static final String SOCKET_USER_PING = "user_ping";
    public static final String SOCKET_IS_TYPING = "typing";
    public static final String SOCKET_IS_STOP_TYPING = "stop_typing";
    public static final String SOCKET_IS_ONLINE = "is_online";
    public static final String SOCKET_IS_LAST_SEEN = "last_seen";
    public static final String SOCKET_CONNECTED = "user_connect";
    public static final String SOCKET_DISCONNECTED = "user_disconnect";
    //group socket constants:
    public static final String SOCKET_SAVE_NEW_MESSAGE_GROUP = "save_group_message";
    public static final String SOCKET_NEW_MESSAGE_GROUP = "new_group_message";
    public static final String SOCKET_USER_PING_GROUP = "user_ping_group";
    public static final String SOCKET_USER_PINGED_GROUP = "user_pinged_group";
    public static final String SOCKET_IS_MESSAGE_GROUP_SENT = "send_group_message";
    public static final String SOCKET_IS_MESSAGE_GROUP_DELIVERED = "group_delivered";
    public static final String SOCKET_IS_MESSAGE_GROUP_SENTT = "group_sent";
    public static final String SOCKET_IS_MEMBER_TYPING = "member_typing";
    public static final String SOCKET_IS_MEMBER_STOP_TYPING = "member_stop_typing";


    /**
     * Status constants
     */

    public static final int IS_WAITING = 0;
    public static final int IS_SENT = 1;
    public static final int IS_DELIVERED = 2;
    public static final int IS_SEEN = 3;
    public static final String CHAT_SERVER_URL = "";
    public static final int LOCATION_SELECTED_ONITEMCLICK = 0;
    //user socket constants:
    public static final int STATUS_USER_TYPING = 0x010;
    public static final int STATUS_USER_STOP_TYPING = 0x011;
    public static final int STATUS_USER_CONNECTED = 0x012;
    public static final int STATUS_USER_DISCONNECTED = 0x013;
    public static final int STATUS_USER_LAST_SEEN = 0x014;}
