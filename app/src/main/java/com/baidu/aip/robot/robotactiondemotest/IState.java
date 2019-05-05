package com.baidu.aip.robot.robotactiondemotest;

/**
 * Created by sunyajie on 2019/3/22.
 */

public interface IState {
    int STATE_INIT = 0;
    int STATE_ING = 1;
    int STATE_PAUSE = 2;
    int STATE_ENDING = 3;

    int getState();
    void setState(int state);

    public enum ActionEvent {
        START_CRUISE, RESUNME_CRUISE, STOP_CRUISE, START_NAV, PAUSE_NAV;
    }
}
