package com.example.collapselayout;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.collapselayout.CollapseLayout.State;
import com.example.expendlayout.R;

public class ListViewDemo extends ListActivity {
	List<String> titles = new ArrayList<>();
	List<String> contents = new ArrayList<>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		getListView().setAdapter(new MyAdapter());
	}
	
	private void init() {
		for (int i = 0; i < 100; i++) {
			titles.add("TITLE#" + i);
		}
		for (int i = 0; i < titles.size(); i++) {
			contents.add("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		}
	}

	class MyAdapter extends BaseAdapter {
		boolean[] openState = new boolean[titles.size()];

		@Override
		public int getCount() {
			return titles.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			Holder h = null;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
				h = new Holder();
				h.title = (TextView) convertView.findViewById(R.id.title);
				h.collapseContainer = (CollapseLayout) convertView.findViewById(R.id.collapseContainer);
				h.content = (TextView) convertView.findViewById(R.id.content);
				h.collapseContainer.setInterpolator(new BounceInterpolator());
				convertView.setTag(h);
			} else {
				h = (Holder) convertView.getTag();
			}
			h.title.setText(titles.get(position));
			final CollapseLayout collapseContianer = h.collapseContainer;
			if (openState[position]) {
				collapseContianer.setState(State.Open);
			} else {
				collapseContianer.setState(State.Close);
			}
			h.title.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (openState[position]) {
						collapseContianer.close();
					} else {
						collapseContianer.open();
					}
					openState[position] = !openState[position];
				}
			});
			h.content.setText(contents.get(position));
			return convertView;
		}
		
		class Holder {
			TextView title;
			CollapseLayout collapseContainer;
			TextView content;
		}
	}
}
