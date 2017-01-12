package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.DTO.JugadorDTO;
import com.mycompany.myapp.domain.FavUser;

import com.mycompany.myapp.domain.Player;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.FavUserRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FavUser.
 */
@RestController
@RequestMapping("/api")
public class FavUserResource {

    private final Logger log = LoggerFactory.getLogger(FavUserResource.class);

    @Inject
    private FavUserRepository favUserRepository;
    @Inject
    private UserRepository userRepository;
    /**
     * POST  /fav-users : Create a new favUser.
     *
     * @param favUser the favUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new favUser, or with status 400 (Bad Request) if the favUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fav-users")
    @Timed
    public ResponseEntity<FavUser> createFavUser(@RequestBody FavUser favUser) throws URISyntaxException {
        log.debug("REST request to save FavUser : {}", favUser);
        if (favUser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("favUser", "idexists", "A new favUser cannot already have an ID")).body(null);
        }
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

       favUser.setUser(user);
       favUser.setTime(ZonedDateTime.now());

        FavUser result = favUserRepository.save(favUser);
        return ResponseEntity.created(new URI("/api/fav-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("favUser", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fav-users : Updates an existing favUser.
     *
     * @param favUser the favUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated favUser,
     * or with status 400 (Bad Request) if the favUser is not valid,
     * or with status 500 (Internal Server Error) if the favUser couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fav-users")
    @Timed
    public ResponseEntity<FavUser> updateFavUser(@RequestBody FavUser favUser) throws URISyntaxException {
        log.debug("REST request to update FavUser : {}", favUser);
        if (favUser.getId() == null) {
            return createFavUser(favUser);
        }
        FavUser result = favUserRepository.save(favUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("favUser", favUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fav-users : get all the favUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of favUsers in body
     */
    @GetMapping("/fav-users")
    @Timed
    public List<FavUser> getAllFavUsers() {
        log.debug("REST request to get all FavUsers");
        List<FavUser> favUsers = favUserRepository.findAll();
        return favUsers;
    }

    /**
     * GET  /fav-users/:id : get the "id" favUser.
     *
     * @param id the id of the favUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the favUser, or with status 404 (Not Found)
     */
    @GetMapping("/fav-users/{id}")
    @Timed
    public ResponseEntity<FavUser> getFavUser(@PathVariable Long id) {
        log.debug("REST request to get FavUser : {}", id);
        FavUser favUser = favUserRepository.findOne(id);
        return Optional.ofNullable(favUser)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /fav-users/:id : delete the "id" favUser.
     *
     * @param id the id of the favUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fav-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteFavUser(@PathVariable Long id) {
        log.debug("REST request to delete FavUser : {}", id);
        favUserRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("favUser", id.toString())).build();
    }

    @GetMapping("/top-players")
    @Timed
    public ResponseEntity<List<JugadorDTO>> getTopPlayers()
        throws URISyntaxException {

        log.debug("REST request to get TopPlayers");

        List<Object[]> topPlayers = favUserRepository.findFavoritePlayers();

        List<JugadorDTO> result = new ArrayList<>();

        topPlayers.forEach(
            topPlayer -> {
                JugadorDTO p = new JugadorDTO();
                p.setPlayer((Player) topPlayer[0]);
                p.setCount((Long) topPlayer[1]);

                result.add(p);
            }

        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/top-five-players")
    @Timed
    public ResponseEntity<List<JugadorDTO>> getFiveTopPlayers()
        throws URISyntaxException {

        log.debug("REST request to get TopPlayers");

        Pageable pageable = new PageRequest(0, 5);

        List<Object[]> topPlayers = favUserRepository.findFiveFavoritePlayers(pageable);

        List<JugadorDTO> result = new ArrayList<>();

        topPlayers.forEach(
            topPlayer -> {
                JugadorDTO p = new JugadorDTO();
                p.setPlayer((Player) topPlayer[0]);
                p.setCount((Long) topPlayer[1]);

                result.add(p);
            }

        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
