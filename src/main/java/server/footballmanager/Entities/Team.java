package server.footballmanager.Entities;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Set;

@Entity
public class Team {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @NotBlank(message = "Name is mandatory")
    private String name;
    private double accountSum;
    @DecimalMin(value = "0.0", message = "Commission value must be greater than 0 and less than 0.1")
    @DecimalMax(value = "0.1", message = "Commission value must be greater than 0 and less than 0.1")
    private double commission;
    @JsonManagedReference

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Player> players;

    public Team() {
    }

    public Team(String name, double accountSum, double commission) {
        this.name = name;
        this.accountSum = accountSum;
        this.commission = commission;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAccountSum() {
        return accountSum;
    }

    public void setAccountSum(double accountSum) {
        this.accountSum = accountSum;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double comm) {
        this.commission = comm;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", accountSum=" + accountSum +
                ", commission=" + commission +
                ", players=" + players +
                '}';
    }
}
