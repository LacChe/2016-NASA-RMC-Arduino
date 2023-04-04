// Flashing LED'S with two different colors
// This program sketch will search for a target and indicate
// whether it centered. 
// Move the target in front of the of the Pixy sensor 
   
#include <SPI.h>  
#include <Pixy.h>

// This is the main Pixy object 
Pixy pixy;

void setup()
{
  Serial.begin(9600);
  Serial.println("Starting...\n");
  pinMode(8, OUTPUT);
  pinMode(7, OUTPUT);
  pinMode(6, OUTPUT);
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
  char buf[32]; 
  
  // grab blocks!
  blocks = pixy.getBlocks();
  
  // If there are detect blocks, print them!
  if (blocks)
  {
    i++;
    // do this (print) every 50 frames because printing every
    // frame would bog down the Arduino
    if (i%50==0)
    {
      //sprintf(buf, "Detected %d:\n", blocks);
      //Serial.println(buf);
      for (j=0; j<blocks; j++)
      {
        //sprintf(buf, "  block %d: ", j);
        //Serial.print(buf); 
        pixy.blocks[j].print();
        q = pixy.blocks[j].signature;
        x = pixy.blocks[j].x;
        y = pixy.blocks[j].y;
        Serial.print(x); 
        Serial.print("  ");
        Serial.print(y);
        Serial.print("  ");
        Serial.println(q);
        if ((x > 155 && x < 165) && (y > 95 && y < 105))
          {
             Serial.println("CENTERED!!!!!!");
             digitalWrite(6, HIGH);
          }
        if (q == 1)
          {
           // digitalWrite(7, LOW);
            digitalWrite(8, HIGH);
            delay(100);
          }
        if (q == 2)
          {
           // digitalWrite(7, LOW);
            digitalWrite(7, HIGH);
            delay(100);
          }
      }
      digitalWrite(8, LOW);
      digitalWrite(7, LOW);
    }
  }  
}

