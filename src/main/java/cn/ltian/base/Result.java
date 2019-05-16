package cn.ltian.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

/**
 * @Title : Result.java
 * @Package : com.huagao.officesys.core.dto
 * @Description: 结果集对象
 * @author ltian
 * @date 2018年7月2日
 * @version v1.0
 */
public class Result implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6288374846131788743L;
    
    // 响应状态码
    protected String statusCode;
    
    // 是否操作成功
    protected boolean success;
    
    // 客户端返回消息
    protected String message;
    
    /**
     * 默认构造
     */
    public Result() {

    }
    
    public Result(String statusCode,  boolean success, String message) {
    	this.statusCode = statusCode;
    	this.success = success;
    	this.message = message;
    }
    
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String toJson() {
		Gson gson = new Gson();
        return gson.toJson(this);  
	}
	
	public String toJson2() {
		Gson gson = new GsonBuilder() 
//				.registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
//		        .setDateFormat("yyyy-MM-dd HH:mm:ss")
//		        .create();
				.serializeNulls()
				.create();
		return gson.toJson(this);  
	}
}
