import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class KeplerComplication extends PApplet {

	

float click = 0.0f; 
float dt = 0.001f;


public void setup() { 
  size(600, 600);
  background(60);
  smooth(); 
  frameRate(15);
} 
 
public void draw() { 
  background(60);
  PFont font;
    font = loadFont("BankGothic-Medium-24.vlw");
  textFont(font, 24);
  click = click + dt;
  String timestamp = str(24.f*click);
  int bg;
  colorMode(RGB,255,255,255);
  String lines[] = loadStrings("dataclock.txt");
  for (int i=0; i < lines.length; i++) {
    float[] sv = PApplet.parseFloat(split(lines[i], ' '));
    
    float koi=sv[0];
    float duration=sv[1]/24.f;
    float depth=sv[2];
    float t0=sv[3];
    float period=sv[4];
    float Rp=sv[5];
    float Teff=sv[6];

    int fv0 = PApplet.parseInt((log(depth)/12.f)*220);
    if(fv0 > 250) {
      fv0=250; 
      }
    int fv1 = PApplet.parseInt((Teff/1300.f)*200);
    if(fv1 > 250) {
      fv1=250; 
      }
    
    
    int nelapsed=PApplet.parseInt(click/period);
    int nwindow=PApplet.parseInt(t0/period);
    float fnow=(click-(nelapsed*period))/period;
    float fwin=(t0-(nwindow*period))/period;
    if((fnow > (fwin-(duration/2.f)/period)) && (fnow < (fwin+(duration/2.f)/period))) { 
      float fstr=fwin-(duration/2.f)/period;
      float fend=fwin+(duration/2.f)/period;
      float fracT=(fnow-fstr)/(fend-fstr);
      float x=period*20.f;
      float y=600-Rp*25.f;
      bg = color(fv0, fv1, sqrt(fv0+fv1));
      float value=alpha(bg);
      if(fracT < 0.10f){
        value=value*0.2f*10.f*fracT;
      }
      if((fracT > 0.10f ) && (fracT < 0.9f)){
      value=value*.2f;
      }
      if(fracT > 0.9f){
        value=value*0.2f*10.f*(1.f-fracT);
      }
      fill(bg, value);
      noStroke();
      ellipse(x,y,Rp*10,Rp*10);
      text(timestamp, 515, 590);
      text("day", 450, 590);
    }
  }
}

public void mousePressed() {
  noLoop();
}


  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "KeplerComplication" });
  }
}
