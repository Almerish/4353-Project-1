import java.util.Date;
import java.util.List;

public class Project {
    // Change these variable initializations to work with DBMS or text files?
    // Idk if we should use lists, so switch these to arrays maybe?
    List<User> members;
    List<Team> teams;
    List<Task> tasks;
    List<TaskCategory> taskCategories;

    // F1
    void createMember(User member) { members.add(member); }
    void createTeam(Team team) { teams.add(team); }
    void createTask(Task task) { tasks.add(task); }
    void createTaskCategory(TaskCategory tc) { taskCategories.add(tc); }
}

class User {
    String name;
}

class Team {
    User[] team;
    void addUser() {} // F1
}

class Task {
    String name;
    String description;
    Task[] subtasks;
    Date due_date;
    User assigned_to;
    Date created_on;
    Date created_by;
    String status;
    Color color; // F5
    void setColor(Color c) {color = c;}

}

class TaskCategory {
    Task[] tasks;
    String name;
    String description;
    Date created_by;
    Date created_on;
    void addTask() {} // F1
}

// F5
enum Color {
    RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE, PINK, BROWN, BLACK, WHITE;
}