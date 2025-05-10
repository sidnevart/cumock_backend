package com.example.cumock.controller;

import com.example.cumock.dto.code_sandbox.*;
import com.example.cumock.model.ProblemTestCase;
import com.example.cumock.repository.ProblemTestCaseRepository;
import com.example.cumock.service.CodeExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/code")
public class CodeSandboxController {

    private final CodeExecutionService executionService;
    private final ProblemTestCaseRepository testCaseRepository;

    public CodeSandboxController(CodeExecutionService executionService, ProblemTestCaseRepository testCaseRepository) {
        this.executionService = executionService;
        this.testCaseRepository = testCaseRepository;
    }

    @PostMapping("/execute")
    public ResponseEntity<CodeExecutionResponse> executeCode(CodeExecutionRequest request) throws IOException, InterruptedException {
        try {
            CodeResult result = executionService.execute(request.getCode(), request.getLanguage(), request.getInput());
            return ResponseEntity.ok(new CodeExecutionResponse(result));
        } catch(IOException | InterruptedException e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CodeExecutionResponse(
                            new CodeResult("", e.getMessage(), 1, 0L)
                    ));
        }
    }

    @PostMapping("/run")
    public ResponseEntity<RunResult> runCode(@RequestBody CodeRequest request) {
        List<ProblemTestCase> samples = testCaseRepository.findByProblemIdAndIsSampleTrue(request.getProblemId());
        List<TestResult> results = new ArrayList<>();

        for (ProblemTestCase testCase : samples) {
            try {
                CodeResult exec = executionService.execute(
                        request.getCode(),
                        testCase.getInput(),
                        request.getLanguage()
                );
                boolean passed = exec.getOutput().trim().equals(testCase.getOutput().trim());
                results.add(new TestResult(
                        testCase.getInput(),
                        exec.getOutput(),
                        testCase.getOutput(),
                        passed,
                        exec.getExecutionTimeMillis()
                ));
            } catch (Exception e){
                results.add(new TestResult(
                        testCase.getInput(),
                        "",
                        testCase.getOutput(),
                        false,
                        0L
                ));
            }

        }
        return ResponseEntity.ok(new RunResult(results));
    }
}
