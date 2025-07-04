package techno_express.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import techno_express.backend.dto.UserProfileDto;
import techno_express.backend.dto.UserProfileUpdateDto;
import techno_express.backend.entity.User;
import techno_express.backend.entity.UserInformation;
import techno_express.backend.repository.UserInformationRepository;
import techno_express.backend.repository.UserRepository;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInformationRepository userInformationRepository;

    public UserProfileDto getProfileByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        var info = user.getUserInformation();

        return new UserProfileDto(
            user.getUsername(),
            info != null ? info.getEmail() : null,
            info != null ? info.getFirst_name() : null,
            info != null ? info.getLast_name() : null,
            info != null ? info.getPhone() : null,
            info != null ? info.getAddress() : null
        );
    }

    public void updateCurrentUserProfile(UserProfileUpdateDto updatedInfo) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepository.findByUsername(username);

    if (user != null && user.getUserInformation() != null) {
        UserInformation info = user.getUserInformation();
        info.setFirst_name(updatedInfo.getFirstName());
        info.setLast_name(updatedInfo.getLastName());
        info.setPhone(updatedInfo.getPhone());
        info.setAddress(updatedInfo.getAddress());
        userInformationRepository.save(info);
    }
}
}
