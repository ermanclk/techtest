package com.napptlilus.testapp.repository;

import com.napptlilus.testapp.model.OompaLoompa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OompaLoompaRepository extends JpaRepository<OompaLoompa,Long> {

        @Query("SELECT o FROM OompaLoompa o WHERE (:name is null or o.name = :name) and (:job is null"
                + " or o.job = :job) and  (:age is null or o.age = :age)")
        Page<OompaLoompa> findAllByNameAndAgeAndJob(String name, String job, Integer age, Pageable paging);
}
