package kr.hhplus.be.server.domain.product;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.domain.salesStat.SalesStat;
import kr.hhplus.be.server.domain.salesStat.SalesStatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
//TODO ProductFacade 분리
public class ProductService {

    private final ProductRepository productRepository;
    private final SalesStatRepository salesStatRepository;

    public ProductService(
            ProductRepository productRepository,
            SalesStatRepository salesStatRepository) {
        this.productRepository = productRepository;
        this.salesStatRepository = salesStatRepository;
    }

//    @Cacheable(value = "products:list", key = "'offset:' + #offset + ':limit:' + #limit")
    public ProductInfo.Products findAll(int offset, int limit){

        List<Product> all = productRepository.findAll(offset, limit);
        return ProductInfo.Products.fromList(all);
    }

//    @Cacheable(value = "products:rank", key = "'limit:' + #limit")
    public ProductInfo.Ranks findRankWithLimit(int limit){
        LocalDate targetDate = LocalDate.now().minusDays(1);

        List<SalesStat> salesStats = salesStatRepository.findAllBySalesDate(targetDate, limit);
        List<Product> products = productRepository.findByIdIn(salesStats.stream()
                                                                .map(SalesStat::getProductId)
                                                                .collect(Collectors.toSet()));

        return ProductInfo.Ranks.of(salesStats, products);
    }

    public ProductInfo.Products findByIdInWithPessimisticLock(List<Long> orderItemIds){
        List<Product> products = productRepository.findByIdInWithPessimisticLock(orderItemIds);
        return ProductInfo.Products.fromList(products);
    }

    public void deductOrderItemQuantity(ProductCommand.DeductStock command){
        List<Product> products = productRepository.findByIdIn(command.getQuantityMap().keySet());
        for (Product product : products) {
            int quantity = command.getQuantityMap().getOrDefault(product.getId(), 0);
            product.decreaseStock(quantity);
        }
    }

    public ProductInfo.Create create(ProductCommand.Create command){
        Product product = Product.create(command.getName(), command.getPrice(), command.getStock());
        productRepository.save(product);
        return ProductInfo.Create.from(product);
    }

    public ProductInfo.IncreaseStock increaseStock(ProductCommand.IncreaseStock command){
        Product product = productRepository.findByIdWithPessimisticLock(command.getProductId())
                .orElseThrow(NoResultException::new);

        product.increaseStock(command.getAdditionStock());
        productRepository.save(product);
        return ProductInfo.IncreaseStock.from(product);
    }

    @Transactional
    public ProductInfo.DecreaseStock decreaseStock(ProductCommand.DecreaseStock command){
        Product product = productRepository.findByIdWithPessimisticLock(command.getProductId())
                .orElseThrow(NoResultException::new);

        product.decreaseStock(command.getDeductedStock());
        productRepository.save(product);
        return ProductInfo.DecreaseStock.from(product);
    }
}
