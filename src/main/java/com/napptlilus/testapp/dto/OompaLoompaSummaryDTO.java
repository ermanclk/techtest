package com.napptlilus.testapp.dto;

import java.io.Serializable;
import java.util.Objects;

public class OompaLoompaSummaryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private Integer age;
    private String job;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OompaLoompaSummaryDTO that = (OompaLoompaSummaryDTO) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(age, that.age) &&
                Objects.equals(job, that.job);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, job);
    }

    @Override
    public String toString() {
        return "OompaLoompaSummaryDTO{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", job='" + job + '\'' +
                '}';
    }
}
