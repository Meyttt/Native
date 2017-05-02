/**
 * Created by Meyttt on 27.04.2017.
 */
public class Reader {
    public static String read(int[] numbers){
        StringBuilder stringBuilder = new StringBuilder();
        for(int n:numbers){
            char a = (char) ('а'+n);
            stringBuilder.append(a+"");
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        System.out.println(read(new int[]{1, 2, 3}));
        System.out.println(Integer.MAX_VALUE * Integer.MAX_VALUE);
    }
}
