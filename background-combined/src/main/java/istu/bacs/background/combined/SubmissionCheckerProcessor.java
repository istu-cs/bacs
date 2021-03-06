package istu.bacs.background.combined;

import istu.bacs.background.combined.db.SubmissionService;
import istu.bacs.db.submission.Submission;
import istu.bacs.db.submission.Verdict;
import istu.bacs.externalapi.ExternalApi;
import istu.bacs.rabbit.QueueName;
import istu.bacs.rabbit.RabbitService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

import static istu.bacs.background.combined.SubmissionCheckerProcessor.PROCESSOR_NAME;
import static istu.bacs.db.submission.Verdict.PENDING;
import static istu.bacs.rabbit.QueueName.CHECKED_SUBMISSIONS;
import static istu.bacs.rabbit.QueueName.SUBMITTED_SUBMISSIONS;

@Slf4j
@Component(PROCESSOR_NAME)
public class SubmissionCheckerProcessor extends SubmissionProcessor {

    static final String PROCESSOR_NAME = "SubmissionCheckerProcessor";

    private final ExternalApi externalApi;

    public SubmissionCheckerProcessor(SubmissionService submissionService, RabbitService rabbitService, ExternalApi externalApi) {
        super(submissionService, rabbitService);
        this.externalApi = externalApi;
    }

    @Override
    protected boolean process(List<Submission> submissions) {
        return externalApi.checkSubmissionResult(submissions);
    }

    @Override
    protected Verdict incomingVerdict() {
        return PENDING;
    }

    @Override
    protected QueueName incomingQueueName() {
        return SUBMITTED_SUBMISSIONS;
    }

    @Override
    protected QueueName outcomingQueueName() {
        return CHECKED_SUBMISSIONS;
    }

    @Override
    protected String processorName() {
        return PROCESSOR_NAME;
    }

    @Override
    protected Logger log() {
        return log;
    }
}