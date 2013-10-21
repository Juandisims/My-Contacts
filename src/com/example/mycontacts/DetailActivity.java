package com.example.mycontacts;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mycontacts.MyContactsDataBase.ContactDetailCursor;

//Ejemplo de actividad que usa fragmentos para componer su vista
public class DetailActivity extends Activity {

	private Integer contact_id;
	private MyContactsDataBase contactsDB;
	private ContactDetailCursor contact;

	//Sobreescribir onCreate para cargar vista y configurar activity
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//establecer como vista el layout indicado
		setContentView(R.layout.activity_detail);
		
		// get the job_id for this job from the bundle passed by MicroJobsList
        Bundle bIn = this.getIntent().getExtras();
        contact_id = Integer.valueOf(bIn.getInt("_id"));

        contactsDB = new MyContactsDataBase(this);
        contact = contactsDB.getContactDetails(contact_id.longValue());
        startManagingCursor(contact);
		
		TextView text01 = (TextView) findViewById(R.id.textView01);
		text01.setText(contact.getColName());
		TextView text02 = (TextView) findViewById(R.id.textView02);
		text02.setText(contact.getColSurname());
		
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
			contactsDB.deleteContact(contact_id);
		    finish();
			return true;

		case android.R.id.home: //id del sistema que identifica el boton Arriba (Up)
			NavUtils.navigateUpFromSameTask(this);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
