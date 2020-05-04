package employee_emails;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class Employee {

  private String firstName;
  private String lastName;
  private String department;
  private String password;
  private String email;
  public String alternateEmail;
  private int mailboxCapacity;
  private Connection connection;

  public Employee() {}

  // Constructor to receive the employee's first name and last name //
  public Employee(String firstName, String lastName) {
    int passwordLength = 8;
    this.firstName = firstName;
    this.lastName = lastName;

    this.department = setDepartment();
    this.password = setPassword(passwordLength);

    switch (this.department) {
      case "sales":
        setMailboxCapacity(1000);
        break;
      case "dev":
        setMailboxCapacity(250);
        break;
      case "acct":
        setMailboxCapacity(500);
        break;
      default:
        setMailboxCapacity(100);
        break;
    }

    setEmail();

    initializeDB();

    System.out.println(this.alternateEmail);
    addEmployee();
  }

  // Initialize the database //
  protected void initializeDB() {
    try {
      Properties properties = new Properties();
      properties.load(new FileInputStream("resources/properties"));
      final String password = properties.getProperty("PASSWORD");
      final String driver = "org.h2.Driver";
      final String url = "jdbc:h2:./resources/employees";
      final String user = "";
      Class.forName(driver);
      connection = DriverManager.getConnection(url, user, password);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Sets the employee's email //
  public void setEmail() {
    String companyName = "fgcu.edu";

    this.email =
        this.firstName.toLowerCase()
            + "."
            + this.lastName.toLowerCase()
            + "@"
            + department
            + "."
            + companyName;
  }

  // Gets the employee's email //
  public String getEmail() {
    return this.email;
  }

  // Sets the employee's department  //
  private String setDepartment() {
    Scanner scanner = new Scanner(System.in);

    System.out.print(
        "DEPARTMENT CODES:\n1 for Sales\n2 for Development\n3 for Accounting\n0 for No Department\n"
            + "Enter Department Code: ");

    int departmentChoice = 0;

    if (scanner.hasNextInt()) {
      departmentChoice = scanner.nextInt();
    } else {
      System.out.println("That is not a valid option\n");
      setDepartment();
    }

    switch (departmentChoice) {
      case 1:
        return "sales";
      case 2:
        return "dev";
      case 3:
        return "acct";
      default:
        return "";
    }
  }

  // Gets the employee's department //
  public String getDepartment() {
    return this.department;
  }

  // Sets a random password for the employee //
  private String setPassword(int length) {
    String passwordSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%";
    char[] password = new char[length];

    for (int i = 0; i < password.length; i++) {
      int randomNumber = (int) (Math.random() * passwordSet.length());
      password[i] = passwordSet.charAt(randomNumber);
    }

    return new String(password);
  }

  // Gets the employee's password //
  private String getPassword() {
    return this.password;
  }

  // Sets the employee's alternate email //
  public void setAlternateEmail(String alternateEmail) {
    this.alternateEmail = alternateEmail;
  }

  // Gets the employee's alternate email //
  public String getAlternateEmail() {
    return this.alternateEmail;
  }

  public void setMailboxCapacity(int mailboxCapacity) {
    this.mailboxCapacity = mailboxCapacity;
  }

  // Gets the employee's mailbox capacity //
  public int getMailboxCapacity() {
    return this.mailboxCapacity;
  }

  // Gets the employee's first name //
  public String getFirstName() {
    return this.firstName;
  }

  // Gets the employee's last name //
  public String getLastName() {
    return this.lastName;
  }

  // Adds an employee and all of their information into the database //
  public void addEmployee() {
    try {
      String sql =
          "INSERT INTO EMPLOYEES (FIRST, LAST, DEPT, EMAIL, PASSWORD, ALT_EMAIL, MB_CAP) VALUES (?, ?, ?, ?, ?, ?, ?)";
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, getFirstName());
      preparedStatement.setString(2, getLastName());
      preparedStatement.setString(3, getDepartment());
      preparedStatement.setString(4, getEmail());
      preparedStatement.setString(5, getPassword());
      preparedStatement.setString(6, getAlternateEmail());
      preparedStatement.setInt(7, getMailboxCapacity());
      preparedStatement.executeUpdate();
      preparedStatement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
