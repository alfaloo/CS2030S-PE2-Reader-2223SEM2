import java.util.stream.Stream;
import java.util.List;
import java.util.ArrayList;

class Main {
  public static String reverse(String s) {
    return Utilities.split(s).map(x -> String.valueOf(x)).reduce("", (x, y) -> y + x);
  }
  public static Stream<String> palindrome(Stream<String> stream) {
    return stream.filter(x -> x.equals(reverse(x)));
  }
}
