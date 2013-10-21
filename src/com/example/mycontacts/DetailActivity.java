package com.example.mycontacts;

import com.example.mycontacts.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

//Ejemplo de actividad que usa fragmentos para componer su vista
public class DetailActivity extends Activity {

	//Sobreescribir onCreate para cargar vista y configurar activity
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//establecer como vista el layout indicado
		setContentView(R.layout.activity_detail);
		
		TextView text01 = (TextView) findViewById(R.id.textView01);
		text01.setText("Perico");
		TextView text02 = (TextView) findViewById(R.id.textView02);
		text02.setText("Palotes");
		//configurar Action Bar
		setupActionBar();
	}

	/**
	 * Configurar Action Bar
	 */
	private void setupActionBar() {

		//Activar el boton de navegacion Arriba (Up)
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	//Sobreescribir onCreateOptionsMenu para indicar el esquema XML que contiene el menu de acciones para la Action Bar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflar el menu y poner acciones en Action Bar si esta.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

	// Redefinir onOptionsItemSelected para capturar la seleccion de acciones en el Action Bar
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Identificar que accion se ha elegido en base a su id
		switch (item.getItemId()) {
		
		case R.id.action_delete: //id que hemos dado a la accion de ajustes
			// Remplazar el fragmento original del FrameLayout 'fragment_container' por el de las preferencias
			//Obtener gestor, crear transaccion, indicar tipo de transaccion (replace) y realizar transaccion (commit)
		
			return true;

		case android.R.id.home: //id del sistema que identifica el boton Arriba (Up)
			//No podemos usar NavUtils por usar fragmentos en el activity
			//Finalizamos la actividad 
			finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
