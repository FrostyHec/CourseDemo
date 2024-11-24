
import org.frosty.common.utils.Pair;
import org.junit.jupiter.api.Test;


public class PairTest {

    @Test
    public void testPairHashcodeEqual(){
        var a = new Pair<>("a", "b");
        var b = new Pair<>("a", "b");
        assert a.equals(b);
        assert a.hashCode() == b.hashCode();
    }
}
