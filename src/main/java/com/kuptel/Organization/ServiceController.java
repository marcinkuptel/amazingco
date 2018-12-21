package com.kuptel.Organization;

import com.kuptel.Organization.Repository.RepositoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The only REST controller in the project, exposing two endpoints:
 * 1) /v1/organization/{nodeId}/descendants - returns all descendants of node with id=<i>nodeId</i>.
 * 2) /v1/organization/{nodeId}/parent - changes the parent of node with id=<i>nodeId</i> to the parent whose id
 * is in the body of the request.
 */
@RestController
@RequestMapping(value = "/v1/organization")
public class ServiceController {

    private OrganizationService organizationService;
    private TaskExecutor executor;

    @Autowired
    public ServiceController(OrganizationService organizationService) {
        this.organizationService = organizationService;
        this.executor = new ThreadPoolTaskExecutor();
    }

    @RequestMapping(value = "/{nodeId}/descendants", method = RequestMethod.GET)
    public ResponseEntity<List<Node>> getDescendants(@PathVariable("nodeId") String nodeId) {
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
