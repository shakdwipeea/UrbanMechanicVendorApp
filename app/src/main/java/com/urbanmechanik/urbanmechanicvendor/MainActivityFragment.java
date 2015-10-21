package com.urbanmechanik.urbanmechanicvendor;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private Button mLogIn = null;
    private EditText mUsernameInput = null;
    private EditText mPasswordInput = null;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_main, container, false);

        mLogIn = (Button) v.findViewById(R.id.login);
        mUsernameInput = (EditText) v.findViewById(R.id.email);
        mPasswordInput = (EditText) v.findViewById(R.id.password);

        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = VolleySingletonRequestQueue
                        .getInstance(v.getContext())
                        .getRequestQueue();


                JSONObject loginDetails = new JSONObject();
                try {
                    loginDetails.put("email", mUsernameInput.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    loginDetails.put("password", mPasswordInput.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, MainActivity.URL + "vendors/login", loginDetails, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String token = "";
                        String error = "";

                        try {
                            token = response.getString("token");

                            SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_prefs_file_key), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.saved_token), token);
                            editor.commit();

                            FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();

                            ft.replace(R.id.the_fragment, new BookingListFragment());
                            ft.addToBackStack(null);

                            ft.commit();



                            Toast.makeText(getActivity(), "Logged In", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Not logged IN", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json = new String(error.networkResponse.data);
                        String cause = "";
                        JSONObject reason = null;

                        try {
                            reason = new JSONObject(json);
                            cause = reason.getString("error");
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Error parsing response json", Toast.LENGTH_SHORT).show();
                        }

                        System.out.println("error = [" + error.getMessage() + "]");
                        Toast.makeText(getActivity(), cause, Toast.LENGTH_SHORT).show();
                    }
                });

                VolleySingletonRequestQueue.getInstance(v.getContext()).addToRequestQueue(request);
            }
        });

        return v;
    }
}
