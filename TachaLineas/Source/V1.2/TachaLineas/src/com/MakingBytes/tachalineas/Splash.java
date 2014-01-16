package com.MakingBytes.tachalineas;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class Splash extends Activity {

  private long Retraso = 3000; // tiempo del splash en milisegundos

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.splash);

    TimerTask task = new TimerTask() { //definimos a Task
      @Override
      public void run() {
    	  gotoMenuPrincipal();
      }
    };

    Timer timer = new Timer();
    timer.schedule(task, Retraso);//Pasado el tiempo, lanza a Task.
  }
	private void gotoMenuPrincipal() {	//La función gotoMenuPrincipal lanzará la activity del menú
		Intent i = new Intent(this, MenuPrincipal.class);
		startActivity(i);
		finish();
	}
}