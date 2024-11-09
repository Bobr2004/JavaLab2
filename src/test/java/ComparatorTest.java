

import com.labab.BeerComparator;
import generated.Beer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComparatorTest {

    private BeerComparator comparator;
    private Beer.BeerItem beerItem1;
    private Beer.BeerItem beerItem2;
    private Beer.BeerItem beerItem3;

    @BeforeEach
    public void setUp() {
        comparator = new BeerComparator();

        // Creating instances of Orangery.Flower
        beerItem1 = new Beer.BeerItem();
        beerItem1.setName("SFfa");

        beerItem2 = new Beer.BeerItem();
        beerItem2.setName("Vsafad");

        beerItem3 = new Beer.BeerItem();
        beerItem3.setName("SFfa");
    }

    @Test
    public void testCompare_LexicographicalOrder() {
        int result = comparator.compare(beerItem1, beerItem2);
        assertEquals(true, result < 0, "Expected Rose to be lexicographically before Tulip");
    }

    @Test
    public void testCompare_SameName() {
        int result = comparator.compare(beerItem1, beerItem3);
        assertEquals(0, result, "Expected comparison to return 0 for flowers with the same name");
    }

    @Test
    public void testCompare_ReverseOrder() {
        int result = comparator.compare(beerItem2, beerItem1);

        assertEquals(true, result > 0, "Expected Tulip to be lexicographically after Rose");
    }
}
