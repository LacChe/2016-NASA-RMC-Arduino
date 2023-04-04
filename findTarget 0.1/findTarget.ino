//
//
//
//
//
//
// comments with a # in front are written by lac

#include <Servo.h> 
#include <SPI.h>  
#include <Pixy.h>

Servo myservo;
int pos = 0; // # position of servo
boolean left = false; // # is the servo going left

Pixy pixy;
int fuzziness = 2; // # larger means less accuracy in finding center

unsigned long pulse_width; // # used by lidar

int scan = 0;
int scanCount = 1; // # how many scans to do before turning servo by 1;


void setup() {
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
  
    // grab blocks!
    blocks = pixy.getBlocks();
    // If there are detect blocks, print them!
    if (true) {
        sprintf(buf, "Detected %d:\n", blocks);
        Serial.print(buf);
        for (j=0; j<blocks; j++){
            String color = "null";
            switch (pixy.blocks[j].signature){
                case 1:
                    color = "  Red\n";
                    break;
                case 2:
                    color = "  2 col XXX\n";
                    break;
                case 3:
                    color = "  3 col XXX\n";
                    break;
                case 4:
                    color = "  4 col XXX\n";
                    break;
                case 5:
                    color = "  Green\n";
                    break;
                case 6:
                    color = "  6 col XXX\n";
                    break;
                case 7:
                    color = "  7 col XXX\n";
                    break;
            }
            int x = pixy.blocks[j].x;
            int y = pixy.blocks[j].y;
            int w = pixy.blocks[j].width;
            int h = pixy.blocks[j].height;
        
            String xStr = "    x:  " + String(x) + "\n";
            String yStr = "    y:  " + String(y) + "\n";
            String wStr = "    w:  " + String(w) + "\n";
            String hStr = "    h:  " + String(h) + "\n";
            
            Serial.print(color); 
            if(color == "  Green\n" || color == "  Red\n"){
                // Serial.print(xStr);
                // Serial.print(yStr);
                // Serial.print(wStr);
                // Serial.print(hStr);
                
                pulse_width = pulseIn(3, HIGH); // Count how long the pulse is high in microseconds
       
                if (x >= 160 - fuzziness && x <=  160 + fuzziness) {
                    Serial.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!CENTERED");
                    Serial.println(color);
                    if(pulse_width != 0){ // If we get a reading that isn't zero, let's print it
                        pulse_width = pulse_width/10; // 10usec = 1 cm of distance for LIDAR-Lite
                        Serial.print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Distance: ");
                        Serial.print(pulse_width); // # print distance
                        Serial.println("cm");
                        delay(200); // # stopping here to show that center distance was detected
                    }
                }
                // # this function draws a kind of map that shows the position of the 
                // drawMap(String(pixy.blocks[j].signature), x, y, w, h);
            }
        }
    }
    scan += 1;
    if(scan == scanCount){
        scan = 0;
        myservo.write(pos);
        if(pos == 0 || pos == 40) left = !left;
        if(left) pos+=1;
        if(!left) pos-=1;
    }
    delay(100); // # too low doesn't leave enough time for scanning before moving servo
}  


void drawMap(String sig, int x, int y, int w, int h){
    // # divide by 10 so serial doesn't take too long to draw
    int maxX = 320/10;
    int maxY = 200/10;
    int maxW = 320/10;
    int maxH = 200/10;
    Serial.print("Map starts here:\n");
    for(int j = 0;j<maxY;j++){
        for(int i = 0;i<maxX;i++){
            if(x/10-w/20 < i && x/10+w/20 > i && y/10-h/20 < j && y/10+h/20 > j){
                Serial.print(sig);
                Serial.print(" ");
            }else{
                Serial.print(". ");
            }
        }
        Serial.print("\n");
    }
}

