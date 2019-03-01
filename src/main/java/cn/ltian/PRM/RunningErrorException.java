package cn.ltian.PRM;

/**
 * @Title : RunningErrorException.java
 * @Package : com.huagao.officesys.core.exception
 * @Description: 运行时代码异常
 * @author ltian
 * @date 2018年7月2日
 * @version v1.0
 */
public class RunningErrorException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5415081256423675653L;

	public RunningErrorException() {
		super();
	}
	
	public RunningErrorException(Throwable cause) {
		super(cause);
		cause.printStackTrace();
	}
	
	public RunningErrorException(String message, Throwable cause) {
		super(message, cause);
		cause.printStackTrace();
	}
}
