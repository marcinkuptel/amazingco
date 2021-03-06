package com.kuptel.Organization.Repository;

import com.kuptel.Organization.Node;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

/**
 * Concrete implementation of the <i>Repository</i> interface
 * based on a PostgreSQL database.
 */
@Service
public class SQLRepository implements Repository {

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

            parentAndHeight.setObject(1, UUID.fromString(parentId));
            parentAndHeight.setInt(2, newHeight);
            parentAndHeight.setObject(3, UUID.fromString(nodeId));
            parentAndHeight.executeUpdate();

            for(Map.Entry<String, Integer> entry : heightUpdates) {
                height.setObject(2, UUID.fromString(entry.getKey()));
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
