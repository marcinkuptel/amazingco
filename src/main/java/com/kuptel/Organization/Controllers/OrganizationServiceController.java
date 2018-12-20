package com.kuptel.Organization.Controllers;

import com.kuptel.Organization.Model.Node;
import com.kuptel.Organization.OrganizationService;
import com.kuptel.Organization.Repository.RepositoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/v1/organization")
public class OrganizationServiceController {

    private OrganizationService organizationService;
    private TaskExecutor executor;

    @Autowired
    public OrganizationServiceController(OrganizationService organizationService) {
        this.organizationService = organizationService;
        this.executor = new ThreadPoolTaskExecutor();
    }

    @RequestMapping(value = "/{nodeId}/children", method = RequestMethod.GET)
    public ResponseEntity<List<Node>> getChildren(@PathVariable("nodeId") String nodeId) {
        List<Node> descendantsOfNode = organizationService.getDescendantsOfNode(nodeId);
        return new ResponseEntity<>(descendantsOfNode, HttpStatus.OK);
    }

    @RequestMapping(value = "/{nodeId}/parent", method = RequestMethod.POST)
    public CompletableFuture<ResponseEntity> changeParent(
            @PathVariable("nodeId") String nodeId,
            @RequestBody String newParentId) {

        return organizationService.changeParentOfNode(nodeId, newParentId)
                .thenApply(response -> new ResponseEntity(response == RepositoryResponse.OK ?
                        HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR));

    }
}
