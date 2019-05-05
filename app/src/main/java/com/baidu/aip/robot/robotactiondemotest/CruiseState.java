package com.baidu.aip.robot.robotactiondemotest;

/**
 * Created by sunyajie on 2019/3/22.
 */

public class CruiseState implements IState {
    private int state;

    public CruiseState() {
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


    public boolean isProcessEvent(ActionEvent actionEvent) {
        boolean isProcessEvent = false;
        if (state == STATE_INIT) {
            switch (actionEvent) {
                case START_CRUISE:
                    isProcessEvent =  true;
                    break;
                case PAUSE_NAV:
                case RESUNME_CRUISE:
                case START_NAV:
                case STOP_CRUISE:
                    isProcessEvent = false;
                    break;
            }
        } else if (state == STATE_ING) {
            switch (actionEvent) {
                case START_CRUISE:
                    isProcessEvent =  false;
                    break;
                case PAUSE_NAV:
                    isProcessEvent = true;
                    break;
                case RESUNME_CRUISE:
                    isProcessEvent = false;
                    break;
                case START_NAV:
                    isProcessEvent = false;
                    break;
                case STOP_CRUISE:
                    isProcessEvent = true;
                    break;

            }
        } else if (state == STATE_PAUSE){
            switch (actionEvent) {
                case START_CRUISE:
                    isProcessEvent =  true;
                    break;
                case PAUSE_NAV:
                    isProcessEvent = false;
                    break;
                case RESUNME_CRUISE:
                    isProcessEvent = false;
                    break;
                case START_NAV:
                    isProcessEvent = false;
                    break;
                case STOP_CRUISE:
                    isProcessEvent = false;
                    break;

            }
        }

        return false;
    }
}
