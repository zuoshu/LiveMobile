package com.oneguy.live.control.callback;

import com.oneguy.live.control.wsdl.IServiceEvents;
import com.oneguy.live.control.wsdl.OperationResult;

/**
 * Created by ZuoShu on 6/29/16.
 */
public abstract class BaseCallback implements IServiceEvents {
    protected abstract void onError(int code, OperationResult result);
    protected abstract void onSuccess(OperationResult result);
    protected abstract void onFinish(OperationResult result);

}
