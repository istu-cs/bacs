package istu.bacs.standings;

import istu.bacs.db.submission.Submission;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static istu.bacs.db.submission.Verdict.ACCEPTED;
import static java.util.Comparator.comparing;

@Data
public class ProblemProgress {

    private final List<Submission> submissions = new ArrayList<>();

    private SolvingResult result = SolvingResult.notSolved(0);

    public void update(Submission newSubmission) {
        submissions.remove(newSubmission);
        submissions.add(newSubmission);
        submissions.sort(comparing(Submission::getSubmissionId));

        int failTries = 0;
        for (Submission submission : submissions) {
            if (submission.getVerdict() == ACCEPTED) {
                result = SolvingResult.solved(failTries, submission);
                return;
            } else {
                failTries++;
            }
        }

        result = SolvingResult.notSolved(failTries);
    }

    public SolvingResult getResult() {
        return result;
    }
}