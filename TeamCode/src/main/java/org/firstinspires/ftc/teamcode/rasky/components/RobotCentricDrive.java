package org.firstinspires.ftc.teamcode.rasky.components;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.rasky.utilities.DrivingMotors;

/**
 * Basic, Joystick based, mecanum drive.
 *
 * @author Lucian
 * @version 1.2
 */
public class RobotCentricDrive {

    DrivingMotors motors;
    Gamepad gamepad;

    // Speed Multiplier
    double speed = 1.0;
    // If the Joystick has a lower value than this one the robot will not move
    final double controllerDeadzone = 0.15;
    // If the robot should move in reverse or not
    double reverse = 1.0;

    public RobotCentricDrive(DrivingMotors motors, Gamepad gamepad) {
        this.motors = motors;
        this.gamepad = gamepad;
    }

    /**
     * Call this function asynchronously from the while in the OpMode
     */
    public void run() {

        double x = gamepad.left_stick_x; // Strafe, Horizontal Axis
        double y = -gamepad.left_stick_y; // Forward, Vertical Axis (joystick has +/- flipped)
        double r = gamepad.right_stick_x; // Rotation, Horizontal Axis

        x = addons(x) * reverse;
        y = addons(y) * reverse;
        r = addons(r);

        /*
        If the rotation and forward direction are both engaged the value can go past 1.0 (100%).
        To prevent that we implement a denominator that normalizes the values to 100% max.
         */
        double normalizer = Math.max(Math.abs(x) + Math.abs(y) + Math.abs(r), 1.0);

        double leftFrontPower = (y + x + r) / normalizer;
        double rightFrontPower = (y - x - r) / normalizer;
        double leftRearPower = (y - x + r) / normalizer;
        double rightRearPower = (y + x - r) / normalizer;

        motors.leftFront.setPower(leftFrontPower);
        motors.rightFront.setPower(rightFrontPower);
        motors.leftRear.setPower(leftRearPower);
        motors.rightRear.setPower(rightRearPower);
    }

    /**
     * Gets the coordinate of a joystick and verifies if it's after the controller deadzone.
     * Also calculates the result after the speed multiplier and returns it.
     *
     * @param coord The coordinate to make modifications to
     * @return The result after modifications
     */
    private double addons(double coord) {
        if (Math.abs(coord) < controllerDeadzone) {
            coord = 0;
        }
        coord = coord * speed;

        return coord;
    }

    public void setReverse(boolean isReverse) {
        if (isReverse) {
            reverse = -1.0;
        } else {
            reverse = 1.0;
        }
    }

}
