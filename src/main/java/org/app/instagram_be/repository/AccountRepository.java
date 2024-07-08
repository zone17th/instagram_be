package org.app.instagram_be.repository;

import org.app.instagram_be.model.entities.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends BaseRepository<Account, Long> {
    Optional<Account> findByUserNameAndPassword(String username, String password);

    Optional<Account> findByUserEmailAndPassword(String email, String password);

    Optional<Account> findByUserPhoneNumberAndPassword(String phoneNumber, String password);

    @Query(
            "SELECT a FROM Account a JOIN a.user u where (a.userName = :userInput or u.email = :userInput or u.phoneNumber = :userInput) and a.password = :password"
    )
    Optional<Account> findAccountByUserInput(@Param("userInput") String userInput, @Param("password") String password);


}
