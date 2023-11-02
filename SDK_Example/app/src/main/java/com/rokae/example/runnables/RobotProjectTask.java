package com.rokae.example.runnables;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rokae.sdk.engine.RobotResult;
import com.rokae.sdk.engine.base.BaseRobot;
import com.rokae.sdk.enums.RobotT;
import com.rokae.sdk.enums.RobotType;
import com.rokae.sdk.model.robot.PCB3Robot;
import com.rokae.sdk.model.robot.PCB4Robot;
import com.rokae.sdk.model.robot.StandardRobot;
import com.rokae.sdk.model.robot.XMateProRobot;
import com.rokae.sdk.model.robot.XMateRobot;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dingchao
 */

public class RobotProjectTask extends BaseRobotTask {
    private static final String TAG = BaseRobot.TAG;
    private String ip;
    private RobotType type;

    public RobotProjectTask(String ip, RobotType type) {
        this.ip = ip;
        this.type = type;
    }

    @Override
    void runTask() throws InterruptedException {
        Log.e(BaseRobot.TAG, "------------------------------------------------");
        BaseRobot robot = newRobot();
        robot.setLog(true);
        RobotResult result = robot.connect();
        if (result.getCode() != 0) return;

        robot.getRobotInfo();
        robot.setPowerState(true);
        robot.setMotionControlMode(RobotT.MotionControlMode.NrtRLTask);
        robot.setOperateMode(RobotT.RobotMode.Operate_automatic);

        result = robot.projectsInfo();
        String data = result.getData();
        JSONArray objects = JSON.parseArray(data);
        if (objects != null && objects.size() > 0) {
            JSONObject o = objects.getJSONObject(0);
            String name = o.getString("name");
            JSONArray taskList = o.getJSONArray("taskList");
            if (!TextUtils.isEmpty(name) && taskList != null && taskList.size() > 0) {
                List<String> arrayList = new ArrayList<>();
                for (int i = 0; i < taskList.size(); i++) {
                    String taskName = (String) taskList.get(i);
                    arrayList.add(taskName);
                }
                robot.loadProject(name, arrayList);
                robot.ppToMain();
                robot.runProject();

                Thread.sleep(5000); // 运动到一定时间暂定5秒
                robot.pauseProject();
                robot.runProject();
                reportState(robot);
            }
        }

        Thread.sleep(3*60000); // 运动3分钟后停止工程
        robot.moveStop();
        robot.disconnect();
    }

    private BaseRobot newRobot() {
        if (type == RobotType.ROBOT_XMATE) {
            Log.d(TAG, "开始测试机型:XMateRobot");
            return new XMateRobot(ip);
        } else if (type == RobotType.ROBOT_XMATE_ER_PRO) {
            Log.d(TAG, "开始测试机型:XMateProRobot");
            return new XMateProRobot(ip);
        } else if (type == RobotType.ROBOT_PCB4) {
            Log.d(TAG, "开始测试机型:PCB4Robot");
            return new PCB4Robot(ip);
        } else if (type == RobotType.ROBOT_PCB3) {
            Log.d(TAG, "开始测试机型:PCB3Robot");
            return new PCB3Robot(ip);
        } else {
            Log.d(TAG, "开始测试机型:StandardRobot");
            return new StandardRobot(ip);
        }
    }
}
