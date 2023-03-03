package space.jachen.gmall.product.BaseCategory3Test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import space.jachen.gmall.product.entity.BaseCategory3;
import space.jachen.gmall.product.service.BaseCategory3Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/3 19:36
 */
@SpringBootTest
public class IdTest {

    @Autowired
    private BaseCategory3Service baseCategory3Service;

    @Test
    public void test(){
        List<BaseCategory3> idList = baseCategory3Service.findBaseCategory3ByCategory3IdList();

        System.out.println("idList = " + idList);
    }


}
