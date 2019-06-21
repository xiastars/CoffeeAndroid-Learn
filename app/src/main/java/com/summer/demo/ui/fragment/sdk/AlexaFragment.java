package com.summer.demo.ui.fragment.sdk;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.View;

import com.summer.demo.R;
import com.summer.demo.base.BaseFragment;
import com.summer.helper.utils.Logs;
import com.willblaschko.android.alexa.AlexaManager;
import com.willblaschko.android.alexa.AuthorizationManager;
import com.willblaschko.android.alexa.audioplayer.AlexaAudioPlayer;
import com.willblaschko.android.alexa.callbacks.AsyncCallback;
import com.willblaschko.android.alexa.interfaces.AvsItem;
import com.willblaschko.android.alexa.interfaces.AvsResponse;
import com.willblaschko.android.alexa.interfaces.audioplayer.AvsPlayContentItem;
import com.willblaschko.android.alexa.interfaces.audioplayer.AvsPlayRemoteItem;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsMediaNextCommandItem;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsMediaPauseCommandItem;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsMediaPlayCommandItem;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsMediaPreviousCommandItem;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsReplaceAllItem;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsReplaceEnqueuedItem;
import com.willblaschko.android.alexa.interfaces.playbackcontrol.AvsStopItem;
import com.willblaschko.android.alexa.interfaces.speaker.AvsAdjustVolumeItem;
import com.willblaschko.android.alexa.interfaces.speaker.AvsSetMuteItem;
import com.willblaschko.android.alexa.interfaces.speaker.AvsSetVolumeItem;
import com.willblaschko.android.alexa.interfaces.speechrecognizer.AvsExpectSpeechItem;
import com.willblaschko.android.alexa.interfaces.speechsynthesizer.AvsSpeakItem;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.AUDIO_SERVICE;

/**
 * @Description: Amazon Alexa智能语音服务
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/6/20 16:13
 */
public class AlexaFragment extends BaseFragment {

    private AlexaManager alexaManager;
    private AlexaAudioPlayer audioPlayer;
    private List<AvsItem> avsQueue = new ArrayList<>();

    @Override
    protected void initView(View view) {
        initAlexaAndroid();
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.view_empty;
    }



    private void initAlexaAndroid(){
        //get our AlexaManager instance for convenience
        AuthorizationManager authorizationManager = new AuthorizationManager(context,"comszbalanx");
        authorizationManager.checkLoggedIn(context,new AsyncCallback<Boolean, Throwable>() {


            @Override
            public void start() {

            }

            @Override
            public void success(Boolean result) {

            }

            @Override
            public void failure(Throwable error) {

            }

            @Override
            public void complete() {

            }
        });
        alexaManager = AlexaManager.getInstance(context, "comszbalanx");


        //instantiate our audio player
        audioPlayer = AlexaAudioPlayer.getInstance(context);

        //Callback to be able to remove the current item and check queue once we've finished playing an item
        audioPlayer.addCallback(alexaAudioPlayerCallback);
    }

    //Our callback that deals with removing played items in our media player and then checking to see if more items exist
    private AlexaAudioPlayer.Callback alexaAudioPlayerCallback = new AlexaAudioPlayer.Callback() {
        @Override
        public void playerPrepared(AvsItem pendingItem) {

        }

        @Override
        public void playerProgress(AvsItem currentItem, long offsetInMilliseconds, float percent) {

        }

        @Override
        public void itemComplete(AvsItem completedItem) {
            avsQueue.remove(completedItem);
            checkQueue();
        }

        @Override
        public boolean playerError(AvsItem item, int what, int extra) {
            return false;
        }

        @Override
        public void dataError(AvsItem item, Exception e) {

        }


    };

    //async callback for commands sent to Alexa Voice
    private AsyncCallback<AvsResponse, Exception> requestCallback = new AsyncCallback<AvsResponse, Exception>() {
        @Override
        public void start() {
            //your on start code
        }

        @Override
        public void success(AvsResponse result) {
            Logs.i( "Voice Success");
            handleResponse(result);
        }

        @Override
        public void failure(Exception error) {
            //your on error code
        }

        @Override
        public void complete() {
            //your on complete code
        }
    };

    /**
     * Handle the response sent back from Alexa's parsing of the Intent, these can be any of the AvsItem types (play, speak, stop, clear, listen)
     * @param response a List<AvsItem> returned from the mAlexaManager.sendTextRequest() call in sendVoiceToAlexa()
     */
    private void handleResponse(AvsResponse response){
        if(response != null){
            //if we have a clear queue item in the list, we need to clear the current queue before proceeding
            //iterate backwards to avoid changing our array positions and getting all the nasty errors that come
            //from doing that
            for(int i = response.size() - 1; i >= 0; i--){
                if(response.get(i) instanceof AvsReplaceAllItem || response.get(i) instanceof AvsReplaceEnqueuedItem){
                    //clear our queue
                    avsQueue.clear();
                    //remove item
                    response.remove(i);
                }
            }
            avsQueue.addAll(response);
        }
        checkQueue();
    }


    /**
     * Check our current queue of items, and if we have more to parse (once we've reached a play or listen callback) then proceed to the
     * next item in our list.
     *
     * We're handling the AvsReplaceAllItem in handleResponse() because it needs to clear everything currently in the queue, before
     * the new items are added to the list, it should have no function here.
     */
    private void checkQueue() {

        //if we're out of things, hang up the phone and move on
        if (avsQueue.size() == 0) {
            return;
        }

        AvsItem current = avsQueue.get(0);

        if (current instanceof AvsPlayRemoteItem) {
            //play a URL
            if (!audioPlayer.isPlaying()) {
                audioPlayer.playItem((AvsPlayRemoteItem) current);
            }
        } else if (current instanceof AvsPlayContentItem) {
            //play a URL
            if (!audioPlayer.isPlaying()) {
                audioPlayer.playItem((AvsPlayContentItem) current);
            }
        } else if (current instanceof AvsSpeakItem) {
            //play a sound file
            if (!audioPlayer.isPlaying()) {
                audioPlayer.playItem((AvsSpeakItem) current);
            }
        } else if (current instanceof AvsStopItem) {
            //stop our play
            audioPlayer.stop();
            avsQueue.remove(current);
        } else if (current instanceof AvsReplaceAllItem) {
            audioPlayer.stop();
            avsQueue.remove(current);
        } else if (current instanceof AvsReplaceEnqueuedItem) {
            avsQueue.remove(current);
        } else if (current instanceof AvsExpectSpeechItem) {
            //listen for user input
            audioPlayer.stop();
            startListening();
        } else if (current instanceof AvsSetVolumeItem) {
            setVolume(((AvsSetVolumeItem) current).getVolume());
            avsQueue.remove(current);
        } else if(current instanceof AvsAdjustVolumeItem){
            adjustVolume(((AvsAdjustVolumeItem) current).getAdjustment());
            avsQueue.remove(current);
        } else if(current instanceof AvsSetMuteItem){
            setMute(((AvsSetMuteItem) current).isMute());
            avsQueue.remove(current);
        }else if(current instanceof AvsMediaPlayCommandItem){
            //fake a hardware "play" press
            sendMediaButton(context, KeyEvent.KEYCODE_MEDIA_PLAY);
        }else if(current instanceof AvsMediaPauseCommandItem){
            //fake a hardware "pause" press
            sendMediaButton(context, KeyEvent.KEYCODE_MEDIA_PAUSE);
        }else if(current instanceof AvsMediaNextCommandItem){
            //fake a hardware "next" press
            sendMediaButton(context, KeyEvent.KEYCODE_MEDIA_NEXT);
        }else if(current instanceof AvsMediaPreviousCommandItem){
            //fake a hardware "previous" press
            sendMediaButton(context, KeyEvent.KEYCODE_MEDIA_PREVIOUS);
        }

    }

    private void startListening(){
        Logs.i("开始收听");
    }

    //adjust our device volume
    private void adjustVolume(long adjust){
        setVolume(adjust, true);
    }

    //set our device volume
    private void setVolume(long volume){
        setVolume(volume, false);
    }

    //set our device volume, handles both adjust and set volume to avoid repeating code
    private void setVolume(final long volume, final boolean adjust){
        AudioManager am = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        final int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        long vol= am.getStreamVolume(AudioManager.STREAM_MUSIC);
        if(adjust){
            vol += volume * max / 100;
        }else{
            vol = volume * max / 100;
        }
        am.setStreamVolume(AudioManager.STREAM_MUSIC, (int) vol, AudioManager.FLAG_VIBRATE);
        //confirm volume change
        alexaManager.sendVolumeChangedEvent(volume, vol == 0, requestCallback);
    }

    //set device to mute
    private void setMute(final boolean isMute){
        AudioManager am = (AudioManager)context. getSystemService(AUDIO_SERVICE);
        am.setStreamMute(AudioManager.STREAM_MUSIC, isMute);
        //confirm device mute
        alexaManager.sendMutedEvent(isMute, requestCallback);
    }

    private static void sendMediaButton(Context context, int keyCode) {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
        context.sendOrderedBroadcast(intent, null);

        keyEvent = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
        intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
        context.sendOrderedBroadcast(intent, null);
    }
}
