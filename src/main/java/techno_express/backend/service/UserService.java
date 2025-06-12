package techno_express.backend.service;
import jakarta.transaction.Transactional;
import org.apache.catalina.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import techno_express.backend.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import techno_express.backend.dto.UserRegisterDto;
import techno_express.backend.entity.UserInformation;
import techno_express.backend.repository.UserRepository;
import techno_express.backend.repository.UserInformationRepository;
import techno_express.backend.exception.UserException;

@Service
public class UserService { }
