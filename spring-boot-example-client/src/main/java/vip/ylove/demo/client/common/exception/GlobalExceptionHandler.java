package vip.ylove.demo.client.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import vip.ylove.demo.client.common.BaseResult;
import vip.ylove.sdk.exception.StException;

/**
 * Created by liuruijie on 2016/12/28.
 * 全局异常处理，捕获所有Controller中抛出的异常。
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	//处理自定义的异常
	@ExceptionHandler(BaseException.class)
	@ResponseBody
	public BaseResult customHandler(BaseException e){
		return BaseResult.fail_(e.getCode(),e.getMessage());
	}

	//加密异常抛出
	@ExceptionHandler(value = StException.class)
	@ResponseBody
	public BaseResult StException(StException e){
		return BaseResult.fail_(e.getCode(),e.getMessage());
	}


	/**
	 * 请求参方法错误自定义响应结果
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
	@ResponseBody
	public BaseResult validException(HttpRequestMethodNotSupportedException e) {
		return BaseResult.fail_(BaseResult.BaseResultCode.REQUEST_METHOD_EXECPTION,"认真阅读api,请求的method是不对的,"+e.getMessage());
	}

	/**
	 * 请求参数错误自定义响应结果
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	@ResponseBody
	public BaseResult validException(HttpMessageNotReadableException e) {
		return BaseResult.fail_(BaseResult.BaseResultCode.PARAM_EXECPTION,"请求方式错误,请认真阅读api,猜测请求接口post json请求,但你的请求参数不对");
	}

	/**
	 * 请求参数错误自定义响应结果
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = BindException.class)
	@ResponseBody
	public BaseResult validException2(BindException e) {
		String msg = e.getBindingResult().getFieldError().getDefaultMessage();
		return BaseResult.fail_(BaseResult.BaseResultCode.PARAM_EXECPTION,msg);
	}


	/**
	 * 请求参数错误自定义响应结果 使用验证器验证参数时生效
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	@ResponseBody
	public BaseResult validException(MethodArgumentNotValidException e) {
		//验证post请求的参数合法性
		MethodArgumentNotValidException notValidException = e;
		String msg = notValidException.getBindingResult().getFieldError().getDefaultMessage();
		return BaseResult.fail_(BaseResult.BaseResultCode.PARAM_EXECPTION,msg);
	}


	/**
	 * 未登陆验证
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = NotLoginException.class)
	@ResponseBody
	public BaseResult LoginException(NotLoginException e) {
		return BaseResult.fail_(BaseResult.BaseResultCode.LOGIN_NOT_LOGIN,e.getMessage());
	}



	//其他未处理的异常
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Object exceptionHandler(Exception e){
		e.printStackTrace();
		return BaseResult.fail_("系统错误",e.getMessage());
	}
}
