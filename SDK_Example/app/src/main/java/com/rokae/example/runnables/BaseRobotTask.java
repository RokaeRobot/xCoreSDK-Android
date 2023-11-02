package com.rokae.example.runnables;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rokae.sdk.engine.RobotResult;
import com.rokae.sdk.engine.base.BaseRobot;

/**
 * @author dingchao
 */

public abstract class BaseRobotTask implements Runnable{

    abstract void runTask() throws Exception;

    public void reportState(BaseRobot robot) {
        int flag = -1;
        RobotResult result;
        while (flag != 0) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            result = robot.getOperateState();
            JSONObject dt = JSON.parseObject(result.getData());
            if (dt.containsKey("operate")) {
                flag = dt.getIntValue("operate");
            } else {
                flag = 0;
            }
        }
    }

    @Override
    public void run() {
        try {
            runTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
