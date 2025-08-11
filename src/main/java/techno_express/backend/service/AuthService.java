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
import techno_express.backend.entity.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import techno_express.backend.repository.*;
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
    private CartRepository cartRepository;

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
    public void register(String request_id, AuthRegisterRequestDto authRegisterRequestDto) {
        logger.info(request_id + " - registrando usuario '" + authRegisterRequestDto.getUsername() + "'...");
        Optional<User> checking_user;

        try{
            checking_user = userRepository.findByEmail(authRegisterRequestDto.getUsername());
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

        User user = new User();
        user.setPassword(encoder.encode(authRegisterRequestDto.getPassword()));
        user.setEmail(authRegisterRequestDto.getUsername());
        user.setRegistered_on(LocalDateTime.now());
        Optional<Role> role = roleRepository.findById(1L); // 1 = USER
        user.setRole(role.get());

        logger.info(request_id + " - guardando usuario..." );
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
        userInformation.setEmail(authRegisterRequestDto.getEmail());
        userInformation.setFirst_name(authRegisterRequestDto.getFirst_name());
        userInformation.setLast_name(authRegisterRequestDto.getLast_name());
        userInformation.setPersonal_id(authRegisterRequestDto.getPersonal_id());
        userInformation.setPhone(authRegisterRequestDto.getPhone());
        userInformation.setAddress(authRegisterRequestDto.getAddress());

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

        logger.info(request_id + " - creandole un carrito vacio...");
        Cart cart = new Cart();
        cart.setCreated_on(LocalDateTime.now());
        cart.setOwner(userInformation);
    }

    public AuthLoginResponseDto login(String request_id, AuthLoginRequestDto authLoginRequestDto) {
        String username = authLoginRequestDto.getEmail();
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
                            authLoginRequestDto.getPassword()
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

        logger.info(request_id + " - actualizando ultimo inicio de sesion...");
        user.setLast_logged_in(LocalDateTime.now());
        userRepository.save(user);

        logger.info(request_id + " - devolviendo informacion...");
        AuthLoginResponseDto result = new AuthLoginResponseDto();
        result.setRole_id(user.getRole().getId());
        result.setToken(token);
        result.setUser_id(user.getId());
        return result;
    }

    public void forgot_password(String request_id, AuthForgotPasswordRequestDto authForgotPasswordRequestDto) {
        String email = authForgotPasswordRequestDto.getEmail();
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

        if (create_new_token) {
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
        message.setText("Hola!\n\n" +
                "Para restablecer tu contraseña, por favor ingresa el token:\n"
                + token +
                "\n\nEste enlace expirará en 30 minutos." +
                "\n\nSaludos," +
                "\nEl equipo de TechnoExpress"
        );

        try {
            logger.info(request_id + " - enviando email...");
            mailSender.send(message);
            logger.info(request_id + " - email enviado correctamente.");
        } catch (Exception e) {
            logger.error(request_id + " - error al enviar el email: {}", e.getMessage());
        }
    }

    public void reset_password(String request_id, AuthResetPassowrdRequestDto authResetPassowrdRequestDto) {
        String token = authResetPassowrdRequestDto.getToken();
        String newPassword = authResetPassowrdRequestDto.getPassword();

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
