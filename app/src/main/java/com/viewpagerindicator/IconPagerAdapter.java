package com.viewpagerindicator;

/**
 * Created by Anoa 34 on 06/10/2015.
 */
public interface IconPagerAdapter {

    int getIconResId(int index);

    // From PagerAdapter
    int getCount();
}
