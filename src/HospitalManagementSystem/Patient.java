package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        String name=scanner.next();
        System.out.print("Enter Patient age: ");
        int age = scanner.nextInt();
        System.out.print("Enter Patient gender: ");
        String gender=scanner.next();
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
    public void viewPatient(){
        try {
            String query="select * from patients";
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            ResultSet resultSet= preparedStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+--------------+---------------------------+---------+----------------+");
            System.out.println("| Patient Id   | Patient Name              | Age     | Gender         |");
            System.out.println("+--------------+---------------------------+---------+----------------+");
            while (resultSet.next()){
                int id=resultSet.getInt("id");
                String name=resultSet.getString("name");
                int age=resultSet.getInt("age");
                String  gender=resultSet.getString("gender");
                System.out.printf("| %-13s| %-26s| %-8s| %-13s\n",id,name,age,gender);
                System.out.println("+--------------+---------------------------+---------+----------------+");

            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id){
        String query="select * from patients where id=?";
        try {
//            System.out.println("Enter Patient Id: ");
//            int id=scanner.nextInt();
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }else {
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}











