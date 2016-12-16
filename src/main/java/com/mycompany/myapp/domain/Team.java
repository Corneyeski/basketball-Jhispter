package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Team.
 */
@Entity
@Table(name = "team")
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "fundate")
    private LocalDate fundate;

    @OneToMany(mappedBy = "gameLocalTeam")
    @JsonIgnore
    private Set<Game> gameLocalTeams = new HashSet<>();

    @OneToMany(mappedBy = "gameVisitorTeam")
    @JsonIgnore
    private Set<Game> gameVisitorTeams = new HashSet<>();

    @OneToMany(mappedBy = "team")
    @JsonIgnore
    private Set<Player> players = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Team name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public Team city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getFundate() {
        return fundate;
    }

    public Team fundate(LocalDate fundate) {
        this.fundate = fundate;
        return this;
    }

    public void setFundate(LocalDate fundate) {
        this.fundate = fundate;
    }

    public Set<Game> getGameLocalTeams() {
        return gameLocalTeams;
    }

    public Team gameLocalTeams(Set<Game> games) {
        this.gameLocalTeams = games;
        return this;
    }

    public Team addGameLocalTeam(Game game) {
        gameLocalTeams.add(game);
        game.setGameLocalTeam(this);
        return this;
    }

    public Team removeGameLocalTeam(Game game) {
        gameLocalTeams.remove(game);
        game.setGameLocalTeam(null);
        return this;
    }

    public void setGameLocalTeams(Set<Game> games) {
        this.gameLocalTeams = games;
    }

    public Set<Game> getGameVisitorTeams() {
        return gameVisitorTeams;
    }

    public Team gameVisitorTeams(Set<Game> games) {
        this.gameVisitorTeams = games;
        return this;
    }

    public Team addGameVisitorTeam(Game game) {
        gameVisitorTeams.add(game);
        game.setGameVisitorTeam(this);
        return this;
    }

    public Team removeGameVisitorTeam(Game game) {
        gameVisitorTeams.remove(game);
        game.setGameVisitorTeam(null);
        return this;
    }

    public void setGameVisitorTeams(Set<Game> games) {
        this.gameVisitorTeams = games;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public Team players(Set<Player> players) {
        this.players = players;
        return this;
    }

    public Team addPlayer(Player player) {
        players.add(player);
        player.setTeam(this);
        return this;
    }

    public Team removePlayer(Player player) {
        players.remove(player);
        player.setTeam(null);
        return this;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Team team = (Team) o;
        if (team.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Team{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", city='" + city + "'" +
            ", fundate='" + fundate + "'" +
            '}';
    }
}
