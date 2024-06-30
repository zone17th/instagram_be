package org.app.instagram_be.repository;

import org.app.instagram_be.model.entities.Account;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends BaseRepository<Account, Long>{
    Optional<Account> findByEmailAndPassword(String email, String password);
}
