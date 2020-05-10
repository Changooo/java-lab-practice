import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.LinkedHashMap;
import java.util.HashMap;
class Board{
	
}
class BombInputException extends RuntimeException{
	
}
class ModeInputException extends RuntimeException{
	
}
class HitException extends RuntimeException{
	
}

class Ship{
	public Ship(int n, int s) {
		num = n; size = s;
	}
	private int size;
	private int num;
	
	int getSize() { return size; }
	int getNum() { return num; }
}

class AircraftCarrier extends Ship{
	AircraftCarrier(int n){super(n, 6);}
}
class Battleship extends Ship{
	Battleship(int n){super(n, 4);}
}
class Submarine extends Ship{
	Submarine(int n){super(n, 3);}
}
class Destroyer extends Ship{
	Destroyer(int n){super(n, 3);}
}
class PatrolBoat extends Ship{
	PatrolBoat(int n){super(n, 2);}
}

public class PA2{
	public static void main(String[] args) throws IOException, BombInputException, ModeInputException, HitException {
		Scanner sc = new Scanner(System.in);
		int bomb = sc.nextInt();
		if(!(bomb > 0)) throw new BombInputException();
		char mode = sc.next().charAt(0);
		String file_path = sc.next();
		char[][] board = new char[10][10];
		LinkedHashMap<Character, Ship> ships = new LinkedHashMap<Character, Ship>();
		ships.put('A', new AircraftCarrier(1));
		ships.put('B', new Battleship(2));
		ships.put('S', new Submarine(2));
		ships.put('D', new Destroyer(1));
		ships.put('P', new PatrolBoat(4));
		try {
			InputStream fis = new FileInputStream ("C:\\Users\\cgl12\\OneDrive\\πŸ≈¡ »≠∏È\\"+file_path);
			InputStreamReader isr = new InputStreamReader (fis); 
			BufferedReader br = new BufferedReader(isr);
			String board_line;
			for(int i=0; i<10; i++){
				board_line = br.readLine();
				for(int j=0; j<10; j++) {
					board[i][j] = j<board_line.length() ? board_line.charAt(j) : ' ';
				}
			}
			br.close();isr.close();fis.close();
			
		} catch(IOException e){
			Random rd = new Random();
			int row, col; 
			boolean is_hor;
			for(int i=0; i<10; i++){
				for(int j=0; j<10; j++) {
					board[i][j] = ' ';
				}
			}
			for(char key : ships.keySet()) {
				int ship_size = ships.get(key).getSize();
				for(int x=0; x<ships.get(key).getNum(); x++) {
					is_hor = rd.nextBoolean();
					row = rd.nextInt(10 - (!is_hor?1:0)*ship_size);
					col = rd.nextInt(10 - ( is_hor?1:0)*ship_size);
					for(int i=0; i<ship_size; i++) {
						int row_plus  = (row+(!is_hor?1:0)*i < 10-1) ? 1 : 0;
						int col_plus  = (col+( is_hor?1:0)*i < 10-1) ? 1 : 0;
						int row_minus = (row+(!is_hor?1:0)*i > 0   ) ? 1 : 0;
						int col_minus = (col+( is_hor?1:0)*i > 0   ) ? 1 : 0;
						
						if(board[row+(!is_hor?1:0)*i   		   ][col+(is_hor?1:0)*i   		  ] != ' ' ||			//center
						   board[row+(!is_hor?1:0)*i -row_minus][col+(is_hor?1:0)*i   		  ] != ' ' ||			//up
						   board[row+(!is_hor?1:0)*i +row_plus ][col+(is_hor?1:0)*i  		  ] != ' ' ||			//down
						   board[row+(!is_hor?1:0)*i   		   ][col+(is_hor?1:0)*i +col_plus ] != ' ' ||			//right
						   board[row+(!is_hor?1:0)*i   		   ][col+(is_hor?1:0)*i -col_minus] != ' ' ||			//left
						   board[row+(!is_hor?1:0)*i -row_minus][col+(is_hor?1:0)*i +col_plus ] != ' ' ||			//upper right
						   board[row+(!is_hor?1:0)*i -row_minus][col+(is_hor?1:0)*i -col_minus] != ' ' ||			//upper left
						   board[row+(!is_hor?1:0)*i +row_plus ][col+(is_hor?1:0)*i +col_plus ] != ' ' ||			//lower right
						   board[row+(!is_hor?1:0)*i +row_plus ][col+(is_hor?1:0)*i -col_minus] != ' ') {			//lower left

							is_hor = rd.nextBoolean();
							row = rd.nextInt(10 - (!is_hor?1:0)*ship_size);
							col = rd.nextInt(10 - ( is_hor?1:0)*ship_size);
							i = -1;
							continue;
						}
					}
					for(int i=0; i<ship_size; i++) {
						board[row+(!is_hor?1:0)*i][col+(is_hor?1:0)*i] = key;
					}
				}
			}
		}
		if(mode == 'd' || mode == 'D') { 		// Debug mode
			String shot;
			int shot_row, shot_col;
			int score = 0;
			for(int i=0; i<10; i++){
				if(i==0) {
					System.out.println("     A  B  C  D  E  F  G  H  I  J  ");
					System.out.println("     -  -  -  -  -  -  -  -  -  -  ");
				}
				System.out.print((i+1) + " " + (i+1==10 ? "| " : " | "));
				for(int j=0; j<10; j++) {
					System.out.print(board[i][j]+"  ");
				}
				System.out.println("  ");
			}
			for(int n=0; n<bomb; n++) {
				shot = sc.next();
				shot_col = ((int)(shot.charAt(0)))-65;
				shot_row = Integer.parseInt(shot.substring(1))-1;
				try {
					if(board[shot_row][shot_col] == ' ') {
						System.out.println("Miss");
						board[shot_row][shot_col] = 'X';
					}
					else if(board[shot_row][shot_col] == 'X' || ((int)board[shot_row][shot_col] >= 97 && (int)board[shot_row][shot_col] <= 122)) {
						throw new HitException();				
					}
					else {
						System.out.println("Hit "+board[shot_row][shot_col]);
						score+=ships.get(board[shot_row][shot_col]).getSize();
						board[shot_row][shot_col] = Character.toLowerCase(board[shot_row][shot_col]); 
					}
					for(int i=0; i<10; i++){
						if(i==0) {
							System.out.println("     A  B  C  D  E  F  G  H  I  J  ");
							System.out.println("     -  -  -  -  -  -  -  -  -  -  ");
						}
						System.out.print((i+1) + " " + (i+1==10 ? "| " : " | "));
						for(int j=0; j<10; j++) {
							if((int)board[i][j] >= 97 && (int)board[i][j] <= 122) {
								System.out.print("X" +board[i][j]+" ");
							}
							else System.out.print(board[i][j]+"  ");
						}
						System.out.println("  ");
					}
				} catch(HitException e) {
					System.out.println("Try again");
					n--;
				}
			}
			System.out.println("Score "+score);
		}
		else if(mode == 'r' || mode == 'R') {	// Release mode
			String shot;
			int shot_row, shot_col;
			int score = 0;
			for(int n=0; n<bomb; n++) {
				shot = sc.next();
				shot_col = ((int)(shot.charAt(0)))-65;
				shot_row = Integer.parseInt(shot.substring(1))-1;
				try {
					if(board[shot_row][shot_col] == ' ') {
						System.out.println("Miss");
						board[shot_row][shot_col] = 'X';
					}
					else if(board[shot_row][shot_col] == 'X' || ((int)board[shot_row][shot_col] >= 97 && (int)board[shot_row][shot_col] <= 122)) {
						throw new HitException();				
					}
					else {
						System.out.println("Hit "+board[shot_row][shot_col]);
						score+=ships.get(board[shot_row][shot_col]).getSize();
						board[shot_row][shot_col] = Character.toLowerCase(board[shot_row][shot_col]); 
					}
				} catch(HitException e) {
					System.out.println("Try again");
					n--;
				}
			}
			for(int i=0; i<10; i++){
				if(i==0) {
					System.out.println("     A  B  C  D  E  F  G  H  I  J  ");
					System.out.println("     -  -  -  -  -  -  -  -  -  -  ");
				}
				System.out.print((i+1) + " " + (i+1==10 ? "| " : " | "));
				for(int j=0; j<10; j++) {
					if((int)board[i][j] >= 97 && (int)board[i][j] <= 122) {
						System.out.print("X" +board[i][j]+" ");
					}
					else System.out.print(board[i][j]+"  ");
				}
				System.out.println("  ");
			}
			System.out.println("Score "+score);

		}
		else throw new ModeInputException();
	}
}