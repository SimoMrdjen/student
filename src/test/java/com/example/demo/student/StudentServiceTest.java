package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    StudentService studentService;
    @Mock
    StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
       studentService = new StudentService(studentRepository);
    }

    @Test
    void shouldGetAllStudents() {
//        String email = "jamila@gmail.com";
//        Student student = new Student(
//                "Jamila",
//                email,
//                Gender.FEMALE
//        );
//        when(studentRepository.findAll()).thenReturn(List.of(student));
//        assertEquals(studentService.getAllStudents(),List.of(student));

        studentService.getAllStudents();
        verify(studentRepository).findAll();
    }

    @Test
    void shouldAddStudentIfEmailDoesntExist() {
        String email = "jamila@gmail.com";

        Student student = new Student(
                "Jamila",
                "dr@gmail.com",
                Gender.FEMALE
        );
        //when(studentRepository.selectExistsEmail(email)).thenReturn(false);
        studentService.addStudent(student);
        ArgumentCaptor<Student> studentArgumentCaptor =
                ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).save(studentArgumentCaptor.capture());

        assertThat(student).isEqualTo(studentArgumentCaptor.getValue());

    }

    @Test
    void shouldThrowInAddStudentIfEmailtExist() {

        Student student = new Student(
                "Jamila",
                "dr@gmail.com",
                Gender.FEMALE
        );
        studentService.addStudent(student);
        when(studentRepository.selectExistsEmail(student.getEmail()))
                .thenReturn(true);

        assertThatThrownBy(() -> studentService.addStudent(student))
                .isExactlyInstanceOf(BadRequestException.class);

    }

    @Test
    void ShouldDeleteStudentIfExist() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        studentService.deleteStudent(1L);

        verify(studentRepository).deleteById(1L);
    }

    @Test
    void IfCallDeleteStudentIfNotExistShouldThrowExc() {
        when(studentRepository.existsById(anyLong())).thenReturn(false);

        assertThatThrownBy(() -> studentService.deleteStudent(anyLong())).isExactlyInstanceOf(StudentNotFoundException.class);
    }
}