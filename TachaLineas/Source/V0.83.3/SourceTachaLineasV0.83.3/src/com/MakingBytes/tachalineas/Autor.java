package com.MakingBytes.tachalineas;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

public class Autor extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.autor);
		 EasyTracker.getInstance(this).activityStart(this);
		 EasyTracker easyTracker = EasyTracker.getInstance(this);
		 easyTracker.send(MapBuilder
				  .createAppView()
				  .set(Fields.SCREEN_NAME, "Autor")
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