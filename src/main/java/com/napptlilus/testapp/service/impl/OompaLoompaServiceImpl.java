package com.napptlilus.testapp.service.impl;

import com.napptlilus.testapp.exception.NotFoundException;
import com.napptlilus.testapp.log.LogMe;
import com.napptlilus.testapp.model.OompaLoompa;
import com.napptlilus.testapp.repository.OompaLoompaRepository;
import com.napptlilus.testapp.service.OompaLoompaService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
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

    /**
     * creates OompaLoompa entity
     * @LogMe annotation adds INFO level log on execution
     * @param oompaLoompa
     */
    @LogMe("creating new Oompaloompa...")
    @Override
    public void create(OompaLoompa oompaLoompa) {
        oompaLoompaRepository.save(oompaLoompa);
    }


    /**
     * updates OompaLoompa entity
     * @LogMe annotation adds INFO level log on execution
     * @CacheEvict, removes record from cache, so it will be fetched from database on next call.
     * @param oompaLoompa
     */
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

    /**
     * finds Oompaloompa by Id
     * @Cacheable annotation provides caching, after first call result(Lazy Loading approach)
     * in next calls it will be use cache, so won't query database, until key is removed from cache by timetolive value exceeds
     * or by any cache evict.
     * key is removed
     * @param id
     * @return
     */
    @Cacheable(value = "olCache", key = "#id")
    public OompaLoompa findById(Long id) {
        return oompaLoompaRepository.findById(id).orElseThrow(() -> new NotFoundException("element not found - " + id));
    }

    /**
     * find all oompaloompas by given Optional parameters, al parameters are optional, uses filter parameter with "or"
     * result is paginated by given Paging objet..
     * @param name
     * @param job
     * @param age
     * @param paging
     * @return
     */
    @Override
    public Page<OompaLoompa> findAll(Optional<String> name, Optional<String> job, Optional<Integer> age, Pageable paging) {
        return oompaLoompaRepository.findAllByNameAndAgeAndJob(
                name.orElse(null),
                job.orElse(null),
                age.orElse(null), paging);
    }

}
