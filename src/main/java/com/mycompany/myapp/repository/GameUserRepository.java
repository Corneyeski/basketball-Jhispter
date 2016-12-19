package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.GameUser;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the GameUser entity.
 */
@SuppressWarnings("unused")
public interface GameUserRepository extends JpaRepository<GameUser,Long> {

    @Query("select gameUser from GameUser gameUser where gameUser.user.login = ?#{principal.username}")
    List<GameUser> findByUserIsCurrentUser();

}
