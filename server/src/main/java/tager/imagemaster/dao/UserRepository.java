package tager.imagemaster.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tager.imagemaster.entity.user.User;
import tager.imagemaster.entity.user.UserType;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findById(int userId);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User findByEmail(String email);

    @Query(nativeQuery = true, value = "select * from user where (username = ?1 or email = ?1) and password = ?2")
    User findByUsernameOrEmailAndPassword(String username_email, String password);

    List<User> findByUserType(UserType userType);
}
