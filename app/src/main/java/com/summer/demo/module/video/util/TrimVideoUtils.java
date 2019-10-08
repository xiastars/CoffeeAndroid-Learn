package com.summer.demo.module.video.util;

/**
 * Created by dell on 2017/6/22.
 */

import android.net.Uri;
import android.support.annotation.NonNull;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.FileDataSourceViaHeapImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * 裁剪视频工具类
 */
public class TrimVideoUtils {

    public static void startTrim(@NonNull File src, String outFilePath, long startMs, long endMs, @NonNull OnTrimVideoListener callback) throws IOException {
        File file = new File(outFilePath);
        genVideoUsingMp4Parser(src, file, startMs, endMs, callback);
    }

    private static void genVideoUsingMp4Parser(@NonNull File src, @NonNull File dst, long startMs, long endMs, @NonNull OnTrimVideoListener callback) throws IOException {
        // NOTE: Switched to using FileDataSourceViaHeapImpl since it does not use memory mapping (VM).
        // Otherwise we get OOM with large movie files.
        Movie movie = MovieCreator.build(new FileDataSourceViaHeapImpl(src.getAbsolutePath()));

        List<Track> tracks = movie.getTracks();
        movie.setTracks(new LinkedList<Track>());
        // remove all tracks we will create new tracks from the old

        for (Track track : tracks) {
            long currentSample = 0;
            double currentTime = 0;
            long startSample1 = -1L;
            long endSample1 = -1L;

            for (int i = 0; i < track.getSampleDurations().length; i++) {
                double delta = track.getSampleDurations()[i] * 1.0;
                if (currentTime <= startMs) {
                    // current sample is still before the new starttime
                    startSample1 = currentSample;
                } else if (currentTime <= endMs) {
//                     current sample is after the new start time and still before the new endtime
                    endSample1 = currentSample;
                } else {
                    break;
                }
                currentTime += (1000.0 * delta / track.getTrackMetaData().getTimescale());
                currentSample++;

            }
            movie.addTrack(new AppendTrack(new CroppedTrack(track, startSample1, endSample1)));
        }

        dst.getParentFile().mkdirs();

        if (!dst.exists()) {
            dst.createNewFile();
        }

        Container out = new DefaultMp4Builder().build(movie);

        FileOutputStream fos = new FileOutputStream(dst);
        FileChannel fc = fos.getChannel();
        out.writeContainer(fc);

        fc.close();
        fos.close();
        if (callback != null)
            callback.getResult(Uri.parse(dst.toString()));
    }

}