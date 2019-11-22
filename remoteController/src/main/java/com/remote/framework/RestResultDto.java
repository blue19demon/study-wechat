package com.remote.framework;

import java.io.Serializable;

/**
 * 
 * @author Mr.梓贤
 *
 */
public class RestResultDto implements Serializable {
    
    private static final long serialVersionUID = 827560194166970585L;
    
    private int code;
    
    private String message;
    
    private Object body;

    public static RestResultDto fail() {
        return new RestResultDto(500, "操作失败");
    }

    public static RestResultDto fail(String message) {
        return new RestResultDto(500, message);
    }

    public static RestResultDto fail(int code, String message) {
        return new RestResultDto(code, message);
    }
    
    public static RestResultDto succeed() {
        return new RestResultDto(200, "OK");
    }
    
    public static RestResultDto succeed(Object body) {
        return new RestResultDto(200, "OK", body);
    }

    public RestResultDto() {
    }

    public RestResultDto(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public RestResultDto(int code, String message, Object body) {
        this.code = code;
        this.message = message;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
    
    public boolean isSuccess() {
		return 200==this.code;
       
    }
}
