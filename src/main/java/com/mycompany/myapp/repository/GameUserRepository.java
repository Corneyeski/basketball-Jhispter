package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Game;
import com.mycompany.myapp.domain.GameUser;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the GameUser entity.
 */
@SuppressWarnings("unused")
public interface GameUserRepository extends JpaRepository<GameUser,Long> {

    @Query("select gameUser from GameUser gameUser where gameUser.user.login = ?#{principal.username}")
    List<GameUser> findByUserIsCurrentUser();

    @Query("select AVG(gameUser.points) FROM GameUser gameUser where gameUser.game = :game ")

    Double gameAvg(@Param("game")Game game);

    @Query("select gameUser.game, AVG(gameUser.points) from GameUser gameUser" +
        " GROUP BY gameUser order by  AVG(gameUser.points) desc")

    List<Object[]> fiveFavoriteGames(Pageable pageable);

//    @Query("select favUser.player, count(favUser) from FavUser favUser " +
//        "GROUP BY favUser.player order by count(favUser) desc")

}
