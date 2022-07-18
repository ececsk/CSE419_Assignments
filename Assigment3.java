package aima.core.Assigments;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.logic.fol.StandardizeApartIndexicalFactory;
import aima.core.logic.fol.domain.DomainFactory;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.inference.FOLBCAsk;
import aima.core.logic.fol.inference.FOLFCAsk;
import aima.core.logic.fol.inference.InferenceProcedure;
import aima.core.logic.fol.inference.InferenceResult;
import aima.core.logic.fol.inference.proof.Proof;
import aima.core.logic.fol.inference.proof.ProofPrinter;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.FOLKnowledgeBaseFactory;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;

/*
Assigment-3
181805036 SEHER KUMSAR
181805077 EMİNE ECE COŞKUNÇAY
*/


public class Assigment3 {
	
	
	public static FOLDomain AllDomain() {
		FOLDomain domain = new FOLDomain();
		
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addConstant("D");
		domain.addConstant("E");
		domain.addConstant("F");
		
		domain.addConstant("george");
		domain.addConstant("john");
		domain.addConstant("robert");
		domain.addConstant("barbara");
		domain.addConstant("christine");
		domain.addConstant("yolanda");
		
		domain.addConstant("bag");
		domain.addConstant("firearm");
		domain.addConstant("gas");
		domain.addConstant("knife");
		domain.addConstant("poison");
		domain.addConstant("rope");
		
		domain.addConstant("bathroom");
		domain.addConstant("dining");
		domain.addConstant("kitchen");
		domain.addConstant("livingroom");
		domain.addConstant("pantry");
		domain.addConstant("study");
		
		// Now predicates
		domain.addPredicate("Person");
		domain.addPredicate("Man");
		domain.addPredicate("Woman");
		domain.addPredicate("Weapon");
		domain.addPredicate("Location");
		domain.addPredicate("Uniq_ppl");
		domain.addPredicate("Murderer");
		
		domain.addPredicate("Killer");
//		domain.addPredicate("Murder");

		//domain.addPredicate("((Person(A) AND Person(B) AND Person(C) AND Person(D) AND Person(E) AND Person(F) AND (A != B) AND (A != C) AND (A != D) AND (A != E) AND (A != F) AND (B != C) AND (B != D) AND (B != E) AND (B != F) AND (C != D) AND (C != E) AND (C != F) AND (D != E) AND (D != F) AND (E != F)) => Uniq_ppl(A,B,C,D,E,F))");
		domain.addPredicate("((Person(A) AND Person(B) AND Person(C) AND Person(D) AND Person(E) AND Person(F) AND (~A = B) AND (~A = C) AND (~A = D) AND (~A = E) AND (~A = F) AND (~B = C) AND (~B = D) AND (~B = E) AND (~B = F) AND (~C = D) AND (~C = E) AND (~C = F) AND (~D = E) AND (~D = F) AND (~E = F)) => Uniq_ppl(A,B,C,D,E,F))");
		
//		domain.addPredicate("Man(george)");
//		domain.addPredicate("Man(john)");
//		domain.addPredicate("Man(robert)");
//		domain.addPredicate("Woman(barbara)");
//		domain.addPredicate("Woman(christine)");
//		domain.addPredicate("Woman(yolanda)");
		domain.addPredicate("Man(X) => Person(X)");
		domain.addPredicate("Woman(X) => Person(X)");
		
//		domain.addPredicate("Weapon(bag)");
//		domain.addPredicate("Weapon(firearm)");
//		domain.addPredicate("Weapon(gas)");
//		domain.addPredicate("Weapon(knife)");
//		domain.addPredicate("Weapon(poison)");
//		domain.addPredicate("Weapon(rope)");
//		domain.addPredicate("Location(bathroom)");
//		domain.addPredicate("Location(dining)");
//		domain.addPredicate("Location(kitchen)");
//		domain.addPredicate("Location(livingroom)");
//		domain.addPredicate("Location(pantry)");
//		domain.addPredicate("Location(study)");
		
//		domain.addPredicate("Uniq_ppl(bathroom, dining, kitchen, livingroom, pantry, study, bag, firearm, gas, knife, poison, rope)");
		
		//domain.addPredicate("Muderer(X)");
		domain.addPredicate("(	(Uniq_ppl(bathroom, dining, kitchen, livingroom, pantry, study) AND Uniq_ppl(bag, firearm, gas, knife, poison, rope)) => Murderer(X))");
		domain.addPredicate("( (~Kitchen = Rope) AND (~Kitchen = Knife) AND (~Kitchen = Bag) AND (~Kitchen = Firearm) AND Man(Kitchen))");
		domain.addPredicate("(	(~barbara = Bag) AND (~george = Bag) AND (~Bag = Bathroom) AND (~Bag = Dining))");
		domain.addPredicate("(	Woman(Rope) AND (Rope = Study))");
		domain.addPredicate("(	Man(Livingroom) AND (~Livingroom = robert))");
		domain.addPredicate("(	(~Knife = Dining))");
		domain.addPredicate("(	(~yolanda = Pantry) AND (~yolanda = Study))");
		domain.addPredicate("(	Firearm = george)");
		domain.addPredicate("(	(Pantry = Gas) AND (Pantry = Gas))");
		return domain;
	}
	
	public static FOLKnowledgeBase MurderKnowledgeBase(
			InferenceProcedure infp) {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(AllDomain(),
				infp);
		
		kb.tell("Man(george)");
		kb.tell("Man(john)");
		kb.tell("Man(robert)");
		kb.tell("Woman(barbara)");
		kb.tell("Woman(christine)");
		kb.tell("Woman(yolanda)");
		
		kb.tell("Person(george)");
		kb.tell("Person(john)");
		kb.tell("Person(robert)");
		kb.tell("Person(barbara)");
		kb.tell("Person(christine)");
		kb.tell("Person(yolanda)");
		
//		kb.tell("(Man(X)) => Person(X))");
//		kb.tell("(Woman(X)) => Person(X))");
		
		kb.tell("Weapon(bag)");
		kb.tell("Weapon(firearm)");
		kb.tell("Weapon(gas)");
		kb.tell("Weapon(knife)");
		kb.tell("Weapon(poison)");
		kb.tell("Weapon(rope)");
		kb.tell("Location(bathroom)");
		kb.tell("Location(dining)");
		kb.tell("Location(kitchen)");
		kb.tell("Location(livingroom)");
		kb.tell("Location(pantry)");
		kb.tell("Location(study)");
		
//		kb.tell("Muderer(X)");
//		kb.tell("(	(Person(A) AND Person(B) AND Person(C) AND Person(D) AND Person(E) AND Person(F) AND (A != B) AND (A != C) AND (A != D) AND (A != E) AND (A != F) AND (B != C) AND (B != D) AND (B != E) AND (B != F) AND (C != D) AND (C != E) AND (C != F) AND (D != E) AND (D != F) AND (E != F)) => Uniq_ppl(A,B,C,D,E,F))");

		
//		kb.tell("(	(Uniq_ppl(bathroom, dining, kitchen, livingroom, pantry, study) AND Uniq_ppl(bag, firearm, gas, knife, poison, rope)) => Murderer(X))");
//		kb.tell("( (~kitchen = rope) AND (~kitchen = knife) AND (~kitchen = bag) AND (~kitchen = firearm) AND Man(kitchen))");
//		kb.tell("(	(~barbara = bag) AND (~george = bag) AND (~Bag = Bathroom) AND (~bag = dining))");
//		kb.tell("(	Woman(rope) AND (rope = study))");
//		kb.tell("(	Man(livingroom) AND (~livingroom = robert))");
//		kb.tell("(	(~knife = dining))");
//		kb.tell("(	(~yolanda = pantry) AND (~yolanda = study))");
//		kb.tell("(	firearm = george)");
//		kb.tell("(	(pantry = gas) AND (pantry = gas))");
		
//		kb.tell("((Person(A) AND Person(B) AND Person(C) AND Person(D) AND Person(E) AND Person(F) AND (~A = B) AND (~A = C) AND (~A = D) AND (~A = E) AND (~A = F) AND (~B = C) AND (~B = D) AND (~B = E) AND (~B = F) AND (~C = D) AND (~C = E) AND (~C = F) AND (~D = E) AND (~D = F) AND (~E = F)) => Uniq_ppl(A,B,C,D,E,F))");
		
		//kb.tell("Uniq_ppl(bathroom, dining, kitchen, livingroom, pantry, study, bag, firearm, gas, knife, poison, rope)");
		kb.tell("Uniq_ppl(bathroom)");
		kb.tell("Uniq_ppl(dining)");
		kb.tell("Uniq_ppl(kitchen)");
		kb.tell("Uniq_ppl(livingroom)");
		kb.tell("Uniq_ppl(pantry)");
		kb.tell("Uniq_ppl(study)");
		kb.tell("Uniq_ppl(bag)");
		kb.tell("Uniq_ppl(firearm)");
		kb.tell("Uniq_ppl(gas)");
		kb.tell("Uniq_ppl(knife)");
		kb.tell("Uniq_ppl(poison)");
		kb.tell("Uniq_ppl(rope)");
		
		
//		kb.tell("(	(Uniq_ppl(bathroom, dining, kitchen, livingroom, pantry, study) AND Uniq_ppl(bag, firearm, gas, knife, poison, rope)) => Murderer(X))");
//		kb.tell("((Woman(x) AND Location(x)) => Killer(x))");
//		kb.tell("Woman(barbara)");
//		kb.tell("Woman(christine)");
//		kb.tell("Woman(yolanda)");
//		kb.tell("Location(christine)");
		
		return kb;
	}
	
	public static FOLKnowledgeBase Clue1KnowledgeBase(
			InferenceProcedure infp) {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(AllDomain(),
				infp);
	//kb.tell("(	(Uniq_ppl(bathroom, dining, kitchen, livingroom, pantry, study) AND Uniq_ppl(bag, firearm, gas, knife, poison, rope)) => Murderer(X))");
	
	
	kb.tell("Man(george)");
	kb.tell("Man(john)");
	kb.tell("Man(robert)");
	kb.tell("Weapon(bag)");
	//kb.tell("Weapon(firearm)");
	kb.tell("Weapon(gas)");
	//kb.tell("Weapon(knife)");
	kb.tell("Weapon(poison)");
	//kb.tell("Weapon(rope)");
	kb.tell("((Location(kitchen) AND Man(x)) => Weapon(bag) AND Weapon(gas) AND Weapon(poison))");
	//kb.tell("((Location(kitchen) AND Man(x)) => Weapon(x))");
	//kb.tell("( (~kitchen = rope) AND (~kitchen = knife) AND (~kitchen = bag) AND (~kitchen = firearm) ))");
	//kb.tell("Man(kitchen)");
		return kb;
	}
	
	public static FOLKnowledgeBase YolandaKnowledgeBase(
			InferenceProcedure infp) {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(AllDomain(),
				infp);
		
//	kb.tell("Person(george)");
//	kb.tell("Person(john)");
//	kb.tell("Person(robert)");
//	kb.tell("Person(barbara)");
//	kb.tell("Person(christine)");
//	
//	kb.tell("Person(yolanda)");
//	
	kb.tell("Location(bathroom) AND Person(yolanda)");
	
//	kb.tell("(NOT(Location(dining))) AND Person(yolanda)");
//	kb.tell("(NOT(Location(kitchen))) AND Person(yolanda)");
//	kb.tell("(NOT(Location(livingroom))) AND Person(yolanda)");
//	kb.tell("(NOT(Location(pantry))) AND Person(yolanda)");
//	kb.tell("(NOT(Location(study))) AND Person(yolanda)");
	
//	kb.tell("Location(dining) => Person(george)");
//	kb.tell("Location(kitchen) => Person(robert)");
//	kb.tell("Location(livingroom) => Person(john)");
//	kb.tell("Location(pantry) => Person(christine)");
//	kb.tell("Location(study) => Person(barbara)");
	
	kb.tell("Weapon(knife) AND Person(yolanda)");
//	kb.tell("Weapon(gas) => Person(christine)");
//	kb.tell("Weapon(rope) => Person(barbara)");
//	kb.tell("Weapon(bag) => Person(john)");
//	kb.tell("Weapon(poison) => Person(robert)");
//	kb.tell("Weapon(firearm) => Person(geroge)");
	
//	kb.tell("(((Location(bathroom) OR Location(dining) OR Location(kitchen) OR Location(livingroom) OR Location(pantry) Location(study)) => Woman(yolanda)) ");
//	kb.tell("(((Location(dining) OR Location(kitchen) OR Location(livingroom) OR Location(pantry) ) => Woman(christine)) ");
//	kb.tell("(((Location(bathroom) OR Location(study)) => Woman(barbara)) ");
	//kb.tell("( (~kitchen = rope) AND (~kitchen = knife) AND (~kitchen = bag) AND (~kitchen = firearm) ))");
	//kb.tell("Man(kitchen)");
		return kb;
	}
	
	public static FOLKnowledgeBase GeorgeKnowledgeBase(
			InferenceProcedure infp) {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(AllDomain(),
				infp);
		
	kb.tell("Location(dining) => Person(george)");
	kb.tell("Weapon(firearm) => Person(george)");

		return kb;
	}
	
	public static FOLKnowledgeBase RobertKnowledgeBase(
			InferenceProcedure infp) {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(AllDomain(),
				infp);
		
		kb.tell("Location(kitchen) => Person(robert)");
		kb.tell("Weapon(poison) => Person(robert)");
		return kb;
	}
	public static FOLKnowledgeBase JohnKnowledgeBase(
			InferenceProcedure infp) {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(AllDomain(),
				infp);
		
		kb.tell("Location(livingroom) => Person(john)");
		kb.tell("Weapon(bag) => Person(john)");
		return kb;
	}
	public static FOLKnowledgeBase BarbaraKnowledgeBase(
			InferenceProcedure infp) {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(AllDomain(),
				infp);
		
		kb.tell("Location(study) => Person(barbara)");
		kb.tell("Weapon(rope) => Person(barbara)");
		return kb;
	}
	
	public static FOLKnowledgeBase ChristineKnowledgeBase(
			InferenceProcedure infp) {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(AllDomain(),
				infp);
		
	kb.tell("Location(pantry) => Person(christine)");
	kb.tell("Weapon(gas) => Person(christine)");
		return kb;
	}
	
	
	
	public static FOLKnowledgeBase KillerKnowledgeBase(
			InferenceProcedure infp) {
		FOLKnowledgeBase kb = new FOLKnowledgeBase(AllDomain(),
				infp);
		kb.tell("Killer(christine) => Person(X)");
		
	
//	kb.tell("Person(christine) => Location(pantry)");
//	kb.tell("Person(christine) => Weapon(gas)");
	
		return kb;
	}
	private static void Killer(InferenceProcedure ip) {
		StandardizeApartIndexicalFactory.flush();
		FOLKnowledgeBase kb = KillerKnowledgeBase(ip);
		String kbStr = kb.toString();
		
		List<Term> variable = new ArrayList<Term>();
		variable.add(new Variable("X"));
		
		Predicate query = new Predicate("Killer", variable);
		InferenceResult answer = kb.ask(query);
		System.out.println("Killer Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query);
		
		for (Proof p : answer.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
		System.out.println("Killer is: " + answer.getProofs());
	}
	
	private static void Man(InferenceProcedure ip) {
		StandardizeApartIndexicalFactory.flush();

		FOLKnowledgeBase kb = MurderKnowledgeBase(ip);

		String kbStr = kb.toString();
		
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("X"));
		
		Predicate query = new Predicate("Man", terms);
		
		InferenceResult answer = kb.ask(query);
		System.out.println("Man Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query);
		
		for (Proof p : answer.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
		//System.out.println("MAN ARE: " + answer);
	}
	
	private static void Woman(InferenceProcedure ip) {
		StandardizeApartIndexicalFactory.flush();

		FOLKnowledgeBase kb = MurderKnowledgeBase(ip);

		String kbStr = kb.toString();
		
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("X"));
		
		Predicate query = new Predicate("Woman", terms);
		
		InferenceResult answer = kb.ask(query);
		System.out.println("Woman Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query);
		
		for (Proof p : answer.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
		//System.out.println("WOMAN ARE: " + answer);
	}
	
	private static void Person(InferenceProcedure ip) {
		StandardizeApartIndexicalFactory.flush();

		FOLKnowledgeBase kb = MurderKnowledgeBase(ip);

		String kbStr = kb.toString();
		
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("X"));
		
		Predicate query = new Predicate("Person", terms);
		
		InferenceResult answer = kb.ask(query);
		System.out.println("Person Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query);
		
		for (Proof p : answer.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
	}
	
	private static void Uniq_ppl(InferenceProcedure ip) {
		StandardizeApartIndexicalFactory.flush();

		FOLKnowledgeBase kb = MurderKnowledgeBase(ip);

		String kbStr = kb.toString();
		
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("X"));
		//terms.add(new Variable("Z"));
		
		Predicate query = new Predicate("Uniq_ppl", terms);
		
		InferenceResult answer = kb.ask(query);
		System.out.println("Uniq_ppl Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query);
		
		for (Proof p : answer.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
	}
	
	
	private static void Clue1(InferenceProcedure ip) {
		StandardizeApartIndexicalFactory.flush();

		FOLKnowledgeBase kb = Clue1KnowledgeBase(ip);

		String kbStr = kb.toString();
		
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("X"));
		//terms.add(new Variable("Z"));
		
		Predicate query = new Predicate("Weapon", terms);
		
		InferenceResult answer = kb.ask(query);
		System.out.println("Clue1 Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query);
		
		for (Proof p : answer.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
		System.out.println("Weapon ARE: " + answer.getProofs());
	}
	
	
	private static void Yolanda(InferenceProcedure ip) {
		StandardizeApartIndexicalFactory.flush();

		FOLKnowledgeBase kb = YolandaKnowledgeBase(ip);

		String kbStr = kb.toString();
		
		List<Term> variable = new ArrayList<Term>();
		variable.add(new Variable("yolanda"));
		
		Predicate query = new Predicate("Location", variable);
		
		InferenceResult answer = kb.ask(query);
		System.out.println("Yolanda Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query);
		
		for (Proof p : answer.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
		System.out.println("Location is: " + answer.getProofs());
		
		
		Predicate query2 = new Predicate("Weapon", variable);
		InferenceResult answer2 = kb.ask(query2);
		System.out.println("Yolanda Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query2);
		
		for (Proof p : answer2.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
		System.out.println("Weapon is: " + answer2.getProofs());
		
	}
	private static void George(InferenceProcedure ip) {
		StandardizeApartIndexicalFactory.flush();
		FOLKnowledgeBase kb = GeorgeKnowledgeBase(ip);
		String kbStr = kb.toString();
		
		List<Term> variable = new ArrayList<Term>();
		variable.add(new Variable("george"));
		
		Predicate query = new Predicate("Location", variable);
		InferenceResult answer = kb.ask(query);
		System.out.println("George Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query);
		
		for (Proof p : answer.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
		System.out.println("Location is: " + answer.getProofs());
		
		
		Predicate query2 = new Predicate("Weapon", variable);
		InferenceResult answer2 = kb.ask(query2);
		System.out.println("George Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query2);
		
		for (Proof p : answer2.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
		System.out.println("Weapon is: " + answer2.getProofs());
		
	}
	private static void Robert(InferenceProcedure ip) {
		StandardizeApartIndexicalFactory.flush();
		FOLKnowledgeBase kb = RobertKnowledgeBase(ip);
		String kbStr = kb.toString();
		
		List<Term> variable = new ArrayList<Term>();
		variable.add(new Variable("robert"));
		
		Predicate query = new Predicate("Location", variable);
		InferenceResult answer = kb.ask(query);
		System.out.println("Robert Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query);
		
		for (Proof p : answer.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
		System.out.println("Location is: " + answer.getProofs());
		
		
		Predicate query2 = new Predicate("Weapon", variable);
		InferenceResult answer2 = kb.ask(query2);
		System.out.println("Robert Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query2);
		
		for (Proof p : answer2.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
		System.out.println("Weapon is: " + answer2.getProofs());
		
	}
	private static void John(InferenceProcedure ip) {
		StandardizeApartIndexicalFactory.flush();
		FOLKnowledgeBase kb = JohnKnowledgeBase(ip);
		String kbStr = kb.toString();
		
		List<Term> variable = new ArrayList<Term>();
		variable.add(new Variable("john"));
		
		Predicate query = new Predicate("Location", variable);
		InferenceResult answer = kb.ask(query);
		System.out.println("John Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query);
		
		for (Proof p : answer.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
		System.out.println("Location is: " + answer.getProofs());
		
		
		Predicate query2 = new Predicate("Weapon", variable);
		InferenceResult answer2 = kb.ask(query2);
		System.out.println("John Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query2);
		
		for (Proof p : answer2.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
		System.out.println("Weapon is: " + answer2.getProofs());
		
	}
	private static void Barbara(InferenceProcedure ip) {
		StandardizeApartIndexicalFactory.flush();
		FOLKnowledgeBase kb = BarbaraKnowledgeBase(ip);
		String kbStr = kb.toString();
		
		List<Term> variable = new ArrayList<Term>();
		variable.add(new Variable("barbara"));
		
		Predicate query = new Predicate("Location", variable);
		InferenceResult answer = kb.ask(query);
		System.out.println("Barbara Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query);
		
		for (Proof p : answer.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
		System.out.println("Location is: " + answer.getProofs());
		
		
		Predicate query2 = new Predicate("Weapon", variable);
		InferenceResult answer2 = kb.ask(query2);
		System.out.println("Barbara Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query2);
		
		for (Proof p : answer2.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
		System.out.println("Weapon is: " + answer2.getProofs());
		
	}
	
	
	private static void Christine(InferenceProcedure ip) {
		StandardizeApartIndexicalFactory.flush();
		FOLKnowledgeBase kb = ChristineKnowledgeBase(ip);
		String kbStr = kb.toString();
		
		List<Term> variable = new ArrayList<Term>();
		variable.add(new Variable("christine"));
		
		Predicate query = new Predicate("Location", variable);
		InferenceResult answer = kb.ask(query);
		System.out.println("Christine Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query);
		
		for (Proof p : answer.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
		System.out.println("Location is: " + answer.getProofs());
		
		
		Predicate query2 = new Predicate("Weapon", variable);
		InferenceResult answer2 = kb.ask(query2);
		System.out.println("Christine Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query2);
		
		for (Proof p : answer2.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
		System.out.println("Weapon is: " + answer2.getProofs());
		
	}
	
	
	private static void fOL_fcAskDemo() {
		System.out.println("---------------------------");
		System.out.println("Forward Chain, Man");
		System.out.println("---------------------------");
		Man(new FOLFCAsk());
		System.out.println("---------------------------");
		System.out.println("Forward Chain, Woman");
		System.out.println("---------------------------");
		Woman(new FOLFCAsk());
		System.out.println("---------------------------");
		System.out.println("Forward Chain, Person");
		System.out.println("---------------------------");
		Person(new FOLFCAsk());
		System.out.println("---------------------------");
		System.out.println("Forward Chain, Uniq_ppl");
		System.out.println("---------------------------");
		Uniq_ppl(new FOLFCAsk());
		
		System.out.println("---------------------------");
		System.out.println("Forward Chain, Clue1");
		System.out.println("---------------------------");
		Clue1(new FOLFCAsk());
		
		System.out.println("---------------------------");
		System.out.println("Forward Chain, Yolanda");
		System.out.println("---------------------------");
		Yolanda(new FOLFCAsk());
		
		System.out.println("---------------------------");
		System.out.println("Forward Chain, George");
		System.out.println("---------------------------");
		George(new FOLFCAsk());
		
		System.out.println("---------------------------");
		System.out.println("Forward Chain, George");
		System.out.println("---------------------------");
		Robert(new FOLFCAsk());
		
		System.out.println("---------------------------");
		System.out.println("Forward Chain, George");
		System.out.println("---------------------------");
		John(new FOLFCAsk());
		
		System.out.println("---------------------------");
		System.out.println("Forward Chain, George");
		System.out.println("---------------------------");
		Barbara(new FOLFCAsk());
		
		
		System.out.println("---------------------------");
		System.out.println("Forward Chain, Christine");
		System.out.println("---------------------------");
		Christine(new FOLFCAsk());
		
		System.out.println("---------------------------");
		System.out.println("Forward Chain, Killer");
		System.out.println("---------------------------");
		Killer(new FOLFCAsk());
	}

	private static void fOL_bcAskDemo() {
		
		System.out.println("---------------------------");
		System.out.println("Forward Chain, Man");
		System.out.println("---------------------------");
		Man(new FOLBCAsk());
		System.out.println("---------------------------");
		System.out.println("Forward Chain, Woman");
		System.out.println("---------------------------");
		Woman(new FOLBCAsk());
		System.out.println("---------------------------");
		System.out.println("Forward Chain, Person");
		System.out.println("---------------------------");
		Person(new FOLBCAsk());
		System.out.println("---------------------------");
		System.out.println("Forward Chain, Uniq_ppl");
		System.out.println("---------------------------");
		Uniq_ppl(new FOLBCAsk());
		
		System.out.println("---------------------------");
		System.out.println("Forward Chain, Clue1");
		System.out.println("---------------------------");
		Clue1(new FOLBCAsk());
		
		System.out.println("---------------------------");
		System.out.println("Forward Chain, Yolanda");
		System.out.println("---------------------------");
		Yolanda(new FOLBCAsk());
		
		System.out.println("---------------------------");
		System.out.println("Forward Chain, George");
		System.out.println("---------------------------");
		George(new FOLBCAsk());
		
		System.out.println("---------------------------");
		System.out.println("Forward Chain, George");
		System.out.println("---------------------------");
		Robert(new FOLBCAsk());
		
		System.out.println("---------------------------");
		System.out.println("Forward Chain, George");
		System.out.println("---------------------------");
		John(new FOLBCAsk());
		
		System.out.println("---------------------------");
		System.out.println("Forward Chain, George");
		System.out.println("---------------------------");
		Barbara(new FOLBCAsk());
		
		
		System.out.println("---------------------------");
		System.out.println("Forward Chain, Christine");
		System.out.println("---------------------------");
		Christine(new FOLBCAsk());
		
		System.out.println("---------------------------");
		System.out.println("Forward Chain, Killer");
		System.out.println("---------------------------");
		Killer(new FOLBCAsk());
		
	}
	
	private FOLKnowledgeBase kbs;

	@Before
	public void setUp() {
		StandardizeApartIndexicalFactory.flush();

		
		kbs = new FOLKnowledgeBase(AllDomain());
	}
	
	
	@Test
	public void testFactNotAddedWhenAlreadyPresent() {
		kbs.tell("(Killer(x) => Person(christine))");
		Assert.assertEquals(1, kbs.getNumberRules());
	}
	
	public static void main(String[] args) {
		
		
		fOL_fcAskDemo();
		fOL_bcAskDemo();
		
	}
	
	
		
	
}

