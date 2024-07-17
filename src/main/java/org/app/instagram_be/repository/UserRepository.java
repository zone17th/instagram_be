package org.app.instagram_be.repository;

import org.app.instagram_be.model.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query("SELECT u from User u join u.account a where u.email = :name or u.phoneNumber = :name or a.userName = :name")
    Optional<User> findByEmailOrPhoneNumber(@Param("name") String userInput);
}
