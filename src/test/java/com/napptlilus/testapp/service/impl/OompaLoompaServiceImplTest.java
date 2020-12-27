package com.napptlilus.testapp.service.impl;

import com.napptlilus.testapp.exception.NotFoundException;
import com.napptlilus.testapp.model.OompaLoompa;
import com.napptlilus.testapp.repository.OompaLoompaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class OompaLoompaServiceImplTest {

    private OompaLoompaServiceImpl classToTest;
    private OompaLoompaRepository oompaLoompaRepository;
    private OompaLoompa dummyObject;
    private final Long TEST_ID=1L;
    private final String TEST_NAME="dummyName";
    private final String TEST_JOB="dummyJob";
    private final Integer TEST_AGE=18;
    private Pageable pageable;

    @Before
    public void setUp() {
        oompaLoompaRepository= mock(OompaLoompaRepository.class);
        dummyObject = mock(OompaLoompa.class);
        pageable=mock(Pageable.class);
        when(oompaLoompaRepository.save(dummyObject)).thenReturn(dummyObject);

        classToTest= spy(new OompaLoompaServiceImpl(oompaLoompaRepository));
    }

    @Test
    public void givenDummyObjectWhenCreateThenCallRepository(){
        //Act
        classToTest.create(dummyObject);
        //Assert
        verify(oompaLoompaRepository,times(1)).save(dummyObject);
    }

    @Test
    public void givenDummyObjectWhenUpdateThenCallRepository(){
        //Arrange
        when(oompaLoompaRepository.save(dummyObject)).thenReturn(any());
        //Act
        classToTest.create(dummyObject);
        //Assert
        verify(oompaLoompaRepository,times(1)).save(dummyObject);
    }

    @Test(expected=IllegalArgumentException.class)
    public void givenIdNullWhenUpdateThenThrowIllegalArgumentException(){
        //Arrange
        when(dummyObject.getId()).thenReturn(null);
        //Act
        classToTest.update(dummyObject);
    }

    @Test(expected= NotFoundException.class)
    public void givenElementNotFoundWhenUpdateThenThrowNotFoundException(){
        //Arrange
        when(dummyObject.getId()).thenReturn(TEST_ID);
        when(oompaLoompaRepository.findById(TEST_ID)).thenReturn(Optional.empty());
        //Act
        classToTest.update(dummyObject);
    }

    @Test
    public void givenElementExistWhenFindByIdThenReturnElement(){
        //Arrange
        when(oompaLoompaRepository.findById(TEST_ID)).thenReturn(Optional.of(dummyObject));
        //Act
        OompaLoompa oompaLoompa= classToTest.findById(TEST_ID);
        //assert
        assertThat(oompaLoompa,equalTo(dummyObject));
    }

    @Test(expected= NotFoundException.class)
    public void givenElementNotExistWhenFindByIdThenThrowNotFoundException(){
        //Arrange
        when(oompaLoompaRepository.findById(TEST_ID)).thenReturn(Optional.empty());
        //Act
        classToTest.findById(TEST_ID);
    }

    @Test
    public void givenNameWhenFindAllThenReturnRepositoryResult(){
        //Arrange
        Page dummyPage= mock(Page.class);
        when(oompaLoompaRepository.findAllByNameAndAgeAndJob(anyString(),anyString(),anyInt(),any())).thenReturn(dummyPage);
        //Act
        Page<OompaLoompa> actual=   classToTest.findAll(Optional.of(TEST_NAME),Optional.of(TEST_JOB),Optional.of(TEST_AGE),pageable);
        //Assert
        assertThat(actual,equalTo(dummyPage));
    }


}