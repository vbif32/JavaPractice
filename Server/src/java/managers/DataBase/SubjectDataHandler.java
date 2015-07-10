package managers.DataBase;

import java.sql.*;
import java.util.ArrayList;

class SubjectDataHandler {
    private static String getNumberOfLabs = "SELECT number_of_labs FROM subject_table WHERE subject_id = ? AND term = ?";
    private static String getGroupsStudying = "SELECT groups_studying FROM subject_table WHERE subject_id =? AND term = ?";
    private PreparedStatement stm;
    private Connection connection;
    private ResultSet res;

    SubjectDataHandler(Connection connect) {
        this.connection = connect;
    }

    int GetNumberOfLabs(int subjectId, int term) {
        try {
            stm = connection.prepareStatement(getNumberOfLabs);
            stm.setInt(1, subjectId);
            stm.setInt(2, term);
            res = stm.executeQuery();
            while (res.next()) {
                return res.getInt("number_of_labs");
            }
            return 0;
        } catch (SQLException e) {
            return 0;
        }
    }

    ArrayList<String> GetGroupsStudying(int subjectId, int term) {
        try {
            GroupTableHandler groupHandler = new GroupTableHandler(connection);
            ArrayList<String> groups = new ArrayList<>();
            stm = connection.prepareStatement(getGroupsStudying);
            stm.setInt(1, subjectId);
            stm.setInt(2, term);
            res = stm.executeQuery();
            while (res.next()) {
                Array temp = res.getArray("groups_studying");
                Integer[] temp2 = (Integer[]) temp.getArray();
                for (int group : temp2) {
                    String groupName = groupHandler.getGroupName(group);
                    if (groupName != null) {
                        groups.add(groupName);
                    }
                }
                return groups;
            }
            return null;
        } catch (SQLException e) {
            return null;
        }
    }
}
