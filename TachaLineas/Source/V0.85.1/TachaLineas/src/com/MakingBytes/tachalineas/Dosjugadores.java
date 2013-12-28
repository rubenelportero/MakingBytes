package com.MakingBytes.tachalineas;

import java.util.Timer;
import java.util.TimerTask;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Dosjugadores extends Activity {
    private int array[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; //un array para controlar el estado de las lineas
    private int elturno = 1, contador=0,linea = 0, numimagen = 0, i, debug=0; //variables
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tablero);
		 EasyTracker.getInstance(this).activityStart(this);
		 EasyTracker easyTracker = EasyTracker.getInstance(this);
		 easyTracker.send(MapBuilder
				  .createAppView()
				  .set(Fields.SCREEN_NAME, "Dos Jugadores")
				  .build()
				);
	    final TextView turno = (TextView)findViewById(R.id.textView1); //leemos el textView
	    final String jugadoruno = getString(R.string.jugadoruno); //más variables
	    final String jugadordos = getString(R.string.jugadordos); //más variables
        Button boton = (Button) findViewById(R.id.button1); //definimos el botón
        boton.setOnClickListener(new View.OnClickListener() { //leemos cuando el botón es pulsado
            public void onClick(View v) {
            	for(i=0;i<15;i++){         //este code se encarga de que las lineas tachadas 
            		if(array[i]==1){       //se guarden en el array con un 2, o lo que viene a 
            			array[i]=2;        //ser igual, no se podrán des-tachar.
            			contador=1; //contador es 1 si se ha convertido alguna línea 
            		}
            	}
            	if (contador==1){  //si contador es 1, se cambia de turno
            		linea = 0;
            		if(elturno==1){
            			turno.setText(jugadordos);
            			elturno = 2;
            		} else {
            			turno.setText(jugadoruno);
            			elturno = 1;
            		}
            	} else showFallo(); //si contador no es 1, no se ha tachado nada 
            								//puesto que no se ha cambiado ninguna linea a 2.
            	contador = 0;       
        		comprobarganador(); //y aquí, tras empezar el nuevo turno, se comprueba si ha ganado alguien
            }
        });
    }
    public void onClick(View v) { //acción realizada al pulsar sobre un Imageview
    	EasyTracker easyTracker = EasyTracker.getInstance(this);
    	switch (v.getId()) {
			case R.id.imageView1: //1 fila //
				numimagen = 0;
				break;
			case R.id.imageView2: //2 fila //
				numimagen = 1;
				break;
			case R.id.ImageView01:
				numimagen = 2;
				break;
			case R.id.ImageView04: //3 fila //
				numimagen = 3;
				break;
			case R.id.ImageView02:
				numimagen = 4;
				break;
			case R.id.ImageView03:
				numimagen = 5;
				break;
			case R.id.ImageView08: //4 fila//
				numimagen = 6;
				break;
			case R.id.ImageView06:
				numimagen = 7;
				break;
			case R.id.ImageView05:
				numimagen = 8;
				break;
			case R.id.ImageView07:
				numimagen = 9;
				break;
			case R.id.ImageView13: //5 fila //
				numimagen = 10;
				break;
			case R.id.ImageView09:
				numimagen = 11;
				break;
			case R.id.ImageView10:
				numimagen = 12;
				break;
			case R.id.ImageView11:
				numimagen = 13;
				break;
			case R.id.ImageView12:
				numimagen = 14;
				break;
		}
		ImageView imagen = (ImageView) findViewById(v.getId()); //se lee la imagen pulsada
		if (array[numimagen]==0){
			array[numimagen] = 1;
			if(comprobar()==0){    //se comprueba que no se haya pulsado en vertical o diagonal
				array[numimagen] = 0;                         
			} else {
				easyTracker.send(MapBuilder 
				.createEvent("Accion",     
			                 "Lineas", 
			                 "linea tachada",  
			                 null)           
				      .build()
				  );
				imagen.setImageResource(R.drawable.linea2); //se cambia la imagen de la linea tachada
			}
		} else if (array[numimagen]==1) {  //código para des-tachar lineas
			array[numimagen] = 0;
			easyTracker.send(MapBuilder 
			.createEvent("Accion",     
		                 "Lineas", 
		                 "linea destachada",  
		                 null)           
			      .build()
			  );
			imagen.setImageResource(R.drawable.linea);
			linea = 0;
		}

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	// Con esto detectamos el botón de atrás
        	if (debug==0){
            Toast.makeText(getApplicationContext(), "No se puede salir sin terminar la partida",
                    Toast.LENGTH_SHORT).show();
            arreglarbug();
        	}
        	debug=1;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private int comprobar(){  //este codigo comprueba que no se haya tachado en vertical o diagonal
    	for(i=0;i<15;i++) {
    		if(array[i]==1){
    			if((linea==0 || linea==1) && i==0){
    				linea=1;
    			} else if ((linea==0 || linea==2) && i>0 && i<3){
    				linea=2;
    			} else if ((linea==0 || linea==3) && i>2 && i<6){
    				linea=3;
    			} else if ((linea==0 || linea==4) && i>5 && i<10){
    				linea=4;
    			} else if ((linea==0 || linea==5) && i>9){
    				linea=5;
    			} else {
    				showToast();
    				return 0;
    			}
    		}
    	}
    	return 1;
    }
    private void comprobarganador(){ //si todas las lineas están tachadas y guardadas con 2, alguien ha ganado
    	EasyTracker easyTracker = EasyTracker.getInstance(this);
    	for(i=0;i<15;i++){
    		if(array[i]==2){
    			contador++;
    		}
    	}
    	if(contador==15){
    		if (elturno==1){
    			Toast toast3 = Toast.makeText(getApplicationContext(), getString(R.string.ganador) + " " + getString(R.string.jugadoruno), Toast.LENGTH_LONG);
    			toast3.show();
    			easyTracker.send(MapBuilder
    				      .createEvent("Accion",     
    				                 "Ganador", 
    				                 "Primer Jugador",  
    				                 null)           
    					      .build()
    					  );
    			gotomenu();
    		} else {
    			Toast toast3 = Toast.makeText(getApplicationContext(), getString(R.string.ganador) + " " + getString(R.string.jugadordos), Toast.LENGTH_LONG);
            	toast3.show();
    			easyTracker.send(MapBuilder
  				      .createEvent("Accion",     
  				                 "Ganador", 
  				                 "Segundo Jugador",  
  				                 null)           
  					      .build()
  					  );
            	gotomenu();
    		}
    	}
    	contador=0;
    }
    
    public void showToast() {  //Las advertencias
    	Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.notachasbien), Toast.LENGTH_SHORT);
    	if (debug==0){
    		toast.show();
    		arreglarbug();
    	}
    	debug=1;
    }
    public void showFallo() {
    	Toast toast2 = Toast.makeText(getApplicationContext(), getString(R.string.notachasnada), Toast.LENGTH_SHORT);
    	if (debug==0){
    		toast2.show();
    		arreglarbug();
    	}
    	debug=1;
    }
	private void gotomenu() { //Se dirije hacia la activity MenuPrincipal.
		EasyTracker.getInstance(this).activityStop(this);
		Intent i = new Intent(this, MenuPrincipal.class);
		startActivity(i);
		finish();
	}
	private void arreglarbug(){
	    TimerTask task = new TimerTask() { //definimos a Task
	        @Override
	        public void run() {
	      	  debug=0;
	        }
	      };

	      Timer timer = new Timer();
	      timer.schedule(task, 2200);//Pasado el tiempo, lanza a Task.
	}
}