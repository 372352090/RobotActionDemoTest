package com.baidu.aip.robot.mylibrary;

/**
 * Created by sunyajie on 2019/2/1.
 */

import java.util.HashMap;

public interface AbcRosInterface {
    public static final int ACTION_TYPE_MOVE = 1;
    public static final int ACTION_TYPE_BODY_TURN = 2;
    public static final int ACTION_TYPE_HEAD_TURN = 3;
    public static final int ACTION_TYPE_STOP_ACTION = 4;
    public static final int ACTION_TYPE_NAVIGATE_START = 5;
    public static final int ACTION_TYPE_NAVIGATE_PAUSE = 6;
    public static final int ACTION_TYPE_NAVIGATE_RESUME = 7;
    public static final int ACTION_TYPE_LIGHT = 8;
    public static final int ACTION_TYPE_MOVE_CHARGE = 9;
    public static final int ACTION_TYPE_FINGER_CLENCH = 10; // 手指完全收缩
    public static final int ACTION_TYPE_FINGER_HALF_CLENCH = 11; // 手指半收缩
    public static final int ACTION_TYPE_FINGER_NO_CLENCH = 12; // 手指伸开
    public static final int ACTION_TYPE_FINGER_LEFT = 13; // 左手手指
    public static final int ACTION_TYPE_FINGER_RIGHT = 14; // 右手手指
    public static final int ACTION_TYPE_HANDSHAKE = 15; // 本体集成动作集，握手动作
    public static final int ACTION_TYPE_HUG = 16; // 本体集成动作集，拥抱动作
    public static final int ACTION_TYPE_GUID = 17; // 本体集成动作集，引导动作，这边请
    public static final int ACTION_TYPE_SALUTE = 18; // 本体集成动作集，敬礼动作
    /**
     * 注册监听ROS层电量，异常等相关信息
     * @param rosListener 监听器，ros根据监听器内action mFilterAction执行回调
     */
    public void registerRosListener(RosListener rosListener);
    /**
     * 获取ROS层当前电量剩余比例
     * @return 剩余电量和满格电量比例
     */
    public float getBatteryLevel();
    /**
     * 获取ROS层电池容量
     * @return 满格电量数
     */
    public void getBatteryScale();
    /**
     * 机器人移动制定距离
     * @param distance 移动距离
     * @param si 移动距离对应单位
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */
    public boolean startMove(float distance, String si, ActionCallBack callBack);
    /**
     * 发送身体转动请求给ROS
     * @param angle 转动角度（顺时针正方向/逆时针负方向）
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */
    public boolean startBodyTurn(float angle, ActionCallBack callBack);
    /**
     * 发送头部转动请求给ROS
     * @param direction 水平转动/垂直转动
     * @param angle 转动角度（顺时针正方向/逆时针负方向；向上为正/向下为负）
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */
    public boolean startHeadTurn(int direction, float angle, ActionCallBack callBack);
    /**
     * 发送停止当前动作指令给ROS
     * @param actionType 指令类型
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */
    public boolean stopAction(int actionType, ActionCallBack callBack);
    /**
     * 导航到固定点,或者巡航
     * @param location 数组长度为1则为导航，否则按照location数组顺序巡航
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */
    public boolean navigateToLocation(Location[] location, ActionCallBack callBack);
    /**
     * 暂停导航/巡航 需要支持恢复
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */
    public boolean pauseNavigate(ActionCallBack callBack);
    /**
     * 恢复处于暂停中的导航/巡航
     * @param callBack 继续导航/巡航的执行结果回调
     * @return 指令发送是否正常
     */
    public boolean resumeNavigate(ActionCallBack callBack);
    /**
     * 设置手臂动作
     * @param armID 机器人手臂id
     * @param actionID 手臂动作id
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */
    public boolean armAction(int armID, int actionID, ActionCallBack callBack);
    /**
     * 设置手指动作
     * @param handActionId 手指动作指令集
     * @param handSide 左手右手id
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */
    public boolean fingerAction(int handSide, int[] handActionId, ActionCallBack callBack);
    /**
     * 手臂动作
     * @param id light id
     * @param colors 亮灯颜色十六进制序列
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */
    public boolean lightAction(int id, int[] colors, ActionCallBack callBack);
    /**
     * 返回充电位置并自动充电
     *
     * @return 指令发送是否正常
     */
    public boolean moveToCharge(ActionCallBack callBack);
    /**
     * 集成化动作：拥抱动作
     *
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */
    public boolean hugAction(ActionCallBack callBack);
    /**
     * 集成化动作：握手动作
     *
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */
    public boolean handShakeAction(ActionCallBack callBack);
    /**
     * 集成化动作：敬礼动作
     *
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */
    public boolean saluteAction(ActionCallBack callBack);
    /**
     * 集成化动作：引导动作
     *
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */
    public boolean guideAction(ActionCallBack callBack);
    /**
     * ROS相关交互监听器
     * 通过addAction/addPowerLevel来增加监听事件
     * ROS层需要事件对相应事件的回调
     * 事件包括
     * 1.ACTION_CHARGE_STATUS 充电状态
     * 2.ACTION_POWER_LEVELS  剩余电量回调
     * 3.ACTION_POWER_TEMP    电池温度警告回调
     * 4.ACTION_ROS_EXCEPTION ROS异常信息回调
     */
    public abstract class RosListener {
        public static final String ACTION_CHARGE_STATUS = "charge_status";
        public static final String ACTION_POWER_LEVELS = "power_levels";
        public static final String ACTION_POWER_TEMP = "power_temp";
        public static final String ACTION_ROS_EXCEPTION = "ros_exception";
        public static final int CHARGE_STATUS_IDLE = 1;
        public static final int CHARGE_STATUS_CHARGINE = 2;
        private HashMap mFilterAction = new HashMap();
        /**
         * ROS启动完成
         */
        public abstract void onRosReady();
        /**
         * 电量达到指定等级时回调
         * @param level 当前电量百分比
         */
        public abstract void onPowerLevelReached(String action,float level);
        /**
         * 电池温度报警回调
         * @param temp 温度，当电池温度达到报警温度时回调
         */
        public abstract void onPowerTemp(float temp);
        /**
         * 充电状态变化回调
         * @param exceptionMsg 异常信息
         */
        public abstract void onRosException(String exceptionMsg);
        /**
         * 充电状态变化回调
         * @param status 充电中/IDLE参见CHARGE_STATUS_IDLE/HARGE_STATUS_CHARGINE
         */
        public abstract void onChargeStatue(int status);
        /**
         * 增加监听电池相关回调事件
         * @param action 监听事件，参见ACTION_XXXX
         */
        public void addAction(String action) {
            mFilterAction.put(action, null);
        }
        /**
         * 增加电量监控等级
         * @param action 监听电量等级
         * @param levels 回调电量等级，比如{10%, 20%}
         */
        public void addPowerLevel(String action, float[] levels) {
            mFilterAction.put(action, levels);
        }
    }
    /**
     * 动作执行指令回调
     */
    public interface ActionCallBack {
        /**
         * 动作指令执行进度回调
         * @param actionType 动作类型
         * @param progress 执行进度，0开始，100结束
         */
        public void onActionProgress(int actionType, float progress);
        /**
         * 动作指令失败回调
         * @param actionType 动作类型
         * @param message 失败信息
         */
        public void onActionFail(int actionType, String message);
        /**
         * 动作指令开始执行回调
         * @param actionType 动作类型
         */
        public void onActionStart(int actionType);
        /**
         * 动作指令执行完毕回调
         * @param actionType 动作类型
         */
        public void onActionComplete(int actionType);
    }
    // 地图中位置信息，可以是点的名称或者是坐标（x,y）
    public class Location {
        public String mLocationName;
        public int mX;
        public int mY;
    }
}
