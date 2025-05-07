package kr.hhplus.be.server.interfaces.api.product;

import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductApiController implements ProductApiSpec {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }


    @Override
    @GetMapping
    public ResponseEntity<ProductResponse.Products> products(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {

        ProductInfo.Products products = productService.findAll(offset, limit);
        return ResponseEntity.ok(ProductResponse.Products.from(products));
    }

    @Override
    @GetMapping("/ranks")
    public ResponseEntity<ProductResponse.Ranks> productsRank() {
        return null;
    }
}
