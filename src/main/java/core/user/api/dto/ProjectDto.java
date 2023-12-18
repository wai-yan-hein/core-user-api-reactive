package core.user.api.dto;

import core.user.api.model.Project;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class ProjectDto {
    private ProjectKey key;
    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double budget;
    private String projectStatus;
    private LocalDateTime updatedDate;
    public Project toEntity(){
        return Project.builder()
                .projectNo(getKey().getProjectNo())
                .compCode(getKey().getCompCode())
                .projectName(getProjectName())
                .startDate(getStartDate())
                .endDate(getEndDate())
                .budget(getBudget())
                .projectStatus(getProjectStatus())
                .updatedDate(getUpdatedDate())
                .build();
    }
}
