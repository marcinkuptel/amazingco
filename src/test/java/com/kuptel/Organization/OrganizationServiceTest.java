package com.kuptel.Organization;

import com.kuptel.Organization.Repository.Repository;
import com.kuptel.Organization.Repository.RepositoryResponse;
import com.kuptel.Organization.Repository.SQLRepository;
import com.kuptel.Organization.Service.OrganizationService;
import com.kuptel.Organization.Service.ServiceResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrganizationServiceTest {

    private OrganizationService organizationService;
    private Repository repository;
    private List<Node> orgStructure = Arrays.asList(
            new Node("root", "null", "root", 0),
            new Node("a", "root", "root", 1),
            new Node("b", "root", "root", 1),
            new Node("c", "a", "root", 2)
    );

    @Before
    public void setup() {
        repository = mock(SQLRepository.class);
        when(repository.getOrganizationStructure()).thenReturn(orgStructure);
        organizationService = new OrganizationService(repository);
    }

    @Test
    public void getDescendantsOfNode_whenValidNodeIdProvided_returnListOfDescendants() {
        List<Node> descendantsOfRoot = organizationService.getDescendantsOfNode("root");
        assertEquals(Arrays.asList(orgStructure.get(1), orgStructure.get(2), orgStructure.get(3)),
                descendantsOfRoot);

        List<Node> descendantsOfA = organizationService.getDescendantsOfNode("a");
        assertEquals(Arrays.asList(orgStructure.get(3)), descendantsOfA);
    }

    @Test
    public void changeParentOfNode_whenValidNodesAreProvided_changeOrgStructureCorrectly() {
        when(repository.changeParentOfNode(eq("b"), eq("c"), eq(3), any()))
                .thenReturn(RepositoryResponse.OK);

        List<Node> descendantsOfA = organizationService.getDescendantsOfNode("a");
        assertEquals(Arrays.asList(orgStructure.get(3)), descendantsOfA);

        ServiceResponse response = organizationService.changeParentOfNode("b", "c");
        assertEquals(ServiceResponse.OK, response);

        List<Node> newDescendantsOfA = organizationService.getDescendantsOfNode("a");
        assertEquals(Arrays.asList(orgStructure.get(3), orgStructure.get(2)), newDescendantsOfA);
    }

    @Test
    public void changeParentOfNode_whenNewParentIsDescendant_returnViolationStatus() {
        ServiceResponse response = organizationService.changeParentOfNode("a", "c");
        assertEquals(ServiceResponse.ANCESTOR_VIOLATION, response);
    }
}
