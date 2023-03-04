package space.jachen.gmall.product.BaseCategory3Test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import space.jachen.gmall.product.entity.BaseCategory3;
import space.jachen.gmall.product.service.BaseCategory3Service;
import space.jachen.gmall.product.service.BaseCategoryTestService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/3 19:36
 */
@SpringBootTest
@SuppressWarnings("all")
public class baseCategoryTestTest {

    @Autowired
    private BaseCategoryTestService baseCategoryTestService;

    /**
     * 根据三级分类Id集合查询
     */
    @Test
    public void test(){
        List<BaseCategory3> idList = baseCategoryTestService.findBaseCategory3ByCategory3IdList();

        System.out.println("idList = " + idList);
    }


}
