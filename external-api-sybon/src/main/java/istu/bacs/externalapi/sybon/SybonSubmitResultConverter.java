package istu.bacs.externalapi.sybon;

import istu.bacs.db.submission.SubmissionResult;
import istu.bacs.db.submission.Verdict;
import org.springframework.core.convert.converter.Converter;

public class SybonSubmitResultConverter implements Converter<SybonSubmitResult, SubmissionResult> {

    private static final SybonTestResultStatusConverter testResultStatusConverter = new SybonTestResultStatusConverter();

    @Override
    public SubmissionResult convert(SybonSubmitResult submission) {
        SybonBuildResultStatus status = submission.getBuildResult().getStatus();
        if (status == SybonBuildResultStatus.PENDING)
            return SubmissionResult.builder()
                    .verdict(Verdict.PENDING)
                    .build();

        if (status == SybonBuildResultStatus.FAILED || status == SybonBuildResultStatus.SERVER_ERROR)
            return SubmissionResult.builder()
                    .buildInfo(submission.getBuildResult().getOutput())
                    .verdict(Verdict.COMPILE_ERROR)
                    .build();

        int maxTimeUsedMillis = 0;
        int maxMemoryUsedBytes = 0;
        Verdict verdict = Verdict.OK;
        Integer testsPassed = 0;
        for (SybonTestGroupResult testGroup : submission.getTestGroupResults()) {
            for (SybonTestResult result : testGroup.getTestResults()) {
                SybonResourceUsage res = result.getResourceUsage();
                maxTimeUsedMillis = Math.max(maxTimeUsedMillis, res.getTimeUsageMillis());
                maxMemoryUsedBytes = Math.max(maxMemoryUsedBytes, res.getMemoryUsageBytes());
                if (result.getStatus() != SybonTestResultStatus.OK) {
                    verdict = testResultStatusConverter.convert(result.getStatus());
                    break;
                }
                testsPassed++;
            }
            if (verdict != Verdict.OK) break;
        }
        if (verdict == Verdict.OK) testsPassed = null;

        return SubmissionResult.builder()
                .buildInfo(submission.getBuildResult().getOutput())
                .verdict(verdict)
                .testsPassed(testsPassed)
                .timeUsedMillis(maxTimeUsedMillis)
                .memoryUsedBytes(maxMemoryUsedBytes)
                .build();
    }
}