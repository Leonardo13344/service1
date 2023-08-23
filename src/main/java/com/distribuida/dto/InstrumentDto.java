package com.distribuida.dto;

import java.util.List;

public class InstrumentDto {
    private Integer id;
    private String name;
    private List<SingerDto> singers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SingerDto> getSingers() {
        return singers;
    }

    public void setSingers(List<SingerDto> singers) {
        this.singers = singers;
    }
}
