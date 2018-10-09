/**
 * Created By : Junyi Chen
 * This is the player 
 * width = 160
 * height = 240
 * sprite sheet from https://www.gameart2d.com/freebies.html
 */

public class Knight {
	
	
	//players position
	private double knightX ;
	private double knightY;
	
	//1 = idle, 2 = attack, 3 = run, 4 = die
	private int knightStatus;
	
	//knight moving position
	public boolean left, right,up,down;
	
	//knight facing left or right
	public boolean faceLeft, faceRight;
	
	//sprite frame
	public int spriteFrame;
	
	//boolean value check if player is getting attack
	public boolean underAttack;
	
	//health of player
	public int knightHealth;
	
	//consturctor initialise player
	public Knight(int x, int y){
		knightX = x;
		knightY = y;
		knightStatus = 1;
		left = false;
		right = false;
		up = false;
		down = false;
		faceLeft = false;
		faceRight = true;
		underAttack = false;
		knightHealth = 1000;
	}
	
	//set staus of knight
	public void setStatus(int i){
		knightStatus = i;
	}
	
	public int getStatus(){
		return knightStatus;
	}
	
	//method set up position X of the palyer
	public void setKnightX(int x){
		knightX = x;
	}
	
	//method set up position Y of the palyer
	public void setKnightY(int y){
		knightY = y;
	}
	
	//get player position X
	public double getKnightX(){
		return knightX;
	}
	
	//get player position Y
	public double getKnightY(){
		return knightY;
	}
	
	//attack detection
	public void attackDetection(ZombieMale zombie){
		double zombieX = zombie.getX();
		double zombieY = zombie.getY();
		//only work when zombie is not dead
		if(zombie.getStatus() != 4){
		if(faceLeft){ // if player is facing left only attack zombie left
			double attackRangeX = knightX;
			if(zombieX + 100.0 > attackRangeX && zombieX <= knightX+50.0){ // zombie got attacked 
				if(zombieY >= knightY && zombieY <= knightY+25.0){
				
					//set zombie status to under attack
					zombie.underAttack = true;
				}
			}
		}  
		else if(faceRight){//if player is facing right
			double attackRangeX = knightX + 100.0;
			//if the zombie within the attack range
			if(zombieX <= attackRangeX && zombieX >= knightX ){
				if(zombieY >= knightY && zombieY <= knightY+25.0){
						
					//set zombie status to under attack
					zombie.underAttack = true;
				}
			}
		} 
		}
	}
	
	public void attackDetectionFemale(ZombieFemale zombie){
		double zombieX = zombie.getX();
		double zombieY = zombie.getY();
		//only work when zombie is not dead
		if(zombie.getStatus() != 4){
		if(faceLeft){ // if player is facing left only attack zombie left
			double attackRangeX = knightX;
			if(zombieX + 100.0 > attackRangeX && zombieX <= knightX+50.0){ // zombie got attacked 
				if(zombieY >= knightY-5.0 && zombieY <= knightY+25.0){
	
					//set zombie status to under attack
					zombie.underAttack = true;
				}
			}
		}  
		else if(faceRight){//if player is facing right
			double attackRangeX = knightX + 100.0;
			//if the zombie within the attack range
			if(zombieX <= attackRangeX && zombieX >= knightX ){
				if(zombieY >= knightY-5.0 && zombieY <= knightY+25.0){
						
					//set zombie status to under attack
					zombie.underAttack = true;
				}
			}
		} 
		}
	}
	
	public void updateKnight(double dt, int velocityX, int velocityY){
		
		
		if(underAttack){
			knightHealth-=3;
			underAttack = false;
			if(knightHealth <= 0){
				spriteFrame = 0;
				this.knightStatus = 4;
			}
		}
		
		if(this.knightStatus == 4){
			if(spriteFrame != 9){
				spriteFrame++;
			}
		}
		else if(this.knightStatus == 1 || this.knightStatus == 2){
			if(spriteFrame == 9){spriteFrame = 0;}
			spriteFrame++;
			
		}
		
		else if(this.knightStatus == 3){
			if(spriteFrame == 9){spriteFrame = 0;}
			spriteFrame++;
			
			// if player is moving
			if(right && up){ //going right up
				if(knightX <= 940){
					knightX += velocityX * dt;
				}
				if(knightY > 5){
					knightY -= velocityY * dt;
				}
			} 
			if(right && down){ //going right down
				if(knightX <= 940){
					knightX += velocityX *dt;
				}
				if(knightY <= 410){
					knightY += velocityY * dt;
				}
			}
			if(left && up){ //going left up
				if(knightX >= 0){
					knightX -= velocityX * dt;
				}
				if(knightY > 5){
					knightY -= velocityY * dt;
				}
			} 
			if(left && down){//going left down
				if(knightX >= 0){
					knightX -= velocityX *dt;
				}
				if(knightY <= 410){
					knightY += velocityY * dt;
				}
			}
			if(right && !up && !down){ //going only right
				if(knightX <= 940){
					knightX += velocityX * dt;
				}
			} 
			if(left && !up && !down){ //going only left
				if(knightX >= 0){
					knightX -= velocityX * dt;
				}
			}
			if(up && !left && !right){ //going only up
				if(knightY > 5){
					knightY -= velocityY *dt;
				}
			}
			if(down && !left && !right){ //going only down
				if(knightY <= 410){
					knightY += velocityY *dt;
				}
			}
		
		}
		
		
	}
	
}
