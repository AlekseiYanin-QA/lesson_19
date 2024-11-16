package src;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentStorange {
    private final Map<Long, Student> studentStorangeMap = new HashMap<>();
    private final StudentSurnameStorage studentSurnameStorage = new StudentSurnameStorage();
    private Long carentId = 0L;

    /**
     * Добавление данных о студенте
     *
     * @param student данные о студенте
     * @return сгенерированный индификатор студента
     */
    public Long createStudent(Student student) throws InputValidationException {
        if (!isValidStudent(student)) {
            throw new InputValidationException("Ошибка: неверные данные о студенте.");
        }
        Long nextId = getNextId();
        studentStorangeMap.put(nextId, student);
        studentSurnameStorage.studentCreated(nextId, student.getSurname());
        return nextId;
    }

    /**
     * Обновление данных о студенте
     *
     * @param id      индификатор студента
     * @param student данные о студенте
     * @return true, если данные были обновлены, false если студент не был найден
     */
    public boolean updateStudent(long id, Student student) throws InputValidationException {
        if (!studentStorangeMap.containsKey(id)) {
            return false;
        } else {
            if (!isValidStudent(student)) {
                throw new InputValidationException("Ошибка: неверные данные о студенте для обновления.");
            }
            String newSurname = student.getSurname();
            String oldSurname = studentStorangeMap.get(id).getSurname();
            studentSurnameStorage.studentUpdated(id, oldSurname, newSurname);
            studentStorangeMap.put(id, student);
            return true;
        }
    }

    /**
     * Удаление данных о студенте
     *
     * @param id индификатор студента
     * @return true, если данные были удалены, false если студент не был найден
     */
    public boolean deleteStudent(long id) throws InputValidationException {
        Student remove = studentStorangeMap.remove(id);
        if (remove != null) {
            String surname = remove.getSurname();
            studentSurnameStorage.studentDeleted(id, surname);
        }
        return remove != null;
    }

    public void search(String input) {
        String[] parts = input.split(",");

        if (input.trim().isEmpty()) {
            printAll();
        } else if (parts.length == 1) {
            String surname = parts[0].trim();
            Set<Long> students = studentSurnameStorage.getStudentsByExactSurname(surname);
            outputStudents(students);
        } else if (parts.length == 2) {
            String firstSurname = parts[0].trim();
            String secondSurname = parts[1].trim();

            if (firstSurname.compareTo(secondSurname) > 0) {
                String temp = firstSurname;
                firstSurname = secondSurname;
                secondSurname = temp;
            }

            Set<Long> students = studentSurnameStorage.getStudentsBetweenSurnames(firstSurname, secondSurname); // Поиск между двумя фамилиями
            outputStudents(students);
        } else {
            System.out.println("Неверный формат ввода. Пожалуйста, введите одну или две фамилии, разделённые запятой.");
        }
    }

    private void outputStudents(Set<Long> students) {
        if (students.isEmpty()) {
            System.out.println("Студенты не найдены.");
        } else {
            for (Long studentId : students) {
                Student student = studentStorangeMap.get(studentId);
                System.out.println(student);
            }
        }
    }

    public Long getNextId() {
        carentId = carentId + 1;
        return carentId;
    }

    public void printAll() {
        System.out.println(studentStorangeMap);
    }

    public void printMap(Map<String, Long> data) {
        data.entrySet().forEach(e -> {
            System.out.println(e.getKey() + " - " + e.getValue());
        });
    }

    public Map<String, Long> getCountByCourse() {
        Map<String, Long> res = studentStorangeMap.values().stream()
                .collect(Collectors.toMap(
                        student -> student.getCourse(),
                        student -> 1L,
                        (count1, count2) -> count1 + count2
                ));
        return res;
    }

    public Map<String, Long> getCountByCity() {
        Map<String, Long> res = studentStorangeMap.values().stream()
                .collect(Collectors.toMap(
                        student -> student.getCity(),
                        student -> 1L,
                        (count1, count2) -> count1 + count2
                ));
        return res;
    }

    private boolean isValidStudent(Student student) {
        return student != null &&
                student.getSurname() != null && !student.getSurname().trim().isEmpty() &&
                student.getName() != null && !student.getName().trim().isEmpty() &&
                student.getCourse() != null && !student.getCourse().trim().isEmpty() &&
                student.getCity() != null && !student.getCity().trim().isEmpty() &&
                student.getAge() > 0;
    }
}

