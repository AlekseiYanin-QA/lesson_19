package src;

import java.util.Map;
import java.util.Set;

public class StudentCommandHandler {

    private final StudentStorange studentStorange = new StudentStorange();

    public void processCommand(Command command) {
        Action action = command.getAction();
        switch (action) {
            case CREATE -> processCreateCommand(command);
            case UPDATE -> processUpdateCommand(command);
            case DELETE -> processDeleteCommand(command);
            case STATS_BY_COURSE -> processStatsByCourseCommand(command);
            case STATS_BY_CITY -> processStatsByCityCommand(command);
            case SEARCH -> processSearchCommand(command);
            default -> System.out.println("Действие " + action + " не поддерживается");
        }
        System.out.println("Обработка команды. Действие:  "
                + command.getAction().name()
                + ", данные: " + command.getData());
    }

    private void processSearchCommand(Command command) {
        String surname = command.getData();
        studentStorange.search(surname);
    }

    public void processStatsByCourseCommand(Command command) {
        Map<String, Long> data = studentStorange.getCountByCourse();
        studentStorange.printMap(data);
    }

    private void processCreateCommand(Command command) {
        String data = command.getData();
        String[] dataArray = data.split(",");

        if (dataArray.length != 5) {
            System.out.println("Ошибка: ожидается 5 данных для создания студента.");
            return;
        }

        try {
            Student student = new Student();
            student.setSurname(dataArray[0]);
            student.setName(dataArray[1]);
            student.setCourse(dataArray[2]);
            student.setCity(dataArray[3]);
            student.setAge(Integer.valueOf(dataArray[4]));

            studentStorange.createStudent(student);
            studentStorange.printAll();
        } catch (NumberFormatException e) {
            System.out.println("Ошибка ввода: возраст должен быть числом.");
        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public void processUpdateCommand(Command command) {
        String data = command.getData();
        String[] dataArray = data.split(",");

        if (dataArray.length != 5) {
            System.out.println("Ошибка: ожидается 5 данных для обновления студента.");
            return;
        }

        try {
            Long id = Long.valueOf(dataArray[0]);
            Student student = new Student();

            student.setSurname(dataArray[1]);
            student.setName(dataArray[2]);
            student.setCourse(dataArray[3]);
            student.setCity(dataArray[4]);
            student.setAge(Integer.valueOf(dataArray[5]));

            studentStorange.updateStudent(id, student);
            studentStorange.printAll();
        } catch (NumberFormatException e) {
            System.out.println("Ошибка ввода: возраст должен быть числом.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ошибка: недостаточно данных для обновления студента.");
        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public void processDeleteCommand(Command command) {
        String data = command.getData();
        try {
            Long id = Long.valueOf(data);

            if (!studentStorange.deleteStudent(id)) {
                System.out.println("Ошибка: студент с ID " + id + " не найден.");
            } else {
                studentStorange.printAll();
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом.");
        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public void processStatsByCityCommand(Command command) {
        Map<String, Long> data = studentStorange.getCountByCity();
        printStatistics(data);
    }

    private void printStatistics(Map<String, Long> data) {
        data.forEach((city, count) ->
                System.out.println(city + " - " + count)
        );
    }
}
