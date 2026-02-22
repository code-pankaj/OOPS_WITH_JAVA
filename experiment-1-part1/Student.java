// UID - 24BCS12303

public class Student {
    public String name;
    public String uid;
    public String section;

    Student(String name, String uid, String section){
        this.name = name;
        this.uid = uid;
        this.section = section;
    }

    public static void main(String[] args){
        Student s1 = new Student("Pankaj", "24BCS123O3", "711-A");
        System.out.println("Name of the student 1 : "+ s1.name);
        System.out.println("UID of the student 1 : "+ s1.uid);
        System.out.println("Section of the student 1 : "+ s1.section);
    }
}
