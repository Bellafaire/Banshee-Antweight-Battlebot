/*TODO: 
 * -ESC interface
 * -Analog voltage readings
 */


#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>

#define SERVICE_UUID        "b2ee9903-faef-4800-970b-28c4f243893f"
#define RIGHT_MOTOR_UUID         "b2ee9903-faef-4800-970b-28c4f2438940"
#define LEFT_MOTOR_UUID          "b2ee9903-faef-4800-970b-28c4f2438941"
#define WEAPON_SPEED_UUID        "b2ee9903-faef-4800-970b-28c4f2438942"
#define BATTERY_VOLTAGE_UUID     "b2ee9903-faef-4800-970b-28c4f2438943"
void initBLE();

boolean connected = false;
int rightMotorSpeed = 0;
int leftMotorSpeed = 0;
int weaponSpeed = 0;

unsigned long lastBatteryVoltageUpdate = 0;
#define BATTERY_VOLTAGE_UPDATE_RATE 1000 //ms between updates

#define MOTOR1_EN 17
#define MOTOR1_CHANNEL 0
#define MOTOR_1A 16
#define MOTOR_2A 4

#define MOTOR2_EN 26
#define MOTOR2_CHANNEL 1
#define MOTOR_3A 27
#define MOTOR_4A 14

void setup() {
  Serial.begin(115200);
  initBLE();
  //PWM Config
  ledcSetup(MOTOR1_CHANNEL, 1000, 8);
  ledcSetup(MOTOR2_CHANNEL, 1000, 8);
  ledcAttachPin(MOTOR1_EN, MOTOR1_CHANNEL);
  ledcAttachPin(MOTOR2_EN, MOTOR2_CHANNEL);

  //standard IO config
  pinMode(MOTOR_1A, OUTPUT);
  pinMode(MOTOR_2A, OUTPUT);
  pinMode(MOTOR_3A, OUTPUT);
  pinMode(MOTOR_4A, OUTPUT);
  pinMode(2, OUTPUT); //DevkitC blue LED
}

void writeMotor(int motor, int speed) {
  if (motor) {
    ledcWrite(MOTOR2_CHANNEL, abs(speed));
    digitalWrite(MOTOR_3A, speed > 0 );
    digitalWrite(MOTOR_4A, speed < 0 );
  } else {
    ledcWrite(MOTOR1_CHANNEL, abs(speed));
    digitalWrite(MOTOR_1A, speed > 0);
    digitalWrite(MOTOR_2A, speed < 0);
  }
}

void setWeaponSpeed(int speed) {

}

float getBatteryVoltage() {
  return 0.0;
}

unsigned long lastDebugString = 0;

void loop() {
  if (connected) {
    writeMotor(0, rightMotorSpeed);
    writeMotor(1, leftMotorSpeed);
    setWeaponSpeed(weaponSpeed);

    if (BATTERY_VOLTAGE_UPDATE_RATE + lastBatteryVoltageUpdate < millis()) {
      lastBatteryVoltageUpdate = millis();
      updateBatteryVoltage();
    }
  } else {
    writeMotor(0, 0);
    writeMotor(1, 0);
    setWeaponSpeed(0);
  }

  if (500 + lastDebugString < millis()) {
    lastDebugString = millis();
    Serial.println("Connected: " + String(connected) + " Left Motor Speed: " + String(leftMotorSpeed) + " Right Motor Speed: " + String(rightMotorSpeed) + + " Weapon Motor Speed: " + String(weaponSpeed));
  }

}
