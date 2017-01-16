package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Game;

import com.mycompany.myapp.domain.Team;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Game entity.
 */
@SuppressWarnings("unused")
public interface GameRepository extends JpaRepository<Game,Long> {



}
