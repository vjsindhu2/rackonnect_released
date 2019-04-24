package com.sportskonnect;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sportskonnect.activities.ChatHistoryActivity;
import com.sportskonnect.activities.CreateMatch;
import com.sportskonnect.activities.ExploreMatchesActivity;
import com.sportskonnect.activities.FAQActivity;
import com.sportskonnect.activities.LeaderBoard;
import com.sportskonnect.activities.ScoringRequestsActivity;
import com.sportskonnect.activities.TermsNconditions;
import com.sportskonnect.activities.TourHistory;
import com.sportskonnect.api.Constant;
import com.sportskonnect.sharedpref.SharedPreference;
import com.sportskonnect.sharedpref.SharedprefKeys;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by revtvLevelinfotech on 12/19/2017.
 */

public class FragmentDrawer extends Fragment {
    public static TextView tvUsername, tvLevel;
    public static CircleImageView user_img;
    public static TextView tvAge;
    private static String TAG = FragmentDrawer.class.getSimpleName();
    private static String[] titles = null;
    TextView faq;
    private RelativeLayout past_tour_rl,chathis_rl;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;
    private RelativeLayout scoring_req;
    private SharedPreference sharedPreference;
    //    SharedData sharedData;
    private FragmentDrawerListener drawerListener;
    private String gender = "";
    private boolean istourselected = false;
    private RelativeLayout leader_board;
    private RelativeLayout exploretour;
    private boolean ismatchselected = true;
    private TextView share_tv;
    private String referal_code = "";
    private TextView terms_tv;

    public FragmentDrawer() {
    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels
//        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        final Context context = getActivity();
//        sharedData = new SharedData(context);
//        String email = sharedData.getValue("emailId");

        Button btnEdit = (Button) layout.findViewById(R.id.btnEdit);
        tvUsername = (TextView) layout.findViewById(R.id.tvUsername);
        tvLevel = (TextView) layout.findViewById(R.id.tvLevel);
        faq = (TextView) layout.findViewById(R.id.faq);
        past_tour_rl = (RelativeLayout) layout.findViewById(R.id.past_tour_rl);
        chathis_rl = (RelativeLayout) layout.findViewById(R.id.chathis_rl);
        share_tv = (TextView) layout.findViewById(R.id.share_tv);
        terms_tv = (TextView) layout.findViewById(R.id.terms_tv);
        tvAge = (TextView) layout.findViewById(R.id.tvAge);
        user_img = (CircleImageView) layout.findViewById(R.id.user_img);
        exploretour = (RelativeLayout) layout.findViewById(R.id.exploretour);
        leader_board = (RelativeLayout) layout.findViewById(R.id.leader_board);

        gender = SharedPreference.getSharedPreferenceString(getActivity(), SharedprefKeys.GENDER, "");

        tvUsername.setText(sharedPreference.getSharedPreferenceString(getActivity(), SharedprefKeys.USER_NAME, ""));
        tvAge.setText(sharedPreference.getSharedPreferenceString(getActivity(), SharedprefKeys.AGE, "") + " Y");

        referal_code = SharedPreference.getSharedPreferenceString(getActivity(), SharedprefKeys.USER_REFERAL_CODE, "");
        tvLevel.setText(Constant.getGameLevelFromlevelId(SharedPreference.getSharedPreferenceString(getActivity(), SharedprefKeys.USER_SELECTED_LEVEL, "")));
        try {
            if (gender.equals("Male")) {

                Picasso.get().load(SharedPreference.getSharedPreferenceString(getActivity(), SharedprefKeys.PROFILE_PIC_URL, "")).placeholder(R.drawable.male).into(user_img);

            } else {
                Picasso.get().load(SharedPreference.getSharedPreferenceString(getActivity(), SharedprefKeys.PROFILE_PIC_URL, "")).placeholder(R.drawable.female).into(user_img);

            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }


        past_tour_rl.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(Gravity.START);
                Intent intent = new Intent(getActivity(), TourHistory.class);
                startActivity(intent);
            }
        });


        chathis_rl.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                mDrawerLayout.closeDrawer(Gravity.START);
                Intent intent = new Intent(getActivity(), ChatHistoryActivity.class);
                startActivity(intent);

            }
        });

        terms_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TermsNconditions.class);
                startActivity(intent);
            }
        });

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FAQActivity.class);
                startActivity(intent);
            }
        });
        share_tv.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {

                mDrawerLayout.closeDrawer(Gravity.START);

                String playstorelink = "https://play.google.com/store/apps/details?id=com.sportskonnect/";
                String shareBody = "Click on the link for downloading 'RACKONNECT' App'" + " and you can earn 50 Points \n" + "Referal Code- " + referal_code + System.getProperty("line.separator") + " " + playstorelink;
                final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(shareIntent, "Share application via.."));

            }
        });

        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Activity_UserProfile.class);
                startActivity(intent);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);

                Intent intent = new Intent(context, Activity_UserProfile.class);
                startActivity(intent);
            }
        });

        Button btnCreate = (Button) layout.findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                choosematchTypeDialog();

                mDrawerLayout.closeDrawer(GravityCompat.START);

            }
        });

        exploretour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_TourList.class);
                startActivity(intent);
            }
        });

        leader_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LeaderBoard.class);
                startActivity(intent);
            }
        });

        RelativeLayout rlExploreMatch = (RelativeLayout) layout.findViewById(R.id.rlExploreMatch);
        rlExploreMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ExploreMatchesActivity.class);
                startActivity(intent);
//                Toast.makeText(getActivity(),"CLICKED",Toast.LENGTH_LONG).show();

//                mDrawerLayout.closeDrawer(Gravity.START);

            }
        });

        RelativeLayout rlSetting = (RelativeLayout) layout.findViewById(R.id.rlSetting);
        rlSetting.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(Gravity.START);

                Intent intent = new Intent(context, Activity_Setting.class);
                startActivity(intent);
            }
        });

        scoring_req = (RelativeLayout) layout.findViewById(R.id.scoring_req);

        scoring_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ScoringRequestsActivity.class);
                startActivity(intent);
            }
        });

        int spancount = 1;
        int spacing = 20;
        boolean includeEdge = false;
//        final TextView tvEmail = (TextView) layout.findViewById(R.id.tvEmail);
//        tvEmail.setText(email);
        //   final RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);


//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spancount, spacing, includeEdge , this));
        // recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

//        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(getActivity());
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                NavigationDrawerAdapter.selected_item = position;
//                recyclerView.getAdapter().notifyDataSetChanged();
//                drawerListener.onDrawerItemSelected(view, position);
//                mDrawerLayout.closeDrawer(containerView);
//
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));

        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.hamburger);

        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });


        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }


    private void choosematchTypeDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = li.inflate(R.layout.dialog_select_match_type, null, false);
        dialog.setContentView(v1);
        dialog.setCancelable(false);

        TextView create_match_tv = (TextView) v1.findViewById(R.id.create_match_tv);
        TextView create_tour_tv = (TextView) v1.findViewById(R.id.create_tour_tv);

        create_tour_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                istourselected = true;
                ismatchselected = false;
                create_tour_tv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                create_tour_tv.setTextColor(getResources().getColor(R.color.color_white));

                create_match_tv.setBackgroundColor(getResources().getColor(R.color.color_white));
                create_match_tv.setTextColor(getResources().getColor(R.color.colorPrimary));


            }
        });


        create_match_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ismatchselected = true;
                istourselected = false;
                create_match_tv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                create_match_tv.setTextColor(getResources().getColor(R.color.color_white));

                create_tour_tv.setBackgroundColor(getResources().getColor(R.color.color_white));
                create_tour_tv.setTextColor(getResources().getColor(R.color.colorPrimary));


            }
        });


        Button btn_cancel = (Button) v1.findViewById(R.id.btn_cancel);
        Button btn_submit = (Button) v1.findViewById(R.id.btn_submit);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                tvPl.setVisibility(View.GONE);
//                btnSubmit.setText("GO TO HOMEPAGE");
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                if (ismatchselected) {


                    SharedPreference.setSharedPreferenceString(getActivity(), SharedprefKeys.MATCH_CREATED_FROM, "2");
                    Intent intent = new Intent(getActivity(), CreateMatch.class);
                    startActivity(intent);


                } else if (istourselected) {

                    Intent intent = new Intent(getActivity(), Activity_TourForm.class);
                    startActivity(intent);
                    ismatchselected = true;

                }


            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }

            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

    }
}
