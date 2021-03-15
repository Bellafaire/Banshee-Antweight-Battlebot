/*TODO:
   -ESC interface
   -Analog voltage readings
*/

//requires https://github.com/madhephaestus/ESP32Servo


#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>
#include <ESP32Servo.h>
#include <driver/adc.h>

#define SERVICE_UUID        "b2ee9903-faef-4800-970b-28c4f243893f"
#define RIGHT_MOTOR_UUID         "b2ee9903-faef-4800-970b-28c4f2438940"
#define LEFT_MOTOR_UUID          "b2ee9903-faef-4800-970b-28c4f2438941"
#define WEAPON_SPEED_UUID        "b2ee9903-faef-4800-970b-28c4f2438942"
#define BATTERY_VOLTAGE_UUID     "b2ee9903-faef-4800-970b-28c4f2438943"
void initBLE();

Servo ESC;

boolean connected = false;
int rightMotorSpeed = 0;
int leftMotorSpeed = 0;
int weaponSpeed = 0;

unsigned long lastBatteryVoltageUpdate = 0;
#define BATTERY_VOLTAGE_UPDATE_RATE 1000 //ms between updates

//it's easier to just calibrate the ADC from test values
#define ADC_8V_VALUE 735
#define ADC_12V_VALUE 1180

#define ESC_CTRL 13
#define BAT_SENSE_ADC_CH 8
#define BAT_SENSE_PIN 25

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

  //servo
  // Allow allocation of timer 3
  ESP32PWM::allocateTimer(3);
  ESC.setPeriodHertz(50);// Standard 50hz servo
  ESC.attach(ESC_CTRL, 500, 2400);

  //standard IO config
  pinMode(MOTOR_1A, OUTPUT);
  pinMode(MOTOR_2A, OUTPUT);
  pinMode(MOTOR_3A, OUTPUT);
  pinMode(MOTOR_4A, OUTPUT);
  pinMode(2, OUTPUT); //DevkitC blue LED
}

void writeMotor(int motor, int speed) {
  if (motor == 1) {
    ledcWrite(MOTOR2_CHANNEL, abs(speed));
    digitalWrite(MOTOR_3A, speed > 0 );
    digitalWrite(MOTOR_4A, speed < 0 );
  } else {
    ledcWrite(MOTOR1_CHANNEL, abs(speed));
    digitalWrite(MOTOR_1A, speed < 0);
    digitalWrite(MOTOR_2A, speed > 0);
  }
}

void setWeaponSpeed(int speed) {
  ESC.write(map(speed, 0, 100, 0, 180));
}

float getBatteryVoltage() {
  //apply linear interpretation on values read from the ADC, technically speaking this allows the voltage divider to be made
  //from any values of resistor as long as the voltage present on the pin is < 3.6V. Calibration values can be filled in above. 
  return ((float)readBatRaw() - (float)ADC_8V_VALUE) * ((12.0 - 8.0) / (ADC_12V_VALUE - ADC_8V_VALUE)) + 8;
}

unsigned long lastDebugString = 0;

int readBatRaw() {
  int read_raw;
  adc2_config_channel_atten((adc2_channel_t) BAT_SENSE_ADC_CH, ADC_ATTEN_11db );
  adc2_get_raw((adc2_channel_t) BAT_SENSE_ADC_CH, ADC_WIDTH_BIT_12, &read_raw);
  return read_raw;
}

void loop() {
  if (connected) {
    writeMotor(0, leftMotorSpeed);
    writeMotor(1, rightMotorSpeed);
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

  if (250 + lastDebugString < millis()) {
    lastDebugString = millis();
    Serial.println("Connected: " + String(connected) + " Left Motor Speed: " + String(leftMotorSpeed) + " Right Motor Speed: " + String(rightMotorSpeed) +  " Weapon Motor Speed: " + String(weaponSpeed) + " Battery Voltage: " + String(getBatteryVoltage()));
  }

}
