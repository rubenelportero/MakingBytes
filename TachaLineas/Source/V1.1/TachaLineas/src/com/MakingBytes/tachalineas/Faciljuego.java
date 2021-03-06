package com.MakingBytes.tachalineas;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Faciljuego extends Activity {
    private int array[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; //un array para controlar el estado de las lineas
    private int elturno = 1, contador=0,linea = 0, numimagen = 0, i, debug=0,ganadorfinal=0; //variables
    private int contaclicks=0,perder;
    private long contaseg=0;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tablerodos);
		EasyTracker.getInstance(this).activityStart(this);
	    final TextView turno = (TextView)findViewById(R.id.contadordepulsaciones); //leemos el textView
	    final Chronometer tiempo = (Chronometer) findViewById(R.id.Cronometro);
		tiempo.start();
		Button boton = (Button) findViewById(R.id.button1); //definimos el botón
        boton.setOnClickListener(new View.OnClickListener() { //leemos cuando el botón es pulsado
            public void onClick(View v) {
            	contaclicks++;
            	turno.setText(String.valueOf(contaclicks));
            	contaseg = SystemClock.elapsedRealtime() - tiempo.getBase();  
            	for(i=0;i<15;i++){           //este code se encarga de que las lineas tachadas 
            		if(array[i]==1){        //se guarden en el array con un 2, o lo que viene a 
            			array[i]=2;        //ser igual, no se podrán des-tachar.
            			contador=1; 	  //contador es 1 si se ha convertido alguna línea 
            		}
            	}
            	if (contador==1){
            		linea=0; 	//si contador es 1, se cambia de turno
            		elturno=2;
            		comprobarganador();
            		moverCPU();
            		elturno=1;
            	} else showFallo(); 	 //si contador no es 1, no se ha tachado nada 			
            	contador = 0;   		//puesto que no se ha cambiado ninguna linea a 2.
        		if (ganadorfinal==0)   //Si ganadorfinal no es 0 es por que ya ha ganado la maquina
        		comprobarganador();   //y aquí, tras empezar el nuevo turno, se comprueba si ha ganado alguien
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	// Con esto detectamos el botón de atrás
        	if (debug==0){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Salir");
            alert.setMessage("¿Quieres terminar la partida?");
            alert.setNegativeButton("No",null);
            alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {  
                public void onClick(DialogInterface dialogo1, int id) {  
                	perder=1;
                    gotomenu();  
                }  
            });  
            alert.show();
            arreglarbug();
        	}
        	debug=1;
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
    	int contadordos=0;
    	for(i=0;i<15;i++){
    		if(array[i]==2){
    			contadordos++;
    		}
    	}
    	if(contadordos==15){
    		if (elturno==1){
    			easyTracker.send(MapBuilder
    				      .createEvent("Accion",     
    				                 "Ganador", 
    				                 "Humano",  
    				                 null)           
    					      .build()
    					  );
    			perder = 0;
    			gotomenu();
    		} else {
    			ganadorfinal=1;
    			Toast toast4 = Toast.makeText(getApplicationContext(), getString(R.string.ganadorcpu), Toast.LENGTH_LONG);
            	toast4.show();
    			easyTracker.send(MapBuilder
  				      .createEvent("Accion",     
  				                 "Ganador", 
  				                 "CPU",  
  				                 null)           
  					      .build()
  					  );
    			perder = 1;
            	gotomenu();
    		}
    	}
    	contadordos=0;
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
		int niveld=1;
		EasyTracker.getInstance(this).activityStop(this);
		if (perder==0) {
			Intent i = new Intent(this, FinPartida.class);
			i.putExtra("clicks", contaclicks);
			i.putExtra("seg", contaseg);
			i.putExtra("nivel", niveld);
			startActivity(i);
			finish();
		} else {
			Intent i = new Intent(this, MenuPrincipal.class);
			startActivity(i);
			finish();
		}
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
	private void moverCPU(){
		int contando=0, contadordos=0;
    	for(i=0;i<15;i++){
    		if(array[i]==2){
    			contadordos++;
    		}
    	}
    	if(contadordos==15)contando=1;
		while(contando==0){
		Random semilla = new Random(System.currentTimeMillis());
		int movimientoalazar = semilla.nextInt(15);
		switch (movimientoalazar){
			case 0:
				if(array[movimientoalazar]==0){
					ImageView imagen = (ImageView) findViewById(R.id.imageView1);
					imagen.setImageResource(R.drawable.linea2);
					array[movimientoalazar]=2;
					contando = 1;
				}
				break;
			case 1:
				if(array[movimientoalazar]==0){
					ImageView imagen = (ImageView) findViewById(R.id.imageView2);
					imagen.setImageResource(R.drawable.linea2);
					array[movimientoalazar]=2;
					contando = 1;
				}
				break;
			case 2:
				if(array[movimientoalazar]==0){
					ImageView imagen = (ImageView) findViewById(R.id.ImageView01);
					imagen.setImageResource(R.drawable.linea2);
					array[movimientoalazar]=2;
					contando = 1;
				}
				break;
			case 3:
				if(array[movimientoalazar]==0){
					ImageView imagen = (ImageView) findViewById(R.id.ImageView04);
					imagen.setImageResource(R.drawable.linea2);
					array[movimientoalazar]=2;
					contando = 1;
				}
				break;
			case 4:
				if(array[movimientoalazar]==0){
					ImageView imagen = (ImageView) findViewById(R.id.ImageView02);
					imagen.setImageResource(R.drawable.linea2);
					array[movimientoalazar]=2;
					contando = 1;
				}
				break;
			case 5:
				if(array[movimientoalazar]==0){
					ImageView imagen = (ImageView) findViewById(R.id.ImageView03);
					imagen.setImageResource(R.drawable.linea2);
					array[movimientoalazar]=2;
					contando = 1;
				}
				break;
			case 6:
				if(array[movimientoalazar]==0){
					ImageView imagen = (ImageView) findViewById(R.id.ImageView08);
					imagen.setImageResource(R.drawable.linea2);
					array[movimientoalazar]=2;
					contando = 1;
				}
				break;
			case 7:
				if(array[movimientoalazar]==0){
					ImageView imagen = (ImageView) findViewById(R.id.ImageView06);
					imagen.setImageResource(R.drawable.linea2);
					array[movimientoalazar]=2;
					contando = 1;
				}
				break;
			case 8:
				if(array[movimientoalazar]==0){
					ImageView imagen = (ImageView) findViewById(R.id.ImageView05);
					imagen.setImageResource(R.drawable.linea2);
					array[movimientoalazar]=2;
					contando = 1;
				}
				break;
			case 9:
				if(array[movimientoalazar]==0){
					ImageView imagen = (ImageView) findViewById(R.id.ImageView07);
					imagen.setImageResource(R.drawable.linea2);
					array[movimientoalazar]=2;
					contando = 1;
				}
				break;
			case 10:
				if(array[movimientoalazar]==0){
					ImageView imagen = (ImageView) findViewById(R.id.ImageView13);
					imagen.setImageResource(R.drawable.linea2);
					array[movimientoalazar]=2;
					contando = 1;
				}
				break;
			case 11:
				if(array[movimientoalazar]==0){
					ImageView imagen = (ImageView) findViewById(R.id.ImageView09);
					imagen.setImageResource(R.drawable.linea2);
					array[movimientoalazar]=2;
					contando = 1;
				}
				break;
			case 12:
				if(array[movimientoalazar]==0){
					ImageView imagen = (ImageView) findViewById(R.id.ImageView10);
					imagen.setImageResource(R.drawable.linea2);
					array[movimientoalazar]=2;
					contando = 1;
				}
				break;
			case 13:
				if(array[movimientoalazar]==0){
					ImageView imagen = (ImageView) findViewById(R.id.ImageView11);
					imagen.setImageResource(R.drawable.linea2);
					array[movimientoalazar]=2;
					contando = 1;
				}
				break;
			case 14:
				if(array[movimientoalazar]==0){
					ImageView imagen = (ImageView) findViewById(R.id.ImageView12);
					imagen.setImageResource(R.drawable.linea2);
					array[movimientoalazar]=2;
					contando = 1;
				}
				break;
		}
	
	}}
}