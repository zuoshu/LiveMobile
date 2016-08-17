package com.oneguy.live.control.callback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oneguy.live.control.wsdl.OperationResult;
import com.oneguy.live.model.bean.Item;

import java.util.List;

/**
 * Created by ZuoShu on 6/29/16.
 */
public abstract class ItemListCallback extends BaseCallback {

    protected abstract void onSuccess(List<Item> itemList);

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

            if (!jsonObject.containsKey("list")) {
                onError(-1, result);
                return;
            }

            List<Item> itemList = JSON.parseArray(jsonObject.getString("list"), Item.class);
            onSuccess(itemList);
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
