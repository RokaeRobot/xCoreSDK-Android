package com.rokae.example;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.rokae.example.databinding.ActivityMainBinding;
import com.rokae.example.runnables.RobotCommandTask;
import com.rokae.example.runnables.RobotInfoConfigTask;
import com.rokae.example.runnables.RobotProjectTask;
import com.rokae.sdk.enums.CommandType;
import com.rokae.sdk.enums.RobotType;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ActivityMainBinding binding;
    private int position;
    private final RobotType[] robotTypes = new RobotType[]{
            RobotType.ROBOT_XMATE,
            RobotType.ROBOT_XMATE_ER_PRO,
            RobotType.ROBOT_STANDARD,
            RobotType.ROBOT_PCB3,
            RobotType.ROBOT_PCB4,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.spRobotType.setOnItemSelectedListener(this);
        binding.btnRobotInfo.setOnClickListener(v -> onClick(1));
        binding.btnRobotProject.setOnClickListener(v -> onClick(2));
        binding.btnRobotMoveAbsJ.setOnClickListener(v -> onClick(3));
        binding.btnRobotMoveJ.setOnClickListener(v -> onClick(4));
        binding.btnRobotMoveL.setOnClickListener(v -> onClick(5));
        binding.btnRobotMoveSP.setOnClickListener(v -> onClick(6));

    }

    private void onClick(int taskType) {
        RobotType type = robotTypes[position];
        String ip = binding.textIp.getText().toString();
        Log.d("TAG", "onClick: ==="+ip);
        switch (taskType) {
            case 1:
                new Thread(new RobotInfoConfigTask(ip, type)).start();
                break;
            case 2:
                new Thread(new RobotProjectTask(ip, type)).start();
                break;
            case 3:
                new Thread(new RobotCommandTask(ip, type, CommandType.MoveAbsJCommand)).start();
                break;
            case 4:
                new Thread(new RobotCommandTask(ip, type, CommandType.MoveJCommand)).start();
                break;
            case 5:
                new Thread(new RobotCommandTask(ip, type, CommandType.MoveLCommand)).start();
                break;
            case 6:
                new Thread(new RobotCommandTask(ip, type, CommandType.MoveSPCommand)).start();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.position = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}