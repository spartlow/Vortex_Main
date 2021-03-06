package org.firstinspires.ftc.teamcode;

import android.graphics.Color;
import android.hardware.Sensor;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.EmptyStackException;
import java.util.concurrent.CancellationException;

/**
 * A class that initializes the autonomous and teleop systems.
 * Must be run at the start of every piece of code as follows:
 *  Init 'name' = new Init();
 *  'name'.init'mode'();
 * @author S Turner
 * @version 2017.1.3
 */

public class Init {
    private HardwareMap hardwareMap;

    /* Declare OpMode members. */
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private DcMotor liftMotor;
    private DcMotor shooterMotor;
    private DcMotor intakeMotor;

    /* Declare Servos */
    private Servo pushButton1;
    private Servo pushButton2;
    private Servo ballRelease;
    private Servo distanceFlag;
    private Servo forkRelease;
    private Servo lineFlag;

    /* Declare Sensors*/
    private ColorSensor colorSensor1;
    private ColorSensor colorSensor2;
    private OpticalDistanceSensor opticalSensor;
    private ModernRoboticsI2cGyro gyroSensor;
    private ModernRoboticsI2cRangeSensor ultrasonicSensor;

    private static final double MID_SERVO       =  0.5 ;
    private int NUM_TICKS = 10;

    /**
     * Initializes the Init class with all its objects set to null.
     */
    public Init() {
        leftMotor = null;
        rightMotor = null;
        liftMotor = null;
        shooterMotor = null;
        intakeMotor = null;
        pushButton1 = null;
        pushButton2 = null;
        ballRelease = null;
        forkRelease = null;
        colorSensor1 = null;
        colorSensor2 = null;
        opticalSensor = null;
        gyroSensor = null;
        ultrasonicSensor = null;
    }

    /**
     * Initializes the motors and sensors in the teleop configuration.
     * @param map The hardware mapping that was defined in LinearOpMode.
     */
    public void initTeleop(HardwareMap map) {
        hardwareMap = map;

        initAll();

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * Initializes the motors and sensors in the configuration necessary
     * for the autonomous layouts.
     * @param map The hardware mapping from LinearOpMode.
     * @param telemetry The telemetry object within the class.
     * @param opMode The current class. Should simply pass in 'this'.
     */
    public void initAuto(HardwareMap map, Telemetry telemetry, LinearOpMode opMode) {
        hardwareMap = map;

        initAll();

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        opMode.idle();

        // Set all motors to run with or without encoders.  Switch to use RUN_USING_ENCODERS when encoders are installed.
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0", "Starting at %7d :%7d",
                leftMotor.getCurrentPosition(),
                rightMotor.getCurrentPosition());
        telemetry.update();

        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();
    }

    /**
     * Defines the initialization defaults that all modes will use.
     * This is private so this comment should not be visible in any Javadocs...
     */
    private void initAll() {
        // Define and Initialize Motors
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor  = hardwareMap.dcMotor.get("right_drive");
        liftMotor = hardwareMap.dcMotor.get("lift");
        shooterMotor = hardwareMap.dcMotor.get("shooter");
        intakeMotor = hardwareMap.dcMotor.get("intake");

        ballRelease = hardwareMap.servo.get("ball_release");
        forkRelease = hardwareMap.servo.get("fork_release");
        pushButton1 = hardwareMap.servo.get("push_button1");
        pushButton2 = hardwareMap.servo.get("push_button2");
        distanceFlag = hardwareMap.servo.get("distance_flag");
        lineFlag = hardwareMap.servo.get("line_flag");

        ballRelease.setPosition(0.4);
        forkRelease.setPosition(0.5);
        pushButton1.setPosition(0.5);
        pushButton2.setPosition(0.0);
        distanceFlag.setPosition(0.5);
        lineFlag.setPosition(0.5);


        // Set the drive motor directions:
        // "REVERSE" the motor that runs backwards when connected directly to the battery.  Set to REVERSE if using AndyMark motors
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);
        liftMotor.setDirection(DcMotor.Direction.FORWARD);
        shooterMotor.setDirection(DcMotor.Direction.FORWARD);
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to zero power
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        liftMotor.setPower(0);
        shooterMotor.setPower(0);
        intakeMotor.setPower(0);

        //Define all of the sensors.
        colorSensor1 = hardwareMap.colorSensor.get("color_sensor1");
        colorSensor2 = hardwareMap.colorSensor.get("color_sensor2");  // Local version is set to default -- 0x3c
        colorSensor1.setI2cAddress(I2cAddr.create8bit(0x3a)); // Adjusting local version to look for 2nd color sensor - front sensor
        opticalSensor = hardwareMap.opticalDistanceSensor.get("optical_sensor");
        ultrasonicSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "ultrasonic_sensor");
        gyroSensor = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gyro_sensor");
        opticalSensor.enableLed(true);
    }


    /* Getter methods for the main code bases to use. */

    public DcMotor getLeftMotor() {
        return leftMotor;
    }

    public DcMotor getRightMotor() {
        return rightMotor;
    }

    public DcMotor getLiftMotor() {
        return liftMotor;
    }

    public DcMotor getShooterMotor() {
        return shooterMotor;
    }

    public DcMotor getIntakeMotor() {
        return intakeMotor;
    }

    public Servo getPushButton1() {
        return pushButton1;
    }

    public Servo getPushButton2() {
        return pushButton2;
    }

    public Servo getDistanceFlag() { return distanceFlag; }

    public Servo getForkRelease() { return forkRelease; }

    public Servo getLineFlag() {
        return lineFlag;
    }

    public Servo getBallRelease() { return ballRelease; }

    public ColorSensor getColorSensor1() {
        return colorSensor1;
    }

    public ColorSensor getColorSensor2() {
        return colorSensor2;
    }

    public OpticalDistanceSensor getOpticalSensor() {
        return opticalSensor;
    }

    public ModernRoboticsI2cGyro getGyroSensor() {
        return gyroSensor;
    }

    public ModernRoboticsI2cRangeSensor getUltrasonicSensor() {
        return ultrasonicSensor;
    }
}

