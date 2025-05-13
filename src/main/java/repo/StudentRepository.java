package repo;

import entity.Student;

import java.io.*;
import java.util.*;

public class StudentRepository {

    private static final String DIRECTORY_PATH = "C:\\studentData";
    private static final String FILE_PATH = DIRECTORY_PATH + "\\student-data.txt";

    // Save a student
    public void saveStudent(Student student) {
        createDirectoryIfNeeded();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(formatStudent(student));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get all students
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return students;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Student student = parseStudent(line);
                if (student != null) students.add(student);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return students;
    }

    // Delete student by ID
    public boolean deleteStudentById(long id) {
        List<Student> students = getAllStudents();
        boolean removed = students.removeIf(s -> s.getId() == id);
        if (removed) {
            writeAllStudents(students);
        }
        return removed;
    }

    // Update student by ID
    public boolean updateStudent(Student updatedStudent) {
        List<Student> students = getAllStudents();
        boolean updated = false;

        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == updatedStudent.getId()) {
                students.set(i, updatedStudent);
                updated = true;
                break;
            }
        }

        if (updated) {
            writeAllStudents(students);
        }

        return updated;
    }

    // Private helper: format Student to CSV line
    private String formatStudent(Student s) {
        return s.getId() + "," + s.getName() + "," + s.getAddress() + "," + s.getAge();
    }

    // Private helper: parse CSV line to Student
    private Student parseStudent(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length != 4) return null;
        try {
            long id = Long.parseLong(parts[0]);
            String name = parts[1];
            String address = parts[2];
            byte age = Byte.parseByte(parts[3]);

            return new Student(id, name, address, age);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Private helper: write all students to file (overwrite)
    private void writeAllStudents(List<Student> students) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Student student : students) {
                writer.write(formatStudent(student));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Private helper: create directory if not exists
    private void createDirectoryIfNeeded() {
        File dir = new File(DIRECTORY_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

}
