package com.napptlilus.testapp.service.impl;

import com.napptlilus.testapp.dto.OompaLoompaDTO;
import com.napptlilus.testapp.exception.NotFoundException;
import com.napptlilus.testapp.model.OompaLoompa;
import com.napptlilus.testapp.repository.OompaLoompaRepository;
import com.napptlilus.testapp.service.OompaLoompaService;
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

    public OompaLoompa findById(Long id) throws InterruptedException {
        Thread.sleep (10000);
        return oompaLoompaRepository.findById(id).orElseThrow(() -> new NotFoundException("element not found - " + id));
    }

    @Override
    public Page<OompaLoompa> findAll(Optional<String> name, Optional<String> job, Optional<Integer> age, Pageable paging) {

        return oompaLoompaRepository.findAllByNameAndAgeAndJob(
                name.orElse(null),
                job.orElse(null),
                age.orElse(null), paging);
    }

    @Override
    public void save(OompaLoompaDTO oompaLoompaDTO) throws NotFoundException {

        OompaLoompa ooampaLoompa;
        if(oompaLoompaDTO.getId() != null){
            ooampaLoompa = oompaLoompaRepository.findById(oompaLoompaDTO.getId())
                    .orElseThrow(() -> new NotFoundException("element not found - " + oompaLoompaDTO.getId()));
        }else{
            ooampaLoompa = new OompaLoompa();
        }
        ooampaLoompa.setAge(oompaLoompaDTO.getAge());
        ooampaLoompa.setDescription(oompaLoompaDTO.getDescription());
        ooampaLoompa.setJob(oompaLoompaDTO.getJob());
        ooampaLoompa.setHeight(oompaLoompaDTO.getHeight());
        ooampaLoompa.setWeight(oompaLoompaDTO.getWeight());
        ooampaLoompa.setName(oompaLoompaDTO.getName());

        oompaLoompaRepository.save(ooampaLoompa);
    }


}
