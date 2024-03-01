package com.example.hw1.Utilities;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import com.example.hw1.Interface.OnPlaybackCompletionListener;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;



public class BackgroundSound {
        private Context context;
        private Executor executor;
        private Handler handler;
        private MediaPlayer mediaPlayer;
        private int resID;
        private OnPlaybackCompletionListener completionListener;

        public BackgroundSound(Context context, int resId) {
            this.context = context;
            this.executor = Executors.newSingleThreadExecutor();
            this.handler = new Handler(Looper.getMainLooper());
            this.resID = resId;
        }
    public void setOnCompletionListener(OnPlaybackCompletionListener listener) {
        this.completionListener = listener;
    }

        public void playSound(){
            executor.execute(() -> {
                try {
                    mediaPlayer = MediaPlayer.create(context, this.resID);
                    mediaPlayer.setLooping(false); // Set looping to false
                    mediaPlayer.setVolume(1.0f, 1.0f);
                    mediaPlayer.setOnCompletionListener(mp -> {
                        if (completionListener != null) {
                            handler.post(() -> {
                                completionListener.onCompletion();
                                // Remove the completion listener after it's been triggered
                                mediaPlayer.setOnCompletionListener(null);
                            });
                        }
                    });
                    mediaPlayer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle or log the exception
                }
            });
        }

        public void stopSound(){
            executor.execute(() -> {
                try {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.setLooping(false);
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle or log the exception
                }
            });
        }

    }

