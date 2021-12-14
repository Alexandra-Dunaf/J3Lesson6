import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Task1Test {
    Main main;

    @Before
    public void prepare(){
        main = new Main();
    }

    @Test
    public void t1(){
        Assert.assertArrayEquals(new Integer[]{5, 35, 2, 1},main.task1(new Integer[]{2, 4, 5, 35, 2, 1}));
    }

    @Test(expected = RuntimeException.class)
    public void t2(){
        Assert.assertArrayEquals(new Integer[]{5, 35, 2, 1},main.task1(new Integer[]{2, 5, 35, 2, 1}));
    }

    @Test()
    public void t3(){
        Assert.assertArrayEquals(new Integer[]{},main.task1(new Integer[]{2, 5, 35, 2, 1, 4}));
    }

    @Test()
    public void t4(){
        Assert.assertArrayEquals(new Integer[]{2, 1},main.task1(new Integer[]{2, 5, 4, 35, 4, 2, 1}));
    }
}
