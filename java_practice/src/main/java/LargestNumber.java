import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by spyne on 8/27/16.
 */
public class LargestNumber
{
    public String largestNumber(int... nums) {
        List<Integer> list = Arrays.stream(nums).boxed().collect(Collectors.toList());
        Collections.sort(list, (o1, o2) -> {
            String s1 = o1.toString();
            String s2 = o2.toString();

            return (s2 + s1).compareTo(s1 + s2);
        });
        if (list.get(0) == 0) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        list.forEach(sb::append);
        return sb.toString();
    }
}
