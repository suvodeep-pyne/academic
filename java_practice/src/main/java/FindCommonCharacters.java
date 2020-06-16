import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class FindCommonCharacters {

  public List<String> commonChars(String[] A) {
    Map<Character, Integer> merged = null;
    boolean first = true;
    for (String a : A) {
      if (first) {
        merged = toMap(a);
        first = false;
      } else {
        intersect(merged, toMap(a));
      }
    }
    return stringify(merged);
  }

  List<String> stringify(Map<Character, Integer> merged) {
    List<String> l = new ArrayList<>();
    if (merged == null) {
      return l;
    }
    for (Map.Entry<Character, Integer> e : merged.entrySet()) {
      String s = e.getKey().toString();
      for (int i = 1; i <= e.getValue(); ++i) {
        l.add(s);
      }
    }
    return l;
  }

  void intersect(Map<Character, Integer> merged, Map<Character, Integer> m) {
    Set<Character> minus = new HashSet<>(merged.keySet());
    minus.removeAll(m.keySet());
    for (Character c : minus) {
      merged.remove(c);
    }

    for (Character c : merged.keySet()) {
      merged.put(c, Math.min(merged.get(c), m.get(c)));
    }
  }

  Map<Character, Integer> toMap(String s) {
    Map<Character, Integer> m = new HashMap<>();
    for (int i = 0; i < s.length(); ++i) {
      char c = s.charAt(i);
      m.put(c, m.getOrDefault(c, 0) + 1);
    }
    return m;
  }
}