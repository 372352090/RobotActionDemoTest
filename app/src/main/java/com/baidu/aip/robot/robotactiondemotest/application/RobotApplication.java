package com.baidu.aip.robot.robotactiondemotest.application;

import android.app.Application;
import android.util.Log;

import com.baidu.aip.robot.mylibrary.AbcRosInterface;
import com.baidu.aip.robot.mylibrary.AbcSpeechControlInterface;
import com.uurobot.u05sdkbaidu.ReceiveDataService;

/**
 * Created by sunyajie on 2019/4/1.
 */

public class RobotApplication extends Application {
    private static ReceiveDataService receiveDataService;
    @Override
    public void onCreate() {
        super.onCreate();
        receiveDataService = ReceiveDataService.getInstance();
        receiveDataService.init(this);
        receiveDataService.registerSpeechLisenter(new AbcSpeechControlInterface.ThirdSpeechCallBack() {
            @Override
            public void onThirdStatus(int status) {

            }

            @Override
            public void onThirdNewSpeech(int id, String speech, boolean isFinal) {

            }

            @Override
            public void onThirdNewAnswer(String answer) {

            }

            @Override
            public void onThirdWakeUp(String word, int wakeUpAngle) {

            }

            @Override
            public void onError(int errorCode) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("klylaction", "initActionModule load map 开始加载地图");
                boolean loadMap = receiveDataService.loadMap("百度公司");
                if (loadMap) {
                    Log.d("klylaction", "initActionModule load map 加载地图成功");
                    receiveDataService.relocation(new AbcRosInterface.RelocationCallback() {
                        @Override
                        public void onRelocationSuccess(AbcRosInterface.Location location) {
                            Log.d("klylaction", "initActionModule relocation 地图复位成功");
                        }

                        @Override
                        public void onRelocationFail(String reason) {
                            Log.d("klylaction", "initActionModule relocation 地图复位失败");
                        }
                    });
                } else {
                    Log.d("klylaction", "initActionModule load map 地图加载失败");
                }
            }
        }).start();
    }
}
