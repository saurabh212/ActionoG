package com.example.paypalactionogram;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Email extends Activity implements View.OnClickListener {

	// all UI elements - edit texts
	EditText etReceiverEmail, etIntro, etReceiverName, etStupidThings,
			etAction;
	Button sendEmail;
	TextView tvDefault;
	// strings keeping final values to be passed onto next activity
	String subject, emailAdd, BriefDescription, actionExpected, body, receiver;

	// accepting the bundle
	ArrayList<String> emails;
	String[] messages;
	int status;
	String PayPalname, ComponentNname;
	// intermediate
	String[] emailList;
	SharedPreferences preferredUrls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email);

		getAllIntentExtras();
		initializeVars();

		sendEmail.setOnClickListener(this);
	}

	private void initializeVars() {

		String forTextEmail = "Default To: ";
		for (String each : emailList)
			forTextEmail += each + "; ";
		forTextEmail += "\nAdd mail?";
		// Default - To( emails):
		tvDefault = (TextView) findViewById(R.id.tvDefault);
		tvDefault.setText(forTextEmail);

		etReceiverEmail = (EditText) findViewById(R.id.etEmails);

		// Subject
		etIntro = (EditText) findViewById(R.id.etIntro);
		subject = ComponentNname + " health status " + status;
		etIntro.setText(subject);

		// Receiver Name
		etReceiverName = (EditText) findViewById(R.id.etName);

		// Brief description
		etStupidThings = (EditText) findViewById(R.id.etThings);

		// Action to be taken
		etAction = (EditText) findViewById(R.id.etAction);

		// send Email button
		sendEmail = (Button) findViewById(R.id.bSentEmail);
	}

	public void onClick(View v) {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

		// compose email list
		emailAdd = etReceiverEmail.getText().toString();
		emails.add(emailAdd);
		emailList = new String[emails.size()];
		emails.toArray(emailList);
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, emailList);

		// compose subject
		subject = etIntro.getText().toString();
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);

		// compose email message
		receiver = etReceiverName.getText().toString();
		String message = (receiver + ",\n\n");

		BriefDescription = etStupidThings.getText().toString();
		message += BriefDescription + "\n";
		for (String each : messages) {
			if (each != null)
				message += "\n" + "*" + each;
		}
		actionExpected = etAction.getText().toString();
		body = message + "\n" + actionExpected + "\n\nThanks,\n";
		preferredUrls = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		PayPalname = preferredUrls.getString("name", "Mehrotra, Saurabh");
		body += PayPalname;
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
		startActivity(emailIntent);
	}

	private void getAllIntentExtras() {
		// string array
		messages = getIntent().getExtras().getStringArray("message");
		// integer status
		status = getIntent().getExtras().getInt("status");
		// ArrayList<string> email
		emails = (ArrayList<String>) getIntent().getExtras().getSerializable(
				"email");
		emailList = new String[emails.size()];
		emails.toArray(emailList);
		// String component name
		ComponentNname = getIntent().getExtras().getString("name");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// finish();
	}

}