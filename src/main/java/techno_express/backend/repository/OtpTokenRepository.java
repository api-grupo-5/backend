package techno_express.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techno_express.backend.entity.OtpToken;

import java.util.Optional;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findByToken(String token);
}
