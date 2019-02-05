package opp.dao;

import opp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    int countByOib(String oib);
    int countByEmail(String email);
    int countByUsername(String username);

    @Override
    void deleteById(Long id);

    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);

    boolean existsByOibAndIdNot(String jmbag, Long id);
    boolean existsByUsernameAndIdNot(String username, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByUsernameAndPasswordNot(String username, String password);
    boolean existsByUsername(String username);
}
