package techno_express.backend.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import techno_express.backend.entity.CartItem;
import techno_express.backend.entity.UserInformation;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @EntityGraph(attributePaths = {
            "product", "product.seller", "product.seller.role"
    })
    List<CartItem> findAllByCart_Id(Long cartId);

    @Modifying
    @Query("DELETE FROM cart_items ci WHERE ci.cart.owner = :owner")
    void deleteByCartOwner(@Param("owner") UserInformation owner);
}
