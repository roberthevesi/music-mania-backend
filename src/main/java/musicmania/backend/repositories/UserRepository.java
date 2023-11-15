package musicmania.backend.repositories;

import musicmania.backend.entities.User;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Optional;

@Repository
@Primary
public interface UserRepository extends JpaRepository<User, Long>, UserDetailsService {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByUsername(String username);
    default UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority("USER"))))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    Optional<User> findByUsername(String username);

}
