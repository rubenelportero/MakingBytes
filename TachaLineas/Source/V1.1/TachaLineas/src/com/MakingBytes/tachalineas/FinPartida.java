package com.MakingBytes.tachalineas;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FinPartida extends Activity {
	long MaxPunt=40000000;
	long Score;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finpartida);
		 EasyTracker.getInstance(this).activityStart(this);
		 int clicks = getIntent().getIntExtra("clicks",0);
		 long contaseg = getIntent().getLongExtra("seg",0);
		 final int nivel = getIntent().getIntExtra("nivel", 0);
		 final TextView MuestraScore1 = (TextView) findViewById(R.id.score1);
		 final TextView MuestraScore2 = (TextView) findViewById(R.id.score2);
		 final TextView MuestraScore3 = (TextView) findViewById(R.id.score3);
		 final TextView MuestraScore4 = (TextView) findViewById(R.id.score4);
		 Score = ((MaxPunt/contaseg)/clicks)*nivel;
		 if (nivel==1){
			 MuestraScore1.setText("Nivel: Fácil");
		 } else if (nivel==2){
			 MuestraScore1.setText("Nivel: Medio");
		 } else if (nivel==3){
			 MuestraScore1.setText("Nivel: Difícil");
		 } else {
			 MuestraScore1.setText("Nivel: Invencible");
		 } 
		 
		 MuestraScore2.setText("Tiempo: " + String.valueOf(contaseg) + "ms");
		 MuestraScore3.setText("Turnos: " + String.valueOf(clicks));
		 MuestraScore4.setText("Score: " + String.valueOf(Score));
		 Button boton = (Button) findViewById(R.id.compartir);
		 boton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					analytics();
					Intent sendIntent = new Intent();
					sendIntent.setAction(Intent.ACTION_SEND);
					if (nivel==1){
						final String compartir = "He ganado en el nivel fácil de #tachalineas con una puntuación de: " + String.valueOf(Score) + " puntos. https://play.google.com/store/apps/details?id=com.MakingBytes.tachalineas";
						sendIntent.putExtra(Intent.EXTRA_TEXT, compartir);
					} else if (nivel==2){
						final String compartir = "He ganado en el nivel medio de #tachalineas con una puntuación de: " + String.valueOf(Score) + " puntos. https://play.google.com/store/apps/details?id=com.MakingBytes.tachalineas";
						sendIntent.putExtra(Intent.EXTRA_TEXT, compartir);
					} else if (nivel==3){
						final String compartir = "He ganado en el nivel difícil de #tachalineas con una puntuación de: " + String.valueOf(Score) + " puntos. https://play.google.com/store/apps/details?id=com.MakingBytes.tachalineas";
						sendIntent.putExtra(Intent.EXTRA_TEXT, compartir);
					} else {
						final String compartir = "He ganado en el nivel invencible de #tachalineas con una puntuación de: " + String.valueOf(Score) + " puntos. https://play.google.com/store/apps/details?id=com.MakingBytes.tachalineas";
						sendIntent.putExtra(Intent.EXTRA_TEXT, compartir);
					}
					sendIntent.setType("text/plain");
					startActivity(sendIntent);
				}
			});
		}
	private void analytics(){
		EasyTracker easyTracker = EasyTracker.getInstance(this);
		easyTracker.send(MapBuilder
			      .createEvent("Accion",     
			                 "Compartir", 
			                 "CompartirVictoria",  
			                 null)           
				      .build()
				  );
	}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	// Con esto detectamos el botón de atrás
            EasyTracker.getInstance(this).activityStop(this);
    		Intent i = new Intent(this, MenuPrincipal.class);
    		startActivity(i);
    		this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}