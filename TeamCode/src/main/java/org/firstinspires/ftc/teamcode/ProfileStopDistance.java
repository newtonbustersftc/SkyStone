/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
  OpMode to profile robot stop distance, and more
 */

@TeleOp(name="ProfileStopDistance", group="Profile")
@Disabled
public class ProfileStopDistance extends LinearOpMode {
    private RobotHardware robot;
    private RobotNavigator navigator;
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        robot = new RobotHardware();
        robot.init(hardwareMap, null);
        robot.getBulkData1();
        navigator = new RobotNavigator(null);
        navigator.reset();
        navigator.setInitPosition(0,0,0);
        navigator.setEncoderCounts(robot.getEncoderCounts(RobotHardware.EncoderType.LEFT),
                robot.getEncoderCounts(RobotHardware.EncoderType.RIGHT),
                robot.getEncoderCounts(RobotHardware.EncoderType.HORIZONTAL));

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        robot.getBulkData1();
        int leftEncoder = robot.getEncoderCounts(RobotHardware.EncoderType.LEFT);
        int rightEncoder = robot.getEncoderCounts(RobotHardware.EncoderType.RIGHT);
        int middleEncoder = - robot.getEncoderCounts(RobotHardware.EncoderType.HORIZONTAL);
        navigator.updateEncoderPos(leftEncoder, rightEncoder, middleEncoder);
        robot.setMotorPower(1,-1,-1,1);
        try {
            Thread.sleep(2000);
        }
        catch (Exception e) {
            // nothing to do
        }
        robot.getBulkData1();
        leftEncoder = robot.getEncoderCounts(RobotHardware.EncoderType.LEFT);
        rightEncoder = robot.getEncoderCounts(RobotHardware.EncoderType.RIGHT);
        middleEncoder = - robot.getEncoderCounts(RobotHardware.EncoderType.HORIZONTAL);
        navigator.updateEncoderPos(leftEncoder, rightEncoder, middleEncoder);
        double currentDistance = navigator.getWorldX();
        robot.setMotorPower(0,0,0,0);
        try {
            Thread.sleep(3000);
        }
        catch (Exception e) {
        }
        robot.getBulkData1();
        leftEncoder = robot.getEncoderCounts(RobotHardware.EncoderType.LEFT);
        rightEncoder = robot.getEncoderCounts(RobotHardware.EncoderType.RIGHT);
        middleEncoder = - robot.getEncoderCounts(RobotHardware.EncoderType.HORIZONTAL);
        navigator.updateEncoderPos(leftEncoder, rightEncoder, middleEncoder);
        double latestDistance = navigator.getWorldX();
        telemetry.addData("Stop Distance", latestDistance - currentDistance);
        telemetry.addData("Start Distance", latestDistance);
        telemetry.addData("End Distance", currentDistance);
        telemetry.update();
        try {
            Thread.sleep(9000);
        }
        catch (Exception e) {
            // nothing to do
        }
    }
}
