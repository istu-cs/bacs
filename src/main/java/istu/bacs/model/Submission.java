package istu.bacs.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Submission {
	
	@Id @GeneratedValue
	private Integer submissionId;
	
    @Column(name = "author_id")
	private User author;
    @Column(name = "contest_id")
	private Contest contest;
	@Column(name = "problem_id")
	private Problem problem;

	private boolean pretestsOnly;
	private LocalDateTime creationTime;
	private Language language;
	private String solution;

	private String externalSubmissionId;

	@Transient
    private SubmissionResult result;

	public Verdict getVerdict() {
	    if (result == null) return Verdict.SERVER_ERROR;
	    return result.getVerdict();
    }
}