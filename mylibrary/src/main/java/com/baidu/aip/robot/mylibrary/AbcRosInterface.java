package com.baidu.aip.robot.mylibrary;

/**
 * Created by sunyajie on 2019/2/1.
 */

import java.util.HashMap;
import java.util.List;

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
    public static final String ACTION_TYPE_RAISE_HAND = "raise_hand"; // 本体集成动作集，举手动作
    public static final String ACTION_TYPE_TWIST_HEAD = "twist_head"; // 本体集成动作集，点头动作
    public static final String ACTION_TURN_HEAD_LEFT = "turn_head_left"; // 本体集成动作集，向左看动作
    public static final String ACTION_TURN_HEAD_RIGHT = "turn_head_right"; // 本体集成动作集，向右看动作
    public static final String ACTION_WAVE = "wave"; // 本体集成动作集，打招呼动作
    public static final String ACTION_DO_AN_ACTION = "do_an_action"; // 本体集成动作集，动一下动作

    /**
     * @param mapName
     * @return
     */
    public boolean loadMap(String mapName);
    /**
     * @return 返回当前已经加载的地图名字
     */
    public String getMapName();

    /**
     * @return 返回机器人地图名字列表
     */
    public List<String> getMapList();

    /**
     * @return 返回机器人地图的导航点列表
     */
    public List<Location> getMapPointList();
    /**
     * @return 返回当前点的位置
     */
    public Location getCurrentPosition();

    /**
     * 开启人脸识别
     * @param actionCallBack
     */
    public void startFaceTrace(ActionCallBack actionCallBack);

    /**
     * 关闭人脸追随,复位成功之后返回当前点的位置
     * @return 是否成功
     */
    public boolean stopFaceTrace();

    /**
     * 头部展示表情
     * @param expressionId 表情ID
     * @param callBack 执行回调
     */
    public void showHeadExpression(String expressionId, ActionCallBack callBack);
    /**
     * 开启触摸监听
     * @param callback
     */
    public void listenTouchEvent(TouchCallback callback);

    public interface TouchCallback{
        /**
         * 左耳
         */
        public static final int TOUCH_TYPE_LEFT_EAR = 1;
        /**
         * 右耳
         */
        public static final int TOUCH_TYPE_RIGHT_EAR = 2;
        /**
         * 头顶
         */
        public static final int TOUCH_TYPE_TOP_HEAD = 3;
        /**
         * 下巴
         */
        public static final int TOUCH_TYPE_JAW = 4;
        /**
         * 腹部
         */
        public static final int TOUCH_TYPE_ABDOMEN  = 5;
        /**
         * 屁股
         */
        public static final int TOUCH_TYPE_ASS  = 6;

        /**
         * 触摸成功并返回触摸时间类型
         * @param type TOUCH_TYPE
         * @param message 备注
         */
        void onSuccessTouch(int type, String message);

        /**
         * 失败
         * @param reason 失败信息
         */
        void onTouchFail(String reason);
    }
    /**
     * 机器人位置重定位,地图加载之后进行的定位操作。
     */
    public void relocation(Location location, RelocationCallback callback);
    /**
     * 注册监听ROS层电量，异常等相关信息
     * @param rosListener 监听器，ros根据监听器内action mFilterAction执行,回调当传入的值为mull时，代表注销检测
     */

    public void registerRosListener(RosListener rosListener);

    /**
     * 注册监听障碍物检测，当传入的值为mull时，代表注销检测
     * @param objectDetectListener
     */

    public void registerObjectDetectListener(ObjectDetectCallback objectDetectListener);
    /**
     * 获取ROS层当前电量剩余比例
     * @return 剩余电量和满格电量比例
     */

    public float getBatteryLevel();
    /**
     * 获取ROS层电池容量
     * @return 满格电量数
     */

    public float getBatteryScale();
    /**
     * 机器人移动制定距离
     * @param distance 移动距离,正值为向前走，负值为向后走。
     * @param si 移动距离对应单位,对应障碍物检测中的距离单位。
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */

    public boolean startMove(float distance, String si, ActionCallBack callBack);
    /**
     * 发送身体转动请求给ROS
     * @param angle 转动角度（顺时针正方向/逆时针负方向），当angle的数值大于360，也要继续旋转，代表转的圈数。
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */

    public boolean startBodyTurn(float angle, ActionCallBack callBack);
    /**
     * 发送头部转动请求给ROS
     * @param direction 水平转动为：0/垂直转动为：1
     * @param angle 转动角度（顺时针正方向- 右/逆时针负方向 - 左；向上为正/向下为负）
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */

    public boolean startHeadTurn(int direction, float angle, ActionCallBack callBack);
    /**
     * 发送停止当前动作指令给ROS
     * @param actionType 指令类型  见本接口提供的action类型值 actionType =  ACTION_TYPE_STOP_ACTION时停止动作。
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
    public boolean navigateToLocation(Location location, ActionCallBack callBack);

    /**
     * 导巡航
     * @param location 按照location数组顺序巡航
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */
    public boolean cruiseToLocations(Location[] location, ActionCallBack callBack);

    /**
     * 返回初始点，某些厂商的初始化点就是开始扫描地图的点。（康力优蓝存在要到初始点加载地图和复位的操作）
     * @param callBack
     * @return
     */
    public boolean goToOrigin(ActionCallBack callBack);
    /**
     * 暂停导航/巡航 需要支持恢复
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */
    public boolean pauseNavigate(ActionCallBack callBack);

    /**
     * 取消 导航/巡航 任务
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */
    public boolean cancelNavigate(ActionCallBack callBack);
    /**
     * 恢复处于暂停中的导航/巡航
     * @param callBack 继续导航/巡航的执行结果回调
     * @return 指令发送是否正常
     */
    public boolean resumeNavigate(ActionCallBack callBack);
    /**
     * 设置手臂动作
     * @param armID 机器人手臂id  左-0；右-1；两只手-2；
     * @param actionID 手臂动作id 厂商枚举。
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */
    public boolean armAction(int armID, int actionID, ActionCallBack callBack);
    /**
     * @param actionID 机器人复合动作指令标识 actionID=ACTION_TYPE_RAISE_HAND(举手，
     *                 actionID = ACTION_TYPE_TWIST_HEAD 点头
     *                 actionID = ACTION_TURN_HEAD_LEFT 向左看动作
     *                 actionID = ACTION_TURN_HEAD_RIGHT 向右看
     *                 actionID = ACTION_WAVE 打招呼动作)
     *                 actionID = ACTION_DO_AN_ACTION 动一下动作)
     * @param callBack  执行结果回调
     * @return  指令发送是否正常
     */
    public boolean multiAction(String actionID, ActionCallBack callBack);
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
     * @param id light id不同代表不同的灯，
     * @param colors 亮灯颜色十六进制序列，该灯序列亮的模式
     * @param callBack 执行结果回调
     * @return 指令发送是否正常
     */
    public boolean lightAction(int id, String colors, ActionCallBack callBack);
    /**
     * 返回充电位置并自动充电
     *
     * @return 指令发送是否正常
     */
    public boolean moveToCharge(ActionCallBack callBack);

    /**
     * 使正在充电的设备取消充电
     *
     * @return 指令发送是否正常
     */
    public boolean stopCharge(ActionCallBack callBack);
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



    public interface ObjectDetectCallback {
        /*
        * 障碍物检测的距离单位
        * */
        public static final String DIMEN_KILOMETER = "dimen_kilometer";
        public static final String DIMEN_METER = "dimen_meter";
        public static final String DIMEN_DECIMETRE = "dimen_decimetre";
        public static final String DIMEN_CENTIMETER = "dimen_centimeter";

        /**
         * 添加障碍物检测关心的等级
         * @param levelName  名称：不同等级名称不同，名称相同则关心的取值范围被覆盖
         * @param lowLevels  关注电量的下限
         * @param highLevel  关注电量的上限
         * @param dimen     距离单位
         */
        void addDetectlevel(String levelName, float lowLevels, float highLevel, String dimen);

        /**
         * 障碍物距离监控回调
         * @param levelName 监控的等级
         * @param level     属于该等级的具体距离数值
         * @param dimenUnit 距离单位
         */
        void onDetectLevelReached(String levelName, float level, String dimenUnit);

        /**
         * 障碍物检测发生异常时回调
         * @param exceptionMsg  异常的具体信息
         */
        void onDetectException(String exceptionMsg);

        /**
         * @return 我们关注的障碍物范围的一个集合如： 1~2（lowLevelRange） 2~3(highLevelRange)
         * key:String, value:float[] ({1,2}) 其中默认单位是米（传入的观察的单位）
         */
        HashMap getDetectLevel();

    }

    /**
     * 重定位回调
     */
    public interface RelocationCallback{

        /**
         * 重定位成功
         * @param location 返回重定位之后当前的位置
         */
        void onRelocationSuccess(Location location);

        /**
         * 重定位失败
         * @param reason 返回失败的原因
         */
        void onRelocationFail(String reason);
    }

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
    public interface RosListener {
        public static final String ACTION_CHARGE_STATUS = "charge_status";
        public static final String ACTION_POWER_LEVELS = "power_levels";
        public static final String ACTION_POWER_TEMP = "power_temp";
        public static final String ACTION_ROS_EXCEPTION = "ros_exception";
        public static final String ACTION_ROS_LOCATION_LOSS = "location_loss";
        /**
         * ROS启动完成
         */
        public abstract void onRosReady();
        /**
         * 电量达到指定等级时回调
         * @param action     监控等级
         * @param level 当前电量百分比 ，同一个值（在非充电状态或者充电状态）只回调一次
         * @param isCharging  是否正在充电，true是正在充电，false不是在充电状态
         */
        public abstract void onPowerLevelReached(String action, float level, boolean isCharging);
        /**
         * 电池温度报警回调
         * @param temp 温度，当电池温度达到报警温度时回调
         */
        public abstract void onPowerTemp(float temp);
        /**
         * 机器人错误信息都是通过这个回调。比如：左臂故障。串口不通；回调的字符串的信息，写成中文。
         * @param exceptionMsg 异常信息
         */
        public abstract void onRosException(String exceptionMsg);
        /**
         * 增加监听电池相关回调事件
         *
         * @param action 监听事件，参见ACTION_XXXX
         */
        void addAction(String action);

        /**
         * 增加电量监控等级
         * @param action 监听电量等级
         * @param levels 回调电量等级，比如{10%, 20%}
         */
        public void addPowerLevel(String action, float[] levels);

        /**
         * @return  我们关注的障碍物范围的一个集合如： 10%~20%（lowLevelRange） 80%~90%(highLevelRange)  key:String, value:float[] ({0.8,0.9})
         */
        HashMap getDetectLevel();
    }
    /**
     * 动作执行指令回调
     */
    public interface ActionCallBack {

        // onActionProgress()函数actionType回调值：表示有突发障碍物出现，影响机器人原本规划好的路线。此时progress值为-1；
        public static final int ACTION_CLOSE_OBSTACLES = 100;

        // onActionProgress()函数actionType回调值：表示动作完成度。此时progress值为完成度的具体值；
        public static final int ACTION_PERCENT = 200;
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

        /**
         * @param cruisePoint  到达导航点回调
         */
        public void onReachedCruisePoint(String cruisePoint);
    }
    // 地图中位置信息，可以是点的名称或者是坐标（x,y）
    public class Location {
        public String mLocationName;
        public int mX;
        public int mY;
        public Location(String mLocationName, int mX, int mY) {
            this.mLocationName = mLocationName;
            this.mX = mX;
            this.mY = mY;
        }
    }
}
