package com.example.collapselayout;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DemoList extends ListActivity {
	String[] arr = {
			"basic",
			"ListView demo",
	};
	
	String[] clazz = {
			"com.example.collapselayout.MainActivity",
			"com.example.collapselayout.ListViewDemo",
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getListView().setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		try {
			Intent intent = new Intent(this, Class.forName(clazz[position]));
			startActivity(intent);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
