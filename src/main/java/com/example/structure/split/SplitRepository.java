package com.example.structure.split;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SplitRepository extends JpaRepository<Split, Long> {
    @Query("SELECT s FROM Split s WHERE s.id = ?1")
    Split findSplitById(Long id);

    @Query("SELECT s FROM Split s WHERE s.userId = ?1")
    Optional<Set<Split>> findSplitbyUser(Long userId);
}
