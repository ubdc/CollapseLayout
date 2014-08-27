package com.example.collapselayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.collapselayout.CollapseLayout.CollapseListener;
import com.example.collapselayout.CollapseLayout.Mode;
import com.example.collapselayout.CollapseLayout.Orientation;
import com.example.expendlayout.R;

public class MainActivity extends Activity {
	LinearInterpolator linearInterpolator = new LinearInterpolator();
	BounceInterpolator bounceInterpolator = new BounceInterpolator();
	AccelerateDecelerateInterpolator accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
	
	Toast toast;
	CollapseLayout el;
	RadioGroup modeGroup;
	RadioGroup interpolatorGroup;
	RadioGroup orientationGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		toast = Toast.makeText(this, "", Toast.LENGTH_LONG);
		el = (CollapseLayout) findViewById(R.id.el);
		el.setCollapseListener(new CollapseListener() {

			@Override
			public void onOpenComplete() {
				showToast("onOpenComplete");
			}

			@Override
			public void onCloseComplete() {
				showToast("onCloseComplete");
			}
			
		});
		modeGroup = (RadioGroup) findViewById(R.id.mode);
		interpolatorGroup = (RadioGroup) findViewById(R.id.interpolator);
		orientationGroup = (RadioGroup) findViewById(R.id.orientation);
	}
	
	private void showToast(String msg) {
		toast.setText(msg);
		toast.show();
	}
	
	public void showEffect(View v) {
		int modeId = modeGroup.getCheckedRadioButtonId();
		switch (modeId) {
		case R.id.fixedTop:
			el.setCollapseMode(Mode.FIXED_START);
			break;
		case R.id.fixedBottom:
			el.setCollapseMode(Mode.FIXED_END);
			break;
		}
		
		int interpolatorId = interpolatorGroup.getCheckedRadioButtonId();
		switch (interpolatorId) {
		case R.id.linear:
			el.setInterpolator(linearInterpolator);
			break;
		case R.id.bounce:
			el.setInterpolator(bounceInterpolator);
			break;
		case R.id.accelerate:
			el.setInterpolator(accelerateDecelerateInterpolator);
			break;
		}
		
		int orientationId = orientationGroup.getCheckedRadioButtonId();
		switch (orientationId) {
		case R.id.vertical:
			el.setCollapseOrientation(Orientation.VERTICAL);
			break;
		case R.id.horizontal:
			el.setCollapseOrientation(Orientation.HORIZONTAL);
			break;
		}
		
		int state = el.getState();
		switch (state) {
		case CollapseLayout.STATE_OPEN:
			el.close();
			break;
		case CollapseLayout.STATE_CLOSE:
			el.open();
			break;
		}
	}
}
