package com.rokae.example.runnables;

import android.util.Log;

import com.rokae.sdk.engine.RobotResult;
import com.rokae.sdk.engine.base.BaseRobot;
import com.rokae.sdk.enums.CommandType;
import com.rokae.sdk.enums.RobotT;
import com.rokae.sdk.enums.RobotType;
import com.rokae.sdk.model.commands.Command;
import com.rokae.sdk.model.commands.MoveAbsJCommand;
import com.rokae.sdk.model.commands.MoveCCommand;
import com.rokae.sdk.model.commands.MoveJCommand;
import com.rokae.sdk.model.commands.MoveLCommand;
import com.rokae.sdk.model.commands.MoveSPCommand;
import com.rokae.sdk.model.position.CartesianPosition;
import com.rokae.sdk.model.robot.PCB3Robot;
import com.rokae.sdk.model.robot.PCB4Robot;
import com.rokae.sdk.model.robot.StandardRobot;
import com.rokae.sdk.model.robot.XMateProRobot;
import com.rokae.sdk.model.robot.XMateRobot;

/**
 * @author dingchao
 */

public class RobotCommandTask extends BaseRobotTask {
    private static final double[][] point_XM7 = new double[][]{
            new double[]{0.63125000000000009, 0.0, 0.50738611415569213, 3.1415926535897931, 2.8327694488239898e-16, 3.1415926535897931},
            new double[]{0.86137738628531924, 0.051027211541989109, 0.89438529789696886, -1.7658729399047501, 0.95852388727136184, -1.8852523154253691},
            new double[]{0.74439614501280904, 0.20511006677661325, 0.51501727073815784, -3.0242307950482075, 0.78792749818891739, -2.6457623322483803},
            new double[]{0.61791934965758877, 0.39433688316773879, 0.57947830016352664, -2.0323048930769838, -0.28059607522718144, 0.76900180009610364}
    };

    private static final String TAG = BaseRobot.TAG;
    private String ip;
    private RobotType type;
    private CommandType commandType = CommandType.MoveAbsJCommand;

    public RobotCommandTask(String ip, RobotType type) {
        this.ip = ip;
        this.type = type;
    }

    public RobotCommandTask(String ip, RobotType type, CommandType commandType) {
        this.ip = ip;
        this.type = type;
        this.commandType = commandType;
    }

    @Override
    void runTask() throws Exception {
        Log.e(BaseRobot.TAG, "------------------------------------------------");
        BaseRobot robot = newRobot();
        robot.setLog(true);
        RobotResult result = robot.connect();
        if (result.getCode() != 0) return;

        robot.getRobotInfo();
        robot.setPowerState(true);
        robot.setMotionControlMode(RobotT.MotionControlMode.NrtCommand);
        robot.setOperateMode(RobotT.RobotMode.Operate_automatic);

        Command[] commands = new Command[]{};
        if (commandType == CommandType.MoveAbsJCommand) {
            commands = new MoveAbsJCommand[]{
                    new MoveAbsJCommand(new double[]{-0.15090446119138207, 0.5240599685200733, 0.25623639049291563, 1.9569014482661953, -0.69615715327071437, 0.98947657057277294, 0.36596682994033763}, 500, 0),
                    new MoveAbsJCommand(new double[]{-0.45938590039582605, 0.18389358689116839, 0.59929273799718497, 1.6274975297741081, -0.53039303217130895, 1.5748903777070646, -0.0039430496783604187}, 500, 10),
                    new MoveAbsJCommand(new double[]{0.46586831744994478, 0.26823886138069059, 0.41533344759297303, 1.5388053971480398, -0.62936355512970577, 1.5470057228817788, 0.85974158354433339}, 500, 80),
                    new MoveAbsJCommand(new double[]{0.57940326893582128, 0.27948770428529224, 0.32331353539518498, 1.0038228862069751, -0.66757741340091814, 1.973807354655684, 0.56572923835824718}, 4000, 0)
            };
        } else if (commandType == CommandType.MoveJCommand) {
            commands = new MoveJCommand[]{
                    new MoveJCommand(point_XM7[0], 500, 0),
                    new MoveJCommand(point_XM7[1], 600, 60),
                    new MoveJCommand(point_XM7[2], 400, 80),
                    new MoveJCommand(point_XM7[3], 1000, 10),
            };
        } else if (commandType == CommandType.MoveLCommand) {
            commands = new MoveLCommand[]{
                    new MoveLCommand(new double[]{0.63125, 0, 0.507386, -Math.PI, 0.0, -Math.PI}, 500, 0),
                    new MoveLCommand(new double[]{0.473249996, 0.27500000370370364, 0.27738611045198847, -Math.PI, 0, -Math.PI}, 500, 0),
                    new MoveLCommand(new double[]{0.30429955938935049, 0.68902795589181065, 0.48138611415569188, -Math.PI, 0, -Math.PI}, 500, 0),
                    new MoveLCommand(new double[]{0.63125000000000009,-5.5511151231257827E-17,0.5073861141556919,2.5705836190982767,0.69414064433921219,-3.0419071779959808}, 500, 0),
            };
        } else if (commandType == CommandType.MoveCCommand) {
            CartesianPosition target = new CartesianPosition(new double[]{0.63125, 0, 0.507386, -Math.PI, 0.0, -Math.PI});
            CartesianPosition aux = new CartesianPosition(new double[]{0.473249996, 0.27500000370370364, 0.27738611045198847, -Math.PI, 0, -Math.PI});
            target.setConfData(new int[]{-1,0,-1,1,0,0,-1,0});
            aux.setConfData(new int[]{-1,0,-1,0,-1,1,0,0});
            commands = new MoveCCommand[]{
                    new MoveCCommand(target,aux , 500, 20),
                    new MoveCCommand(target,aux , 800, 100),
                    new MoveCCommand(target,aux , 600, 30),
            };
        }
        else if (commandType == CommandType.MoveSPCommand) {
            commands = new MoveAbsJCommand[]{
                    new MoveAbsJCommand(new double[]{0, Math.PI / 6, -Math.PI / 2, 0, -Math.PI / 3, 0}, 500, 0),
            };
            robot.moveAppend(commands);

            commands = new MoveLCommand[]{
                    new MoveLCommand(new double[]{0.464449, 0.136000, 0.484029, Math.PI, 0.0, Math.PI}, 500, 50),
            };
            robot.moveAppend(commands);

            CartesianPosition position = new CartesianPosition(new double[]{0.464449, -0.136000, 0.364949, -Math.PI, Math.PI/6, -Math.PI});
            commands = new MoveSPCommand[]{
                    new MoveSPCommand(position, 0.03, 0.001, Math.PI * 6, true, 500)
            };
            robot.moveAppend(commands);

            commands = new MoveLCommand[]{
                    new MoveLCommand(new double[]{0.350727, 0.136000, 0.322142, -Math.PI, 0.0, -Math.PI}, 500, 10),
            };
            robot.moveAppend(commands);

            robot.moveStart();
        }
        robot.moveAppend(commands);
        robot.moveStart();
        reportState(robot);
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
