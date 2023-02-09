#include <ESP8266WiFi.h>
#include <ESPAsyncWebServer.h>
#include "ArduinoJson.h"
#include "AsyncJson.h"
#include <Servo.h>

AsyncWebServer server(80);

boolean set_ssid = false;
const char *ssid = "hand";
const char *password = "Password";
String fn1 = "open";
Servo servoFin1;
int pos = 0; // 700 - 2300 for non-standard servos 
int resa = 0;

unsigned int localPort = 8888;
IPAddress    apIP(192, 168, 4, 1);
IPAddress    broadcastIP(255, 255, 255, 255);
struct ip_addr *IPaddress;
IPAddress address;

void notFound(AsyncWebServerRequest *request)
{
  request->send(404, "application/json", "{\"message\":\"Not found\"}");
  //request->send(404, "text/plain", "Page Not found");
}

void setup()
{
  // servo Initialization
  servoFin1.attach(5); // D1 on NodeMCU ESP8266
  
  // wifi tgings
  Serial.begin(115200);
  Serial.println();
  Serial.print("Configuring access point...");
  WiFi.mode(WIFI_AP);
  WiFi.softAPConfig(apIP, apIP, IPAddress(255, 255, 255, 0));
  WiFi.softAP(ssid, password);
  IPAddress myIP = WiFi.softAPIP();
  Serial.print("AP IP address: ");
  Serial.println(myIP);
  Serial.print(" MAC Address is:");
  Serial.println (WiFi.softAPmacAddress());

  // landing page
  server.on("/", HTTP_GET, [](AsyncWebServerRequest *request) {
    request->send(200, "application/json", "{\"message\":\"Welcome\"}");
  });

  // JSON GET: http://192.168.4.1/get-finger?fn1=open&fn2=close
  server.on("/get-finger", HTTP_GET, [](AsyncWebServerRequest *request) {
    if (request->hasParam("fn1") && request->hasParam("fn2"))
    {
      fn1 = request->getParam("fn1")->value();
    }
    request->send(200, "application/json", "{\"pose\":\"changed\"}");
  });

  server.onNotFound(notFound);
  server.begin();
}

void loop()
{
  Serial.println(fn1);
  
  if (fn1 == "close"){
    slowMove(servoFin1, 10, true);
  } else if (fn1 == "open") {
    slowMove(servoFin1, 10, false);
  } else if (fn1 == "try"){
    slowMove(servoFin1, 5, true);
  } else {
      Serial.println("else");
  }
}


void slowMove(Servo myservo,int j, bool toClose){
  if (toClose) {
  for (pos = 2100; pos >= 700; pos -= j) {
    myservo.writeMicroseconds(pos);
    delay(5);
  }
  } else {
  for (pos = 700; pos <= 2100; pos += j) {
    myservo.writeMicroseconds(pos);
    delay(5);
  }
  }
  fn1 = "standby"; // God knows how many lines of code I deleted to find this...
}
