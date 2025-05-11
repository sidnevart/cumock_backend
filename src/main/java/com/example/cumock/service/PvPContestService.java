package com.example.cumock.service;

import com.example.cumock.dto.pvp.PvPProgressResponse;
import com.example.cumock.model.ProblemTestCase;
import com.example.cumock.model.PvPContest;
import com.example.cumock.model.Submission;
import com.example.cumock.repository.ProblemTestCaseRepository;
import com.example.cumock.repository.PvPContestRepository;
import com.example.cumock.repository.SubmissionRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PvPContestService {

    private final PvPContestRepository repository;

    private final SubmissionRepository submissionRepository;

    private final ProblemTestCaseRepository testCaseRepository;

    private final RunResultCacheService runResultCache;





    public PvPContestService(
            PvPContestRepository repository,
            SubmissionRepository submissionRepository,
            ProblemTestCaseRepository testCaseRepository
            , RunResultCacheService runResultCache
    ) {
        this.repository = repository;
        this.submissionRepository = submissionRepository;
        this.testCaseRepository = testCaseRepository;
        this.runResultCache = runResultCache;
    }


    public PvPContest startMatch(Long user1Id, Long user2Id, Long task1Id, Long task2Id) {
        PvPContest contest = new PvPContest();
        contest.setUser1Id(user1Id);
        contest.setUser2Id(user2Id);
        contest.setProblem1Id(task1Id);
        contest.setProblem2Id(task2Id);
        contest.setStartTime(LocalDateTime.now());
        contest.setStatus("ONGOING");
        return repository.save(contest);
    }


    @Scheduled(fixedRate = 60_000)
    public void checkSomething() {
        System.out.println("Каждую минуту проверяю...");
    }

    public Optional<PvPContest> findOngoingById(Long contestId) {
        return repository.findByIdAndStatus(contestId, "ONGOING");
    }

    public void endMatchWithEvaluation(Long contestId) {
        Optional<PvPContest> optional = repository.findById(contestId);
        if (optional.isEmpty()) return;
        PvPContest contest = optional.get();

        List<Submission> submissions = submissionRepository.findByContestIdAndVerdict(contestId, "OK");

        Long winnerId = null;

        if (submissions.size() == 1) {
            winnerId = submissions.get(0).getUserId();
        } else if (submissions.size() == 2) {
            Submission s1 = submissions.get(0);
            Submission s2 = submissions.get(1);

            if (s1.getAttempt() < s2.getAttempt()) {
                winnerId = s1.getUserId();
            } else if (s2.getAttempt() < s1.getAttempt()) {
                winnerId = s2.getUserId();
            } else {
                // сравнение по времени
                if (s1.getCreatedAt().isBefore(s2.getCreatedAt())) {
                    winnerId = s1.getUserId();
                } else {
                    winnerId = s2.getUserId();
                }
            }
        }

        contest.setEndTime(LocalDateTime.now());
        contest.setStatus("FINISHED");
        contest.setWinnerId(winnerId);
        repository.save(contest);
    }

    public PvPProgressResponse getProgress(Long userId, Long problemId, Long contestId, boolean isSubmit) {
        List<ProblemTestCase> tests = testCaseRepository.findByProblemId(problemId);
        int total = tests.size();

        int passed = 0;
        int attempts = isSubmit
                ? submissionRepository.countByUserIdAndProblemIdAndContestId(userId, problemId, contestId)
                : 0;

        if (isSubmit) {
            List<Submission> submissions = submissionRepository.findByUserIdAndProblemIdAndContestId(userId, problemId, contestId);
            boolean solved = submissions.stream().anyMatch(s -> "OK".equals(s.getVerdict()));
            return new PvPProgressResponse(userId, contestId, solved ? total : 0, total, solved, attempts);
        } else {
            passed = runResultCache.getPassed(userId, problemId, contestId);
            return new PvPProgressResponse(userId, contestId, passed, total, false, attempts);
        }


    }

}
