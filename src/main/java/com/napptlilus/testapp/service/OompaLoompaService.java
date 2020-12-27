package com.napptlilus.testapp.service;

import com.napptlilus.testapp.dto.OompaLoompaDTO;
import com.napptlilus.testapp.model.OompaLoompa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OompaLoompaService {

    OompaLoompa findById (Long id) throws InterruptedException;

    Page<OompaLoompa> findAll(Optional<String> name, Optional<String> job, Optional<Integer> age, Pageable paging);

    void save(OompaLoompaDTO oompaLoompaDTO) throws Exception;
}
