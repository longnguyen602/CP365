
package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;


public class MyAgent extends BasicMarioAIAgent implements Agent
{

	public MyAgent()
	{
		super("MyAgent");
		reset();
	}

	// Does (row, col) contain an enemy?
	public boolean hasEnemy(int row, int col) {
		return enemies[row][col] != 0;
	}

	// Is (row, col) empty?
	public boolean isEmpty(int row, int col) {
		return (levelScene[row][col] == 0);
	}


	// Display Mario's view of the world
	public void printObservation() {
		System.out.println("**********OBSERVATIONS**************");
		for (int i = 0; i < mergedObservation.length; i++) {
			for (int j = 0; j < mergedObservation[0].length; j++) {
				if (i == mergedObservation.length / 2 && j == mergedObservation.length / 2) {
					System.out.print("M ");
				}
				else if (hasEnemy(i, j)) {
					System.out.print("E ");
				}
				else if (!isEmpty(i, j)) {
					System.out.print("B ");
				}
				else {
					System.out.print(" ");
				}
			}
			System.out.println();
		}
		System.out.println("************************");
	}

	// Actually perform an action by setting a slot in the action array to be true
	public boolean[] getAction()
	{
		//running and jumping seems to be the best way to get past level 0
		//after level 0 everything I tried could not pass
      int x=9;
      int y=9;
      boolean run= true;
		//action[Mario.KEY_SPEED] = action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
      // for (int i=1; i<=9; i+=1){
      // if (hasEnemy(x,y+i) || !isEmpty(x,y+i) || !isEmpty(x,y+1))
      // {
      //    //action[Mario.KEY_RIGHT] = false;
      //    System.out.println("Next Block has Enemy");
				 action[Mario.KEY_SPEED] = action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
				 action[Mario.KEY_RIGHT] = true;
      //   //  action[Mario.KEY_SPEED] = action[Mario.KEY_JUMP] = isMarioAbleToJump;
      //  // action[Mario.KEY_RIGHT] = false;
      //  // action[Mario.KEY_LEFT] = true;
			//
      // }//end of if
      // else {
      //         action[Mario.KEY_LEFT] = false;
      //       action[Mario.KEY_RIGHT] = true;
			//
			//
      // }
			//
      //  }//end of for loop


      printObservation();
		return action;
	}

	// Do the processing necessary to make decisions in getAction
	public void integrateObservation(Environment environment)
	{
		super.integrateObservation(environment);
    	levelScene = environment.getLevelSceneObservationZ(2);
	}

	// Clear out old actions by creating a new action array
	public void reset()
	{
		action = new boolean[Environment.numberOfKeys];
	}
}
