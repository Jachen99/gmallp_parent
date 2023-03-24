package space.jachen.gmall.activity.config;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import space.jachen.gmall.activity.utils.CacheHelper;

/**
 * @author JaChen
 * @date 2023/3/24 19:52
 */
@Component
public class MessageReceive {

    /**
     * 监听redis发布订阅中的秒杀商品的状态
     * @param message
     */
    public void receiveMessage(String message){
        System.out.println("----------收到消息了message："+message);
        if(!StringUtils.isEmpty(message)) {
            /*
             消息格式
                skuId:0 表示没有商品
                skuId:1 表示有商品
             */
            // 因为传递过来的数据为 ""6:1""
            message = message.replaceAll("\"","");
            String[] split = StringUtils.split(message, ":");
            if (split == null || split.length == 2) {
                assert split != null;
                // 存入本地缓存
                CacheHelper.put(split[0], split[1]);
            }
        }
    }

}
