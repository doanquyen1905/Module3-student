package com.example.student.repositories.impl;

import com.example.student.dto.StudentDTO;
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
    public List<StudentDTO> findAll() {
        List<StudentDTO> students = new ArrayList<>();
        long id;
        String name;
        String address;
        Float point;
        String nameClass;
        try {
            PreparedStatement preparedStatement = BaseRepository.getConnection().prepareStatement("select id,student.name,address,point,classroom.name as name_class  from student join classroom on student.id_class = classroom.id_class");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                id = resultSet.getLong("id");
                name = resultSet.getString("name");
                address = resultSet.getString("address");
                point = resultSet.getFloat("point");
                nameClass = resultSet.getString("name_class");
                students.add(new StudentDTO(id,name,address,point,nameClass));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return students;
    }

    @Override
    public void save(Student student) {
        try {
            PreparedStatement preparedStatement = BaseRepository.getConnection().prepareStatement("insert into student(name,address,point,id_class) values (?,?,?,?)");
            preparedStatement.setString(1, student.getName());
            preparedStatement.setString(2, student.getAddress());
            preparedStatement.setFloat(3, student.getPoint());
            preparedStatement.setLong(4,student.getIdClass());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean deleteById(Long id) {
        boolean isDelete;
        try {
            PreparedStatement statement = BaseRepository.getConnection().prepareStatement("delete from student where id=?;");
            statement.setLong(1, id);
            isDelete = statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return isDelete;
    }

    @Override
    public List<StudentDTO> findByName(String name) {
        List<StudentDTO> result = new ArrayList<>();
        try {
            PreparedStatement statement = BaseRepository.getConnection().prepareStatement("select student.id,student.name, student.address,student.point, classroom.name as name_class from student join classroom on classroom.id_class = student.id_class where student.name like CONCAT('%',?,'%')");
            statement.setString(1,"%"+ name+"%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String studentName = resultSet.getString("name");
                String address = resultSet.getString("address");
                float point = resultSet.getFloat("point");
                String nameClass = resultSet.getString("name_class");
                result.add(new StudentDTO(id, studentName, address, point,nameClass));
            }
//
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public StudentDTO findById(long id) {
        StudentDTO studentDTO = null;
        try {
            PreparedStatement statement = BaseRepository.getConnection()
                    .prepareStatement("select id, student.name, address, point, c.name AS name_class from student join classroom c on c.id_class = student.id_class WHERE id=?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                float point = resultSet.getFloat("point");
                String nameClass = resultSet.getString("name_class");
                studentDTO = new StudentDTO(id, name, address, point,nameClass);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return studentDTO;
    }

    @Override
    public void update(long id, StudentDTO student) {
            try {
                PreparedStatement preparedStatement = BaseRepository.getConnection()
                        .prepareStatement("UPDATE student SET name=?, address=?, point=?, id_class=? WHERE id=?");
                preparedStatement.setString(1, student.getName());
                preparedStatement.setString(2, student.getAddress());
                preparedStatement.setFloat(3, student.getPoint());
                preparedStatement.setLong(4,student.getIdClass());
                preparedStatement.setLong(5, id);
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated == 0) {
                    throw new SQLException("Update student failed, no rows affected.");
                }

                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }
}
