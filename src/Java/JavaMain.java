package Java;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import javax.print.PrintException;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import Java.FlatTree.Node;
import mpi.MPI;
import mpi.MPIException;

public class JavaMain {

	static FlatTree[] allTrees;
	static int treeIndex = 0;

	public final static double MIN_MATCH = 0.90;
	static JavaMain ref = new JavaMain();
	
	static String userFolder;

	public static void main(String[] args) throws IOException, MPIException {

//		boolean intFlag = args[0].equals("1");
//		String subDir = args[1];
//		String sourcesDir = args[2];
//		String pastDir = args[3];
//		String userFolder = args[4];
//		double decayFactor = Double.parseDouble(args[5]);
//		boolean useITF = args[6].equals("1");
		
		boolean intFlag = true;
		String subDir = "assign3";
		String pastDir = "???";
		String sourcesDir = "???";
		userFolder = "../workFiles/nohbodyz@gmail.com";
		boolean useITF = false;
		double decayFactor = Double.parseDouble("0.3");
		
//		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		
		// Create the primary and secondary file count for splitting
		int primaryCount, secondaryCount = 0;
		
		primaryCount = (new File(userFolder + "/" + subDir + "/Java")).list().length;
		
		if (!sourcesDir.equals("???"))
			secondaryCount += (new File(userFolder + "/" + sourcesDir + "/Java")).list().length;
		if (!pastDir.equals("???"))
			secondaryCount += (new File(userFolder + "/" + pastDir + "/Java")).list().length;
		
		System.out.println(primaryCount + " " + secondaryCount);
		allTrees = new FlatTree[primaryCount + secondaryCount];
		
		// Create all the trees using a unique index into allTrees
		try (Stream<Path> paths = Files.walk(Paths.get(userFolder + "/" + subDir + "/Java"))) {
		    paths
		        .filter(Files::isRegularFile)
		        .forEach(path -> ref.prepareTree(path));
		}
		
		if (!sourcesDir.equals("???")) {
			try (Stream<Path> paths = Files.walk(Paths.get(userFolder + "/" + sourcesDir + "/Java"))) {
			    paths
			        .filter(Files::isRegularFile)
			        .forEach(path -> ref.prepareTree(path));
			}
		}
		
		if (!pastDir.equals("???")) {
			try (Stream<Path> paths = Files.walk(Paths.get(userFolder + "/" + pastDir + "/Java"))) {
			    paths
			        .filter(Files::isRegularFile)
			        .forEach(path -> ref.prepareTree(path));
			}
		}


		// Count files that contain a particular tree 
		HashMap<String, Integer> fileCounts = new HashMap<String, Integer>();

		for (FlatTree ft: allTrees) {
			if (ft == null)
				break;
			Iterator<Entry<String, Integer>> it = ft.firstNode.treeCounts.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>) it.next();
				if (fileCounts.get(pair.getKey()) == null)
					fileCounts.put(pair.getKey(), 0);
				fileCounts.put(pair.getKey(), fileCounts.get(pair.getKey())+1);
			}
		}


		List<String> stopWords = Arrays.asList(")", "(", "[", "]", "{", "}", ";", ",");

		FlatTree ft;
		for (int i=0; i < allTrees.length; i++) {
			ft = allTrees[i];
			if (ft == null)
				break;
			ft.assignWeights(ft.firstNode, stopWords, fileCounts, ft.firstNode, allTrees.length, useITF);
			ft.allChildren(ft.firstNode);
		}
		// Start the MPI-specific code
		MPI.Init(args);
		
		// Build our index pairs as the "jobs" for each node to handle
		IndexPair next = new IndexPair();
		int primaryPairCount = primaryCount * (primaryCount-1) / 2;
		int secondaryPairCount = primaryCount * secondaryCount;
		ByteBuffer primaryBuffer  = MPI.newByteBuffer(next.getExtent() * primaryPairCount);
		ByteBuffer secondaryBuffer = MPI.newByteBuffer(next.getExtent() * secondaryPairCount);
		
		int k = 0;
		int x = 0;
		for (int i=0; i<primaryCount; i++) {
			for (int j=0; j < i; j++) {
			    IndexPair.Data n = next.getData(primaryBuffer, k);
			    n.putFirst(i);
			    n.putSecond(j);
			    k++;
			}
			for (int p=0; p<secondaryCount; p++) {
				IndexPair.Data n = next.getData(secondaryBuffer, x);
				n.putFirst(i);
				n.putSecond(p);
				x++;
			}
		}
		
		// Divide the jobs to the nodes
		int worldSize = MPI.COMM_WORLD.getSize();
		int c1 = primaryPairCount/worldSize + 1;
		int c2 = secondaryCount/worldSize + 1;
		ByteBuffer primaryJobs  = MPI.newByteBuffer(next.getExtent() * c1);
		ByteBuffer secondaryJobs = MPI.newByteBuffer(next.getExtent() * c2);
		
		MPI.COMM_WORLD.scatter(primaryBuffer,c1,next.getType(),primaryJobs,c1,next.getType(),0);
		MPI.COMM_WORLD.scatter(secondaryBuffer,c2,next.getType(),secondaryJobs,c2,next.getType(),0);
		
		// Start the tree comparisons
		ArrayList<PairValue> myScores = new ArrayList<PairValue>();
		IndexPairValue ipv = new IndexPairValue();
		ByteBuffer myIPVs = MPI.newByteBuffer(ipv.getExtent() * (c1 + c2));

		FlatTree tree1, tree2;

		long startTime = System.nanoTime();
		
		PairValue minScore = new PairValue("aaa", "bbb", 0, -1, -1);
		myScores.add(minScore);
		
		HashMap<FlatTree, Double> selfScore = new HashMap<FlatTree, Double>();
		double record = 0;
		
		// TODO: Gather then Broadcast
		// Run through all trees to get the total score matched to themselves
		for (int i=0; i<allTrees.length; i++) {
			tree1 = allTrees[i];
			if (tree1 == null)
				break;
			record = minScore.assignSimilarity2(tree1, tree1, true, selfScore, decayFactor);
			selfScore.put(tree1, record);
			System.out.println("Self score: " + tree1.originFile + " " + record);
		}
		
		int ipvIndex = 0;
		int rank = MPI.COMM_WORLD.getRank();

		// Primary jobs
		for (int i=0; i<c1; i++) {
			IndexPair.Data n = next.getData(primaryJobs, i);
			
			System.out.println("JOB: " + i + " " + n.getFirst() + " " + n.getSecond());
			
			tree1 = allTrees[n.getFirst()];
			tree2 = allTrees[n.getSecond()];
			
			if (tree1 == null || tree2 == null)
				break;

			// Only run the comparison if one of the trees is a primary file
			if (tree1.fileDirSouce.equals(subDir) || tree2.fileDirSouce.equals(subDir)) {
				System.out.println(tree1.subSource + " " + tree2.subSource);
				// Do not compare files within a submission unless specified
				if ((tree1.subSource.equals(tree2.subSource) && intFlag) 
						|| !tree1.subSource.equals(tree2.subSource)) {
	
					PairValue nextPV = new PairValue(tree1.originFile, tree2.originFile, 0, tree1.firstNode.id, tree2.firstNode.id);
					System.out.println("Calculating: " + tree1.originFile + " " + tree2.originFile);
					nextPV.score = nextPV.assignSimilarity2(tree1, tree2, false, selfScore, decayFactor);
	
					if (nextPV.score >= MIN_MATCH) {
						myScores.add(nextPV);
						
						IndexPairValue.Data pv = ipv.getData(myIPVs, ipvIndex);
					    pv.putRank(rank);
					    pv.putListIndex(ipvIndex);
					    pv.putValue(nextPV.score);
					    ipvIndex++;
					}
				}
			}
		}
		
		// Secondary jobs
		if (secondaryCount != 0) {
			for (int i=0; i<c2; i++) {
				IndexPair.Data n = next.getData(secondaryJobs, i);
				
				tree1 = allTrees[(n.getFirst()/primaryCount - 1) * secondaryCount + n.getFirst()];
				tree2 = allTrees[(n.getSecond()/secondaryCount) * primaryCount + n.getSecond()];
				
				if (tree1 == null || tree2 == null)
					break;
				
				// Only run the comparison if one of the trees is a primary file
				if (tree1.fileDirSouce.equals(subDir) || tree2.fileDirSouce.equals(subDir)) {
					System.out.println(tree1.subSource + " " + tree2.subSource);
					// Do not compare files within a submission unless specified
					if ((tree1.subSource.equals(tree2.subSource) && intFlag) 
							|| !tree1.subSource.equals(tree2.subSource)) {
		
						PairValue nextPV = new PairValue(tree1.originFile, tree2.originFile, 0, tree1.firstNode.id, tree2.firstNode.id);
						System.out.println("Calculating: " + tree1.originFile + " " + tree2.originFile);
						nextPV.score = nextPV.assignSimilarity2(tree1, tree2, false, selfScore, decayFactor);
		
						if (nextPV.score >= MIN_MATCH) {
							myScores.add(nextPV);
							
							IndexPairValue.Data pv = ipv.getData(myIPVs, ipvIndex);
						    pv.putRank(rank);
						    pv.putListIndex(ipvIndex);
						    pv.putValue(nextPV.score);
						    ipvIndex++;
						}
					}
				}
			}
		}
		
		MPI.COMM_WORLD.barrier();
		
		// Gather all of the scores and histories from the nodes
		ByteBuffer allIPVsBuf = MPI.newByteBuffer(ipv.getExtent() * (c1 + c2) * worldSize);
		MPI.COMM_WORLD.gather(myIPVs, c1+c2, ipv.getType(), allIPVsBuf, c1+c2, ipv.getType(), 0);

		IndexPairValueComp[] allIPVs = new IndexPairValueComp[(c1 + c2) * worldSize];
		if (rank == 0) {
			for (int i=0; i < (c1+c2)*worldSize; i++) {
				IndexPairValue.Data pv = ipv.getData(allIPVsBuf, i);
				IndexPairValueComp ipvComp = new IndexPairValueComp();
			    ipvComp.rank = pv.getRank();
			    ipvComp.listIndex = pv.getListIndex();
			    ipvComp.value = pv.getValue();
			    allIPVs[i] = ipvComp;
			}
			Arrays.sort(allIPVs);
		}
		
		int reportLim = Math.min(250, (c1+c2)*worldSize);
		
		MPI.COMM_WORLD.bcast(allIPVs, reportLim, ipv.getType(), 0);
		
		PairValue nextPV;
		FlatTree t1, t2;
		
		// Make the top scoring output files
		for (int i=0; i<reportLim; i++) {
			IndexPairValueComp pv = allIPVs[i];
			// Node only handles the matches that came from it
			if (pv.rank == rank && pv.listIndex >= 0) {
				nextPV = myScores.get(pv.listIndex);
				if (nextPV.score > MIN_MATCH) {
					t1 = nextPV.t1;
					t2 = nextPV.t2;
					
					if (t1.treeMade != true)
						t1.createJavascriptTree(t1.firstNode, userFolder);
					if (t2.treeMade != true)
						t2.createJavascriptTree(t2.firstNode, userFolder);
					
					System.out.println(nextPV.file1 + " " + nextPV.file2 + " " + nextPV.score);
					nextPV.makeMatchFile(userFolder);
					nextPV.makeScoreFile(userFolder);
				}
			}
		}

		long endTime = System.nanoTime();
		
		System.out.println("TEST TIME: " + (endTime-startTime)/1000000000.0 + "\n");
		
		MPI.Finalize();
	}
	
	String fileName;
	String[] filePathSplit;
	String fileDirSource;
	String subSource;
	String[] subSourceSplit;
	
	static Java8Lexer lexer; 
	static TokenStream tokenStream;
	static Java8Parser parser;
	static ParseTree tree;
	
	static List<? extends Token> myTokens;
	static Vocabulary myVocab;
	
	ArrayList<Integer> startPos;
	ArrayList<Integer> endPos;
	ArrayList<Integer> line;
	
	Token myToken;
	String[] remFlags = new String[] {"LINE_COMMENT", "COMMENT", "WS"};
	
	Node myNode;
	FlatTree myTree;
	ArrayList<Node> nodesList;
	
	String treeString;

	public void prepareTree(Path filePath) {
		
		startPos = new ArrayList<Integer>();
		endPos = new ArrayList<Integer>();
		line = new ArrayList<Integer>();
		
		nodesList = new ArrayList<Node>(1000);

		fileName = filePath.toString();
		System.out.println(fileName);
		String splitter = File.separator.replace("\\","\\\\");
		filePathSplit = fileName.split(splitter);
		fileDirSource = filePathSplit[3];
		subSource = filePathSplit[5];
		subSourceSplit = subSource.split("_");
		subSource = subSourceSplit[0] + "_" + subSourceSplit[1];
		System.out.println("Creating tree: " + fileName);

		try {
		
			System.gc();
			
			//prepare token stream
			CharStream stream = null;
			try {
				stream = CharStreams.fromFileName(fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Prepare parser and lexer 
			lexer = new Java8Lexer(stream);
			stream = null;
			tokenStream = new CommonTokenStream(lexer);
			parser = new Java8Parser(tokenStream);
			tokenStream = null;
			tree = parser.compilationUnit();

			lexer.reset();
			myTokens = lexer.getAllTokens();
			myVocab = lexer.getVocabulary();
			
			lexer = null;
			
			System.out.println(fileName + " " + (Runtime.getRuntime().totalMemory()));

			
			while (startPos.size() < myTokens.size()) {
				startPos.add(-1);
				endPos.add(-1);
				line.add(-1);
			}
			
			
			int nodeCount = 0;

			// Store position information for each token in stream
			// Tokens are retrieved in the same order as an in-order leaf traversal
			for (int i = 0; i < myTokens.size(); i++) {
				myToken = myTokens.get(i);
				if (Arrays.asList(remFlags).contains(myVocab.getSymbolicName(myToken.getType())) == false) {
					startPos.set(i, myToken.getStartIndex());
					endPos.set(i, myToken.getStopIndex());
					line.set(i, myToken.getLine());
					nodeCount++;
				}
				else {
					startPos.set(i, -1);
					endPos.set(i, -1);
					line.set(i, -1);
				}
			}
			
			myTokens = null;
			myVocab = null;

			// Show AST in console
			// System.out.println(tree.toStringTree(parser) + "\n");

			// Flatten the ANTLR generated parse tree
			// FlatTree myLeaflessTree = new FlatTree(tree.toStringTree(parser)); 
			// myLeaflessTree.leafless = true;
			treeString = tree.toStringTree(parser);
			
			myTree = new FlatTree(treeString);
			System.out.println("Made tree");
			treeString = null;
			myTree.originFile = fileName;
			myTree.fileDirSouce = fileDirSource;
			myTree.subSource = subSource;

			// Keep information on where in the source code the tree represents
			while (nodesList.size() <= nodeCount) {
				nodesList.add(myTree.new Node());
			}
			System.out.println("Traversing leaves...");
			myTree.traverseLeavesList(nodesList, myTree.firstNode);
			System.out.println("Traversed!");
			
			int j = 0;

			System.out.println("Assigning positions...");
			for (int i = 0; i < nodeCount; i++) {
				
				// nodesList contains only the nodes not ignored so
				// the index needs to be realigned to the position lists
				while(startPos.get(j) == -1)
					j++;
				myNode = nodesList.get(i);
				myNode.startPos = startPos.get(j);
				myNode.endPos = endPos.get(j);
				myNode.startLine = line.get(j);
				myNode.endLine = line.get(j);
				//System.out.println(myNode.startPos + ":" + myNode.endPos + " " + myNode.startLine + " " + myNode.endLine);
				j++;
			}
			
			nodesList = null;
			startPos = null;
			endPos = null;
			line = null;
			
			myTree.assignPositions(myTree.firstNode);
			
			System.out.println("Assigned!");

			// Replace the subtree of an expressionStatement with in-order
			// traversal string of leaves
			myTree.replaceExpr(myTree.firstNode);

			// Create hash values to count subtrees
			// Note: children are no longer in-order afterwards
			System.out.println("Creating hashes...");
			myTree.createHashes(myTree.firstNode);
			System.out.println("Created!");
			
			// Keep track of how often a subtree appears below every node
			System.out.println("Counting hashes...");
			myTree.updateAllCounts(myTree.firstNode);
			System.out.println("Counted!");
			//myTree.firstNode.printCounts();
			
			// Create image representations of the trees
//			generateAntlrTreeImage(parser, tree, "./trees/" + fileName.substring(fileName.lastIndexOf('/')+1, fileName.lastIndexOf('.')) + "_antlr.png");
//			generateFlatTreeImage(parser, myTree, "./trees/" + fileName.substring(fileName.lastIndexOf('/')+1, fileName.lastIndexOf('.')) + ".png");

			// Look for the next available index to write into allTrees
			while(allTrees[treeIndex] != null)
				treeIndex++;
			allTrees[treeIndex] = myTree;
			treeIndex++;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void generateFlatTreeImage(Java8Parser myParser, FlatTree myTree, String fileName) {

		System.out.println("Generating flat tree image: " + fileName);
		FlatTreeViewer viewr2 = new FlatTreeViewer(Arrays.asList(
				myParser.getRuleNames()),myTree);

		try {
			viewr2.save(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PrintException e) {
			e.printStackTrace();
		}
	}

	public static void generateAntlrTreeImage(Java8Parser myParser, ParseTree antlrTree, String fileName) {

		System.out.println("Generating ANTLR tree image: " + fileName);
		TreeViewer viewr = new TreeViewer(Arrays.asList(
				myParser.getRuleNames()),antlrTree);

		try {
			viewr.save(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PrintException e) {
			e.printStackTrace();
		}
	}
}