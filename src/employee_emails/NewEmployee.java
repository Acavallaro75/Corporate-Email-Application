package employee_emails;

import java.util.Scanner;

public class NewEmployee {

  private String firstName;
  private String lastName;

  protected void collectData() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter employee's first name: ");
    this.firstName = scanner.next();

    System.out.print("Enter employee's last name: ");
    this.lastName = scanner.next();

    sendData();
  }

  protected void sendData() {
    Employee employee = new Employee(this.firstName, this.lastName);
  }
}
