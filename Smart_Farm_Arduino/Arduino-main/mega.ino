#include <Thread.h>
#include <ThreadController.h>

#include <SoftwareSerial.h>
#include <Adafruit_NeoPixel.h>
#include <DHT.h>
#include <Servo.h>
#include <Wire.h>
#include <SimpleTimer.h>

#define TX 11 // nodeMcu softSerial RX -> arduino 3
#define RX 10 // nodeMcu softSerial TX -> arduino 2
#define PIN_DHT 34   // 온습도 센서 

#define LED_PIN 41    // 네오픽셀 D핀과 연결한 아두이노 핀 번호
#define LED_COUNT 59 // 네오픽셀 LED 개

#define Echo1 22      // 초음파1
#define Trig1 23      // 초음파1

#define Echo2 24      // 초음파2
#define Trig2 25      // 초음파2

#define Echo3 26      // 초음파3
#define Trig3 27      // 초음파3

// 4채널 릴레이
#define pump1 38       // 펌프1
#define pump2 39       // 펌프2
#define pump3 40       // 펌프3

// 2채널 릴레이
#define fan1 36 // 쿨링팬
#define fan2 37 // 쿨링팬

//1채널 릴레이
#define doorServo 12 // 자물쇠

#define illuminanceSensor A15

#define doorSensor 35 // 문 센서

#define waterFlow1 2 // 수량센서1
#define waterFlow2 3 // 수량센서2
#define waterFlow3 7 // 수량센서3

SoftwareSerial nodeSerial(RX, TX); // (RX, TX)

Servo servo; 

//온습도센서
DHT myDHT11(PIN_DHT, DHT11);
String tempC;
String humid;

//자석센서
String magneticValue;

//초음파센서
long firstDistance;
long secondDistance;
long thirdDistance;

//조도센서
int illuminanceValue;

// led 스트랩
int color_r = 255; // RED
int color_g = 255; // GREEN
int color_b = 255; // BLUE
Adafruit_NeoPixel strip(LED_COUNT, LED_PIN, NEO_GRB + NEO_KHZ800);

long previousMillis = 0;
const long interval = 200;  

volatile double water = 0;

ThreadController controll = ThreadController();
Thread thread1 = Thread();
Thread thread2 = Thread();

SimpleTimer timer;

void setup(){
  Serial.begin(57600);     // 시리얼 모니터용 하드웨어 시리얼 시작
  nodeSerial.begin(57600); // 컨트롤러 명령 수신용 소프트웨어 시리얼 시작

  pinMode(doorSensor, INPUT_PULLUP); //자석센서

  pinMode(Trig1, OUTPUT); //초음파센서
  pinMode(Echo1, INPUT);  //초음파센서

  pinMode(Trig2, OUTPUT); //초음파센서
  pinMode(Echo2, INPUT);  //초음파센서

  pinMode(Trig3, OUTPUT); //초음파센서
  pinMode(Echo3, INPUT);  //초음파센서

  pinMode(fan1, OUTPUT); //쿨링팬1
  pinMode(fan2, OUTPUT); //쿨링팬2

  pinMode(pump1, OUTPUT); //펌프1
  pinMode(pump2, OUTPUT); //펌프2
  pinMode(pump3, OUTPUT); //펌프3

  
  pinMode(waterFlow1,INPUT_PULLUP);
  pinMode(waterFlow2,INPUT_PULLUP);
  pinMode(waterFlow3,INPUT_PULLUP);
  
  attachInterrupt(digitalPinToInterrupt(waterFlow1),flow, FALLING);

 // Wire.begin(1);                // use address #8 (0x08)
 // Wire.onRequest(requestEvent); 
  //Wire.onReceive(receiveEvent);
  
  doorOpen();


  strip.begin(); // 네오픽셀 초기화(필수)
  myDHT11.begin();

  // 모든 네오픽셀에 색 설정하기
  for (int i = 0; i < LED_COUNT; i++)
  {
    strip.setPixelColor(i, color_r, color_g, color_b);
  }
  // 네오픽셀 설정값 적용하기
  strip.show();

  digitalWrite(fan1, LOW);
  digitalWrite(fan2, LOW);
  digitalWrite(pump1, HIGH);
  digitalWrite(pump2, HIGH);
  digitalWrite(pump3, HIGH);

  thread1.onRun(thread1Loop);
  thread1.setInterval(5);

  thread2.onRun(thread2Loop);
  thread2.setInterval(5);
  
  controll.add(&thread1);
  controll.add(&thread2);
}

void loop(){
   controll.run();
}
void thread1Loop(){
 String action = "";
  while(nodeSerial.available() > 0) {
    String action = nodeSerial.readStringUntil(';'); // 1바이트 값 읽고 시리얼 버퍼를 비운다 - 이값은 버리는 값임     
      Serial.println(action);
      if (action.indexOf("sensing")>=0){
        delay(100);
        serialCommunication();
        
      }
      //-----------조명제어-----------//
      else if (action.indexOf("ledOff")>=0){
        ledControl(0,0,0);
      }else if (action.indexOf("ledOn")>=0){
        ledControl(255,255,255);
      //-----------쿨링팬제어-----------//
      }else if(action.indexOf("fanOn")>=0){
        digitalWrite(fan1, LOW);
        digitalWrite(fan2, LOW);
      }else if(action.indexOf("fanOff")>=0){
        digitalWrite(fan1, HIGH);
        digitalWrite(fan2, HIGH);

      //-----------펌프제어-----------//
      }else if(action.indexOf("pump1On")>=0){
        PumpOn1();
      }else if(action.indexOf("pump1Off")>=0){
        digitalWrite(pump1, HIGH);
      }else if(action.indexOf("pump2On")>=0){
        PumpOn2();
      }else if(action.indexOf("pump2Off")>=0){
        digitalWrite(pump2, HIGH);
      }else if(action.indexOf("pump3On")>=0){
        PumpOn3();
      }else if(action.indexOf("pump3Off")>=0){
        digitalWrite(pump3, HIGH);
      }
      //-----------문 제어-----------//
      else if(action.indexOf("doorOpen")>=0){
        doorOpen();
      }else if(action.indexOf("doorClose")>=0){
        doorClose();
      }

      
      action = ""; 
  }
}
void thread2Loop(){
  timer.run();
}
void PumpOn1(){
  digitalWrite(pump1, LOW);
  timer.setTimeout(5000, PumpOff1);
}
void PumpOn2(){
  digitalWrite(pump2, LOW);
  timer.setTimeout(5000, PumpOff2);
}
void PumpOn3(){
  digitalWrite(pump3, LOW);
  timer.setTimeout(5000, PumpOff3);
}

void PumpOff1(){
  digitalWrite(pump1, HIGH);
}
void PumpOff2(){
  digitalWrite(pump2, HIGH);
}
void PumpOff3(){
  digitalWrite(pump3, HIGH);
}


void doorOpen(){
  servo.attach(doorServo);
  int angle;
 
  for(angle = 0; angle < 90; angle++)  // 열기
      {
        servo.write(angle); 
  }
}
void doorClose(){
  servo.attach(doorServo);
  
  int angle;
  for(angle = 95; angle > 0; angle--)  // 잠그기
      {
        servo.write(angle); 
  }
}
void sensing()
{
  firstDistance = distance(Trig1, Echo1);
  secondDistance = distance(Trig2, Echo2);
  thirdDistance = distance(Trig3, Echo3);
  tempC = String(myDHT11.readTemperature(false), 3);
  humid = String(myDHT11.readHumidity(false), 3);
  magneticValue = String(digitalRead(doorSensor));
  illuminanceValue = analogRead(illuminanceSensor);
  Serial.println(illuminanceValue);
  
}
void serialCommunication()
{
  sensing();
  delay(100);
  nodeSerial.print("temp");
  nodeSerial.print(tempC); // 컨트롤러에 String 형식으로 전송
  nodeSerial.print(";");
  nodeSerial.print("hum");
  nodeSerial.print(humid);
  nodeSerial.print(";");
  nodeSerial.print("mag");
  nodeSerial.print(magneticValue);
  nodeSerial.print(";");
  nodeSerial.print("1st");
  nodeSerial.print(firstDistance);
  nodeSerial.print(";");
  nodeSerial.print("2nd");
  nodeSerial.print(secondDistance);
  nodeSerial.print(";");
  nodeSerial.print("3rd");
  nodeSerial.print(thirdDistance);
  nodeSerial.print(";");
  nodeSerial.print("illu");
  nodeSerial.print(illuminanceValue);
  nodeSerial.print(";");

}
long distance(int trig, int echo)
{
  long duration, distance;

  digitalWrite(trig, LOW);
  delayMicroseconds(2);
  digitalWrite(trig, HIGH);
  delayMicroseconds(10);
  digitalWrite(trig, LOW);

  duration = pulseIn(echo, HIGH);
  distance = duration * 0.034 / 2;

  return (distance);
}
void ledControl(int r,int g,int b){
  for (int i = 0; i < LED_COUNT; i++) {
    strip.setPixelColor(i, r, g, b);
    }
    strip.show();

}

void flow(){
  water += (1/5888.0)*1000; //단위mL
}
