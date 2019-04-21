//===============================
//Karsten Pease WordNet.java
//Main program for project 3
//===============================
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Queue;


public class WordNet {

    LinearProbingHashST<Integer,String> IDmap;
    LinearProbingHashST<String,Queue <Integer>> wordMap;
    //I chose to put a queue because it is first in last out and using a stack would not
    // make since if I want to grab the last element in the queue.
    Graph G;
    ShortestCommonAncestor sca;
    int verticies;
    String temp1,temp2;
    String tempWord;
    In in, in2;

   //Read in the file just like the code he gave us for prog 1 & 2.
    public WordNet(String synsets, String hypernyms){
        if(synsets == null){
            throw new NullPointerException();
        }
        if(hypernyms == null){
            throw new NullPointerException();
        }
        IDmap = new LinearProbingHashST<Integer,String>();
        wordMap = new LinearProbingHashST<String, Queue<Integer>>();

        in = new In(synsets);
        in2 = new In(hypernyms);


        while(in.hasNextLine()){
            temp1 = in.readLine();
            String [] parse = temp1.split(",");
            //put it on an array because there is many values that we need to palce.
            int ID = Integer.parseInt(parse[0]);
            String[] words = parse[1].split(" ");
            IDmap.put(ID, parse[1]);// cant put words in because it asks for a stirng not an array of strings.
            //Can do ID because it is an int.
            for(int i = 0; i < words.length; i++){
                tempWord = words[i];
                if(!wordMap.contains(tempWord)){
                    wordMap.put(tempWord, new Queue<Integer>());
                    wordMap.get(tempWord).enqueue(ID);
                }else{
                    break;
                }
            }
        }

        verticies = wordMap.size();
        G = new Graph(verticies);

        // now we read in the Hypernyms.txt.
        //almost same method as above.
        while(in2.hasNextLine()){
            temp2 = in2.readLine();
            String [] parse2 = temp2.split(",");
            int HypID = Integer.parseInt(parse2[0]);
            for(int i = 0; i<parse2.length; i++){
                int Hypernyms = Integer.parseInt(parse2[i]);
                G.addEdge(HypID, Hypernyms);
            }
        }
        sca = new ShortestCommonAncestor(G);
    }


    public Iterable<String> nouns(){
        return wordMap.keys();
        // iterate though the word Map get the nouns keys
    }


    public boolean isNoun(String word){
        return wordMap.contains(word);
        // checks to see if the word map has a noun true yes false no.
    }


    public int distance(String noun1, String noun2){
        ErrorCheck2(noun1,noun2);
        ErrorCheck1(noun1,noun2);
        Queue<Integer> A = wordMap.get(noun1);
        Queue<Integer> B = wordMap.get(noun2);
        return sca.length(A, B);
        //requirements must access length.
        // On the Queue the ints are put into 2 variables for their specific noun at the location
        // The ints "A" and "B" are then passed into shortest common ancestor to the length method
    }


    public String sca(String noun1, String noun2){
        ErrorCheck2(noun1,noun2);
        ErrorCheck1(noun1,noun2);
        Queue<Integer> A = wordMap.get(noun1);
        Queue<Integer> B = wordMap.get(noun2);
        int ancestor = sca.ancestor(A, B);
        return IDmap.get(ancestor);
        //Requirements must access ancestor.
        // Same as distance but the ID map finds the closest ancestor between the two points passed in.
    }

    public void ErrorCheck1(String noun1, String noun2){
        if(isNoun(noun1)== false){
            throw new IllegalArgumentException();
        }if(isNoun(noun2)== false){
            throw new IllegalArgumentException();
        }
        //simple error methods so we can stay clean on code.
    }

    public void ErrorCheck2(String noun1, String noun2){
        if(noun1 == null){
            throw new NullPointerException();
        }
        if(noun2 == null){
            throw new NullPointerException();
    }
   }


    public static void main(String[] args) {
    }
}
