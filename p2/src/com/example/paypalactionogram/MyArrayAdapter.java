package com.example.paypalactionogram;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyArrayAdapter extends ArrayAdapter<Node> {

	public Context context;
	public String[] values;
	public List<Node> items;
	public Node selectedNode;
	public int row;
	Button bLeft, bCenter, bRight;
	TextView tv;

	public MyArrayAdapter(Context context, int resource, List<Node> n) {
		super(context, resource, R.layout.row, n);
		this.context = context;
		this.row = resource;
		this.items = n;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// get a layout inflater service
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// attach the view to the service
		View rowView = inflater.inflate(R.layout.row, parent, false);

		bLeft = (Button) rowView.findViewById(R.id.bL);
		bCenter = (Button) rowView.findViewById(R.id.bC);
		bRight = (Button) rowView.findViewById(R.id.bR);
		tv = (TextView) rowView.findViewById(R.id.tvrow);

		// Change the icon for Windows and iPhone
		Log.v("component", items.toString());

		selectedNode = items.get(position);

		setRightArrowColors();
		setLeftArrowColors();

		// selectedNode = items.get(0);
		Log.v("component", selectedNode.getName());

		tv.setText(selectedNode.getName());		
		
		// tv.setBackgroundResource(R.color.mydark);

		// String s = selectedNode.component.toString();
		// bCenter.setText( selectedNode.getName() );
		return rowView;
	}

	private void setRightArrowColors() {
		// TODO Auto-generated method stub
		switch (Math.abs(selectedNode.outStatus)) {

		case 2: // red
			bRight.setBackgroundResource(R.drawable.red_right);
			break;

		case 1: // yellow
			bRight.setBackgroundResource(R.drawable.orange_right);
			break;
		case 0: // green
			bRight.setBackgroundResource(R.drawable.green_right);
			break;
		}
	}

	private void setLeftArrowColors() {
		// TODO Auto-generated method stub
		switch (Math.abs(selectedNode.inStatus)) {

		case 2: // red
			bLeft.setBackgroundResource(R.drawable.red_right);
			break;

		case 1: // yellow
			bLeft.setBackgroundResource(R.drawable.orange_right);
			break;
		case 0: // green
			bLeft.setBackgroundResource(R.drawable.green_right);
			break;
		}
	}
}
