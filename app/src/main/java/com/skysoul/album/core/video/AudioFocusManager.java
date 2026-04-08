package com.skysoul.album.core.video;

import android.content.Context;
import android.media.AudioManager;

public class AudioFocusManager {
    interface OnAudioFocusListener {
        void onAudioFocusChange();
    }

    private AudioManager audioManager = null;
    private OnAudioFocusListener focusListener = null;
    private AudioManager.OnAudioFocusChangeListener onAudioFocusListener = null;

    public void setOnAudioFocusListener(OnAudioFocusListener focusListener) {
        this.focusListener = focusListener;
    }

    public void requestAudioFocus(Context context) {
        if (audioManager == null) {
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        if (onAudioFocusListener == null) {
            onAudioFocusListener = new AudioManager.OnAudioFocusChangeListener(){

                @Override
                public void onAudioFocusChange(int focusChange) {
                    try {
                        if (focusChange != AudioManager.AUDIOFOCUS_GAIN) {
                            if (focusListener != null) {
                                focusListener.onAudioFocusChange();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }
        audioManager.requestAudioFocus(
                onAudioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    public void abandonAudioFocus() {
        if (audioManager != null){
            audioManager.abandonAudioFocus(onAudioFocusListener);
        }
    }
}
