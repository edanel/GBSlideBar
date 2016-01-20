package so.orion.gbslidebar;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import so.orion.slidebar.GBSlideBar;
import so.orion.slidebar.GBSlideBarListener;


public class MainActivity extends AppCompatActivity {

    private GBSlideBar gbSlideBar;
    private SlideAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Log.i("edanelx",mAdapter.getCount()+"");


        gbSlideBar.setAdapter(mAdapter);

        gbSlideBar.setPosition(2);

        gbSlideBar.setOnGbSlideBarListener(new GBSlideBarListener() {
            @Override
            public void onPositionSelected(int position) {
                Log.d("edanelx","selected "+position);
            }
        });
    }
}
