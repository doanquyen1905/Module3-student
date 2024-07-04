package com.example.student.controllers;

import com.example.student.dto.StudentDTO;
import com.example.student.models.Classroom;
import com.example.student.models.Student;
import com.example.student.services.IClassroomService;
import com.example.student.services.IStudentService;
import com.example.student.services.impl.ClassroomService;
import com.example.student.services.impl.StudentService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "StudentController", value = "/student")
public class StudentControllers extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final IStudentService studentService = new StudentService();
    private static final IClassroomService classroomService = new ClassroomService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create":
                List<Classroom> classrooms = classroomService.findAll();
                req.setAttribute("classrooms",classrooms);
                req.getRequestDispatcher("/student/create.jsp").forward(req, resp);
                break;
            case "edit":
                editShowForm(req, resp);
                break;
            default:
                List<StudentDTO> allStudents = studentService.findAll();
                req.setAttribute("students", allStudents);
                req.getRequestDispatcher("/student/list.jsp").forward(req, resp);
                break;
        }
    }

    private void editShowForm(HttpServletRequest req, HttpServletResponse resp) {
        long id = Long.parseLong(req.getParameter("id"));
        StudentDTO student = studentService.findById(id);
        List<Classroom> editClassrooms = classroomService.findAll();
        req.setAttribute("student", student);
        req.setAttribute("classrooms", editClassrooms);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/student/edit.jsp");
        try {
            dispatcher.forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String action = req.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create":
                String studentName = req.getParameter("name");
                String address = req.getParameter("address");
                Long idClass = Long.parseLong(req.getParameter("classroom"));
                Float point = null;
                if (req.getParameter("point") != null) {
                    try {
                        point = Float.parseFloat(req.getParameter("point"));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                if (studentName != null && address != null && point != null) {
                    Student student = new Student(studentName,address,point,idClass);
                    studentService.save(student);
                } else {
                    req.setAttribute("message", "Thông tin không hợp lệ. Vui lòng điền đầy đủ các trường.");
                }
                resp.sendRedirect(req.getContextPath() + "/student");
                break;
            case "delete":
                Long studentId = null;
                if (req.getParameter("id") != null) {
                    try {
                        studentId = Long.parseLong(req.getParameter("id"));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                if (studentId != null) {
                    boolean isDeleted = studentService.deleteById(studentId);
                    if (!isDeleted) {
                        req.setAttribute("message", "Xóa không thành công.");
                    }
                } else {
                    req.setAttribute("message", "ID không hợp lệ.");
                }
                resp.sendRedirect(req.getContextPath() + "/student");
                break;
            case "search":
                String search = req.getParameter("search");
                List<StudentDTO> students = studentService.findByName(search);
                req.setAttribute("students", students);
                req.getRequestDispatcher("/student/list.jsp").forward(req, resp);
                break;
            case "edit":
                updateStudent(req, resp);
            default:
                resp.sendRedirect(req.getContextPath() + "/student");
                break;
        }
    }

    private void updateStudent(HttpServletRequest req, HttpServletResponse resp) {
        long id = Long.parseLong(req.getParameter("id"));
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        Float point = Float.parseFloat(req.getParameter("point"));
        Long idClass = Long.parseLong(req.getParameter("classroom"));
       StudentDTO student = studentService.findById(id);
        if (student != null) {
            student.setName(name);
            student.setAddress(address);
            student.setPoint(point);
            student.setIdClass(idClass);
            studentService.update(id, student);
            req.setAttribute("student", student);
            req.setAttribute("message", "Cập nhật thành công");
            RequestDispatcher dispatcher = req.getRequestDispatcher("student/edit.jsp");
            try {
                dispatcher.forward(req, resp);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        } else {
            req.setAttribute("message", "Sinh viên không tồn tại");
            RequestDispatcher dispatcher = req.getRequestDispatcher("student/edit.jsp");
            try {
                dispatcher.forward(req, resp);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        }
    }

}
