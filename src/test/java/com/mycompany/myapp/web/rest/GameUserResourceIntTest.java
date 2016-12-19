package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BasketballApp;

import com.mycompany.myapp.domain.GameUser;
import com.mycompany.myapp.repository.GameUserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GameUserResource REST controller.
 *
 * @see GameUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BasketballApp.class)
public class GameUserResourceIntTest {

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Inject
    private GameUserRepository gameUserRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restGameUserMockMvc;

    private GameUser gameUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GameUserResource gameUserResource = new GameUserResource();
        ReflectionTestUtils.setField(gameUserResource, "gameUserRepository", gameUserRepository);
        this.restGameUserMockMvc = MockMvcBuilders.standaloneSetup(gameUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameUser createEntity(EntityManager em) {
        GameUser gameUser = new GameUser()
                .points(DEFAULT_POINTS)
                .time(DEFAULT_TIME);
        return gameUser;
    }

    @Before
    public void initTest() {
        gameUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createGameUser() throws Exception {
        int databaseSizeBeforeCreate = gameUserRepository.findAll().size();

        // Create the GameUser

        restGameUserMockMvc.perform(post("/api/game-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gameUser)))
            .andExpect(status().isCreated());

        // Validate the GameUser in the database
        List<GameUser> gameUserList = gameUserRepository.findAll();
        assertThat(gameUserList).hasSize(databaseSizeBeforeCreate + 1);
        GameUser testGameUser = gameUserList.get(gameUserList.size() - 1);
        assertThat(testGameUser.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testGameUser.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    @Transactional
    public void createGameUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gameUserRepository.findAll().size();

        // Create the GameUser with an existing ID
        GameUser existingGameUser = new GameUser();
        existingGameUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameUserMockMvc.perform(post("/api/game-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingGameUser)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<GameUser> gameUserList = gameUserRepository.findAll();
        assertThat(gameUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllGameUsers() throws Exception {
        // Initialize the database
        gameUserRepository.saveAndFlush(gameUser);

        // Get all the gameUserList
        restGameUserMockMvc.perform(get("/api/game-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))));
    }

    @Test
    @Transactional
    public void getGameUser() throws Exception {
        // Initialize the database
        gameUserRepository.saveAndFlush(gameUser);

        // Get the gameUser
        restGameUserMockMvc.perform(get("/api/game-users/{id}", gameUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gameUser.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.time").value(sameInstant(DEFAULT_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingGameUser() throws Exception {
        // Get the gameUser
        restGameUserMockMvc.perform(get("/api/game-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGameUser() throws Exception {
        // Initialize the database
        gameUserRepository.saveAndFlush(gameUser);
        int databaseSizeBeforeUpdate = gameUserRepository.findAll().size();

        // Update the gameUser
        GameUser updatedGameUser = gameUserRepository.findOne(gameUser.getId());
        updatedGameUser
                .points(UPDATED_POINTS)
                .time(UPDATED_TIME);

        restGameUserMockMvc.perform(put("/api/game-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGameUser)))
            .andExpect(status().isOk());

        // Validate the GameUser in the database
        List<GameUser> gameUserList = gameUserRepository.findAll();
        assertThat(gameUserList).hasSize(databaseSizeBeforeUpdate);
        GameUser testGameUser = gameUserList.get(gameUserList.size() - 1);
        assertThat(testGameUser.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testGameUser.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingGameUser() throws Exception {
        int databaseSizeBeforeUpdate = gameUserRepository.findAll().size();

        // Create the GameUser

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGameUserMockMvc.perform(put("/api/game-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gameUser)))
            .andExpect(status().isCreated());

        // Validate the GameUser in the database
        List<GameUser> gameUserList = gameUserRepository.findAll();
        assertThat(gameUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGameUser() throws Exception {
        // Initialize the database
        gameUserRepository.saveAndFlush(gameUser);
        int databaseSizeBeforeDelete = gameUserRepository.findAll().size();

        // Get the gameUser
        restGameUserMockMvc.perform(delete("/api/game-users/{id}", gameUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GameUser> gameUserList = gameUserRepository.findAll();
        assertThat(gameUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
