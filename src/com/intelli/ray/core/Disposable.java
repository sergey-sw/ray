package com.intelli.ray.core;

/**
 * Indicates, that managed component should make some actions before it will be destroyed.
 * <p/>
 * Author: Sergey Saiyan sergey.sova42@gmail.com
 * Date: 30.06.2015 23:33
 */
public interface Disposable {

    void onDestroy();
}
