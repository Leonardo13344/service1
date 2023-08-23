package com.distribuida.repo;

import com.distribuida.db.Instrument;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class InstrumentRepository {

    @PersistenceContext(unitName = "restapi_PU")
    private EntityManager em;

    public List<Instrument> findAll() {
        return this.em.createQuery("SELECT i FROM Instrument i", Instrument.class).getResultList();
    }

    public Instrument findById(Integer id) {
        return this.em.find(Instrument.class, id);
    }

    public Instrument create(Instrument instrument) {
        this.em.persist(instrument);
        return instrument;
    }

    public Instrument update(Instrument instrument) {
        return this.em.merge(instrument);
    }

    public void delete(Integer id) {
        Instrument instrument = this.em.find(Instrument.class, id);
        this.em.remove(instrument);
    }

}
