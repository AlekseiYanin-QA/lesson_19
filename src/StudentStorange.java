package src;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentStorange {
    private Map<Long, Student> studentStorangeMap = new HashMap<>();
    private StudentSurnameStorage studentSurnameStorage = new StudentSurnameStorage();
    private Long carentId = 0L;

    /**
     * Добавление данных о студенте
     * @param student данные о студенте
     * @return сгенерированный индификатор студента
     */
    public Long createStudent(Student student) {
        Long nextId = getNextId();
        studentStorangeMap.put(nextId, student);
        studentSurnameStorage.studentCreated(nextId, student.getSurname());
        return nextId;
    }

    /**
     * Обновление данных о студенте
     * @param id индификатор студента
     * @param student данные о студенте
     * @return true, если данные были обновлены, false если студент не был найден
     */
    public boolean updateStudent(long id, Student student) {
        if (!studentStorangeMap.containsKey(id)) {
            return false;
        } else {
            String newSurame = student.getSurname();
            String oldSurame = studentStorangeMap.get(id).getSurname();
            studentSurnameStorage.studentUpdated(id, oldSurame, newSurame);
            studentStorangeMap.put(id, student);
            return true;
        }


    }

    /**
     * Удаление данных о студенте
     * @param id индификатор студента
     * @return true, если данные были удалены, false если студент не был найден
     */
    public boolean deleteStudent(long id) {
        Student remove = studentStorangeMap.remove(id);
        if (remove != null) {
            String surname = remove.getSurname();
            studentSurnameStorage.studentDeleted(id, surname);
        }
        return remove != null;
    }

    public void search(String surname) {
      Set<Long> students = studentSurnameStorage
              .getStudentBySurnameLessOrEqualThan(surname);
      for (Long studentId : students) {
         Student student = studentStorangeMap.get(studentId);
         System.out.println(student);
      }
    }

    public Long getNextId() {
        carentId = carentId + 1;
        return carentId;
    }

    public void printAll(){
        System.out.println(studentStorangeMap);
    }

    public void printMap(Map<String, Long> data){
        data.entrySet().stream().forEach(e -> {
            System.out.println(e.getKey() + " - " + e.getValue());
        });
    }

    public Map<String, Long> getCountByCourse() {
      Map<String, Long> res = studentStorangeMap.values().stream()
                .collect(Collectors.toMap(
                        student ->  student.getCourse(),
                        student -> 1L,
                        (count1, count2) -> count1 + count2
                ));
      return res;
    }

}

//public Map<String, Long> getCountByCourse() {
//    Map<String, Long> res = new HashMap<>();
//    for (Student student : studentStorangeMap.values()) {
//        String key = student.getCourse();
//        Long count = res.getOrDefault(key, 0L);
//        count++;
//        res.put(key, count);
//    }
//    return res;