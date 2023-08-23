package com.distribuida.rest;

import com.distribuida.client.SingerInstrumentRestClient;
import com.distribuida.db.Instrument;
import com.distribuida.dto.InstrumentDto;
import com.distribuida.repo.InstrumentRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@Path("/instruments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Singleton
public class InstrumentRest {

    @Inject
    private InstrumentRepository iR;

    @Inject
    @RestClient
    SingerInstrumentRestClient clientSingerInstrument;

    @GET
    @Path("/singers")
    public List<InstrumentDto> findAllSingersOfInstrument(){
        return iR.findAll()
                .stream()
                .map(obj ->{
                    InstrumentDto dto = new InstrumentDto();
                    dto.setId(Math.toIntExact(obj.getId()));
                    dto.setName(obj.getName());
                    dto.setSingers(clientSingerInstrument.findByInstrumentId(dto.getId()));
                    return dto;
                }).collect(Collectors.toList());
    }

    @GET
    public List<Instrument> findAll(){
        return iR.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Integer id){
        var instrument = iR.findById(id);
        if(instrument == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(instrument).build();
    }

    @POST
    public Response create(Instrument instrument){
        iR.create(instrument);
        return Response.ok(instrument).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, Instrument tmp){
        var instrumentAux = iR.findById(id);
        if(instrumentAux == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        instrumentAux.setName(tmp.getName());
        iR.update(instrumentAux);
        return Response.ok(instrumentAux).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id){
        var instrument = iR.findById(id);
        if(instrument == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        iR.delete(id);
        return Response.ok().build();
    }
}
