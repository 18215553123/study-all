package org.ty.rpc.sample160001.po;

/**
 * <dl>
 * <dt>org.ty.rpc.POJO</dt>
 * <dd>Description:RPC 响应封装</dd>
 * <dd>Copyright: Copyright (C) 2015</dd>
 * <dd>Company: </dd>
 * <dd>CreateDate: 2016/3/4</dd>
 * </dl>
 *
 * @author tangyun
 */
public class RpcResponse {
    private String requestId;
    private Throwable error;
    private Object result;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
