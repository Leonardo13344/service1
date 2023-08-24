package com.distribuida.repo;

import com.distribuida.db.Singer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


import java.util.List;

@ApplicationScoped
public class SingerRepository {

    @PersistenceContext(unitName = "servicio1")
    private EntityManager em;


    public List<Singer> findAll() {
        return this.em.createQuery("SELECT s FROM Singer s", Singer.class).getResultList();
    }

    public Singer findById(Integer id) {
        return this.em.find(Singer.class, id);
    }

    public Singer create(Singer singer) {
        this.em.persist(singer);
        return singer;
    }

    public Singer update(Singer singer) {
        return this.em.merge(singer);
    }

    public void delete(Integer id) {
        Singer singer = this.em.find(Singer.class, id);
        this.em.remove(singer);
    }


}