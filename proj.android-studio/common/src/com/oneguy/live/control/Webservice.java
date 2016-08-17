package com.oneguy.live.control;

import com.game.common.BuildConfig;
import com.oneguy.live.Constants;
import com.oneguy.live.control.callback.ItemListCallback;
import com.oneguy.live.control.callback.RoomCallback;
import com.oneguy.live.control.wsdl.lemoninfo;

/**
 * Created by ZuoShu on 6/29/16.
 */
public class Webservice {
    private static Webservice instance;

    public static synchronized Webservice getInstance() {
        if (instance == null) {
            instance = new Webservice();
        }
        return instance;
    }

    public void getCurrentRoom(String token, RoomCallback callback) {
        lemoninfo lemoninfo = new lemoninfo();
        lemoninfo.enableLogging = BuildConfig.DEBUG;
        lemoninfo.setCallback(callback);
        lemoninfo.liveoptAsync(token, Constants.OPT_GET_LIVE_ROOM, "");
    }

    public void getFutureItems(String token, ItemListCallback callback) {
        lemoninfo lemoninfo = new lemoninfo();
        lemoninfo.enableLogging = BuildConfig.DEBUG;
        lemoninfo.setCallback(callback);
        lemoninfo.liveoptAsync(token, Constants.OPT_GET_FUTURE_ITEM, "");
    }


}
