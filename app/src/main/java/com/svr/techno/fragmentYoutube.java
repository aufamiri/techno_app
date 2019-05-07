package com.svr.techno;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.svr.techno.Adapters.Models.ItemModel;

public class fragmentYoutube extends Fragment {
    private ItemModel itemModel;
    private YouTubePlayerView youtubePlayer;

    public FullScreenHelper fullScreenHelper = new FullScreenHelper(getActivity());

    @Override
    public void onCreate(@Nullable Bundle savedInstacedState) {
        super.onCreate(savedInstacedState);

        if (getArguments() != null) {
            itemModel = (ItemModel) getArguments().getSerializable("item");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_youtube, parent, false);

        TextView detailsText = view.findViewById(R.id.details_text);
        detailsText.setText(itemModel.getDescription());

        youtubePlayer = view.findViewById(R.id.youtube_view);
        initYoutubePlayer();
        return view;
    }

/*
    @Override
    public void onBackPressed() {
        if (youtubePlayer.isFullScreen()) {
            youtubePlayer.exitFullScreen();
        }
        else
        {
            super.onBackPressed();

        }
    }
*/

//TODO: ipmlement onBackPressed on fragment
    private void initYoutubePlayer() {
        getLifecycle().addObserver(youtubePlayer);

        youtubePlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                String videoId = "G-YNNJIe2Vk"; //TODO: get videoID from itemModel
                youTubePlayer.cueVideo(videoId, 0);
            }
        });
        youtubePlayer.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                fullScreenHelper.enterFullScreen();
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                fullScreenHelper.exitFullScreen();
            }
        });
    }


}
