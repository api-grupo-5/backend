package techno_express.backend.service;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import techno_express.backend.dto.AuthRequestDto;
import techno_express.backend.dto.AuthResponseDto;
import techno_express.backend.entity.User;
import techno_express.backend.entity.Role;

import java.time.LocalDateTime;

import techno_express.backend.dto.UserRegisterDto;
import techno_express.backend.entity.UserInformation;
import techno_express.backend.repository.UserRepository;
import techno_express.backend.repository.UserInformationRepository;
import techno_express.backend.repository.RoleRepository;
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
    private PasswordEncoder encoder;

    @Transactional
    public void register(String request_id, UserRegisterDto userRegisterDto) {
        logger.info(request_id + " - registrando usuario " + userRegisterDto.getUsername() + "...");

        try{
            User checking_user = userRepository.findByEmail(userRegisterDto.getEmail());

            if(checking_user != null){
                logger.error(request_id + " - Usuario ya existe: " + userRegisterDto.getEmail());
                throw new UserException.AlreadyExists();
            }
        } catch (DataIntegrityViolationException e) {
            logger.error(request_id + " - Error de integridad de datos: ", e);
            throw new UserException.InvalidData();
        } catch (Exception e) {
            logger.error(request_id + " - Error inesperado al verificar usuario: ", e);
            throw e;
        }

        // Get or create role
        Role userRole;
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
                    logger.info(request_id + " - Creando rol por defecto: USER");
                    userRole = new Role();
                    userRole.setName("USER");
                    userRole.setPermissions("READ");
                    userRole = roleRepository.save(userRole);
                }
            }
        } catch (Exception e) {
            logger.error(request_id + " - Error al manejar roles: ", e);
            throw e;
        }

        User user = new User();
        
        user.setPassword(encoder.encode(userRegisterDto.getPassword()));        
        user.setEmail(userRegisterDto.getEmail());
        user.setRegistered_on(LocalDateTime.now());
        user.setRole(userRole);
        logger.info(request_id + " - guardando usuario en la tabla 'accounts'..." );

        try{
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            logger.error(request_id + " - Error de integridad al guardar usuario: ", e);
            throw new UserException.InvalidData();
        } catch (Exception e) {
            logger.error(request_id + " - Error inesperado al guardar usuario: ", e);
            throw e;
        }

        logger.info(request_id + " - asignandole los datos de usuario correspondientes...");
        UserInformation userInformation = new UserInformation();
        userInformation.setUser(user);
        userInformation.setUsername(userRegisterDto.getUsername());
        userInformation.setFirst_name(userRegisterDto.getFirst_name());
        userInformation.setLast_name(userRegisterDto.getLast_name());
        userInformation.setPersonal_id(userRegisterDto.getPersonal_id());
        userInformation.setPhone(userRegisterDto.getPhone());
        userInformation.setAddress(userRegisterDto.getAddress());

        logger.info(request_id + " - guardando usuario en la tabla 'accounts_information'..." );
        try{
            userInformationRepository.save(userInformation);
        } catch (DataIntegrityViolationException e) {
            logger.error(request_id + " - Error de integridad al guardar información del usuario: ", e);
            throw new UserException.InvalidData();
        } catch (Exception e) {
            logger.error(request_id + " - Error inesperado al guardar información del usuario: ", e);
            throw e;
        }
    }

    public AuthResponseDto login(String request_id, AuthRequestDto authRequestDto) {
        String email = authRequestDto.getEmail();

        logger.info(request_id + " - autenticando usuario: " + email + "...");
        logger.info(request_id + " - email recibido: " + authRequestDto.getEmail());
        logger.info(request_id + " - password recibido: " + (authRequestDto.getPassword() != null ? "***" : "NULL"));
        
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDto.getEmail(),
                        authRequestDto.getPassword()
                )
        );

        try{
            User user = userRepository.findByEmail(email);

            if(user == null){
                throw new UserException.NotFound();
            }

            String token = jwtService.generateToken(user);
            logger.info(request_id + " - token: " + token);

            AuthResponseDto response = new AuthResponseDto();
            response.setToken(token);
            return response;
        } catch (DataIntegrityViolationException e) {
            throw new UserException.InvalidData();
        } catch (Exception e) {
            throw e;
        }
    }
}
