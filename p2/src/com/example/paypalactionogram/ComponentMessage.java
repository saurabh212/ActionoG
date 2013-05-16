package com.example.paypalactionogram;


import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ComponentMessage extends Activity implements View.OnClickListener {
	Node passNode;
	// ArrayList<Node> passList;
	Button composeEmail;
	TextView name;
	ListView listview2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cmessage);
		initialize();
		/*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, passNode.message);
		 * for ( String item : calMessage){ passNode.message.add( item ); }
		 */
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.row3, passNode.message);
		listview2.setAdapter(adapter);
		//listview2.setBackgroundResource( R.drawable.bag );

	}

	private void initialize() {
		composeEmail = (Button) findViewById(R.id.compose);
		name = (TextView) findViewById(R.id.component_name);
		listview2 = (ListView) findViewById(R.id.message_list);
		composeEmail.setOnClickListener(this);

		Bundle i = getIntent().getExtras();
		passNode = (Node) i.getSerializable("passNode");
		// name.setText( passNode.toString() );
		name.setText(passNode.getName());
	}
	

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.compose:
			// String[] calMessage = new String[ passNode.message.size() ];
			// passNode.message.toArray(calMessage);
			Intent emailSet = new Intent(this, Email.class);
			
			String[] messages = new String[ passNode.message.size()];
			passNode.message.toArray(messages);
			
			//String[] emails = new String[ passNode.email.size() ];
			//passNode.email.toArray(emails);
			
			emailSet.putExtra("email", passNode.email);
			emailSet.putExtra("message", messages);
	
			emailSet.putExtra("status", Math.max( 
											Math.abs( passNode.inStatus.intValue( ) ), 
											Math.abs( passNode.inStatus.intValue( ) ) 
										   )
							 );
			emailSet.putExtra("name", passNode.component);
			// emailSet.putExtra("message", calMessage );
			startActivity(emailSet);
		}
	}
}
