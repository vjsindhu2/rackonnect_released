package com.sportskonnect.adapters;

import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.sportskonnect.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;


public class SearchResultsRecyclerAdapter extends RecyclerView.Adapter<SearchResultsRecyclerAdapter.ViewHolder> {

    private static final CharacterStyle STYLE_NORMAL = new StyleSpan(Typeface.NORMAL);
    private final AppCompatActivity mContext;
    private SearchResultsRecyclerAdapterListener searchResultsRecyclerAdapterListener;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<AutocompletePrediction> mResultList;


    public SearchResultsRecyclerAdapter(AppCompatActivity mContext, GoogleApiClient mGoogleApiClient, ArrayList<AutocompletePrediction> mResultList) {
        this.mContext = mContext;
        this.mGoogleApiClient = mGoogleApiClient;
        this.mResultList = mResultList;
    }

    public ArrayList<AutocompletePrediction> getmResultList() {
        return mResultList;
    }

    public void setmResultList(ArrayList<AutocompletePrediction> mResultList) {
        this.mResultList = mResultList;
    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }

    @Override
    public SearchResultsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemLayout = inflater.inflate(R.layout.item_search_results, parent, false);
        return new ViewHolder(itemLayout);
    }

    @Override
    public void onBindViewHolder(SearchResultsRecyclerAdapter.ViewHolder holder, int position) {
        setLayoutSearchResults(holder, position);
    }

    private void setLayoutSearchResults(final SearchResultsRecyclerAdapter.ViewHolder holder, final int position) {
        AutocompletePrediction item = mResultList.get(position);

        holder.txtPlace.setText(item.getPrimaryText(STYLE_NORMAL));
        holder.txtAddress.setText(item.getSecondaryText(STYLE_NORMAL));
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtPlace;
        private final TextView txtAddress;

        public ViewHolder(View itemView) {
            super(itemView);

            txtPlace = (TextView) itemView.findViewById(R.id.txt_place);
            txtAddress = (TextView) itemView.findViewById(R.id.txt_address);

            itemView.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {


                    AutocompletePrediction item = mResultList.get(getLayoutPosition());
                    String placeId = item.getPlaceId();
                    CharSequence primaryText = item.getPrimaryText(null);

                    Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
                    PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                            .getPlaceById(mGoogleApiClient, placeId);
                    placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

                    /*Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
                            Toast.LENGTH_SHORT).show();*/
                    Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
                }
            });
        }
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            // Format details of the place for display and show it in a TextView.
//            mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
//                    place.getId(), place.getAddress(), place.getPhoneNumber(),
//                    place.getWebsiteUri()));

            // Display the third party attributions if set.
            final CharSequence thirdPartyAttribution = places.getAttributions();

            Log.i(TAG, "Place details received: " + place.getName());
            searchResultsRecyclerAdapterListener.onItemSelected(place);

            places.release();
        }
    };


    public interface SearchResultsRecyclerAdapterListener {

        void onItemSelected(Place place);

        void onSnackBarShow(String message);

    }

    public SearchResultsRecyclerAdapterListener getSearchResultsRecyclerAdapterListener() {
        return searchResultsRecyclerAdapterListener;
    }

    public void setSearchResultsRecyclerAdapterListener(SearchResultsRecyclerAdapterListener searchResultsRecyclerAdapterListener) {
        this.searchResultsRecyclerAdapterListener = searchResultsRecyclerAdapterListener;
    }
}
