package com.pratech.accesscontroldb.server;

import java.util.Map;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.user.rebind.rpc.ProxyCreator;
import com.google.gwt.user.rebind.rpc.SerializableTypeOracle;


public class MyProxyCreator extends ProxyCreator {

    private final String methodStrTemplate = "@Override\n"
            + "protected <T> com.google.gwt.http.client.Request doInvoke(ResponseReader responseReader, "
            + "String methodName, RpcStatsContext statsContext, String requestData, "
            + "com.google.gwt.user.client.rpc.AsyncCallback<T> callback) {\n"
            + "${method-body}" + "}\n";

    public MyProxyCreator(JClassType serviceIntf) {
        super(serviceIntf);
    }

    protected void generateProxyMethods(SourceWriter w,
            SerializableTypeOracle serializableTypeOracle, TypeOracle typeOracle,
            Map<JMethod, JMethod> syncMethToAsyncMethMap) {
        // generate standard proxy methods
        super.generateProxyMethods(w, serializableTypeOracle,
        		typeOracle, syncMethToAsyncMethMap);

        // generate additional method
        overrideDoInvokeMethod(w);
    }

    private void overrideDoInvokeMethod(SourceWriter w) {
        StringBuilder methodBody = new StringBuilder();
        methodBody
                .append("final com.google.gwt.user.client.rpc.AsyncCallback newAsyncCallback = new com.pratech.accesscontroldb.client.MyAsyncCallback(callback);\n");
        methodBody
                .append("return super.doInvoke(responseReader, methodName, statsContext, requestData, newAsyncCallback);\n");

        String methodStr = methodStrTemplate.replace("${method-body}",
                methodBody);
        w.print(methodStr);
    }

}