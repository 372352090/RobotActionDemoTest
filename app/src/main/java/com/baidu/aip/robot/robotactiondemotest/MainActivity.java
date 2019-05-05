package com.baidu.aip.robot.robotactiondemotest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionConfig.Builder builder = new ActionConfig.Builder();
        ActionConfig config = builder.context(this).mapName("map1").originLocation("one").build();

        Log.d(TAG, "onCreate:getMapName " + config.getMapName());
        Log.d(TAG, "onCreate:getOrigin " + config.getOrigin());

        ActionConfig.Builder builder1 = new ActionConfig.Builder();
        builder1.context(this).originLocation("two").build();
        Log.d(TAG, "onCreate:1 getMapName " + config.getMapName());
        Log.d(TAG, "onCreate:1 getOrigin " + config.getOrigin());
    }
}
