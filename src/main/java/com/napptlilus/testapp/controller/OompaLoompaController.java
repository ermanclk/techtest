package com.napptlilus.testapp.controller;

import com.napptlilus.testapp.dto.OompaLoompaDTO;
import com.napptlilus.testapp.dto.OompaLoompaSummaryDTO;
import com.napptlilus.testapp.exception.NotFoundException;
import com.napptlilus.testapp.model.OompaLoompa;
import com.napptlilus.testapp.service.OompaLoompaService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/oompaloompa")
public class OompaLoompaController {

    private OompaLoompaService oompaLoompaService;
    private ModelMapper modelMapper;

    public OompaLoompaController(OompaLoompaService oompaLoompaService,ModelMapper modelMapper) {
        this.oompaLoompaService = oompaLoompaService;
        this.modelMapper = modelMapper;
    }

    /**
     * creates OompaLoompa
     * Wrapped with Hystrix, on any failure or timeout will execute fallback method.
     * @param oompaLoompaDTO
     * @return
     */
    @HystrixCommand(
            fallbackMethod = "fallbackSaveOrUpdate",
            commandKey = "saveCommand")
    @PostMapping
    public ResponseEntity<String> create(@RequestBody OompaLoompaDTO oompaLoompaDTO) {
        oompaLoompaService.create(convertToEntity(oompaLoompaDTO));
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    /**
     * updates OompaLoompa
     * Wrapped with Hystrix, on any failure or timeout will execute fallback method.
     * @param oompaLoompaDTO
     * @return
     */
    @HystrixCommand(
            fallbackMethod = "fallbackSaveOrUpdate",
            commandKey = "saveCommand",
            ignoreExceptions = {NotFoundException.class,IllegalArgumentException.class})
    @PutMapping
    public ResponseEntity<String> update(@RequestBody OompaLoompaDTO oompaLoompaDTO) {
        oompaLoompaService.update(convertToEntity(oompaLoompaDTO));
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    /**
     * find full object details by id
     * @param id
     * @return
     */
    @GetMapping(path = "/{id}")
    public OompaLoompaDTO findById(@PathVariable String id) {
        return convertToDTO(oompaLoompaService.findById(Long.parseLong(id)), OompaLoompaDTO.class);
    }

    /**
     * find all oompa loompa list, if parameters (name, job, age) exist,then filter by them.
     * If there is not any filter parameter bring them all by pagination
     * Only put name, age and job parameters in response, according to requirement.
     * in query
     * @param name
     * @param job
     * @param age
     * @param pageNumber
     * @param size
     * @return
     */
    @GetMapping(path = "/findAll")
    public Map<String, Object> findAll(@RequestParam Optional<String> name,
                                       @RequestParam Optional<String> job,
                                       @RequestParam Optional<Integer> age,
                                       @RequestParam(defaultValue = "0") int pageNumber,
                                       @RequestParam(defaultValue = "10") int size) {

        Page<OompaLoompa> page = oompaLoompaService.findAll(name,job,age,PageRequest.of(pageNumber, size));

        List<OompaLoompaSummaryDTO> result= page.getContent().stream()
                .map(oompaLoompa-> convertToDTO(oompaLoompa, OompaLoompaSummaryDTO.class))
                .collect(Collectors.toList());

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("content", result);
        responseMap.put("currentPage", page.getNumber());
        responseMap.put("totalItems", page.getTotalElements());
        responseMap.put("totalPages", page.getTotalPages());

        return responseMap;
    }

    private ResponseEntity<String> fallbackSaveOrUpdate(OompaLoompaDTO oompaLoompaDTO) {
        return new ResponseEntity<>("service temporarily unavailable, operation failed for: " +oompaLoompaDTO,
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    private <T> T convertToDTO(OompaLoompa oompaLoompa, Class<T> dtoClassType) {
        return modelMapper.map(oompaLoompa, dtoClassType);
    }

    private OompaLoompa convertToEntity(OompaLoompaDTO oompaLoompaDTO){
        return modelMapper.map(oompaLoompaDTO, OompaLoompa.class);
    }
}