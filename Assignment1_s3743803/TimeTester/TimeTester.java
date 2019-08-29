package TimeTester;

public class TimeTester {

    private long start;
    private long end;
    //Calculates the time taken to run coded between TimeTester.start() & TimeTester.end() --> in seconds (s)

    /*
    The overhead time taken to run these methods is negligable --> averages about 5.0 * 10^-6 seconds
     */
    public long timeTaken()
    {
        System.out.println("Time taken: " + (end - start) * Math.pow(10, -9) + " seconds");
        return end - start;
    }

    //Call this before the method to be tested
    public void start()
    {
        start = System.nanoTime();
    }
    //Call this after the method to be tested
    public void end()
    {
        end = System.nanoTime();
    }

}
