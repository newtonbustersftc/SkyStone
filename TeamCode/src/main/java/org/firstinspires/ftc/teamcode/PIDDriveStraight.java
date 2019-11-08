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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.io.File;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="PIDDriveStraight", group="Test")
public class PIDDriveStraight extends LinearOpMode {
    private RobotHardware robot;
    private RobotNavigator navigator;
    private PIDController pidDrive;
    private RobotProfile robotProfile;
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        try{
            robotProfile = RobotProfile.loadFromFile(new File("/sdcard/FIRST/profile.json"));
        } catch (Exception e) {
        }
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        robot = new RobotHardware();
        robot.init(hardwareMap, robotProfile);
        robot.getBulkData1();
        navigator = new RobotNavigator(robotProfile);
        navigator.reset();
        navigator.setInitPosition(0,0,0);
        navigator.setInitEncoderCount(robot.getEncoderCounts(RobotHardware.EncoderType.LEFT),
                robot.getEncoderCounts(RobotHardware.EncoderType.RIGHT),
                robot.getEncoderCounts(RobotHardware.EncoderType.HORIZONTAL));

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        robot.getBulkData1();
        int leftEncoder = robot.getEncoderCounts(RobotHardware.EncoderType.LEFT);
        int rightEncoder = robot.getEncoderCounts(RobotHardware.EncoderType.RIGHT);
        int middleEncoder = robot.getEncoderCounts(RobotHardware.EncoderType.HORIZONTAL);
        navigator.updateAllPositions(leftEncoder, rightEncoder, middleEncoder);
        robot.setMotorPower(0.5,0.5,0.5,0.5);

        pidDrive = new PIDController(0.1,-0.001,0);
        pidDrive.setSetpoint(0);
        pidDrive.setOutputRange(0, 1);
        pidDrive.setInputRange(-5, 5);
        pidDrive.enable();
        while (navigator.getWorldY() < 200) {
            robot.getBulkData1();
            leftEncoder = robot.getEncoderCounts(RobotHardware.EncoderType.LEFT);
            rightEncoder = robot.getEncoderCounts(RobotHardware.EncoderType.RIGHT);
            middleEncoder = robot.getEncoderCounts(RobotHardware.EncoderType.HORIZONTAL);
            navigator.updateAllPositions(leftEncoder, rightEncoder, middleEncoder);
            double error = navigator.getWorldX();
            double correction = pidDrive.performPID(error);
            robot.setMotorPower(0.5 + correction,0.5 - correction, 0.5 + correction , 0.5 - correction);
            telemetry.addData("x pos", navigator.getWorldX());
            telemetry.addData("y pos", navigator.getWorldY());
            telemetry.addData("left encoder", leftEncoder);
            telemetry.addData("right encoder", rightEncoder);
            telemetry.addData("middle encoder", middleEncoder);
            telemetry.update();
        }

        robot.getBulkData1();
        leftEncoder = robot.getEncoderCounts(RobotHardware.EncoderType.LEFT);
        rightEncoder = robot.getEncoderCounts(RobotHardware.EncoderType.RIGHT);
        middleEncoder = robot.getEncoderCounts(RobotHardware.EncoderType.HORIZONTAL);
        navigator.updateAllPositions(leftEncoder, rightEncoder, middleEncoder);
        double currentDistance = navigator.getWorldY();
        robot.setMotorPower(0,0,0,0);
        try {
            Thread.sleep(3000);
        }
        catch (Exception e) {
        }
        robot.getBulkData1();
        leftEncoder = robot.getEncoderCounts(RobotHardware.EncoderType.LEFT);
        rightEncoder = robot.getEncoderCounts(RobotHardware.EncoderType.RIGHT);
        middleEncoder = robot.getEncoderCounts(RobotHardware.EncoderType.HORIZONTAL);
        navigator.updateAllPositions(leftEncoder, rightEncoder, middleEncoder);
        double latestDistance = navigator.getWorldY();
        telemetry.addData("Stop Distance", latestDistance - currentDistance);
        telemetry.addData("Start Distance", latestDistance);
        telemetry.addData("End Distance", currentDistance);
        telemetry.update();
        try {
            Thread.sleep(9000);
        }
        catch (Exception e) {
        }
    }
}