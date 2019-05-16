package cn.ltian.base;

/**
 * @Title : JsonResult.java
 * @Package : com.huagao.officesys.core.dto
 * @Description: Result : JSON类型 响应的结果对象，用于服务端向客户端传递统一格式的数据
 * @author ltian
 * @date 2018年7月2日
 * @version v1.0
 */
public class JsonResult<T> extends Result {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 7567090878643356907L;
	
	// 客户端返回数据
    protected T data;
    
    /**
     * 默认构造
     */
    public JsonResult() {

    }
    
    public JsonResult(String statusCode,  boolean success, String message) {
    	this.statusCode = statusCode;
    	this.success = success;
    	this.message = message;
    }
    
    public JsonResult(String statusCode,  boolean success, String message, T data) {
    	this.statusCode = statusCode;
    	this.success = success;
    	this.message = message;
    	this.data = data;
    }
    
    public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
