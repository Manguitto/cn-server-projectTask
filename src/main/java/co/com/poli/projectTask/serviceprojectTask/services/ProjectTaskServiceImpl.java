package co.com.poli.projectTask.serviceprojectTask.services;


import co.com.poli.projectTask.serviceprojectTask.domain.ProjectTask;
import co.com.poli.projectTask.serviceprojectTask.repository.ProjectTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectTaskServiceImpl implements ProjectTaskService {
    final ProjectTaskRepository projectTaskRepository;


    public ProjectTaskServiceImpl(ProjectTaskRepository projectTaskRepository) {
        this.projectTaskRepository = projectTaskRepository;
    }

    @Override
    public ProjectTask createProjectTask(ProjectTask projectTask) {
        ProjectTask projectTaskName = getProjectTaskByName(projectTask.getNamePT());
        if (projectTaskName != null) {
            return projectTaskName;
        }
        return projectTaskRepository.save(projectTask);
    }

    @Override
    public ProjectTask deleteProjectTask(Long idTask, String projectIdentifier) {
        ProjectTask projectTaskProjectIdentifier = getProjectTaskByIdentifier(projectIdentifier);

        if (projectTaskProjectIdentifier != null && projectTaskProjectIdentifier.getId() == idTask) {
            projectTaskProjectIdentifier.setStatus("deleted");
            return projectTaskRepository.save(projectTaskProjectIdentifier);
        }
        return null;
    }

    @Override
    public ProjectTask getProjectTaskByName(String name) {
        ProjectTask projectTask = projectTaskRepository.findAllByNamePT(name);
        if (projectTask != null) {
            return projectTask;
        }
        return null;
    }

    @Override
    public ProjectTask getProjectTaskByIdentifier(String projectIdentifier) {
        ProjectTask projectTask = projectTaskRepository.findByProjectIdentifier(projectIdentifier);
        if (projectTask != null) {
            return projectTask;
        }
        return null;
    }

    @Override
    public ProjectTask getProjectTaskById(Long id) {
        Optional<ProjectTask> projectTask = projectTaskRepository.findById(id);
        if(projectTask.isPresent()){
            return  projectTask.get();
        }
        return null;
    }

    @Override
    public List<ProjectTask> getProjectTaskByProject(String projectI) {
        List<ProjectTask> projectTasks = projectTaskRepository.findAllByProjectIdentifier(projectI);
        if (projectTasks.isEmpty()) {
            return null;
        }
        return projectTasks;
    }

    @Override
    public Double getHoursProject(String projectI) {
        List<ProjectTask> projectTasks = projectTaskRepository.findAllByProjectIdentifier(projectI);
        if (!projectTasks.isEmpty()) {
            Double sumProjectTask = projectTasks.stream().filter(result -> !result.getStatus().equals("deleted"))
                    .map(result -> result.getHours()).collect(Collectors.summingDouble(x -> x));
            return sumProjectTask;
        }
        return null;
    }

    @Override
    public Double getHoursProjectByStatus(String projectI, String status) {
        List<ProjectTask> projectTasks = projectTaskRepository.findAllByProjectIdentifierAndStatus(projectI, status);
        if (!projectTasks.isEmpty()) {
            Double sumProjectTask = projectTasks.stream()
                    .map(result -> result.getHours()).collect(Collectors.summingDouble(x -> x));
            return sumProjectTask;
        }
        return null;
    }
}
