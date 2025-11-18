package com.dentalclinic.dentalclinicapp.repository;

import com.dentalclinic.dentalclinicapp.entity.UserActivitySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivitySummaryRepository extends JpaRepository<UserActivitySummary, Long> {
    void deleteByUserId(Long userId);
}
