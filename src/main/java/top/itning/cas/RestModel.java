package top.itning.cas;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Rest 返回消息
 *
 * @author itning
 */
class RestModel<T> implements Serializable {
    private int code;
    private String msg;
    private T data;

    RestModel() {
    }

    private RestModel(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    RestModel(T data) {
        this(HttpStatus.OK.value(), "查询成功", data);
    }

    int getCode() {
        return code;
    }

    void setCode(int code) {
        this.code = code;
    }

    String getMsg() {
        return msg;
    }

    void setMsg(String msg) {
        this.msg = msg;
    }

    T getData() {
        return data;
    }

    void setData(T data) {
        this.data = data;
    }
}
