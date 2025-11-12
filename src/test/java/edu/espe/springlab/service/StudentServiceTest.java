package edu.espe.springlab.service;

import edu.espe.springlab.domain.Student;
import edu.espe.springlab.dto.StudentRequestData;
import edu.espe.springlab.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import({StudentService.class}) // Importa el servicio real, no la clase de test
public class StudentServiceTest {

    @Autowired
    private StudentService service;

    @Autowired
    private StudentRepository repository;

    @Test
    void shouldNotAllowDuplicateEmail() {
        Student existing = new Student();
        existing.setFullName("Existing");
        existing.setEmail("duplicate@example.com");
        existing.setBirthDate(LocalDate.of(1990, 2, 2));
        existing.setActive(true);

        repository.save(existing);

        StudentRequestData req = new StudentRequestData();
        req.setFullName("New User Dup");
        req.setEmail("duplicate@example.com");
        req.setBirthDate(LocalDate.of(1990, 2, 2));


        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email already exists");
    }
}
