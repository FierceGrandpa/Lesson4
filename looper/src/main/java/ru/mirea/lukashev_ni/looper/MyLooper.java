package ru.mirea.lukashev_ni.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MyLooper extends Thread {
    public Handler mHandler;
    private Handler mainHandler;
    public MyLooper(Handler mainThreadHandler) {
        mainHandler =mainThreadHandler;
    }
    public void run() {
        Log.d("MyLooper", "run");
        Looper.prepare();
        mHandler = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                int age = Integer.parseInt(msg.getData().getString("age"));
                String work = msg.getData().getString("work");
                try {
                    Thread.sleep(age * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String result = String.format("Your age is %d and you work is %s", age, work);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                // Send the message back to main thread message queue use main thread message Handler.
                mainHandler.sendMessage(message);
            }
        };
        Looper.loop();
    }
}
