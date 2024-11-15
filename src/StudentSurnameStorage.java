package src;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class StudentSurnameStorage {
    private TreeMap<String, Set<Long>> surnameTreeMap = new TreeMap<>();

    public void studentCreated(long id, String surname) {
        Set<Long> existingIds = surnameTreeMap.getOrDefault(surname, new HashSet<>());
        existingIds.add(id);
        surnameTreeMap.put(surname, existingIds);
    }
    public void studentDeleted(long id, String surname) {
        surnameTreeMap.get(surname).remove(id);
    }
    public void studentUpdated(long id, String oldSurname, String newSurname) {
        studentDeleted(id, oldSurname);
        studentCreated(id, newSurname);
    }

    /**
     * Данный метод возвращает униканый индентификатор студента, чьи фамилия бменьше или равны переданной
     * @return
     */

    public Set<Long> getStudentBySurnameLessOrEqualThan(String surname) {
        Set <Long> res = surnameTreeMap.headMap(surname, true)
                .values()
                .stream()
                .flatMap(longs -> longs.stream())
                .collect(Collectors.toSet());
        return res;
    }

}
