package com.MakingBytes.tachalineas;
import java.util.Timer;
import java.util.TimerTask;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.example.games.basegameutils.BaseGameActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FinPartida extends BaseGameActivity {
	private AdView adView;
	public int z=0;
	long MaxPunt=40000000;
	long Score;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finpartida);
		 EasyTracker.getInstance(this).activityStart(this);
		 final String publicidad = getString(R.string.IDpublicidad);
		 adView = new AdView(this);
		 adView.setAdUnitId(publicidad);
		 adView.setAdSize(AdSize.BANNER);
		 LinearLayout layout = (LinearLayout)findViewById(R.id.fin_partida);
		 layout.addView(adView);
		 AdRequest adRequest = new AdRequest.Builder().build();
		 adView.loadAd(adRequest);
		 logroganar();
		 int clicks = getIntent().getIntExtra("clicks",0);
		 long contaseg = getIntent().getLongExtra("seg",0);
		 final int nivel = getIntent().getIntExtra("nivel", 0);
		 final TextView MuestraScore1 = (TextView) findViewById(R.id.score1);
		 final TextView MuestraScore2 = (TextView) findViewById(R.id.score2);
		 final TextView MuestraScore3 = (TextView) findViewById(R.id.score3);
		 final TextView MuestraScore4 = (TextView) findViewById(R.id.score4);
		 Score = ((MaxPunt/contaseg)/clicks)*nivel;
		 logronivel(nivel);
		 marcador(nivel,Score);
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
					logrocompartir();
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
    		this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void logrocompartir(){
       final String logro_compartir = getString(R.string.logro_compartir_5);
       getGamesClient().incrementAchievement(logro_compartir,1);
    }
    private void logroganar(){
        TimerTask task = new TimerTask() { //definimos a Task
            @Override
            public void run() {
        		if (getGamesClient().isConnected()) {
        			 final String logro_aburrido = getString(R.string.logro_aburrido);
				     getGamesClient().incrementAchievement(logro_aburrido,1);
        		}
            }
          };

          Timer timer = new Timer();
          timer.schedule(task, 1000);//Pasado el tiempo, lanza a Task.
    }
    private void logronivel(final int i){
        TimerTask task = new TimerTask() { //definimos a Task
            @Override
            public void run() {
        		if (getGamesClient().isConnected()) {
        			if(i==1){
        			final String logro_nivel = getString(R.string.logro_facil_10);
        			getGamesClient().incrementAchievement(logro_nivel,1);
        			} else if(i==2){
        			final String logro_nivel = getString(R.string.logro_medio_10);
        			getGamesClient().incrementAchievement(logro_nivel,1);
        			} else {
        			final String logro_nivel = getString(R.string.logro_dificil_10);
        			getGamesClient().incrementAchievement(logro_nivel,1);
        			} 
        		} 
            }
          };

          Timer timer = new Timer();
          timer.schedule(task, 1000);//Pasado el tiempo, lanza a Task.
    }
	private void marcador(final int i, final long score){
        TimerTask task = new TimerTask() { //definimos a Task
            @Override
            public void run() {
        		if (getGamesClient().isConnected()) {
        			if(i==1){
        			final String LEADERBOARD_ID = getString(R.string.marcador_facil);
        			getGamesClient().submitScore(LEADERBOARD_ID, score);
        			} else if(i==2){
        			final String LEADERBOARD_ID = getString(R.string.marcador_medio);
        			getGamesClient().submitScore(LEADERBOARD_ID, score);
        			} else {
        			final String LEADERBOARD_ID = getString(R.string.marcador_dificil);
        			getGamesClient().submitScore(LEADERBOARD_ID, score);
        			} 
        		}
            }
          };

          Timer timer = new Timer();
          timer.schedule(task, 1000);//Pasado el tiempo, lanza a Task.

	}
	private void Compartido(){
		Toast toast5 = Toast.makeText(getApplicationContext(), getString(R.string.comparte_score), Toast.LENGTH_LONG);
		toast5.show();
	}
    @Override
	public void onSignInFailed() {
	}
	@Override
	public void onSignInSucceeded() {
		if(z==0){
			Compartido();
			z=1;
		}
	}
}