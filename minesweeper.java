	
package cs1302.p1;

import java.util.Scanner;
import java.util.Arrays;
import java.io.*;

/**
 * This class represents a Minesweeper game.
 * 
 * @author Ty Burns  tab26715@uga.edu
 */
public class Minesweeper {
    public static int rows;
    public static int cols;
    public static int row;//used for commands
    public static int col;//used for commands
    public static int grid[][];
    public static String user[][];
    public static int win=0;
    public static int minesTouching;
    public static int numMines;
    /**
     * Constructs an object instance of the {@link Minesweeper} class using the
     * information provided in <code>seedFile</code>. Documentation about the 
     * format of seed files can be found in the project's <code>README.md</code>
     * file.
     *
     * @param seedFile the seed file used to construct the game
     * @see            <a href="https://github.com/mepcotterell-cs1302/cs1302-minesweeper-alpha/blob/master/README.md#seed-files">README.md#seed-files</a>
     */
    public Minesweeper(File seedFile) {
	//	File seedFile
	BufferedReader reader;
	String dimensions="0";
	int mineNum=0;
	try{
	reader = new BufferedReader(new FileReader(seedFile));
	dimensions=reader.readLine();
	String[] dim=dimensions.split(" ");
	rows=Integer.parseInt(dim[0]);
	cols=Integer.parseInt(dim[1]);
	if(rows>10 || cols>10){
	    System.out.println("");
	    System.out.println("\u0CA0_\u0CA0 says, \"Cannot create a mine field with that many rows and/or columns!\"");
	    System.exit(0);
	}
	else if(rows<1 || cols<1){
	    System.out.println("");
	    System.out.println("\u0CA0_\u0CA0 says, \"Cannot create a mine field with that many rows and/or columns!\"");
	    System.exit(0);
	}
	grid=new int[rows][cols];
	for(int i=0;i<rows;i++){
	    for(int j=0;j<cols;j++){
		grid[i][j]=0;}         //populates grid with all 0s
	}
	numMines = Integer.parseInt(reader.readLine());
	for(int i=0;i<numMines;i++){
	    String mineLoc=reader.readLine();
	    String[] location=mineLoc.split(" ");
	    row=Integer.parseInt(location[0]);
	    col=Integer.parseInt(location[1]);
	    grid[row][col]=1; 
	}
}
	catch(Exception e){
	    System.out.println("Cannot create game with "+seedFile+" because it is not formatted correctly");
	    System.exit(0);
	}
	// TODO implement

    } // Minesweeper


    /**
     * Constructs an object instance of the {@link Minesweeper} class using the
     * <code>rows</code> and <code>cols</code> values as the game grid's number
     * of rows and columns respectively. Additionally, One quarter (rounded up) 
     * of the squares in the grid will will be assigned mines, randomly.
     *
     * @param rows the number of rows in the game grid
     * @param cols the number of cols in the game grid
     */
    public Minesweeper(int rows, int cols) {
	if(rows>10 || cols>10){
	    System.out.println("");
	    System.out.println("\u0CA0_\u0CA0 says, \"Cannot create a mine field with that many rows and/or columns!\"");
	    System.exit(0);
	}
	else if(rows<1 || cols<1){
	    System.out.println("");
	    System.out.println("\u0CA0_\u0CA0 says, \"Cannot create a mine field with that many rows and/or columns!\"");
	    System.exit(0);
	}
	grid=new int[rows][cols];
	for(int i=0;i<rows;i++){
	    for(int j=0;j<cols;j++){
		grid[i][j]=0;}         //populates grid with all 0s
	}
	numMines=(int)Math.ceil((rows*cols)*.25);
	for(int i=numMines;i>0;i--){
	    int ranRow=(int)(Math.random()*rows);
	    int ranCol=(int)(Math.random()*cols);
	    if(grid[ranRow][ranCol]==0)
		grid[ranRow][ranCol]=1;    //marks mines with 1
	    else
		i++;
	}

	// TODO implement
    } // Minesweeper
    

    /**
     * Starts the game and execute the game loop.
     */
    public void run() {
	Scanner com = new Scanner(System.in);
	int round=0;
	int comCode;
	setUserGrid(rows, cols);
	while(win==0){
	    System.out.println("Rounds completed: "+round);
	    displayGrid(rows, cols);
	    System.out.println("");
	    System.out.print("Enter command: ");
	   comCode=detectCommand(com.nextLine());
	   switch(comCode){
	   case 1:
	       help();
	       break;
	   case 2:
	       quit();
	       break;
	   case 3:
	       mark(row, col);
	       break;
	   case 4:
	      int revealTouch=reveal(row,col);
	      user[row][col]=(""+revealTouch);
	       break;
	   case 5:
	       guess(row, col);
	       break;
	   case 6:
	       System.out.println("");
	       System.out.println("\u0CA0_\u0CA0 says, \"Command not recognized! Invalid bounds\"");
	       help();
	       break;
	   default:
	       System.out.println("");
	       System.out.println("\u0CA0_\u0CA0 says, \"Command not recognized!\"");
	       break;
	   }//switch
	   winCheck();
	   if(win==1){
	       winner(round);}
	   else if(win==2){
	       displayGrid(rows,cols);
	       loser();}
	   round++;
	    
}

	// TODO implement

    } // run


    /**
     * The entry point into the program. This main method does implement some
     * logic for handling command line arguments. If two integers are provided
     * as arguments, then a Minesweeper game is created and started with a 
     * grid size corresponding to the integers provided and with 10% (rounded
     * up) of the squares containing mines, placed randomly. If a single word 
     * string is provided as an argument then it is treated as a seed file and 
     * a Minesweeper game is created and started using the information contained
     * in the seed file. If none of the above applies, then a usage statement
     * is displayed and the program exits gracefully. 
     *
     * @param args the shell arguments provided to the program
     */
    public static void main(String[] args) {
	System.out.print("        _\n");
	System.out.print("  /\\/\\ (_)_ __   ___  _____      _____  ___ _ __   ___ _ __\n");
	System.out.print(" /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\ '__|\n");
	System.out.print("/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |\n");
	System.out.print("\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|\n");
	System.out.print("                             ALPHA EDITION |_| v2017.f\n");
	

	/*
	  The following switch statement has been designed in such a way that if
	  errors occur within the first two cases, the default case still gets
	  executed. This was accomplished by special placement of the break
	  statements.
	*/

	Minesweeper game = null;

	switch (args.length) {

        // random game
	case 2: 

	    // int rows, cols;

	    // try to parse the arguments and create a game
	    try {
		rows = Integer.parseInt(args[0]);
		cols = Integer.parseInt(args[1]);
		game = new Minesweeper(rows, cols);
		break;
	    } catch (NumberFormatException nfe) { 
		// line intentionally left blank
	    } // try

	// seed file game
	case 1: 

	    String filename = args[0];
	    File file = new File(filename);

	    if (file.isFile()) {
		game = new Minesweeper(file);
		break;
	    } // if
    
        // display usage statement
	default:

	    System.out.println("Usage: java Minesweeper [FILE]");
	    System.out.println("Usage: java Minesweeper [ROWS] [COLS]");
	    System.exit(0);

	} // switch

	// if all is good, then run the game
	game.run();

    } // main
    public static void setUserGrid(int rows,int cols){
      	user=new String[rows][cols];
	for(int i=0;i<rows;i++){
	    for(int j=0;j<cols;j++){
		user[i][j]=(" ");}
	}}

    public static void revealGrid(int rows,int cols,int[][] grid){
	//System.out.print(" "+rows+" "+cols+" "+grid[5][5]);
	System.out.println("");
	for(int x=0;x<rows;x++){
	    System.out.print(" "+x);  
	        for(int y=0;y<cols;y++){
		    if(y==(cols-1)){
			System.out.println(" | "+grid[x][y]+" |");}
		    else
			System.out.print(" | "+grid[x][y]);}}
	System.out.print("     ");
	for(int i=0;i<cols;i++){
	System.out.print(i+"   ");}}
    public static void mark(int row,int col){
	try{
	    user[row][col]="F";}//try
	catch(Exception e){
	    System.out.println("Command out of bounds");
	}//catch
    }//mark
    public static void guess(int row,int col){
	try{
	    user[row][col]="?";}//try
	catch(Exception e){
	    System.out.println("Command out of bounds");
	}//catch

    }//guess
    public static void help(){
	System.out.println("");
	System.out.print("Commands Available...\n - Reveal: r/reveal row col\n -   Mark: m/mark   row col\n -  Guess: g/guess  row col\n -   Help: h/help\n -   Quit: q/quit\n");
    }//help
    public static void quit(){
	System.out.println("");
	System.out.println("\u10DA(\u0CA0_\u0CA0\u10DA)");
	System.out.println("Y U NO PLAY MORE?");
	System.out.println("Bye!");
	System.exit(0);
    }//quit
    public static void displayGrid(int rows,int cols){
	for(int i=0;i<rows;i++){
	    System.out.print(" "+i);  
	        for(int j=0;j<cols;j++){
		    if(j==(cols-1)){
			System.out.println(" | "+user[i][j]+" |");}
		    else
			System.out.print(" | "+user[i][j]);}}
	System.out.print("     ");
	for(int x=0;x<cols;x++){
	System.out.print(x+"   ");}
    }//displayGrid

    public static int detectCommand(String command){
	int cmnd=0;
	String commandTrim=command.trim();
	String[] arr = commandTrim.split("[ ]+");
	if(arr.length==1){
	    String proCmnd = Arrays.toString(arr);
	    proCmnd = proCmnd.substring(1,proCmnd.length()-1);
	    if(proCmnd.equalsIgnoreCase("help")||proCmnd.equalsIgnoreCase("h")){
		cmnd=1;}
	    else if(proCmnd.equalsIgnoreCase("quit")||proCmnd.equalsIgnoreCase("q")){
		cmnd=2;}
	    else
		cmnd=6;}
	if(arr.length==3){
	    String proCmnd2 =(arr[0].trim());
	    row=Integer.parseInt(arr[1].trim());
	    col=Integer.parseInt(arr[2].trim());
	    if(row>=rows || col>=cols || row<0 || col<0)
		cmnd=6;
	   else if(proCmnd2.equalsIgnoreCase("mark")||proCmnd2.equalsIgnoreCase("m")){
		cmnd=3;}
	    else if(proCmnd2.equalsIgnoreCase("reveal")||proCmnd2.equalsIgnoreCase("r")){
		cmnd=4;}
	    else if(proCmnd2.equalsIgnoreCase("guess")||proCmnd2.equalsIgnoreCase("g")){
		cmnd=5;}
	    else
		cmnd=6;
	} return cmnd;}//detectCommand

    public static int reveal(int row, int col){
	minesTouching=0;
	if(row>=rows || col>=cols || row<0 || col<0){
	    System.out.println("Command out of bounds");
	}
	else if(grid[row][col]==1){
	    win=2;
	    user[row][col]="*";}
	else {
	    if(row==0 && col==0){
		for(int i=0;i<=2;i++){
		    for(int j=0;j<=2;j++){
			if(grid[i][j]==1){
			    minesTouching++;}
		    }
		}
	    }//if row==0 and col==0
	  else if(row==0 && col!=0 && col!=cols-1){
		for(int i=0;i<=2;i++){
		    for(int j=col-1;j<=col+1;j++){
			if(grid[i][j]==1){
			    minesTouching++;}
		    }
		}
	    }//else if row==0
	    else if(col==0 && row!=0 && row!=rows-1){
		for(int i=row-1;i<=row+1;i++){
		    for(int j=0;j<=2;j++){
			if(grid[i][j]==1){
			    minesTouching++;}
		    }
		}
	    }//else if col==0
	    else if(row==rows-1 && col==cols-1){
		for(int i=row-1;i<=row;i++){
		    for(int j=col-1;j<=col;j++){
			if(grid[i][j]==1){
			    minesTouching++;}
		    }
		}
	    }//else if row==rows-1 and col==cols-1
	    else if(col==0 && row==rows-1){
		for(int i=row-1;i<=row;i++){
		    for(int j=0;j<=2;j++){
			if(grid[i][j]==1){
			    minesTouching++;}
		    }
		}
	    }//else if col==0 && row==rows-1
	    else if(row==0 && col==cols-1){
		for(int i=0;i<=2;i++){
		    for(int j=col-1;j<=col;j++){
			if(grid[i][j]==1){
			    minesTouching++;}
		    }
		}
	    }//else if row==0 and col==cols-1
	    else if(col==cols-1 && row!=0 && row!=rows-1){
		for(int i=row-1;i<=row+1;i++){
		    for(int j=col-1;j<=col;j++){
			if(grid[i][j]==1){
			    minesTouching++;}
		    }
		}
	    }//else if col==cols-1
	    else if(row==rows-1 && col!=0 && col!=cols-1){
		for(int i=row-1;i<=row;i++){
		    for(int j=col-1;j<=col+1;j++){
			if(grid[i][j]==1){
			    minesTouching++;}
		    }
		}
	    }//else if row==rows-1
	    else{
	      	for(int i=row-1;i<=row+1;i++){
		    for(int j=col-1;j<=col+1;j++){
			if(grid[i][j]==1){
			    minesTouching++;}
		    }
		}
	    }//else
	}//else
    return minesTouching;
    }//reveal
    public static void winner(int round){
	int score=(rows*cols)-numMines-round;
	System.out.println("");
	/*	System.out.print("¦¦¦¦¦¦¦¦¦_¦¦¦¦¦¦¦¦¦¦¦¦¦¦_¦¦¦¦ So Doge\n
 ¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦_¯¦¦¦¦¦\n
 ¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦_¯¦¦¦¦¦¦¦ Such Score\n
 ¦¦¦¦¦¦¦¦_¯¦¦¯¯¯¯___¯¦¦¦¦¦¦¦¦¦\n
 ¦¦¦¦¦__¯¦¦¦¦¦¦¦¦¦¦¦¦¦¦_¦¦¦¦¦¦ Much Minesweeping\n
 ¦¦¦_¯¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¯¦¦¯¦¦¦¦¦\n
 ¦¦¦¦¦¦__¦¦¦¦¦¦¦¦¦¦¦¦¦¦¯_¦¦¦¦¦ Wow\n
 ¦¦¦¦¦¦¦¯¦¦¦¦¦_¯¦_¦¦¦¦¦¦¦¦¦¦¦¦\n
 ¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¯¦¦¦¦¦¦¦¦¯_¦¦\n
 ¦¦¦¦_¦¦_¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦\n
 ¯¦¯¦_¦_¦¦_¦¯¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦\n
 ¦¦¦¦¯¦¯¦¦__¦_¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦\n
 ¦¦¦¦¯¯__¦¦¦_¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦\n
 ¦¦¦¦¦¦¦¦¯¯¯¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦\n
 ¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦_¦¦¦¦¦\n
 ¦¦¯_¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦_¦¦¦¦¦¦¦\n
 ¦¦¦¦¯_¦¦¦¦¦¦¦¦¦¦___¯¦¦¦¦_¯¦¦¦ CONGRATULATIONS!\n
 ¦¦¦¦¦¦¯______¯¯¯¦¦¦¦¦__¯¦¦¦¦¦ YOU HAVE WON!\n
 ¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¯¯¦¦¦¦¦¦¦");*/
	System.out.print("You've won! Doge wishes he was here but he wont compile! ");
	System.out.println("SCORE: "+score);
        System.exit(0);}
    public static void loser(){
	System.out.println("");
	System.out.println(" Oh no... You revealed a mine!");
	System.out.println("  __ _  __ _ _ __ ___   ___    _____   _____ _ __ ");
	System.out.println(" / _` |/ _` | '_ ` _ \\ / _ \\  / _ \\ \\ / / _ \\ '__|");
	System.out.println("| (_| | (_| | | | | | |  __/ | (_) \\ V /  __/ |   ");
	System.out.println(" \\__, |\\__,_|_| |_| |_|\\___|  \\___/ \\_/ \\___|_|   ");
	System.out.println(" |___/                                            \r\n");
	System.exit(0);}
    public static void winCheck(){
	int minesFlagged=0;
	for(int i=0;i<rows;i++){
	    for(int j=0;j<cols;j++){
		if(grid[i][j]==1 && user[i][j].equals("F")){
		    minesFlagged++;
		}}	
		if(minesFlagged==numMines){
		    win=1;
		}
	    
	}
    }

    } // Minesweeper
