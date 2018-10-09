/**
 * Zombie Male
 * Created By : Junyi Chen
 * width = 160
 * height = 240
 * sprite sheet from https://www.gameart2d.com/freebies.html
 */

public class ZombieMale {



	//1 = moving to player, 2 = attacking, 3  = dead
	private int zombieStatus;
	
	//health of zombie
	public int zombieHealth;
	
	//zombie facing 
	private boolean faceLeft;
	private boolean faceRight;
	
	//zombie position
	private double maleX ;
	private double maleY;
	
	//moving velocity(random)
	private double velocityX, velocityY;
	
	//width = 160 height = 240
	private int width;
	private int height;
	
	//value to check if the zombie is getting attacked by player
	public boolean underAttack;
	
	//sprite frame
	public int spriteFrame;

	//get zombie status
	public int getStatus(){
		return zombieStatus;
	}
	
	//set zombie status
	public void setStatus(int i){
		zombieStatus = i;
	}
	
	//Constructor 
	public ZombieMale(double randomX, double randomY, double randomVX, double randomVY){
		//when initialising each zombie given a random position 
		maleX = randomX;
		maleY = randomY;
		//zombie moving toward player
		zombieStatus = 1;
		velocityX = randomVX; //random velocityx
		velocityY = randomVY; //random Velocityy
		spriteFrame = 0;
		width = 160;
		height = 240;
		zombieHealth = 100;
		underAttack = false;
	}
	
	//get the side that zombie is facing
	public String faceing(){
		if(faceRight){
			return "right";
		} else{
			return "left";
		}
	}
	
	//get width of zombie
	public int getWidth(){
		return width;
	}
	
	//get hieght of zombie
	public int getHeight(){
		return height;
	}
	
	//get zombie position X
	public double getX(){
		return maleX;
	}
	
	//get zombie position Y
	public double getY(){
		return maleY;
	}
	
	//function update zombie
	public void updateZombie(Knight player, double dt){
		if(underAttack){ // if player is attacking zombie
			//losing health
			zombieHealth -=3;
			underAttack = false;
			if(zombieHealth <= 0){//zombie is dead when health is below 0
				setStatus(3);
			}
		}
		
		//zombie is attacking
		if(zombieStatus == 2){
			
			spriteFrame++;
			if(spriteFrame > 7){spriteFrame = 0;}	
			
			//check player within the attack range, if not, move toward player
			if(playerWithinAttackRange(player.getKnightX(), player.getKnightY()) && player.getStatus() != 4){//if player is not dead
				player.underAttack = true;
			} else {
				setStatus(1);
			}
		}
		//zombie is dead
		else if (zombieStatus == 3){
			if(spriteFrame!= 9){
				spriteFrame++;
			}
		} else {
			//zombie move toward player
			if(spriteFrame == 9){spriteFrame = 0;}
			//if zombie at left of player
			if(maleX <= player.getKnightX()){ 
				faceRight = true; //zombie should face right
				setFaceLeft(false);
				//move to player
				maleX+= velocityX * dt;
			}
			else if(maleX > player.getKnightX() ){
				//if zombie at right of player
				setFaceLeft(true); //zombie should face left
				faceRight = false;
				//move to player
				maleX -= velocityX *dt;
			}
			
			if(maleY < player.getKnightY()){
				//zombie is above player
				maleY += velocityY *dt;
			}
			else if(maleY > player.getKnightY()){
				//zombie below player
				maleY -= velocityY *dt;
			}

			spriteFrame++;
			
			//check if player within attack range
			if(playerWithinAttackRange(player.getKnightX(), player.getKnightY())){
					spriteFrame = 0;
				//if player within attack range, attack player
				setStatus(2);
			}
		}
	}
	
	//function that check if the player within attack range, return true if yes, else return false;
	private boolean playerWithinAttackRange(double playerX, double playerY) {
		
		//player within attack range
		if(maleX >= playerX){
			if(maleY >= playerY && maleY <= playerY + 25.0){
				if(playerX >= maleX || playerX +50.0 >=  maleX){
					return true;
				}
				}
		}
		
		
		if(maleX < playerX){
			if(maleY >= playerY && maleY <= playerY + 25.0){
				if(playerX <= maleX + 50.0){
					return true;
				}
			}
		}
		return false;
	}

	public boolean isFaceLeft() {
		return faceLeft;
	}

	public void setFaceLeft(boolean faceLeft) {
		this.faceLeft = faceLeft;
	}
	

}
