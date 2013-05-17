package com.example.paypalactionogram;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONObject;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
/*
 * ActionHealth is the main class-activity. This is where the user starts with on the application.
 * The class creates, loads, transforms, and structures the data available in form of JSON file obtained from the back-end.
 * Failure to load, failure to transform, or other problems are thrown as alerts to user, recommending suitable action which can be taken.
 * If the all previous processes worked well without giving a jab, the "View Health" button appears.
 * Paving way for user to navigate into and through hierarchy of domains and sub-domains and components.
 * 
 * The activity also bears a menu item, wherein information "ABout", a few "HELP" tutorials, or adding
 * custom preferences  can come handy to user in understanding the application better.
 */
public class ActionHealth extends Activity implements OnClickListener {

	private String[] List = { 
			"https://dl.dropboxusercontent.com/u/32902686/Actionogram/email%2Barrows%2Bmessage%2Bdictionary.json",
			"https://dl.dropboxusercontent.com/u/32902686/Actionogram/email%2Barrows%2Bmessage%2Bdictionary%2Bstatuses.json",
			"https://dl.dropboxusercontent.com/u/32902686/Actionogram/type%20prob%20name.json", 
			"http://10.239.64.77/html/scrap/actiongram/data.json" 		};
	
	/*
	 *  fetch JSON object data in form of string from the url
	 */
	public String InternetData, PayPalname, ManualLink;
	/* 
	 *Build the tree hierarchy, and store the pointer to the root Node and the passing Node here
	 *See Node.class for more details
	 */
	public static Node rootNode, passNode;
	/*
	 * BUild the tree hierarchy and store the root List and the passing List
	 */
	public static ArrayList<Node> passList, rootList;
	/*
	 * A flag variable saving whether the screen has been viewed once
	 */
	public static Boolean hasRanOnce;
	/*
	 * Displays the progress element, while the application loadas and orders the tree data
	 */
	public ProgressBar pgb;
	/*
	 * UI element button on screen
	 */
	public Button Go, supportTeam, reLoad;
	
	public TextView screenStatus;
	
	SharedPreferences preferredUrls;
	protected String URL;
	boolean useManual;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_action_health);
		
		preferencesFetch();
		Initialize();
		
		// begin AsyncTask doInBackground fetch of data
		RequestData(URL);
	}

	private void preferencesFetch() {
		preferredUrls = PreferenceManager.getDefaultSharedPreferences( getBaseContext() );
		PayPalname = preferredUrls.getString("name", "Mehrotra, Saurabh");		
		ManualLink = preferredUrls.getString("manual", "https://dl.dropboxusercontent.com/u/32902686/Actionogram/123.json");
		useManual = preferredUrls.getBoolean( "checkbox", false );
		if( useManual ){
			URL = ManualLink;
		}
		else{
			URL = List [ (int) (Integer.parseInt( preferredUrls.getString("list", "1") ) - 1)] ;
		}
		
	}

	private void Initialize( ) {
		Go = ( Button ) findViewById( R.id.DisplayHealth );
		Go.setOnClickListener( this );
		Go.setVisibility( android.view.View.INVISIBLE );
		
		screenStatus = (TextView) findViewById( R.id.loading);
		
		supportTeam = (Button) findViewById( R.id.bSupportTeam );
		supportTeam.setVisibility(android.view.View.INVISIBLE );
		supportTeam.setOnClickListener( this);
		
		reLoad = (Button) findViewById( R.id.bReload );
		reLoad.setVisibility(android.view.View.INVISIBLE );
		reLoad.setOnClickListener( this);
		
		hasRanOnce = Boolean.FALSE;
		rootNode = new Node( );
		passList = new ArrayList<Node>( );
		rootList = new ArrayList<Node>( );
		pgb = (ProgressBar) findViewById( R.id.progressBar2 );
	}

	public void RequestData( String string ) {
		GetMethodEx requestObject = new GetMethodEx( );
		requestObject.setActivity( this );
		requestObject.execute( string );
	}

	// function called by OnPostExecute of GetMethodEx, i.e. when the
	// Internet data has been fetched and ready to use
	public void BuildNodes( ) {
		try {
			// create JSON objects and the subsequent hierarchies
			JSONObject jObject = new JSONObject( InternetData );
			rootNode = rootNode.createNode( jObject );
			// assign passNode value to be pass on to ListDomainClass
			// passNode = new Node(rootNode);
			passList.add( rootNode );
		} catch ( Exception e ) {
			e.printStackTrace( );
		}

	}

	protected void StartListUp() {
		// start new activity for publishing the results as a list
		Intent ListUp = new Intent(this, ListOnScreen.class);
		ListUp.putExtra("passList", passList);
		startActivity( ListUp );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_action_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.preferences:			
			Intent prefAct = new Intent(this, Prefs.class);			
			startActivity(prefAct);
			break;
		case R.id.about:
			Intent aboutMe = new Intent(this, AboutMe.class);
			startActivity(aboutMe);
			break;
		case R.id.exit:
			finish();
			break;
		}
		return false;
	}

	
	public void onBackPressed(){
		
		dialogAlert();
	}
	
	private void dialogAlert() {
		
		//configure a dialog builder
		AlertDialog.Builder builder = new AlertDialog.Builder( this );
		builder.setMessage( "Are you sure you want to exit? ")
				.setTitle( " Alert ")
				.setNegativeButton("Yes, exit", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				})
				.setPositiveButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
		//create 
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.DisplayHealth:			
			setNotification();			
			StartListUp();
			break;
		case R.id.bSupportTeam:
			dialogAlert();
		case R.id.bReload:
			preferencesFetch();
			Initialize();
			RequestData(URL);
		}
	}

	private void setNotification() {
		
		/*
		 * Create notifications for the app in the action bar --in Beta stage
		 */
		NotificationManager nm;
		/*
		 * for generating unique ID for Notifications
		 */
		int uniqueID = 1394873;
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		//to clear the notification automatically once it is clicked on form the action bar menu
		nm.cancel( uniqueID);
		
		//create a pending intent link where the action message must direct to
		Intent intent = new Intent( this, ActionHealth.class );
		PendingIntent p = PendingIntent.getActivity( this, 0, intent, 0);
		
		//body and title of the message
		String body = "This is Actionogram health monitoring app. Thanks for your support.";
		String title = "Actionogram";
		
		//create a notification
		Notification n = new Notification(R.drawable.alarm, body, System.currentTimeMillis());
		n.setLatestEventInfo( this, title, body, p);
		n.defaults = Notification.DEFAULT_ALL;
		
		//notify the manager about it
		nm.notify( uniqueID, n);
	}

}
//protected final static String URL = "https://dl.dropboxusercontent.com/u/32902686/Actionogram/a2.json";
	//protected final static String URL ="https://dl.dropboxusercontent.com/u/32902686/Actionogram/ActionoGramPyEnd.json";
	//protected final static String URL = "http://dl.dropboxusercontent.com/u/32902686/Actionogram/ActionoGramPyEnd.json";
	//protected final static String URL =  "https://dl.dropboxusercontent.com/u/32902686/Actionogram/email_messages.json";
	//protected final static String URL = "https://dl.dropboxusercontent.com/u/32902686/Actionogram/emailaa.json";
	//protected final static String URL = "https://dl.dropboxusercontent.com/u/32902686/Actionogram/messageType.json";
	//protected final static String URL ="https://dl.dropboxusercontent.com/u/32902686/Actionogram/email%2Barrows%2Bmessage%2Bdictionary.json";
	//protected final static String URL ="https://dl.dropboxusercontent.com/u/32902686/Actionogram/email%2Barrows%2Bmessage%2Bdictionary%2Bstatuses.json";
	//protected final static String URL = "http://dl.dropbox.com/u/32902686/Actionogram/ActionogramLongJson.json";
	/*  <string-array name="values">
      <item >1</item>
      <item >2</item>
      <item >3</item>
      <item >4</item>
  </string-array>
  */