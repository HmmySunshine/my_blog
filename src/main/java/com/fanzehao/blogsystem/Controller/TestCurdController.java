package com.fanzehao.blogsystem.Controller;

import com.fanzehao.blogsystem.model.TestStudent;
import com.fanzehao.blogsystem.pojo.StudentJo;
import com.fanzehao.blogsystem.repository.StudentRepository;
import com.fanzehao.blogsystem.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TestCurdController {
    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/getStudents")
    public ResponseEntity<List<TestStudent>> getStudents(){
        return ResponseEntity.ok(studentRepository.findAll());
    }

    @DeleteMapping("/deleteStudent/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id){
        studentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addStudent")
    public ResponseEntity<TestStudent> addStudent(@RequestBody StudentJo studentJo){
        TestStudent testStudent = new TestStudent();
        testStudent.setAge(studentJo.getAge());
        testStudent.setStuName(studentJo.getName());
        System.out.println(studentJo.getGender());
        if (studentJo.getGender().equals("1")) {
            testStudent.setGender("男");
        } else {
            testStudent.setGender("女");
        }

        try {
            testStudent.setEnterTime(Utils.parseStringToTimeStamp(studentJo.getDate()));
            studentRepository.save(testStudent);
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(testStudent);

    }
}
