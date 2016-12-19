package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.BasketballApp;

import com.mycompany.myapp.domain.FavUser;
import com.mycompany.myapp.repository.FavUserRepository;

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
 * Test class for the FavUserResource REST controller.
 *
 * @see FavUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BasketballApp.class)
public class FavUserResourceIntTest {

    private static final Boolean DEFAULT_LIKED = false;
    private static final Boolean UPDATED_LIKED = true;

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Inject
    private FavUserRepository favUserRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFavUserMockMvc;

    private FavUser favUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FavUserResource favUserResource = new FavUserResource();
        ReflectionTestUtils.setField(favUserResource, "favUserRepository", favUserRepository);
        this.restFavUserMockMvc = MockMvcBuilders.standaloneSetup(favUserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FavUser createEntity(EntityManager em) {
        FavUser favUser = new FavUser()
                .liked(DEFAULT_LIKED)
                .time(DEFAULT_TIME);
        return favUser;
    }

    @Before
    public void initTest() {
        favUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createFavUser() throws Exception {
        int databaseSizeBeforeCreate = favUserRepository.findAll().size();

        // Create the FavUser

        restFavUserMockMvc.perform(post("/api/fav-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favUser)))
            .andExpect(status().isCreated());

        // Validate the FavUser in the database
        List<FavUser> favUserList = favUserRepository.findAll();
        assertThat(favUserList).hasSize(databaseSizeBeforeCreate + 1);
        FavUser testFavUser = favUserList.get(favUserList.size() - 1);
        assertThat(testFavUser.isLiked()).isEqualTo(DEFAULT_LIKED);
        assertThat(testFavUser.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    @Transactional
    public void createFavUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = favUserRepository.findAll().size();

        // Create the FavUser with an existing ID
        FavUser existingFavUser = new FavUser();
        existingFavUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFavUserMockMvc.perform(post("/api/fav-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingFavUser)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FavUser> favUserList = favUserRepository.findAll();
        assertThat(favUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFavUsers() throws Exception {
        // Initialize the database
        favUserRepository.saveAndFlush(favUser);

        // Get all the favUserList
        restFavUserMockMvc.perform(get("/api/fav-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(favUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].liked").value(hasItem(DEFAULT_LIKED.booleanValue())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))));
    }

    @Test
    @Transactional
    public void getFavUser() throws Exception {
        // Initialize the database
        favUserRepository.saveAndFlush(favUser);

        // Get the favUser
        restFavUserMockMvc.perform(get("/api/fav-users/{id}", favUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(favUser.getId().intValue()))
            .andExpect(jsonPath("$.liked").value(DEFAULT_LIKED.booleanValue()))
            .andExpect(jsonPath("$.time").value(sameInstant(DEFAULT_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingFavUser() throws Exception {
        // Get the favUser
        restFavUserMockMvc.perform(get("/api/fav-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFavUser() throws Exception {
        // Initialize the database
        favUserRepository.saveAndFlush(favUser);
        int databaseSizeBeforeUpdate = favUserRepository.findAll().size();

        // Update the favUser
        FavUser updatedFavUser = favUserRepository.findOne(favUser.getId());
        updatedFavUser
                .liked(UPDATED_LIKED)
                .time(UPDATED_TIME);

        restFavUserMockMvc.perform(put("/api/fav-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFavUser)))
            .andExpect(status().isOk());

        // Validate the FavUser in the database
        List<FavUser> favUserList = favUserRepository.findAll();
        assertThat(favUserList).hasSize(databaseSizeBeforeUpdate);
        FavUser testFavUser = favUserList.get(favUserList.size() - 1);
        assertThat(testFavUser.isLiked()).isEqualTo(UPDATED_LIKED);
        assertThat(testFavUser.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingFavUser() throws Exception {
        int databaseSizeBeforeUpdate = favUserRepository.findAll().size();

        // Create the FavUser

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFavUserMockMvc.perform(put("/api/fav-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(favUser)))
            .andExpect(status().isCreated());

        // Validate the FavUser in the database
        List<FavUser> favUserList = favUserRepository.findAll();
        assertThat(favUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFavUser() throws Exception {
        // Initialize the database
        favUserRepository.saveAndFlush(favUser);
        int databaseSizeBeforeDelete = favUserRepository.findAll().size();

        // Get the favUser
        restFavUserMockMvc.perform(delete("/api/fav-users/{id}", favUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FavUser> favUserList = favUserRepository.findAll();
        assertThat(favUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
