package com.kuptel.Organization;

import com.kuptel.Organization.Model.Node;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Service(value="sql-source")
public class OrganizationSQLDataSource implements OrganizationDataSource{

    @Override
    public List<Node> getOrganizationStructure() {

        String url = "jdbc:postgresql://postgres:5432/organization";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "postgres");

        List<Node> result = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, props)) {

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
}
