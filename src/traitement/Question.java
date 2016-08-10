package traitement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.plaf.synth.SynthSpinnerUI;


public class Question {
	public String name;
	public List<Condition> conditions;
	public String type;
	public List<Reponse> reponses;
	int questionNumber;
	double naValue;
	boolean isAnswer;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Condition> getCondition() {
		return conditions;
	}

	public void setCondition(List<Condition> condition) {
		this.conditions = condition;
	}

	public void setNa() {
		for (int i = 0; i < conditions.size(); i++) {
			if (conditions.get(i).isNa) {
				naValue = conditions.get(i).eq;
			}
		}
	}
	/**
	 * fonction servant a traiter une condition particulière
	 * @param qRet
	 * @param c
	 * @param answers
	 * @param loopPart
	 * @return qRet
	 */
	public QuestionReturn gestionNbrChecked(QuestionReturn qRet,Condition c , List<Reponse> answers,String loopPart){
		if(c.type[0]!=10){
			int nbrCheck = 0;
			for(int i = 0 ; i < answers.size();i++){
				if(loopPart.isEmpty()){
					if(answers.get(i).reponseNumeric==1){
						nbrCheck++;
					}
				} else if (answers.get(i).questionTag.split("\\.")[1].equals(loopPart)){
					if(answers.get(i).reponseNumeric==1){
						nbrCheck++;
					}
				}
			}
			if(c.infSup && !c.bEq){
				if(nbrCheck<c.checked){
					qRet.validate=false;
				}
			}else if(!c.infSup && !c.bEq){
				if(nbrCheck>c.checked){
					qRet.validate=false;
				}
			}else if(c.bEq) {
				if(nbrCheck==c.checked){
					qRet.validate=false;
				}
			}
			if(!qRet.validate && !c.withBraket){
				for(int i = 0 ;i<answers.size();i++){
					if(loopPart.isEmpty()){
						answers.get(i).disqualif=true;
						if(c.isAer){
							answers.get(i).isAerDisq=true;
						}
					} else if (answers.get(i).questionTag.split("\\.")[1].equals(loopPart)){
						answers.get(i).disqualif=true;
						if(c.isAer){
							answers.get(i).isAerDisq=true;
						}
					}
				}
			}
		} else {
			boolean isValidate =false;
			for(int i = 0 ; i < answers.size();i++){
				for(int j=0 ; j < c.uncheck.length;j++){
					if(loopPart.isEmpty()){
						if(answers.get(i).reponseNumeric ==1){
							String temp = answers.get(i).questionTag.split("_")[1];
							if(answers.get(i).isPartOfLoop()){
								temp = temp.split("\\.")[0];
							}
							if(c.uncheck[j] == Integer.valueOf(temp)){
								isValidate= true;
							}
						}
					} else {
						if(answers.get(i).questionTag.split("\\.")[1].equals(loopPart)) {
							if(answers.get(i).reponseNumeric ==1){
								String temp = answers.get(i).questionTag.split("_")[1];
								if(answers.get(i).isPartOfLoop()){
									temp = temp.split("\\.")[0];
								}
								if(c.uncheck[j] == Integer.valueOf(temp)){
									isValidate= true;
								}
							}
						}
					}
				}
			}
			if(!isValidate){
				qRet.validate=false;
				if(!c.withBraket){
					for(int i = 0 ; i < answers.size();i++){
						if(loopPart.isEmpty()){
							qRet = gestionDisqualif(qRet, c, answers.get(i));
						} else {
							if(answers.get(i).questionTag.split("\\.")[1].equals(loopPart)){
								qRet = gestionDisqualif(qRet, c, answers.get(i));
							}
						}
					}
				}
			}
		}
		return qRet;
	}
   /**
    * fonction servant au traitement d'une condition particulière
    * @param qRet
    * @param andc
    * @param answers
    * @param loopPart
    * @return qRet
    */	
	public QuestionReturn gestionNbrCheckedAndOr(QuestionReturn qRet,AndCondition andc , List<Reponse> answers,String loopPart){
		Condition c =andc.secondPart;
		int nbrCheck = 0;
		for(int i = 0 ; i < answers.size();i++){
			if(loopPart.isEmpty()){
				if(answers.get(i).reponseNumeric==1){
					nbrCheck++;
				}
			} else if (answers.get(i).questionTag.split("\\.")[1].equals(loopPart)){
				if(answers.get(i).reponseNumeric==1){
					nbrCheck++;
				}
			}
		}
		if(c.infSup && !c.bEq){
			if(nbrCheck<c.checked){
				qRet.validate=false;
			}
		}else if(!c.infSup && !c.bEq){
			if(nbrCheck>c.checked){
				qRet.validate=false;
			}
		}else if(c.bEq) {
			if(nbrCheck==c.checked){
				qRet.validate=false;
			}
		}
		if(!qRet.validate){
			for(int i = 0 ;i<answers.size();i++){
				if(loopPart.isEmpty()){
					gestionDisqualifAndOr(answers.get(i), qRet, andc);
				} else if (answers.get(i).questionTag.split("\\.")[1].equals(loopPart)){
					gestionDisqualifAndOr(answers.get(i), qRet, andc);
				}
			}
		}
		return qRet;
	}
	/**
	 * fonction de disqualification des conditions simples
	 * @param qRet
	 * @param c
	 * @param answer
	 * @return qRet
	 */
	private QuestionReturn gestionDisqualif(QuestionReturn qRet, Condition c, Reponse answer) {
		if(c.withBraket){
			//System.outprintln("passage braket no and or");
		}
		if (c.skip && !c.doubleSkip && c.associateCondition.isEmpty()) {
			qRet.gotSkipTo = true;
			
			qRet.questionSkip = c.questionSkip;
			qRet.validate = true;
			qRet.questionSkip = qRet.questionSkip.replaceAll(" ", "");
			qRet.setQuestionNumber();
			if (answer.partOfLoop) {
				qRet.loopPart.add(new SkipCondition(answer.questionName, answer.questionTag.split("\\.")[1]));
			}
		} else if (c.skip && c.doubleSkip && c.associateCondition.isEmpty()) {
			//qRet.doubleSkip = true;
			qRet.skipTo.add(new SkipCondition(c.questionSkip, c.questionSkipTo, true));
			/*qRet.beginSkip = c.questionSkip;
			qRet.endSkip = c.questionSkipTo;*/
			qRet.validate = true;
			//qRet.gotSkipTo = true;
			if (answer.partOfLoop) {
				qRet.loopPart
						.add(new SkipCondition(c.questionSkip, c.questionSkipTo, answer.questionTag.split("\\.")[1]));

			}
		} else if (c.multiple && c.associateCondition.isEmpty()) {
			if (answer.questionTag.contains(".")) {
				qRet.conditions.add(new MultipleCondition(c.questionSkip, answer.questionTag.split("\\.")[1]));
			} else {
				qRet.conditions.add(new MultipleCondition(c.questionSkip));
			}
		} else if (!c.associateCondition.isEmpty()) {	
			if (c.andorOr) {
				qRet.andConditions.add(new AndCondition(answer, false, c.associateCondition, true));
			} else {
				qRet.andConditions.add(new AndCondition(answer, false, c.associateCondition, false));
			}
			if(!c.withBraket){
			answer.disqualif = true;
				if(c.isAer){
					answer.isAerDisq=true;
				}
			}
		} else {
			qRet.validate = false;
			if(!c.withBraket){
				answer.disqualif = true;
				if(c.isAer){
					answer.isAerDisq=true;
				}
			}
		}
		if(!c.withBraket){
			if(c.questionValue){
				for(int i = 0 ; i < c.valueCondition.size();i++){
					c.valueCondition.get(i).isValueDisqu=true;
				}
			}
		}
		return qRet;
	}

	public Question(String name) {
		this.type = null;
		this.name = name;
		this.conditions = new ArrayList<Condition>();
		this.reponses = new ArrayList<Reponse>();
		if (this.name != "") {
			String temp = name.split("@")[0];
			temp = temp.replaceAll(". ", "");
			temp = temp.replaceAll("[^\\d.]", "");
			if (!temp.isEmpty()) {
				questionNumber = Integer.valueOf(temp);
			}
			this.name = name.split("@")[0];
			naValue = -999999;
		}
	}
	/**
	 * fonction de gestion des checkbox
	 * @param option
	 * @param answers
	 * @param c
	 * @return qRet
	 */
	public QuestionReturn gestionCheckBox(QuestionReturn option, List<Reponse> answers, Condition c) {
		for (int i = 0; i < answers.size(); i++) {
			for (int k = 0; k < c.checkbox.length; k++) {
				int testNum = 0;
				if (answers.get(i).reponseNumeric == 1) {
					String temp = answers.get(i).questionTag;
					temp = temp.split("_")[temp.split("_").length - 1];
					if (temp.contains(".")) {
						temp = temp.split("\\.")[0];
					}
					temp = temp.replaceAll("[^\\d.]", "");
					testNum = Integer.valueOf(temp);
				}
				if (testNum == c.checkbox[k] && !answers.get(i).shouldBeEmpty && !answers.get(i).isEmpty) {
					option.validate = false;
				}
			}
		}
		return option;
	}

/**
 * fonction de traitement d'une condition particulière
 * @param option
 * @param answers
 * @param andC
 * @return qRet
 */
	public QuestionReturn gestionAndOrCheckBox(QuestionReturn option, List<Reponse> answers, AndCondition andC) {
		QuestionReturn qRet = new QuestionReturn();
		qRet.validate = true;
		for (int i = 0; i < answers.size(); i++) {
			Reponse answer = answers.get(i);
			if (!andC.firstPart) {
				if (andC.loopPart.isEmpty()) {
					if (andC.andOr) {
						if(!andC.secondPart.withBraket){
							answer.disqualif = true;
						}
						qRet.validate = false;
						if (andC.previous != null &&(!andC.secondPart.withBraket)) {
							andC.previous.firstAnswer.disqualif = true;
						}

					} else {
						qRet = gestionConditionAndOr(qRet, answer, andC);
					}

				} else if (answer.questionTag.contains(".")) {
					if (andC.andOr) {
						if(!andC.secondPart.withBraket){					
							answer.disqualif = true;
						}
						qRet.validate = false;
						if (andC.previous != null && !andC.secondPart.withBraket) {
							andC.previous.firstAnswer.disqualif = true;
						}
					} else {
						if (andC.loopPart.equals(answer.questionTag.split("\\.")[1])) {
							qRet = gestionConditionAndOr(qRet, answer, andC);
						}
					}
				}
			} else {
				if (andC.loopPart.isEmpty()) {
					qRet = gestionConditionAndOr(qRet, answer, andC);
				} else if (answer.questionTag.contains(".")) {
					if (answer.questionTag.split("\\.")[1].equals(andC.loopPart)
							&& !answer.questionTag.contains("other") && !answer.questionTag.contains("NA")) {
						qRet = gestionConditionAndOr(qRet, answer, andC);
					}
				}
			}

		}
		return qRet;
	}
	/**
	 * fonction servant a disqualifier et/ou rediriger vers le traitement adéquat si nécessaire
	 * @param option
	 * @param answer
	 * @param andC
	 * @return qRet
	 */
	public QuestionReturn gestionAndOr(QuestionReturn option, Reponse answer, AndCondition andC) {
		QuestionReturn qRet = new QuestionReturn();
		if(andC.secondPart.tag==null){
			if (!andC.firstPart) {
				if (andC.loopPart.isEmpty()) {
					if (andC.andOr) {
						if(!andC.secondPart.withBraket){
							answer.disqualif = true;
						}
						qRet.validate = false;
						if (andC.previous != null && !andC.secondPart.withBraket) {
							andC.previous.firstAnswer.disqualif = true;
						}
	
					} else {
						qRet = gestionConditionAndOr(qRet, answer, andC);
					}
	
				} else if (answer.questionTag.contains(".")) {
					if (andC.andOr) {
						if(!andC.secondPart.withBraket){
							answer.disqualif = true;
						}
						qRet.validate = false;
						if (andC.previous != null && !andC.secondPart.withBraket) {
							andC.previous.firstAnswer.disqualif = true;
						}
					} else {
						if (andC.loopPart.equals(answer.questionTag.split("\\.")[1])) {
							qRet = gestionConditionAndOr(qRet, answer, andC);
						}
					}
				}
			} else {
				if (andC.loopPart.isEmpty()) {
					qRet.validate = true;
					qRet = gestionConditionAndOr(qRet, answer, andC);
	
				} else if (answer.questionTag.contains(".")) {
					if (answer.questionTag.split("\\.")[1].equals(andC.loopPart) && !answer.questionTag.contains("other")
							&& !answer.questionTag.contains("NA")) {
						qRet = gestionConditionAndOr(qRet, answer, andC);
					}
				}
			}
		} else if ( answer.questionTag.contains(andC.secondPart.tag)){
			if (!andC.firstPart) {
				if (andC.loopPart.isEmpty()) {
					if (andC.andOr) {
						if(!andC.secondPart.withBraket){
							answer.disqualif = true;
						}
						qRet.validate = false;
						if (andC.previous != null && !andC.secondPart.withBraket) {
							andC.previous.firstAnswer.disqualif = true;
						}
	
					} else {
						qRet = gestionConditionAndOr(qRet, answer, andC);
					}
	
				} else if (answer.questionTag.contains(".")) {
					if (andC.andOr) {
						if(!andC.secondPart.withBraket){
							answer.disqualif = true;
						}
						qRet.validate = false;
						if (andC.previous != null && !andC.secondPart.withBraket) {
							andC.previous.firstAnswer.disqualif = true;
						}
					} else {
						if (andC.loopPart.equals(answer.questionTag.split("\\.")[1])) {
							qRet = gestionConditionAndOr(qRet, answer, andC);
						}
					}
				}
			} else {
				if (andC.loopPart.isEmpty()) {
					qRet.validate = true;
					qRet = gestionConditionAndOr(qRet, answer, andC);
	
				} else if (answer.questionTag.contains(".")) {
					if (answer.questionTag.split("\\.")[1].equals(andC.loopPart) && !answer.questionTag.contains("other")
							&& !answer.questionTag.contains("NA")) {
						qRet = gestionConditionAndOr(qRet, answer, andC);
					}
				}
			}
		}

		return qRet;
	}

	/**
	 * fonction qui se charge de la disqualification d'une réponse 
	 * @param answer
	 * @param qRet
	 * @param andC
	 */
	private void gestionDisqualifAndOr(Reponse answer,QuestionReturn qRet,AndCondition andC){
		Condition c = andC.secondPart;

		if (andC.andOr) {
			if(!c.withBraket){
				answer.disqualif = true;
				if(c.isAer){
					answer.isAerDisq=true;
				}
			}
			qRet.validate = false;
			if(!c.withBraket){
				andC.firstAnswer.disqualif = true;
				if(c.isAer){
					andC.firstAnswer.isAerDisq = true;
				}
			}
			if (!c.associateCondition.isEmpty()) {
				if (c.andorOr) {
					qRet.andConditions.add(new AndCondition(answer, false, c.associateCondition, andC, true));
				} else {
					qRet.andConditions.add(new AndCondition(answer, false, c.associateCondition, andC, false));
				}
			}
			if (andC.previous != null && !c.withBraket) {
				andC.previous.firstAnswer.disqualif = true;
			}
		} else {
			if (andC.firstPart) {
				if (!c.associateCondition.isEmpty()) {
					if (c.andorOr) {
						qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, andC, true));
					} else {
						qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, andC, false));
					}
				}
			} else {
				if(!c.withBraket){
					answer.disqualif = true;
					if(c.isAer){
						answer.isAerDisq=true;
					}
				}
				qRet.validate = false;
				if (!c.associateCondition.isEmpty()) {
					if (c.andorOr) {
						qRet.andConditions.add(new AndCondition(answer, false, c.associateCondition, andC, true));
					} else {
						qRet.andConditions.add(new AndCondition(answer, false, c.associateCondition, andC, false));
					}
				}
			}
		}
	}
	/**
	 * fonction de validation d'une condition brute (ex EQ2) dans le cas d'une condition multiple
	 * @param option
	 * @param answer
	 * @param andC
	 * @return qRet
	 */
	
	public QuestionReturn gestionConditionAndOr(QuestionReturn option, Reponse answer, AndCondition andC) {

		
		QuestionReturn qRet = option;
		Condition c = andC.secondPart;
		for (int h = 0; h < c.type.length; h++) {
			if (c.type[h] == 0) {
				if (answer.reponseNumeric > (double) c.sup && answer.reponseNumeric != naValue && !answer.shouldBeEmpty
						&& !answer.isEmpty) {
					gestionDisqualifAndOr(answer, qRet, andC);
				}
				if (answer.reponseNumeric <= (double) c.sup && answer.reponseNumeric != naValue && !answer.shouldBeEmpty
						&& !answer.isEmpty) {
					if (!c.associateCondition.isEmpty()) {
						if (c.andorOr) {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, andC, true));
						} else {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, andC, false));
						}
					}
				}
				if (!answer.disqualif && !c.withBraket) {
					if (!andC.andOr) {
						andC.firstAnswer.disqualif = false;
						if (andC.previous != null) {
							if (!andC.previous.andOr) {
								andC.previous.firstAnswer.disqualif = false;
							}
						}
					}
				}
			}
			if (c.type[h] == 1) {
				if (answer.reponseNumeric < (double) c.inf && answer.reponseNumeric != naValue && !answer.shouldBeEmpty
						&& !answer.isEmpty) {
					gestionDisqualifAndOr(answer, qRet, andC);
				}
				if (answer.reponseNumeric >= (double) c.inf && answer.reponseNumeric != naValue && !answer.shouldBeEmpty
						&& !answer.isEmpty) {
					if (!c.associateCondition.isEmpty()) {
						if (c.andorOr) {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, andC, true));
						} else {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, andC, false));
						}
					}
				}
				if (!answer.disqualif && !c.withBraket) {
					if (!andC.andOr) {
						andC.firstAnswer.disqualif = false;
						if (andC.previous != null) {
							if (!andC.previous.andOr) {
								andC.previous.firstAnswer.disqualif = false;
							}
						}
					}
				}
			}
			if (c.type[h] == 2) {
				if (answer.reponseNumeric== (double) c.eq && answer.reponseNumeric != naValue && !answer.shouldBeEmpty
						&& !answer.isEmpty && !c.multiple && !c.skip && !c.doubleSkip) {
					gestionDisqualifAndOr(answer, qRet, andC);
				}
				if (answer.reponseNumeric != (double) c.eq && answer.reponseNumeric != naValue && !answer.shouldBeEmpty
						&& !answer.isEmpty && !c.multiple && !c.skip && !c.doubleSkip) {
					if (!c.associateCondition.isEmpty()) {
						if (c.andorOr) {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, andC, true));
						} else {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, andC, false));
						}
					}
				}
				if (!answer.disqualif&& !c.withBraket) {
					if (!andC.andOr) {
						andC.firstAnswer.disqualif = false;
						if (andC.previous != null) {
							if (!andC.previous.andOr) {
								andC.previous.firstAnswer.disqualif = false;
							}
						}
					}
				}
			}
			if (c.type[h] == 3) {
				if (answer.reponseNumeric != (double) c.neq && answer.reponseNumeric != naValue && !answer.shouldBeEmpty
						&& !answer.isEmpty && !c.multiple && !c.skip && !c.doubleSkip) {
					gestionDisqualifAndOr(answer, qRet, andC);
				}
				if (answer.reponseNumeric != (double) c.neq && answer.reponseNumeric != naValue && !answer.shouldBeEmpty
						&& !answer.isEmpty && !c.multiple && !c.skip && !c.doubleSkip) {
					if (!c.associateCondition.isEmpty()) {
						if (c.andorOr) {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, andC, true));
						} else {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, andC, false));
						}
					}
				}
				if (!answer.disqualif &&!c.withBraket) {
					if (!andC.andOr) {
						andC.firstAnswer.disqualif = false;
						if (andC.previous != null) {
							if (!andC.previous.andOr) {
								andC.previous.firstAnswer.disqualif = false;
							}
						}
					}
				}
			}
			if (c.type[h] == 4) {
				for (int k = 0; k < c.checkbox.length; k++) {
					if (answer.reponseNumeric == c.checkbox[k] && !answer.shouldBeEmpty && !answer.isEmpty) {
						gestionDisqualifAndOr(answer, qRet, andC);
						break;
					}
				}
				if (!answer.disqualif&& !c.withBraket) {
					if (!c.associateCondition.isEmpty()) {
						if (c.andorOr) {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, andC, true));
						} else {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, andC, false));
						}
					}
					if (!andC.andOr && andC.previous != null) {
						andC.previous.firstAnswer.disqualif = false;
					}
				}
			}
			if (c.type[h] == 5) {
				if ((answer.reponseNumeric > (double) c.max || answer.reponseNumeric < (double) c.min)
						&& answer.reponseNumeric != naValue && !answer.shouldBeEmpty && !answer.isEmpty) {
					gestionDisqualifAndOr(answer, qRet, andC);
				}
				if ((answer.reponseNumeric <= (double) c.max || answer.reponseNumeric >= (double) c.min)
						&& answer.reponseNumeric != naValue && !answer.shouldBeEmpty && !answer.isEmpty) {
					if (!c.associateCondition.isEmpty()) {
						if (c.andorOr) {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, andC, true));
						} else {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, andC, false));
						}
					}
				}
				if (!answer.disqualif &&!c.withBraket) {
					if (!andC.andOr) {
						andC.firstAnswer.disqualif = false;
						if (andC.previous != null) {
							if (!andC.previous.andOr) {
								andC.previous.firstAnswer.disqualif = false;
							}
						}
					}
				}
			}
		
			if (c.type[h] == 7) {
				qRet.isConstSum = true;
				if (answer.questionTag.contains(".")) {
					qRet.isLoop = true;
					if (qRet.loopNumber == "") {
						qRet.loopNumber = answer.questionTag.split("\\.")[1];
						qRet.sum = 0;
						if (answer.reponseNumeric != naValue && !answer.questionTag.contains("NA")) {
							qRet.sum += answer.reponseNumeric;
						}
						if (!answer.questionTag.contains("NA")) {
							qRet.questionTagSum.add(answer.questionTag);
						}
					} else if (qRet.loopNumber.equals(answer.questionTag.split("\\.")[1])) {
						if (answer.reponseNumeric != naValue && !answer.questionTag.contains("NA")) {
							qRet.sum += answer.reponseNumeric;
						}
						if (!answer.questionTag.contains("NA")) {
							qRet.questionTagSum.add(answer.questionTag);
						}
					} else {
						if (qRet.sum != c.constSumRes) {
							answer.disqualif = true;
							if(c.isAer){
								answer.isAerDisq=true;
							}
							andC.firstAnswer.disqualif = true;
							if(c.isAer){
								andC.firstAnswer.isAerDisq=true;
							}

						}
						qRet.questionTagSum.clear();
						qRet.sum = 0;
						qRet.loopNumber = answer.questionTag.split("\\.")[1];
						if (answer.reponseNumeric != naValue) {
							qRet.sum += answer.reponseNumeric;
						}
					}
				} else {
					if (answer.reponseNumeric != naValue && !answer.questionTag.contains("NA")) {
						qRet.sum += answer.reponseNumeric;
					}
				}
			}
			if (c.type[h] == 8) {
				for (int k = 0; k < c.checkbox.length; k++) {
					int testNum = 0;
					if (answer.reponseNumeric == 1) {
						String temp = answer.questionTag;
						temp = temp.split("_")[temp.split("_").length - 1];
						if (temp.contains(".")) {
							temp = temp.split("\\.")[0];
						}
						temp = temp.replaceAll("[^\\d.]", "");
						testNum = Integer.valueOf(temp);
					}
					if (testNum == c.checkbox[k] && !answer.shouldBeEmpty && !answer.isEmpty) {
						gestionDisqualifAndOr(answer, qRet, andC);
					}
				}
				if (!answer.disqualif && !c.withBraket) {
					if (!c.associateCondition.isEmpty()) {
						if (c.andorOr) {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, andC, true));
						} else {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, andC, false));
						}
					}
					if (!andC.andOr) {
						andC.firstAnswer.disqualif = false;
						if (andC.previous != null) {
							if (!andC.previous.andOr) {
								andC.previous.firstAnswer.disqualif = false;
							}
						}
					}
				} else {
					if (!c.associateCondition.isEmpty()) {
						if (c.andorOr) {
							qRet.andConditions.add(new AndCondition(answer, false, c.associateCondition, andC, true));
						} else {
							qRet.andConditions.add(new AndCondition(answer, false, c.associateCondition, andC, false));
						}
					}
				}
			}
		}
		return qRet;
	}
    /**
     * fonction en cours de développement , objectif pouvoir dire si tel case est coché dans la colonne 1, je veux que la colonne 2 soit non null
     * @param option
     * @param c
     * @return qRet
     */
	private QuestionReturn gestionConditionNotEmpty(QuestionReturn option, Condition c) {
		QuestionReturn qRet = option;
		if (c.conditionSens == 0) {
			for (int i = 0; i < reponses.size(); i++) {
				for (int j = i + 1; j < reponses.size(); j++) {
					if (reponses.get(i).questionTag.split("_").length >= 3
							&& reponses.get(j).questionTag.split("_").length >= 3) {
						if (reponses.get(i).questionTag.split("_")[1].equals(reponses.get(j).questionTag.split("_")[1])
								&& !reponses.get(i).questionTag.split("_")[2]
										.equals(reponses.get(j).questionTag.split("_")[2])) {
							if (reponses.get(i).isEmpty && !reponses.get(j).isEmpty) {

								qRet.validate = false;
								reponses.get(i).disqualif = true;
								reponses.get(j).disqualif = true;
							}
							if (reponses.get(i).reponseNumeric == 0
									&& (reponses.get(j).reponseNumeric != 0 && !reponses.get(j).isEmpty)) {

								qRet.validate = false;
								reponses.get(i).disqualif = true;
								reponses.get(j).disqualif = true;
							}
						}
					}
				}
			}
		} else {
			for (int i = 0; i < reponses.size(); i++) {
				for (int j = i + 1; j < reponses.size(); j++) {
					if (reponses.get(i).questionTag.split("_").length >= 3
							&& reponses.get(j).questionTag.split("_").length >= 3) {
						if (reponses.get(i).questionTag.split("_")[2]
								.equals(reponses.get(j).questionTag.split("_")[2])) {
							if (reponses.get(i).isEmpty && !reponses.get(j).isEmpty) {

								qRet.validate = false;
								reponses.get(i).disqualif = true;
								reponses.get(j).disqualif = true;
							}
							if (reponses.get(i).reponseNumeric == 0
									&& (reponses.get(j).reponseNumeric != 0 && !reponses.get(j).isEmpty)) {
								qRet.validate = false;
								reponses.get(i).disqualif = true;
								reponses.get(j).disqualif = true;
							}
						}
					}
				}
			}
		}
		return qRet;
	}

	public QuestionReturn gestionTypeCondition(QuestionReturn option, Condition c, Reponse answer) {
		QuestionReturn qRet = new QuestionReturn();
		qRet = option;
		for (int h = 0; h < c.type.length; h++) {
			if (c.type[h] == 0) {
				if (answer.reponseNumeric > (double) c.sup && answer.reponseNumeric != naValue && !answer.shouldBeEmpty
						&& !answer.isEmpty) {
					qRet = gestionDisqualif(qRet, c, answer);
				} else if (answer.reponseNumeric <= (double) c.sup && answer.reponseNumeric != naValue
						&& !answer.shouldBeEmpty && !answer.isEmpty && !c.associateCondition.isEmpty()) {
					if (c.andorOr) {
						qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, true));
					} else {
						qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, false));
					}
				}
			}
			if (c.type[h] == 1) {
				if (answer.reponseNumeric <= (double) c.inf && answer.reponseNumeric != naValue && !answer.shouldBeEmpty
						&& !answer.isEmpty) {
					qRet = gestionDisqualif(qRet, c, answer);
				}
				if (answer.reponseNumeric > (double) c.inf && answer.reponseNumeric != naValue && !answer.shouldBeEmpty
						&& !answer.isEmpty) {
					if (c.multiple) {
						if (answer.questionTag.contains(".")) {
							qRet.conditions
									.add(new MultipleCondition(c.questionSkip, answer.questionTag.split("\\.")[1]));
						} else {
							qRet.conditions.add(new MultipleCondition(c.questionSkip));
						}
					}
					if (!c.associateCondition.isEmpty()) {

						if (c.andorOr) {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, true));
						} else {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, false));
						}

					}
				}

			}
			if (c.type[h] == 2) {
				if (answer.reponseNumeric != (double) c.eq && answer.reponseNumeric != naValue && !answer.shouldBeEmpty
						&& !answer.isEmpty && !c.multiple && !c.skip && !c.doubleSkip) {
					
					if (c.skip && !c.doubleSkip && c.associateCondition.isEmpty()) {
						qRet.gotSkipTo = true;
						qRet.questionSkip = c.questionSkip;
						qRet.validate = true;
						qRet.questionSkip = qRet.questionSkip.replaceAll(" ", "");
						qRet.setQuestionNumber();
						if (answer.partOfLoop) {
							qRet.loopPart
									.add(new SkipCondition(answer.questionName, answer.questionTag.split("\\.")[1]));
						}
					} else if (c.skip && c.doubleSkip && c.associateCondition.isEmpty()) {
						qRet.doubleSkip = true;
						qRet.beginSkip = c.questionSkip;
						qRet.endSkip = c.questionSkipTo;
						qRet.validate = true;
						qRet.gotSkipTo = true;
						if (answer.partOfLoop) {
							qRet.loopPart.add(new SkipCondition(c.questionSkip, c.questionSkipTo,
									answer.questionTag.split("\\.")[1]));

						}
					} else if (c.multiple && c.associateCondition.isEmpty()) {
						if (answer.questionTag.contains(".")) {
							qRet.conditions
									.add(new MultipleCondition(c.questionSkip, answer.questionTag.split("\\.")[1]));
						} else {
							qRet.conditions.add(new MultipleCondition(c.questionSkip));
						}
					} else if (!c.associateCondition.isEmpty()) {
						if (c.andorOr) {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, true));
						} else {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, false));
						}

					}

				} else if (answer.reponseNumeric == c.eq && answer.reponseNumeric != naValue && !answer.shouldBeEmpty
						&& !answer.isEmpty) {
					qRet = gestionDisqualif(qRet, c, answer);
				}

			}
			if (c.type[h] == 3) {
				if (answer.reponseNumeric == (double) c.neq && answer.reponseNumeric != naValue && !answer.shouldBeEmpty
						&& !answer.isEmpty && !c.multiple && !c.skip && !c.doubleSkip) {
					if (c.skip && !c.doubleSkip && c.associateCondition.isEmpty()) {
						qRet.gotSkipTo = true;
						qRet.questionSkip = qRet.questionSkip.replaceAll(" ", "");
						qRet.questionSkip = c.questionSkip;
						qRet.validate = true;
						qRet.setQuestionNumber();
						if (answer.partOfLoop) {
							qRet.loopPart
									.add(new SkipCondition(answer.questionName, answer.questionTag.split("\\.")[1]));
						}
					} else if (c.skip && c.doubleSkip && c.associateCondition.isEmpty()) {
						qRet.doubleSkip = true;
						qRet.beginSkip = c.questionSkip;
						qRet.endSkip = c.questionSkipTo;
						qRet.validate = true;
						qRet.gotSkipTo = true;
						if (answer.partOfLoop) {
							qRet.loopPart.add(new SkipCondition(c.questionSkip, c.questionSkipTo,
									answer.questionTag.split("\\.")[1]));

						}
					} else if (c.multiple && c.associateCondition.isEmpty()) {
						qRet.conditions.add(new MultipleCondition(c.questionSkip));
					} else if (!c.associateCondition.isEmpty()) {
						if (c.andorOr) {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, true));
						} else {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, false));
						}

					}
				} else if (answer.reponseNumeric != (double) c.neq && answer.reponseNumeric != naValue
						&& !answer.shouldBeEmpty && !answer.isEmpty) {
					qRet = gestionDisqualif(qRet, c, answer);
					
				}

			}
			if (c.type[h] == 4) {

				for (int k = 0; k < c.checkbox.length; k++) {
					
					if (answer.reponseNumeric == c.checkbox[k] && !answer.shouldBeEmpty && !answer.isEmpty) {
						qRet = gestionDisqualif(qRet, c, answer);
						break;
					}
				}

				if (!answer.disqualif && !c.withBraket && !c.associateCondition.isEmpty()) {
					if (c.andorOr) {
						qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, true));
					} else {
						qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, false));
					}
				}
			}
			if (c.type[h] == 5) {
				if ((answer.reponseNumeric > (double) c.max || answer.reponseNumeric < (double) c.min)
						&& answer.reponseNumeric != naValue && !answer.shouldBeEmpty && !answer.isEmpty) {
					qRet = gestionDisqualif(qRet, c, answer);
				} else if ((answer.reponseNumeric < (double) c.max || answer.reponseNumeric > (double) c.min)
						&& answer.reponseNumeric != naValue && !answer.shouldBeEmpty && !answer.isEmpty) {
					if (!c.associateCondition.isEmpty()) {
						if (c.andorOr) {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, true));
						} else {
							qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, false));
						}

					}
				}
			}
			if (c.type[h] == 7) {
				qRet.isConstSum = true;
				if (answer.questionTag.contains(".")) {
					qRet.isLoop = true;
					if (qRet.loopNumber == "") {
						qRet.loopNumber = answer.questionTag.split("\\.")[1];
						qRet.sum = 0;
						if (answer.reponseNumeric != naValue && !answer.questionTag.contains("NA")) {
							qRet.sum += answer.reponseNumeric;
						}
						if (!answer.questionTag.contains("NA")) {
							qRet.questionTagSum.add(answer.questionTag);
						}
					} else if (qRet.loopNumber.equals(answer.questionTag.split("\\.")[1])) {
						if (answer.reponseNumeric != naValue && !answer.questionTag.contains("NA")) {
							qRet.sum += answer.reponseNumeric;
						}
						if (!answer.questionTag.contains("NA")) {
							qRet.questionTagSum.add(answer.questionTag);
						}
					} else {
						if (qRet.sum != c.constSumRes) {
							qRet.validate = false;
							answer.disqualif = true;
							if(c.isAer){
								answer.isAerDisq=true;
							}

						}
						qRet.questionTagSum.clear();
						qRet.sum = 0;
						qRet.loopNumber = answer.questionTag.split("\\.")[1];
						if (answer.reponseNumeric != naValue) {
							qRet.sum += answer.reponseNumeric;
						}
					}
				} else {
					if (answer.reponseNumeric != naValue && !answer.questionTag.contains("NA")) {
						qRet.sum += answer.reponseNumeric;
					}
				}
			}
			if (c.type[h] == 8) {

				for (int k = 0; k < c.checkbox.length; k++) {
					int testNum = 0;
					if (answer.reponseNumeric == 1) {
						String temp = answer.questionTag;
						temp = temp.split("_")[temp.split("_").length - 1];
						if (temp.contains(".")) {
							temp = temp.split("\\.")[0];
						}
						temp = temp.replaceAll("[^\\d.]", "");
						testNum = Integer.valueOf(temp);
					}
					if (testNum == c.checkbox[k] && !answer.shouldBeEmpty && !answer.isEmpty) {
						qRet = gestionDisqualif(qRet, c, answer);
						break;
					}
				}
				if (!answer.disqualif && !c.withBraket && !c.associateCondition.isEmpty()) {

					if (c.andorOr) {
						qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, true));
					} else {
						qRet.andConditions.add(new AndCondition(answer, true, c.associateCondition, false));
					}

				}
			}
		}

		return qRet;
	}

	/**
	 * fonction de traitement de la condition brute si il s'agit d'une date
	 * @param option
	 * @param c
	 * @param answer
	 * @return qRet
	 */
	private QuestionReturn gestionTypeConditionDate(QuestionReturn option, Condition c, Reponse answer) {
		QuestionReturn qRet = new QuestionReturn();
		qRet = option;	
		if(name.contains("P6")){
			int toto=0;
			toto++;
		}
		for (int h = 0; h < c.type.length; h++) {
			if (c.type[h] == 0) {
				if (answer.reponseDate != null) {
					if (answer.reponseDate.after(c.dateCondition)   && answer.reponseNumeric != naValue
							&& !answer.shouldBeEmpty && !answer.isEmpty) {
						if (c.skip && !c.doubleSkip) {	
							qRet.gotSkipTo = true;
							qRet.questionSkip = c.questionSkip;
							qRet.validate = true;
							qRet.questionSkip = qRet.questionSkip.replaceAll(" ", "");
							qRet.setQuestionNumber();
							if (answer.partOfLoop) {
								qRet.loopPart.add(
										new SkipCondition(answer.questionName, answer.questionTag.split("\\.")[1]));
							}
						} else if (c.skip && c.doubleSkip) {
							qRet.doubleSkip = true;
							qRet.beginSkip = c.questionSkip;
							qRet.endSkip = c.questionSkipTo;
							qRet.validate = true;
							qRet.gotSkipTo = true;
							if (answer.partOfLoop) {
								qRet.loopPart.add(new SkipCondition(c.questionSkip, c.questionSkipTo,
										answer.questionTag.split("\\.")[1]));

							}
						} else if (c.multiple) {
							if (answer.questionTag.contains(".")) {
								qRet.conditions
										.add(new MultipleCondition(c.questionSkip, answer.questionTag.split("\\.")[1]));
							} else {
								qRet.conditions.add(new MultipleCondition(c.questionSkip));
							}
						} else {
							qRet.validate = false;
							if(!c.withBraket){
								answer.disqualif = true;
								if(c.isAer){
									answer.isAerDisq=true;
								}
							}
						}
					} 
				}

			}
			if (c.type[h] == 1) {
				if (answer.reponseDate != null) {
					if (answer.reponseDate.before(c.dateCondition)  && answer.reponseNumeric != naValue
							&& !answer.shouldBeEmpty && !answer.isEmpty) {
						if (c.skip && !c.doubleSkip) {
							qRet.gotSkipTo = true;
							qRet.questionSkip = c.questionSkip;
							qRet.validate = true;
							qRet.setQuestionNumber();
							qRet.questionSkip = qRet.questionSkip.replaceAll(" ", "");
							if (answer.partOfLoop) {
								qRet.loopPart.add(
										new SkipCondition(answer.questionName, answer.questionTag.split("\\.")[1]));
							}
						} else if (c.skip && c.doubleSkip) {
							qRet.doubleSkip = true;
							qRet.beginSkip = c.questionSkip;
							qRet.endSkip = c.questionSkipTo;
							qRet.validate = true;
							qRet.gotSkipTo = true;
							if (answer.partOfLoop) {
								qRet.loopPart.add(new SkipCondition(c.questionSkip, c.questionSkipTo,
										answer.questionTag.split("\\.")[1]));

							}
						} else if (c.multiple) {
							if (answer.questionTag.contains(".")) {
								qRet.conditions
										.add(new MultipleCondition(c.questionSkip, answer.questionTag.split("\\.")[1]));
							} else {
								qRet.conditions.add(new MultipleCondition(c.questionSkip));
							}
						} else {
							qRet.validate = false;
							if(!c.withBraket){
								answer.disqualif = true;
								if(c.isAer){
									answer.isAerDisq=true;
								}
							}

						}
					}
					if (answer.reponseDate.compareTo(c.dateCondition)>0 && answer.reponseNumeric != naValue
							&& !answer.shouldBeEmpty && !answer.isEmpty && c.multiple) {
						if (answer.questionTag.contains(".")) {
							qRet.conditions
									.add(new MultipleCondition(c.questionSkip, answer.questionTag.split("\\.")[1]));
						} else {
							qRet.conditions.add(new MultipleCondition(c.questionSkip));
						}
					}
				}
			}
			if (c.type[h] == 2) {
				if (answer.reponseDate != null) {
					if (answer.reponseDate.equals(c.dateCondition) && answer.reponseNumeric != naValue
							&& !answer.shouldBeEmpty && !answer.isEmpty && !c.multiple && !c.skip && !c.doubleSkip) {
						qRet.validate = false;
						if(!c.withBraket){
							answer.disqualif = true;
							if(c.isAer){
								answer.isAerDisq=true;
							}
						}


					} else if (answer.reponseDate.compareTo(c.dateCondition)==0 && answer.reponseNumeric != naValue
							&& !answer.shouldBeEmpty && !answer.isEmpty) {
						if (c.skip && !c.doubleSkip) {
							qRet.gotSkipTo = true;
							qRet.questionSkip = c.questionSkip;
							qRet.validate = true;
							qRet.questionSkip = qRet.questionSkip.replaceAll(" ", "");
							qRet.setQuestionNumber();
							if (answer.partOfLoop) {
								qRet.loopPart.add(
										new SkipCondition(answer.questionName, answer.questionTag.split("\\.")[1]));
							}
						} else if (c.skip && c.doubleSkip) {
							qRet.doubleSkip = true;
							qRet.beginSkip = c.questionSkip;
							qRet.endSkip = c.questionSkipTo;
							qRet.validate = true;
							qRet.gotSkipTo = true;
							if (answer.partOfLoop) {
								qRet.loopPart.add(new SkipCondition(c.questionSkip, c.questionSkipTo,
										answer.questionTag.split("\\.")[1]));

							}
						} else if (c.multiple) {
							if (answer.questionTag.contains(".")) {
								qRet.conditions
										.add(new MultipleCondition(c.questionSkip, answer.questionTag.split("\\.")[1]));
							} else {
								qRet.conditions.add(new MultipleCondition(c.questionSkip));
							}
						}
					}
				}
			}
			if (c.type[h] == 3) {
				if (answer.reponseDate != null) {
					if (!answer.reponseDate.equals(c.dateCondition)  && answer.reponseNumeric != naValue
							&& !answer.shouldBeEmpty && !answer.isEmpty && !c.multiple && !c.skip && !c.doubleSkip) {

						qRet.validate = false;
						if(!c.withBraket){
							answer.disqualif = true;
							if(c.isAer){
								answer.isAerDisq=true;
							}
						}


					} else if (answer.reponseDate.compareTo(c.dateCondition)!=0 && answer.reponseNumeric != naValue
							&& !answer.shouldBeEmpty && !answer.isEmpty) {
						if (c.skip && !c.doubleSkip) {
							qRet.gotSkipTo = true;
							qRet.questionSkip = qRet.questionSkip.replaceAll(" ", "");
							qRet.questionSkip = c.questionSkip;
							qRet.validate = true;
							qRet.setQuestionNumber();
							if (answer.partOfLoop) {
								qRet.loopPart.add(
										new SkipCondition(answer.questionName, answer.questionTag.split("\\.")[1]));
							}
						} else if (c.skip && c.doubleSkip) {
							qRet.doubleSkip = true;
							qRet.beginSkip = c.questionSkip;
							qRet.endSkip = c.questionSkipTo;
							qRet.validate = true;
							qRet.gotSkipTo = true;
							if (answer.partOfLoop) {
								qRet.loopPart.add(new SkipCondition(c.questionSkip, c.questionSkipTo,
										answer.questionTag.split("\\.")[1]));

							}
						} else if (c.multiple) {
							qRet.conditions.add(new MultipleCondition(c.questionSkip));
						}
					}
				}
			}
			

			
		}

		return qRet;
	}
	/**
	 * fonction de traitement des conditions, retourne qRet.validate a true ou false selon la validation
	 * @param c
	 * @param answer
	 * @param option
	 * @return qRet
	 */
	public QuestionReturn tryCondition(Condition c, Reponse answer, QuestionReturn option,boolean onBraketTreatment) {
		QuestionReturn qRet = new QuestionReturn();
		qRet = option;
		if (c.questionValue && !onBraketTreatment) {
			if (c.countryTag.isEmpty()) {
				if(answer.isPartOfLoop()){
					qRet.conditions.add(new MultipleCondition(c.questionSkip,answer.reponseTexte, answer.reponseNumeric,answer.questionTag.split("\\.")[1] ));
					
				}else {
					qRet.conditions.add(new MultipleCondition(c.questionSkip, answer.reponseNumeric, answer.reponseTexte));
				}
			} else if(option.etudename.contains(c.countryTag)) {
				if(answer.isPartOfLoop()){
					qRet.conditions.add(new MultipleCondition(c.questionSkip, answer.reponseNumeric, answer.reponseTexte,
							c.countryTag,answer.questionTag.split(".")[1]));
				}else {
					qRet.conditions.add(new MultipleCondition(c.questionSkip, answer.reponseNumeric, answer.reponseTexte,
							c.countryTag));
				}	
				
			}
		} else {
			if (c.notEmptyCondition) {
				qRet = gestionConditionNotEmpty(qRet, c);
			} else {
				if (c.isDate) {
					if (c.tag != null) {
						if (!c.countryTag.isEmpty()) {
							if (answer.questionTag.contains(c.tag) && qRet.etudename.contains(c.countryTag)) {
								qRet = gestionTypeConditionDate(qRet, c, answer);
							}
						} else if (answer.questionTag.contains(c.tag)) {
							qRet = gestionTypeConditionDate(qRet, c, answer);
						}
					} else {
						if (c.countryTag.isEmpty()) {
							qRet = gestionTypeConditionDate(qRet, c, answer);
						} else if (qRet.etudename.contains(c.countryTag)) {
							qRet = gestionTypeConditionDate(qRet, c, answer);
						}
					}
				} else {
					if (c.tag != null) {
						if (!c.countryTag.isEmpty()) {
							if (answer.questionTag.contains(c.tag) && qRet.etudename.contains(c.countryTag)) {
								qRet = gestionTypeCondition(qRet, c, answer);
							}
						} else if (answer.questionTag.contains(c.tag)) {
							qRet = gestionTypeCondition(qRet, c, answer);
						}
					} else {
						if (c.countryTag.isEmpty()) {
							qRet = gestionTypeCondition(qRet, c, answer);
						} else if (qRet.etudename.contains(c.countryTag)) {
							qRet = gestionTypeCondition(qRet, c, answer);
						}
					}
				}
			}
		}
		return qRet;
	}

	/**
	 * fonction de traitements des questions de façon linéaire par rapport au master.
	 * 
	 * IN : QuestionReturn option , classe contenant toute les options déjà récupérés sur l'études, comme par exemple les conditions multiples
	 * OUT : QuestionReturn qRet , contient les informations d'options si elles sont encore nécéssaire plus les informations détectée dans la question
	 * */
	public QuestionReturn questionTreatement(QuestionReturn option) {
		QuestionReturn qRet = new QuestionReturn();
		boolean alreadyAssign = false;
		qRet.etudename = option.etudename;
		boolean alreadyDoubleSkip = false;
		boolean isSkiped = false;
		if (!reponses.isEmpty()) {
			if(!option.skipTo.isEmpty()){
				for(int i = 0 ; i <option.skipTo.size();i++){
					if(option.skipTo.get(i).questionName.equals(this.name)){
						option.questionSkip = option.skipTo.get(i).questionTo;
						option.skipTo.remove(i);
						option.gotSkipTo=true;
						break;
					}
				}
			}
			// traitement des SkipTo
			if (option.gotSkipTo) { 
				if (option.doubleSkip) {
					alreadyDoubleSkip = true;
				}
				if (option.doubleSkip && option.beginSkip.equals(name)) {
					option.questionSkip = option.endSkip;
					option.setQuestionNumber();
					option.doubleSkip = false;
					alreadyDoubleSkip = false;
					isSkiped=true;
				}
				if (!option.doubleSkip) {
					for (int i = 0; i < reponses.size(); i++) {
						if (reponses.get(i).partOfLoop) {
							for (int j = 0; j < option.loopPart.size(); j++) {
								if (reponses.get(i).questionTag.split("\\.")[1]
										.equals(option.loopPart.get(j).loopNumber)) {
									reponses.get(i).shouldBeEmpty = true;
								}
							}
						} else {
							reponses.get(i).shouldBeEmpty = true;
						}
					}
						qRet = option;
						alreadyAssign = true;
					
				}
				if(option.questionSkip.equals(this.name)){
					isSkiped=true;
					option.gotSkipTo = false;
					option.questionSkip = "";
				}
			}
			//traitement des conditions multiples avec then
			if (option.conditions.size() > 0) {
				for (int i = 0; i < option.conditions.size(); i++) {
					boolean passage = false;
					for (int j = 0; j < reponses.size(); j++) {
						if (!reponses.get(j).questionTag.contains("other")
								&& !reponses.get(j).questionTag.contains("NA")) {
							if (reponses.get(j).partOfLoop) {
								if (reponses.get(j).questionName.equals(option.conditions.get(i).questionName)
										&& reponses.get(j).questionTag.split("\\.")[1]
												.equals(option.conditions.get(i).partOfLoop)) {
									if (option.conditions.get(i).secondCondition != null) {
										qRet = tryCondition(option.conditions.get(i).secondCondition, reponses.get(j),
												qRet,false);
										passage = true;
									}
								}
							} else {
								if (reponses.get(j).questionName.equals(option.conditions.get(i).questionName)) {
									if (option.conditions.get(i).secondCondition != null) {
										qRet = tryCondition(option.conditions.get(i).secondCondition, reponses.get(j),
												qRet,false);
										passage = true;
									}
								}
							}
						}
					}
					if (passage) {
						option.conditions.remove(i);
						i--;
					}
				}
			}
			//traitement des andCondition simple
			if (option.andConditions.size() > 0) {
				for (int i = 0; i < option.andConditions.size(); i++) {
					boolean del = false;
					if (this.name.equals(option.andConditions.get(i).qName)) {
						if (!option.andConditions.get(i).firstPart) {
							for (int j = 0; j < reponses.size(); j++) {
								if (reponses.get(j).questionName.equals(option.andConditions.get(i).qName)) {
									if (option.andConditions.get(i).loopPart.isEmpty()) {
										if (option.andConditions.get(i).andOr) {
											reponses.get(j).disqualif = true;
										
											if(option.andConditions.get(i).secondPart.isAer){
												reponses.get(j).isAerDisq=true;
											}
											if (!option.andConditions.get(i).secondPart.associateCondition.isEmpty()) {
												if (option.andConditions.get(i).secondPart.andorOr) {
													qRet.andConditions
															.add(new AndCondition(reponses.get(j), false,
																	option.andConditions
																			.get(i).secondPart.associateCondition,
																	option.andConditions.get(i), true));
												} else {
													qRet.andConditions
															.add(new AndCondition(reponses.get(j), false,
																	option.andConditions
																			.get(i).secondPart.associateCondition,
																	option.andConditions.get(i), false));
												}
											}
											if (option.andConditions.get(i).previous != null) {
												option.andConditions.get(i).previous.firstAnswer.disqualif = true;
											}
											del = true;
										} else {
											qRet = gestionConditionAndOr(qRet, reponses.get(j),
													option.andConditions.get(i));
										}

									} else if (reponses.get(j).questionTag.contains(".")) {
										if (option.andConditions.get(i).andOr) {
											reponses.get(j).disqualif = true;
											if(option.andConditions.get(i).secondPart.isAer){
												reponses.get(j).isAerDisq=true;
											}
											if (!option.andConditions.get(i).secondPart.associateCondition.isEmpty()) {
												if (option.andConditions.get(i).secondPart.andorOr) {
													qRet.andConditions
															.add(new AndCondition(reponses.get(j), false,
																	option.andConditions
																			.get(i).secondPart.associateCondition,
																	option.andConditions.get(i), true));
												} else {
													qRet.andConditions
															.add(new AndCondition(reponses.get(j), false,
																	option.andConditions
																			.get(i).secondPart.associateCondition,
																	option.andConditions.get(i), false));
												}
											}
											if (option.andConditions.get(i).previous != null) {
												option.andConditions.get(i).previous.firstAnswer.disqualif = true;
											}
											del = true;
										} else {
											if (option.andConditions.get(i).loopPart
													.equals(reponses.get(j).questionTag.split("\\.")[1])) {
												qRet = gestionConditionAndOr(qRet, reponses.get(j),
														option.andConditions.get(i));
											}
										}
									}
								}
							}
						} else {
							for (int j = 0; j < reponses.size(); j++) {
								if (reponses.get(j).questionName.equals(option.andConditions.get(i).qName)) {
									if (option.andConditions.get(i).loopPart.isEmpty()) {
										qRet.validate = true;
										qRet = gestionConditionAndOr(qRet, reponses.get(j),
												option.andConditions.get(i));
										del = true;
									} else if (reponses.get(j).questionTag.contains(".")) {
										if (reponses.get(j).questionTag.split("\\.")[1]
												.equals(option.andConditions.get(i).loopPart)
												&& !reponses.get(j).questionTag.contains("NA")) {
											qRet = gestionConditionAndOr(qRet, reponses.get(j),
													option.andConditions.get(i));
											del = true;
										}
									}
								}
							}
						}
					}
					if (del) {
						option.andConditions.remove(i);
						i--;
					}
				}

			}
		   
			qRet.isAnswer = true;
			int countSpecific = 0;
			int insertSpecific = 0;
			// décompte des conditions avec parenthèse dans la question
			for (int i = 0; i < conditions.size(); i++) {
				if (conditions.get(i).withBraket) {
					countSpecific++;
				}
			}
			if(!isSkiped){
				for (int i = 0; i < reponses.size(); i++) {
					for (int j = 0; j < conditions.size(); j++) {
						if (!reponses.get(i).questionTag.contains("NA")) {
							if (conditions.get(j) != null) {
								if (conditions.get(j).withBraket && conditions.get(j).subCondition.size() >= 1
										&& countSpecific > insertSpecific) {
									//si il s'agit d'une condition avec parenthèse, on l'ajoute a qRet et on la traitera a la fin de l'étude
									if(!conditions.get(j).subCondition.get(0).isValue){
										conditions.get(j).subCondition.get(0).link = this.name;
									}
									qRet.specificC.add(new SpecificList(conditions.get(j).subCondition,conditions.get(j).isAer));
									if (!conditions.get(j).countryTag.isEmpty()) {
										qRet.specificC
												.get(qRet.specificC.size() - 1).countryTag = conditions.get(j).countryTag
														.replaceAll(" ", "");
									}
									if(conditions.get(j).subCondition.get(0).c.questionValue){
 										conditions.get(j).subCondition.get(0).c.questionSkip = this.name;
									}
									insertSpecific++;
								} else if (!conditions.get(j).withBraket) {
									// traitement des conditions simples
									if(conditions.get(j).isCheckedCondition){
										if(reponses.get(i).isPartOfLoop()){
											
											qRet = gestionNbrChecked(qRet, conditions.get(j), reponses, reponses.get(i).questionTag.split("\\.")[1]);
											qRet.validate=true;
										}else {
											qRet = gestionNbrChecked(qRet, conditions.get(j), reponses, "");
											qRet.validate=true;
										}
										
									}else {
										qRet = tryCondition(conditions.get(j), reponses.get(i), qRet,false);
									}
								}
							}
						}
					}
				}
				//traitement des const sum
				if (qRet.isConstSum && !qRet.isLoop) {
					for (int h = 0; h < conditions.size(); h++) {
						for (int j = 0; j != conditions.get(h).type.length; j++) {
							if (conditions.get(h).type[j] == 7) {
								if (qRet.sum != conditions.get(h).constSumRes) {
									for (int k = 0; k != reponses.size(); k++) {
										if (qRet.isLoop == false) {
											if (!reponses.get(k).questionTag.contains("NA")) {
												reponses.get(k).disqualif = true;
												if(conditions.get(h).isAer){
													reponses.get(k).isAerDisq=true;
												}
											}
										}
									}
									qRet.validate = false;
								}
							}
						}
					}
				}
			}
			//recopie des informations d'options si nécessaire 
			if (alreadyDoubleSkip && !alreadyAssign) {
				qRet.doubleSkip = true;
				qRet.beginSkip = option.beginSkip;
				qRet.endSkip = option.endSkip;
				qRet.gotSkipTo = true;
				for (int i = 0; i < option.loopPart.size(); i++) {
					qRet.loopPart.add(option.loopPart.get(i));
				}
			}
			if (option.conditions.size() > 0 && !alreadyAssign) {
				qRet.conditions.addAll(option.conditions);
			}
			if (option.andConditions.size() > 0 && !alreadyAssign) {
				qRet.andConditions.addAll(option.andConditions);
			}

			if (option.specificC.size() > 0 && !alreadyAssign) {
				qRet.specificC.addAll(option.specificC);
			}
		} else {
			this.isAnswer = false;
			if (option.gotSkipTo) {
				if (option.questionSkip.equals(name) && !option.doubleSkip) {
					option.gotSkipTo = false;
					option.questionSkip = "";
				}
			}
			qRet = option;
		}
		if(option.skipTo.size()>0){
			if(!qRet.skipTo.isEmpty()){
				for(int i= 0 ; i < option.skipTo.size();i++){
					boolean add = true;
					for(int j = 0 ; j < qRet.skipTo.size();j++){
						if(qRet.skipTo.get(j).equals(option.skipTo.get(i))){
							add = false;
						}
					}
					if(add){
						qRet.skipTo.add(option.skipTo.get(i));
					}
				}
			} else {
				qRet.skipTo.addAll(option.skipTo);
			}
		}
	    if(name.equals("S7c") ||name.equals("S8")){
			//System.out.println();
		}
		// ajout des réponses nécessaires pour les conditions avec parenthèse
		for (int i = 0; i < qRet.specificC.size(); i++) {
			for (int h = 0; h < qRet.specificC.get(i).conditions.size(); h++) {
				for (int j = 0; j < reponses.size(); j++) {
					if (!qRet.specificC.get(i).conditions.get(h).isValue) {
						if (!reponses.get(j).isEmpty && !qRet.specificC.get(i).conditions.get(h).isLink
								&& qRet.specificC.get(i).conditions.get(h).link.equals(reponses.get(j).questionName)) {
							if(qRet.specificC.get(i).conditions.get(h).c.tag!=null){
								if(reponses.get(j).questionTag.contains(qRet.specificC.get(i).conditions.get(h).c.tag)){
									qRet.specificC.get(i).conditions.get(h).answers.add(reponses.get(j));
								}
							}else {
								qRet.specificC.get(i).conditions.get(h).answers.add(reponses.get(j));
							}
						}
					} else {
						if (!reponses.get(j).isEmpty && qRet.specificC.get(i).conditions.get(h).c.questionSkip
								.equals(reponses.get(j).questionName)) {
							if(qRet.specificC.get(i).conditions.get(h).c.tag!=null){
								if(reponses.get(j).questionTag.contains(qRet.specificC.get(i).conditions.get(h).c.tag)){
									qRet.specificC.get(i).conditions.get(h).answers.add(reponses.get(j));
								}
							}else {
								if(qRet.specificC.get(i).conditions.get(h).c.questionValue){
									if(reponses.get(j).isPartOfLoop()){
										qRet.specificC.get(i).conditions.get(h).c.valueCondition.add(reponses.get(j));
										
									}else {
										qRet.specificC.get(i).conditions.get(h).c.valueCondition.add(reponses.get(j));
									}
								} else {
									qRet.specificC.get(i).conditions.get(h).answers.add(reponses.get(j));
								}
							}
						} else if (!reponses.get(j).isEmpty && qRet.specificC.get(i).conditions.get(h).link.equals(reponses.get(j).getQuestionName())){
							qRet.specificC.get(i).conditions.get(h).answers.add(reponses.get(j));
						}
					}
				}
			}
		}
		
		for(int i = 0 ; i < qRet.specificC.size();i++){
			for(int j = 0 ; j < qRet.specificC.get(i).conditions.size();j++){
				if(!qRet.specificC.get(i).conditions.get(j).isLink){
					if(qRet.specificC.get(i).conditions.get(j).c.questionValue){
						for(int h = 0 ; h < reponses.size();h++){
 							if(reponses.get(h).questionName.equals(qRet.specificC.get(i).conditions.get(j).c.questionSkip)){
											}
						}
					}
				}
			}
		}
		
		
		return qRet;
	}

}
