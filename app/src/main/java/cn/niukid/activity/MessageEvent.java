package cn.niukid.activity;

/**
 * Created by bill on 9/7/17.
 */

public class MessageEvent {
    public final static int LOADING_ERROR = -1;
    public final static int LOADING_START = 0;
    public final static int LOADING_FINISH = 1;

    public int type;
    public MessageEvent(int type){
        this.type = type;
    }

    /**
     * the code defined in ExceptionHandle
     * */
    public int code;
    /**
     * the message from ResponeThrowable defined in ExceptionHandle
     * */
    public String message;
    public MessageEvent(int code,String message){
        this.type = LOADING_ERROR;
        this.code = code;
        this.message = message;

    }

}
