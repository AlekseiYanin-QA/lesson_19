package src;

import java.util.*;
import java.util.stream.Collectors;

public class StudentSurnameStorage {
    private final TreeMap<String, Set<Long>> surnameTreeMap = new TreeMap<>();

    public void studentCreated(long id, String surname) {
        Set<Long> existingIds = surnameTreeMap.getOrDefault(surname, new HashSet<>());
        existingIds.add(id);
        surnameTreeMap.put(surname, existingIds);
    }
    public void studentDeleted(long id, String surname) throws InputValidationException {
        Set<Long> studentIds = surnameTreeMap.get(surname);
        if (studentIds == null || !studentIds.contains(id)) {
            throw new InputValidationException("Ошибка: студент с ID " + id + " и фамилией " + surname + " не найден.");
        }
        studentIds.remove(id);
        if (studentIds.isEmpty()) {
            surnameTreeMap.remove(surname);
        }
    }
    public void studentUpdated(long id, String oldSurname, String newSurname) throws InputValidationException {
        studentDeleted(id, oldSurname);
        studentCreated(id, newSurname);
    }


    /**
     * Возвращает уникальных студентов, чьи фамилии точно совпадают с переданным параметром
     */
    public Set<Long> getStudentsByExactSurname(String surname) {
        return Optional.ofNullable(surnameTreeMap.get(surname)).orElse(Collections.emptySet());
    }

    /**
     * Возвращает студентов, чьи фамилии находятся в диапазоне от первой до второй включительно
     */
    public Set<Long> getStudentsBetweenSurnames(String firstSurname, String secondSurname) {
        SortedMap<String, Set<Long>> subMap = surnameTreeMap
                .subMap(firstSurname, true, secondSurname, true);
        return subMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}


/**
 * Данный метод возвращает униканый индентификатор студента, чьи фамилия бменьше или равны переданной

Метод переписан в отдельные методы

 public Set<Long> getStudentBySurnameLessOrEqualThan(String surname) {
 Set <Long> res = surnameTreeMap.headMap(surname, true)
 .values()
 .stream()
 .flatMap(longs -> longs.stream())
 .collect(Collectors.toSet());
 return res;
 }
 */
