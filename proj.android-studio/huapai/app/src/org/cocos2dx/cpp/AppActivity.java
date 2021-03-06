/****************************************************************************
 Copyright (c) 2008-2010 Ricardo Quesada
 Copyright (c) 2010-2012 cocos2d-x.org
 Copyright (c) 2011      Zynga Inc.
 Copyright (c) 2013-2014 Chukong Technologies Inc.

 http://www.cocos2d-x.org

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ****************************************************************************/
package org.cocos2dx.cpp;

import org.cocos2dx.lib.Cocos2dxActivity;
import android.content.Intent;
import android.os.Bundle;
//import com.anysdk.framework.PluginWrapper;
import com.game.common.GameUtil;
import com.oneguy.live.view.activity.LiveHallActivity;

public class AppActivity extends Cocos2dxActivity {

    protected void onCreate(Bundle savedState)
    {
        super.onCreate(savedState);
//        PluginWrapper.init(this); // for plugins
        GameUtil.initGameModule(this, getContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//		PluginWrapper.onActivityResult(requestCode, resultCode, data);
        GameUtil.handleResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
//		PluginWrapper.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
//		PluginWrapper.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
//		PluginWrapper.onResume();
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
//		PluginWrapper.onNewIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    protected void onStop() {
//		PluginWrapper.onStop();
        super.onStop();
    }

    @Override
    protected void onRestart() {
//		PluginWrapper.onRestart();
        super.onRestart();
    }

    public static void Share() {
        Bundle data = new Bundle();
        data.putString("token", "abcdef");
        Intent intent = new Intent(AppActivity.getContext(), LiveHallActivity.class);
        intent.putExtras(data);
        AppActivity.getContext().startActivity(intent);
    }

    // this function is called from cocos
    public void goLive(String token){
        Bundle data = new Bundle();
        data.putString("token", "token");
        Intent intent = new Intent(AppActivity.getContext(), LiveHallActivity.class);
        intent.putExtras(data);
        AppActivity.getContext().startActivity(intent);
    }
}
