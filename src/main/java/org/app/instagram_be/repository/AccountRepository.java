package org.app.instagram_be.repository;

import org.app.instagram_be.model.entities.Account;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends BaseRepository<Account, Long>{
    Optional<Account> findByUserNameAndPassword(String username, String password);
    Optional<Account> findByUserEmailAndPassword(String email, String password);
    Optional<Account> findByUserPhoneNumberAndPassword(String phoneNumber, String password);

}
