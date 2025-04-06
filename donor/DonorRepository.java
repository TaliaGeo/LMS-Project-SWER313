package com.example.lms.donor;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lms.user.User;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {
    Donor findByUserId(Long userId);
    Optional<Donor> findByUser_Username(String username);
    Optional<Donor> findByUser(User user);

}
