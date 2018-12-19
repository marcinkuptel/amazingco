package com.kuptel.Organization;

import com.kuptel.Organization.Model.Node;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Service(value="sql-source")
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

        try (Connection conn = DriverManager.getConnection(DB_URL, props)) {

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM amazingco");
            while (rs.next()) {
                result.add(new Node(rs.getString(1),
                                    rs.getString(2),
                                    rs.getString(3),
                                    rs.getInt(4)));
            }
            rs.close();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        return result;
    }

    @Override
    public boolean changeParentOfNode(String nodeId, String parentId) {

        try(Connection conn = DriverManager.getConnection(DB_URL, props)) {

            PreparedStatement st = conn.prepareStatement(
                    "UPDATE amazingco set parent = ? WHERE ID = ?");
            st.setString(1, parentId);
            st.setString(2, nodeId);
            return st.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
