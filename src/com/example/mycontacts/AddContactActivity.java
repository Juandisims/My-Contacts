package com.example.mycontacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class AddContactActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_user, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void addContact(View view) {
		
		EditText editText1 = (EditText) findViewById(R.id.contact_name);
		String name = editText1.getText().toString();
		EditText editText2 = (EditText) findViewById(R.id.contact_surname);
		String surname = editText2.getText().toString();
		
		if (!name.isEmpty()) {
			MyContact c = new MyContact();
			c.setName(name);
			c.setSurname(surname);

			Intent data = new Intent();
			data.putExtra(MyContact.CONTACT_KEY,c);
			setResult(RESULT_OK,data);
		}
		
		finish();
	}

}
