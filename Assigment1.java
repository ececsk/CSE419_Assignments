package aima.core.Assigments;

/*
Assigment-1
181805036 SEHER KUMSAR
181805077 EMİNE ECE COŞKUNÇAY
*/

import java.math.BigDecimal;
import java.util.*;


import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensBoard.Config;
import aima.core.environment.nqueens.NQueensFunctions;
import aima.core.environment.nqueens.QueenAction;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.TreeSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.search.informed.GreedyBestFirstSearch;
import aima.core.search.informed.RecursiveBestFirstSearch;
import aima.core.search.local.GeneticAlgorithm;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aima.core.search.uninformed.DepthLimitedSearch;
import aima.core.search.uninformed.IterativeDeepeningSearch;
import aima.core.search.uninformed.UniformCostSearch;
import aima.core.search.framework.qsearch.GraphSearch;


public class Assigment1 {
	
	private static void Assigment1() {
		
		Assigment1 demo = new Assigment1();
		// prog.setBoardSize(32);
		demo.addProgressTracker(demo::printProgress);
		
		solveNQueensWithBreadthFirstSearch();
		System.out.println("NQueens Breadth-first search experiment (boardSize=" + boardSize + ") -->");
		demo.initExperiment(Config.EMPTY);
		demo.startExperiment(new BreadthFirstSearch<>(new TreeSearch<>()));
		demo.printResult();
		
		
		solveNQueensWithDepthFirstSearch();
		System.out.println("NQueens Depth-first search experiment (boardSize=" + boardSize + ") -->");
		demo.initExperiment(Config.EMPTY);
		demo.startExperiment(new DepthFirstSearch<>(new TreeSearch<>()));
		demo.printResult();
		
		
		solveNQueensWithDepthLimitedSearch();
		System.out.println("NQueens Depth-limited search experiment (boardSize=" + boardSize + ") -->");
		demo.initExperiment(Config.EMPTY);
		demo.startExperiment(new DepthLimitedSearch<>(boardSize));
		demo.printResult();
		
		
		solveNQueensWithIterativeDeepeningSearch();
		System.out.println("NQueens Iterative Deepening Search experiment (boardSize=" + boardSize + ") -->");
		demo.initExperiment(Config.EMPTY);
		demo.startExperiment(new IterativeDeepeningSearch<>());
		demo.printResult();
		
		
		solveNQueensUniformCostSearch();
		System.out.println("NQueens Uniform Cost Search experiment (boardSize=" + boardSize + ") -->");
		demo.initExperiment(Config.EMPTY);
		demo.startExperiment(new UniformCostSearch<>());
		demo.printResult();
		
		
		//
		solveNQueensGreedyBestFirstSearch();
		System.out.println("NQueens Greedy Best First Search experiment (boardSize=" + boardSize + ") -->");
		
		demo.initExperiment(Config.EMPTY);
		demo.startExperiment(new UniformCostSearch<>(new GraphSearch<>()));
		demo.printResult();
		
		solveNQueensWithAStarSearch();
		System.out.println("NQueens A Star Search experiment (boardSize=" + boardSize + ") -->");
		demo.initExperiment(Config.QUEENS_IN_FIRST_ROW);
		demo.startExperiment(new AStarSearch<>(new GraphSearch<>(), NQueensFunctions::getNumberOfAttackingPairs));
		demo.printResult();
		//
		
		//solveNQueensBestFirstSearch();
		//solveNQueensRecursiveBestFirstSearch();
		/*
		System.out.println("NQueens RecursiveBestFirstSearch experiment (boardSize=" + boardSize + ") -->");
		demo.initExperiment(Config.QUEENS_IN_FIRST_ROW);
		demo.startRecursiveBestFirstSearch();
		demo.printResult();
		 */
		
		
		System.out.println("Board Size = " + boardSize);
		
		System.out.println("Board Layouts = " + (new BigDecimal(boardSize)).pow(boardSize));
		
		

	}
	
	private static int boardSize = 8;
	
	public static boolean testGoal(NQueensBoard state) {
		   return state.getNumberOfQueensOnBoard() == state.getSize() && state.getNumberOfAttackingPairs() == 0;
		}
	
	
	public int k = 30;
	
	//public int h = 0;

	private GeneticAlgorithm<Integer> genAlgo;
	private SearchForActions<NQueensBoard, QueenAction> search;
	private NQueensBoard board;
	private List<ProgressTracker> progressTracers = new ArrayList<>();
	
	
	public void setBoardSize(int size) {
		boardSize = size;
		board = new NQueensBoard(boardSize);
	}

	public void addProgressTracker(ProgressTracker tracer) {
		progressTracers.add(tracer);
	}

	public void initExperiment(Config config) {
		board = new NQueensBoard(boardSize, config);
		genAlgo = null;
		search = null;
	}
	
	public void startExperiment(SearchForActions<NQueensBoard, QueenAction> search) {

		//search.addNodeListener(n -> notifyProgressTrackers(n.getState(), search.getMetrics()));

		Problem<NQueensBoard, QueenAction> problem;
		if (board.getNumberOfQueensOnBoard() == 0)
			problem = new GeneralProblem<>(board, NQueensFunctions::getIFActions,
					NQueensFunctions::getResult, NQueensFunctions::testGoal);
		else
			problem = new GeneralProblem<>(board, NQueensFunctions::getCSFActions,
					NQueensFunctions::getResult, NQueensFunctions::testGoal);
		Optional<List<QueenAction>> actions = search.findActions(problem);
		if (actions.isPresent())
			for (QueenAction action : actions.get())
				board = NQueensFunctions.getResult(board, action);

		notifyProgressTrackers(board, search.getMetrics());
	}
	
	
	public NQueensBoard getBoard() {
		return board;
	}

	private void printProgress(NQueensBoard board, Metrics metrics) {
		System.out.println(board.getNumberOfAttackingPairs() + " attacking pairs " + metrics);
	}

	private void printResult() {
		if (board != null) {
			System.out.println("Final State:\n" + board);
			System.out.println("Attacking pairs: " + board.getNumberOfAttackingPairs());
		}
		if (genAlgo != null)
			System.out.println("Metrics: " + genAlgo.getMetrics());
		if (search != null)
			System.out.println("Metrics: " + search.getMetrics());
		System.out.println("Experiment finished.\n");
	}

	

	private void notifyProgressTrackers(NQueensBoard board, Metrics metrics) {
		for (ProgressTracker tracker : progressTracers)
			tracker.trackProgress(board, metrics);
	}

	public interface ProgressTracker {
		void trackProgress(NQueensBoard board, Metrics metrics);
	}
	
	
	
	
	
	private static void solveNQueensWithBreadthFirstSearch() {
	    System.out.println("\n--- NQueensDemo BFS ---");

	    Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createIncrementalFormulationProblem(boardSize);
	    SearchForActions<NQueensBoard, QueenAction> search = new BreadthFirstSearch<>(new GraphSearch<>());
	    Optional<List<QueenAction>> actions = search.findActions(problem);

	    actions.ifPresent(qActions -> qActions.forEach(System.out::println));
	    System.out.println(search.getMetrics());
	    
	}
	
	private static void solveNQueensWithDepthFirstSearch() {
		System.out.println("\n--- NQueensDemo DFS ---");
		
		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createIncrementalFormulationProblem(boardSize);
		SearchForActions<NQueensBoard, QueenAction> search = new DepthFirstSearch<>(new TreeSearch<>());
		Optional<List<QueenAction>> actions = search.findActions(problem);
		
		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
		
	}
	
	private static void solveNQueensWithDepthLimitedSearch() {
		System.out.println("\n--- NQueensDemo DLS ---");
		
		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createIncrementalFormulationProblem(boardSize);
		SearchForActions<NQueensBoard, QueenAction> search = new DepthLimitedSearch<>(boardSize);
		Optional<List<QueenAction>> actions = search.findActions(problem);
		
		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
		
	}
	
	private static void solveNQueensWithIterativeDeepeningSearch() {
		System.out.println("\n--- NQueensDemo Iterative DS ---");
		
		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createIncrementalFormulationProblem(boardSize);
		SearchForActions<NQueensBoard, QueenAction> search = new IterativeDeepeningSearch<>();
		Optional<List<QueenAction>> actions = search.findActions(problem);
		
		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
		
	}
	
	private static void solveNQueensUniformCostSearch() {
		System.out.println("\n--- NQueensDemo UniformCostSearch ---");
		
		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createIncrementalFormulationProblem(boardSize);
		SearchForActions<NQueensBoard, QueenAction> search = new UniformCostSearch<>();
		Optional<List<QueenAction>> actions = search.findActions(problem);
		
		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
		
	}
	
	private static void solveNQueensGreedyBestFirstSearch() {
		System.out.println("\n--- NQueensDemo GreedyBestFirstSearch ---");
		
		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createCompleteStateFormulationProblem(boardSize, Config.QUEENS_IN_FIRST_ROW);
		SearchForActions<NQueensBoard, QueenAction> search = new GreedyBestFirstSearch<>(new GraphSearch<>(),
				NQueensFunctions::getNumberOfAttackingPairs);
		Optional<List<QueenAction>> actions = search.findActions(problem);
		
		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
		
	}
	
	private static void solveNQueensWithAStarSearch() {
		System.out.println("\n--- NQueensDemo A* ---");
		
		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createCompleteStateFormulationProblem(boardSize, Config.QUEENS_IN_FIRST_ROW);
		SearchForActions<NQueensBoard, QueenAction> search = new AStarSearch<>(new GraphSearch<>(),
				NQueensFunctions::getNumberOfAttackingPairs);
		Optional<List<QueenAction>> actions = search.findActions(problem);
		
		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
		
	}
	/*
	private static void solveNQueensBestFirstSearch() {
		System.out.println("\n--- NQueensDemo BestFirstSearch ---");
		
		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createCompleteStateFormulationProblem(boardSize, Config.QUEENS_IN_FIRST_ROW);
		SearchForActions<NQueensBoard, QueenAction> search = new BestFirstSearch<>(new AStarSearch<>(),
				NQueensFunctions::getNumberOfAttackingPairs);
		
		Optional<List<QueenAction>> actions = search.findActions(problem);
		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
		
	}
	*/
	
	//
	private static void solveNQueensRecursiveBestFirstSearch() {
		System.out.println("\n--- NQueensDemo RecursiveBestFirstSearch ---");
		
		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createCompleteStateFormulationProblem(boardSize, Config.QUEENS_IN_FIRST_ROW);
		
		//SearchForActions<NQueensBoard, QueenAction> search = new RecursiveBestFirstSearch<>(new AStarSearch<>(h));
		SearchForActions<NQueensBoard, QueenAction> search = new RecursiveBestFirstSearch<NQueensBoard, QueenAction>(null);
				
		Optional<List<QueenAction>> actions = search.findActions(problem);
		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
		
	}
	
	
	public static void main(String[] args) {
		
		Assigment1();
		
	}
	
	
}