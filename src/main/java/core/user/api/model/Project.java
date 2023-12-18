package core.user.api.model;

import core.user.api.dto.ProjectDto;
import core.user.api.dto.ProjectKey;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class Project {
    @Id
    private String projectNo;
    private String compCode;
    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double budget;
    private String projectStatus;
    private LocalDateTime updatedDate;

    public ProjectDto buildDto(){
        return ProjectDto.builder()
                .key(ProjectKey.builder()
                        .projectNo(getProjectNo())
                        .compCode(getCompCode())
                        .build())
                .projectName(getProjectName())
                .startDate(getStartDate())
                .endDate(getEndDate())
                .budget(getBudget())
                .projectStatus(getProjectStatus())
                .updatedDate(getUpdatedDate())
                .build();
    }
}
