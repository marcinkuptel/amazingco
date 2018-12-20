package com.kuptel.Organization.Repository;

import com.kuptel.Organization.Model.Node;
import java.util.List;

public interface OrganizationDataSource {
    List<Node> getOrganizationStructure();
    RepositoryResponse changeParentOfNode(String nodeId, String parentId);
}
