package giladoved.chicagolivejazz.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import giladoved.chicagolivejazz.Models.Show;
import giladoved.chicagolivejazz.R;


public class ShowTemplate extends Fragment {

    public static String YOUTUBE_API_KEY;

    YouTubePlayerSupportFragment videoPlayer;
    TextView headlineTxt;
    TextView timeTxt;
    TextView priceTxt;
    TextView detailsTxt;

    Show show;

    public ShowTemplate() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            show = getArguments().getParcelable("show");
            if (show == null) show = new Show();
        }

        View view = inflater.inflate(R.layout.fragment_show_template, container, false);

        YOUTUBE_API_KEY = view.getContext().getString(R.string.youtube_api_key);

        videoPlayer = new YouTubePlayerSupportFragment();
        headlineTxt = view.findViewById(R.id.headline);
        timeTxt = view.findViewById(R.id.time);
        priceTxt = view.findViewById(R.id.price);
        detailsTxt = view.findViewById(R.id.details);

        headlineTxt.setText(show.getHeadline());
        timeTxt.setText(show.getTime());
        priceTxt.setText(show.getPrice());
        detailsTxt.setText(show.getDetails());
        String video = show.getVideo();
        if (video != null) {
            initializeVideo(video);
        }

        return view;
    }

    private void initializeVideo(final String video) {
        videoPlayer.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    player.cueVideo(video);
                    player.setShowFullscreenButton(false);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
                Toast.makeText(getActivity(), "Error loading youtube video", Toast.LENGTH_SHORT).show();
            }
        });
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.video, videoPlayer);
        fragmentTransaction.commit();
    }

}
