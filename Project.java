import java.sql.*;
import java.util.Scanner;

public class project1 {


    /*SQL statements based off of https://www.tutorialspoint.com/sqlite/sqlite_java.htm
     * and https://www.sqlitetutorial.net/sqlite-java/*/
    public project1() {
        connect();
    }
    private Connection connect() {
        Connection c = null;
        String url = "jdbc:sqlite:Project_1.db";
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        // System.out.println("Opened database successfully");

        // Create a new table
        String sql1 = "CREATE TABLE IF NOT EXISTS Members (\n"
                + "	MemberId TEXT, \n"
                + " MemberPassword TEXT, \n"
                + "	name TEXT\n"
                + ");";
        String sql2 = "CREATE TABLE IF NOT EXISTS Tasks (\n"
                + "TaskId TEXT, \n Description TEXT, \n DueDate TEXT, \n "
                + "AssignedTo TEXT, \n CreatedOn TEXT, \n CreatedBy TEXT, \n Status TEXT, \n Color TEXT\n"
                + ");";
        String sql3 = "CREATE TABLE IF NOT EXISTS Teams (\n"
                + "	TeamId TEXT, \n"
                + " MemberId TEXT \n"
                + ");";
        String sql4 = "CREATE TABLE IF NOT EXISTS Categories (\n" +
                "    Name TEXT, \n" +
                "    Description TEXT, \n" +
                "    CreatedBy TEXT, \n" +
                "    CreatedOn DATE \n" +
                ");";
        String sql5 = "CREATE TABLE IF NOT EXISTS Subtasks (\n" +
                "    TaskId TEXT, \n" +
                "    SubtaskId TEXT \n" +
                ");";
        String sql6 = "CREATE TABLE IF NOT EXISTS CategoryTaskMap (\n" +
                "    CategoryName TEXT, \n" +
                "    TaskId TEXT\n" +
                ");";
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            stmt.execute(sql1);
            stmt.execute(sql2);
            stmt.execute(sql3);
            stmt.execute(sql4);
            stmt.execute(sql5);
            stmt.execute(sql6);
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        //Above code should create the MemberId table if not exists aka no Project_1.db in folder prior to running program

        return c;
    }

    public void insertMembers(String MemberId, String MemberPassword) {
        String sql = "INSERT INTO Members(MemberId,MemberPassword) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, MemberId);
            pstmt.setString(2, MemberPassword);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertTasks(String TaskId, String Description, String DueDate, String AssignedTo, String CreatedOn, String CreatedBy, String Status, String Color) {
        String sql = "INSERT INTO Tasks(TaskId,Description,DueDate,AssignedTo,CreatedOn,CreatedBy,Status,Color) VALUES(?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, TaskId);
            pstmt.setString(2, Description);
            pstmt.setString(3, DueDate);
            pstmt.setString(4, AssignedTo);
            pstmt.setString(5, CreatedOn);
            pstmt.setString(6, CreatedBy);
            pstmt.setString(7, Status);
            pstmt.setString(8, Color);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertTeams(String TeamId, String MemberId) {
        String sql = "INSERT INTO Teams(TeamId,MemberId) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, TeamId);
            pstmt.setString(2, MemberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void insertCategories(String Name, String Description, String CreatedBy, String CreatedOn) {
        String sql = "INSERT INTO Categories(Name, Description, CreatedBy, CreatedOn) VALUES(?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, Name);
            pstmt.setString(2, Description);
            pstmt.setString(3, CreatedBy);
            pstmt.setString(4, CreatedOn);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertSubtasks(String TaskId, String SubtaskId) {
        // Should only insert if TaskId currently exists.
        String sql = "INSERT INTO Subtasks(TaskId,SubtaskId) SELECT TaskId, ? FROM Tasks WHERE TaskId=?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, SubtaskId);
            pstmt.setString(2, TaskId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertCategoryTaskMap(String CategoryName, String TaskId) {
        String sql = "INSERT INTO CategoryTaskMap(CategoryName,TaskId) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, CategoryName);
            pstmt.setString(2, TaskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteMembers(String MemberId, String MemberPassword) {
        String sql = "DELETE FROM Teams WHERE MemberId=? AND EXISTS(SELECT 1 FROM Members WHERE MemberId=? AND MemberPassword=?)";
        try (Connection conn = this.connect();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, MemberId);
            pstmt.setString(2, MemberId);
            pstmt.setString(3, MemberPassword);
            pstmt.executeUpdate();
            System.out.println("Successfully deleted the following member from Teams: " + MemberId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String sql2 = "DELETE FROM Members WHERE MemberId=? AND MemberPassword=?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql2)) {
            pstmt.setString(1, MemberId);
            pstmt.setString(2, MemberPassword);
            pstmt.executeUpdate();
            System.out.println("Successfully deleted the following member from Members: " + MemberId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteTasks(String TaskId) {
        String sql = "DELETE FROM Tasks WHERE TaskId=?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, TaskId);
            pstmt.executeUpdate();
            System.out.println("Successfully deleted the following task: " + TaskId);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String sql2 = "DELETE FROM Subtasks WHERE TaskId=?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql2)) {
            pstmt.setString(1, TaskId);
            pstmt.executeUpdate();
            System.out.println("Successfully deleted the subtasks from the following task: " + TaskId);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String sql3 = "DELETE FROM CategoryTaskMap WHERE TaskId=?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql3)) {
            pstmt.setString(1, TaskId);
            pstmt.executeUpdate();
            System.out.println("Successfully deleted the following task from its category: " + TaskId);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteTeams(String TeamId) {
        String sql = "DELETE FROM Teams WHERE TeamId=?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, TeamId);
            pstmt.executeUpdate();
            System.out.println("Successfully deleted the following team: " + TeamId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void deleteCategories(String Name) {
        String sql = "DELETE FROM Categories WHERE Name=?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, Name);
            pstmt.executeUpdate();
            System.out.println("Successfully deleted the following category: " + Name);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = "DELETE FROM CategoryTaskMap WHERE CategoryName=?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, Name);
            pstmt.executeUpdate();
            System.out.println("Successfully deleted the removed tasks from the following category: " + Name);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteSubtasks(String TaskId, String SubtaskId) {
        String sql = "DELETE FROM Subtasks WHERE TaskId=? AND SubtaskId=?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, TaskId);
            pstmt.setString(2, SubtaskId);
            pstmt.executeUpdate();
            System.out.println("Successfully deleted the following subtask: " + SubtaskId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void printTable() {
        Connection c = null;
        String url = "jdbc:sqlite:Project_1.db";
        Statement s = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            s = c.createStatement();
            int numOfTables = 5;
            String tableNames[] = new String[]{"Categories", "CategoryTaskMap", "Tasks", "Teams", "Subtasks"};
            String sqlArr[] = new String[numOfTables];
            for (int i = 0; i < numOfTables; i++) {
                sqlArr[i] = "SELECT * FROM " + tableNames[i];
            }
            for (int i = 0; i < numOfTables; i++) { // Go through each table
                ResultSet rs = s.executeQuery(sqlArr[i]);
                ResultSetMetaData rsmd = rs.getMetaData();
                System.out.println("===" + tableNames[i] + " Table===");
                int count = 1;
                while (rs.next()) { // Go through each row of the current table
                    System.out.println(tableNames[i] + " #" + count);
                    for (int j = 1; j <= rsmd.getColumnCount(); j++) {
                        String colName = rsmd.getColumnName(j);
                        System.out.println("\t" + colName + ": " +rs.getString(colName));
                    }
                    count++;
                }
                rs.close();
            }
            c.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void main(String args[]) {

        project1 app = new project1();
        Scanner input = new Scanner(System.in);

        System.out.println("=== TASK MANAGER ==="); // PLACEHOLDER for title

        int Choice1;
        do {
            System.out.println("Current Tasks:\n--"); // PLACEHOLDER
            app.printTable();

            System.out.println("Insert number of what you wish to do: \n[1] Create \n[2] Edit \n[3] Delete \n[4] Quit");
            Choice1 = input.nextInt();
            System.out.println(); // skip line after user entry (for readability)
            switch (Choice1) {
                // Create Entity
                case 1: {

                    int Choice2;
                    do {
                        System.out.println("Insert number of what you wish to create: \n[1] Member \n[2] Task \n[3] Team \n[4] Task Category \n[5] Back");
                        Choice2 = input.nextInt();
                        input.nextLine(); // to read the return key
                        System.out.println(); // skip line after user entry (for readability)
                        switch (Choice2) {
                            // Create Member
                            case 1: {
                                System.out.println("Enter Member ID");
                                //For some reason the first nextLine input gets automatically a NULL look into later
                                //String why = input.nextLine();
                                String a = input.nextLine();
                                System.out.println("Enter Member Password");
                                String b = input.nextLine();
                                app.insertMembers(a, b);
                                break;
                            }
                            // Create Task
                            case 2: {
                                System.out.println("Enter Task ID");
                                //For some reason the first nextLine input gets automatically a NULL look into later
                                //String why = input.nextLine();
                                String a = input.nextLine();
                                System.out.println("Enter Description");
                                String b = input.nextLine();
                                System.out.println("Enter Due Date");
                                String c = input.nextLine();
                                System.out.println("Enter Assigned To");
                                String d = input.nextLine();
                                //Make automatic once users are implemented
                                System.out.println("Enter Created On");
                                String e = input.nextLine();
                                //Make automatic at some point
                                System.out.println("Enter Created By");
                                String f = input.nextLine();
                                //Preset status options maybe?
                                System.out.println("Enter Status");
                                String g = input.nextLine();
                                //Preset colors?
                                System.out.println("Enter Color");
                                String h = input.nextLine();
                                app.insertTasks(a, b, c, d, e, f, g, h);

                                // Preset subtasks
                                String answer;
                                do {
                                    System.out.println("Do you want to add any subtasks? (y/n)");
                                    answer = input.nextLine();
                                }
                                while (!(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n")));
                                if (answer.equalsIgnoreCase("y")) { // add subtasks until user says to not continue
                                    do {
                                        System.out.println("Enter Task");
                                        String taskId = input.nextLine();
                                        System.out.println("Enter SubTask");
                                        String subtaskId = input.nextLine();
                                        app.insertSubtasks(taskId, subtaskId);
                                        System.out.println("Continue adding subtasks? (y/n)");
                                        answer = input.nextLine();
                                    }
                                    while (answer.equalsIgnoreCase("y"));
                                }

                                break;
                            }
                            // Create Team
                            case 3: {
                                System.out.println("Enter Team ID");
                                //For some reason the first nextLine input gets automatically a NULL look into later
                                //String why = input.nextLine();
                                String a = input.nextLine();
                                System.out.println(a);
                                System.out.println("Enter Member ID");
                                String b = input.nextLine();
                                System.out.println(b);
                                app.insertTeams(a, b);
                                break;
                            }
                            // Create Task Category
                            case 4: {
                                System.out.println("Enter Category Name");
                                //For some reason the first nextLine input gets automatically a NULL look into later
                                //String why = input.nextLine();
                                String a = input.nextLine();
                                System.out.println(a);
                                System.out.println("Enter Description");
                                String b = input.nextLine();
                                System.out.println(b);
                                System.out.println("Enter Created By");
                                String c = input.nextLine();
                                System.out.println(c);
                                System.out.println("Enter Created On");
                                String d = input.nextLine();
                                System.out.println(d);
                                app.insertCategories(a, b, c, d);
                                break;
                            }
                            case 5: {
                                break; // exit loop, return to main menu without futher edits
                            }
                            default: {
                                System.out.println("Invalid Entry");
                            }
                        }
                    } while (Choice2 != 5);
                    break;

                }
                // Edit(manipulate) Entity
                case 2: {
                    // After entering 5 in case 1
                    System.out.println("Edit (WIP) --> Main Menu"); //PLACEHOLDER for edit menu
                    break;
                }
                // Delete Entity
                case 3: {
                    int Choice2;
                    do {
                        System.out.println("Insert number of what you wish to delete: \n[1] Member \n[2] Task \n[3] Team \n[4] Task Category \n[5] Back");
                        Choice2 = input.nextInt();
                        input.nextLine(); // to read the return key
                        System.out.println(); // skip line after user entry (for readability)
                        switch (Choice2) {
                            // Delete Member
                            case 1: {
                                System.out.println("Enter Member ID");
                                String a = input.nextLine();
                                System.out.println("Enter Member Password");
                                String b = input.nextLine();
                                app.deleteMembers(a, b);
                                break;
                            }
                            // Delete Task
                            case 2: {
                                System.out.println("Enter Task ID");
                                //For some reason the first nextLine input gets automatically a NULL look into later
                                //String why = input.nextLine();
                                String a = input.nextLine();
                                app.deleteTasks(a);
                                break;
                            }
                            // Create Team
                            case 3: {
                                System.out.println("Enter Team ID");
                                //For some reason the first nextLine input gets automatically a NULL look into later
                                //String why = input.nextLine();
                                String a = input.nextLine();
                                app.deleteTeams(a);
                                break;
                            }
                            // Create Task Category
                            case 4: {
                                System.out.println("Enter Category Name");
                                //For some reason the first nextLine input gets automatically a NULL look into later
                                //String why = input.nextLine();
                                String a = input.nextLine();
                                app.deleteCategories(a);
                                break;
                            }
                            case 5: {
                                break; // exit loop, return to main menu without futher edits
                            }
                            default: {
                                System.out.println("Invalid Entry");
                            }
                        }
                    } while (Choice2 != 5);
                    break;
                }
                case 4: {
                    break; // exit loop & program
                }
                default: {
                    System.out.println("Invalid Entry");
                }
            }
        } while (Choice1 != 4);
    }
}

