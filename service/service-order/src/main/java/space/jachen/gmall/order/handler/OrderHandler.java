package space.jachen.gmall.order.handler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.jachen.gmall.order.service.OrderService;

/**
 * @author JaChen
 * @date 2023/3/21 21:17
 */
@Component
public class OrderHandler {

    @Autowired
    private OrderService orderService;

    /**
     * 取消订单的handler方法
     *
     * @param orderId  orderId
     * @return  ReturnT<String>
     */
    @XxlJob(value = "OrderHandler",init = "init",destroy = "destroy")
    public ReturnT<String> execute(Long orderId){
        System.out.println("execute方法执行了 ====》 ");
        // 动态新增任务 需要对admin进行二次开发 新增动态接口
        // 待处理......
        // 处理业务
        orderService.execExpiredOrder(orderId);

        return ReturnT.SUCCESS;
    }
    private void init(){
        System.out.println("OrderHandler init >>>>> " + true);
        String jobParam = XxlJobHelper.getJobParam();
        System.out.println("jobParam = " + jobParam);
    }
}
