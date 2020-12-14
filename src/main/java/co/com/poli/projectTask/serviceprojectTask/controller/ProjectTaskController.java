package co.com.poli.projectTask.serviceprojectTask.controller;

import co.com.poli.projectTask.serviceprojectTask.domain.ProjectTask;
import co.com.poli.projectTask.serviceprojectTask.model.ErrorMessage;
import co.com.poli.projectTask.serviceprojectTask.services.ProjectTaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/task")
public class ProjectTaskController {

    private final ProjectTaskService projectTaskService;

    public ProjectTaskController(ProjectTaskService projectTaskService) {
        this.projectTaskService = projectTaskService;
    }

    @GetMapping(value = "/project/{projectidentifier}")
    public ResponseEntity<List<ProjectTask>> listProjectTaskByProject(@PathVariable("projectidentifier")
                                                                                  String projectIdentifier){
        log.info("Fetching ProjectTask with projectIdentifier {}", projectIdentifier);
        List<ProjectTask> projectTask = projectTaskService.getProjectTaskByProject(projectIdentifier);
        if(projectTask.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(projectTask);

    }

    @GetMapping(value ="/hours/project/{projectidentifier}")
    public  ResponseEntity<Double> sumHoursProjectTaskByStatusNotDelete(@PathVariable ("projectidentifier")
                                                                                    String projectIdentifier){
        log.info("Fetching ProjectTask with projectIdentifier {}", projectIdentifier);
        Double projectTask = projectTaskService.getHoursProject(projectIdentifier);
        if(projectTask == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(projectTask);
    }

    @GetMapping(value ="/hours/project/{projectidentifier}/{status}")
    public ResponseEntity<Double> sumHoursProjectTaskByStatus(@PathVariable ("projectidentifier") String projectIdentifier,
                                                              @PathVariable ("status") String status){
        log.info("Fetching ProjectTask with projectIdentifier {}", projectIdentifier);
        Double projectTask = projectTaskService.getHoursProjectByStatus(projectIdentifier, status);
        if(projectTask == 0){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(projectTask);
    }

    @PostMapping
    public  ResponseEntity<ProjectTask> createProjecTask(@Valid @RequestBody ProjectTask projectTask,
                                                         BindingResult result){
        log.info("create ProjectTask {}", projectTask);
        if(result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        ProjectTask projectTaskBD = projectTaskService.createProjectTask(projectTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectTaskBD);
    }

    @DeleteMapping(value = "/{idTask}/{projectidentifier}")
    public  ResponseEntity <ProjectTask> deleteProjectTask(@PathVariable("idTask") Long id,
                                                           @PathVariable("projectidentifier") String projectIdentifier){
        log.info("Fetching & Deleting Invoice with id {}", id);
        ProjectTask projectTaskID = projectTaskService.getProjectTaskById(id);
        if(projectTaskID == null){
            log.error("Unable to delete. ProjectTask with id {} not found.", id);
            return  ResponseEntity.notFound().build();
        }
        ProjectTask projectTask = projectTaskService.deleteProjectTask(id, projectIdentifier);
        return ResponseEntity.ok(projectTask);

    }

    private String formatMessage( BindingResult result){
        List<Map<String,String>> errors = result.getFieldErrors().stream()
                .map(err ->{
                    Map<String,String> error =  new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;

                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString="";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
