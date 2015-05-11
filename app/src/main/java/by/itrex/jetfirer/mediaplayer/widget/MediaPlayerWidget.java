package by.itrex.jetfirer.mediaplayer.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import by.itrex.jetfirer.mediaplayer.R;
import by.itrex.jetfirer.mediaplayer.model.Track;
import by.itrex.jetfirer.mediaplayer.service.MediaPlayerService;
import by.itrex.jetfirer.mediaplayer.util.Utils;

/**
 * Created by Konstantin on 11.05.2015.
 */
public class MediaPlayerWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent intent = new Intent(context, MediaPlayerWidget.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.media_player_widget);
        remoteViews.setOnClickPendingIntent(R.id.root_widget, pendingIntent);

        MediaPlayerService mediaPlayerService = MediaPlayerService.getInstance();
        mediaPlayerService.initDefaultPlaylist(context);

        Track track = mediaPlayerService.getCurrentTrack();
        if (track != null) {
            remoteViews.setTextViewText(R.id.playing_title, track.getTitle());
            remoteViews.setTextViewText(R.id.playing_artist, track.getArtist());
            remoteViews.setTextViewText(R.id.max_duration, Utils.convertDuration(track.getDuration()));
            remoteViews.setTextViewText(R.id.playing_duration, Utils.convertDuration(track.getDuration() / 2));
            remoteViews.setProgressBar(R.id.track_duration_progress, track.getDuration(), track.getDuration() / 2, false);
        }

        // Tell the widget manager
        appWidgetManager.updateAppWidget(appWidgetIds[0], remoteViews);
    }
}
