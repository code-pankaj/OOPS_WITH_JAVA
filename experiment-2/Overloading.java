// UID - 24BCS12303

public class Overloading {
    public static int area(int length, int breadth){
        return length*breadth;
    }
    public static int area(int side){
        return side*side;
    }
    public static void main(String[] args){
        int rect = area(5,3);
        int square = area(10);
        System.out.println("Area of rectangle : "+ rect);
        System.out.println("Area of Square : "+ square);
    }
}
