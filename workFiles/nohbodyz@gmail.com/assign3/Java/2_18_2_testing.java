import java.io.File;
import java.lang.reflect.Array;

public class testing {

	public static void main(String[] args) {
		File myfile = new File("/Users/jacklewis/dsaws/WordNet/mySyns.txt");
		
		In synsetsIn = new In(myfile);
		//In hypernymsIn = new In(hypernyms);
		
		
		
		String[] newArr = synsetsIn.readAllLines();
		String[] subArr = newArr[0].split(",");
		
		System.out.println(subArr[1]);
		
		
		
		
	}

}
