package student;
import java.sql.*;
import java.util.Scanner;

public class StudentManagement {
    static final String URL = "jdbc:mysql://localhost:3306/student_management";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "14367898Lm&";

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
    }

    static void addStudent(Scanner sc) {
        String sql = "INSERT INTO students(name,email,phone) VALUES(?,?,?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            System.out.print("Name: "); ps.setString(1, sc.nextLine());
            System.out.print("Email: "); ps.setString(2, sc.nextLine());
            System.out.print("Phone: "); ps.setString(3, sc.nextLine());
            ps.executeUpdate();
            System.out.println("Student registered.");
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    static void addCourse(Scanner sc) {
        String sql = "INSERT INTO courses(course_name,duration_months) VALUES(?,?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            System.out.print("Course name: "); ps.setString(1, sc.nextLine());
            System.out.print("Duration in months: "); ps.setInt(2, Integer.parseInt(sc.nextLine()));
            ps.executeUpdate();
            System.out.println("Course added.");
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    static void enrollStudent(Scanner sc) {
        String sql = "INSERT INTO enrollments(student_id,course_id,enrollment_date) VALUES(?,?,CURDATE())";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            System.out.print("Student ID: "); ps.setInt(1, Integer.parseInt(sc.nextLine()));
            System.out.print("Course ID: "); ps.setInt(2, Integer.parseInt(sc.nextLine()));
            ps.executeUpdate();
            System.out.println("Enrollment completed.");
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    static void addAttendance(Scanner sc) {
        String sql = "INSERT INTO attendance(student_id,course_id,total_classes,attended_classes) VALUES(?,?,?,?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            System.out.print("Student ID: "); ps.setInt(1, Integer.parseInt(sc.nextLine()));
            System.out.print("Course ID: "); ps.setInt(2, Integer.parseInt(sc.nextLine()));
            System.out.print("Total classes: "); ps.setInt(3, Integer.parseInt(sc.nextLine()));
            System.out.print("Attended classes: "); ps.setInt(4, Integer.parseInt(sc.nextLine()));
            ps.executeUpdate();
            System.out.println("Attendance saved.");
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    static void addMarks(Scanner sc) {
        String sql = "INSERT INTO marks(student_id,course_id,marks) VALUES(?,?,?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            System.out.print("Student ID: "); ps.setInt(1, Integer.parseInt(sc.nextLine()));
            System.out.print("Course ID: "); ps.setInt(2, Integer.parseInt(sc.nextLine()));
            System.out.print("Marks: "); ps.setDouble(3, Double.parseDouble(sc.nextLine()));
            ps.executeUpdate();
            System.out.println("Marks saved.");
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    static void showResults() {
        String sql = """
            SELECT s.student_id, s.name, c.course_name, m.marks,
                   a.total_classes, a.attended_classes,
                   ROUND(a.attended_classes * 100.0 / NULLIF(a.total_classes,0),2) AS attendance_percent
            FROM students s
            JOIN marks m ON s.student_id=m.student_id
            JOIN courses c ON c.course_id=m.course_id
            LEFT JOIN attendance a ON a.student_id=s.student_id AND a.course_id=c.course_id
            ORDER BY s.student_id
            """;
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("\nID | Name | Course | Marks | Attendance");
            while (rs.next()) {
                System.out.printf("%d | %s | %s | %.2f | %s%%%n",
                    rs.getInt("student_id"), rs.getString("name"),
                    rs.getString("course_name"), rs.getDouble("marks"),
                    rs.getString("attendance_percent"));
            }
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- STUDENT MANAGEMENT ---");
            System.out.println("1. Register Student");
            System.out.println("2. Add Course");
            System.out.println("3. Enroll Student");
            System.out.println("4. Enter Attendance");
            System.out.println("5. Enter Marks");
            System.out.println("6. Generate Result");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> addStudent(sc);
                case "2" -> addCourse(sc);
                case "3" -> enrollStudent(sc);
                case "4" -> addAttendance(sc);
                case "5" -> addMarks(sc);
                case "6" -> showResults();
                case "0" -> { sc.close(); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}

		// TODO Auto-generated method stub
