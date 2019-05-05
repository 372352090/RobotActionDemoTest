package com.baidu.aip.robot.robotactiondemotest;

import android.content.Context;

import com.baidu.aip.robot.mylibrary.AbcRosInterface;

/**
 * Created by sunyajie on 2019/3/22.
 */

public class ActionConfig {

    private static ActionConfig mConfig;
    private Context mContext;
    private String mapName = "robotABC";
    private String origin = "origin";
    private int interceptNum = 5;
    private boolean isNavEndGoHome = true;
    private boolean isCruiseEndGoHome = true;

    private boolean isNavInterceptedGoHome = true;

    private AbcRosInterface.Location[] locations;

    private static ActionConfig getInstance() {
        if (mConfig == null) {
            synchronized (ActionConfig.class) {
                if (mConfig == null) {
                    mConfig = new ActionConfig();
                }
            }
        }
        return mConfig;
    }

    public static class Builder {
        public Builder() {
            mConfig = getInstance();
        }

        public Builder context(Context context) {
            mConfig.mContext = context;
            return this;
        }

        public Builder mapName(String name) {
            mConfig.mapName = name;
            return this;
        }

        public Builder originLocation(String name) {
            mConfig.origin = name;
            return this;
        }

        public Builder interceptNum(int num) {
            mConfig.interceptNum = num;
            return this;
        }

        public Builder isNavEndGoHome(boolean isNavEndGoHome) {
            mConfig.isNavEndGoHome = isNavEndGoHome;
            return this;
        }

        public Builder isCruiseEndGoHome(boolean isCruiseEndGoHome) {
            mConfig.isCruiseEndGoHome = isCruiseEndGoHome;
            return this;
        }

        public Builder isNavInterceptedGoHome(boolean isNavInterceptedGoHome) {
            mConfig.isNavInterceptedGoHome = isNavInterceptedGoHome;
            return this;
        }

        public Builder cruiseLocations(AbcRosInterface.Location[] locations) {
            mConfig.locations = locations;
            return this;
        }


        public ActionConfig build() {
            return mConfig;
        }
    }


    public static ActionConfig getmConfig() {
        return mConfig;
    }

    public String getMapName() {
        return mapName;
    }

    public String getOrigin() {
        return origin;
    }

    public int getInterceptNum() {
        return interceptNum;
    }

    public boolean isNavEndGoHome() {
        return isNavEndGoHome;
    }

    public boolean isCruiseEndGoHome() {
        return isCruiseEndGoHome;
    }

    public boolean isNavInterceptedGoHome() {
        return isNavInterceptedGoHome;
    }

    public AbcRosInterface.Location[] getLocations() {
        return locations;
    }

    public Context getContext() {
        return mContext;
    }
}
