package com.napptlilus.testapp.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.napptlilus.testapp.dto.OompaLoompaDTO;
import com.napptlilus.testapp.exception.NotFoundException;
import com.napptlilus.testapp.log.LogMe;
import com.napptlilus.testapp.model.OompaLoompa;
import com.napptlilus.testapp.repository.OompaLoompaRepository;
import com.napptlilus.testapp.service.OompaLoompaService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OompaLoompaServiceImpl implements OompaLoompaService {

    OompaLoompaRepository oompaLoompaRepository;

    public OompaLoompaServiceImpl(OompaLoompaRepository oompaLoompaRepository) {
        this.oompaLoompaRepository = oompaLoompaRepository;
    }

    @LogMe("creating new Oompaloompa...")
    @Override
    public void create(OompaLoompa oompaLoompa) {
        oompaLoompaRepository.save(oompaLoompa);
    }


    @Override
    @LogMe("Updating Oompaloompa..")
    @CacheEvict(value = "olCache", key = "#oompaLoompa.id")
    public void update(OompaLoompa oompaLoompa) {

        if(oompaLoompa.getId() == null ){
            throw new IllegalArgumentException("id is mandatory for update operation.");
        }
        OompaLoompa existingOompaLoompa = oompaLoompaRepository.findById(oompaLoompa.getId())
                    .orElseThrow(() -> new NotFoundException("cannot find element with id:" + oompaLoompa.getId()));

        BeanUtils.copyProperties(oompaLoompa,existingOompaLoompa);
        oompaLoompaRepository.save(existingOompaLoompa);
    }

    @Cacheable(value = "olCache", key = "#id")
    public OompaLoompa findById(Long id) {
        return oompaLoompaRepository.findById(id).orElseThrow(() -> new NotFoundException("element not found - " + id));
    }

    @Override
    public Page<OompaLoompa> findAll(Optional<String> name, Optional<String> job, Optional<Integer> age, Pageable paging) {
        return oompaLoompaRepository.findAllByNameAndAgeAndJob(
                name.orElse(null),
                job.orElse(null),
                age.orElse(null), paging);
    }

}
