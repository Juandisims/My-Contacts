package com.example.mycontacts;

import java.util.ArrayList;
import java.util.List;

import com.example.mycontacts.MyContactsDataBase.ContactsCursor;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends ListActivity{
	
	private static final int ADD_CONTACT_CODE = 1;

	//Instancia que referencia a la ListView del Activity para poder interaccionar con ella
	//en el modo contextual (listeners del CAB)
	private ListView mListView;

	//Instancia que mantiene los datos a mostrar en el ListView
	//Se declara aqui para poder usarla en los metodos del modo contextual
	private List<String> mListaEjemplo;
	
	private BaseAdapter mListAdapter;
	
	private MyContactsDataBase contactsDB;

	private ContactsCursor mCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		contactsDB = new MyContactsDataBase(this);
		
		setContentView(R.layout.activity_main);
		
		// inicializar lista contenidos de ejemplo
		mListaEjemplo = new ArrayList<String>();
		mListaEjemplo.add("Perico");
		mListaEjemplo.add("Ana");
//		mListaEjemplo.add("Juan");
//		mListaEjemplo.add("Remigio");
//		mListaEjemplo.add("Chisca");
//		mListaEjemplo.add("Donald");
//		mListaEjemplo.add("Smither");
//		mListaEjemplo.add("Burt");
		

		// Crear y establecer un adaptador para mListaEjemplo y la ListView
//		mListAdapter =new ArrayAdapter<String>(this.getBaseContext(),
//				R.layout.list_item, 
//				R.id.titulo,
//				mListaEjemplo);
		
		mCursor = contactsDB.getJobs(ContactsCursor.SortBy.name);
		mListAdapter = new SimpleCursorAdapter(this, R.layout.list_item, 
				mCursor,
				new String[] { "name" }, 
				new int[] { R.id.titulo });
		
		setListAdapter(mListAdapter);
	}
	
	@Override
	public void onRestart(){
		super.onRestart();
		mCursor.requery();
		mListAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Identificar que accion se ha elegido en base a su id
		switch (item.getItemId()) {
		case R.id.action_user_add:
			Intent intent = new Intent(this, AddContactActivity.class);
			startActivityForResult(intent, ADD_CONTACT_CODE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	@Override 
	protected void onListItemClick(ListView listView, 
			View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);

		//Obtenemos el item seleccionado
		Object o = getListAdapter().getItem(position);
		//Creamos un intent para iniciar la actividad de detalle
		Intent intent = new Intent(this, DetailActivity.class);
		
		//Pasamos el titulo del item como informacion extra en el intent para la nueva actividad
//		intent.putExtra("CONTROL_04", o.toString());
		
		mCursor.moveToPosition(position);
		intent.putExtra("_id", (int) mCursor.getColId());
		
		//iniciamos la actividad
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == ADD_CONTACT_CODE)
			if (data.hasExtra(MyContact.CONTACT_KEY)) {
				MyContact c = data.getExtras().getParcelable(MyContact.CONTACT_KEY);
//				mListaEjemplo.add(c.getName());
				contactsDB.addContact(c.getName(),c.getSurname());
				mCursor.requery();
				mListAdapter.notifyDataSetChanged();
			}
				
	}
}
