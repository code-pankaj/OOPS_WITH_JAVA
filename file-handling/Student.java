import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;


public class Student {
    public static void main(String[] args) {   
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your name : ");
        String name = sc.nextLine();
        System.out.print("Enter your roll number : ");
        int rollNo = sc.nextInt();
        sc.nextLine(); 
        System.out.print("Enter your Course Name : ");
        String course = sc.nextLine();
        sc.close();
        try {
            File file = new File("student.txt");
            FileWriter writer = new FileWriter(file);
            writer.append("Name : " + name + "\n");
            writer.append("RollNo. : " + rollNo + "\n");
            writer.append("Course : " + course + "\n");
            writer.close();
            System.out.println("\n");
            System.out.println("File written successfully!");
            System.out.println("\n");
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()){
                String curr = reader.nextLine();
                System.out.println(curr);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}