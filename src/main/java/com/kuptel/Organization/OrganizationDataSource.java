package com.kuptel.Organization;

import com.kuptel.Organization.Model.Node;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OrganizationDataSource {
    CompletableFuture<List<Node>> getOrganizationStructure();
}
