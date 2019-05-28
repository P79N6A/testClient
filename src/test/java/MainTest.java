import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: lucaszyang
 * @Date: 2019/05/28/15:49
 * @Description:
 */
public class MainTest {



    @org.junit.Test
    public void isNumber() {


        String s = "dsfdsfds";
        String s1 = "132215";
        String s2 = "1231n";
        String s3 = "22*@*/dsd";

        System.out.println(Main.isNumber(s));
        System.out.println(Main.isNumber(s1));
        System.out.println(Main.isNumber(s2));
        System.out.println(Main.isNumber(s3));

    }
}