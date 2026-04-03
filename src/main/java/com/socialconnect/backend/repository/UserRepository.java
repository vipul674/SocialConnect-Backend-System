package com.socialconnect.backend.repository;

import com.socialconnect.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameAndIdNot(String username, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    Page<User> findByIdNot(Long id, Pageable pageable);

    @Query("""
            select distinct u from User u
            left join UserPreference p on p.user.id = u.id
            where u.id <> :userId
              and (:preferredLocation is null or lower(p.preferredLocation) = lower(:preferredLocation))
              and (:interestToken is null or lower(p.userInterests) like lower(concat('%', :interestToken, '%')))
            """)
    Page<User> findRecommendationCandidates(
            @Param("userId") Long userId,
            @Param("preferredLocation") String preferredLocation,
            @Param("interestToken") String interestToken,
            Pageable pageable
    );
}
