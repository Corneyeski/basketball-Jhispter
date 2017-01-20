package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.DTO.JugadorDTO;
import com.mycompany.myapp.domain.FavUser;

import com.mycompany.myapp.domain.Player;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

/**
 * Spring Data JPA repository for the FavUser entity.
 */
@SuppressWarnings("unused")
public interface FavUserRepository extends JpaRepository<FavUser,Long> {

//    @Query("select favUser from FavUser favUser where favUser.user.login = ?#{principal.username}")
//    List<FavUser> findByUserIsCurrentUser();

    @Query("select favUser.player, count(favUser) from FavUser favUser " +
    "GROUP BY favUser.player order by count(favUser) desc")

    List<Object[]> findFavoritePlayers();

    @Query("select favUser.player, count(favUser) from FavUser favUser " +
        "GROUP BY favUser.player order by count(favUser) desc")

    List<Object[]> findFiveFavoritePlayers(Pageable pageable);

    @Query("select favUser.time from FavUser favUser" +
        "  where favUser.player = :player")

    List<ZonedDateTime> favoriteEvolutionPlayer(@Param("player")Player player);
}
