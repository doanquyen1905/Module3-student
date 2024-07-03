package com.example.student.repositories.impl;

import com.example.student.models.Student;
import com.example.student.repositories.IStudentRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository implements IStudentRepository {

//
//    static {
//        students = new ArrayList<>();
//        students.add(new Student(1L, "haiTT", "QN", 10.0f));
//        students.add(new Student(2L, "Bảo Ngọc", "HN", 8.0f));
//        students.add(new Student(3L, "Bảo Kỳ", "DN", 6.0f));
//        students.add(new Student(5L, "Cook", "Bàn ăn", 2f));
//    }
    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = BaseRepository.getConnection().prepareStatement("select * from classroom");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                Float point = resultSet.getFloat("point");
                students.add(new Student(id,name,address,point));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return students;
    }

    @Override
    public void save(Student student) {
        try {
            PreparedStatement preparedStatement = BaseRepository.getConnection().prepareStatement("insert into classroom(name,address,point) values (?,?,?)");

            preparedStatement.setString(1, student.getName());
            preparedStatement.setString(2, student.getAddress());
            preparedStatement.setFloat(3, student.getPoint());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean deleteById(Long id) {
        boolean isDelete;
        try {
            PreparedStatement statement = BaseRepository.getConnection().prepareStatement("delete from classroom where id=?;");
            statement.setLong(1, id);
            isDelete = statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return isDelete;
    }

    @Override
    public List<Student> findByName(String name) {
        List<Student> result = new ArrayList<>();
        try {
            PreparedStatement statement = BaseRepository.getConnection().prepareStatement("select * from classroom where name like ?");
            statement.setString(1,"%"+ name+"%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String studentName = resultSet.getString("name");
                String address = resultSet.getString("address");
                float point = resultSet.getFloat("point");

                result.add(new Student(id, studentName, address, point));
            }
//            resultSet.close();
//            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public Student findById(long id) {
        Student student = null;
        try {
            PreparedStatement statement = BaseRepository.getConnection()
                    .prepareStatement("SELECT * FROM classroom WHERE id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                float point = resultSet.getFloat("point");

                student = new Student(id, name, address, point);
            }

//            resultSet.close();
//            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return student;
    }

    @Override
    public void update(long id, Student student) {
            try {
                PreparedStatement preparedStatement = BaseRepository.getConnection()
                        .prepareStatement("UPDATE student SET name=?, address=?, point=? WHERE id=?");
                preparedStatement.setString(1, student.getName());
                preparedStatement.setString(2, student.getAddress());
                preparedStatement.setFloat(3, student.getPoint());
                preparedStatement.setLong(4, id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }
}
