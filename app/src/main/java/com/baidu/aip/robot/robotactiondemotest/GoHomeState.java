package com.baidu.aip.robot.robotactiondemotest;

/**
 * Created by sunyajie on 2019/3/22.
 */

public class GoHomeState implements IState {
    public GoHomeState() {
        this.state = STATE_INIT;
    }

    private int state;
    @Override
    public int getState() {
        return this.state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
    }
}
