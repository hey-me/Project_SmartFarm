#include <SimpleTimer.h>

#include <Thread.h>
#include <ThreadController.h>

#include <Adafruit_NeoPixel.h>
#include <ArduinoJson.h>
#include <DHT.h>
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <SoftwareSerial.h>
#include <WebSocketsServer.h>
#include <ESP8266WebServer.h>
#include <time.h>

#include <Wire.h>


#define SERVER_IP "192.168.166.245:8080"

#ifndef STASSID
//#define STASSID "U+Net2BC0"
//#define STAPSK  "HCDF4@5FDF"
#define STASSID "seung"
#define STAPSK  "seunguk12"


#define TX D6 // arduino softSerial RX -> NodeMcu D6(TX)
#define RX D7 // arduino softSerial TX -> NodeMcu D7(RX) 
#endif
SoftwareSerial arduSerial(RX, TX); // (RX, TX)



String tempC;
String humid;
String magneticValue;
String firstdistance;
String secondDistance;
String thirdDistance;
String illuminanceValue;

char curTime[20]; //시간

int timer1Id ;

int timer2Id ;

int timer3Id ;

SimpleTimer timer;

int settingH1;
int settingM1;

int settingH2;
int settingM2;

int settingH3;
int settingM3;

int settingH4;
int settingM4;

ThreadController controll = ThreadController();
Thread thread = Thread();
Thread thread2 = Thread();
Thread thread3 = Thread();
Thread thread4 = Thread();

WebSocketsServer webSocket = WebSocketsServer(80); //WebSocket



//-------------------------------------------------------------------------------

void setup() {
  Serial.begin(57600);         // 시리얼 모니터용 하드웨어 시리얼 시작
  arduSerial.begin(57600);     // 센서 제어용 소프트웨어 시리얼 시작
  pinMode(LED_BUILTIN, OUTPUT);  
  
  Serial.println();

  WiFi.begin(STASSID, STAPSK);//wifi 연결
  
  while (WiFi.status() != WL_CONNECTED) { //wifi 연결 대기
    delay(500);
    Serial.print(".");
    digitalWrite(LED_BUILTIN, HIGH);
  }
  Serial.println("");
  Serial.print("Connected! IP address: ");
  Serial.println(WiFi.localIP());
  digitalWrite(LED_BUILTIN, LOW);
  webSocket.begin();
  webSocket.onEvent(webSocketEvent);//콜백실행
  
  configTime(9*3600, 0, "pool.ntp.org", "time.nist.gov");  // Timezone 9 for Korea
  while (!time(nullptr)) delay(500);

  Wire.begin(); // join i2c bus (address optional for master)
  Wire.beginTransmission(1);

  thread.onRun(transmitData);
//  thread.setInterval(600000);
  thread.setInterval(30000);
  
  thread2.onRun(setTiming);
  thread2.setInterval(1000);

  thread3.onRun(webScoketLoop);
  thread3.setInterval(5);

  thread4.onRun(actionCycle);
  thread4.setInterval(1000);
  
  controll.add(&thread);
  controll.add(&thread2);
  controll.add(&thread3);
  controll.add(&thread4);
  
 timer1Id = timer.setInterval(1000*60*60, pump1Cycle); // 5초에 한번 notify 함수 호출
 timer2Id = timer.setInterval(1000*60*60, pump2Cycle); // 5초에 한번 notify 함수 호출
 timer3Id = timer.setInterval(1000*60*60, pump3Cycle); // 5초에 한번 notify 함수 호출
  
            
//  setTime(t->tm_hour,t->tm_min,t->tm_sec,t->tm_mday,t->tm_mon+1,t->tm_year+1900);


  settingH1=9;
  settingM1=0;

  settingH2=22;
  settingM2=0;

  settingH3=9;
  settingM3=0;

  settingH4=22;
  settingM4=0;
}

void loop() {
controll.run();
}
void webScoketLoop(){
  webSocket.loop();
}
void actionCycle(){
  timer.run();
}

void setTiming(){
  time_t now = time(nullptr);
  struct tm *t;
  t = localtime(&now);
  //sprintf(curTime,"%04d-%02d-%02d %02d:%02d:%02d", t->tm_year+1900, t->tm_mon+1, t->tm_mday, t->tm_hour, t->tm_min, t->tm_sec);
  if(t->tm_hour == settingH1 & t->tm_min== settingM1 & t->tm_sec==0 ){
    serialCommunication("ledOn");
    Serial.println("led켬");
  }
   if(t->tm_hour == settingH2 & t->tm_min== settingM2 & t->tm_sec==0 ){
    serialCommunication("ledOff");
    Serial.println("led 끔");
  }
}
void pump1Cycle(){
     serialCommunication("pump1On");
}
void pump2Cycle(){
     serialCommunication("pump2On");
}
void pump3Cycle(){
     serialCommunication("pump3On");
}


//-------------------------------------------------------------------------------------------------//
//void settingInterrupt(void (*anyMethod)(String), int interval, String action) { 
//  long previousMillis = 0;
//  unsigned long currentMillis = millis();
//  interval=intervar*1000;
//if( currentMillis - previousMillis > interval ) { 
//  previousMillis = currentMillis;
//  anyMethod(action);
//  }
//}
//-------------------------------------------------------------------------------------------------//

void transmitData(){
  Wire.requestFrom(1,256);
  serialCommunication("sensing");
  String sensingData=makeJson();
  httpPost(sensingData);
}

//-------------------------------------------------------------------------------------------------//
String makeJson(){
  String sensingData;
  StaticJsonDocument<400> doc;
  doc["device"] = "sm01";
  doc["magneticValue"] = magneticValue;
  doc["tempC"] = tempC;
  doc["humid"] = humid;
  doc["firstdistance"] = firstdistance;
  doc["secondDistance"] = secondDistance;
  doc["thirdDistance"] = thirdDistance;
  doc["illuminanceValue"] = illuminanceValue;
  doc["ip"] = WiFi.localIP();
    
  serializeJson(doc, sensingData); 
  return sensingData;
}

void serialCommunication(String action){
  
   arduSerial.print(action);
   arduSerial.print(";");
   delay(100); 
   

   if(action.equals("sensing")){
    if (arduSerial.available() > 0) {    // 시리얼 버퍼에 데이터가 있으면
    while(arduSerial.available() > 0){
    String inString="";
    inString = arduSerial.readStringUntil(';');
    delay(10); 
    
    if(inString.startsWith("temp")){
      inString.replace("temp","");
      tempC=inString;
    }
    if(inString.startsWith("hum")){
      inString.replace("hum","");
      humid=inString;
    }
    if(inString.startsWith("mag")){
      inString.replace("mag","");
      magneticValue=inString;
    }
    if(inString.startsWith("1st")){
      inString.replace("1st","");
      firstdistance=inString;
    }
    if(inString.startsWith("2nd")){
      inString.replace("2nd","");
      secondDistance=inString;
    }
    if(inString.startsWith("3rd")){
      inString.replace("3rd","");
      thirdDistance=inString;
    }
    if(inString.startsWith("illu")){
      inString.replace("illu","");
      illuminanceValue=inString;
    }
  }
}
   }
   
}

//-------------------------------------------------------------------------------------------------//
void httpPost(String httpRequest){
  //  wait for WiFi connection
  if ((WiFi.status() == WL_CONNECTED)) {
    WiFiClient client;
    HTTPClient http;
    Serial.print("[HTTP] begin...\n");
    // configure traged server and url
    http.begin(client, "http://" SERVER_IP "/arduino/"); //HTTP
    http.addHeader("Content-Type", "application/json");

    Serial.print("[HTTP] POST...\n");

    int httpCode = http.POST(httpRequest);


    // httpCode will be negative on error
    if (httpCode > 0) {
      // HTTP header has been send and Server response header has been handled
      Serial.printf("[HTTP] POST... code: %d\n", httpCode);
    
      // file found at server
      if (httpCode == HTTP_CODE_OK) {
        const String& payload = http.getString();
        Serial.println("received payload:\n<<");
        Serial.println(payload);
        Serial.println(">>");
      }
    } else {
      Serial.printf("[HTTP] POST... failed, error: %s\n", http.errorToString(httpCode).c_str());
    }

    http.end();
  }
}

//-------------------------------------------------------------------------------------------------//
//Callback function(웹소켓 메세지를 수신하면 실행)
void webSocketEvent(uint8_t num,
                     WStype_t type,
                     uint8_t * payload,
                     size_t length) {
   //웹소켓이벤트의 타입
   switch(type) {
     //클라이언트 연결 해제시
     case WStype_DISCONNECTED:
       Serial.printf("[%u] Disconnected!\n", num);
       break;
       //클라이언트 연결시
     case WStype_CONNECTED:
       {
         IPAddress ip = webSocket.remoteIP(num);
         Serial.printf("[%u] Connected!\n", num);
         Serial.println(ip.toString());
       }
       break;
      
     //클라이언트에게 응답을 보낸다.
    case WStype_TEXT:
       {
         Serial.printf("[%u] Text: %s\n", num, payload);
         //data에 따라 LED를 제어
         String text = String((char *) &payload[0]);
         if(text=="ledOn"){
            serialCommunication("ledOn");
         } 
         if(text=="ledOff"){
           serialCommunication("ledOff");
         }
         if(text=="showTime"){
            time_t now = time(nullptr);
            struct tm *t;
            t = localtime(&now);
            sprintf(curTime,"%04d-%02d-%02d %02d:%02d:%02d", t->tm_year+1900, t->tm_mon+1, t->tm_mday, t->tm_hour, t->tm_min, t->tm_sec);
            Serial.println(curTime);
         } 
         if(text=="fanOn"){
           serialCommunication("fanOn");
         }
         if(text=="fanOff"){
           serialCommunication("fanOff");
         }
         if(text=="pump1On"){
           serialCommunication("pump1On");
         }
         if(text=="pump1Off"){
           serialCommunication("pump1Off");
         }
         if(text=="pump2On"){
           serialCommunication("pump2On");
         }
         if(text=="pump2Off"){
           serialCommunication("pump2Off");
         }
         if(text=="pump3On"){
           serialCommunication("pump3On");
         }
         if(text=="pump3Off"){
           serialCommunication("pump3Off");
         }
        
         if(text=="doorOpen"){
           serialCommunication("doorOpen");
         }
         if(text=="doorClose"){
          Serial.println(settingH1);
           Serial.println(settingM1);
           Serial.println(settingH2);
           Serial.println(settingM2);
           serialCommunication("doorClose");
           
         }
         if(text.indexOf("changeLedSetting")>=0){
             text.replace("changeLedSetting/","");
             int h1=text.substring(0,2).toInt();
             int m1=text.substring(3,5).toInt();

             int h2=text.substring(6,8).toInt();
             int m2=text.substring(9,11).toInt();
             
             settingH1 =h1;
             settingM1 =m1;

             settingH2 =h2;
             settingM2 =m2;
             Serial.println(settingH1);
             Serial.println(settingM1);
             Serial.println(settingH2);
             Serial.println(settingM2);
         }
         if(text.indexOf("pumpSetting1")>=0){
            text.replace("pumpSetting1/","");
            int inter=text.toInt()*1000;
            Serial.println(inter);
            timer.deleteTimer(timer1Id);
            timer1Id=timer.setInterval(inter, pump1Cycle);
            
           }
           if(text.indexOf("pumpSetting2")>=0){
            text.replace("pumpSetting2/","");
            int inter=text.toInt()*1000;
            Serial.println(inter);
            timer.deleteTimer(timer2Id);
            timer2Id=timer.setInterval(inter, pump2Cycle);
           }
           if(text.indexOf("pumpSetting3")>=0){
            text.replace("pumpSetting3/","");
            int inter=text.toInt()*1000;
            timer.deleteTimer(timer3Id);
            timer3Id=timer.setInterval(inter, pump3Cycle);
           }
         webSocket.sendTXT(num, "변경 완료");
       }
       break;
      
     //기타 필요에 따라
     case WStype_BIN:
     case WStype_ERROR:
     case WStype_FRAGMENT_TEXT_START:
     case WStype_FRAGMENT_BIN_START:
     case WStype_FRAGMENT:
     case WStype_FRAGMENT_FIN:
     default:
       break;
   }
}
