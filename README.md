CollapseLayout
=================
CollapseLayout can collapse/expand layout with smooth animation. 

Here is a gif showing the effect:

![demo gif](/images/demo.gif "Demo gif")


How to use
---------------

```xml
<com.example.collapselayout.CollapseLayout
	android:id="@+id/el"
	android:layout_width="match_parent"
	android:layout_height="wrap_content" >
	
	<LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical" >
        
        .........
        
</LinearLayout>
	
</com.example.collapselayout.CollapseLayout>
```

```java
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
```
```xml
<declare-styleable name="CollapseLayout">
	<attr name="collapseOrientation">
	    <enum name="vertical" value="0" />
	    <enum name="horizontal" value="1" />
	</attr>
	<attr name="collapseMode">
	    <enum name="fixed_end" value="0"/>
	    <enum name="fixed_start" value="1"/>
	</attr>
	<attr name="initialCollapseState">
	    <enum name="open" value="0"/>
	    <enum name="close" value="1"/>
	</attr>
	<attr name="collapseDuration" format="integer"/>
</declare-styleable>
```
