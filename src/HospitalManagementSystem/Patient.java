package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;
    public Patient(Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }

    public void addPatient(){
        System.out.print("Enter Patient name: ");
        String name=scanner.nextLine();
        System.out.print("Enter Patient age: ");
        int age = scanner.nextInt();
        System.out.print("Enter Patient gender: ");
        String gender=scanner.nextLine();
        try {
            String query="insert into patients(name,age,gender) values (?,?,?)";
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            int rowsAffected=preparedStatement.executeUpdate();
            if(rowsAffected>0){
                System.out.println("Patient details added successfully ");
            }else {
                System.out.println("Enter Valid Details ");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
