package pos.api.cart;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/** Standalone checks for shared cart join codes; no database required. */
public class JoinCodeTest {
   public static void main(String[] args) {
      Random random = new Random(331);
      Set<String> seen = new HashSet<String>();

      for (int i = 0; i < 10000; i++) {
         String code = SharedCartDao.newJoinCode(random);
         check(code.length() == 6, "Code must be 6 characters: " + code);
         check(code.matches("[A-Z2-9]+"), "Code must be capital letters and digits: " + code);
         check(!code.matches(".*[01OIL].*"), "Code must not use 0, 1, O, I, or L: " + code);
         seen.add(code);
      }

      // 10,000 codes out of about 887 million should almost never repeat.
      check(seen.size() > 9990, "Codes repeat too often: " + seen.size() + " unique of 10000");
      System.out.println("Join code checks passed.");
   }

   private static void check(boolean condition, String message) {
      if (!condition) {
         throw new AssertionError(message);
      }
   }
}
