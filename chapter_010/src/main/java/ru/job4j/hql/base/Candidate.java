package ru.job4j.hql.base;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "candidates", schema = "public")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "experience", nullable = false)
    private String experience;

    @Column(name = "salary", nullable = false)
    private Integer salary;

    public Candidate() {
    }

    public static Candidate of(String name, String experience, Integer salary) {
        Candidate candidate = new Candidate();
        candidate.setName(name);
        candidate.setExperience(experience);
        candidate.setSalary(salary);
        return candidate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Candidate)) {
            return false;
        }

        Candidate candidate = (Candidate) o;

        if (!Objects.equals(id, candidate.id)) {
            return false;
        }
        if (!name.equals(candidate.name)) {
            return false;
        }
        if (!Objects.equals(experience, candidate.experience)) {
            return false;
        }
        return Objects.equals(salary, candidate.salary);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + (experience != null ? experience.hashCode() : 0);
        result = 31 * result + (salary != null ? salary.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Candidate.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("experience='" + experience + "'")
                .add("salary=" + salary)
                .toString();
    }
}
