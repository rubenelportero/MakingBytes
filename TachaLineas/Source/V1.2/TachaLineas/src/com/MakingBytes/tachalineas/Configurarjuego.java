package com.MakingBytes.tachalineas;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Configurarjuego extends Activity {
	 private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configurarjuego);
		 EasyTracker.getInstance(this).activityStart(this);
		    final String publicidad = getString(R.string.IDpublicidad);
		    adView = new AdView(this);
		    adView.setAdUnitId(publicidad);
		    adView.setAdSize(AdSize.BANNER);
		    LinearLayout layout = (LinearLayout)findViewById(R.id.configurar_juego);
		    layout.addView(adView);
		    AdRequest adRequest = new AdRequest.Builder().build();
		    adView.loadAd(adRequest);
		final TextView dificulty = (TextView)findViewById(R.id.mododificultad); //leemos el textView
	    final String facil = getString(R.string.Facil); //más variables
	    final String medio = getString(R.string.Medio); //más variables
	    final String dificil = getString(R.string.Dificil); //más variables
	    final String invencible = getString(R.string.Invencible); //más variables
	    final SeekBar barradificultad=(SeekBar)findViewById(R.id.seekBar1);
		barradificultad.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				int niveldificultad=barradificultad.getProgress();
            	if(niveldificultad== 0){
            		dificulty.setText(facil);
            	} else if (niveldificultad==1){
            		dificulty.setText(medio);
            	} else if (niveldificultad==2){
            		dificulty.setText(dificil);
            	}else if (niveldificultad==3){
            		dificulty.setText(invencible);
            	}
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});
    	
	    Button boton = (Button) findViewById(R.id.button1); //definimos el botón
        boton.setOnClickListener(new View.OnClickListener() { //leemos cuando el botón es pulsado
        	public void onClick(View v) {
            	int niveldificultad = barradificultad.getProgress();
            	if(niveldificultad== 0){
            		gotofacil();
            	} else if (niveldificultad==1){
            		gotomedio();
            	} else if (niveldificultad==2){
            		gotodificil();
            	}else if (niveldificultad==3){
            		gotoimposible();
            	}
            }
        });
    }
	@Override
	  public void onPause() {
	    adView.pause();
	    super.onPause();
	  }

	  @Override
	  public void onResume() {
	    super.onResume();
	    adView.resume();
	  }

	  @Override
	  public void onDestroy() {
	    adView.destroy();
	    super.onDestroy();
	  }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	// Con esto detectamos el botón de atrás
            EasyTracker.getInstance(this).activityStop(this);
    		Intent i = new Intent(this, MenuPrincipal.class);
    		startActivity(i);
    		finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
	private void gotofacil() {
    	EasyTracker easyTracker = EasyTracker.getInstance(this);
		easyTracker.send(MapBuilder
			      .createEvent("Accion",     
			                 "PartidaIniciada", 
			                 "Facil",  
			                 null)           
				      .build()
				  );
		Intent i = new Intent(this, Faciljuego.class);
		startActivity(i);
		finish();
	}
	private void gotomedio() {
    	EasyTracker easyTracker = EasyTracker.getInstance(this);
		easyTracker.send(MapBuilder
			      .createEvent("Accion",     
			                 "PartidaIniciada", 
			                 "Medio",  
			                 null)           
				      .build()
				  );
		Intent i = new Intent(this, MedioJuego.class);
		startActivity(i);
		finish();
	}
	private void gotodificil() {
    	EasyTracker easyTracker = EasyTracker.getInstance(this);
		easyTracker.send(MapBuilder
			      .createEvent("Accion",     
			                 "PartidaIniciada", 
			                 "Dificil",  
			                 null)           
				      .build()
				  );
		Intent i = new Intent(this, Dificiljuego.class);
		startActivity(i);
		finish();
	}
	private void gotoimposible() {
    	EasyTracker easyTracker = EasyTracker.getInstance(this);
		easyTracker.send(MapBuilder
			      .createEvent("Accion",     
			                 "PartidaIniciada", 
			                 "Invencible",  
			                 null)           
				      .build()
				  );
		Intent i = new Intent(this, ImposibleJuego.class);
		startActivity(i);
		finish();
	}
}