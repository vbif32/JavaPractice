package managers.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Kiril on 10.07.2015.
 */
class GroupTableHandler {

    private static String RetrieveNameCommand = "SELECT group_name FROM group_data WHERE group_id = ?";
    private static String RetreiveIdCommand = "SELECT group_id FROM group_data WHERE group_name = ?";
    private PreparedStatement stm;
    private Connection connection;
    private ResultSet res;

    GroupTableHandler(Connection connect){
        connection = connect;
    }

    String getGroupName(int groupId){
        String name;
        try{
            stm = connection.prepareStatement(RetrieveNameCommand);
            stm.setInt(1,groupId);
            res = stm.executeQuery();
            while(res.next()){
                name = res.getString("group_name");
                return name;
            }
        }catch(SQLException e){
            return null;
        }
        return null;
    }

    boolean checkCorrectness(String name){
        try{
            stm = connection.prepareStatement(RetreiveIdCommand);
            stm.setString(1,name);
            res=stm.executeQuery();
            while(res.next()){
                return true;
            }
            return false;
    }catch(SQLException e){
            return false;
        }
    }

    int getGroupId(String GroupName){
        try{
            stm = connection.prepareStatement(RetreiveIdCommand);
            stm.setString(1,GroupName);
            res=stm.executeQuery();
            while(res.next()){
                return res.getInt("group_id");
            }
            return 0;
        }catch(SQLException e){
            return 0;
        }
    }

}
