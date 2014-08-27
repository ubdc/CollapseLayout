package com.example.collapselayout;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.expendlayout.R;

public class Demo1 extends Activity {
	CollapseLayout collapseLayout;
	RadioGroup radioGroup;
	SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo1);
		pref = getSharedPreferences("collapse", Context.MODE_PRIVATE);
		collapseLayout = (CollapseLayout) findViewById(R.id.collapseLayout);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.startWidthClosed:
					pref.edit().putBoolean("startWidthClosed", true).commit();
					break;
				case R.id.startWidthOpened:
					pref.edit().putBoolean("startWidthClosed", false).commit();
					break;
				}
			}
		});
		if (pref.getBoolean("startWidthClosed", false)) {
			radioGroup.check(R.id.startWidthClosed);
		} else {
			radioGroup.check(R.id.startWidthOpened);
		}
	}
	
	public void openOrClose(View v) {
		switch (collapseLayout.getState()) {
		case CollapseLayout.STATE_CLOSE:
			collapseLayout.open();
			break;
		case CollapseLayout.STATE_OPEN:
			collapseLayout.close();
			break;
		}
	}
}
