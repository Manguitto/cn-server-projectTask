package co.com.poli.projectTask.serviceprojectTask.repository;

import co.com.poli.projectTask.serviceprojectTask.domain.ProjectTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Long> {
    ProjectTask findByProjectIdentifier(String projectIdentifier);
    ProjectTask findAllByNamePT(String namePT);
    List<ProjectTask> findAllByProjectIdentifier(String projectIdentifier);
    List<ProjectTask> findAllByProjectIdentifierAndStatus(String projectIdentifier, String status);
}
