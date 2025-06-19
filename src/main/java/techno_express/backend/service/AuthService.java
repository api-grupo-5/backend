package techno_express.backend.service;
import jakarta.transaction.Transactional;
import org.apache.catalina.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import techno_express.backend.dto.AuthRequestDto;
import techno_express.backend.dto.AuthResponseDto;
import techno_express.backend.entity.Role;
import techno_express.backend.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import techno_express.backend.dto.UserRegisterDto;
import techno_express.backend.entity.UserInformation;
import techno_express.backend.repository.RoleRepository;
import techno_express.backend.repository.UserRepository;
import techno_express.backend.repository.UserInformationRepository;
import techno_express.backend.exception.UserException;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInformationRepository userInformationRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JavaMailSender mailSender; 

    @Autowired
    private PasswordEncoder encoder;

    @Transactional
    public void register(String request_id, UserRegisterDto userRegisterDto) {
        logger.info(request_id + " - registrando usuario '" + userRegisterDto.getUsername() + "'...");

        try{
            User checking_user = userRepository.findByUsername(userRegisterDto.getUsername());

            if(checking_user != null){
                logger.error(request_id + " - el usuario ya existe" );
                throw new UserException.AlreadyExists();
            }
        } catch (DataIntegrityViolationException e) {
            logger.error(request_id + " - error en los datos del usuario");
            throw new UserException.InvalidData();
        } catch (Exception e) {
            logger.error(request_id + " - error desconocido durante el registro: " + e.getMessage());
            throw e;
        }

        User user = new User();
        user.setUsername(userRegisterDto.getUsername());
        user.setPassword(encoder.encode(userRegisterDto.getPassword()));
        user.setRegistered_on(LocalDateTime.now());

        Role user_role = roleRepository.findById(1L).orElseThrow(() -> new RuntimeException("Rol por defecto no encontrado"));
        user.setRole(user_role);
        logger.info(request_id + " - guardando usuario en la tabla 'accounts'..." );

        try{
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            logger.error(request_id + " - error en los datos del usuario");
            throw new UserException.InvalidData();
        } catch (Exception e) {
            logger.error(request_id + " - error desconocido al guardar el usuario en la tabla 'accounts': " + e.getMessage());
            throw e;
        }

        logger.info(request_id + " - asignandole los datos de usuario correspondientes...");
        UserInformation userInformation = new UserInformation();
        userInformation.setUser(user);
        userInformation.setFirst_name(userRegisterDto.getFirst_name());
        userInformation.setLast_name(userRegisterDto.getLast_name());
        userInformation.setEmail(userRegisterDto.getEmail());
        userInformation.setPersonal_id(userRegisterDto.getPersonal_id());
        userInformation.setPhone(userRegisterDto.getPhone());
        userInformation.setAddress(userRegisterDto.getAddress());

        logger.info(request_id + " - guardando usuario en la tabla 'accounts_information'..." );
        try{
            userInformationRepository.save(userInformation);
        } catch (DataIntegrityViolationException e) {
            logger.error(request_id + " - error en los datos del usuario");
            throw new UserException.InvalidData();
        } catch (Exception e) {
            logger.error(request_id + " - error desconocido al guardar el usuario en la tabla 'accounts_information': " + e.getMessage());
            throw e;
        }
    }

    public String login(String request_id, AuthRequestDto authRequestDto) {
        String username = authRequestDto.getUsername();
        User user;

        try{
            logger.info(request_id + " - buscando usuario '" + username + "' en la base de datos...");
            user = userRepository.findByUsername(username);

            if(user == null){
                logger.error(request_id + " - el usuario no existe");
                throw new UserException.NotFound();
            }

        } catch (DataIntegrityViolationException e) {
            logger.error(request_id + " - los datos recibidos estan corrompidos: " + e.getMessage());
            throw new UserException.InvalidData();
        } catch (Exception e) {
            logger.error(request_id + " - error desconocido durante la obtencion: " + e.getMessage());
            throw e;
        }

        logger.info(request_id + " - validando credenciales del usuario con las recibidas...");
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            authRequestDto.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            logger.error(request_id + " - credenciales inválidas");
            throw new UserException.InvalidData();

        } catch(Exception e){
            logger.error(request_id + " - error desconocido durante la validacion: " + e.getMessage());
            throw new UserException.InvalidData(); // o un error genérico si querés ocultar detalles
        }

        logger.info(request_id + " - generando token para el usuario...");
        String token = jwtService.generateToken(user);
        logger.info(request_id + " - token: " + token);

        logger.info(request_id + " - asignando token...");
        AuthResponseDto response = new AuthResponseDto();
        response.setToken(token);

        return token;
    }

    public void sendRecoveryToken(String email) {
        logger.info("Iniciando envío de token de recuperación para: {}", email);

        Optional<UserInformation> infoOpt = userInformationRepository.findByEmail(email);
        if (infoOpt.isEmpty()) {
            logger.error("Email no registrado: {}", email);
            throw new RuntimeException("Email no registrado");
        }

        User user = infoOpt.get().getUser();
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);

        logger.info("Token de recuperación generado: {}", token);

        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@technoexpress.com");
        message.setTo(email);
        message.setSubject("Recuperación de contraseña");
        message.setText("Hola!\n\nPara restablecer tu contraseña, hacé clic en el siguiente enlace:\n"
                + resetLink + "\n\nEste enlace expirará en 30 minutos.\n\nSaludos,\nEl equipo de TechnoExpress");

        try {
            logger.info("Enviando email a {}", email);
            mailSender.send(message);
            logger.info("Email enviado correctamente.");
        } catch (Exception e) {
            logger.error("Error al enviar el email: {}", e.getMessage());
        }
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
            .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado");
        }

        String hashed = encoder.encode(newPassword);
        user.setPassword(hashed);

        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }

}
