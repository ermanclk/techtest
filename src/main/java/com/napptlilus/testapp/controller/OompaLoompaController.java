package com.napptlilus.testapp.controller;

import com.napptlilus.testapp.dto.OompaLoompaDTO;
import com.napptlilus.testapp.exception.NotFoundException;
import com.napptlilus.testapp.model.OompaLoompa;
import com.napptlilus.testapp.service.OompaLoompaService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.ServiceUnavailableException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/oompaloompa")
public class OompaLoompaController {

    private static final Logger LOG = LoggerFactory.getLogger(OompaLoompaController.class);

    private OompaLoompaService oompaLoompaService;

    public OompaLoompaController(OompaLoompaService oompaLoompaService) {
        this.oompaLoompaService = oompaLoompaService;
    }

    @HystrixCommand(
            fallbackMethod = "fallbackFindById",
            ignoreExceptions = {NotFoundException.class},
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")})
    @PostMapping
    public ResponseEntity save(@RequestBody OompaLoompaDTO oompaLoompaDTO) throws Exception {
        oompaLoompaService.save(oompaLoompaDTO);
        return new ResponseEntity("successfully saved", HttpStatus.OK);
    }




    @GetMapping(path = "/{id}")
    @Cacheable(value = "oompaloompas", key = "#oompaLoompa.id")
    public ResponseEntity findById(@PathVariable String id) throws InterruptedException {
        LOG.info("finding requested OompaLoompa: ",id);
        OompaLoompa oompaLoompa = oompaLoompaService.findById(Long.parseLong(id));
        return new ResponseEntity(oompaLoompa, HttpStatus.OK);
    }

    public ResponseEntity fallbackSave(OompaLoompaDTO oompaLoompaDTO) throws ServiceUnavailableException {
        return new ResponseEntity("service temporarily unavailable", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping(path = "/findAll")
    public ResponseEntity<OompaLoompa> findAll(@RequestParam Optional<String> name,
                                               @RequestParam Optional<String> job,
                                               @RequestParam Optional<Integer> age,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {

        Pageable paging = PageRequest.of(page, size);
        Page<OompaLoompa> oompaLoompas = oompaLoompaService.findAll(name,job,age,paging);
        Map<String, Object> response = new HashMap<>();
        response.put("oompaLoompas", oompaLoompas.getContent());
        response.put("currentPage", oompaLoompas.getNumber());
        response.put("totalItems", oompaLoompas.getTotalElements());
        response.put("totalPages", oompaLoompas.getTotalPages());

        return new ResponseEntity(response, HttpStatus.OK);
    }
}