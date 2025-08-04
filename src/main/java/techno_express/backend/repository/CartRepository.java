package techno_express.backend.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import techno_express.backend.entity.Cart;
import techno_express.backend.entity.UserInformation;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @EntityGraph(attributePaths = {
            "items",
            "items.product",
            "items.product.seller"
    })
    Optional<Cart> findByOwner(UserInformation owner);

    @Modifying
    @Query("DELETE FROM carts c WHERE c.id = :cart_id")
    void deleteCartId(Long cart_id);
}
