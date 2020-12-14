package co.com.poli.projectTask.serviceprojectTask.services;

import co.com.poli.projectTask.serviceprojectTask.domain.ProjectTask;

import java.util.List;

public interface ProjectTaskService {
    ProjectTask createProjectTask(ProjectTask projectTask);
    ProjectTask deleteProjectTask(Long idTask, String projectIdentifier);
    ProjectTask getProjectTaskByName(String name);
    ProjectTask getProjectTaskByIdentifier(String projectIdentifier);
    List<ProjectTask> getProjectTaskByProject(String projectI);
    Double getHoursProject (String projectI);
    Double getHoursProjectByStatus (String projectI, String status);
}
