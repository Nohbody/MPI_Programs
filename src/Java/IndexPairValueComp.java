package Java;

public class IndexPairValueComp implements Comparable<IndexPairValueComp> {

	int listIndex, rank;
	double value;
	
	@Override
	public int compareTo(IndexPairValueComp o) {
		if (value < o.value)
			return 1;
		else if (value > o.value)
			return -1;
		return 0;
	}

}
