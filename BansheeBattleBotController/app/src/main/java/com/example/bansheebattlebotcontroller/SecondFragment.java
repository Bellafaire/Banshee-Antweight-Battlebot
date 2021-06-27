package com.example.bansheebattlebotcontroller;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import io.github.controlwear.virtual.joystick.android.JoystickView;

/* the more interesting page of the app where we communicate with the robot through touch controls.
Uses the BluetoothInterface class in order to abstract away some of the communication

TODO add a proper disconnect button, only way to disconnect at the moment is to close the app.
 */
public class SecondFragment extends Fragment {

    //status text that's displayed in the center. If you wanted to add more status items then you can put them below
    //currently reports only the below variables (with batteryVoltage coming directly from the bot)
    TextView statusText;
    SeekBar weaponControl;

    public static int leftMotorSpeed = 0;
    public static int rightMotorSpeed = 0;
    public static int weaponSpeed = 0;
    public static double batteryVoltage = 0.0;
    boolean active = true;
    Handler handler;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    //update the bot periodically, this ensures that the bot isn't flooded with updates from the user constantly.
    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            try {
                //check to make sure that we've completed service discovery, otherwise no data can be sent and we'd only be
                //backing up the queue with useless information
                if (MainActivity.bluetooth.isServiceDiscovered()) {
                    update(false);
                } else {
                    //have status text reflect that we're still in the process of connecting
                    statusText.setText("Connecting...");
                }
            } catch (NullPointerException e) {
                Log.e("runnable", "could not update ble device because of null pointer");
            }
            if (active) {
                //150ms is sufficent for a bot like this, it takes time to physically respond anyway.
                handler.postDelayed(this, 150);
            }
        }
    };


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        active = true;
        handler = new Handler();
        handler.post(updater);
        statusText = (TextView) view.findViewById(R.id.status);

        view.findViewById(R.id.allStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAll(view);
            }
        });

        //using a joystick library created by controlwear (https://github.com/controlwear/virtual-joystick-android)
        JoystickView joystick = (JoystickView) view.findViewById(R.id.joystickView);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                //take the joystick position and translate it into speeds for the two motors, with the below equations forward will put both fully on and right/left will turn the motors in opposite directions.
                rightMotorSpeed = constrain((int) (255 * (strength / 100.0) * Math.sin(angle * Math.PI / 180.0)) - (int) (255 * (strength / 100.0) * Math.cos(angle * Math.PI / 180.0)), -255, 255);
                leftMotorSpeed = constrain((int) (255 * (strength / 100.0) * Math.sin(angle * Math.PI / 180.0)) + (int) (255 * (strength / 100.0) * Math.cos(angle * Math.PI / 180.0)), -255, 255);
            }
        });

        //using a seek bar in order to control the weapon speed.
        weaponControl = (SeekBar) view.findViewById(R.id.seekBar4);
        weaponControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                weaponSpeed = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    public void stopAll(View view) {
        // Do something in response to button click
        MainActivity.bluetooth.clearQueue();
        leftMotorSpeed = 0;
        rightMotorSpeed = 0;
        weaponSpeed = 0;
        weaponControl.setVerticalScrollbarPosition(0);
        update(true);

    }


    //provide value and range, get the value within that range.
    public static int constrain(int x, int min, int max) {
        if (x > max) {
            return max;
        } else if (x < min) {
            return min;
        } else {
            return x;
        }
    }

    int lastRightMotorSpeed = 0;
    int lastLeftMotorSpeed = 0;
    int lastWeaponMotorSpeed = 0;

    //updates status text on the android device screen and sends over bluetooth updates to the bot.
    void update(boolean override) {
        statusText.setText(
                "Status\n" +
                        "Left Motor: " + leftMotorSpeed + "\n" +
                        "Right Motor: " + rightMotorSpeed + "\n" +
                        "Weapon Speed: " + weaponSpeed + "\n" +
                        "Battery Voltage: " + batteryVoltage + "\n" +
                        "Queue Length: " + MainActivity.bluetooth.getQueueLength() + "\n"
        );

        //only send bluetooth updates if the values actually changed (helps to keep the queue down)
        if (rightMotorSpeed != lastRightMotorSpeed || override) {
            MainActivity.bluetooth.write(rightMotorSpeed + "", BluetoothInterface.RIGHT_MOTOR_UUID);
            lastRightMotorSpeed = rightMotorSpeed;
        }
        if (leftMotorSpeed != lastLeftMotorSpeed || override) {
            MainActivity.bluetooth.write(leftMotorSpeed + "", BluetoothInterface.LEFT_MOTOR_UUID);
            lastLeftMotorSpeed = leftMotorSpeed;
        }
        if (weaponSpeed != lastWeaponMotorSpeed || override) {
            MainActivity.bluetooth.write(weaponSpeed + "", BluetoothInterface.WEAPON_SPEED_UUID);
            lastWeaponMotorSpeed = weaponSpeed;

        }
    }
}