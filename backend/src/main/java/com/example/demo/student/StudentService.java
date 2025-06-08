package com.example.demo.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service//业务逻辑部分，标记业务逻辑组件
public class StudentService {
    private final StudentRepository studentRepository;
    @Autowired//Spring 启动时会自动扫描 StudentRepository，并生成实现类。
    //然后它会通过构造器把这个对象传给 StudentService，用了@Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }//因为 StudentRepository 继承了 JpaRepository，里面已经定义好了 findAll() 方法。
    //Spring Data JPA 自动实现了查找所有记录的功能。
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }
    public Student addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository
                .findStudentByEmail(student.getEmail());//这里使用了在 Repository 中定义的方法。
        //Spring 根据方法名 findStudentByEmail 自动匹配字段 email，并调用对应的 SQL 查询。
        //如果找到了，就说明邮箱已被注册，抛出异常。
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        Student savedStudent = studentRepository.save(student);
        System.out.println(savedStudent);
        return savedStudent;
    }
    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if(!exists){
            throw new IllegalStateException(
                    "student with id"+studentId+"does not exists"
            );
        }
        studentRepository.deleteById(studentId);
    }
    @Transactional
    public void updateStudent(Long studentId,
                              String name,
                              String email) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(()->new IllegalStateException(
                        "student with id"+studentId+"does not exists"
                ));
                if(name!=null&&
                name.length()>0&&
                !Objects.equals(student.getName(),name)){
                    student.setName(name);
                }
                if(email!=null&&
                email.length()>0&&
                !Objects.equals(student.getEmail(),email)){
                      Optional<Student>studentOptional=studentRepository
                              .findStudentByEmail(email);
                      if(studentOptional.isPresent()){
                          throw new IllegalStateException("email taken");
                      }
                      student.setEmail(email);
        }
                //@Transactional 开启了数据库事务；
        //Spring 会自动把通过 findById() 拿到的 student 放进持久化上下文（Persistence Context）；
        //在这个上下文中，任何对实体对象的修改都会被自动“感知”；
        //当方法结束时，Spring 会自动生成 UPDATE SQL 并提交事务
    }
}
