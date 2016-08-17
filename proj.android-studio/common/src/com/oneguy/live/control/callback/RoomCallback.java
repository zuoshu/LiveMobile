package com.oneguy.live.control.callback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oneguy.live.control.wsdl.OperationResult;
import com.oneguy.live.model.bean.Room;

/**
 * Created by ZuoShu on 6/29/16.
 */
public abstract class RoomCallback extends BaseCallback {

    protected abstract void onSuccess(Room room);

    @Override
    public void Completed(OperationResult result) {
        if (result.Exception != null) {
            onError(-1, result);
            return;
        }

        if (result.Result == null) {
            onError(-1, result);
            return;
        }

        if (result.Result != null) {
            String resultString = (String) result.Result;
            JSONObject jsonObject = JSON.parseObject(resultString);
            if (!jsonObject.containsKey("code")) {
                onError(-1, result);
                return;
            }

            int code = jsonObject.getIntValue("code");
            if (code != 0) {
                onError(code, result);
                return;
            }

            if (!jsonObject.containsKey("Room")) {
                onError(-1, result);
                return;
            }

            Room room = JSON.parseObject(jsonObject.getString("Room"), Room.class);
            onSuccess(room);
        }

        onFinish(result);
    }

    @Override
    protected void onSuccess(OperationResult result) {
    }

    @Override
    protected void onError(int code, OperationResult result) {

    }

    @Override
    protected void onFinish(OperationResult result) {

    }

    @Override
    public void Starting() {

    }
}
