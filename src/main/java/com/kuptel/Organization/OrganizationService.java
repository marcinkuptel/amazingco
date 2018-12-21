package com.kuptel.Organization;

import com.kuptel.Organization.Exceptions.NodeUnknownException;
import com.kuptel.Organization.Exceptions.ParentUpdateFailedException;
import com.kuptel.Organization.Repository.Repository;
import com.kuptel.Organization.Repository.RepositoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Class containing the central business logic of the project.
 * It holds the whole structure of Amazing Co in memory and updates
 * it in response to user requests. Since we have one operation that
 * only reads and one that reads and writes, we use a read-write lock.
 *
 * This class also has a reference to a repository
 * where changes to organization structure should also be reflected.
 */
@Service
public class OrganizationService {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    private Repository dataSource;
    private List<Node> nodes;
    private Map<String, Node> nodeRef;

    @Autowired
    public OrganizationService(Repository dataSource) {
        this.dataSource = dataSource;
        loadOrganizationStructure();
    }

    /**
     * Method that returns all the descendants of the node identified
     * with <i>nodeId</i>. It uses BFS (Breadth First Search) algorithm
     * to walk the tree of nodes.
     *
     * @param nodeId Node for which we want to find descendants.
     * @return List of nodes that are descendants of <i>nodeId</i>.
     */
    public List<Node> getDescendantsOfNode(String nodeId) {

        readLock.lock();

        try {
            Node node = nodeRef.get(nodeId);
            if (node != null) {
                Queue<Node> queue = new LinkedList<>();
                queue.add(node);
                List<Node> result = new ArrayList<>();

                while(!queue.isEmpty()) {
                    Node n = queue.remove();
                    result.addAll(n.getChildren());
                    queue.addAll(n.getChildren());
                }

                return result;

            } else {
                throw new NodeUnknownException();
            }
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Method that changes the parent of node with id=<i>nodeId</i> to node with id=<i>newParentId</i>.
     *
     * @param nodeId Id of the node which should have its parent replaced.
     * @param newParentId Id of the node which should be the new parent of node with id=<i>nodeId</i>.
     * @return Whether the operation was successful.
     */
    @Async("asyncExecutor")
    public CompletableFuture<RepositoryResponse> changeParentOfNode(String nodeId, String newParentId) {

        writeLock.lock();

        try {
            Node node = nodeRef.get(nodeId);
            Node newParent = nodeRef.get(newParentId);

            if (node != null && newParent != null) {

                Node currentParent = nodeRef.get(node.getParent());
                currentParent.removeChild(node);
                newParent.addChild(node);
                node.setParent(newParentId);
                node.setHeight(newParent.getHeight() + 1);
                List<Map.Entry<String, Integer>> heightUpdates = updateHeightsForChildNodes(node);

                RepositoryResponse response = dataSource.changeParentOfNode(nodeId, newParentId,
                        newParent.getHeight() + 1, heightUpdates);

                if (response == RepositoryResponse.OK) {
                    return CompletableFuture.completedFuture(response);
                } else {
                    throw new ParentUpdateFailedException();
                }
            } else {
                throw new NodeUnknownException();
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Helper method to update heights of all descendants of <i>startNode</i>.
     * @param startNode Node whose descendatns should have their heights updated.
     * @return List of nodeId, height pairs used by <i>dataSource</i> to update
     * organization structure in the repository.
     */
    private List<Map.Entry<String, Integer>> updateHeightsForChildNodes(Node startNode) {
        List<Map.Entry<String, Integer>> heightUpdates = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();
        queue.add(startNode);

        while(!queue.isEmpty()) {
            Node node = queue.remove();
            for(Node n : node.getChildren()) {
                n.setHeight(node.getHeight() + 1);
            }
            queue.addAll(node.getChildren());
            heightUpdates.add(new SimpleEntry<>(node.getId(), node.getHeight() + 1));
        }
        return heightUpdates;
    }

    /**
     * Loads organization structure from the available repository.
     */
    private void loadOrganizationStructure() {

        writeLock.lock();

        try {
            nodes = dataSource.getOrganizationStructure();
            nodeRef = nodes.stream().collect(Collectors.toMap(node -> node.getId(), node -> node));

            for(Node n : nodes){
                Node parent = nodeRef.get(n.getParent());
                if (parent != null) {
                    parent.addChild(n);
                }
            }
        } finally {
            writeLock.unlock();
        }
    }
}
