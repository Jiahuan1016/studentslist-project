package com.example.demo.student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
@CrossOrigin(origins = "https://studentlist-frontend.onrender.com")
@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {
    private final StudentService studentService;
    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    @GetMapping
    public List<Student> getStudents() {
        return studentService.getStudents();
    }
    @PostMapping
    public ResponseEntity<Student> registerNewStudent(@RequestBody Student student) {
        try {
            Student savedStudent = studentService.addNewStudent(student);
            System.out.println(savedStudent);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @DeleteMapping(path="{studentId}")
    public void deleteStudent(@PathVariable("studentId") Long studentId){
        studentService.deleteStudent(studentId);
    }
    @PutMapping(path="{studentId}")
    public void updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam(required = false)String name,
            @RequestParam(required = false)String email){
        studentService.updateStudent(studentId,name,email);
    }
}