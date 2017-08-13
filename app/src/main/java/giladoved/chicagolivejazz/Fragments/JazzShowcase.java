package giladoved.chicagolivejazz.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import giladoved.chicagolivejazz.Models.Show;
import giladoved.chicagolivejazz.R;
import giladoved.chicagolivejazz.API.RequestManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class JazzShowcase extends Fragment {

    public static String API_BASE_URL;

    List<Show> shows;
    FragmentTabHost tabHost;
    KProgressHUD loader;

    public JazzShowcase() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jazz_showcase, container, false);

        API_BASE_URL = view.getContext().getString(R.string.api_base_url);

        tabHost = view.findViewById(android.R.id.tabhost);
        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        shows = new ArrayList<>();

        loader = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        RequestQueue queue = RequestManager.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        String url = API_BASE_URL + "/api/jazzshowcase";
        loader.show();
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("chicago-live-jazz", response.toString());
                        try {
                            JSONArray showsJson = response.getJSONArray("events");
                            for (int i = 0; i < showsJson.length(); i++) {
                                JSONObject showJson = showsJson.getJSONObject(i);
                                String headline = showJson.getString("headline");
                                String time = showJson.getString("time");
                                String price = showJson.getString("price");
                                String details = showJson.getString("details");
                                String video = showJson.getString("video");
                                Show show = new Show(headline, time, price, details, video);
                                shows.add(show);
                            }

                            if (shows.size() > 0) {
                                createTabs();
                            } else {
                                Toast.makeText(getActivity(), "No shows today!", Toast.LENGTH_SHORT).show();
                            }

                            loader.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "There was an error retrieving the info for this jazz club.", Toast.LENGTH_SHORT).show();
                        Log.d("chicago-live-jazz", error.toString());
                        loader.dismiss();
                    }
                }){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Response<JSONObject> resp = super.parseNetworkResponse(response);
                if(!resp.isSuccess()) {
                    return resp;
                }
                long now = System.currentTimeMillis();
                Cache.Entry entry = resp.cacheEntry;
                if(entry == null) {
                    entry = new Cache.Entry();
                    entry.data = response.data;
                    entry.responseHeaders = response.headers;
                }
                entry.ttl = now + 3 * 60 * 60 * 1000;  //keeps cache for 3 hr

                return Response.success(resp.result, entry);
            }
        };
        queue.add(request);
        return view;
    }

    private void createTabs() {
        for (int i = 0; i < shows.size(); i++) {
            Show show = shows.get(i);
            Bundle bundle = new Bundle();
            bundle.putParcelable("show", show);
            tabHost.addTab(tabHost.newTabSpec(show.getTime()).setIndicator(show.getTime()), ShowTemplate.class, bundle);
            TextView tv = tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorText));
        }
    }
}
