package com.sports.clip.sport;

import com.sports.clip.users.User;
import com.sports.clip.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class LoadDatabase {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("clientSecret:secret")
    private String clientSecret;
    @Autowired
    Environment env;

    @Bean
    CommandLineRunner initUsers(SportRepository sportRepository, UserRepository userRepository) {
        return args -> {
            System.out.println(env);
            try {

                if (!userRepository.existsByEmail("user@domain.com")) {

                    String email = "user@domain.com";
                    User.Role role = User.Role.USER;
                    String pwd = passwordEncoder.encode("user");
                    log.info("save {}", userRepository.save(new User(null, email, pwd, role)));

                    sportRepository.save(new Sport(null, "Socker"));
                    sportRepository.save(new Sport(null, "Cricket"));
                }
            } catch (Exception ex) {

            }
        };
    }

}
