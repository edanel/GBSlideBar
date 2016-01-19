package so.orion.slidebar;

import android.graphics.drawable.StateListDrawable;

/**
 * 项目名称：GBSlideBar
 * 类描述：
 * 创建人：Edanel
 * 创建时间：16/1/14 下午5:16
 * 修改人：Edanel
 * 修改时间：16/1/14 下午5:16
 * 修改备注：
 */
public interface GBSlideBarAdapter {

    int getCount();
    String getText(int position);
    StateListDrawable getItem(int position);
    int getTextColor(int position);
}
