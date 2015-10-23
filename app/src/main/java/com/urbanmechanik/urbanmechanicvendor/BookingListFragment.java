package com.urbanmechanik.urbanmechanicvendor;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookingListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    ArrayList<Booking> mBooking = new ArrayList<Booking>();
    public BookingListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_booking_list, container, false);


        getBookings();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(false);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new MyAdapter(mBooking);
        mRecyclerView.setAdapter(mAdapter);

        Toast.makeText(getActivity(), "Booking list fragment", Toast.LENGTH_SHORT).show();
        return v;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh: {
                getBookings();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void getBookings () {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.shared_prefs_file_key), Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(getString(R.string.saved_token), null);


        JSONObject data = new JSONObject();
        try {
            data.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, MainActivity.URL + "bookings?token=" + token, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray bookings = response.getJSONArray("bookings");
                    mBooking.clear();

                    for (int i = 0; i < bookings.length(); i++) {
                        JSONObject booking_object = bookings.getJSONObject(i);
                        Booking b = new Booking(
                                booking_object.getInt("id"),
                                booking_object.getString("type"),
                                booking_object.getString("vendor"),
                                booking_object.getString("user"),
                                booking_object.getString("status"),
                                booking_object.getString("problem"),
                                booking_object.getString("slot"),
                                booking_object.optString("feedback", ""),
                                booking_object.optString("ratings", "")
                        );
                        mBooking.add(b);
                    }

                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error = [" + error.getMessage() + "]");
                String s = "Dk";
                try {
                    s = new String(error.networkResponse.data);
                    JSONObject reason = new JSONObject(s);
                    System.out.println("error = [" + reason.getString("error") + "]");
                } catch (JSONException e) {
                    System.out.println("Cannot parse " + s);
                }

            }
        });

        VolleySingletonRequestQueue.getInstance(getActivity()).addToRequestQueue(request);
    }

}
