package uz.devops.productms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.devops.productms.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}