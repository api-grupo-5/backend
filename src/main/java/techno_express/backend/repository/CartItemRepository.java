package techno_express.backend.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import techno_express.backend.entity.CartItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @EntityGraph(attributePaths = {
            "product", "product.seller", "product.seller.role"
    })
    List<CartItem> findAllByCartId(Long cartId);

    @Modifying
    @Query("DELETE FROM cart_items ci WHERE ci.cart.id = :cart_id")
    void deleteByCartId(@Param("cart_id") Long cart_id);

    @Query("SELECT ci FROM cart_items ci WHERE ci.cart.id = :cart_id AND ci.product.id = :product_id")
    Optional<CartItem> findByCartIdAndProductId(@Param("cart_id") Long cart_id,
                                                @Param("product_id") Long product_id);


    @Query("SELECT ci.product.id FROM cart_items ci WHERE ci.cart.id = :cart_id")
    List<Long> findAllProductIdsByCartId(@Param("cart_id") Long cart_id);

    @Modifying
    @Query("DELETE FROM cart_items ci WHERE ci.cart.id = :cart_id AND ci.product.id = :product_id")
    void deleteByCartIdAndProductId(Long cart_id, Long product_id);
}
