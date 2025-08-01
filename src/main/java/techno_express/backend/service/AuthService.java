package techno_express.backend.service;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import techno_express.backend.dto.*;
import techno_express.backend.entity.OtpToken;
import techno_express.backend.entity.Role;
import techno_express.backend.entity.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import techno_express.backend.entity.UserInformation;
import techno_express.backend.repository.OtpTokenRepository;
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
    private OtpTokenRepository otpTokenRepository;

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
        Optional<User> checking_user;

        try{
            checking_user = userRepository.findByEmail(userRegisterDto.getUsername());
        } catch (DataIntegrityViolationException e) {
            logger.error(request_id + " - error en los datos del usuario");
            throw new UserException.InvalidData();
        } catch (Exception e) {
            logger.error(request_id + " - error desconocido durante el registro: " + e.getMessage());
            throw e;
        }

        if(checking_user.isPresent()){
            logger.error(request_id + " - el usuario ya existe" );
            throw new UserException.AlreadyExists();
        }

        // Get or create role
        Role userRole = null;
        try {
            if (userRegisterDto.getRole() != null && !userRegisterDto.getRole().isEmpty()) {
                logger.info(request_id + " - Buscando rol especificado: " + userRegisterDto.getRole());
                userRole = roleRepository.findByName(userRegisterDto.getRole());
                if (userRole == null) {
                    logger.error(request_id + " - Rol no encontrado: " + userRegisterDto.getRole());
                    throw new UserException.InvalidData();
                }
            } else {
                logger.info(request_id + " - Usando rol por defecto: USER");
                userRole = roleRepository.findByName("USER");
                if (userRole == null) {
                    // esto no deberia ir porque le agrega responsabilidades que no tendria que tener el registro, te lo dejo porque es práctico para testear pero en realidad no va
                    logger.info(request_id + " - Creando rol por defecto: USER");
                    userRole = new Role();
                    userRole.setName("USER");
                    userRole.setPermissions("READ");
                    userRole = roleRepository.save(userRole);
                }
            }
        } catch (Exception e) {
            logger.error(request_id + " - Error al manejar roles: ", e);
        }
        User user = new User();
        user.setPassword(encoder.encode(userRegisterDto.getPassword()));
        user.setEmail(userRegisterDto.getUsername());
        user.setRegistered_on(LocalDateTime.now());
        user.setRole(userRole);

        logger.info(request_id + " - guardando usuario en la tabla 'accounts'..." );
        try{
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            logger.error(request_id + " - error en los datos del usuario: " + e);
            throw new UserException.InvalidData();
        } catch (Exception e) {
            logger.error(request_id + " - error desconocido al guardar el usuario en la tabla 'accounts': " + e);
            throw e;
        }

        logger.info(request_id + " - asignandole los datos de usuario correspondientes...");
        UserInformation userInformation = new UserInformation();
        userInformation.setUser(user);
        userInformation.setEmail(userRegisterDto.getEmail());
        userInformation.setFirst_name(userRegisterDto.getFirst_name());
        userInformation.setLast_name(userRegisterDto.getLast_name());
        userInformation.setPersonal_id(userRegisterDto.getPersonal_id());
        userInformation.setPhone(userRegisterDto.getPhone());
        userInformation.setAddress(userRegisterDto.getAddress());

        logger.info(request_id + " - guardando usuario en la tabla 'accounts_information'..." );
        try{
            userInformationRepository.save(userInformation);
        } catch (DataIntegrityViolationException e) {
            logger.error(request_id + " - error en los datos del usuario: "+ e);
            throw new UserException.InvalidData();
        } catch (Exception e) {
            logger.error(request_id + " - error desconocido al guardar el usuario en la tabla 'accounts_information': " + e);
            throw e;
        }
    }

    public HashMap<String, Object> login(String request_id, AuthRequestDto authRequestDto) {
        String username = authRequestDto.getEmail();
        Optional<User> optionalUser;

        try{
            logger.info(request_id + " - buscando usuario '" + username + "' en la base de datos...");
            optionalUser = userRepository.findByEmail(username);
            if(optionalUser.isEmpty()){
                logger.error(request_id + " - el usuario no existe");
                throw new UserException.NotFound();
            }
        } catch (DataIntegrityViolationException e) {
            logger.error(request_id + " - los datos recibidos estan corrompidos: " + e);
            throw new UserException.InvalidData();
        } catch (Exception e) {
            logger.error(request_id + " - error desconocido durante la obtencion: " + e);
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
            logger.error(request_id + " - error desconocido durante la validacion: " + e);
            throw new UserException.InvalidData(); // o un error genérico si querés ocultar detalles
        }

        logger.info(request_id + " - generando token para el usuario...");
        User user = optionalUser.get();
        String token = jwtService.generateToken(user);

        logger.info(request_id + " - asignando token...");
        AuthResponseDto response = new AuthResponseDto();
        response.setToken(token);

        logger.info(request_id + " - actualizando ultimo inicio de sesion...");
        user.setLast_logged_in(LocalDateTime.now());
        userRepository.save(user);

        HashMap<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user.getId());
        result.put("role", user.getRole());
        return result;
    }

    public void sendRecoveryToken(String request_id, AuthForgotPasswordDto authForgotPasswordDto) {
        String email = authForgotPasswordDto.getEmail();
        logger.info(request_id + " - validando que exista el usuario: {}...", email);

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            logger.error(request_id + " - Email no registrado");
            throw new UserException.NotFound();
        }


        logger.info(request_id + " - validando si el usuario ya tiene un token generado");
        User user = optionalUser.get();
        Optional<OtpToken> checking_email = otpTokenRepository.findByUsername(user.getEmail());
        boolean create_new_token = true;
        boolean token_doesnt_exist = checking_email.isEmpty();
        String token = "";
        if (!token_doesnt_exist) {
            logger.error(request_id + " - el usuario ya tiene un token generado");

            logger.info(request_id + " - validando si el vencimiento del token que tiene es valido actualmente...");
            if (LocalDateTime.now().isBefore(checking_email.get().getExpiration())) {
                logger.info(request_id + " - el token sigue siendo valido actualmente");
                create_new_token = false;
                token = checking_email.get().getToken();
            } else{
                logger.info(request_id + " - el token está vencido, eliminando token viejo");
                otpTokenRepository.delete(checking_email.get());
            }
        }

        if ((!token_doesnt_exist && create_new_token) || (create_new_token)) {
            logger.info(request_id + " - generando token...");
            String username = user.getEmail();
            token = UUID.randomUUID().toString();
            LocalDateTime expiration = LocalDateTime.now().plusMinutes(30);

            OtpToken otp = new OtpToken();
            otp.setToken(token);
            otp.setExpiration(expiration);
            otp.setUser_id(user);
            otp.setUsername(username);
            otpTokenRepository.save(otp);

            logger.info("Token de recuperación generado: {}", token);
        }

        logger.info(request_id + " - generando mail de recuperación...");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@technoexpress.com");
        message.setTo(email);
        message.setSubject("Recuperación de contraseña");
        message.setText("Hola!\n\nPara restablecer tu contraseña, por favor ingresa el token:\n"
                + token + "\n\nEste enlace expirará en 30 minutos.\n\nSaludos,\nEl equipo de TechnoExpress");

        try {
            logger.info(request_id + " - Enviando email...");
            mailSender.send(message);
            logger.info(request_id + " - Email enviado correctamente.");
        } catch (Exception e) {
            logger.error(request_id + " - Error al enviar el email: {}", e);
        }
    }

    public void resetPassword(String request_id, AuthResetPasswordDto authResetPasswordDto) {
        String token = authResetPasswordDto.getToken();
        String newPassword = authResetPasswordDto.getPassword();

        logger.info(request_id + " - token recibido: " + token);
        logger.info(request_id + " - validando que el token exista en la base de datos...");
        Optional<OtpToken> otp = otpTokenRepository.findByToken(token);

        if (otp.isEmpty()){
            logger.error(request_id + " - token no encontrado");
            throw new UserException.InvalidOtpToken();
        }

        logger.info(request_id + " - validando que el token no este vencido...");
        if (LocalDateTime.now().isAfter(otp.get().getExpiration())) {
            logger.error(request_id + " - el token esta vencido");
            throw new UserException.ExpiredToken();
        }

        logger.info(request_id + " - actualizando datos del usuario...");
        Optional<User> user = userRepository.findByEmail(otp.get().getUsername());
        user.get().setPassword(encoder.encode(newPassword));
        userRepository.save(user.get());

        logger.info(request_id + " - eliminado token usado...");
        otpTokenRepository.delete(otp.get());
    }
}
