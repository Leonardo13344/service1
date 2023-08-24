package com.distribuida.client;

import com.distribuida.dto.InstrumentDto;
import com.distribuida.dto.SingerDto;
import com.distribuida.dto.SingerInstrumentDto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/singer-instruments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = "singer-instrumentRestClient")
public interface SingerInstrumentRestClient {

    @GET
    List<SingerInstrumentDto> findAll();

    @GET
    @Path("/{id}")
    SingerInstrumentDto findById(@PathParam("id") Integer id);

    @GET
    @Path("/singer/{id}")
    List<InstrumentDto> findBySingerId(@PathParam("id") Integer id);

    @GET
    @Path("/instrument/{id}")
    List<SingerDto> findByInstrumentId(@PathParam("id") Integer id);

    @POST
    SingerInstrumentDto create(SingerInstrumentDto singerInstrumentDto);

    @PUT
    @Path("/{id}")
    SingerInstrumentDto update(@PathParam("id") Integer id, SingerInstrumentDto singerInstrumentDto);

    @DELETE
    @Path("/{id}")
    void delete(@PathParam("id") Integer id);

    @GET
    @Path("/{id}/{id2}")
    SingerInstrumentDto findByIds(@PathParam("id") Integer id, @PathParam("id2") Integer id2);

}
