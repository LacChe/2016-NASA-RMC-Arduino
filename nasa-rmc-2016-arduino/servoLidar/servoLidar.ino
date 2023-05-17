// This sketch uses a servo and a LIDAR-Lite in the
// PWM mode
// The Pixy and the LIDAR-lite Module are mounted on a servo
// motor which sweeps +/- 180 degrees

#include <SPI.h>
#include <Pixy.h>
#include <Servo.h>

unsigned long pulse_width;
Servo myservo;
Pixy pixy;

int pos = 0;         // Position of the servo (degress, [0, 180])
int distance = 0;    // Distance measured

void setup()
{
  // Serial output
  Serial.begin(115200);
  Serial.println("Starting... \n");
  pinMode(2, OUTPUT); // Set pin 2 as trigger pin
  pinMode(3, INPUT); // Set pin 3 as monitor pin
  pinMode(8, OUTPUT);
  pinMode(7, OUTPUT);
  pinMode(6, OUTPUT);
  pinMode(9, OUTPUT);
  digitalWrite(2, LOW); // Set trigger LOW for continuous read
  // Servo control
  myservo.attach(9);
  pixy.init();
}

int q = 0;
int x = 0;
int y = 0;
int xValueLower = 0;
int xValueUpper = 0;
int yValueLower = 0;
int yValueUpper = 0;

void loop()
{
  static int i = 0;
  int j;
  uint16_t blocks;
  char buff[32];
  pos = 0;
  
  for(pos = 0; pos <= 180; pos += 2)
  {
    myservo.write(pos);
    blocks = pixy.getBlocks();
    delay(10);
    if (blocks)
      {
        i++;
        digitalWrite(6, LOW);
        digitalWrite(8, LOW);
        digitalWrite(7, LOW);
        if (i%50 == 0)
          for (j=0; j<blocks; j++)
            {
              //pixy.blocks(j).print();
              q = pixy.blocks[j].signature;
              x = pixy.blocks[j].x;
              y = pixy.blocks[j].y;
              Serial.print(x); 
              Serial.print("  ");
              Serial.print(y);
              Serial.print("  ");
              Serial.print(q);
              //if (x > 135 && x < 185)
              //  {
              //Serial.println("FIRE THE LASER!!!");
              pulse_width = pulseIn(3, HIGH); // Count how long the pulse is high in microseconds
                if(pulse_width != 0)
                  { 
                    // If we get a reading that isn't zero, let's print it
                    pulse_width = pulse_width/10; // 10usec = 1 cm of distance for LIDAR-Lite
                    Serial.print("  Angle & Distance:  ");
                    Serial.print(pos);
                    Serial.print("   ");
                    Serial.println(pulse_width);
                  }
                  //delay(50);
                  //Found target
                  digitalWrite(6, HIGH);
                  delay(100);
               // }
              if (q == 1)
                {
                  digitalWrite(8, HIGH);
                  delay(100);
                }
              if (q == 2)
                {
                  digitalWrite(7, HIGH);
                  delay(100);
                }
             }
        }
  }
      
  // Sweep the other way            
   
  for(pos = 180; pos>=0; pos-=2)
  {
    myservo.write(pos);
    delay(10);
    blocks = pixy.getBlocks();
    if (blocks)
      {
        i++;
        digitalWrite(6, LOW);
        digitalWrite(8, LOW);
        digitalWrite(7, LOW);
        if (i%50 == 0)
          for (j=0; j<blocks; j++)
            {
              //pixy.blocks(j).print();
              q = pixy.blocks[j].signature;
              x = pixy.blocks[j].x;
              y = pixy.blocks[j].y;
              Serial.print(x); 
              Serial.print("  ");
              Serial.print(y);
              Serial.print("  ");
              Serial.print(q);
              //if (x > 135 && x < 185)
              //  {
                  Serial.print("  Angle and Distance:  ");
                  pulse_width = pulseIn(3, HIGH); // Count how long the pulse is high in microseconds
                  if(pulse_width != 0)
                    { 
                      // If we get a reading that isn't zero, let's print it
                      pulse_width = pulse_width/10; // 10usec = 1 cm of distance for LIDAR-Lite
                      Serial.print(pos);
                      Serial.print("   ");
                      Serial.println(pulse_width);
                    }
                  // Found target  
                  digitalWrite(6, HIGH);
                  delay(100);
              //  }
              if (q == 1)
                {
                  digitalWrite(8, HIGH);
                  delay(50);
                }
              if (q == 2)
                {
                  digitalWrite(7, HIGH);
                  delay(50);
                }
             }
        }
  }
  
}

