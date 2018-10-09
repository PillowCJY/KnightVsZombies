/**
 * Knight vs Zombies 
 * W = Move up
 * S = Move Down
 * A = Move Left
 * D = Move Right
 * Space = Attack
 * 
 * Created By : Junyi Chen
 * 						  
 * Sprite sheets from : https://www.gameart2d.com/freebies.html
 * Audio from : https://freesound.org/people/GameAudio/packs/13940/
 *GameBackgorund from : https://game-factory.deviantart.com/art/Zombie-Weeners-Cemetery-183334704
 *Background soundtrack : 
 */

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class KnightVsZombie extends GameEngine{

	//Game state
	enum GameState {Menu,GameInstruction, Playing, Pause,NextRound, Options, Over}
	GameState gameState;
	
	//Game variables
	int backgroundNumber;//For background
	int menuOption;
	
	//Audio 
	AudioClip zombieRoar;
	AudioClip warCry;
	AudioClip playerDeath;
	AudioClip background;
	AudioClip beep;	
	AudioInputStream run;
	AudioInputStream roar;
	Clip clip;
	Clip clip2;
	//volume of background sound
	float volume = 1;
	
	//Images for menu
	Image play;
	Image playHighlighted;
	Image options;
	Image optionsHighlighted;
	Image exit; 
	Image exitHighlighted;

	Image darkBackground;
	Image title;
	Image whiteBackground;
	Image  gameBackground;
	Image gameOverBG;
	
	Image mute;
	Image muteHighlighted;
	Image changeBackground;
	Image changeBackgroundHighlighted;
	
	Image surviveTitle;
	Image scoreTitle;
	Image hint;
	Image scoreBorder;
	Image pause;
	Image gameOver;
	Image gameInstruction;
	
	//player Score
	int playerScore;
	
	//player sprite sheet
		Image[] knightAttack = new Image[10];
		Image[] knightDead = new Image[10];
		Image[] knightIdle = new Image[10];
		Image[] knightRun = new Image[10];
		
		
	//explosion
		Image explosion;
		Image[] explosionImages;
		int explosionFrame;
		
	//ZombieMale sprite sheet
		public Image[] maleAttack = new Image[8];
		public Image[] maleDead = new Image[10];
		public Image[] maleWalk = new Image[10];
	
	//ZombieFemale sprite sheet
		public Image[] femaleAttack = new Image[8];
		public Image[] femaleDead = new Image[10];
		public Image[] femaleWalk = new Image[10];

	//player and zombies
		private Knight player;
		private ZombieMale[] zombieMale;
		private ZombieFemale[] zombieFemale;
		
		//number of zombies 
		private int zombieNumMale;
		private int zombieNumFemale;
		
		//min number of zombies, used for rand function
		private int zombieRandom;
		
	public static void main(String[] args) {
		createGame(new KnightVsZombie(), 15);
	}
	//Initialise the game
	public void init(){
		this.setWindowSize(1100, 650);
		gameState = GameState.Menu;
		backgroundNumber = 0;
		menuOption = 0;
		
		resetGame();
		readImages();
		readKnightSprites();
		readZombieMaleSprites();
		readZombieFemaleSprites();
		
		
		readExplosion();
		zombieRoar = loadAudio("zombieRoar.wav");
		warCry = loadAudio("warCry.wav");
		playerDeath = loadAudio("playerDeath.wav");
		background  = loadAudio("pvzbackground.wav");
		beep 	 = loadAudio("beep.wav");
		URL url = null;
		URL url2 = null;
		url = this.getClass().getClassLoader().getResource("run.wav");
		url2 = this.getClass().getClassLoader().getResource("warCry.wav");
		try {
			run = AudioSystem.getAudioInputStream(url);
			roar = AudioSystem.getAudioInputStream(url2);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			clip = AudioSystem.getClip();
			clip.open(run);
			clip2 = AudioSystem.getClip();
			clip2.open(roar);
		} catch (LineUnavailableException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//play background music
		startAudioLoop(background, volume);
		
	}
	
	//Read  Images
		public void readImages(){
			play               = loadImage("menu\\Play1.png");
			playHighlighted    = loadImage("menu\\Playhighlighted.png");
			options            = loadImage("menu\\Options1.png");
			optionsHighlighted = loadImage("menu\\Optionshighlighted.png");
			exit               = loadImage("menu\\Exit1.png");
			exitHighlighted    = loadImage("menu\\Exithighlighted.png");

			darkBackground					 = loadImage("menu\\NewBackground.png");
			title				 = loadImage("menu\\titlegame2.png");
			whiteBackground					 = loadImage("menu\\whiteBackground.png");
			gameBackground = loadImage("menu\\gameBackground.png");

			mute              = loadImage("menu\\mutesound.png");
			muteHighlighted   = loadImage("menu\\mutesoundhighlighted.png");
			changeBackground              = loadImage("menu\\background1.png");
			changeBackgroundHighlighted   = loadImage("menu\\background1highlighted.png");
			
			surviveTitle = loadImage("menu\\survived.png");
			scoreTitle = loadImage("menu\\score.png");
			hint = loadImage("menu\\hint.png");
			scoreBorder = loadImage("menu\\scoreBorder.png");
			pause = loadImage("menu\\pause.png");
			gameOver = loadImage("menu\\gameOver.png");
			gameInstruction = loadImage("menu\\gameInstruction.png");
			gameOverBG = loadImage("menu\\gameOverBG.png");
		}
	
	public void resetGame(){
		zombieNumMale = 2; 
		zombieNumFemale = 2;
		zombieRandom = 1; //rand() + zombieRandom which ensures that at least one zombie will be generated at the beginning
		player = new Knight(300,350); 
		playerScore = 0; 
		resetZombie();
	}
	
	public void resetZombie(){
		//random number of zombie (at the beginning zombieRandom = 1 so it insures that at least 2 zombies  will be initialised)
		zombieNumMale = rand(zombieNumMale) + zombieRandom;
		zombieNumFemale = rand(zombieNumFemale) + zombieRandom;
		double randX; //random positionX for zombie
		double randY; //random position Y
		double randVX; //random velcoity X
		double randVY; //random velocity Y
		
		zombieMale = new ZombieMale[zombieNumMale];
		for(int i = 0; i < zombieNumMale ; i++){
			//generate a random position for each zombie 
			if(i < zombieNumMale/2){
				randX = rand(10) - 110;
			} else {
				randX = rand(100) + 1100;
			}
				randY = rand(650);
			
			//generate a random velcity for each zombie
			randVX = rand(25) + 10;
			randVY = rand(25) + 10;
			
				zombieMale[i] = new ZombieMale(randX, randY, randVX, randVY);
		}
		
		zombieFemale = new ZombieFemale[zombieNumFemale];
		for(int i = 0; i < zombieNumFemale ; i++){
			//generate a random position for each zombie
			if(i < zombieNumFemale/2){
				randX = rand(10) - 110;
			} else {
				randX = rand(100) + 1100;
			}
				randY = rand(650);
			
			//generate a random velcity for each zombie
			randVX = rand(25) + 5;
			randVY = rand(25) + 5;
			
				zombieFemale[i] = new ZombieFemale(randX, randY, randVX, randVY);
		}

	}
	
	//Read the explosion sprite sheets
	public void readExplosion(){
		explosion = loadImage("explosion.png");
		 explosionImages = new Image[20];
	        // Extract Individual Images
	        for(int i = 0; i < 20; i++) {
	            explosionImages[i] = subImage(explosion, i*128, 0, 128, 128);
	        }
	        
	}
	
	//Read the Knight sprite sheets
	public void readKnightSprites(){
			
			for(int i = 0; i < 10; i++){
				String url1 = "knight\\Attack ("+(i+1)+").png";
				String url2 ="knight\\Dead ("+ (i+1) +").png";
				String url3 = "knight\\Idle ("+(i+1)+").png";
				String url4 ="knight\\Run ("+ (i+1) +").png";
				
				knightAttack[i] = loadImage(url1);	
				knightDead[i] = loadImage(url2);
				knightIdle[i] = loadImage(url3);			
				knightRun[i] = loadImage(url4);
				
			}
		}
	
	//Read Zombie sprite sheet 
	public void readZombieMaleSprites(){
		for(int i = 0; i < 10; i++){
			if(i < 8){
				String url1 = "zombieMale\\Attack ("+(i+1)+").png";
				maleAttack[i] = loadImage(url1);	
			}
			String url2 ="zombieMale\\Dead ("+ (i+1) +").png";
			String url3 = "zombieMale\\Walk ("+(i+1)+").png";
		
			
			maleDead[i] = loadImage(url2);
			maleWalk[i] = loadImage(url3);
			
		}
	}
	
	//Read Zombie female sprite sheet
	public void readZombieFemaleSprites(){
		for(int i = 0; i < 10; i++){
			if(i < 8){
				String url1 = "zombieFemale\\Attack ("+(i+1)+").png";
				femaleAttack[i] = loadImage(url1);	
			}
			String url2 ="zombieFemale\\Dead ("+ (i+1) +").png";
			String url3 = "zombieFemale\\Walk ("+(i+1)+").png";
		
			femaleDead[i] = loadImage(url2);
			femaleWalk[i] = loadImage(url3);
			
		}
	}
	
	//Draw player
	public void drawPlayer(){
		switch(player.getStatus()){
			case 1: //Player idle
				if(player.faceRight){
					drawImage(knightIdle[player.spriteFrame], player.getKnightX(), player.getKnightY(), 160,240);
				} else if(player.faceLeft){
					drawImage(knightIdle[player.spriteFrame], player.getKnightX()+160, player.getKnightY(), -160,240);
				}
					break;
			case 2: //Player attack
				if(player.faceRight){
					drawImage(knightAttack[player.spriteFrame], player.getKnightX(), player.getKnightY(), 160,240);
					drawImage(explosionImages[explosionFrame], player.getKnightX()+100, player.getKnightY() + 120);
				} else if(player.faceLeft){
					drawImage(knightAttack[player.spriteFrame], player.getKnightX()+160, player.getKnightY(), -160,240);
					drawImage(explosionImages[explosionFrame], player.getKnightX()-60, player.getKnightY() + 120);

				}
				break;
			case 3: //Player running
				if(player.faceRight){
					drawImage(knightRun[player.spriteFrame], player.getKnightX(), player.getKnightY(), 160,240);
				} else if(player.faceLeft){
					drawImage(knightRun[player.spriteFrame], player.getKnightX()+160, player.getKnightY(), -160,240);
				}
					break;
			case 4: //Player dead
				if(player.faceRight){
					drawImage(knightDead[player.spriteFrame], player.getKnightX(), player.getKnightY(), 200,240);
				} else if(player.faceLeft){
					drawImage(knightDead[player.spriteFrame], player.getKnightX()+200, player.getKnightY(), -200,240);
				}
					break;
		}
		changeColor(red);
		//Drawing the health of player 
		drawSolidRectangle(player.getKnightX(), player.getKnightY(), player.knightHealth/10, 5);
	}
	
	//Draw Zombie Male
	public void drawZombieMale(ZombieMale zombie){
		switch(zombie.getStatus()){
		case 1:
			//Zombie  moving toward to player 
			if(zombie.faceing().equals("right")){
				drawImage(maleWalk[zombie.spriteFrame], zombie.getX(), zombie.getY(), zombie.getWidth(),zombie.getHeight());
			} else if(zombie.faceing().equals("left")){
				drawImage(maleWalk[zombie.spriteFrame], zombie.getX()+160, zombie.getY(), (-zombie.getWidth()),zombie.getHeight());
			}
			break;
		
		case 2:
			//palyer within zombie attack range, zombie attack
			if(zombie.faceing().equals("right")){
				drawImage(maleAttack[zombie.spriteFrame], zombie.getX(), zombie.getY(), zombie.getWidth(),zombie.getHeight());
			} else if(zombie.faceing().equals("left")){
				drawImage(maleAttack[zombie.spriteFrame], zombie.getX()+160, zombie.getY(), (-zombie.getWidth()),zombie.getHeight());
			}
			break;
		case 3:
			//zombie dead
			if(zombie.faceing().equals("right")){
				drawImage(maleDead[zombie.spriteFrame], zombie.getX(), zombie.getY(), zombie.getWidth(),zombie.getHeight());
			} else if(zombie.faceing().equals("left")){
				drawImage(maleDead[zombie.spriteFrame], zombie.getX()+160, zombie.getY(), (-zombie.getWidth()),zombie.getHeight());
			}
			break;
		}
		changeColor(black);
		//Drawing zombie health
		drawSolidRectangle(zombie.getX(),zombie.getY(),zombie.zombieHealth, 5);
	}
	
	//Draw zombie female
	public void drawZombieFemale(ZombieFemale zombie){
		switch(zombie.getStatus()){
		case 1:
			//Zombie moving toward player
			if(zombie.faceing().equals("right")){
				drawImage(femaleWalk[zombie.spriteFrame], zombie.getX(), zombie.getY(), zombie.getWidth(),zombie.getHeight());
			} else if(zombie.faceing().equals("left")){
				drawImage(femaleWalk[zombie.spriteFrame], zombie.getX()+160, zombie.getY(), (-zombie.getWidth()),zombie.getHeight());
			}
			break;
		
		case 2:
			//generate a random velcity for each zombie
			if(zombie.faceing().equals("right")){
				drawImage(femaleAttack[zombie.spriteFrame], zombie.getX(), zombie.getY(), zombie.getWidth(),zombie.getHeight());
			} else if(zombie.faceing().equals("left")){
				drawImage(femaleAttack[zombie.spriteFrame], zombie.getX()+160, zombie.getY(), (-zombie.getWidth()),zombie.getHeight());
			}
			break;
		case 3:
			//Zombie dead
			if(zombie.faceing().equals("right")){
				drawImage(femaleDead[zombie.spriteFrame], zombie.getX(), zombie.getY(), zombie.getWidth(),zombie.getHeight());
			} else if(zombie.faceing().equals("left")){
				drawImage(femaleDead[zombie.spriteFrame], zombie.getX()+160, zombie.getY(), (-zombie.getWidth()),zombie.getHeight());
			}
			break;
		}
		changeColor(black);
		//Draw zombie health
		drawSolidRectangle(zombie.getX(),zombie.getY(),zombie.zombieHealth, 5);
	}
	
	//function that check if any zombie is alive
	public boolean nextRound(){
		
		for(ZombieMale zombie : zombieMale){
			if(zombie.getStatus() != 3){//at least one zombie is alive
				return false;
			}
		} 
		
		for(ZombieFemale zombie : zombieFemale){
			if(zombie.getStatus() != 3){
				return false;
			}
		}
		//all zombies are dead, next round
		return true;
	}
	
	//Check player Score, each zombie killed, score + 10 
	public int checkScore(){
		int zombieLeft = 0;
		//check how many zombie left
		for(ZombieMale zombie : zombieMale){
			if(zombie.getStatus() != 3){ //zombie is not dead
				zombieLeft++;
			}
		}
		
		for(ZombieFemale zombie : zombieFemale){
			if(zombie.getStatus() != 3){
				zombieLeft++;
			}
		}
		//check how many zombie in total, and then minus zombie left to calculate how many zombies are killed
		return (zombieNumMale + zombieNumFemale - zombieLeft) *10; 
	}
	
	
	//Function to paint the options menu
		public void paintOptions() {
			if(backgroundNumber == 0){
				//draw background
				drawImage(darkBackground, 0, 0, width(), height());
				//draw title
				drawImage(title, 310, 120);
			}

			if(backgroundNumber== 1){
				//draw new background
				drawImage(whiteBackground, 0, 0);
				//draw title
				drawImage(title, 310, 120);
			}

			//Play
			if(menuOption == 0) {
				drawImage(muteHighlighted, 438, 200);
			} else {
				drawImage(mute,  438, 200);
			}

			//Options
			if(menuOption == 1) {
				drawImage(changeBackgroundHighlighted, 384, 250);
			} else {
				drawImage(changeBackground, 384, 250);
			}

			//Exit
			if(menuOption == 2) {
				drawImage(exitHighlighted, 501, 300);
			} else {
				drawImage(exit, 501, 300);
			}
		}
		
	public void paintMenu() {
		if(backgroundNumber == 0){
			//draw background
			drawImage(darkBackground, 0, 0, width(), height());
			//draw title
			drawImage(title, 310, 120);
		}
		
		if(backgroundNumber == 1){
			//draw new background
			drawImage(whiteBackground, 0, 0);
			//draw title
			drawImage(title, 310, 120);
		}
		//Play
		if(menuOption == 0) {
			drawImage(playHighlighted, 498, 200);
		} else {
			drawImage(play,  498, 200);
		}

		//Options
		if(menuOption == 1) {
			drawImage(optionsHighlighted, 473, 250);
		} else {
			drawImage(options, 473, 250);
		}

		//Exit
		if(menuOption == 2) {
			drawImage(exitHighlighted, 501, 300);
		} else {
			drawImage(exit, 501, 300);
		}
	}
	
	@Override
	public void update(double dt) {
		// TODO Auto-generated method stub
		
		if(gameState == GameState.Playing){ 

			if(explosionFrame >= 19){explosionFrame = 0;}
			explosionFrame ++;
			
			//Update player
			player.updateKnight(dt, 100, 100); 
			//Update each zombie
			for(ZombieMale zombie : zombieMale){
	
					if(player.getStatus() == 2){ // if palyer is attacking, check if zombie withink attack range
						player.attackDetection(zombie);
					}
					zombie.updateZombie(player, dt);
			}
			
			//Update each zombie
			for(ZombieFemale zombie : zombieFemale){
				
				if(player.getStatus() == 2){ // if palyer is attacking, check if zombie withink attack range
					player.attackDetectionFemale(zombie);
				}
				zombie.updateZombie(player, dt);
			}
			
			if(player.getStatus() == 4){ // game over
				playAudio(playerDeath, volume);
				playerScore += checkScore();
				gameState = GameState.Over;
				
			}
			
			if(nextRound()){ // check if going next round
				playAudio(warCry, volume); 
				playerScore += checkScore();
				gameState = GameState.NextRound;
				this.zombieNumMale = 2;
				this.zombieNumFemale = 2;
				this.zombieRandom++; //increase the number of zombies thats gonna be generated
				this.resetZombie(); // reset zombies
			}
			
		}
	}

	@Override
	public void paintComponent() {
		// TODO Auto-generated method stub
		if(gameState == GameState.Menu){
			paintMenu();
		} else if(gameState == GameState.Options){
			paintOptions();
		}
		else if(gameState == GameState.GameInstruction){
			drawImage(gameInstruction, 0,0);
		}
		else if (gameState == GameState.NextRound){
			drawImage(darkBackground, 0,0);
			drawImage(surviveTitle, 393,100);
			drawImage(scoreTitle, 475,187);
			drawImage(scoreBorder, 467, 250);
			drawImage(hint, 376,310);
			drawText(477,289, Integer.toString(playerScore));
		} 
		else if(gameState == GameState.Over){
			drawImage(gameOverBG, 0,0);
			drawImage(gameOver, 410, 120);
			drawImage(exitHighlighted, 410,220);
			drawImage(scoreBorder, 467, 290);
			drawText(477,329, Integer.toString(playerScore));
		}
		else if (gameState == GameState.Pause){
			drawImage(gameBackground, 0,0, 1100, 760);

			for(ZombieMale zombie : zombieMale){	
				drawZombieMale(zombie);
			}
			
			for(ZombieFemale zombie : zombieFemale){
				drawZombieFemale(zombie);	
			}
			
			drawPlayer();
			drawImage(pause, 473,280);
		}
		else if(gameState == GameState.Playing){
		
		drawImage(gameBackground, 0,0, 1100, 760);
		
		for(ZombieMale zombie : zombieMale){	
			drawZombieMale(zombie);
		}
		
		for(ZombieFemale zombie : zombieFemale){
			drawZombieFemale(zombie);	
		}
		
		drawPlayer();
		
		
		}
	}
	 public void keyPressedOptions(KeyEvent e) {
	    	
			if(e.getKeyCode() == KeyEvent.VK_W) {
				if(menuOption > 0) {
					menuOption--;
				}
			}
			if(e.getKeyCode() == KeyEvent.VK_S) {
				if(menuOption < 2) {
					menuOption++;
				}
			}
			//Enter Key
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				if(menuOption == 0) {
						//change sound
						if(volume > 0){
							volume = -50;
							stopAudioLoop(background);
						} else if(volume <0){
							volume = 1;
							background = loadAudio("pvzbackground.wav");
							startAudioLoop(background, volume);
						}
				} else if(menuOption == 1) {
					//change background
					if(backgroundNumber == 0){
						backgroundNumber= 1;
					}else if(backgroundNumber == 1){
						backgroundNumber = 0;
					}
					
				} else {
					//Exit
					gameState = GameState.Menu;
					menuOption = 0;
				}
			}
	    }
	 public void keyPressedMenu(KeyEvent e) {
			
			if(e.getKeyCode() == KeyEvent.VK_W) {
				if(menuOption > 0) {
					menuOption--;
				}
			}
			
			if(e.getKeyCode() == KeyEvent.VK_S) {
				if(menuOption < 2) {
					menuOption++;
				}
			}
			//Enter Key
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				if(menuOption == 0) {
					//Start Game
					gameState = GameState.GameInstruction;
					resetGame();
				} else if(menuOption == 1) {
					//Option Menu
					gameState = GameState.Options;
					menuOption = 0;
				} else {
					//Exit
					System.exit(0);
				}
			}
		}
	public void keyPressed(KeyEvent e){
		//Game Menu
		if(gameState == GameState.Menu)
		{
			keyPressedMenu(e);
			playAudio(beep, volume);
		} else if (gameState == GameState.Options){	
			keyPressedOptions(e);
			playAudio(beep, volume);
		}
		
		else if (gameState == GameState.Pause){
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
				gameState = GameState.Playing;
			}
		} else if(gameState == GameState.NextRound || gameState == GameState.GameInstruction){
			if(e.getKeyCode() == KeyEvent.VK_ENTER ){
				gameState = GameState.Playing;
				playAudio(zombieRoar, volume);
			}
		} else if (gameState == GameState.Over){
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				gameState = GameState.Menu;
			}
		}
		//game is playing
		else if(gameState == GameState.Playing){
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
				gameState = GameState.Pause;
			}
			if(e.getKeyCode() == KeyEvent.VK_D){
				if(player.getStatus() != 4){
					player.faceRight = true;
					player.faceLeft = false;
					player.left = false;
					player.right = true;
					player.setStatus(3);
					clip.loop(clip.LOOP_CONTINUOUSLY);

				}
			}
			
			if(e.getKeyCode() == KeyEvent.VK_A){
				if(player.getStatus() != 4){
					player.faceLeft = true;
					player.faceRight = false;
					player.left = true;
					player.right = false;
					player.setStatus(3);
					clip.loop(clip.LOOP_CONTINUOUSLY);
				}
			}
			
			if(e.getKeyCode() == KeyEvent.VK_W){
				if(player.getStatus() != 4){
					player.up = true;
					player.down = false;
					player.setStatus(3);
					clip.loop(clip.LOOP_CONTINUOUSLY);
				}
			}
			
			if(e.getKeyCode() == KeyEvent.VK_S){
				if(player.getStatus() != 4){
					player.up = false;
					player.down = true;
					player.setStatus(3);
					clip.loop(clip.LOOP_CONTINUOUSLY);
				}
			}
			
			if(e.getKeyCode() == KeyEvent.VK_SPACE){
				if(player.getStatus() != 4){
					player.setStatus(2);
					
				}
			}
		}
	}
	
	public void keyReleased(KeyEvent e){
		if(gameState == GameState.Playing){
			if(e.getKeyCode() == KeyEvent.VK_D){
				if(player.getStatus() != 4){
					player.right = false;
					player.setStatus(1);
					clip.stop();
				}
			}
			
			if(e.getKeyCode() == KeyEvent.VK_A){
				if(player.getStatus() != 4){
					player.left = false;
					player.setStatus(1);
					clip.stop();
				}
			}
			
			if(e.getKeyCode() == KeyEvent.VK_W){
				if(player.getStatus() != 4){
					player.up = false;
					player.setStatus(1);
					clip.stop();
				}
			}
			
			if(e.getKeyCode() == KeyEvent.VK_S){
				if(player.getStatus() != 4){
					player.down = false;
					player.setStatus(1);
					clip.stop();
				}
			}
			 if(e.getKeyCode() == KeyEvent.VK_SPACE){
				 if(player.getStatus() != 4){
					 player.setStatus(1);
					 clip2.stop();
				 }
			 }
		}
	}
}
