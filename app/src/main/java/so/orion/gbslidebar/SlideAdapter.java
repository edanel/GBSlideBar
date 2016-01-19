package so.orion.gbslidebar;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import so.orion.slidebar.GBSlideBarAdapter;


/**
 * 项目名称：GBSlideBar
 * 类描述：
 * 创建人：Edanel
 * 创建时间：16/1/14 下午5:45
 * 修改人：Edanel
 * 修改时间：16/1/14 下午5:45
 * 修改备注：
 */
public class SlideAdapter implements GBSlideBarAdapter {


    protected StateListDrawable[] mItems;
    protected String[] content = new String[]{"Tag","More","Reject"};
    protected int[] textColor;

    public SlideAdapter(Resources resources, int[] items) {
        int size = items.length;
        mItems = new StateListDrawable[size];
        Drawable drawable;
        for (int i = 0; i < size; i++) {
            drawable = resources.getDrawable(items[i]);
            if (drawable instanceof StateListDrawable) {
                mItems[i] = (StateListDrawable) drawable;
            } else {
                mItems[i] = new StateListDrawable();
                mItems[i].addState(new int[] {}, drawable);
            }
        }
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public String getText(int position) {
        return content[position];
    }

    @Override
    public StateListDrawable getItem(int position) {
        return mItems[position];
    }

    @Override
    public int getTextColor(int position) {
        return textColor[position];
    }

    public void setTextColor(int[] color){
        textColor = color;
    }
}
