#define MOTOR_1EN 17
#define MOTOR_1A 16
#define MOTOR_2A 4

#define MOTOR_2EN 26
#define MOTOR_3A 27
#define MOTOR_4A 14

void setup() {
  // put your setup code here, to run once:
  pinMode(MOTOR_1EN, OUTPUT);
  pinMode(MOTOR_1A, OUTPUT);
  pinMode(MOTOR_2A, OUTPUT);
  pinMode(MOTOR_2EN, OUTPUT);
  pinMode(MOTOR_3A, OUTPUT);
  pinMode(MOTOR_4A, OUTPUT);


}

void writeMotor(int motor, int dir, int speed) {
  if (motor) {
    digitalWrite(MOTOR_2EN, speed);
    digitalWrite(MOTOR_3A, !dir);
    digitalWrite(MOTOR_4A, dir);
  } else {
    digitalWrite(MOTOR_1EN, speed);
    digitalWrite(MOTOR_1A, !dir);
    digitalWrite(MOTOR_2A, dir);
  }
}


void loop() {
  for (int a = 0; a <= 1; a++) {
    for (int b = 0; b <= 1; b++) {
      writeMotor(0, a, b);
      writeMotor(1, 0, 0);
      delay(1000);
    }
  }

  for (int a = 0; a <= 1; a++) {
    for (int b = 0; b <= 1; b++) {
      writeMotor(0, 0, 0);
      writeMotor(1, a, b);
      delay(1000);
    }
  }

}

