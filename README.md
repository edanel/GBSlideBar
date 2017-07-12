# GBSlideBar 滑动选择控件
[项目不再维护,有需要的可自行根据需求对源码进行修改]
类似uber的滑动选择工具条

# 效果图

![animation](https://raw.githubusercontent.com/edanel/GBSlideBar/master/screenshot/preview-480.gif)

# 使用

```xml

	<so.orion.slidebar.GBSlideBar
        android:id="@+id/gbslidebar"
        android:layout_width="wrap_content"
        android:layout_height="100dp"
        android:layout_centerInParent="true"
        app:gbs_anchor_height="25dp"
        app:gbs_anchor_width="25dp"
        app:gbs_background="#e0e0e0"
        app:gbs_paddingBottom="65dp"
        app:gbs_placeholder_width="20dp"
        app:gbs_placeholder_height="20dp"
        app:gbs_paddingLeft="10dp"
        app:gbs_paddingRight="10dp"
        app:gbs_paddingTop="25dp"
        app:gbs_textSize="14sp"
        app:gbs_textColor="#666" />
        
```

```java
	private GBSlideBar gbSlideBar;
    private SlideAdapter mAdapter;
       gbSlideBar = (GBSlideBar) findViewById(R.id.gbslidebar);

        Resources resources = getResources();
        mAdapter = new SlideAdapter(resources, new int[]{
                R.drawable.btn_tag_selector,
                R.drawable.btn_more_selector,
                R.drawable.btn_reject_selector});
                
        mAdapter.setTextColor(new int[]{
                Color.GREEN,
                Color.BLUE,
                Color.RED
        });
        
        gbSlideBar.setAdapter(mAdapter);
        gbSlideBar.setPosition(2);
        gbSlideBar.setOnGbSlideBarListener(new GBSlideBarListener() {
            @Override
            public void onPositionSelected(int position) {
                Log.d("edanelx","selected "+position);
            }
        });
```

# 引用

```
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```
```
	dependencies {
	        compile 'com.github.edanel:GBSlideBar:0.5'
	}
```

# 其他

参考：[android-phased-seek-bar](https://github.com/ademar111190/android-phased-seek-bar)
