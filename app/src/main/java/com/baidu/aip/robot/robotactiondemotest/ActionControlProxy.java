package com.baidu.aip.robot.robotactiondemotest;

import android.util.Log;

import com.baidu.aip.robot.mylibrary.AbcRosInterface;

import java.util.List;

import static com.baidu.aip.robot.robotactiondemotest.IState.STATE_ENDING;
import static com.baidu.aip.robot.robotactiondemotest.IState.STATE_ING;
import static com.baidu.aip.robot.robotactiondemotest.IState.STATE_INIT;
import static com.baidu.aip.robot.robotactiondemotest.IState.STATE_PAUSE;

/**
 * Created by sunyajie on 2019/3/22.
 */

public class ActionControlProxy implements AbcRosInterface {

    private static final String TAG = "ActionControlProxy";
    private AbcRosInterface actionEngine;
    public static final int ACTION_TYPE_FUBAO = 1;
    public static final int ACTION_TYPE_KLYL = 2;
    private ActionConfig mConfig;
    private CruiseState cruiseState = new CruiseState();
    private NavState navState = new NavState();
    private GoHomeState goHomeState = new GoHomeState();


    public ActionControlProxy(AbcRosInterface actionEngine) {
//        switch (type) {
//            case ACTION_TYPE_FUBAO:
//                break;
//            case ACTION_TYPE_KLYL:
//                this.actionEngine = actionEngine;
//                break;
//            default:
//                break;
//        }
        this.actionEngine = actionEngine;
    }

    public void initActionModule(ActionConfig config) {
        this.mConfig = config;
        loadMap(config.getMapName());

    }

    public boolean isSuccessStopMoving() {
        if (navState.getState() == STATE_ING) {
            return false;
        } else {
            if (cruiseState.getState() == STATE_ING) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        pauseCruise(null);
                    }
                }).start();
                return true;
            }
            if (goHomeState.getState() == STATE_ING) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        cancelNavigate(null);
                        goHomeState.setState(STATE_PAUSE);
                    }
                }).start();
                return true;
            }
        }
//        其他状况都返回true，交给demo处理。
        return true;
    }

    public boolean isBackToWakeUp() {
        if (cruiseState.getState() == STATE_PAUSE) {
//            恢复巡航
            new Thread(new Runnable() {
                @Override
                public void run() {
                    resumeCruise();
                }
            }).start();
            return false;
        } else if (goHomeState.getState() == STATE_PAUSE) {
//            恢复回家
            new Thread(new Runnable() {
                @Override
                public void run() {
                    goToHome();
                }
            }).start();
            return false;
        }
        return true;
    }


    @Override
    public boolean loadMap(String mapName) {
        return actionEngine.loadMap(mapName);
    }

    @Override
    public String getMapName() {
        return actionEngine.getMapName();
    }

    @Override
    public List<String> getMapList() {
        return actionEngine.getMapList();
    }

    @Override
    public List<Location> getMapPointList() {
        return actionEngine.getMapPointList();
    }

    @Override
    public Location getCurrentPosition() {
        return actionEngine.getCurrentPosition();
    }

    @Override
    public void startFaceTrace(ActionCallBack actionCallBack) {
        boolean flag = isExecuteAction();
        if (flag) {
            actionEngine.startFaceTrace(actionCallBack);
        }
    }

    @Override
    public boolean stopFaceTrace() {
        boolean flag = isExecuteAction();
        if (flag) {
            return actionEngine.stopFaceTrace();
        }
        return false;
    }

    @Override
    public void showHeadExpression(String expressionId, ActionCallBack callBack) {
        actionEngine.showHeadExpression(expressionId, callBack);
    }

    @Override
    public void listenTouchEvent(TouchCallback callback) {
        actionEngine.listenTouchEvent(callback);
    }

    @Override
    public void relocation(Location location, RelocationCallback callback) {
        actionEngine.relocation(new Location("jia",0,0), callback);
    }


    @Override
    public void registerRosListener(RosListener rosListener) {
        actionEngine.registerRosListener(rosListener);
    }

    @Override
    public void registerObjectDetectListener(ObjectDetectCallback objectDetectListener) {
        actionEngine.registerObjectDetectListener(objectDetectListener);
    }

    @Override
    public float getBatteryLevel() {
        return actionEngine.getBatteryLevel();
    }

    @Override
    public float getBatteryScale() {
        return actionEngine.getBatteryScale();
    }

    @Override
    public boolean startMove(float distance, String si, ActionCallBack callBack) {
        boolean flag = isExecuteAction();
        if (flag) {
            return actionEngine.startMove(distance, si, callBack);
        }
        return false;
    }

    @Override
    public boolean startBodyTurn(float angle, ActionCallBack callBack) {
        boolean flag = isExecuteAction();
        if (flag) {
            return actionEngine.startBodyTurn(angle, callBack);
        }
        return false;
    }

    @Override
    public boolean startHeadTurn(int direction, float angle, ActionCallBack callBack) {
        boolean flag = isExecuteAction();
        if (flag) {
            return actionEngine.startHeadTurn(direction, angle, callBack);
        }
        return false;
    }

    @Override
    public boolean stopAction(int actionType, ActionCallBack callBack) {
        return actionEngine.stopAction(actionType, callBack);
    }

    public void startNav(Location location, final ActionCallBack callBack) {
        boolean flag = isExecuteStartNav();
        if (flag) {
            navState.setState(STATE_ING);
            goHomeState.setState(STATE_INIT);
            navigateToLocation(location, new ActionCallBack() {
                @Override
                public void onActionProgress(int actionType, float progress) {
                    callBack.onActionProgress(actionType, progress);
                }

                @Override
                public void onActionFail(int actionType, String message) {
                    callBack.onActionFail(actionType, message);
                    navState.setState(IState.STATE_ENDING);
                }

                @Override
                public void onActionStart(int actionType) {
                    callBack.onActionStart(actionType);
                }

                @Override
                public void onActionComplete(int actionType) {
                    navState.setState(IState.STATE_ENDING);
                    callBack.onActionComplete(actionType);
                }

                @Override
                public void onReachedCruisePoint(String cruisePoint) {

                }
            });
        } else {
            Log.e(TAG, "startNav: Naving can't repeat start NavMethod");
        }
    }

    private boolean isExecuteStartNav() {
        //        巡航当前状态是否可以执行该动作
        boolean isCruise = false;
        switch (cruiseState.getState()) {
            case STATE_INIT:
                isCruise = true;
                break;
            case STATE_ING:
                isCruise = false;
                break;
            case STATE_PAUSE:
                isCruise = true;
                break;
            case STATE_ENDING:
                isCruise = true;
                break;
        }
//        导航当前状态是否可以执行当前动作
        boolean isNav = false;
        switch (navState.getState()) {
            case STATE_INIT:
                isNav = true;
                break;
            case STATE_ING:
                isNav = false;
                break;
            case STATE_PAUSE:
                isNav = true;
                break;
            case STATE_ENDING:
                isNav = true;
                break;
        }
//        回家当前状态是否可以执行当前动作
        boolean isHome = false;
        switch (goHomeState.getState()) {
            case STATE_INIT:
                isHome = true;
                break;
            case STATE_ING:
                isHome = false;
                break;
            case STATE_PAUSE:
                isHome = true;
                break;
            case STATE_ENDING:
                isHome = true;
                break;
        }
        if (isCruise && isNav && isHome) {
            return true;
        }
        return false;
    }

    public void startCruise(final ActionCallBack callBack) {
//        判断当前状态是否可以执行
        boolean flag = isExecuteStartCruise();
        Location[] locations = mConfig.getLocations();
        if (mConfig.getLocations().length < 2) {
            locations = (Location[])getMapPointList().toArray();
        }
        if (flag) {
            cruiseState.setState(STATE_ING);
            goHomeState.setState(STATE_INIT);
            cruiseToLocations(mConfig.getLocations(), new ActionCallBack() {
                @Override
                public void onActionProgress(int actionType, float progress) {
                    callBack.onActionProgress(actionType, progress);
                }

                @Override
                public void onActionFail(int actionType, String message) {
                    cruiseState.setState(STATE_ENDING);
                    callBack.onActionFail(actionType, message);
                }

                @Override
                public void onActionStart(int actionType) {
                    callBack.onActionStart(actionType);
                }

                @Override
                public void onActionComplete(int actionType) {
                    callBack.onActionComplete(actionType);
                }

                @Override
                public void onReachedCruisePoint(String cruisePoint) {
                    callBack.onReachedCruisePoint(cruisePoint);
                }
            });
        }
    }

    private boolean isExecuteStartCruise() {
        //        巡航当前状态是否可以执行该动作
        boolean isCruise = false;
        switch (cruiseState.getState()) {
            case STATE_INIT:
                isCruise = true;
                break;
            case STATE_ING:
                isCruise = false;
                break;
            case STATE_PAUSE:
                isCruise = false;
                break;
            case STATE_ENDING:
                isCruise = true;
                break;
        }
//        导航当前状态是否可以执行当前动作
        boolean isNav = false;
        switch (navState.getState()) {
            case STATE_INIT:
                isNav = true;
                break;
            case STATE_ING:
                isNav = false;
                break;
            case STATE_PAUSE:
                isNav = true;
                break;
            case STATE_ENDING:
                isNav = true;
                break;
        }
//        回家当前状态是否可以执行当前动作
        boolean isHome = false;
        switch (goHomeState.getState()) {
            case STATE_INIT:
                isHome = true;
                break;
            case STATE_ING:
                isHome = false;
                break;
            case STATE_PAUSE:
                isHome = true;
                break;
            case STATE_ENDING:
                isHome = true;
                break;
        }
        if (isCruise && isNav && isHome) {
            return true;
        }
        return false;
    }

    @Override
    public boolean navigateToLocation(Location location, ActionCallBack callBack) {
        return actionEngine.navigateToLocation(location, callBack);
    }

    @Override
    public boolean cruiseToLocations(Location[] locations, ActionCallBack callBack) {
        return actionEngine.cruiseToLocations(locations, callBack);
    }

    public void goToHome() {
        boolean flag = isExecuteStartGoHome();
        if (flag) {
            goHomeState.setState(STATE_ING);
            goToOrigin(new ActionCallBack() {
                @Override
                public void onActionProgress(int actionType, float progress) {

                }

                @Override
                public void onActionFail(int actionType, String message) {
                    goHomeState.setState(STATE_ENDING);
                }

                @Override
                public void onActionStart(int actionType) {

                }

                @Override
                public void onActionComplete(int actionType) {
                    goHomeState.setState(STATE_ENDING);
                }

                @Override
                public void onReachedCruisePoint(String cruisePoint) {

                }
            });
        }
    }

    private boolean isExecuteStartGoHome() {
        //        巡航当前状态是否可以执行该动作
        boolean isCruise = false;
        switch (cruiseState.getState()) {
            case STATE_INIT:
                isCruise = true;
                break;
            case STATE_ING:
                isCruise = false;
                break;
            case STATE_PAUSE:
                isCruise = false;
                break;
            case STATE_ENDING:
                isCruise = true;
                break;
        }
//        导航当前状态是否可以执行当前动作
        boolean isNav = false;
        switch (navState.getState()) {
            case STATE_INIT:
                isNav = true;
                break;
            case STATE_ING:
                isNav = false;
                break;
            case STATE_PAUSE:
                isNav = true;
                break;
            case STATE_ENDING:
                isNav = true;
                break;
        }
//        回家当前状态是否可以执行当前动作
        boolean isHome = false;
        switch (goHomeState.getState()) {
            case STATE_INIT:
                isHome = true;
                break;
            case STATE_ING:
                isHome = false;
                break;
            case STATE_PAUSE:
                isHome = true;
                break;
            case STATE_ENDING:
                isHome = false;
                break;
        }
        if (isCruise && isNav && isHome) {
            return true;
        }
        return false;
    }

    @Override
    public boolean goToOrigin(ActionCallBack callBack) {
        return actionEngine.goToOrigin(callBack);
    }

    public void pauseCruise(final ActionCallBack callBack) {
        boolean flag = isExecutePauseCruise();
        if (flag) {
            cruiseState.setState(STATE_PAUSE);
            pauseNavigate(new ActionCallBack() {
                @Override
                public void onActionProgress(int actionType, float progress) {
                    callBack.onActionProgress(actionType, progress);
                }

                @Override
                public void onActionFail(int actionType, String message) {
                    callBack.onActionFail(actionType, message);
                }

                @Override
                public void onActionStart(int actionType) {
                    callBack.onActionStart(actionType);
                }

                @Override
                public void onActionComplete(int actionType) {
                    callBack.onActionComplete(actionType);
                }

                @Override
                public void onReachedCruisePoint(String cruisePoint) {
                    callBack.onReachedCruisePoint(cruisePoint);
                }
            });
        }
    }

    private boolean isExecutePauseCruise() {
//        巡航当前状态是否可以执行该动作
        boolean isCruise = false;
        switch (cruiseState.getState()) {
            case STATE_INIT:
                isCruise = true;
                break;
            case STATE_ING:
                isCruise = true;
                break;
            case STATE_PAUSE:
                isCruise = false;
                break;
            case STATE_ENDING:
                isCruise = false;
                break;
        }
//        导航当前状态是否可以执行当前动作
        boolean isNav = false;
        switch (navState.getState()) {
            case STATE_INIT:
                isNav = true;
                break;
            case STATE_ING:
                isNav = false;
                break;
            case STATE_PAUSE:
                isNav = true;
                break;
            case STATE_ENDING:
                isNav = true;
                break;
        }
//        回家当前状态是否可以执行当前动作
        boolean isHome = false;
        switch (goHomeState.getState()) {
            case STATE_INIT:
                isHome = true;
                break;
            case STATE_ING:
                isHome = true;
                break;
            case STATE_PAUSE:
                isHome = true;
                break;
            case STATE_ENDING:
                isHome = true;
                break;
        }
        if (isCruise && isNav && isHome) {
            return true;
        }
        return false;
    }

    @Override
    public boolean pauseNavigate(ActionCallBack callBack) {
        return actionEngine.pauseNavigate(callBack);
    }

    public void cancelGoHomeAndCruise() {
        boolean flag = isCancelNavAndCruise();
        if (flag) {
            cruiseState.setState(STATE_ENDING);
            goHomeState.setState(STATE_PAUSE);
            cancelNavigate(null);
        }
    }

    private boolean isCancelNavAndCruise() {
        //        巡航当前状态是否可以执行该动作
        boolean isCruise = false;
        switch (cruiseState.getState()) {
            case STATE_INIT:
                isCruise = true;
                break;
            case STATE_ING:
                isCruise = true;
                break;
            case STATE_PAUSE:
                isCruise = true;
                break;
            case STATE_ENDING:
                isCruise = true;
                break;
        }
//        导航当前状态是否可以执行当前动作
        boolean isNav = false;
        switch (navState.getState()) {
            case STATE_INIT:
                isNav = true;
                break;
            case STATE_ING:
                isNav = false;
                break;
            case STATE_PAUSE:
                isNav = true;
                break;
            case STATE_ENDING:
                isNav = true;
                break;
        }
//        回家当前状态是否可以执行当前动作
        boolean isHome = false;
        switch (goHomeState.getState()) {
            case STATE_INIT:
                isHome = true;
                break;
            case STATE_ING:
                isHome = true;
                break;
            case STATE_PAUSE:
                isHome = true;
                break;
            case STATE_ENDING:
                isHome = true;
                break;
        }
        if (isCruise && isNav && isHome) {
            return true;
        }
        return false;
    }

    @Override
    public boolean cancelNavigate(ActionCallBack callBack) {
        return actionEngine.cancelNavigate(callBack);
    }

    public void resumeCruise() {
        boolean flag = isResumeCruise();
        if (flag) {
            cruiseState.setState(STATE_ING);
            resumeNavigate(null);
        }
    }

    private boolean isResumeCruise() {
        //        巡航当前状态是否可以执行该动作
        boolean isCruise = false;
        switch (cruiseState.getState()) {
            case STATE_INIT:
                isCruise = false;
                break;
            case STATE_ING:
                isCruise = false;
                break;
            case STATE_PAUSE:
                isCruise = true;
                break;
            case STATE_ENDING:
                isCruise = false;
                break;
        }
//        导航当前状态是否可以执行当前动作
        boolean isNav = false;
        switch (navState.getState()) {
            case STATE_INIT:
                isNav = true;
                break;
            case STATE_ING:
                isNav = false;
                break;
            case STATE_PAUSE:
                isNav = true;
                break;
            case STATE_ENDING:
                isNav = true;
                break;
        }
//        回家当前状态是否可以执行当前动作
        boolean isHome = false;
        switch (goHomeState.getState()) {
            case STATE_INIT:
                isHome = true;
                break;
            case STATE_ING:
                isHome = false;
                break;
            case STATE_PAUSE:
                isHome = true;
                break;
            case STATE_ENDING:
                isHome = true;
                break;
        }
        if (isCruise && isNav && isHome) {
            return true;
        }
        return false;
    }

    @Override
    public boolean resumeNavigate(ActionCallBack callBack) {
        return actionEngine.resumeNavigate(callBack);
    }



    @Override
    public boolean armAction(int armID, int actionID, ActionCallBack callBack) {
        boolean flag = isExecuteAction();
        if (flag) {
            return actionEngine.armAction(armID, actionID, callBack);
        }
        return false;
    }

    private boolean isExecuteAction() {
        if (cruiseState.getState() == STATE_ING
                && navState.getState() == STATE_ING
                && goHomeState.getState() == STATE_ING) {
            Log.d(TAG, "isExecuteAction: 当前机器人在运动中，不可以执行动作指令");
            return false;
        }
        return true;
    }

    @Override
    public boolean multiAction(String actionID, ActionCallBack callBack) {
        boolean flag = isExecuteAction();
        if (flag) {
            return actionEngine.multiAction(actionID, callBack);
        }
        return false;
    }

    @Override
    public boolean fingerAction(int handSide, int[] handActionId, ActionCallBack callBack) {
        boolean flag = isExecuteAction();
        if (flag) {
            return actionEngine.fingerAction(handSide, handActionId, callBack);
        }
        return false;
    }

    @Override
    public boolean lightAction(int id, String colors, ActionCallBack callBack) {
        boolean flag = isExecuteAction();
        if (flag) {
            return actionEngine.lightAction(id, colors, callBack);
        }
        return false;
    }

    @Override
    public boolean moveToCharge(ActionCallBack callBack) {
        boolean flag = isExecuteAction();
        if (flag) {
            return actionEngine.moveToCharge(callBack);
        }
        return false;
    }

    @Override
    public boolean stopCharge(ActionCallBack callBack) {
        return false;
    }

    @Override
    public boolean hugAction(ActionCallBack callBack) {
        boolean flag = isExecuteAction();
        if (flag) {
            return actionEngine.hugAction(callBack);
        }
        return false;
    }

    @Override
    public boolean handShakeAction(ActionCallBack callBack) {
        boolean flag = isExecuteAction();
        if (flag) {
            return actionEngine.handShakeAction(callBack);
        }
        return false;
    }

    @Override
    public boolean saluteAction(ActionCallBack callBack) {
        boolean flag = isExecuteAction();
        if (flag) {
            return actionEngine.saluteAction(callBack);
        }
        return false;
    }

    @Override
    public boolean guideAction(ActionCallBack callBack) {
        boolean flag = isExecuteAction();
        if (flag) {
            return actionEngine.guideAction(callBack);
        }
        return false;
    }
}
