/**
 * Created by Meyttt on 03.05.2017.
 */
public class InterfaceTest {
}
interface Cat{
    final static String name = "Cat";
}
class PrettyCat implements Cat{
    static String name = "Pretty Cat";
    public static String getName(){
        return name;
    }

    public static void main(String[] args) {
        System.out.println(getName());
    }
}

