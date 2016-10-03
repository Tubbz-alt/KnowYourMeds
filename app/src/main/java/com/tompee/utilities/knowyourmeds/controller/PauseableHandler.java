package com.tompee.utilities.knowyourmeds.controller;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.Vector;

public class PauseableHandler extends Handler {
    private final Vector<Message> mMessageQueueBuffer = new Vector<>();
    private final WeakReference<PauseableHandlerCallback> mCallBack;
    private boolean mPaused = false;

    public PauseableHandler(PauseableHandlerCallback callback) {
        mCallBack = new WeakReference<>(callback);
    }

    final public void resume() {
        mPaused = false;

        while (mMessageQueueBuffer.size() > 0) {
            final Message msg = mMessageQueueBuffer.elementAt(0);
            mMessageQueueBuffer.removeElementAt(0);
            sendMessage(msg);
        }
    }

    final public void pause() {
        mPaused = true;
    }

    @Override
    final public void handleMessage(Message msg) {
        if (mCallBack.get() != null) {
            if (mPaused) {
                if (mCallBack.get().storeMessage(msg)) {
                    Message msgCopy = new Message();
                    msgCopy.copyFrom(msg);
                    mMessageQueueBuffer.add(msgCopy);
                }
            } else {
                mCallBack.get().processMessage(msg);
            }
        }
    }

    public interface PauseableHandlerCallback {
        boolean storeMessage(Message message);

        void processMessage(Message message);
    }
}
