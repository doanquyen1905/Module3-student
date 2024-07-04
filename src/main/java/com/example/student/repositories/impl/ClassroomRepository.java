package com.example.student.repositories.impl;

import com.example.student.models.Classroom;
import com.example.student.repositories.IClassroomRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClassroomRepository implements IClassroomRepository {
    @Override
    public List<Classroom> findAll() {
        List<Classroom> classrooms = new ArrayList<>();
        long id;
        String name;
        String address;
        Float point;
        String nameClass;
        try {
            PreparedStatement preparedStatement = BaseRepository.getConnection()
                    .prepareStatement("select * from student_demo.classroom ");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                id = resultSet.getLong("id_class");
                name = resultSet.getString("name");
                classrooms.add(new Classroom(id,name));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return classrooms;
    }
}
