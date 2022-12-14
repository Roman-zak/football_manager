package server.footballmanager.DTO;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import server.footballmanager.Entities.Team;

public class PlayerDTO {
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Surname is mandatory")
    private String surname;
    @Min(0)
    private int monthExperience;
    @Min(0)
    private int yearOfBirth;
    @Min(0)
    private long team_id;

    public PlayerDTO(String name, String surname, int monthExperience, int yearOfBirth, long team_id) {
        this.name = name;
        this.surname = surname;
        this.monthExperience = monthExperience;
        this.yearOfBirth = yearOfBirth;
        this.team_id = team_id;
    }

    public PlayerDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getMonthExperience() {
        return monthExperience;
    }

    public void setMonthExperience(int monthExperience) {
        this.monthExperience = monthExperience;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public long getTeam_id() {
        return team_id;
    }

    public void setTeam_id(long team_id) {
        this.team_id = team_id;
    }
}
