/**
 * Created by Meyttt on 01.05.2017.
 */
public class StaticTests {
    static String stringS;
    String stringD;
    class ClassD{
        //static String innerStringS;
        String innerStringD;
        //static String getStr(){return null;}
        String mainStringS = stringS;
        String mainStringD = stringD;
    }
    static class ClassS{
        String nestedStringD;
        static String nestedStringS;
        String mainStringS = stringS;
//        String mainStringD = stringD;
        static String getNestedStringS(){
            return nestedStringS;
        }
        String getNestedStringD(){
            return nestedStringD;
        }
    }
}
