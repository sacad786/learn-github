import org.junit.Test;

import java.text.NumberFormat;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class PersonTest {
    @Test
    public void shouldReturnHelloworld() {
        Person sacad = new Person();
        assertEquals("hello world", sacad.helloWorld() );
    }
    @Test
    public void shouldReturnHelloSacad() {
        Person maxamed = new Person();
        assertEquals("hello sacad", maxamed.hello("sacad"));
    }
    @Test
    public void shouldReturnNuberOfObjects() {
        Person sacadman = new Person();
        Person getSacadman = new Person();
        assertEquals(2, Person.numberOfObjects());
    }

    @Test
    public void shouldReturnTheNumberOfLegs(){
        FarmProblem farm = new FarmProblem();
        assertEquals(36,farm.animals(2, 3, 5));
        assertEquals(22,farm.animals(1, 2, 3));
        assertEquals(50,farm.animals(5, 2, 8));
    }

    @Test
    public void shouldReturnHowManyHoursInASecond(){
        HoursToSeconds time = new HoursToSeconds();
        assertEquals(7200, time.howManySeconds(2));
        assertEquals(36000, time.howManySeconds(10));
        assertEquals(86400, time.howManySeconds(24));
    }

    @Test
    public void shouldReturnMultipliedArray() {
        EasyProblems MutiplyArrrayByTwo = new EasyProblems();
        int[] expected = new int[] {2,4,6};
        assertEquals(expected, MutiplyArrrayByTwo.getMultipliedArr(new int[] {1, 2, 3}));
    }

    @Test
    public void shouldReturnTheMaxElement(){
        VoteMajority majority = new VoteMajority();
        assertEquals("A", majority.majorityVotes(new String[] {"A", "A", "B"}));
        assertEquals("A", majority.majorityVotes(new String[] {"A", "A", "A", "B", "C", "A"}));
        assertEquals(null, majority.majorityVotes(new String[] {"A", "B", "B", "A", "C", "C"}));
    }

    @Test
    public void shouldReturnSumOfAllNumbersInARange(){
        EasyProblems SumOfAllNumbersInARange = new EasyProblems();
        assertEquals(10, SumOfAllNumbersInARange.sumAll(new int[] {4,1}));
    }

    @Test
    public void shouldReturnDiffBetweenArrays(){
        EasyProblems differenceBetweenArrays = new EasyProblems();
//        String[] expected = new String[] {"saad"};
        String[] expected = new String[] {"gaa"};
        assertEquals(expected, differenceBetweenArrays.diffBetweenArrays(new String[] {"af","ss"}, new String[] {"af","af"}));
    }
}

