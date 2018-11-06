//TimePanel Responsible for displaying time elapsed since game has started;

import java.util.TimerTask;
import javax.swing.JButton;

public class TimePanel extends JButton{
  int seconds = 0;
  int minutes = 0;
  int hours = 0;
  long secondsTotal = 0;
  java.util.Timer updateTimer= new java.util.Timer(); //Creating timer
  
  //constructor used for loaded game
  TimePanel(long resumeSeconds)
  {
    secondsTotal = resumeSeconds;   //before we do anything to our given "resumeSeconds" value we need to save it
    if(resumeSeconds>=3600) 
    {
      //if the game play is longer than an hour
      hours = (int)resumeSeconds/3600;   //extract number of hours played
      resumeSeconds = resumeSeconds - (3600 * hours); // subtract number of seconds for however many hours have been played
    }
    if(resumeSeconds>= 60) { //if the game play hasn't gone longer than an hour
      minutes = (int)resumeSeconds/60; //set number of minutes played;
      resumeSeconds = resumeSeconds - (60 * minutes); // subtract number of seconds for however many minutes have been played
    }
    if(resumeSeconds<=59){ // if the game hasn't been played longer than  minute
      seconds = (int)resumeSeconds; 
      setText(Integer.toString(hours) + ":" + Integer.toString(minutes) + ":" + Integer.toString(seconds));
    }

    setText(Integer.toString(hours) + ":" + Integer.toString(minutes) + ":" + Integer.toString(seconds));
    startTimer();
  }
  
  //Constructor used for new game
  TimePanel(){
    setText(Integer.toString(hours) + ":" + Integer.toString(minutes) + ":" + Integer.toString(seconds));
  }
  
  //function used for updating 
  void update() {
    seconds++;
    secondsTotal++;
    if(seconds>59) {
      seconds = 0;
      minutes++;
      if(minutes>59) {
        minutes = 0;
        hours++;
      }
      else {
        setText(Integer.toString(hours) + ":" + Integer.toString(minutes) + ":" + Integer.toString(seconds));
      }
    }
    else{
      setText(Integer.toString(hours) + ":" + Integer.toString(minutes) + ":" + Integer.toString(seconds));
    }
    //System.out.println("Update!");
  }
  
  long getTime() {
    return secondsTotal;
  }
  
  void pause(){
    updateTimer.cancel();
  }
  
  // Overloaded resume, takes long parameters
  void resume(long newTime)
  {
    secondsTotal = newTime;   //before we do anything to our given "resumeSeconds" value we need to save it
    if(newTime>=3600) 
    {
      //if the game play is longer than an hour
      hours = (int)newTime/3600;   //extract number of hours played
      newTime = newTime - (3600 * hours); // subtract number of seconds for however many hours have been played
    }
    if(newTime>= 60) { //if the game play hasn't gone longer than an hour
      minutes = (int)newTime/60; //set number of minutes played;
      newTime = newTime - (60 * minutes); // subtract number of seconds for however many minutes have been played
    }
    if(newTime<=59){ // if the game hasn't been played longer than  minute
      seconds = (int)newTime; 
      setText(Integer.toString(hours) + ":" + Integer.toString(minutes) + ":" + Integer.toString(seconds));
    }

    setText(Integer.toString(hours) + ":" + Integer.toString(minutes) + ":" + Integer.toString(seconds));
    startTimer();
  }
  
  void resume() 
  {
    TimePanel timer = new TimePanel(secondsTotal);
  }
  
  void startTimer() {
    updateTimer.scheduleAtFixedRate(new TimerTask() {   // schedule timer to perform update
      @Override
      public void run() {
        update();
      }
      }, 0, 1000);
  }
  
  void resetTimer() {
    if(Main.played==false)
    pause();
    seconds = 0;
    minutes = 0;
    hours = 0;
    secondsTotal = 0;
    setText(Integer.toString(hours) + ":" + Integer.toString(minutes) + ":" + Integer.toString(seconds));
    
    /*else {
      TimePanel timer = new TimePanel(placeholder);
    }*/
  }
  
  /* Instructions on how to use timer. 
   * 
   * The moment the save button is clicked, the elapsed time will be saved in a 
   * variable named "savedTime" which is located in GameWindow, this variable can now be 
   * used to write to a save file.
   * 
   * 
   * In a previously played game, timer will start ticking as soon as the game has been loaded. 
   * 
   * TimePanel has two constructors, the constructor with no parameters is used for a brand new game. 
   * The constructor with the parameter of type "long" is used for previously played games. The constructor with 
   * the "type" long parameter needs a "long" value to reset the timer to what it was when the user decided to save.
   * 
   * resetTimer function will be called whenever reset button is pressed.
   */
  

}