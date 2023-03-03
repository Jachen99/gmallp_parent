package space.jachen.gmall.common.execption;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.common.result.ResultCodeEnum;

/**
 * 自定义全局异常类
 */
// callSuper=true：表示在生成equals()和hashCode()方法时，调用父类的equals()和hashCode()方法。
// 为了保证生成的equals()和hashCode()方法能够正确地处理父类的属性，避免出现错误。
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "自定义全局异常类")
public class GmallException extends RuntimeException {

    @ApiModelProperty(value = "异常状态码")
    private Integer code;

    /**
     * 通过状态码和错误消息创建异常对象
     * @param message  String
     * @param code  Integer
     */
    public GmallException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * 接收枚举类型对象
     * @param resultCodeEnum  ResultCodeEnum
     */
    public GmallException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "GmallException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
