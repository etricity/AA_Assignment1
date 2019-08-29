package generation;

import TimeTester.TimeTester;

public class Generator_Driver {

    public static void main(String[] args)
    {
        TimeTester t = new TimeTester();
        DataGenerator generator = new DataGenerator();

        generator.generateData();

        t.start();
        generator.generateTestIn();
        t.end();
        t.timeTaken();
    }
}
