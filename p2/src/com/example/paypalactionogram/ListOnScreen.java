package com.example.paypalactionogram;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ListOnScreen extends Activity implements OnItemClickListener,
		OnItemLongClickListener {

	public Node passNode;
	public ArrayList<Node> passList, rootList;
	protected MyArrayAdapter adapter;
	ListView listView;
	public Integer level;
	public Integer[] positionRecord = new Integer[10];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// full screen
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// set layout xml file
		setContentView(R.layout.activity_health_display);
		// initialise and open the bundle data
		OpenBundlePassed();
		// set the list view
		ListViewSet();
	}

	private void OpenBundlePassed() {
		listView = (ListView) findViewById(R.id.mylist);
		Bundle basket = getIntent().getExtras();
		passList = (ArrayList<Node>) basket.getSerializable("passList");
		rootList = (ArrayList<Node>) basket.getSerializable("passList");
		level = 0;
	}

	protected void ListViewSet() {
		adapter = new MyArrayAdapter(this, R.id.tvrow, passList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// instantiate a new activity referring back to itself on click of a
		// particular node
		Toast.makeText(this,
				"Component " + passList.get(position).getName() + " clicked.",
				Toast.LENGTH_SHORT).show();

		/*
		 * Toast.makeText(this, "Component " + passList.get(position).toString()
		 * + " clicked.", Toast.LENGTH_SHORT).show();
		 */
		passNode = passList.get(position);
		positionRecord[level] = position;
		level += 1;

		try {
			if (passNode.children.size() != 0) {// if the selected node is type
												// domain
				passList = passNode.children;
				ListViewSet();
			} else {
				// else the selected domain is type component
				Intent componentMessage = new Intent(this,
						ComponentMessage.class);
				componentMessage.putExtra("passNode", passNode);
				startActivity(componentMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onBackPressed() {

		try {
			level -= 1;
			passList = rootList;
			if (level == -1) {
				super.onBackPressed();
			} else if (level == 0) {
				ListViewSet();
			} else {
				for (Integer i = 0; i < level; i++) {
					Log.d("index i ", i.toString());
					passList = passList.get(positionRecord[i]).children;
				}
				ListViewSet();
			}
		} catch (Exception e) {
			Intent ListUp = new Intent(this, ListOnScreen.class);
			ListUp.putExtra("passList", rootList);
			startActivity(ListUp);
			this.finish();
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View v, int position,
			long id) {
		// instantiate a new activity referring back to itself on click of a
		// particular node
		Toast.makeText(this,
				"Component " + passList.get(position).getName() + " clicked.",
				Toast.LENGTH_SHORT).show();

		/*
		 * Toast.makeText(this, "Component " + passList.get(position).toString()
		 * + " clicked.", Toast.LENGTH_SHORT).show();
		 */
		passNode = passList.get(position);
		positionRecord[level] = position;
		level += 1;

		try {
			if (passNode.children.size() != 0) {// if the selected node is type
												// domain
				passList = passNode.children;
				ListViewSet();
			} else {
				// else the selected domain is type component
				Intent componentMessage = new Intent(this,
						ComponentMessage.class);
				componentMessage.putExtra("passNode", passNode);
				startActivity(componentMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
