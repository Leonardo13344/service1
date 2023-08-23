package com.distribuida.rest;

import com.distribuida.client.SingerInstrumentRestClient;
import com.distribuida.db.Singer;
import com.distribuida.dto.SingerDto;
import com.distribuida.repo.SingerRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;


import java.util.List;
import java.util.stream.Collectors;

@Path("/singers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Singleton
public class SingerRest {

    @Inject
    private SingerRepository sR;

    @Inject
    @RestClient
    SingerInstrumentRestClient clientSingerInstrument;

    @GET
    @Path("/instruments")
    public List<SingerDto> findAllInstrumentsOfSinger(){
        return sR.findAll()
                .stream()
                .map(obj ->{
                    SingerDto dto = new SingerDto();
                    dto.setId(obj.getId());
                    dto.setFirstName(obj.getFirstName());
                    dto.setLastName(obj.getLastName());
                    dto.setBirthDate(obj.getBirthDate());
                    dto.setVersion(obj.getVersion());
                    dto.setInstruments(clientSingerInstrument.findBySingerId(dto.getId()));
                    return dto;
                }).collect(Collectors.toList());
    }

    @GET
    public List<Singer> findAll(){
        return sR.findAll();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Integer id){
        var obj = sR.findById(id);
        if(obj == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(obj).build();
    }

    @POST
    public Response create(Singer singer){
        sR.create(singer);
        return Response.status(Response.Status.CREATED.getStatusCode(), "singer created").build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, Singer tmp){
        var obj = sR.findById(id);
        if(obj == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        obj.setFirstName(tmp.getFirstName());
        obj.setLastName(tmp.getLastName());
        obj.setBirthDate(tmp.getBirthDate());
        obj.setVersion(tmp.getVersion());
        sR.update(obj);
        return Response.ok(obj).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id){
        var obj = sR.findById(id);
        if(obj == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        sR.delete(id);
        return Response.ok(obj).build();
    }

}