package space.jachen.gmall.list.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import space.jachen.gmall.domain.list.Goods;

/**
 * @author JaChen
 * @date 2023/3/14 13:47
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
