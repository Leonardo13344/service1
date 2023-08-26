package com.distribuida.rest;

import com.distribuida.client.SingerInstrumentRestClient;
import com.distribuida.db.Instrument;
import com.distribuida.dto.InstrumentDto;
import com.distribuida.dto.InstrumentDtoC;
import com.distribuida.dto.SingerDto;
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

@Path("/instruments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Singleton
public class InstrumentRest {

    @Inject
    private InstrumentRepository iR;

    @Inject
    private SingerRepository sR;

    @Inject
    @RestClient
    SingerInstrumentRestClient clientSingerInstrument;

    private InstrumentDto mapInstrumentToDto(Instrument instrument) {
        InstrumentDto dto = new InstrumentDto();
        dto.setId(Math.toIntExact(instrument.getId()));
        dto.setName(instrument.getName());
        var singers = clientSingerInstrument.findByInstrumentId(dto.getId());
        List<SingerDto> singersDto = singers.stream().map(sin ->{
            SingerDto singerDto = new SingerDto();
            singerDto.setId(sin.getSingerId());
            singerDto.setFirstName(sR.findById(sin.getSingerId()).getFirstName());
            singerDto.setLastName(sR.findById(sin.getSingerId()).getLastName());
            singerDto.setBirthDate(sR.findById(sin.getSingerId()).getBirthDate());
            singerDto.setVersion(sR.findById(sin.getSingerId()).getVersion());
            return singerDto;
        }).collect(Collectors.toList());
        dto.setSingers(singersDto);
        return dto;
    }

    @GET
    @Timeout(4000)
    @Retry(maxRetries = 2)
    public List<InstrumentDto> findAllSingersOfInstrument(){
        return iR.findAll()
                .stream()
                .map(this::mapInstrumentToDto).collect(Collectors.toList());
    }

    @GET
    @Timeout(4000)
    @Retry(maxRetries = 2)
    @Path("/{id}")
    public Response getById(@PathParam("id") Integer id){
        var instrument = iR.findById(id);
        if(instrument == null){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Instrumento no encontrado")
                    .build();
        }
        InstrumentDto dto = mapInstrumentToDto(instrument);
        return Response.ok(dto).build();
    }

    @POST
    @Timeout(4000)
    @Retry(maxRetries = 2)
    public Response create(Instrument p){
        iR.create(p);
        return Response.ok(p).entity("Instrumento creado exitosamente").build();
    }

    @PUT
    @Timeout(4000)
    @Retry(maxRetries = 2)
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, InstrumentDtoC instrument){

        var instrumentAux = iR.findById(id);

        if(instrumentAux == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Instrumento no encontrado").build();
        }
        if(instrument.getSingersId()!=null){
            instrument.getSingersId().forEach(singerId -> {
                if(sR.findById(singerId) != null){
                    SingerInstrumentDto singerInstrumentDto = new SingerInstrumentDto();
                    singerInstrumentDto.setSingerId(singerId);
                    singerInstrumentDto.setInstrumentId(id);
                    clientSingerInstrument.create(singerInstrumentDto);
                }
            });
        }
        instrumentAux.setName(instrument.getName());
        iR.update(instrumentAux);
        return Response.ok(instrument).entity("Instrumento actualizado exitosamente").build();
    }

    @DELETE
    @Timeout(4000)
    @Retry(maxRetries = 2)
    @Path("/{id}")
    public Response delete(@PathParam("id") Integer id){
        var instrument = iR.findById(id);
        if(instrument == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Instrumento no encontrado").build();
        }
        iR.delete(id);
        clientSingerInstrument.findByInstrumentId(id).stream().forEach(x->clientSingerInstrument.delete(x.getId()));
        return Response.ok().entity("Instrumento eliminado exitosamente").build();
    }
}
