package ai.shopsense.product.repository;

import ai.shopsense.product.domain.ProductRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductRecord, UUID> {
    Page<ProductRecord> findByRetailerIgnoreCaseAndTitleContainingIgnoreCase(String retailer, String title, Pageable pageable);
}
