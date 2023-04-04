// lac

#include <Servo.h> 
#include <SPI.h>  
#include <Pixy.h>
#include <math.h>
#include <Wire.h>
#define SLAVE_ADDRESS 0x05

int X = 0;
int Y = 0;
int Xp = 0;
int Yp = 0;
double radianAlpha = 0;
boolean foundLeft = false, foundRight = false;

// stuff for sending to pi
int number, age, stat = 0;
byte x1, x2, y1, y2;

Servo myservo;
int pos = 0; // # position of servo
boolean leftTurn = false; // # is the servo going left

Pixy pixy;
int fuzziness = 5; // # larger means less accuracy in finding center

unsigned long right = 0; // # used by lidar
unsigned long left = 0;
double d = 157-6*2; // target distance

int range = 180; // # servo moves between 0 and range degrees

void setup() {
    Wire.begin(SLAVE_ADDRESS);
    Wire.onReceive(receiveData);
    Wire.onRequest(sendData);
    
    Serial.begin(9600);
    Serial.print("Starting...\n");
    
    myservo.attach(9);
    
    pixy.init(); // Start serial communications
    
    pinMode(2, OUTPUT); // Set pin 2 as trigger pin
    pinMode(3, INPUT); // Set pin 3 as monitor pin
    digitalWrite(2, LOW); // Set trigger LOW for continuous read
    
    Serial.print("Done.\n");
}

void loop() {
    int j;
    uint16_t blocks;
    char buf[32]; 
  
    blocks = pixy.getBlocks();
    for (j=0; j<blocks; j++){
       int x = pixy.blocks[j].x;
       int y = pixy.blocks[j].y;
       int w = pixy.blocks[j].width;
       int h = pixy.blocks[j].height;

       /** 
        *  get distances to targets
        *  green at sig 3
        *  red at sig 5
        */ 
        if(pixy.blocks[j].signature == 5){ // change to target color
            if (x >= 160 - fuzziness && x <=  160 + fuzziness) {
                Serial.println("red center"); 
                unsigned long a = pulseIn(3, HIGH);
                unsigned long b = pulseIn(3, HIGH);
                unsigned long c = pulseIn(3, HIGH);
                unsigned long d = pulseIn(3, HIGH);
                unsigned long e = pulseIn(3, HIGH);
                left = (a + b + c + d + e) / 5;
                left = left / 10;
                // leftTurn = false;
                if (foundRight == true && foundLeft == true){
                   //foundRight == false;
                }
                foundLeft = true;
            }
        }
        if(pixy.blocks[j].signature == 3){ // change to target sig
            if (x >= 160 - fuzziness && x <=  160 + fuzziness) {
                Serial.println("green center"); 
                unsigned long a = pulseIn(3, HIGH);
                unsigned long b = pulseIn(3, HIGH);
                unsigned long c = pulseIn(3, HIGH);
                unsigned long d = pulseIn(3, HIGH);
                unsigned long e = pulseIn(3, HIGH);
                right = (a + b + c + d + e) / 5;
                right = right / 10;
                // leftTurn = true;
                if (foundRight == true && foundLeft == true){
                   //foundLeft == false;
                }
                foundRight = true;
            }
        }
    }

    /** 
     *  move servo
     */
    if(pos == 0 || pos == range) leftTurn = !leftTurn;
    if(leftTurn) pos+=1;
    if(!leftTurn) pos-=1;
    myservo.write(pos);

    /** 
     *  display results
     */ 
    if (millis()%500 > 250){
        //Serial.print("left: ");
        //Serial.print(left);
        //Serial.print(" right: ");
        //Serial.print(right);
        if(foundLeft && foundRight && false){
            getY((double)(left),(double)(right));
            getX((double)(left));
            Serial.print("| X: "); 
            Serial.print(X); 
            Serial.print(" Y: "); 
            Serial.println(Y);
            foundLeft = foundRight = false;
        }
    }
    if (millis()%1000 == 0){
        age ++;
    }
    updateStatus();
    delay(20); // # lower than 20 doesn't leave enough time for scanning before moving servo
}

void updateStatus(){
    stat = 0;
    if(!foundLeft && !foundRight) stat = 3;
    else if(!foundLeft) stat = 1;
    else if(!foundRight) stat = 2;
    //Serial.print(foundLeft); 
    //Serial.print(" "); 
    //Serial.print(foundRight); 
    //Serial.print(" "); 
    //Serial.println(stat); 
}

void getY(double l, double r){
    double arccosine = (pow(l,2) + pow(d,2) - pow(r,2)) / (2 * l * d);
    double radianC = acos(arccosine);
    radianAlpha = M_PI / 2 - radianC;
    double yd = l * cos(radianAlpha);
    Y = (int) yd;
    y1 = Y >> 8;
    y2 = Y;
}

void getX(double l){
    double xd = l * sin(radianAlpha);
    X = 75 + (int) xd; // + 75 from edge of arena to left target center
    x1 = X >> 8;
    x2 = X;
}

void receiveData(int byteCount){
    while(Wire.available()){
        number = Wire.read();
        Serial.print("Received: ");
        Serial.print(number);
    }
}

void sendData(){
    if(number == 0){
        Wire.write(x1);
        Serial.print(" Sent: ");
        Serial.println(x1);
    }
    if(number == 1){
        Wire.write(x2);
        Serial.print(" Sent: ");
        Serial.println(x2);
    }
    if(number == 2){
        Wire.write(y1);
        Serial.print(" Sent: ");
        Serial.println(y1);
    }
    if(number == 3){
        Wire.write(y2);
        Serial.print(" Sent: ");
        Serial.println(y2);
    }
    if(number == 4){
        byte a = age;
        Wire.write(a);
        Serial.print(" Sent age: ");
        Serial.println(a);
    }
    if(number == 5){
        //update stat ?
        updateStatus();
        Wire.write(stat);
        Serial.print(" Sent stat: ");
        Serial.println(stat);
    }
    if (number == 6){
        if(left - right > 30) {
           Wire.write(1);
        } else if(right - left > 30) {
           Wire.write(2);  
        } else {
           Wire.write(0);
        }
    }
}
