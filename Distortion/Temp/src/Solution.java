import java.math.BigDecimal;
import java.util.*;

class Solution {

	public static void main(String[] argh) {
		// Input
		Solution sol = new Solution();
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		List<Store> list = new ArrayList<Store>();
		List<BigDecimal> nums = new ArrayList<BigDecimal>();
		String[] out = new String[n];
		for (int i = 0; i < n; i++) {
			String s = sc.next();
			BigDecimal d = new BigDecimal(s);
			Store temp = sol.new Store(s, d, i);
			list.add(temp);
			nums.add(new BigDecimal(s));
		}

		// Write your code here
		Collections.sort(nums, Collections.reverseOrder());

		for (int i = 0; i < n; i++) {
			second: for (int j = 0; j < n; j++) {
				if (nums.get(i).equals(list.get(j).info) && list.get(j).used == false) {
					out[i] = list.get(j).original;
					list.get(j).used = true;
					break second;
				}
			}
		}

		// Output
		for (int i = 0; i < n; i++) {
			System.out.println(out[i]);
		}
		sc.close();
	}

	class Store {
		String original;
		BigDecimal info;
		int index;
		boolean used = false;

		public Store(String s, BigDecimal d, int i) {
			original = s;
			info = d;
			index = i;
		}
	}

}
