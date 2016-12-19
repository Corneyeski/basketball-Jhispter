package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FavUser;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FavUser entity.
 */
@SuppressWarnings("unused")
public interface FavUserRepository extends JpaRepository<FavUser,Long> {

    @Query("select favUser from FavUser favUser where favUser.user.login = ?#{principal.username}")
    List<FavUser> findByUserIsCurrentUser();

}
