package kr.hhplus.be.server.domain.product;

import jakarta.persistence.NoResultException;
import kr.hhplus.be.server.application.salesStat.SalesStatProcessor;
import kr.hhplus.be.server.domain.redis.RedisCommonStore;
import kr.hhplus.be.server.domain.redis.RedisZSetStore;
import kr.hhplus.be.server.domain.salesStat.SalesStat;
import kr.hhplus.be.server.domain.salesStat.SalesStatRepository;
import kr.hhplus.be.server.domain.salesStat.TypedScore;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
//TODO ProductFacade 분리
public class ProductService {

    private final ProductRepository productRepository;
    private final SalesStatRepository salesStatRepository;
    private final RedisZSetStore redisZSetStore;
    private final RedisCommonStore redisCommonStore;

    public ProductService(
            ProductRepository productRepository,
            SalesStatRepository salesStatRepository,
            RedisZSetStore redisZSetStore,
            RedisCommonStore redisCommonStore) {
        this.productRepository = productRepository;
        this.salesStatRepository = salesStatRepository;
        this.redisZSetStore = redisZSetStore;
        this.redisCommonStore = redisCommonStore;
    }

    @Cacheable(value = "products:list", key = "'offset:' + #offset + ':limit:' + #limit")
    public ProductInfo.Products findAll(int offset, int limit){

        List<Product> all = productRepository.findAll(offset, limit);
        return ProductInfo.Products.fromList(all);
    }

    @Cacheable(value = "last3days", key = "'salesRank'")
    public ProductInfo.Ranks findLast3DaysRank(){
        LocalDate targetDate = LocalDate.now().minusDays(1);

        List<SalesStat> salesStats = salesStatRepository.findAllBySalesDate(targetDate, 3);
        List<Product> products = productRepository.findByIdIn(salesStats.stream()
                                                                .map(SalesStat::getProductId)
                                                                .collect(Collectors.toSet()));

        return ProductInfo.Ranks.of(salesStats, products);
    }

    @Cacheable(value = "realTime", key = "'salesRank'")
    public ProductInfo.Ranks findRealTimeRank(){
        String redisKey = SalesStatProcessor.getDailySalesReportKey(LocalDate.now());

        return redisCommonStore.hasKey(redisKey)
                ? fetchRankFromRedis(redisKey)
                : findLast3DaysRank();
    }


    public ProductInfo.Products findAll(Integer offset, Integer limit){

        List<Product> all = productRepository.findAll(offset, limit);
        return ProductInfo.Products.fromList(all);
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

    public ProductInfo.DecreaseStock decreaseStock(ProductCommand.DecreaseStock command){
        Product product = productRepository.findByIdWithPessimisticLock(command.getProductId())
                .orElseThrow(NoResultException::new);

        product.decreaseStock(command.getDeductedStock());
        productRepository.save(product);
        return ProductInfo.DecreaseStock.from(product);
    }

    private ProductInfo.Ranks fetchRankFromRedis(String key) {
        List<TypedScore<String>> typedScores = redisZSetStore.reverseRangeWithScores(key, 0, 2);

        if (typedScores.isEmpty()) {
            return findLast3DaysRank();
        }

        List<Long> productIds = extractProductIds(typedScores);
        List<Product> products = productRepository.findByIdIn(productIds);

        productIdsValidation(productIds, products);

        return ProductInfo.Ranks.of(typedScores, products, LocalDate.now());
    }

    private List<Long> extractProductIds(List<TypedScore<String>> typedScores) {
        return typedScores.stream()
                .map(TypedScore::value)
                .map(Long::parseLong)
                .toList();
    }

    private void productIdsValidation(List<Long> productIds, List<Product> products){
        // 조회된 Product ID 목록 추출
        Set<Long> foundIds = products.stream()
                .map(Product::getId)
                .collect(Collectors.toSet());

        // 누락된 ID 추출
        List<Long> missingIds = productIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        // 예외 처리
        if (!missingIds.isEmpty()) {
            throw new IllegalStateException("DB에 존재하지 않는 상품 ID: " + missingIds);
        }

    }
}
