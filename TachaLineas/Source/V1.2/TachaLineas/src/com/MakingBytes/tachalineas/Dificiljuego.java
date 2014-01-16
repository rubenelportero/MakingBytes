package com.MakingBytes.tachalineas;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Dificiljuego extends Activity {
	private AdView adView;
    private int array[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; //un array para controlar el estado de las lineas
    private int elturno = 1, contador=0,linea = 0, numimagen = 0, i, debug=0,ganadorfinal=0; //variables
    private int contaclicks=0,perder;
    private long contaseg=0;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tablerodos);
		 EasyTracker.getInstance(this).activityStart(this);
		    final String publicidad = getString(R.string.IDpublicidad);
		    adView = new AdView(this);
		    adView.setAdUnitId(publicidad);
		    adView.setAdSize(AdSize.BANNER);
		    LinearLayout layout = (LinearLayout)findViewById(R.id.tablero_dos);
		    layout.addView(adView);
		    AdRequest adRequest = new AdRequest.Builder().build();
		    adView.loadAd(adRequest);
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
	            		linea=0; 		  //si contador es 1, se cambia de turno
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
			int niveld=3;
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
			int contando=estrategia(), contadordos=sumatorio();
			if(contando==0)contando=buscarpiramide();
			if(contando==0)contando=buscarladel1y1();
			if(contando==0)contando=buscardosydos();
			if(contando==0)contando=buscarladel3();
			if(contando==0)contando=buscarladel3y3();
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
		private int sumatorio(){
			int contadorsuma=0;
	    	for(i=0;i<15;i++){
	    		if(array[i]==2){
	    			contadorsuma++;
	    		}
	    	}
	    	return contadorsuma;
		}
		private int estrategia(){
			int conta=0;
			if(sumatorio()==9){
				for(i=10;i<15;i++)if(array[i]==0)conta++;
				if(conta==5){
					//TACHARCOLUMNA5
					tacharcolumna5(0);
					return 1;
				}
			}
			if(sumatorio()==10){
				for(i=6;i<10;i++)if(array[i]==0)conta++;
				if(conta==4){
					//TACHARCOLUMNA4
					tacharcolumna4(0);
					return 1;
				} else conta=0;
				for(i=10;i<15;i++)if(array[i]==0)conta++;
				if(conta==4){
					//TACHARCOLUMNA5
					tacharcolumna5(0);
					return 1;
				} else conta=0;
				for(i=10;i<15;i++)if(array[i]==0)conta++;
				if(conta==5){
					//TACHARCOLUMNA5-1
					tacharcolumna5(1);
					return 1;
				}
				
			}
			if(sumatorio()==11){
				for(i=3;i<6;i++)if(array[i]==0)conta++;
				if(conta==3){
					//TACHARCOLUMNA3
					tacharcolumna3(0);
					return 1;
				} else conta=0;
				for(i=6;i<10;i++)if(array[i]==0)conta++;
				if(conta==3){
					//TACHARCOLUMNA4
					tacharcolumna4(0);
					return 1;
				} else conta=0;
				for(i=10;i<15;i++)if(array[i]==0)conta++;
				if(conta==3){
					//TACHARCOLUMNA5
					tacharcolumna5(0);
					return 1;
				} else conta=0;
				for(i=6;i<10;i++)if(array[i]==0)conta++;
				if(conta==4){
					//TACHARCOLUMNA4-1
					tacharcolumna4(1);
					return 1;
				} else conta=0;
				for(i=10;i<15;i++)if(array[i]==0)conta++;
				if(conta==4){
					//TACHARCOLUMNA5-1
					tacharcolumna5(1);
					return 1;
				}
			}
			if(sumatorio()==12){
				conta=0;
				for(i=1;i<3;i++)if(array[i]==0)conta++;
				if(conta==2){
					//TACHARCOLUMNA2
					tacharcoluma2(0);
					return 1;
				} else conta=0;
				for(i=3;i<6;i++)if(array[i]==0)conta++;
				if(conta==2){
					//TACHARCOLUMNA3
					tacharcolumna3(0);
					return 1;
				} else conta=0;
				for(i=6;i<10;i++)if(array[i]==0)conta++;
				if(conta==2){
					//TACHARCOLUMNA4
					tacharcolumna4(0);
					return 1;
				} else conta=0;
				for(i=10;i<15;i++)if(array[i]==0)conta++;
				if(conta==2){
					//TACHARCOLUMNA5
					tacharcolumna5(0);
					return 1;
				} else conta=0;
				for(i=3;i<6;i++)if(array[i]==0)conta++;
				if(conta==3){
					//TACHARCOLUMNA3-1
					tacharcolumna3(1);
					return 1;
				} else conta=0;
				for(i=6;i<10;i++)if(array[i]==0)conta++;
				if(conta==3){
					//TACHARCOLUMNA4-1
					tacharcolumna4(1);
					return 1;
				} else conta=0;
				for(i=10;i<15;i++)if(array[i]==0)conta++;
				if(conta==3){
					//TACHARCOLUMNA5-1
					tacharcolumna5(1);
					return 1;
				} else conta=0;	
			}
		return 0;
		}
		private void tacharcolumna1(){
			ImageView imagen = (ImageView) findViewById(R.id.imageView1);
			imagen.setImageResource(R.drawable.linea2);
			array[0]=2;
		}
		private void tacharcoluma2(int num){
			contador=0;
			for(i=1;i<3;i++)if(array[i]==0)contador++;
			if(contador!=num){
				ImageView imagen = (ImageView) findViewById(R.id.imageView2);
				imagen.setImageResource(R.drawable.linea2);
				array[1]=2;
			}
			contador=0;
			for(i=1;i<3;i++)if(array[i]==0)contador++;
			if(contador!=num){
				ImageView imagen2 = (ImageView) findViewById(R.id.ImageView01);
				imagen2.setImageResource(R.drawable.linea2);
				array[2]=2;
			}
		}
		private void tacharcolumna3(int num){
			contador=0;
			for(i=3;i<6;i++)if(array[i]==0)contador++;
			if(contador!=num){
				ImageView imagen = (ImageView) findViewById(R.id.ImageView04);
				imagen.setImageResource(R.drawable.linea2);
				array[3]=2;
			}
			contador=0;
			for(i=3;i<6;i++)if(array[i]==0)contador++;
			if(contador!=num){
				ImageView imagen2 = (ImageView) findViewById(R.id.ImageView02);
				imagen2.setImageResource(R.drawable.linea2);
				array[4]=2;
			}
			contador=0;
			for(i=3;i<6;i++)if(array[i]==0)contador++;
			if(contador!=num){
				ImageView imagen3 = (ImageView) findViewById(R.id.ImageView03);
				imagen3.setImageResource(R.drawable.linea2);
				array[5]=2;
			}
		}
		private void tacharcolumna4(int num){
			contador=0;
			for(i=6;i<10;i++)if(array[i]==0)contador++;
			if(contador!=num){
				ImageView imagen = (ImageView) findViewById(R.id.ImageView08);
				imagen.setImageResource(R.drawable.linea2);
				array[6]=2;
			}
			contador=0;
			for(i=6;i<10;i++)if(array[i]==0)contador++;
			if(contador!=num){
				ImageView imagen2 = (ImageView) findViewById(R.id.ImageView06);
				imagen2.setImageResource(R.drawable.linea2);
				array[7]=2;
			}
			contador=0;
			for(i=6;i<10;i++)if(array[i]==0)contador++;
			if(contador!=num){
				ImageView imagen3 = (ImageView) findViewById(R.id.ImageView05);
				imagen3.setImageResource(R.drawable.linea2);
				array[8]=2;
			}
			contador=0;
			for(i=6;i<10;i++)if(array[i]==0)contador++;
			if(contador!=num){
				ImageView imagen4 = (ImageView) findViewById(R.id.ImageView07);
				imagen4.setImageResource(R.drawable.linea2);
				array[9]=2;
			}
		}
		private void tacharcolumna5(int num){
			contador=0;
			for(i=10;i<15;i++)if(array[i]==0)contador++;
			if(contador!=num){
				ImageView imagen = (ImageView) findViewById(R.id.ImageView13);
				imagen.setImageResource(R.drawable.linea2);
				array[10]=2;
			}
			contador=0;
			for(i=10;i<15;i++)if(array[i]==0)contador++;
			if(contador!=num){
				ImageView imagen2 = (ImageView) findViewById(R.id.ImageView09);
				imagen2.setImageResource(R.drawable.linea2);
				array[11]=2;
			}
			contador=0;
			for(i=10;i<15;i++)if(array[i]==0)contador++;
			if(contador!=num){
				ImageView imagen3 = (ImageView) findViewById(R.id.ImageView10);
				imagen3.setImageResource(R.drawable.linea2);
				array[12]=2;
			}
			contador=0;
			for(i=10;i<15;i++)if(array[i]==0)contador++;
			if(contador!=num){
				ImageView imagen4 = (ImageView) findViewById(R.id.ImageView11);
				imagen4.setImageResource(R.drawable.linea2);
				array[13]=2;
			}
			contador=0;
			for(i=10;i<15;i++)if(array[i]==0)contador++;
			if(contador!=num){
				ImageView imagen5 = (ImageView) findViewById(R.id.ImageView12);
				imagen5.setImageResource(R.drawable.linea2);
				array[14]=2;
			}
		}
		private int buscardosydos(){
			int conta[]={0,0,0,0,0},z=0,z2=0,z3=0,z4=0,z5=0;
			for(i=1;i<3;i++)if(array[i]==0)conta[1]++;
			for(i=3;i<6;i++)if(array[i]==0)conta[2]++;
			for(i=6;i<10;i++)if(array[i]==0)conta[3]++;
			for(i=10;i<15;i++)if(array[i]==0)conta[4]++;
			if(array[0]==0)conta[0]=1;
			for(i=0;i<5;i++)if(conta[i]==2)z++;
			for(i=0;i<5;i++)if(conta[i]==0)z2++;
			for(i=0;i<5;i++)if(conta[i]==1)z3++;
			for(i=0;i<5;i++)if(conta[i]==3)z4++;
			for(i=0;i<5;i++)if(conta[i]==4)z5++;
			if(z3>=2)z2+=2;
			if(z4>=2)z2+=2;
			if(z5>=2)z2+=2;
			if(z>=2&&z2==2){
				for(i=0;i<5;i++){
					switch (i){
						case 0:
							if((conta[i]==0)||(z==2&&conta[i]==2))break;
							tacharcolumna1();
							return 1;
						case 1:
							if((conta[i]==0)||(z==2&&conta[i]==2))break;
							tacharcoluma2(0);
							return 1;
						case 2:
							if((conta[i]==0)||(z==2&&conta[i]==2))break;
							tacharcolumna3(0);
							return 1;
						case 3:
							if((conta[i]==0)||(z==2&&conta[i]==2))break;
							tacharcolumna4(0);
							return 1;
						case 4:
							if((conta[i]==0)||(z==2&&conta[i]==2))break;
							tacharcolumna5(0);
							return 1;
					}
				}
			}
			if(z==1&&z2==3){
				for(i=0;i<5;i++){
					if(conta[i]>1&&conta[i]!=2){
						switch (i){
							case 1:
								if(conta[i]==2||conta[i]==0||conta[i]<3)break;
								tacharcoluma2(2);
								return 1;
							case 2:
								if(conta[i]==2||conta[i]==0||conta[i]<3)break;
								tacharcolumna3(2);
								return 1;
							case 3:
								if(conta[i]==2||conta[i]==0||conta[i]<3)break;
								tacharcolumna4(2);
								return 1;
							case 4:
								if(conta[i]==2||conta[i]==0||conta[i]<3)break;
								tacharcolumna5(2);
								return 1;
						}
					}
				}
			}
		return 0;
		} 
		private int buscarpiramide(){
		int conta0=0,conta=0,conta2=0,conta3=0,conta4=0;
		if(array[0]==0)conta0++;
		for(i=1;i<3;i++)if(array[i]==0)conta++;
		for(i=3;i<6;i++)if(array[i]==0)conta2++;
		for(i=6;i<10;i++)if(array[i]==0)conta3++;
		for(i=10;i<15;i++)if(array[i]==0)conta4++;
		if(conta==2||conta2>=2||conta3>=2||conta4>=2){
			if(conta2>=3||conta3>=3||conta4>=3){
				if(conta0==1){ //localizado el 1
					if(conta==2){ //localizado el 2
						if(conta2==3){ //localizado el 3
							if(conta3!=0 && conta4!=0){ //no se puede hacer
								return 0;
							} else if(conta3==0){ //si la columna 4 ya es 0...
								tacharcolumna5(0);
								return 1;
							} else {             //si la columna 5 no es 0...
								tacharcolumna4(0);
								return 1;
							}
						} else{   //no localizado el 3 (no es la tercera columna)
							if(((conta3==3&&conta4==0)||(conta4==3&&conta3==0))&&conta2!=0)tacharcolumna3(0);
							else if(conta3>3&&conta4==0) tacharcolumna4(3);
							else if (conta4>3&&conta3==0) tacharcolumna5(3);
							else if ((conta3==3&&conta2==0)&&conta4!=0)tacharcolumna5(0);
							else if ((conta4==3&&conta2==0)&&conta3!=0)tacharcolumna4(0);
							else return 0;
							return 1;
						}
					} else { //no localizado el 2 (no es la segunda columna)
						if(buscar2y3piramide()==1) return 1;
						else return 0;
					} //FIN conta==2
				} else if(conta==2){ //encontrado el 2, pero no el 1 o el 2.
					if(conta2==3){
						if(conta3>1&&conta4==0)tacharcolumna4(1);
						else if(conta4>1&&conta3==0)tacharcolumna5(1);
						else if(conta3==1&&conta4!=0)tacharcolumna5(0);
						else if(conta4==1&&conta3!=0)tacharcolumna4(0);
						else return 0;
						return 1;
					} else if(conta3==3){
						if(conta4>1&&conta2==0)tacharcolumna5(1);
						else if(conta2>1&&conta4==0)tacharcolumna3(1);
						else if(conta2==1&&conta4!=0)tacharcolumna5(0);
						else if(conta4==1&&conta2!=0)tacharcolumna3(0);
						else return 0;
						return 1;
					}
				} else if(conta==1){
					if(buscar2y3piramide()==1) return 1;
					else return 0;
				} else if(conta2==3){
					if(conta3==1&&conta4!=2)tacharcolumna5(2);
					else if (conta4==1&&conta3!=2)tacharcolumna4(2);
					else if(conta3==2&&conta4!=1) tacharcolumna5(1);
					else if(conta4==2&&conta3!=1) tacharcolumna4(1);
					else return 0;
					return 1;
				} else if(conta2==2){
					if(conta3==1&&conta4!=3)tacharcolumna5(3);
					else if (conta4==1&&conta3!=3)tacharcolumna4(3);
					else if(conta3==3&&conta4!=1) tacharcolumna5(1);
					else if(conta4==3&&conta3!=1) tacharcolumna4(1);
					else return 0;
					return 1;	
				} else if(conta2==1){
					if(conta3==2&&conta4!=3)tacharcolumna5(3);
					else if (conta4==2&&conta3!=3)tacharcolumna4(3);
					else if(conta3==3&&conta4!=2) tacharcolumna5(2);
					else if(conta4==3&&conta3!=2) tacharcolumna4(2);
					else return 0;
					return 1;				
				}
			} //FIN if(conta2>=3||conta3>=3||conta4>=3)
		}return 0;
		}
		private int buscar2y3piramide(){
			int conta=0,conta2=0,conta3=0,conta4=0;
			for(i=1;i<3;i++)if(array[i]==0)conta++;
			for(i=3;i<6;i++)if(array[i]==0)conta2++;
			for(i=6;i<10;i++)if(array[i]==0)conta3++;
			for(i=10;i<15;i++)if(array[i]==0)conta4++;
			if(conta2==2){ //localizado el 2
				if (conta3==3&&conta4!=0){ tacharcolumna5(0);
					return 1;
				}
				if((conta3>3&&conta4!=0)||(conta4>3&&conta3!=0)) return 0;
				else if(conta4==3&&conta3!=0&&conta!=1)tacharcolumna4(0);
				else if(conta4>3&&conta3==0&&conta!=1) tacharcolumna5(3);
				else if(conta3>3&&conta4==0&&conta!=1) tacharcolumna4(3);
				else if(conta3==3&&conta4==0&&conta==1) tacharcoluma2(0); 
				else if(conta4==3&&conta3==0&&conta==1) tacharcoluma2(0); 
				else return 0;
				return 1;
			} else if(conta3==2){ //localizado el 2
					if(conta2==3&&conta4!=0&&conta!=1) tacharcolumna5(0);
					else if(conta2==0&&conta4>3&&conta!=1)tacharcolumna5(3);
					else if(conta4==3&&conta2!=0&&conta!=1) tacharcolumna3(0);
					else if(conta2==3&&conta4==0&&conta==1) tacharcoluma2(0); 
					else if(conta4==3&&conta2==0&&conta==1) tacharcoluma2(0);
					else return 0;
					return 1;
			} else if(conta4==2){ //localizado el 2
					if(conta2==0&&conta3>3) tacharcolumna4(3);
					else if(conta2==3&&conta3!=0&&conta!=1) tacharcolumna4(0);
					else if(conta3==3&&conta2!=0&&conta!=1) tacharcolumna3(0);
					else if(conta2==3&&conta3==0&&conta==1) tacharcoluma2(0); 
					else if(conta3==3&&conta2==0&&conta==1) tacharcoluma2(0);
					else return 0;
					return 1;
			} else if(conta2==3){
				if(conta3!=0&&conta4!=0)return 0;
				if(conta3==0&&conta4!=2&&conta!=1) tacharcolumna5(2);
				else if(conta3!=2&&conta4==0&&conta!=1)tacharcolumna4(2);
				else if(conta3==2&&conta4==0&&conta==1) tacharcoluma2(0); 
				else if(conta4==2&&conta3==0&&conta==1) tacharcoluma2(0);
				else return 0;
				return 1;
			}else if(conta3==3){
				if(conta4>2&&conta!=1&&conta2!=1) tacharcolumna5(2);
				else if(conta4==2&&conta!=0&&conta2==0) tacharcoluma2(0); 
				else if(conta4==2&&conta2!=0&&conta==0) tacharcoluma2(0);
				else return 0;
				return 1;
			}else if(conta4==3){
				if (conta3<2) return 0;
				if(conta3==2&&conta2==1){
					tacharcoluma2(0);
					return 1;
				}
				if (conta3!=2&&conta2==0){
					tacharcolumna4(2);
					return 1;
				}
			} return 0;
		}
		private int buscarladel3(){
			int conta[]={0,0,0,0,0},z=0,z2=0;
			for(i=1;i<3;i++)if(array[i]==0)conta[1]++;
			for(i=3;i<6;i++)if(array[i]==0)conta[2]++;
			for(i=6;i<10;i++)if(array[i]==0)conta[3]++;
			for(i=10;i<15;i++)if(array[i]==0)conta[4]++;
			if(array[0]==0)conta[0]=1;
			for(i=0;i<5;i++)if(conta[i]==1)z++;
			for(i=0;i<5;i++)if(conta[i]==0)z2++;
			if(z==3&&z2==1){
				for(i=0;i<5;i++){
					if(conta[i]>1){
						switch (i){
							case 1:
								tacharcoluma2(0);
								return 1;
							case 2:
								tacharcolumna3(0);
								return 1;
							case 3:
								tacharcolumna4(0);
								return 1;
							case 4:
								tacharcolumna5(0);
								return 1;
						}
					}
				}
			}
			if((z==2&&z2==2)||(z==4&&z2==0)){
				for(i=0;i<5;i++){
					if(conta[i]>1){
						switch (i){
							case 1:
								tacharcoluma2(1);
								return 1;
							case 2:
								tacharcolumna3(1);
								return 1;
							case 3:
								tacharcolumna4(1);
								return 1;
							case 4:
								tacharcolumna5(1);
								return 1;
						}
					}
				}
			}
			return 0;
		}
		private int buscarladel3y3(){
			int conta[]={0,0,0,0,0},z=0,z2=0,z3=0,z4=0;
			for(i=1;i<3;i++)if(array[i]==0)conta[1]++;
			for(i=3;i<6;i++)if(array[i]==0)conta[2]++;
			for(i=6;i<10;i++)if(array[i]==0)conta[3]++;
			for(i=10;i<15;i++)if(array[i]==0)conta[4]++;
			if(array[0]==0)conta[0]=1;
			for(i=0;i<5;i++)if(conta[i]==3)z++;
			for(i=0;i<5;i++)if(conta[i]==0)z2++;
			for(i=0;i<5;i++)if(conta[i]==1)z3++;
			for(i=0;i<5;i++)if(conta[i]==2)z4++;
			if(z3==2)z2=z2+2;
			if(z4==2)z2=z2+2;
			if(z==3&&z2==2){
				for(i=0;i<5;i++){
					if(conta[i]>1){
						switch (i){
							case 1:
								tacharcoluma2(0);
								return 1;
							case 2:
								tacharcolumna3(0);
								return 1;
							case 3:
								tacharcolumna4(0);
								return 1;
							case 4:
								tacharcolumna5(0);
								return 1;
						}
					}
				}
			}
			if(z==2&&z2==2){
				for(i=0;i<5;i++){
					if(conta[i]>1&&conta[i]!=3){
						switch (i){
							case 1:
								tacharcoluma2(0);
								return 1;
							case 2:
								tacharcolumna3(0);
								return 1;
							case 3:
								tacharcolumna4(0);
								return 1;
							case 4:
								tacharcolumna5(0);
								return 1;
						}
					}
				}
			}
			if(z==1&&z2==3){
				for(i=0;i<5;i++){
					if(conta[i]>3){
						switch (i){
							case 2:
								tacharcolumna3(3);
								return 1;
							case 3:
								tacharcolumna4(3);
								return 1;
							case 4:
								tacharcolumna5(3);
								return 1;
						}
					}
				}
			}
			return 0;
		}
		private int buscarladel1y1(){
			int conta[]={0,0,0,0,0},z=0,z2=0,z3=0,z0=0,pos,j,nigga=0;
			for(i=1;i<3;i++)if(array[i]==0)conta[1]++;
			for(i=3;i<6;i++)if(array[i]==0)conta[2]++;
			for(i=6;i<10;i++)if(array[i]==0)conta[3]++;
			for(i=10;i<15;i++)if(array[i]==0)conta[4]++;
			if(array[0]==0)conta[0]=1;
			for(i=0;i<5;i++)if(conta[i]==1)z++;
			for(i=0;i<5;i++)if(conta[i]==2)z2++;
			for(i=0;i<5;i++)if(conta[i]==3)z3++;		
			for(i=0;i<5;i++)if(conta[i]==0)z0++;
			if(z>=1&&z2>=1&&z3>=1){//empezamos primero verificando pirámide
				if(z==2)pos=1;
				else if(z2==2) pos=2;
				else if(z3==2) pos=3;
				else return 0;
				for(j=4;j>0;j--){
					for(i=0;i<5;i++){
						if(conta[i]>j){
							switch (i){
							case 1:
								if((conta[i]==2&&z2==1)||(conta[i]==3&&z3==1))break;
								tacharcoluma2(pos);
								return 1;
							case 2:
								if((conta[i]==2&&z2==1)||(conta[i]==3&&z3==1))break;
								tacharcolumna3(pos);
								return 1;
							case 3:
								if((conta[i]==2&&z2==1)||(conta[i]==3&&z3==1))break;
								tacharcolumna4(pos);
								return 1;
							case 4:
								if((conta[i]==2&&z2==1)||(conta[i]==3&&z3==1))break;
								tacharcolumna5(pos);
								return 1;
							}
						}
					}
				}
			}
			if(z2>=2&&z>=0){//Ahora verificaremos la 2 y 2
				if(z==2)pos=0;
				else if(z2==3&&z==0)pos=2;
				else if (z==1&&z0==1) pos=1;
				else if(z==1&&z2==4){
					tacharcolumna1();
					return 1;
				}
				else return 0;
					for(i=0;i<5;i++){
						switch (i){
						case 1:
							if((conta[i]==2&&z2==2)||(conta[i]==pos)||conta[i]<pos)break;
							tacharcoluma2(pos);
							return 1;
						case 2:
							if(conta[i]==2&&z2==2||(conta[i]==pos)||conta[i]<pos)break;
							tacharcolumna3(pos);
							return 1;
						case 3:
							if(conta[i]==2&&z2==2||(conta[i]==pos)||conta[i]<pos)break;
							tacharcolumna4(pos);
							return 1;
						case 4:
							if(conta[i]==2&&z2==2||(conta[i]==pos)||conta[i]<pos)break;
							tacharcolumna5(pos);
							return 1;
						}
					}
					return 0;
				}
			if(z3>=2&&z>=1){  //Ahora verificaremos la 3 y 3
				if(z==2)pos=0;
				else pos=1;
				for(i=0;i<5;i++)if(conta[i]==0)nigga++;
				if(nigga!=1)return 0;
				for(j=4;j>0;j--){
					for(i=0;i<5;i++){
						if(conta[i]>j){
							switch (i){
							case 1:
								if(j==3&&(conta[i]==3&&z3==2))break;
								tacharcoluma2(pos);
								return 1;
							case 2:
								if(j==3&&(conta[i]==3&&z3==2))break;
								tacharcolumna3(pos);
								return 1;
							case 3:
								if(j==3&&(conta[i]==3&&z3==2))break;
								tacharcolumna4(pos);
								return 1;
							case 4:
								if(j==3&&(conta[i]==3&&z3==2))break;
								tacharcolumna5(pos);
								return 1;
							}
						}
					}
				}
			}
			if(conta[3]==conta[4]&&conta[4]!=0){  
				if(z==2){
					if(conta[0]==1&&conta[1]==1&&conta[2]!=0)tacharcolumna3(0);
					else if(conta[0]==1&&conta[2]==1&&conta[1]!=0)tacharcoluma2(0);
					else return 0;
					return 1;
				}
				if(z==3&&conta[4]!=1){
					tacharcolumna1();
					return 1;
				}
				if(z==1&&z0==1){
					if(conta[1]>1)tacharcoluma2(1);
					else if(conta[2]>1)tacharcolumna3(1);
					else return 0;
					return 1;
				}
				if(z0==2){
					if(conta[0]!=0)tacharcolumna1();
					else if(conta[1]!=0)tacharcoluma2(0);
					else tacharcolumna3(0);
					return 1;
				}	
			}
			return 0;
		}
	}