package by.itrex.jetfirer.mediaplayer.widget;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.itrex.jetfirer.mediaplayer.R;

/**
 * Created by Konstantin on 24.04.2015.
 */
public class MediaPlayerController extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_player_controller, null);

        return view;
    }
}
