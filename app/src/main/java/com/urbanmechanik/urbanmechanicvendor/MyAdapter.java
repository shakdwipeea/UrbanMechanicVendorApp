package com.urbanmechanik.urbanmechanicvendor;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Akash on 19-10-2015.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Booking> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mId, mType, mVendor, mUser, mStatus, mProblem, mSlot;
        public TextView mFeedback, mRating;
        public CardView mCardView;
        public Spinner mSpinner;

        public static String [] bookingStatusValues;

        public ViewHolder(View v) {
            super(v);
            mId = (TextView) v.findViewById(R.id.booking_id);
            mType = (TextView) v.findViewById(R.id.booking_type);
            mVendor = (TextView) v.findViewById(R.id.booking_vendor);
            mUser = (TextView) v.findViewById(R.id.booking_user);
            mProblem = (TextView) v.findViewById(R.id.booking_problem);
            mSlot = (TextView) v.findViewById(R.id.booking_slot);
            mFeedback = (TextView) v.findViewById(R.id.booking_feedback);
            mRating = (TextView) v.findViewById(R.id.booking_rating);
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mSpinner = (Spinner) v.findViewById(R.id.status_select);


            ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(v.getContext(),
                    R.array.booking_status_values,  android.R.layout.simple_spinner_item);

            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            mSpinner.setAdapter(spinnerAdapter);

            bookingStatusValues = v.getContext().getResources().getStringArray(R.array.booking_status_values);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Booking> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_booking, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        int id = mDataset.get(position).getId();
        String status = mDataset.get(position).getStatus();

        holder.mId.setText(String.valueOf(id));
        holder.mType.setText(mDataset.get(position).getType());
        holder.mVendor.setText(mDataset.get(position).getVendor());
        holder.mUser.setText(mDataset.get(position).getUser());
        holder.mProblem.setText(mDataset.get(position).getProblem());
        holder.mSlot.setText(mDataset.get(position).getSlot());
        holder.mFeedback.setText(mDataset.get(position).getFeedback());
        holder.mRating.setText(mDataset.get(position).getRatings());


        int index = Arrays.asList(ViewHolder.bookingStatusValues).indexOf(status.toLowerCase());
        System.out.println("Index is " + index);
        holder.mSpinner.setSelection(index);

        holder.mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                JSONObject postData = new JSONObject();

                final Context c = view.getContext();
                SharedPreferences sharedPreferences = c.getSharedPreferences(c.getString(R.string.shared_prefs_file_key), Context.MODE_PRIVATE);
                String token = sharedPreferences.getString(c.getString(R.string.saved_token), "");

                try {
                    postData.put("token", token);
                    postData.put("id", mDataset.get(position).getId());
                    postData.put("status", parent.getItemAtPosition(pos));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, MainActivity.URL + "bookings/status", postData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(c, "Value Updated", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(c, "Error occured", Toast.LENGTH_SHORT).show();
                    }
                });

                VolleySingletonRequestQueue.getInstance(c).addToRequestQueue(request);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
