/* 	    *******Copyright (C) 2014 Making Bytes - Rubén García Segovia************	
  	This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/*********************************************************
 * http://MakingBytes.com http://rubenelportero.net 	 *
 * ¡Bienvenido a mi código, si estas leyendo esto		 *
 * es por que estás leyendo el código del juego,		 *
 * solo diré que eres libre de modificar, leer y aprender*
 * de él ya que es de licencia libre.					 *
 * Tras aclarar esto, te deseo suerte para que entiendas *
 * mi forma de programar, y ante todo, gracias por 		 *
 * querer leer mi código, no prometo que todo esté		 *
 * correcto, así que no te asustes por lo que puedas ver *
 * por ahí abajo, ¡lanzate a la aventura!				 *
 *********************************************************/

package com.MakingBytes.tachalineas;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MenuPrincipal extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_principal);
		if (!verificarconexion(this)) {
		    Toast.makeText(getBaseContext(),
		            "Es necesario estar conectado a internet, por favor comprueba tu conexión", Toast.LENGTH_LONG)
		            .show();
		    this.finish();
		}
		 EasyTracker.getInstance(this).activityStart(this);

	}    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	// Con esto detectamos el botón de atrás
        	AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Salir");
            alert.setMessage("¿Quieres salir de Tacha Lineas?");
            alert.setNegativeButton("No",null);
            alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {  
                public void onClick(DialogInterface dialogo1, int id) {  
                	pirarse(); 
                }  
            });  
            alert.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void pirarse(){ 
    	Toast.makeText(getApplicationContext(), "...Cerrando aplicación...",
                Toast.LENGTH_SHORT).show();
        EasyTracker.getInstance(this).activityStop(this);
        this.finish();
    }
    
    public void onClick(View v) { //acción realizada al pulsar sobre un textview
    	EasyTracker easyTracker = EasyTracker.getInstance(this);
    	switch (v.getId()) { //Switch para ver que realizar dependiendo del View pulsado
		case R.id.mp_opcion1: 
			easyTracker.send(MapBuilder
			      .createEvent("Accion",     
			                 "BotonPulsado", 
			                 "1 Jugador",  
			                 null)           
				      .build()
				  );
			gotoconfigjugar();
			break;
		case R.id.mp_opcion2:
			easyTracker.send(MapBuilder
				      .createEvent("Accion",     
				                 "BotonPulsado", 
				                 "2 Jugadores",  
				                 null)           
					      .build()
					  );
			gotodosjugadores();
			break;
		case R.id.mp_opcion3:
			easyTracker.send(MapBuilder
				      .createEvent("Accion",     
				                 "BotonPulsado", 
				                 "Informacion",  
				                 null)           
					      .build()
					  );
			openOptionsMenu();
			break;
		} 
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	EasyTracker easyTracker = EasyTracker.getInstance(this);
    	switch(item.getItemId()){
    	case R.id.m_acerca:
			easyTracker.send(MapBuilder
				      .createEvent("Accion",     
				                 "BotonPulsado", 
				                 "Autor",  
				                 null)           
					      .build()
					  );
    		gotoautor();
    		return true;
    	case R.id.m_comojugar:
			easyTracker.send(MapBuilder
				      .createEvent("Accion",     
				                 "BotonPulsado", 
				                 "ComoJugar",  
				                 null)           
					      .build()
					  );
    		gotoComoJugar();
    		return true;
    	}
		return super.onOptionsItemSelected( item);
    }
	//De aquí para abajo, todos los goto.
	private void gotoautor() {
		EasyTracker.getInstance(this).activityStop(this);
		Intent i = new Intent(this, Autor.class);
		startActivity(i);
		this.finish();
	}
	private void gotoComoJugar() {
		EasyTracker.getInstance(this).activityStop(this);
		Intent i = new Intent(this, ComoJugar.class);
		startActivity(i);
		this.finish();
	}
	private void gotodosjugadores() {
		EasyTracker.getInstance(this).activityStop(this);
		Intent i = new Intent(this, Dosjugadores.class);
		startActivity(i);
		this.finish();
	}
	private void gotoconfigjugar() {
		EasyTracker.getInstance(this).activityStop(this);
		Intent i = new Intent(this, Configurarjuego.class);
		startActivity(i);
		this.finish();
	}
	public static boolean verificarconexion(Context context) {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	    boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
	    return isConnected;
	}
}
