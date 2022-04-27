package vip.ylove.demo.client.common;

public class BaseResult<T> implements Result{

    /**
     * 状态编码 参考BaseResultCode
     */
    int code = -1;
    /**
     * 结果描述
     */
    String msg = "成功";
    /**
     * 数据集
     */
    T  data;
    /**
     * 时间戳
     */
    Long timestamp;

    public BaseResult() {
    }

    public BaseResult(int code, String msg, T data, Long timestamp) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.timestamp = timestamp;
    }

    /**
     * 初始化
     * @param code
     * @param msg
     * @param data
     * @param timestamp
     * @return
     */
    private BaseResult<T> init(int code, String msg, T data, Long timestamp){
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.timestamp = timestamp;
       return this;
    }
    /**
     * 响应成功 code = 0
     * @param msg
     * @param data
     * @return
     */
    public BaseResult<T> success( String msg, T data){
        return this.init(BaseResultCode.SUCCESS,msg,data, System.currentTimeMillis());
    }
    /**
     * 响应成功
     * @param code
     * @param data
     * @return
     */
    public BaseResult<T> success( int code, T data){
        return this.init(code,msg,data, System.currentTimeMillis());
    }
    /**
     * 响应成功 code = 0
     * @param data
     * @return
     */
    public BaseResult<T> success(T data){
        return this.init(BaseResultCode.SUCCESS,"成功",data, System.currentTimeMillis());
    }

    /**
     * 响应成功 code = 0
     * @param msg
     * @return
     */
    public BaseResult<T> success(String msg){
        return this.init(BaseResultCode.SUCCESS,msg,null, System.currentTimeMillis());
    }
    /**
     * 响应成功 code = 0
     * @return
     */
    public BaseResult<T> success(){
        return this.init(BaseResultCode.SUCCESS,null,null, System.currentTimeMillis());
    }

    /**
     * 响应失败 code = 1
     * @return
     */
    public BaseResult<T> fail(){
        return this.init(BaseResultCode.DEFAULT_FAIL,null,null, System.currentTimeMillis());
    }

    /**
     * 响应失败 code = 1
     * @param msg
     * @param data
     * @return
     */
    public BaseResult<T> fail( String msg, T data){
        return this.init(BaseResultCode.DEFAULT_FAIL,msg,data, System.currentTimeMillis());
    }
    /**
     * 响应失败
     * @param code
     * @param data
     * @return
     */
    public BaseResult<T> fail( int code, T data){
        return this.init(code,msg,data, System.currentTimeMillis());
    }
    /**
     * 响应失败 code = 1
     * @param data
     * @return
     */
    public BaseResult<T> fail(T data){
        return this.init(BaseResultCode.DEFAULT_FAIL,null,data, System.currentTimeMillis());
    }

    /**
     * 响应失败 code = 1
     * @param msg
     * @return
     */
    public BaseResult<T> fail(String msg){
        return this.init(BaseResultCode.DEFAULT_FAIL,msg,null, System.currentTimeMillis());
    }


    /**
     * 返回结果编码 集合
     */
    public interface BaseResultCode{
        /**
         * 请求参数错误信息
         */
        int PARAM_EXECPTION = -5;
        /**
         * 请求方法错误
         */
        int REQUEST_METHOD_EXECPTION = -4;
        /**
         * 成功
         */
        int SUCCESS = 0;
        /**
         * 默认失败
         */
        int DEFAULT_FAIL = 1;
        /**
         * 未登陆
         */
        int LOGIN_NOT_LOGIN = 401;
        /**
         * 登陆验证码失败
         */
        int LOGIN_FAIL_LOGIN = 402;
        /**
         * 无权限访问
         */
        int LOGIN_NOT_AUTH = 403;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }



    /**
     * 判断是否成功 code 0则成功
     * @return
     */
    public boolean isSuccess(){
        if(this.getCode() == BaseResultCode.SUCCESS){
            return true;
        }
        return false;
    }

    /*--------------------*/
    /**
     * 响应成功 code = 0
     * @param msg
     * @param data
     * @return
     */
    public static BaseResult success_( String msg, Object data){
        return new BaseResult(BaseResultCode.SUCCESS,msg,data, System.currentTimeMillis());
    }
    /**
     * 响应成功
     * @param code
     * @param data
     * @return
     */
    public static BaseResult success_( int code, Object data){
        return new BaseResult(code,"成功",data, System.currentTimeMillis());
    }
    /**
     * 响应成功 code = 0
     * @param data
     * @return
     */
    public static BaseResult success_(Object data){
        return new BaseResult(BaseResultCode.SUCCESS,"成功",data, System.currentTimeMillis());
    }

    /**
     * 响应成功 code = 0
     * @param msg
     * @return
     */
    public static BaseResult success_(String msg){
        return new BaseResult(BaseResultCode.SUCCESS,msg,null, System.currentTimeMillis());
    }
    /**
     * 响应成功 code = 0
     * @return
     */
    public static BaseResult success_(){
        return new BaseResult(BaseResultCode.SUCCESS,"成功",null, System.currentTimeMillis());
    }

    /**
     * 响应失败 code = 1
     * @return
     */
    public static BaseResult fail_(){
        return new BaseResult(BaseResultCode.DEFAULT_FAIL,null,null, System.currentTimeMillis());
    }

    /**
     * 响应失败 code = 1
     * @param msg
     * @param data
     * @return
     */
    public static BaseResult fail_( String msg, Object data){
        return new BaseResult(BaseResultCode.DEFAULT_FAIL,msg,data, System.currentTimeMillis());
    }
    /**
     * 响应失败
     * @param code
     * @param data
     * @return
     */
    public static BaseResult fail_( int code, Object data){
        return new BaseResult(code,null,data, System.currentTimeMillis());
    }
    /**
     * 响应失败
     * @param code
     * @param msg
     * @return
     */
    public static BaseResult fail_( int code, String msg){
        return new BaseResult(code,msg,null, System.currentTimeMillis());
    }
    /**
     * 响应失败 code = 1
     * @param data
     * @return
     */
    public static BaseResult fail_(Object data){
        return new BaseResult(BaseResultCode.DEFAULT_FAIL,null,data, System.currentTimeMillis());
    }

    /**
     * 响应失败 code = 1
     * @param msg
     * @return
     */
    public static BaseResult fail_(String msg){
        return new BaseResult(BaseResultCode.DEFAULT_FAIL,msg,null, System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }


}
