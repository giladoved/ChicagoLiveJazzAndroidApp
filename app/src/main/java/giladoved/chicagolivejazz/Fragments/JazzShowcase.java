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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import giladoved.chicagolivejazz.Models.Show;
import giladoved.chicagolivejazz.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class JazzShowcase extends Fragment {

    public static String API_BASE_URL;

    List<Show> shows;
    FragmentTabHost tabHost;

    public JazzShowcase() {
        shows = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jazz_showcase, container, false);

        API_BASE_URL = view.getContext().getString(R.string.api_base_url);

        tabHost = view.findViewById(android.R.id.tabhost);
        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(API_BASE_URL + "/api/jazzshowcase", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Log.d("chicago-live-jazz", jsonObject + " ");
                try {
                    JSONArray showsJson = jsonObject.getJSONArray("events");
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

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "There was an error while trying to retreive show info", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject error) {
                Toast.makeText(getActivity(), "There was an error retrieving the info for this jazz club.", Toast.LENGTH_SHORT).show();
                Log.e("chicago-live-jazz", error.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String error, Throwable throwable) {
                Toast.makeText(getActivity(), "There was an error retrieving the info for this jazz club.", Toast.LENGTH_SHORT).show();
                Log.e("chicago-live-jazz", error);
            }
        });

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
