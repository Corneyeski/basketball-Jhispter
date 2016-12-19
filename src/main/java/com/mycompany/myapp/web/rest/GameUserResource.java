package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.GameUser;

import com.mycompany.myapp.repository.GameUserRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing GameUser.
 */
@RestController
@RequestMapping("/api")
public class GameUserResource {

    private final Logger log = LoggerFactory.getLogger(GameUserResource.class);
        
    @Inject
    private GameUserRepository gameUserRepository;

    /**
     * POST  /game-users : Create a new gameUser.
     *
     * @param gameUser the gameUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gameUser, or with status 400 (Bad Request) if the gameUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/game-users")
    @Timed
    public ResponseEntity<GameUser> createGameUser(@RequestBody GameUser gameUser) throws URISyntaxException {
        log.debug("REST request to save GameUser : {}", gameUser);
        if (gameUser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("gameUser", "idexists", "A new gameUser cannot already have an ID")).body(null);
        }
        GameUser result = gameUserRepository.save(gameUser);
        return ResponseEntity.created(new URI("/api/game-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("gameUser", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /game-users : Updates an existing gameUser.
     *
     * @param gameUser the gameUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gameUser,
     * or with status 400 (Bad Request) if the gameUser is not valid,
     * or with status 500 (Internal Server Error) if the gameUser couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/game-users")
    @Timed
    public ResponseEntity<GameUser> updateGameUser(@RequestBody GameUser gameUser) throws URISyntaxException {
        log.debug("REST request to update GameUser : {}", gameUser);
        if (gameUser.getId() == null) {
            return createGameUser(gameUser);
        }
        GameUser result = gameUserRepository.save(gameUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("gameUser", gameUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /game-users : get all the gameUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of gameUsers in body
     */
    @GetMapping("/game-users")
    @Timed
    public List<GameUser> getAllGameUsers() {
        log.debug("REST request to get all GameUsers");
        List<GameUser> gameUsers = gameUserRepository.findAll();
        return gameUsers;
    }

    /**
     * GET  /game-users/:id : get the "id" gameUser.
     *
     * @param id the id of the gameUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gameUser, or with status 404 (Not Found)
     */
    @GetMapping("/game-users/{id}")
    @Timed
    public ResponseEntity<GameUser> getGameUser(@PathVariable Long id) {
        log.debug("REST request to get GameUser : {}", id);
        GameUser gameUser = gameUserRepository.findOne(id);
        return Optional.ofNullable(gameUser)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /game-users/:id : delete the "id" gameUser.
     *
     * @param id the id of the gameUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/game-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteGameUser(@PathVariable Long id) {
        log.debug("REST request to delete GameUser : {}", id);
        gameUserRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("gameUser", id.toString())).build();
    }

}
