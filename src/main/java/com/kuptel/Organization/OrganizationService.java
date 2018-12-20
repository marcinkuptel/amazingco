package com.kuptel.Organization;

import com.kuptel.Organization.Exceptions.NodeUnknownException;
import com.kuptel.Organization.Exceptions.ParentUpdateFailedException;
import com.kuptel.Organization.Model.Node;
import com.kuptel.Organization.Repository.OrganizationDataSource;
import com.kuptel.Organization.Repository.RepositoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Service
public class OrganizationService {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    private OrganizationDataSource dataSource;
    private List<Node> nodes;
    private Map<String, Node> nodeRef;

    @Autowired
    public OrganizationService(@Qualifier("sql-source") OrganizationDataSource dataSource) {
        this.dataSource = dataSource;
    }

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

    @Async("asyncExecutor")
    public CompletableFuture<RepositoryResponse> changeParentOfNode(String nodeId, String newParentId) {

        writeLock.lock();

        try {
            Node node = nodeRef.get(nodeId);
            Node newParent = nodeRef.get(newParentId);

            if (node != null && newParent != null) {

                RepositoryResponse response = dataSource.changeParentOfNode(nodeId, newParentId);

                if (response == RepositoryResponse.OK) {
                    Node currentParent = nodeRef.get(node.getParent());
                    currentParent.removeChild(node);
                    newParent.addChild(node);
                    node.setParent(newParentId);
                    node.setHeight(newParent.getHeight() + 1);
                    updateHeightsForChildNodes(node);

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

    private void updateHeightsForChildNodes(Node startNode) {
        Queue<Node> queue = new LinkedList<>();
        queue.add(startNode);

        while(!queue.isEmpty()) {
            Node node = queue.remove();
            for(Node n : node.getChildren()) {
                n.setHeight(node.getHeight() + 1);
            }
            queue.addAll(node.getChildren());
        }
    }

    @PostConstruct
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
