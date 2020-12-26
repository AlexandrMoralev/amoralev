package ru.job4j.hql.base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.mapping.modelsrelations.TestAppContext;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class CandidateDaoTest {

    private final CandidateDao candidateDao;

    private Candidate firstCandidate;
    private Candidate secondCandidate;
    private Candidate thirdCandidate;

    public CandidateDaoTest() {
        this.candidateDao = new CandidateDao(TestAppContext.createAnnotationBasedUtils());
    }

    @BeforeEach
    public void init() {
        firstCandidate = Candidate.of("firstCandidate", "exp_1", 1000);
        secondCandidate = Candidate.of("secondCandidate", "exp_2", 200);
        thirdCandidate = Candidate.of("thirdCandidate", "exp_3", 30);
        Collection<Candidate> candidates = Arrays.asList(firstCandidate, secondCandidate, thirdCandidate);
        candidates.forEach(candidateDao::save);
    }

    @Test
    public void testSelectSavedCandidates() {
        Collection<Candidate> candidates = List.of(firstCandidate, secondCandidate, thirdCandidate);
        Collection<Candidate> savedCandidates = new HashSet<>(candidateDao.findAll());

        assertEquals(3, savedCandidates.size());
        assertTrue(savedCandidates.containsAll(candidates));

        Collection<Candidate> candidatesById = candidates.stream()
                .map(Candidate::getId)
                .flatMap(id -> candidateDao.findById(id).stream())
                .collect(Collectors.toList());
        assertEquals(3, candidatesById.size());
        assertTrue(candidatesById.containsAll(candidates));

        Collection<Candidate> candidatesByName = candidates.stream()
                .map(Candidate::getName)
                .flatMap(name -> candidateDao.findByName(name).stream())
                .collect(Collectors.toList());
        assertEquals(3, candidatesByName.size());
        assertTrue(candidatesByName.containsAll(candidates));
    }

    @Test
    public void testUpdateSavedCandidates() {
        Collection<Candidate> candidates = List.of(firstCandidate, secondCandidate, thirdCandidate);
        Collection<Candidate> savedCandidates = new HashSet<>(candidateDao.findAll());

        assertEquals(3, savedCandidates.size());
        assertTrue(savedCandidates.containsAll(candidates));

        final String prefix = "NEW_";
        Collection<String> newNames = new ArrayList<>();

        savedCandidates.stream()
                .map(c -> {
                    c.setName(prefix + c.getName());
                    c.setExperience(prefix + c.getExperience());
                    c.setSalary(0);
                    return c;
                }).forEach(c -> {
            newNames.add(c.getName());
            candidateDao.update(c);
        });

        savedCandidates = new HashSet<>(candidateDao.findAll());
        assertEquals(3, savedCandidates.size());
        assertTrue(
                savedCandidates.stream()
                        .allMatch(c -> c.getName().startsWith(prefix)
                                && c.getExperience().startsWith(prefix)
                                && c.getSalary() == 0)
        );

        Collection<Candidate> candidatesById = candidates.stream()
                .map(Candidate::getId)
                .flatMap(id -> candidateDao.findById(id).stream())
                .collect(Collectors.toList());
        assertEquals(3, candidatesById.size());
        assertTrue(candidatesById.containsAll(savedCandidates));

        Collection<Candidate> candidatesByName = newNames.stream()
                .flatMap(name -> candidateDao.findByName(name).stream())
                .collect(Collectors.toList());
        assertEquals(3, candidatesByName.size());
        assertTrue(candidatesByName.containsAll(savedCandidates));
    }

    @Test
    public void testDeleteSavedCandidates() {
        Collection<Candidate> candidates = List.of(firstCandidate, secondCandidate, thirdCandidate);
        Collection<Candidate> savedCandidates = new HashSet<>(candidateDao.findAll());

        assertEquals(3, savedCandidates.size());
        assertTrue(savedCandidates.containsAll(candidates));

        candidateDao.deleteById(firstCandidate.getId());

        savedCandidates = new HashSet<>(candidateDao.findAll());
        assertEquals(2, savedCandidates.size());
        assertFalse(savedCandidates.contains(firstCandidate));
        assertTrue(savedCandidates.contains(secondCandidate));
        assertTrue(savedCandidates.contains(thirdCandidate));

        assertTrue(candidateDao.findById(firstCandidate.getId()).isEmpty());
        assertTrue(candidateDao.findByName(firstCandidate.getName()).isEmpty());

        assertTrue(candidateDao.findById(secondCandidate.getId()).isPresent());
        assertTrue(candidateDao.findByName(secondCandidate.getName()).isPresent());
        assertTrue(candidateDao.findById(thirdCandidate.getId()).isPresent());
        assertTrue(candidateDao.findByName(thirdCandidate.getName()).isPresent());

        candidateDao.deleteById(secondCandidate.getId());

        savedCandidates = new HashSet<>(candidateDao.findAll());
        assertEquals(1, savedCandidates.size());
        assertFalse(savedCandidates.contains(firstCandidate));
        assertFalse(savedCandidates.contains(secondCandidate));
        assertTrue(savedCandidates.contains(thirdCandidate));

        assertTrue(candidateDao.findById(firstCandidate.getId()).isEmpty());
        assertTrue(candidateDao.findByName(firstCandidate.getName()).isEmpty());
        assertTrue(candidateDao.findById(secondCandidate.getId()).isEmpty());
        assertTrue(candidateDao.findByName(secondCandidate.getName()).isEmpty());

        assertTrue(candidateDao.findById(thirdCandidate.getId()).isPresent());
        assertTrue(candidateDao.findByName(thirdCandidate.getName()).isPresent());

        candidateDao.deleteById(thirdCandidate.getId());

        savedCandidates = new HashSet<>(candidateDao.findAll());
        assertTrue(savedCandidates.isEmpty());

        assertTrue(candidateDao.findById(firstCandidate.getId()).isEmpty());
        assertTrue(candidateDao.findByName(firstCandidate.getName()).isEmpty());
        assertTrue(candidateDao.findById(secondCandidate.getId()).isEmpty());
        assertTrue(candidateDao.findByName(secondCandidate.getName()).isEmpty());
        assertTrue(candidateDao.findById(thirdCandidate.getId()).isEmpty());
        assertTrue(candidateDao.findByName(thirdCandidate.getName()).isEmpty());
    }

}
