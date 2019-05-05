package com.baidu.aip.robot.mylibrary;

/**
 * Created by sunyajie on 2019/3/21.
 */

public interface AbcSpeechControlInterface {


    public final static int VOLUME_TYPE_MINIMUM = 1;  // 设置音量类型最小
    public final static int VOLUME_TYPE_MAXNUM = 2;   // 设置音量类型最大
    public final static int VOLUME_TYPE_INCREASE = 3;  // 设置音量类型增加
    public final static int VOLUME_TYPE_DECREASE = 4;  // 设置音量类型减少
    public final static int VOLUME_TYPE_SET_VALUE = 5;  // 设置音量具体值

    void registerSpeechLisenter(ThirdSpeechCallBack callBack);

    void startWakeUp();

    void stopWakeUp();

    void startASR();

    void stopASR();

    void startTTS(String speech, boolean isWakeUpMode);

    void stopTTS();

    void palyMusic(String path, boolean isNetworkPath, PlayCallback callback);

    void randomPlayMusic(String path, boolean isNetworkPath, PlayCallback callback);

    void pauseMusic(PlayCallback callback);

    void resumeMusic(PlayCallback callback);

    void stopMusic(PlayCallback callback);

    void palyVideo(String path, PlayCallback callback);

    void pauseVideo(PlayCallback callback);

    void resumeVideo(PlayCallback callback);

    void stopVideo(PlayCallback callback);


    /**
     * 设置调节音量
     * @param type  详见 VOLUME_TYPE
     * @param volume  音量值
     * @param callback  设置相关回调
     */
    void setVolume(int type, float volume, VolumeCallback callback);

    void setHeadWifi(String ssId, String ssPwd, SetWifiCallback callback);

    boolean isSerialReady();

    /**
     * 获取网络状态
     * @param callback
     */
    void getNetworkState(boolean isNeedNetWorkCount, NetworkStateCallback callback);

    public interface SetWifiCallback {
        void onSuccess();
        void onFail(String reason);
    }

    /**
     * 网络状态回调
     */
    public interface NetworkStateCallback {

        public static final int STATE_NETWORK_DISCONNECTED = 0;
        public static final int STATE_NETWORK_CONNECTING = 1;
        public static final int STATE_NETWORK_CONNECTED = 2;
        void onSuccess(String state, String ssId, String pwd);

        void onFail(String reason);
    }

    /**
     * 播放状态回调
     */
    public interface PlayCallback {

        void onPlaySuccess(String path);

        void onPlayFail(String reason);

        void onPause();

        void onStop();

        void onResume();
    }

    public interface VolumeCallback{
        void onSetVolumeSuccess();

        void onSetVolumeFail(String reason);
    }


    /**
     * 语音识别回调
     */
    public interface ThirdSpeechCallBack {
        /**
         * @param status
         */
        public void onThirdStatus(int status);// status

        public void onThirdNewSpeech(int id, String speech, boolean isFinal);

        public void onThirdNewAnswer(String answer);

        public void onThirdWakeUp(String word, int wakeUpAngle);

        public static final int ERROR_TTS_FAIL = 1;

        public static final int ERROR_STOP_TTS_FAIL = 2;

        public static final int ERROR_START_ASR_FAIL = 3;

        public static final int ERROR_STOP_ASR_FAIL = 4;

        public static final int ERROR_SET_WIFI_FAIL = 5;

//        public static final int ERROR_STOP_TTS_FAIL = 6;
        /**
         * @param errorCode 对应以上整数类型进行错误回调
         */
        public void onError(int errorCode);
    }
}
