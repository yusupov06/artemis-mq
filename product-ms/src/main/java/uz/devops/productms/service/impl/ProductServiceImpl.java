package uz.devops.productms.service.impl;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import uz.devops.productms.domain.Product;
import uz.devops.productms.repository.ProductRepository;
import uz.devops.productms.service.ProductService;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @JmsListener(destination = "get-product-by-id", selector = "JMSType='Long'")
    public Product getProductById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find product"));
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }
}
