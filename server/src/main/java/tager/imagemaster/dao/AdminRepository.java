package tager.imagemaster.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tager.imagemaster.entity.user.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    boolean existsByKeyt(String keyt);

    Admin findByKeyt(String keyt);
}
