package com.distribuida.dto;

import java.util.List;

public class InstrumentDtoC {

    private Integer id;
    private String name;
    private List<Integer> singersId;

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

    public List<Integer> getSingersId() {
        return singersId;
    }

    public void setSingersId(List<Integer> singersId) {
        this.singersId = singersId;
    }
}
