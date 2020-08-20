public class Person {
    public static int personCounter;

    public String helloWorld() {
        return "hello world";
    }

    public String hello(String name) {
        return "hello " + name;
    }

    public Person() {
        personCounter++;
    }

    public static int numberOfObjects(){
        return personCounter;
    }
}
