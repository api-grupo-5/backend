package techno_express.backend.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import techno_express.backend.entity.User;
import java.util.ArrayList;
import techno_express.backend.dto.UserDto;
import techno_express.backend.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    public void register(UserDto dto) {
        User u = new User();
        u.setUsername(dto.getUsername());
        u.setPassword(encoder.encode(dto.getPassword()));
        repo.save(u);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = repo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Not found"));
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), user.getPassword(), new ArrayList<>()
        );
    }
}
