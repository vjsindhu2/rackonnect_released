package com.sportskonnect.api;


import com.sportskonnect.api.Callbacks.AddGameInfoData;
import com.sportskonnect.api.Callbacks.CategoriesDataResponse;
import com.sportskonnect.api.Callbacks.ChatListHistResponse;
import com.sportskonnect.api.Callbacks.ChatStatusDoubleRespo;
import com.sportskonnect.api.Callbacks.CheckTourTimeRespo;
import com.sportskonnect.api.Callbacks.CreateMatchRespo;
import com.sportskonnect.api.Callbacks.EndtourRespo;
import com.sportskonnect.api.Callbacks.FetchLocationRespo;
import com.sportskonnect.api.Callbacks.FetchOpponentsRespo;
import com.sportskonnect.api.Callbacks.FetchSavedLocationRespo;
import com.sportskonnect.api.Callbacks.FetchTourMatchRespo;
import com.sportskonnect.api.Callbacks.GlobalRankingRespo;
import com.sportskonnect.api.Callbacks.InsertChatAdapter;
import com.sportskonnect.api.Callbacks.InsertFullChat;
import com.sportskonnect.api.Callbacks.JoinDualMatchResponse;
import com.sportskonnect.api.Callbacks.LoginResponse;
import com.sportskonnect.api.Callbacks.MatchListDualResponse;
import com.sportskonnect.api.Callbacks.MatchtourPercentRespo;
import com.sportskonnect.api.Callbacks.OpponentsResponse;
import com.sportskonnect.api.Callbacks.PlaceAPIRESPO;
import com.sportskonnect.api.Callbacks.PlayerTurnUpRespo;
import com.sportskonnect.api.Callbacks.SaveCatDataRespo;
import com.sportskonnect.api.Callbacks.SaveCustomLocationRespo;
import com.sportskonnect.api.Callbacks.SaveProfiledataResponse;
import com.sportskonnect.api.Callbacks.SaveScoreResponse;
import com.sportskonnect.api.Callbacks.SaveuserPrefRespo;
import com.sportskonnect.api.Callbacks.SendMessageDualRespo;
import com.sportskonnect.api.Callbacks.StartScoringResponse;
import com.sportskonnect.api.Callbacks.UpdateGameInfoData;
import com.sportskonnect.api.Callbacks.UserRankingRespo;
import com.sportskonnect.api.Callbacks.UserRankingResponse;
import com.sportskonnect.modal.UserOnlineStatus;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Apis {

    @FormUrlEncoded
    @POST("admin/api/webservices/login")
    Call<LoginResponse> getlogin(@Field("mobile_number") String mobile_number,@Field("hashkey") String hashkey);

    @POST("admin/api/webservices/category")
    Call<CategoriesDataResponse> getCategories();

    @FormUrlEncoded
    @POST("admin/api/webservices/save_catdetails")
    Call<SaveCatDataRespo> setplayerdata(@Field("userid") String userid, @Field("gameinfo") String gameinfo, @Field("lat") double lat, @Field("lng") double lng, @Field("address") String address);

    @Multipart
    @POST("admin/api/webservices/save_logindetails")
    Call<SaveProfiledataResponse> saveprofiledata(@Part("mobile_number") RequestBody mobile_number, @Part("email") RequestBody email, @Part("gender") RequestBody gender, @Part("name") RequestBody name, @Part MultipartBody.Part image, @Part("age") RequestBody age, @Part("time") RequestBody time, @Part("dob") RequestBody dob, @Part("referal") RequestBody referal);


    @FormUrlEncoded
    @POST("admin/api/webservices/save_logindetails")
    Call<SaveProfiledataResponse> without_imagesaveprofiledata(@Field("mobile_number") String mobile_number, @Field("email") String email, @Field("gender") String gender, @Field("name") String name, @Field("image") String image, @Field("age") String age, @Field("time") String time, @Field("dob") String dob, @Field("referal") String referal);


    @GET("maps/api/geocode/json?")
    Call<FetchLocationRespo> getLocationName(@Query("latlng") String latlng, @Query("sensor") boolean sensor, @Query("key") String key);

    @GET("maps/api/place/autocomplete/json?")
    Call<PlaceAPIRESPO> getplacedata(@Query("input") String input, @Query("key") String key);

    @FormUrlEncoded
    @POST("admin/api/webservices/fetchsave_location")
    Call<FetchSavedLocationRespo> fetchsavedlocation(@Field("userid") String userid);

    @FormUrlEncoded
    @POST("admin/api/webservices/fetchplayer.php")
    Call<FetchOpponentsRespo> fetchOpponets(@Field("userid") String userid, @Field("catid") int catid, @Field("levelid") int levelid, @Field("current_lat") String current_lat, @Field("current_long") String current_long, @Field("current_radius") String current_radius);


    @FormUrlEncoded
    @POST("admin/api/webservices/fetchplayerbyfilter")
    Call<FetchOpponentsRespo> filteropponets(@Field("catid") String catid, @Field("levelid") String levelid, @Field("gender") String gender, @Field("minage") String minage, @Field("maxage") String maxage, @Field("time") String time, @Field("userid") String userid);

    /*    filter match Doubles*/
    @FormUrlEncoded
    @POST("admin/api/webservices/doublematchbyfilter.php")
    Call<MatchListDualResponse> doublematchbyfilter(@Field("catid") String catid, @Field("levelid") String levelid, @Field("gender") String gender, @Field("minage") String minage, @Field("maxage") String maxage, @Field("time") String time, @Field("userid") String userid);

    /*    filter match Doubles*/


    @FormUrlEncoded
    @POST("admin/api/webservices/tournamentfilter.php")
    Call<FetchTourMatchRespo> tournamentfilter(@Field("catid") String catid, @Field("cityname") String cityname, @Field("minfees") String minfees, @Field("maxfees") String maxfees, @Field("userid") String userid);

    /*    save user Preference*/
    @FormUrlEncoded
    @POST("admin/api/webservices/savemypreference")
    Call<SaveuserPrefRespo> saveUserpreference(@Field("userid") String userid, @Field("prtime") String prtime, @Field("pgender") String pgender, @Field("pmin_age") String pmin_age, @Field("pmax_age") String pmax_age);

    @FormUrlEncoded
    @POST("admin/api/webservices/addchat")
    Call<InsertChatAdapter> insertChat(@Field("mid") String mid, @Field("fid") String fid, @Field("msg") String msg);

    @FormUrlEncoded
    @POST("admin/api/webservices/checkchatstatus")
    Call<InsertChatAdapter> fetchChatThread(@Field("mid") String mid, @Field("fid") String fid);

    @FormUrlEncoded
    @POST("admin/api/webservices/getallchat")
    Call<InsertFullChat> getallchat(@Field("mid") String mid, @Field("fid") String fid);

    @FormUrlEncoded
    @POST("admin/api/webservices/updateflag")
    Call<UserOnlineStatus> saveonlineorNot(@Field("flag") String flag, @Field("userid") String userid);

    /*    get Match list*/
    @FormUrlEncoded
    @POST("admin/api/webservices/matchlist")
    Call<MatchListDualResponse> getAllMatches(@Field("userid") String userid);

    /*    get Match History list*/
    @FormUrlEncoded
    @POST("admin/api/webservices/matchhistory")
    Call<MatchListDualResponse> getAllMatchesHistory(@Field("userid") String userid);
/*    get Match History list*/
    @FormUrlEncoded
    @POST("admin/api/webservices/chatlist")
    Call<ChatListHistResponse> getAllchatlistHistory(@Field("userid") String userid,@Field("catid") String catid);

    /*    get Match list Duals*/
    @FormUrlEncoded
    @POST("admin/api/webservices/matchlistdouble")
    Call<MatchListDualResponse> getAllMatchesDuals(@Field("catid") String catid, @Field("userid") String userid, @Field("current_lat") String current_lat, @Field("current_long") String current_long, @Field("current_radius") String current_radius);

    /*    get Match list Duals*/

    @POST("admin/api/webservices/get_percentage.php")
    Call<MatchtourPercentRespo> gettourpercentage();

    /*    get User Ranking list*/
    @FormUrlEncoded
    @POST("admin/api/webservices/getdashboard")
    Call<UserRankingResponse> getuserRanking(@Field("userid") String userid);

    /*    get All Request list*/
    @FormUrlEncoded
    @POST("admin/api/webservices/score_request")
    Call<MatchListDualResponse> getAllMatchesRequests(@Field("userid") String userid);

    /*    get checkstartscoring */
    @FormUrlEncoded
    @POST("admin/api/webservices/checkstartscoring")
    Call<StartScoringResponse> checkstartscoring(@Field("end_time") String end_time, @Field("matchid") String matchid, @Field("date") String date, @Field("matchtype") String matchtype);

    /*    get playerturnup */
    @FormUrlEncoded
    @POST("admin/api/webservices/playerturnup")
    Call<PlayerTurnUpRespo> playerturnup(@Field("matchid") String matchid);

    /*    get Doubles playerturnup */
    @FormUrlEncoded
    @POST("admin/api/webservices/playerturnupdouble")
    Call<PlayerTurnUpRespo> playerDoublesturnup(@Field("matchid") String matchid, @Field("memberid1") String memberid1, @Field("memberid2") String memberid2, @Field("memberid3") String memberid3);

    /*    get savescore */
    @FormUrlEncoded
    @POST("admin/api/webservices/savescore")
    Call<SaveScoreResponse> savescore(@Field("midpoint") int midpoint, @Field("fidpoint") int fidpoint, @Field("matchid") String matchid, @Field("type") String type, @Field("matchtype") String matchtype);


    /*    Verify Score */
    @FormUrlEncoded
    @POST("admin/api/webservices/verifyscore")
    Call<SaveScoreResponse> verifyscore(@Field("matchid") String matchid);

    /*    Verify Score */
    @FormUrlEncoded
    @POST("admin/api/webservices/verifyscoredouble")
    Call<SaveScoreResponse> verifyscoredouble(@Field("matchid") String matchid, @Field("groupid") String groupid, @Field("mid") String mid);

    /*    Socil Login Verifcation Score */
    @FormUrlEncoded
    @POST("admin/api/webservices/sociallogin")
    Call<LoginResponse> sociallogin(@Field("email_id") String email_id);

    /*    Api used to create match*/

    @FormUrlEncoded
    @POST("admin/api/webservices/creatematch")
    Call<CreateMatchRespo> createMatch(@Field("catid") String catid,

                                       @Field("lat") String lat,
                                       @Field("lng") String lng,
                                       @Field("address") String address,
                                       @Field("matchname") String matchname,
                                       @Field("date") String date,
                                       @Field("start_time") String start_time,
                                       @Field("end_time") String end_time,
                                       @Field("mid") String mid,
                                       @Field("fid") String fid,
                                       @Field("midlevel") String midlevel,
                                       @Field("fidlevel") String fidlevel,
                                       @Field("matchtype") String matchtype

    );

    @FormUrlEncoded
    @POST("admin/api/webservices/createtournament.php")
    Call<CreateMatchRespo> createTour(@Field("catid") int catid,
                                      @Field("lat") String lat,
                                      @Field("lng") String lng,
                                      @Field("address") String address,
                                      @Field("matchname") String matchname,
                                      @Field("date") String date,
                                      @Field("time") String time,
                                      @Field("mid") String mid,
                                      @Field("midlevel") String midlevel,
                                      @Field("matchtype") String matchtype,
                                      @Field("amount") int amount,
                                      @Field("total_amount") int total_amount,
                                      @Field("txn_id") String txn_id,
                                      @Field("winner_amount") int winner_amount,
                                      @Field("runnerup_amount") int runnerup_amount,
                                      @Field("company_amount") int company_amount,
                                      @Field("host_amount") int host_amount,
                                      @Field("group_member") int group_member,
                                      @Field("orderid") String orderid,
                                      @Field("cityname") String cityname
    );

    /*    Create dual match API*/
    @FormUrlEncoded
    @POST("admin/api/webservices/creatematchdouble")
    Call<CreateMatchRespo> createDualMatch(@Field("catid") String catid,

                                           @Field("lat") String lat,
                                           @Field("lng") String lng,
                                           @Field("address") String address,
                                           @Field("matchname") String matchname,
                                           @Field("date") String date,
                                           @Field("start_time") String start_time,
                                           @Field("end_time") String end_time,
                                           @Field("mid") String mid,
                                           @Field("midlevel") String midlevel,
                                           @Field("matchtype") String matchtype

    );


    @FormUrlEncoded
    @POST("admin/api/webservices/save_customelocation")
    Call<SaveCustomLocationRespo> save_customlocation(@Field("userid") String userid,
                                                      @Field("radius") int radius,
                                                      @Field("lat") double lat,
                                                      @Field("lng") double lng,
                                                      @Field("address") String address
    );

    @FormUrlEncoded
    @POST("admin/api/webservices/checkvalidatetime")
    Call<StartScoringResponse> checkvalidatetime(@Field("validatetime") String validatetime,
                                                 @Field("matchid") String matchid,
                                                 @Field("date") String date,
                                                 @Field("matchtype") String matchtype,
                                                 @Field("groupid") String group_id
    );


    @FormUrlEncoded
    @POST("admin/api/webservices/savedoublefid")
    Call<JoinDualMatchResponse> joinDualmatch(@Field("userid") String userid, @Field("userlevel") String userlevel, @Field("groupid") String groupid, @Field("memberid") String memberid);


    /*    Join Tournament*/
    @FormUrlEncoded
    @POST("admin/api/webservices/save_tournament.php")
    Call<JoinDualMatchResponse> joinTournament(@Field("userid") String userid, @Field("userlevel") String userlevel, @Field("tournament_id") String groupid, @Field("fid") String fid, @Field("txn_id") String txn_id, @Field("orderid") String orderid);

    /*    get all Tour Tournament*/
    @FormUrlEncoded
    @POST("admin/api/webservices/get_tournament.php")
    Call<FetchTourMatchRespo> getAllTour(@Field("catid") String catid, @Field("userid") String userid, @Field("current_lat") String current_lat, @Field("current_long") String current_long, @Field("current_radius") String current_radius);

    /*    get all Tour History*/
    @FormUrlEncoded
    @POST("admin/api/webservices/gettournamenthistory.php")
    Call<FetchTourMatchRespo> getAllTourHistory(@Field("catid") String catid, @Field("userid") String userid, @Field("current_lat") String current_lat, @Field("current_long") String current_long, @Field("current_radius") String current_radius);


    @FormUrlEncoded
    @POST("admin/api/webservices/getallchatdouble")
    Call<FetchAllDualChatResponse> getallchatdouble(@Field("groupid") String groupid);

    @FormUrlEncoded
    @POST("admin/api/webservices/checkchatstatusdouble")
    Call<ChatStatusDoubleRespo> checkchatstatusdouble(@Field("mid") String mid, @Field("groupid") String groupid);

    @FormUrlEncoded
    @POST("admin/api/webservices/addchatdouble")
    Call<SendMessageDualRespo> addchatdouble(@Field("mid") String mid, @Field("msg") String msg, @Field("groupid") String groupid);

    @FormUrlEncoded
    @POST("admin/api/webservices/checktournamenttime")
    Call<CheckTourTimeRespo> checktournamenttime(@Field("tournament_id") String tournament_id, @Field("time") String time, @Field("date") String date, @Field("noofplayer") String noofplayer, @Field("validatetime") String validatetime);

    @FormUrlEncoded
    @POST("admin/api/webservices/endtournament")
    Call<EndtourRespo> endtournament(@Field("tournament_id") String tournament_id, @Field("winnermemberid") String winnermemberid, @Field("runnermemberid") String runnermemberid, @Field("hostmemberid") String hostmemberid);

    @FormUrlEncoded
    @POST("admin/api/webservices/verifytournament")
    Call<EndtourRespo> verifytournament(@Field("tournament_id") String tournament_id, @Field("mid") String mid, @Field("noofplayer") String noofplayer, @Field("flag") String flag, @Field("validatetime") String validatetime, @Field("date") String date);

    @FormUrlEncoded
    @POST("admin/api/webservices/leaderboardall")
    Call<GlobalRankingRespo> globalranking(@Field("catid") String catid);

    @FormUrlEncoded
    @POST("admin/api/webservices/leaderboard")
    Call<UserRankingRespo> getuserrank(@Field("userid") String userid, @Field("catid") String catid);

    @FormUrlEncoded
    @POST("admin/api/webservices/add_token")
    Call<CreateMatchRespo> setToken(@Field("userid") String userid, @Field("token") String token, @Field("gamenotipush") String gamenotipush, @Field("cancelnotipush") String cancelnotipush, @Field("scorenotipush") String scorenotipush, @Field("gamenotiemail") String gamenotiemail, @Field("cancelnotiemail") String cancelnotiemail, @Field("scorenotiemail") String scorenotiemail, @Field("sound") String sound, @Field("chat") String chat);

    @FormUrlEncoded
    @POST("admin/api/webservices/deleteaccount")
    Call<CreateMatchRespo> delAccount(@Field("userid") String userid);

    /*    Resend Updated Score*/
    @FormUrlEncoded
    @POST("admin/api/webservices/resendupdatescore")
    Call<CreateMatchRespo> resendupdateScore(@Field("matchid") String matchid);

    @FormUrlEncoded
    @POST("admin/api/webservices/getdatabyid")
    Call<OpponentsResponse> getopponetinfo(@Field("userid") String userid);


    @FormUrlEncoded
    @POST("admin/api/webservices/getgameinfo")
    Call<AddGameInfoData> getgameInfo(@Field("userid") String user_id);

    @FormUrlEncoded
    @POST("admin/api/webservices/updategameinfo")
    Call<UpdateGameInfoData> updateGameInfo(@Field("userid") String user_id, @Field("gameinfo") String gameinfo);


}
