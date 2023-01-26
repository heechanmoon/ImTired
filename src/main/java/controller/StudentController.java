package controller;

import model.Student;
import util.ScannerUtil;

import java.sql.*;
import java.util.ArrayList;

public class StudentController {
    static PreparedStatement pstmt = null;
    static Connection connection = null;
    static ResultSet resultSet = null;

    public StudentController(){
        initialize();
    }

    public void initialize(){
        String address = "jdbc:mysql://localhost/basic";
        String username = "root";
        String password = "cs621";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(address, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void terminate() throws SQLException {
        if(resultSet != null){
            resultSet.close();
        }

        if(pstmt != null){
            pstmt.close();
        }

        if(connection != null){
            connection.close();
        }
    }

    public void insert(String query, Student s){
        try {
            pstmt = connection.prepareStatement(query);

            pstmt.setString(1,s.getName());
            pstmt.setInt(2,s.getKorean());
            pstmt.setInt(3,s.getEnglish());
            pstmt.setInt(4,s.getMath());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Student> selectAll(){
        String query = "select * from `student`";
        ArrayList<Student> list = new ArrayList<>();
        try {
            pstmt = connection.prepareStatement(query);
            resultSet = pstmt.executeQuery(query);
            while(resultSet.next()){
                Student s = new Student();
                s.setId(resultSet.getInt("id"));
                s.setName(resultSet.getString("name"));
                s.setKorean(resultSet.getInt("korean"));
                s.setEnglish(resultSet.getInt("english"));
                s.setMath(resultSet.getInt("math"));

                list.add(s);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public Student selectOne(int id){
        String query = "select * from `student` where `id` = ?";
        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1,id);
            resultSet = pstmt.executeQuery();

            if(resultSet.next()){
                Student s = new Student();
                s.setId(resultSet.getInt("id"));
                s.setName(resultSet.getString("name"));
                s.setKorean(resultSet.getInt("korean"));
                s.setEnglish(resultSet.getInt("english"));
                s.setMath(resultSet.getInt("math"));

                return s;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void update(int id, int korean, int english, int math){
        String query = "update `student` set `korean` = ?, `english` = ?, `math` = ? where `id` = ?";

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1,korean);
            pstmt.setInt(2,english);
            pstmt.setInt(3,math);
            pstmt.setInt(4,id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id){
        String query = "delete from `student` where `id` = ?";
        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
