//==========================
//Karsten Pease Outcast.java
//
//will print out the outcast in the outcast files compared to synets and hypernyms. 
//==========================
public class Outcast {
    int Max;
    int length;
    String word;
    WordNet wordNet;

    public Outcast(WordNet WordNet) {
       wordNet = WordNet;
       //take the wordNet object from the Wordnet class. 
       length = 0;
       Max =0;
    }

public String outcast(String[] nouns) {
        word = "";
        // for each method, Jimmy explained  it very well in lab. 
        for(String a : nouns) {
            for(String b : nouns) {
                    //for each string it iterates however any nouns are in the array for 
               //the first thing passed in and then the second one passed in. 
                if(a == b){
                    break;       //stop does not work.
                }else {
                    length += wordNet.distance(a, b); 
                    //the length is increased the amount of distance that is between a and b,
                    
                }
            }
            while(length > Max) {
                Max = length;
                word = a;
            //if the length is bigger than 0. 
            //whatever the length is, is created into the max and the empty string is casted to what the first 
                //element in the array is. 
            }
        }
  
        return word;
        //return what the string was that was iterated first. 
    }

//given in assignment page. 
public static void main(String[] args) {
    WordNet wordnet = new WordNet(args[0], args[1]);
    Outcast outcast = new Outcast(wordnet);
    for (int t = 2; t < args.length; t++) {
        In in = new In(args[t]);
        String[] nouns = in.readAllStrings();
        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    }
}

}
