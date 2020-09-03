#include <SoftwareSerial.h>
#include "DHT.h"  
#define BT_RXD 8
#define BT_TXD 7
SoftwareSerial bluetooth(BT_RXD, BT_TXD);
DHT dht(10, DHT22);  
unsigned long time_previous, time_current;

void setup(){
  Serial.begin(9600);
  dht.begin();
  bluetooth.begin(9600);
  time_previous = millis();  
}
 
void loop(){
  time_current = millis();
  
  float h = dht.readHumidity();
  float t = dht.readTemperature(); 
 

  if(time_current - time_previous >= 2000){
    bluetooth.print(t);
    bluetooth.print(",");
    bluetooth.print(h);
    bluetooth.println();
    Serial.println(t);
    Serial.println(h);
    
    
    if (bluetooth.available()) {
      Serial.write(bluetooth.read());
    }
     if (Serial.available()) {
      bluetooth.write(Serial.read());
    }
    time_previous = time_current;
  }
 
  
}
