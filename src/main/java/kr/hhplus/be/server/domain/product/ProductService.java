package kr.hhplus.be.server.domain.product;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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

    public ProductInfo.OrderItem getOrderItems(ProductCommand.OrderItem command){
        Product product = productRepository.findById(command.getProductId())
                .orElseThrow(NoResultException::new);
        return ProductInfo.OrderItem.from(product);
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
