package tager.imagemaster.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tager.imagemaster.entity.user.PointEntry;

import java.util.List;

@Repository
public interface PointEntryRepository extends JpaRepository<PointEntry, Integer> {
    List<PointEntry> findByUserId(int userId);
}
