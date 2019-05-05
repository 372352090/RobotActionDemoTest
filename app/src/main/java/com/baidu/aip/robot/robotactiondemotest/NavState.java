package com.baidu.aip.robot.robotactiondemotest;

/**
 * Created by sunyajie on 2019/3/22.
 */

public class NavState implements IState {
    private int state;

    public NavState() {
        this.state = STATE_INIT;
    }

    @Override
    public int getState() {
        return this.state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
    }
}
