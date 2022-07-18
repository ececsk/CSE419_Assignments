package aima.core.Assigments;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;


import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensFunctions;
import aima.core.environment.nqueens.NQueensGenAlgoUtil;
import aima.core.environment.nqueens.QueenAction;
import aima.core.environment.nqueens.NQueensBoard.Config;
import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;
import aima.core.search.csp.Variable;
import aima.core.search.csp.examples.NQueensCSP;
import aima.core.search.csp.solver.CspListener;
import aima.core.search.csp.solver.CspSolver;
import aima.core.search.csp.solver.FlexibleBacktrackingSolver;
import aima.core.search.csp.solver.MinConflictsSolver;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.Problem;

import aima.core.search.local.FitnessFunction;
import aima.core.search.local.GeneticAlgorithm;
import aima.core.search.local.HillClimbingSearch;
import aima.core.search.local.Individual;
import aima.core.search.local.Scheduler;
import aima.core.search.local.SimulatedAnnealingSearch;
import aima.core.util.datastructure.XYLocation;


/*
Assigment-2
181805036 SEHER KUMSAR
181805077 EMİNE ECE COŞKUNÇAY
*/


public class Assigment2 {
	
	private static int boardSize = 8;
	
	private static CSP<Variable, Integer> csp;
    private CspSolver<Variable, Integer> solver;
    private final CspListener.StepCounter<Variable, Integer> stepCounter = new CspListener.StepCounter<>();
    
    
	private int populationSize = 10;
	private double mutationProbability = 0.2;
	public int k = 30;
	private double lambda = 2.0 / 100;
	private int maxIterations = 500;


	private static Random random = new Random();
	private GeneticAlgorithm<Integer> genAlgo;
	private static SearchForActions<NQueensBoard, QueenAction> search;
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

		search.addNodeListener(n -> notifyProgressTrackers(n.getState(), search.getMetrics()));

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
	
	public void startGenAlgoExperiment(boolean randomConfig) {
		Collection<Integer> alphabet = NQueensGenAlgoUtil.getFiniteAlphabetForBoardOfSize(boardSize);
		FitnessFunction<Integer> fitnessFn = NQueensGenAlgoUtil.getFitnessFunction();

		genAlgo = new GeneticAlgorithm<Integer>(boardSize, alphabet, mutationProbability, random) {
			protected void updateMetrics(Collection<Individual<Integer>> pop, int itCount, long time) {
				super.updateMetrics(pop, itCount, time);
				double avg = 0.0;
				double max = Double.NEGATIVE_INFINITY;
				for (Individual<Integer> ind : pop) {
					double fval = fitnessFn.apply(ind);
					avg += fval;
					max = Math.max(max, fval);
				}
				avg /= pop.size();
				metrics.set("fitMax", max);
				metrics.set("fitAvg", avg);
			}
		};
		genAlgo.addProgressTracer((it, pop) -> notifyProgressTrackers(pop, fitnessFn));

		List<Individual<Integer>> population = new ArrayList<>();
		List<Integer> rep = new ArrayList<>();
		for (int i = 0; i < boardSize; i++)
			rep.add(0);
		for (int i = 0; i < populationSize; i++)
			if (randomConfig)
				population.add(NQueensGenAlgoUtil.generateRandomIndividual(boardSize));
			else
				population.add(new Individual<>(rep));

		Individual<Integer> result = genAlgo.geneticAlgorithm(population, fitnessFn, maxIterations);

		board = NQueensGenAlgoUtil.getBoardForIndividual(result);
	}
	
	// 2 tane NQueensBoard oluşturuldu
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

	private void notifyProgressTrackers(Collection<Individual<Integer>> population, FitnessFunction<Integer> fitnessFn) {
		Individual<Integer> best = genAlgo.retrieveBestIndividual(population, fitnessFn);
		notifyProgressTrackers(NQueensGenAlgoUtil.getBoardForIndividual(best), genAlgo.getMetrics());
	}

	private void notifyProgressTrackers(NQueensBoard board, Metrics metrics) {
		for (ProgressTracker tracker : progressTracers)
			tracker.trackProgress(board, metrics);
	}

	public interface ProgressTracker {
		void trackProgress(NQueensBoard board, Metrics metrics);
	}
	
	public static boolean testGoal(NQueensBoard state) {
		return state.getNumberOfQueensOnBoard() == state.getSize() && state.getNumberOfAttackingPairs() == 0;
	}
	
	// CSP
	
	
    private static NQueensBoard getBoard(Assignment<Variable, Integer> assignment) {
        NQueensBoard board2 = new NQueensBoard(csp.getVariables().size());
        for (Variable var : assignment.getVariables()) {
            int col = Integer.parseInt(var.getName().substring(1)) - 1;
            int row = assignment.getValue(var) - 1;
            board2.addQueenAt(new XYLocation(col, row));
        }
        return board2;
    }
    
	public void startExperiment2() {
		
        Optional<Assignment<Variable, Integer>> solution = solver.solve(csp);
        if (solution.isPresent()) {
            NQueensBoard board2 = getBoard(solution.get());            
        }
    }
	
	
	
	
	private static void solveNQueensMinConflictsSolver() {
		int size = 8;
		CSP<Variable, Integer> csp = new NQueensCSP(size);
		CspListener.StepCounter<Variable, Integer> stepCounter = new CspListener.StepCounter<>();
		CspSolver<Variable, Integer> solver;
		Optional<Assignment<Variable, Integer>> solution;
		
		System.out.println(size + "-Queens (Min-Conflicts)");
		solver = new MinConflictsSolver<>(1000);
		solver.addCspListener(stepCounter);
		stepCounter.reset();
		solution = solver.solve(csp);
		if (solution.isPresent())
			//for (QueenAction action : solution.get())
			//	board = NQueensFunctions.getResult(board, action);
			System.out.println((solution.get().isSolution(csp) ? ":-) " : ":-( ") + solution.get());
		
		System.out.println(stepCounter.getResults() + "\n");
		
		NQueensBoard board2 = new NQueensBoard(size, Config.QUEEN_IN_EVERY_COL);
		System.out.println("\n Final State: \n"+ board2);
		
	    
	}
	
	private static void solveNQueensFlexibleBacktrackingSolver() {
		int size = 8;
		CSP<Variable, Integer> csp = new NQueensCSP(size);
		CspListener.StepCounter<Variable, Integer> stepCounter = new CspListener.StepCounter<>();
		CspSolver<Variable, Integer> solver;
		Optional<Assignment<Variable, Integer>> solution;
		
		System.out.println(size + "-Queens (Backtracking + MRV & DEG + LCV + AC3)");
		solver = new FlexibleBacktrackingSolver<Variable, Integer>().setAll();
		solver.addCspListener(stepCounter);
		stepCounter.reset();
		solution = solver.solve(csp);
		if (solution.isPresent())
			//for (QueenAction action : solution.get())
			//	board = NQueensFunctions.getResult(board, action);
			//System.out.println(getBoard(solution.get()));
			
			System.out.println(solution.get());
		System.out.println(stepCounter.getResults() + "\n");
		
		NQueensBoard board2 = new NQueensBoard(size, Config.QUEEN_IN_EVERY_COL);
		System.out.println("\n Final State: \n"+ board2);
		 
	}

	
	
	private static void startNQueensDemo() {
		
		solveNQueensWithSimulatedAnnealingSearch();
		solveNQueensWithHillClimbingSearch();
		solveNQueensWithGeneticAlgorithmSearch();
		solveNQueensWithRandomWalk();
	}

	
	private static void solveNQueensWithSimulatedAnnealingSearch() {
		System.out.println("\n--- NQueensDemo Simulated Annealing ---");

		Problem<NQueensBoard, QueenAction> problem =
				NQueensFunctions.createCompleteStateFormulationProblem(boardSize, Config.QUEENS_IN_FIRST_ROW);
		SimulatedAnnealingSearch<NQueensBoard, QueenAction> search =
				new SimulatedAnnealingSearch<>(NQueensFunctions::getNumberOfAttackingPairs,
						new Scheduler(20, 0.045, 100));
		Optional<List<QueenAction>> actions = search.findActions(problem);

		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
		System.out.println("Available Actions \n" + problem.getActions(problem.getInitialState()).toString());
		System.out.println("Final State:\n" + search.getLastState());
	}

	private static void solveNQueensWithHillClimbingSearch() {
		System.out.println("\n--- NQueensDemo HillClimbing ---");

		Problem<NQueensBoard, QueenAction> problem =
				NQueensFunctions.createCompleteStateFormulationProblem(boardSize, Config.QUEENS_IN_FIRST_ROW);
		HillClimbingSearch<NQueensBoard, QueenAction> search = new HillClimbingSearch<>
				(n -> -NQueensFunctions.getNumberOfAttackingPairs(n));
		Optional<List<QueenAction>> actions = search.findActions(problem);

		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
		
		System.out.println("Available Actions \n" + problem.getActions(problem.getInitialState()).toString());
		System.out.println("Final State:\n" + search.getLastState());
		
		
		
	}

	private static void solveNQueensWithGeneticAlgorithmSearch() {
		System.out.println("\n--- NQueensDemo GeneticAlgorithm ---");

		FitnessFunction<Integer> fitnessFunction = NQueensGenAlgoUtil.getFitnessFunction();
		Predicate<Individual<Integer>> goalTest = NQueensGenAlgoUtil.getGoalTest();
		// Generate an initial population
		Set<Individual<Integer>> population = new HashSet<>();
		for (int i = 0; i < 50; i++)
			population.add(NQueensGenAlgoUtil.generateRandomIndividual(boardSize));

		GeneticAlgorithm<Integer> ga = new GeneticAlgorithm<>(boardSize,
				NQueensGenAlgoUtil.getFiniteAlphabetForBoardOfSize(boardSize), 0.15);

		// Run for a set amount of time
		Individual<Integer> bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, goalTest, 1000L);
		System.out.println("Max time 1 second, Best Individual:\n"
				+ NQueensGenAlgoUtil.getBoardForIndividual(bestIndividual));
		System.out.println("Board Size      = " + boardSize);
		System.out.println("# Board Layouts = " + (new BigDecimal(boardSize)).pow(boardSize));
		System.out.println("Fitness         = " + fitnessFunction.apply(bestIndividual));
		System.out.println("Is Goal         = " + goalTest.test(bestIndividual));
		System.out.println("Population Size = " + ga.getPopulationSize());
		System.out.println("Iterations      = " + ga.getIterations());
		System.out.println("Took            = " + ga.getTimeInMilliseconds() + "ms.");

		// Run till goal is achieved
		bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, goalTest, 0L);
		System.out.println("");
		System.out.println("Max time unlimited, Best Individual:\n" +
				NQueensGenAlgoUtil.getBoardForIndividual(bestIndividual));
		System.out.println("Board Size      = " + boardSize);
		System.out.println("# Board Layouts = " + (new BigDecimal(boardSize)).pow(boardSize));
		System.out.println("Fitness         = " + fitnessFunction.apply(bestIndividual));
		System.out.println("Is Goal         = " + goalTest.test(bestIndividual));
		System.out.println("Population Size = " + ga.getPopulationSize());
		System.out.println("Itertions       = " + ga.getIterations());
		System.out.println("Took            = " + ga.getTimeInMilliseconds() + "ms.");
	}

	
	private static void solveNQueensWithRandomWalk() {
		System.out.println("\n--- NQueensDemo RandomWalk ---");
		NQueensBoard board;
		int i = 0;
		long startTime = System.currentTimeMillis();
		do {
			i++;
			board = new NQueensBoard(boardSize, Config.QUEEN_IN_EVERY_COL);
		} while (board.getNumberOfAttackingPairs() > 0);
		long stopTime = System.currentTimeMillis();
		System.out.println("Solution found after generating " + i + " random configurations ("
				+ (stopTime - startTime) + " ms).");
	}
		
	public static void main(String[] args) {
		solveNQueensMinConflictsSolver();
		solveNQueensFlexibleBacktrackingSolver();
		startNQueensDemo();
		
		Assigment2 demo = new Assigment2();
		// prog.setBoardSize(32);
		demo.addProgressTracker(demo::printProgress);
		
		
		
		System.out.println("NQueens genetic algorithm experiment (boardSize=" + demo.boardSize + ", popSize="
				+ demo.populationSize + ", mutProb=" + demo.mutationProbability + ") -->");
		demo.initExperiment(Config.EMPTY);
		demo.startGenAlgoExperiment(false);
		demo.printResult();
		
		
		
	}	
		
	

}