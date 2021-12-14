import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class Task2Test {
    Integer[] inArray;
    Main main;

    public Task2Test(Integer[] inArray) {
        this.inArray = inArray;
    }

    @Parameterized.Parameters
    public static Collection <Object[]> data(){
        return Arrays.asList(new Object[][]{
                {new Integer[]{4, 4, 1}},
                {new Integer[]{4, 3, 2}},
                {new Integer[]{4, 4, 4}},
                {new Integer[]{1, 4, 1}},
        });
    }
    @Before
    public void init(){
        main = new Main();
    }

    @Test
    public void massTask2Test(){
        Assert.assertTrue(Main.task2(inArray));
    }
}
