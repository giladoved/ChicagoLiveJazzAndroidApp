package giladoved.chicagolivejazz.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
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


public class GreenMill extends Fragment {

    public static String YOUTUBE_API_KEY;
    public static String API_BASE_URL;

    YouTubePlayerView videoPlayerView;

    TextView headlineTxt;
    TextView timeTxt;
    TextView priceTxt;
    TextView detailsTxt;

    List<Show> shows;

    public GreenMill() {
        shows = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_green_mill, container, false);

        YOUTUBE_API_KEY = view.getContext().getString(R.string.youtube_api_key);
        API_BASE_URL = view.getContext().getString(R.string.api_base_url);

        videoPlayerView = view.findViewById(R.id.video);
        headlineTxt = view.findViewById(R.id.headline);
        timeTxt = view.findViewById(R.id.time);
        priceTxt = view.findViewById(R.id.price);
        detailsTxt = view.findViewById(R.id.details);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(API_BASE_URL + "/api/greenmill", new JsonHttpResponseHandler() {
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
                        displayData();
                    } else {
                        Toast.makeText(getActivity(), "No shows today!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "There was an error while trying to retreive show info", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void displayData() {
        Show show = shows.get(0);
        headlineTxt.setText(show.getHeadline());
        timeTxt.setText(show.getTime());
        priceTxt.setText(show.getPrice());
        detailsTxt.setText(show.getDetails());
        String video = show.getVideo();
        if (video != null) {
            initializeVideo(video);
        }
    }

    private void initializeVideo(final String video) {
        videoPlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    player.cueVideo(video);
                    player.setShowFullscreenButton(false);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
                String errorMessage = String.format("YouTube Error (%1$s)", errorReason.toString());
                Log.e("chicago-live-jazz", "Error loading youtube: " + errorMessage);
                Toast.makeText(getActivity(), "Error loading youtube video", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
