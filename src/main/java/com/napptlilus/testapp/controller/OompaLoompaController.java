package com.napptlilus.testapp.controller;

import com.napptlilus.testapp.dto.OompaLoompaDTO;
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
@RequestMapping("/api/oompaloompa")
public class OompaLoompaController {

    private OompaLoompaService oompaLoompaService;
    private ModelMapper modelMapper;

    public OompaLoompaController(OompaLoompaService oompaLoompaService,ModelMapper modelMapper) {
        this.oompaLoompaService = oompaLoompaService;
        this.modelMapper = modelMapper;
    }

    @HystrixCommand(
            fallbackMethod = "fallbackSaveOrUpdate",
            commandKey = "saveCommand")
    @PostMapping
    public ResponseEntity create(@RequestBody OompaLoompaDTO oompaLoompaDTO) throws Exception {
        oompaLoompaService.create(convertToEntity(oompaLoompaDTO));
        return new ResponseEntity("Success", HttpStatus.OK);
    }

    @HystrixCommand(
            fallbackMethod = "fallbackSaveOrUpdate",
            commandKey = "saveCommand",
            ignoreExceptions = {NotFoundException.class,IllegalArgumentException.class})
    @PutMapping
    public ResponseEntity update(@RequestBody OompaLoompaDTO oompaLoompaDTO) throws Exception {
        oompaLoompaService.update(convertToEntity(oompaLoompaDTO));
        return new ResponseEntity("Success", HttpStatus.OK);
    }


    @GetMapping(path = "/{id}")
    public OompaLoompaDTO findById(@PathVariable String id) {
        return convertToDto(oompaLoompaService.findById(Long.parseLong(id)));
    }

    @GetMapping(path = "/findAll")
    public Map<String, Object> findAll(@RequestParam Optional<String> name,
                                       @RequestParam Optional<String> job,
                                       @RequestParam Optional<Integer> age,
                                       @RequestParam(defaultValue = "0") int pageNumber,
                                       @RequestParam(defaultValue = "10") int size) {

        Page<OompaLoompa> page = oompaLoompaService.findAll(name,job,age,PageRequest.of(pageNumber, size));
        List<OompaLoompaDTO> result= page.getContent().stream().map(this::convertToDto).collect(Collectors.toList());

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("content", result);
        responseMap.put("currentPage", page.getNumber());
        responseMap.put("totalItems", page.getTotalElements());
        responseMap.put("totalPages", page.getTotalPages());

        return responseMap;
    }

    private OompaLoompaDTO convertToDto(OompaLoompa oompaLoompa) {
        return modelMapper.map(oompaLoompa, OompaLoompaDTO.class);
    }

    private OompaLoompa convertToEntity(OompaLoompaDTO oompaLoompaDTO){
        return modelMapper.map(oompaLoompaDTO, OompaLoompa.class);
    }

    private ResponseEntity fallbackSaveOrUpdate(OompaLoompaDTO oompaLoompaDTO) {
        return new ResponseEntity("service temporarily unavailable, operation failed for: " +oompaLoompaDTO, HttpStatus.SERVICE_UNAVAILABLE);
    }
}