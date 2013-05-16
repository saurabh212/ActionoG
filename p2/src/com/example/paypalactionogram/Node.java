package com.example.paypalactionogram;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Node implements Serializable {

	public String component;
	public Integer inStatus;
	public Integer outStatus;
	public ArrayList<Node> children;
	public Node parent;
	public ArrayList<String> message;
	public ArrayList<String> email;

	public Node createNode(JSONObject ob) {
		Node n = new Node();
		try {
			n.component = ob.getString("component");
			n.inStatus = Integer.valueOf(ob.getInt( "instatus"));
			n.outStatus = Integer.valueOf(ob.getInt( "outstatus"));

			/*
			 * Message Allocated here
			 */
			n.message = new ArrayList<String>();
			boolean flag = true;
			JSONArray messages = ob.getJSONArray("message");
			JSONObject messageEntry;
			String type = "";
			String name = "";
			int ctr = 1;
			
			if (messages != null) {
				for (int vip = 0; vip < messages.length(); vip++) {
					flag = false;
					messageEntry =  messages.getJSONObject( vip );
					type = messageEntry.getString("type");
					name = messageEntry.getString("name");
			
					
					JSONArray problems = messageEntry.getJSONArray("problem");					
					for (int ip = 0; ip < problems.length(); ip=ip+2)
						n.message.add( Integer.valueOf(ctr++)+ ":" + 
								" Parameter - " + problems.getString(ip).toString() +
								"\n  Type - " + type + "\n  Name - " + name );	
				}
			}

			if (flag) {
				n.message.add("healthy");
			}

			/*
			 * Email lists Allocated here
			 */
			n.email = new ArrayList<String>();
			JSONArray emails = ob.getJSONArray("email");
			for (int ip = 0; ip < emails.length(); ip++)
				n.email.add( emails.getString(ip).toString() );

			/*
			Log.v("messaegs", n.email.toString());
			Log.v("messaegs", n.message.toString());
			Log.v("status", n.status.toString());
			Log.v("component ", n.component);*/

			/*
			 * Children Allocated here
			 */
			n.children = new ArrayList<Node>();
			JSONArray children = ob.getJSONArray("children");
			for (int ip = 0; ip < children.length(); ip++)
				n.children.add(createNode(children.getJSONObject(ip)));
			

		} catch (JSONException ex) {
			ex.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return n;
	}

	public String getName() {
		return " " + component;
	}

}
