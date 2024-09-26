package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url="jdbc:mysql://localhost:3306/hospital";
    private static final String username="root";
    private static final String password="tushar123456";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner=new Scanner(System.in);
        try {

            Connection connection= DriverManager.getConnection(url,username,password);
            Patient patient=new Patient(connection,scanner);
            Doctor doctor=new Doctor(connection);

            while (true){
                System.out.println("Hospital Management System ");
                System.out.println("1. Add patient ");
                System.out.println("2. View patient ");
                System.out.println("3. View Doctors ");
                System.out.println("4. Book Appointment ");
                System.out.println("5. View Appointments ");
                System.out.println("6. Exit ");
                System.out.print("Enter Choice: ");
                int choice = scanner.nextInt();
                switch (choice){
                    case 1:
                        patient.addPatient();
                        break;
                    case 2:
                        patient.viewPatient();
                        break;
                    case 3:
                        doctor.viewDoctor();
                        break;
                    case 4:
                        bookAppointment(patient,doctor,connection,scanner);
                    case 5:
                        viewAppointments(connection,scanner,patient);
                        break;
                    case 6:
                        return;
                    default:
                        System.out.println("Enter Valid choice ");
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public  static void bookAppointment(Patient patient, Doctor doctor, Connection connection,Scanner scanner){
        System.out.print("Enter Patient Id: ");
        int patientId=scanner.nextInt();
        System.out.print("Enter Doctor Id: ");
        int doctorId= scanner.nextInt();
        System.out.print("Enter appointment Date(YYYY-MM-DD): ");
        String appointmentDate=scanner.next();
        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if (checkDoctorAvailibility(doctorId,appointmentDate,connection)){
                String appointmentQuery="insert into appointments(patient_id,doctor_id,appointment_date) values(?,?,?)";
                try{
                    PreparedStatement preparedStatement= connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,patientId);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3,appointmentDate);
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows>0){
                        System.out.println("Appointment Booked");
                    }else {
                        System.out.println("Failed to book Appointment ");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else {
                System.out.println("Doctor is not available on this date ");
            }
        }else {
            System.out.println("Doctor or Patient Does not exist ");
        }
    }

    public static boolean checkDoctorAvailibility(int doctorId,String appointmentDate,Connection connection){
        String query="select count(*) from appointments where doctor_id=? and appointment_date=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()){
                int count = resultSet.getInt(1);
                if (count==0){
                    return true;
                }else {
                    return false;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static void viewAppointments(Connection connection,Scanner scanner,Patient patient){
        System.out.print("Enter Your Id: ");
        int id= scanner.nextInt();
        String query="select * from appointments where patient_id=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            System.out.println("Appointments ");
            System.out.println("+------------+-----------+------------------+");
            System.out.println("| Your Id    | Doctor Id | Appointment Date |");
            System.out.println("+------------+-----------+------------------+");
            while (resultSet.next()){
                int patient_id=resultSet.getInt("patient_id");
                int doctor_id=resultSet.getInt("doctor_id");
                String appointment_date=resultSet.getString("appointment_date");
                System.out.printf("| %-12s| %-10s| %-17s|\n",patient_id,doctor_id,appointment_date);
                System.out.println("+------------+-----------+------------------+");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
