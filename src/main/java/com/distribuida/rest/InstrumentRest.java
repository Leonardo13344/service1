package com.distribuida.rest;

import com.distribuida.client.SingerInstrumentRestClient;
import com.distribuida.db.Instrument;
import com.distribuida.dto.InstrumentDto;
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

    @GET
    @Timeout(4000)
    @Retry(maxRetries = 2)
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
    @Timeout(4000)
    @Retry(maxRetries = 2)
    public List<Instrument> findAll(){
        return iR.findAll();
    }

    @GET
    @Timeout(4000)
    @Retry(maxRetries = 2)
    @Path("/{id}")
    public Response getById(@PathParam("id") Integer id){
        var instrument = iR.findById(id);
        if(instrument == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(instrument).build();
    }

    @POST
    @Timeout(4000)
    @Retry(maxRetries = 2)
    public Response create(InstrumentDto p){
        p.getSingers().stream().forEach(singerDto -> {
            var singer = sR.findById(singerDto.getId());
            if(singer != null){
                SingerInstrumentDto singerInstrumentDto = new SingerInstrumentDto();
                singerInstrumentDto.setSingerId(p.getId());
                singerInstrumentDto.setInstrumentId(singerDto.getId());
                clientSingerInstrument.create(singerInstrumentDto);
            }
        });
        Instrument instrument = new Instrument();
        instrument.setName(p.getName());
        instrument.setId(p.getId());
        iR.create(instrument);
        return Response.ok(p).build();
    }

    @PUT
    @Timeout(4000)
    @Retry(maxRetries = 2)
    @Path("/{id}")
    public Response update(@PathParam("id") Integer id, InstrumentDto tmp){
        tmp.getSingers().stream().forEach(singerDto -> {
            var singer = sR.findById(singerDto.getId());
            if(singer != null){
                SingerInstrumentDto singerInstrumentDto = new SingerInstrumentDto();
                singerInstrumentDto.setSingerId(tmp.getId());
                singerInstrumentDto.setInstrumentId(singerDto.getId());
                Integer idSingerInstrument = clientSingerInstrument.findByIds(tmp.getId(), singerDto.getId()).getId();
                clientSingerInstrument.update(idSingerInstrument ,singerInstrumentDto);
            }
        });
        var instrumentAux = iR.findById(id);
        if(instrumentAux == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        instrumentAux.setName(tmp.getName());
        iR.update(instrumentAux);
        return Response.ok(instrumentAux).build();
    }

    @DELETE
    @Timeout(4000)
    @Retry(maxRetries = 2)
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
