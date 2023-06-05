package uz.devops.productms.service;

import uz.devops.productms.domain.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product addProduct(Product product);

}
