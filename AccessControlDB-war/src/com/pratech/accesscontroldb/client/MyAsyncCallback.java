package com.pratech.accesscontroldb.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;

public class MyAsyncCallback<T> implements AsyncCallback<T> {

    private final AsyncCallback<T> asyncCallback;

    public MyAsyncCallback(AsyncCallback<T> asyncCallback) {
        this.asyncCallback = asyncCallback;
    }

    public void onFailure(Throwable caught) {
        if (caught instanceof StatusCodeException &&
        		((StatusCodeException)caught).getStatusCode() == 512) {
        	Window.alert("Su sesi\u00F3n ha expirado. \nPor favor ingrese nuevamente o actualice la p\u00e1gina.");
            return;
        }
        if (caught instanceof ACDBException) {
        	Window.alert(caught.getMessage());
            return;
        }

        asyncCallback.onFailure(caught);
    }

    public void onSuccess(T result) {
        asyncCallback.onSuccess(result);
    }

}