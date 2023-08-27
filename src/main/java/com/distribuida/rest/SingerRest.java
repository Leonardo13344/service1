package com.distribuida.rest;

import com.distribuida.client.SingerInstrumentRestClient;
import com.distribuida.db.Instrument;
import com.distribuida.db.Singer;
import com.distribuida.dto.InstrumentDto;
import com.distribuida.dto.SingerDto;
import com.distribuida.dto.SingerDtoC;
import com.distribuida.dto.SingerInstrumentDto;
import com.distribuida.repo.InstrumentRepository;
import com.distribuida.repo.SingerRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
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

    @Inject
    private InstrumentRepository iR;

    private SingerDto mapSingerToDto(Singer singer) {
        SingerDto dto = new SingerDto();
        dto.setId(Math.toIntExact(singer.getId()));
        dto.setFirstName(singer.getFirstName());
        dto.setLastName(singer.getLastName());
        dto.setBirthDate(singer.getBirthDate());
        dto.setVersion(singer.getVersion());
        var instruments = clientSingerInstrument.findBySingerId(dto.getId());
        List<InstrumentDto> instrumentsDto = instruments.stream().map(ins -> {
            InstrumentDto instrumentDto = new InstrumentDto();
            instrumentDto.setId(ins.getInstrumentId());
            instrumentDto.setName(iR.findById(ins.getInstrumentId()).getName());
            return instrumentDto;
        }).collect(Collectors.toList());
        dto.setInstruments(instrumentsDto);
        return dto;
    }

    @GET
    @Timeout(4000)
    @Retry(maxRetries = 2)
    public List<SingerDto> findAllInstrumentsOfSinger() {
        return sR.findAll()
                .stream()
                .map(this::mapSingerToDto).collect(Collectors.toList());
    }

    @GET
    @Timeout(4000)
    @Retry(maxRetries = 2)
    @Path("/{id}")
    public Response findById(@PathParam("id") Integer id) {
        var singer = sR.findById(id);
        if (singer == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Cantante no encontrado").build();
        }
        SingerDto dto = mapSingerToDto(singer);
        return Response.ok(dto).build();
    }

    @POST
    @Timeout(4000)
    @Retry(maxRetries = 2)
    public Response create(Singer singer) {
        sR.create(singer);
        return Response.ok(singer).entity("Cantante creado exitosamente").build();
    }

    @PUT
    @Timeout(4000)
    @Retry(maxRetries = 2)
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, SingerDtoC tmpSinger) {

        var singer = sR.findById(id);
        if (singer == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Cantante no encontrado").build();
        }
        if(tmpSinger.getInstrumentsId()!= null){
            tmpSinger.getInstrumentsId().forEach(ins -> {
                if (iR.findById(ins) != null) {
                    SingerInstrumentDto singerInstrumentDto = new SingerInstrumentDto();
                    singerInstrumentDto.setSingerId(id);
                    singerInstrumentDto.setInstrumentId(ins);
                    clientSingerInstrument.create(singerInstrumentDto);
                }
            });
        }

        singer.setFirstName(tmpSinger.getFirstName());
        singer.setLastName(tmpSinger.getLastName());
        singer.setBirthDate(tmpSinger.getBirthDate());
        singer.setVersion(tmpSinger.getVersion());
        sR.update(singer);
        return Response.ok(singer).entity("Cantante actualizado exitosamente").build();
    }

    @DELETE
    @Timeout(4000)
    @Retry(maxRetries = 2)
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id) {
        var singer = sR.findById(id);
        if (singer == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Cantante no encontrado").build();
        }
        sR.delete(id);
        clientSingerInstrument.findBySingerId(id).forEach(x->clientSingerInstrument.delete(x.getId()));
        return Response.ok(singer).entity("Cantante eliminado exitosamente").build();
    }

}