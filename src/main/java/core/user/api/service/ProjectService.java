package core.user.api.service;

import core.user.api.common.Util1;
import core.user.api.dto.ProjectDto;
import core.user.api.model.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final R2dbcEntityTemplate template;
    private final SeqService seqService;

    public Flux<ProjectDto> findAll(String compCode) {
        return template.select(Project.class).matching(Query.query(Criteria.where("comp_code")
                        .is(compCode)))
                .all().map(Project::buildDto);
    }

    public Mono<ProjectDto> saveOrUpdate(ProjectDto dto) {
        Project project = dto.toEntity();
        String projectNo = project.getProjectNo();
        project.setUpdatedDate(LocalDateTime.now());
        if (Util1.isNullOrEmpty(projectNo)) {
            return seqService.getNextCode("Project", 5).flatMap(seqNo -> {
                project.setProjectNo(seqNo);
                return template.insert(project).map(Project::buildDto);
            });
        }
        return template.update(project).map(Project::buildDto);
    }

    public Mono<ProjectDto> findById(String projectNo, String compCode) {
        return template.select(Project.class)
                .matching(Query.query(Criteria.where("project_no")
                        .is(projectNo).and("comp_code").is(compCode)))
                .one().map(Project::buildDto);
    }

    public Flux<ProjectDto> getProjectByDate(String updatedDate) {
        return template.select(Project.class)
                .matching(Query.query(Criteria.where("updated_date")
                        .greaterThanOrEquals(updatedDate)))
                .all()
                .map(Project::buildDto);
    }

    public Flux<ProjectDto> autoCompete(String code, String compCode) {
        return template.select(Project.class)
                .matching(Query.query(Criteria.where("project_no")
                        .like(code)
                        .and("comp_code").is(compCode)))
                .all()
                .map(Project::buildDto);
    }
}
