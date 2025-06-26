package techno_express.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import techno_express.backend.entity.User;
import techno_express.backend.exception.UserException;
import techno_express.backend.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        try{
            logger.info("UserDetailsServiceImpl - buscando usuario con email: " + email);
            Optional<User> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isEmpty()) {
                throw new UsernameNotFoundException("Usuario no encontrado: " + email);
            }

            User user = optionalUser.get();
            logger.info("UserDetailsServiceImpl - usuario encontrado");
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    List.of(new SimpleGrantedAuthority(user.getRole().getName()))
            );

        } catch (DataIntegrityViolationException e) {
            throw new UserException.InvalidData();
        } catch (Exception e) {
            throw e;
        }
    }
}
