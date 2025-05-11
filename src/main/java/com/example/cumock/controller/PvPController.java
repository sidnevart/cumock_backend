package com.example.cumock.controller;

import com.example.cumock.dto.pvp.PvPProgressResponse;
import com.example.cumock.model.PvPContest;
import com.example.cumock.model.Submission;
import com.example.cumock.repository.ProblemTestCaseRepository;
import com.example.cumock.repository.PvPContestRepository;
import com.example.cumock.repository.SubmissionRepository;
import com.example.cumock.service.PvPContestService;
import com.example.cumock.service.SubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pvp")
public class PvPController {

    private final PvPContestService contestService;
    private final SubmissionRepository submissionRepository;
    private final SubmissionService submissionService;
    private final ProblemTestCaseRepository testCaseRepository;
    private final PvPContestRepository contestRepository;


    public PvPController(
            PvPContestService contestService,
            SubmissionRepository submissionRepository,
            SubmissionService submissionService,
            ProblemTestCaseRepository testCaseRepository,
            PvPContestRepository contestRepository
    ) {
        this.contestService = contestService;
        this.submissionRepository = submissionRepository;
        this.submissionService = submissionService;
        this.testCaseRepository = testCaseRepository;
        this.contestRepository = contestRepository;
    }

    @GetMapping("/progress")
    public ResponseEntity<PvPProgressResponse> getProgress(
            @RequestParam Long contestId,
            @RequestParam Long userId,
            @RequestParam Long problemId,
            @RequestParam(defaultValue = "false") boolean isSubmit
    ) {
        Optional<PvPContest> byId = contestRepository.findById(contestId);
        System.out.println(">>> Найдён без status: " + byId.isPresent());
        System.out.println(">>> Его статус: " + byId.map(PvPContest::getStatus).orElse("null"));



        System.out.println(">>> Найдён с id: " + byId.isPresent());


        System.out.println(">>> Найдён без status: " + byId.isPresent());
        System.out.println(">>> Его статус: " + byId.map(PvPContest::getStatus).orElse("null"));


        Optional<PvPContest> optional = contestService.findOngoingById(contestId);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();


        PvPProgressResponse progress = contestService.getProgress(userId, problemId, contestId, isSubmit);
        return ResponseEntity.ok(progress);
    }


}
