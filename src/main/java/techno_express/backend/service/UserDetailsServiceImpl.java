package techno_express.backend.service;

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

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        try{
            User user = userRepository.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("Usuario no encontrado: " + username);
            }

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
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
