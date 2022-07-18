package aima.core.Assigments;

/*
 *  181805036 Seher KUMSAR
 * 181805077 Emine Ece CO�KUN�AY
 */

import aima.core.probability.bayes.*;
import aima.core.probability.bayes.impl.*;
import aima.core.probability.util.*;
import aima.core.probability.domain.*;
import aima.core.probability.bayes.model.*;
import aima.core.probability.proposition.*;

public class Assignment4 {

	public static final RandVar EXAM_LEVEL = new RandVar("EXAM_LEVEL", new BooleanDomain());
	public static final RandVar IQ_LEVEL = new RandVar("IQ_LEVEL", new BooleanDomain());
	public static final RandVar MARKS = new RandVar("MARKS", new BooleanDomain());
	public static final RandVar ADMISSION = new RandVar("ADMISSION", new BooleanDomain());
	public static final RandVar APTI_SCORE = new RandVar("APTI_SCORE", new BooleanDomain());

	public static BayesianNetwork constructAdmitUniversity() {
		
		FiniteNode examlEVEL = new FullCPTNode(EXAM_LEVEL, new double[] { 0.3, 0.7 });
		FiniteNode iqLevel = new FullCPTNode(IQ_LEVEL, new double[] { 0.2, 0.8 });
		
		@SuppressWarnings("unused")
		FiniteNode apti_score = new FullCPTNode(APTI_SCORE, new double[] {
				// IQ=true, SCORE=true
				0.6,
				// IQ=true, SCORE=false
				0.4,
				// IQ=false, SCORE=true
				0.25,
				// IQ=false, SCORE=false
				0.75 }, iqLevel);

		// return new BayesNet(examlEVEL, iqLevel);
		

		FiniteNode marks = new FullCPTNode(MARKS, new double[] {
				// EXP=true, IQ=true, MARKS=true
				0.2,
				// EXP=true, IQ=true, MARKS=false
				0.8,
				// EXP=true, IQ=false, MARKS=true
				0.1,
				// EXP=true, IQ=false, MARKS=false
				0.9,
				// EXP=false, IQ=true, MARKS=true
				0.5,
				// EXP=false, IQ=true, MARKS=false
				0.5,
				// EXP=false, IQ=false, MARKS=true
				0.4,
				// EXP=false, IQ=false, MARKS=false
				0.6 }, examlEVEL, iqLevel);
		
		
		@SuppressWarnings("unused")
		FiniteNode admission = new FullCPTNode(ADMISSION, new double[] {
				// MARKS=true, ADMISSION=true
				0.1,
				// MARKS=true, ADMISSION=false
				0.9,
				// MARKS=false, ADMISSION=true
				0.4,
				// MARKS=false, ADMISSION=false
				0.6 }, marks);

		return new BayesNet(examlEVEL, iqLevel);
	}

	public static void BayesNetModel() {

		BayesianNetwork studentNet = constructAdmitUniversity();
		
		// Construct the BayesModel from the BayesNet
		// We have not passed any inference procedure. Hence, the default inference procedure will be used.
		FiniteBayesModel model = new FiniteBayesModel(studentNet);

		AssignmentProposition EXAMLEVEL = new AssignmentProposition(EXAM_LEVEL, true);
		AssignmentProposition notEXAMLEVEL = new AssignmentProposition(EXAM_LEVEL, false);
		AssignmentProposition IQLEVEL = new AssignmentProposition(IQ_LEVEL, true);
		AssignmentProposition notIQLEVEL = new AssignmentProposition(IQ_LEVEL, false);

		AssignmentProposition marks = new AssignmentProposition(MARKS, true);
		AssignmentProposition notmarks = new AssignmentProposition(MARKS, false);
		AssignmentProposition admission = new AssignmentProposition(ADMISSION, true);
		AssignmentProposition notadmission = new AssignmentProposition(ADMISSION, false);

		AssignmentProposition APTISCORE = new AssignmentProposition(APTI_SCORE, true);
		AssignmentProposition notAPTISCORE = new AssignmentProposition(APTI_SCORE, false);

		System.out.println("The random variables in the model = " + model.getRepresentation());

		// We can calculate the prior probabilities of a variety of combinations of random variables

		System.out.println("The prior probability of having a Marks = " + model.prior(marks));
		System.out.println("The prior probability of having a Admission = " + model.prior(admission));
		System.out.println("The prior probability of having a Exam Level = " + model.prior(EXAMLEVEL));
		System.out.println("The prior probability of having a IQ Level = " + model.prior(IQLEVEL));
		System.out.println("The prior probability of having a Aptitude Score = " + model.prior(APTISCORE));
		System.out.println("---");

		// System.out.println("The probability of having a marks and admission simultaneously is = "+ model.prior(marks, admission));

		System.out.println("CASE1: ");
		System.out.println("P(a=1 | m=1) . P(m=1 | i=0, e=1) . P(i=0) . P(e=1) . P(s=0 | i=0)");
		System.out.println("P(a,m,~i,e, ~s) = " + model.prior(admission, marks, notIQLEVEL, EXAMLEVEL, notAPTISCORE));

		System.out.println("CASE2: ");
		System.out.println("P(a=0 | m=0) . P(m=0 | i=1, e=0) . P(i=1) . P(e=0) . P(s=1 | i=1)");
		System.out
				.println("P(~a,~m,i,~e, s) = " + model.prior(notadmission, notmarks, IQLEVEL, notEXAMLEVEL, APTISCORE));

	}

	public static void main(String[] args) {

		BayesNetModel();

	}

}
