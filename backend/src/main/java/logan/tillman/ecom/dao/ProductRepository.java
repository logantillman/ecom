package logan.tillman.ecom.dao;

import logan.tillman.ecom.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByReleaseDateBetween(OffsetDateTime start, OffsetDateTime end);
}
