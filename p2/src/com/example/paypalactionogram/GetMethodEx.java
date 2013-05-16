package com.example.paypalactionogram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import java.net.URISyntaxException;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Paint.Align;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;


@SuppressLint("NewApi")
public class GetMethodEx extends AsyncTask< String, Integer, String > {
	
	String data;
	BufferedReader in;
	StringBuilder sb;
	Boolean iCanUse;
	int percent;	
	URI website;
	HttpClient client;
	ActionHealth activity;
	ProgressBar pg;
	
	
	public void setActivity( ActionHealth activity ){
		this.activity = activity;
		pg = activity.pgb;
	}
	
	protected void onPreExecute( ){
		pg.setVisibility( android.view.View.VISIBLE);
	}
	
	@Override
	protected String doInBackground( String... params ) {	
		
		publishProgress( 20 );
		return connectOverUrlAndObtainJsonString( params[0] );
	}
	
	protected void onProgressUpdate( Integer...progress ){
		pg.setProgress( progress[0] );
	}
	
	protected void onPostExecute( String result ) {
		if( result!= null && !result.equalsIgnoreCase("") ){
			activity.screenStatus.setVisibility(android.view.View.INVISIBLE );
			activity.InternetData = result;
			pg.setVisibility( android.view.View.GONE);
			activity.BuildNodes( );
			
			activity.Go.setVisibility( android.view.View.VISIBLE );
			activity.reLoad.setVisibility( android.view.View.VISIBLE );
		}
		else{
			activity.supportTeam.setVisibility( android.view.View.VISIBLE );
			activity.screenStatus.setText("Data Not Found. Please check url/ contact support team");
		}
		
	}
	private String connectOverUrlAndObtainJsonString( String para ) {
		client = new DefaultHttpClient( );		
		try {
			
			website = new URI( para );
			HttpGet request = new HttpGet( );
			request.setURI( website );
			HttpResponse response = client.execute( request );
			
			publishProgress ( 40 );
			in = new BufferedReader( new InputStreamReader( response.getEntity( ).getContent() ) );
			try {
				sb = new StringBuilder();
				String line;
				//String nl = System.getProperty("line separator");
				while ( ( line = in.readLine( ) ) != null ) 
				{	
					sb.append(line);
				}
				publishProgress ( 70 );
			} catch (Exception e) {
			} finally {
				data = sb.toString();
				publishProgress ( 100 );
				in.close();	
			}
			//throw new HttpResponseException(404, "OK");
			
		}
			//activity.screenStatus.setText( "Sorry! Server is either down or not reachable. Come back Later!" );
		 catch (URISyntaxException e) {
			//activity.screenStatus.setText( "Incorrect URL! Check in preference settings." );
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

}
//data = "{	\"component\": \"risk-chennai\",	\"status\": 0,	\"children\": [		{			\"component\": \"actions\",			\"status\": 0,			\"children\": [				{					\"component\": \"limitationlifecycleserv\",					\"status\": 0,					\"children\": []				},				{					\"component\": \"paymentserv\",					\"status\": 0,					\"children\": []				}			]		},		{			\"component\": \"resolutions\",			\"status\": 0,			\"children\": [				{					\"component\": \"ciproattack\",					\"status\": 0,					\"children\": []				},				{					\"component\": \"occ-attack\",					\"status\": 0,					\"children\": []				}			]		}	]}";
// Magic permissions
/*
 * StrictMode.ThreadPolicy policy = new StrictMode.
 * ThreadPolicy.Builder().permitAll().build();
 * StrictMode.setThreadPolicy(policy);
 */
/*
 * HttpClient client = new DefaultHttpClient(); URI website = new
 * URI("https://dev.paypal.com/wiki/General" + "/Intern2013ActionoGram");
 * HttpGet request = new HttpGet(); request.setURI(website); HttpResponse
 * response = client.execute(request);
 * 
 * in = new BufferedReader(new InputStreamReader
 * (response.getEntity().getContent()));
 */
/*
 * URL url = new URL("http://www.google.com"); HttpURLConnection urlConnection
 * =(HttpURLConnection) url.openConnection();
 * urlConnection.setRequestMethod("GET"); urlConnection.connect();
 * 
 * in =new BufferedReader(new
 * InputStreamReader(urlConnection.getInputStream()));
 */
/*
 * in = new BufferedReader(new FileReader(new File("" +
 * "C:\\Users\\samehrotra\\workspace\\" + "SaurabhMehrotra\\src\\com\\example\\"
 * + "saurabhmehrotra\\json.txt")));
 */
// in = new BufferedReader(new FileReader("json.txt"));

// in = new BufferedReader(new
// FileReader("C:\\Users\\samehrotra\\Desktop\\json.txt"));
/*
 * try{ sb = new StringBuilder(""); String l = ""; String nl =
 * System.getProperty("line separator");
 * 
 * while((l = in.readLine())!= null) { String tag="getmethodex"; Log.v(tag,
 * "in while loop"); sb.append(l+nl); } } catch(Exception e){ String
 * tag="try method is one"; Log.v(tag, "try"); } finally{
 * sb.append("hdsahf sduhfhfkjdsfjhdsfjhsafdjhhf"); data += sb.toString();
 * in.close();
 * 
 * }
 */
/*
 * String[] datatemp = {"{"+ "	\"component\": \"risk-chennai\","+
 * "	\"status\": 0,"+ "	\"children\": ["+ "		{"+
 * "			\"component\": \"actions\","+ "			\"status\": 0,"+
 * "		\"children\": ["+ "				{"+
 * "					\"component\": \"limitationlifecycleserv\","+
 * "					\"status\": 0,"+ "					\"children\": []"+ "				},"+ "				{"+
 * "					\"component\": \"paymentserv\","+ "					\"status\": 0,"+
 * "					\"children\": []"+ "				}"+ "			]"+ "		},"+ "		{"+
 * "			\"component\": \"resolutions\","+ "			\"status\": 0,"+
 * "			\"children\": ["+ "				{"+ "				\"component\": \"ciproattack\","+
 * "				\"status\": 0,"+ "					\"children\": []"+ "				},"+ "			{"+
 * "				\"component\": \"occ-attack\","+ "					\"status\": 0,"+
 * "					\"children\": []"+ "				}"+ "		]"+ "		}"+ "	] }" };
 */