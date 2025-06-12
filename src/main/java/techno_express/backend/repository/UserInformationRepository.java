package techno_express.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techno_express.backend.entity.UserInformation;

@Repository
public interface UserInformationRepository extends JpaRepository<UserInformation, Long> {}