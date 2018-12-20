package com.kuptel.Organization.Repository;

import com.kuptel.Organization.Model.Node;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

@Service
public class OrganizationSQLDataSource implements OrganizationDataSource {

    private static String DB_URL = "jdbc:postgresql://postgres:5432/organization";
    private static String USERNAME = "postgres";
    private static String PASSWORD = "postgres";
    private static Properties props = new Properties();
    static {
        props.setProperty("user", USERNAME);
        props.setProperty("password", PASSWORD);
    }

    @Override
    public List<Node> getOrganizationStructure() {
        List<Node> result = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, props);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM amazingco");) {

            while (rs.next()) {
                result.add(new Node(rs.getString(1),
                                    rs.getString(2),
                                    rs.getString(3),
                                    rs.getInt(4)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return result;
    }

    @Override
    public RepositoryResponse changeParentOfNode(String nodeId, String parentId, int newHeight,
                                                 List<Map.Entry<String, Integer>> heightUpdates) {

        try(Connection conn = DriverManager.getConnection(DB_URL, props);
            PreparedStatement parentAndHeight =
                    conn.prepareStatement("UPDATE amazingco set parent = ?, height = ? WHERE ID = ?");
            PreparedStatement height =
                    conn.prepareStatement("UPDATE amazingco set height = ? WHERE parent = ?")) {

            conn.setAutoCommit(false);

            parentAndHeight.setString(1, parentId);
            parentAndHeight.setInt(2, newHeight);
            parentAndHeight.setString(3, nodeId);
            parentAndHeight.executeUpdate();

            for(Map.Entry<String, Integer> entry : heightUpdates) {
                height.setString(2, entry.getKey());
                height.setInt(1, entry.getValue().intValue());
                height.executeUpdate();
            }

            conn.commit();

            return RepositoryResponse.OK;

        } catch (SQLException e) {
            e.printStackTrace();
            return RepositoryResponse.UPDATE_FAILED;
        }
    }
}
