package com.kuptel.Organization;

import com.kuptel.Organization.Model.Node;
import java.util.List;

public interface OrganizationDataSource {
    List<Node> getOrganizationStructure();
}
