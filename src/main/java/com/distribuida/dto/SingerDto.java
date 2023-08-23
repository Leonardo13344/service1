package com.distribuida.dto;

import java.util.Date;
import java.util.List;

public class SingerDto {

    private Integer id;

    private String firstName;

    private String lastName;

    private Date birthDate;

    private Integer version;

    private List<InstrumentDto> instruments;

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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<InstrumentDto> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<InstrumentDto> instruments) {
        this.instruments = instruments;
    }
}
