package space.jachen.gmall.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import space.jachen.gmall.common.execption.GmallException;
import space.jachen.gmall.common.result.Result;

/**
 * 全局异常处理类
 *
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<Object> error(Exception e){
        e.printStackTrace();
        return Result.fail();
    }

    /**
     * 自定义异常处理方法
     * @param e GmallException
     *
     * @return  Result<Object>
     */
    @ExceptionHandler(GmallException.class)
    @ResponseBody
    public Result<Object> error(GmallException e){
        return Result.fail(e.getMessage());
    }
}
