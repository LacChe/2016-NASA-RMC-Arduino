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
int fuzziness = 5; // # larger means less accuracy in finding center

unsigned long pulse_width; // # used by lidar
int index1 = 0;
int index2 = 0;
unsigned long dist1[30]; // # stored previous data
unsigned long dist2[30]; // target 2
unsigned long margin = 100; // accepted mm difference between 

int scan = 0;
int scanCount = 1; // # how many scans to do before turning servo by 1;
int range = 45; // # servo moves between 0 and range degrees

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
        for (j=0; j<blocks; j++){
            String color = "null";
            switch (pixy.blocks[j].signature){
                case 1:
                    color = "Red";
                    break;
                case 2:
                    color = "Blue";
                    break;
                case 3:
                    color = "Green";
                    break;
                case 4:
                    color = "Green";
                    break;
                case 5:
                    color = "Red";
                    break;
                case 6:
                    color = "Red";
                    break;
                case 7:
                    color = "Red";
                    break;
            }
            int x = pixy.blocks[j].x;
            int y = pixy.blocks[j].y;
            int w = pixy.blocks[j].width;
            int h = pixy.blocks[j].height;
        
            // String xStr = "    x:  " + String(x) + "\n";
            // String yStr = "    y:  " + String(y) + "\n";
            // String wStr = "    w:  " + String(w) + "\n";
            // String hStr = "    h:  " + String(h) + "\n";
            
            if(color == "Red" || color == "Green"){ // change to target color
                if (x >= 160 - fuzziness && x <=  160 + fuzziness) {
                    
                     // get average
                      unsigned long a = pulseIn(3, HIGH);
                      unsigned long b = pulseIn(3, HIGH);
                      unsigned long c = pulseIn(3, HIGH);
                      unsigned long d = pulseIn(3, HIGH);
                      unsigned long e = pulseIn(3, HIGH);

                      pulse_width = (a + b + c + d + e) / 5;

                     // # do error calculation
                     /*
                      if(color == "Red"){
                        dist1[index] = pulse_width;
                        index1 += 1;
                      }
                      if(color == "Green"){
                        dist2[index] = pulse_width;
                        index2 += 1;
                      }
                     */
                      
                     // # debug stuff:
                     // pulse_width = pulse_width / 10; // 10usec = 1 cm of distance for LIDAR-Lite
                      Serial.print(color);
                      Serial.print(" at: ");
                      Serial.print(pulse_width); // # print distance
                      Serial.print("mm");
                      if (left) Serial.println(" left");
                      if (!left) Serial.println(" right");
                      // delay(200); // # stopping here to show that center distance was detected
                }
            }
       }
       scan += 1;
       if(scan == scanCount){
            scan = 0;
            myservo.write(pos);
            if(pos == 0 || pos == range) left = !left;
            if(left) pos+=1;
            if(!left) pos-=1;
       }
    delay(20); // # lower than 20 doesn't leave enough time for scanning before moving servo
}

