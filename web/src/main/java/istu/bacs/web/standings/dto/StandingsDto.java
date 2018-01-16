package istu.bacs.web.standings.dto;

import istu.bacs.web.contest.dto.ContestMetaDto;
import istu.bacs.web.standings.Standings;
import lombok.Data;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
public class StandingsDto {

    private ContestMetaDto contest;
    private List<ContestantRowDto> contestants;

    public StandingsDto(Standings standings) {
        contest = new ContestMetaDto(standings.getContest());
        contestants = standings.getRows().stream()
                .map(ContestantRowDto::new)
                .collect(toList());
    }
}