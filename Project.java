import java.sql.*;
import java.util.Scanner;

public class project1 {

    /*SQL statements based off of https://www.tutorialspoint.com/sqlite/sqlite_java.htm
     * and https://www.sqlitetutorial.net/sqlite-java/*/

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
                + "TaskId TEXT, \n Description TEXT, \n Subtasks TEXT, \n DueDate TEXT, \n "
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
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            stmt.execute(sql1);
            stmt.execute(sql2);
            stmt.execute(sql3);
            stmt.execute(sql4);
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

    public void insertTasks(String TaskId, String Description, String Subtasks, String DueDate, String AssignedTo, String CreatedOn, String CreatedBy, String Status, String Color) {
        String sql = "INSERT INTO Tasks(TaskId,Description,Subtasks,DueDate,AssignedTo,CreatedOn,CreatedBy,Status,Color) VALUES(?,?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, TaskId);
            pstmt.setString(2, Description);
            pstmt.setString(3, Subtasks);
            pstmt.setString(4, DueDate);
            pstmt.setString(5, AssignedTo);
            pstmt.setString(6, CreatedOn);
            pstmt.setString(7, CreatedBy);
            pstmt.setString(8, Status);
            pstmt.setString(9, Color);
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
    }


    public static void main(String args[]) {

        project1 app = new project1();
        Scanner input = new Scanner(System.in);

        System.out.println("=== TASK MANAGER ==="); // PLACEHOLDER for title

        int Choice1;
        do {
            System.out.println("Current Tasks:\n--"); // PLACEHOLDER
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
                                System.out.println("Enter Subtasks");
                                String c = input.nextLine();
                                System.out.println("Enter Due Date");
                                String d = input.nextLine();
                                System.out.println("Enter Assigned To");
                                String e = input.nextLine();
                                //Make automatic once users are implemented
                                System.out.println("Enter Created On");
                                String f = input.nextLine();
                                //Make automatic at some point
                                System.out.println("Enter Created By");
                                String g = input.nextLine();
                                //Preset status options maybe?
                                System.out.println("Enter Status");
                                String h = input.nextLine();
                                //Preset colors?
                                System.out.println("Enter Color");
                                String i = input.nextLine();
                                app.insertTasks(a, b, c, d, e, f, g, h, i);
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
