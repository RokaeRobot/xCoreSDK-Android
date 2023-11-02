package com.rokae.example.runnables;

import static com.rokae.sdk.enums.Ag.RobotStateField.jointAcc_c;

import android.util.Log;

import com.rokae.sdk.engine.RobotResult;
import com.rokae.sdk.engine.base.BaseRobot;
import com.rokae.sdk.enums.Ag;
import com.rokae.sdk.enums.RobotT;
import com.rokae.sdk.enums.RobotType;
import com.rokae.sdk.model.robot.PCB3Robot;
import com.rokae.sdk.model.robot.PCB4Robot;
import com.rokae.sdk.model.robot.StandardRobot;
import com.rokae.sdk.model.robot.XMateProRobot;
import com.rokae.sdk.model.robot.XMateRobot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author dingchao
 */

public class RobotInfoConfigTask extends BaseRobotTask {
    private static final String TAG = BaseRobot.TAG;
    private String ip;
    private RobotType type;

    public RobotInfoConfigTask(String ip, RobotType type) {
        this.ip = ip;
        this.type = type;
    }

    @Override
    void runTask() throws Exception {
        Log.e(BaseRobot.TAG, "------------------------------------------------");
        BaseRobot robot = newRobot();
        robot.setLog(true);
        RobotResult connect = robot.connect();
        if (connect.getCode() != 0) return;
        robot.setPowerState(false);
        robot.setPowerState(true);
        robot.getPowerState();
        robot.getRobotInfo();
        robot.getOperateState();
        robot.setOperateMode(RobotT.RobotMode.Operate_automatic);
        robot.getFlangePos();
        robot.posture(RobotT.CoordinateType.endInRef);
        robot.carPosture(RobotT.CoordinateType.flangeInBase);
        robot.setDI(1, 1, true);
        robot.getDI(1, 1);
        robot.setDO(1, 1, 1);
        robot.getDO(1, 1);
        robot.getAI(1, 1);
        robot.setAO(1, 1, 1.0);
        robot.setSimulationMode(true);

        robot.clearServoAlarm();
        robot.queryControllerLog(10, Arrays.asList(1, 2));
        robot.setMotionControlMode(RobotT.MotionControlMode.NrtRLTask);
        robot.moveReset();
        robot.setDefaultSpeed(100);
        robot.setDefaultZone(50);
        robot.setMaxCacheSize(10);
        robot.adjustSpeedOnline(10);

        List<String> fields = new ArrayList<>();
        fields.add(Ag.RobotStateField.jointVel_m);
        fields.add(Ag.RobotStateField.jointPos_c);
        fields.add(Ag.RobotStateField.jointVel_c);
        fields.add(jointAcc_c);
        robot.startReceiveRobotState(100, fields);

        robot.setMotionControlMode(RobotT.MotionControlMode.NrtCommand);
        robot.setToolSet("tool0", "wobj0");

        robot.getJointPos();
        robot.getJointVel();
        robot.getJointTorque();
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
