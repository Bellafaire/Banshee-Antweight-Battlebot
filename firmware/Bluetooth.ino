BLECharacteristic *batteryVoltage;

class cb : public BLEServerCallbacks    {
    void onConnect(BLEServer* pServer) {
      connected = true;
      digitalWrite(2, HIGH);
      Serial.println("BLE Device Connected");
    }
    void onDisconnect(BLEServer* pServer) {
      connected = false;
      digitalWrite(2, LOW);
      Serial.println("BLE Device Disconnected");
    }
};

class rightMotorCallback : public BLECharacteristicCallbacks  {
    void onWrite(BLECharacteristic *pCharacteristic) {
      int speed = String(pCharacteristic->getValue().c_str()).toInt();
      rightMotorSpeed =  speed;
//      Serial.println("**** set right motor speed to: " + String(speed));
    }
};

class leftMotorCallback : public BLECharacteristicCallbacks  {
    void onWrite(BLECharacteristic *pCharacteristic) {
      int speed = String(pCharacteristic->getValue().c_str()).toInt();
      leftMotorSpeed =  speed;
//      Serial.println("**** set left motor speed to: " + String(speed));
    }
};


class weaponMotorCallback : public BLECharacteristicCallbacks  {
    void onWrite(BLECharacteristic *pCharacteristic) {
      int speed = String(pCharacteristic->getValue().c_str()).toInt();
      weaponSpeed =  speed;
//      Serial.println("**** set weapon motor speed to: " + String(speed));
    }
};


void initBLE() {
  BLEDevice::init("Banshee");
  BLEServer *pServer = BLEDevice::createServer();
  BLEService *pService = pServer->createService(SERVICE_UUID);

  //add server callback so we can detect when we're connected.
  pServer->setCallbacks(new cb());

  //right motor
  BLECharacteristic *rightMotor = pService->createCharacteristic(
                                    RIGHT_MOTOR_UUID,
                                    BLECharacteristic::PROPERTY_WRITE
                                  );
  rightMotor->setCallbacks(new rightMotorCallback());
  rightMotor->setValue("0");

  //left motor
  BLECharacteristic *leftMotor = pService->createCharacteristic(
                                   LEFT_MOTOR_UUID,
                                   BLECharacteristic::PROPERTY_WRITE
                                 );
  leftMotor->setCallbacks(new leftMotorCallback());
  leftMotor->setValue("0");


  //weapon
  BLECharacteristic *weaponSpeed = pService->createCharacteristic(
                                     WEAPON_SPEED_UUID,
                                     BLECharacteristic::PROPERTY_WRITE
                                   );
  weaponSpeed->setCallbacks(new weaponMotorCallback());
  weaponSpeed->setValue("0");

  //battery voltage
  batteryVoltage = pService->createCharacteristic(
                     BATTERY_VOLTAGE_UUID,
                     BLECharacteristic::PROPERTY_READ   |
                     BLECharacteristic::PROPERTY_NOTIFY
                   );

  pService->start();
  // BLEAdvertising *pAdvertising = pServer->getAdvertising();  // this still is working for backward compatibility
  BLEAdvertising *pAdvertising = BLEDevice::getAdvertising();
  pAdvertising->addServiceUUID(SERVICE_UUID);
  pAdvertising->setScanResponse(true);
  pAdvertising->setMinPreferred(0x06);  // functions that help with iPhone connections issue
  pAdvertising->setMinPreferred(0x12);
  BLEDevice::startAdvertising();
}

void updateBatteryVoltage() {
  batteryVoltage->setValue(String(getBatteryVoltage()).c_str());
  batteryVoltage->notify();
}
