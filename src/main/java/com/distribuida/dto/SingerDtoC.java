package com.distribuida.dto;

import java.time.LocalDate;
import java.util.List;

public class SingerDtoC {

    private Integer id;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private Integer version;

    private List<Integer> instrumentsId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<Integer> getInstrumentsId() {
        return instrumentsId;
    }

    public void setInstrumentsId(List<Integer> instrumentsId) {
        this.instrumentsId = instrumentsId;
    }
}
