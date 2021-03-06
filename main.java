import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class main implements ActionListener, MouseListener, MouseMotionListener, KeyListener, FocusListener{
	//PROPERTIES
	JFrame theframe = new JFrame("can this work thanks");
	animation thepanel = new animation();
	Timer thetimer = new Timer(1000/60, this);
	// Game screen
	JTextArea chatArea = new JTextArea();
	JScrollPane thescroll = new JScrollPane(chatArea);
	JTextField chat = new JTextField();
	JButton send = new JButton("Send");
	JButton darkON = new JButton("ON");
	JButton darkOFF = new JButton("OFF");
	JButton lockIn = new JButton("LOCK IN");
	// draw map methods
	static String[][] strBoard;
	// Settings
	JTextField portNumber = new JTextField();
	JTextField serverIP = new JTextField();
	// Connection Screen
	JTextField username = new JTextField("Username");
	JTextField portIPConnect = new JTextField("Port #,IP Address");
	SuperSocketMaster ssm;
	
	// the game logic
	int intTurn = 1;
	String strTemp[];
	
	PrintWriter connections = null;
	
	String strUsername = "";
	String strIP = "";
	int intPort = 3000;
	String portIP[] = new String[2];
		
	//METHODS
	public void actionPerformed(ActionEvent evt){
		if(evt.getSource() == thetimer){
			thepanel.repaint();
		// chat background turns black, text turns white
		}else if(evt.getSource() == darkON){
			thepanel.blnDarkMode = true;
			darkON.setEnabled(false);
			darkOFF.setEnabled(true);
			chatArea.setBackground(Color.BLACK);
			chatArea.setForeground(Color.WHITE);
			thescroll.setBackground(Color.BLACK);
			thescroll.setForeground(Color.WHITE);
			chat.setBackground(Color.BLACK);
			chat.setForeground(Color.WHITE);
		// chat background turns white, text turns black
		}else if(evt.getSource() == darkOFF){
			thepanel.blnDarkMode = false;
			darkON.setEnabled(true);
			darkOFF.setEnabled(false);
			chatArea.setBackground(Color.WHITE);
			chatArea.setForeground(Color.BLACK);
			thescroll.setBackground(Color.WHITE);
			thescroll.setForeground(Color.BLACK);
			chat.setBackground(Color.WHITE);
			chat.setForeground(Color.BLACK);
		}else if(evt.getSource() == lockIn){
			lockIn.setVisible(false);
			thepanel.blnLockedIn = true; 
			// adding the components of the chat onto the panel
			thepanel.add(thescroll);
			thepanel.add(send);
			thepanel.add(chat);
			thepanel.add(darkON);
			thepanel.add(darkOFF);
		}else if(evt.getSource() == send){
			ssm.sendText(chat.getText());
			chatArea.append("me: "+chat.getText()+"\n");
			chat.setText("");
		}else if(evt.getSource() == username){
			strUsername = username.getText();
		}else if(evt.getSource() == portIPConnect){
			
		}else if(evt.getSource() == ssm){
			chatArea.append("opponent: "+ssm.readText() + "\n");
		}
	}
	public void mouseMoved(MouseEvent evt){
	}
	public void mouseDragged(MouseEvent evt){
		// if the game just started and the players are rearranging their pieces on the board
		if(thepanel.blnGameStart == true){
			
		// if the player has locked in their pieces
		}else if(thepanel.blnLockedIn == true){
			// if it's the server's turn
			if(intTurn == 1){
				// stuff happens and then: 
				intTurn = 2;
			// if it's the client's turn 
			}else if(intTurn == 2){
				// stuff happens and then:
				intTurn = 1;
			}
		}
    }
	public void mouseExited(MouseEvent evt){
	}
	public void mouseEntered(MouseEvent evt){
	}
	public void mousePressed(MouseEvent evt){
	}
	public void mouseReleased(MouseEvent evt){
	}
	public void mouseClicked(MouseEvent evt){
		// user on main menu page
		if(thepanel.blnMainMenu == true){
			// going to settings page
			if(evt.getX() >= 180 && evt.getX() <= 440 && evt.getY() >= 590 && evt.getY() <= 670){
				thepanel.blnSettings = true;
				thepanel.blnMainMenu = false;
				// adding the text fields to the panel
				thepanel.add(portNumber);
				thepanel.add(serverIP);
				portNumber.setVisible(true);
				serverIP.setVisible(true);
			// going to the help screens
			}else if(evt.getX() >= 860 && evt.getX() <= 1120 && evt.getY() >= 590 && evt.getY() <= 670 && thepanel.blnSettings == false){
				thepanel.blnHelp1 = true;
				thepanel.blnMainMenu = false;
			// going to play
			}else if(evt.getX() >= 500 && evt.getX() <= 800 && evt.getY() >= 520 && evt.getY() <= 670){
				thepanel.blnMainMenu = false;
				// going to connection screen
				if(thepanel.blnGameInProgress == false){
					thepanel.blnConnect = true;
					thepanel.add(username);
					thepanel.add(portIPConnect);
					username.setForeground(Color.GRAY);// username placeholder
					portIPConnect.setForeground(Color.GRAY);// port and IP placeholder
				// resuming game after already having connected
				}else{
					thepanel.blnGameStart = true;
					thescroll.setVisible(true);
					send.setVisible(true);
					chat.setVisible(true);
					darkON.setVisible(true);
					darkOFF.setVisible(true);
					if(thepanel.blnLockedIn == true){
						lockIn.setVisible(true);
					}
				}
				
			}
		// user on settings page
		}else if(thepanel.blnSettings == true){
			// going back to the main menu
			if(evt.getX() >= 993 && evt.getX() <= 1201 && evt.getY() >= 26 && evt.getY() <= 81){
				thepanel.blnMainMenu = true;
				thepanel.blnSettings = false;
				// copying text field contents to a text file
				try{
					connections = new PrintWriter(new FileWriter("connections.txt"));
					connections.println(portNumber.getText());
					connections.println(serverIP.getText());
					connections.close();
				}catch(IOException e){
					System.out.println("IOException in settings page");
				}
				// getting rid of the text fields
				portNumber.setVisible(false);
				serverIP.setVisible(false);
			}
		// user on the first help screen 
		}else if(thepanel.blnHelp1 == true){
			thepanel.blnSettings = false;
			// going to next help screen
			if(evt.getX() >= 1178 && evt.getX() <= 1232 && evt.getY() >= 608 && evt.getY() <= 682){
				thepanel.blnHelp2 = true;
				thepanel.blnHelp1 = false;
			}
		// user on second help screen
		}else if(thepanel.blnHelp2 == true){
			// going to previous help screen
	     	if(evt.getX() >= 1178 && evt.getX() <= 1232 && evt.getY() >= 608 && evt.getY() <= 682){
				thepanel.blnHelp3 = true;
				thepanel.blnHelp2 = false;
			// going to next help screen
			}else if(evt.getX() >= 47 && evt.getX() <= 101 && evt.getY() >= 608 && evt.getY() <= 682){
				thepanel.blnHelp1 = true;
				thepanel.blnHelp2 = false;
			}
		// user on third help screen
		}else if(thepanel.blnHelp3 == true){
			// going to previous help screen
			if(evt.getX() >= 1178 && evt.getX() <= 1232 && evt.getY() >= 608 && evt.getY() <= 682){
				thepanel.blnHelp4 = true;
				thepanel.blnHelp3 = false;
			// going to next help screen
			}else if(evt.getX() >= 47 && evt.getX() <= 101 && evt.getY() >= 608 && evt.getY() <= 682){
				thepanel.blnHelp2 = true;
				thepanel.blnHelp3 = false;
			}
		// user on fourth help screen
		}else if(thepanel.blnHelp4 == true){
			// going to previous help screen
			if(evt.getX() >= 1012 && evt.getX() <= 1217 && evt.getY() >= 44 && evt.getY() <= 108){
				thepanel.blnMainMenu = true;
				thepanel.blnHelp4 = false;
			// going to main menu
			}else if(evt.getX() >= 47 && evt.getX() <= 101 && evt.getY() >= 608 && evt.getY() <= 682){
				thepanel.blnHelp3 = true;
				thepanel.blnHelp4 = false;
			}
		}else if(thepanel.blnConnect == true){
			// if user clicks play and they have filled in the connection requirements
			if(evt.getX() >= 580 && evt.getX() <= 770 && evt.getY() >= 420 && evt.getY() <= 480 && !username.getText().equals("Username") && !portIPConnect.getText().equals("Port #, IP Address")){
				// getting the port # and IP from the text fields
				portIP = portIPConnect.getText().split(",");
				intPort = Integer.parseInt(portIP[0]);
				strIP = portIP[1];
				System.out.println(intPort+strIP);
				
				thepanel.blnGameStart = true;
				thepanel.blnConnect = false;
				username.setVisible(false);
				portIPConnect.setVisible(false);
				thepanel.add(lockIn);
			
			// if player clicks on server
			}else if(evt.getX() >= 470 && evt.getX() <= 670 && evt.getY() >= 210 && evt.getY() <= 260){// if user clicks server and enters information
						thepanel.blnConnectionClientPressed = false;
						thepanel.blnConnectionServerPressed = true;
						animation.blnServer = true;
						strBoard = Board(animation.blnServer);
			// if player clicks on client
			}else if(evt.getX() >= 690 && evt.getX() <= 890 && evt.getY() >= 210 && evt.getY() <= 260){// if user clicks client and enters information
						thepanel.blnConnectionServerPressed = false;
						thepanel.blnConnectionClientPressed = true;
						animation.blnServer = false;
						strBoard = Board(animation.blnServer);
			}
			
		// user on the game screen
		}else if(thepanel.blnGameStart == true){ 
			// going back to the main menu
			if(evt.getX() >= 0 && evt.getX() <= 200 && evt.getY() >= 0 && evt.getY() <= 50){
				thepanel.blnMainMenu = true;
				thepanel.blnGameStart = false;
				thepanel.blnGameInProgress = true;
				thepanel.blnPieceSelected = false;
				// removing components of the chat from game screen
				thescroll.setVisible(false);
				send.setVisible(false);
				chat.setVisible(false);
				darkON.setVisible(false);
				darkOFF.setVisible(false);
				lockIn.setVisible(false);
			}else if(thepanel.blnPieceSelected == false){
				
				// piece selection and moving process
				for(int row = 0; row < 8; row++){// rows of 2d array
					for(int col = 0; col < 9; col++){ // columns of 2d array
						if(evt.getX() >= (col * 75) + 70 && evt.getX() <= (col * 75) + 135 && evt.getY() >= (row * 75) + 70 && evt.getY() <= (row * 75) + 135){// if within board
							if(!strBoard[row][col].equals("N/N/N") && thepanel.blnPieceSelected == false){// is clicked box not empty and hasn't been selected
								String strTemp = strBoard[row][col];// save piece in temp value
								System.out.println(strBoard[row][col]);
								// draw seletion box on selected cell
								thepanel.intSelectX = (col * 75) + 60;
								thepanel.intSelectY = (row * 75) + 60;
								thepanel.blnPieceSelected = true;
								thepanel.curCol = col; 
								thepanel.curRow = row;
								break;
							}
						}
					}
				}
			}else if(thepanel.blnPieceSelected == true){
				for(int row = 0; row < 8; row++){
					for(int col = 0; col < 9; col++){
						// if next box clicked is box to right of selected one and user can move right
						if((thepanel.curCol + 1) < 9 && evt.getX() >= ((thepanel.curCol + 1) * 75) + 70 && evt.getX() <= ((thepanel.curCol + 1) * 75) + 135 && evt.getY() >= (thepanel.curRow * 75) + 70 && evt.getY() <= (thepanel.curRow * 75) + 135){
							System.out.println(strBoard[thepanel.curRow][thepanel.curCol + 1]);
							thepanel.nextRow = thepanel.curRow;
							thepanel.nextCol = thepanel.curCol + 1;
							thepanel.blnPieceRight = true;
							break;
						// user moves left and selected spot isn't the left most one 
						}else if((thepanel.curCol - 1) >= 0 && evt.getX() >= ((thepanel.curCol - 1) * 75) + 70 && evt.getX() <= ((thepanel.curCol - 1) * 75) + 135 && evt.getY() >= (thepanel.curRow * 75) + 70 && evt.getY() <= (thepanel.curRow * 75) + 135){
							System.out.println(strBoard[thepanel.curRow][thepanel.curCol - 1]);
							thepanel.nextRow = thepanel.curRow;
							thepanel.nextCol = thepanel.curCol - 1;
							thepanel.blnPieceLeft = true;
							break;
						// user moves down and selected spot isn't the lowest one 	
						}else if((thepanel.curRow + 1) < 8 && evt.getX() >= (thepanel.curCol * 75) + 70 && evt.getX() <= (thepanel.curCol * 75) +  135 && evt.getY() >= ((thepanel.curRow + 1) * 75) + 70 && evt.getY() <= ((thepanel.curRow + 1) * 75) + 135){
							System.out.println(strBoard[thepanel.curRow + 1][thepanel.curCol]);
							thepanel.nextRow = thepanel.curRow + 1;
							thepanel.nextCol = thepanel.curCol;
							thepanel.blnPieceDown = true;
							break;	
						// user moves up and selected spot isn't the highest one
						}else if((thepanel.curRow - 1) >= 0 && evt.getX() >= (thepanel.curCol * 75) + 70 && evt.getX() <= (thepanel.curCol * 75) +  135 && evt.getY() >= ((thepanel.curRow - 1) * 75) + 70 && evt.getY() <= ((thepanel.curRow - 1) * 75) + 135){
							System.out.println(strBoard[thepanel.curRow - 1][thepanel.curCol]);
							thepanel.nextRow = thepanel.curRow - 1;
							thepanel.nextCol = thepanel.curCol;
							thepanel.blnPieceUp = true;
							break;
						}
					}
					break;
				}	
				
			}
		}
	}
	public void keyReleased(KeyEvent evt){
	}
	public void keyTyped(KeyEvent evt){
	}
	public void keyPressed(KeyEvent evt){
	}
	// methods for placeholder text in the jtextfields
	public void focusGained(FocusEvent evt){
		if(username.getText().equals("Username") && username.isFocusOwner() == true){
			username.setText("");
			username.setForeground(Color.BLACK);
		}else if(portIPConnect.getText().equals("Port #,IP Address") && portIPConnect.isFocusOwner() == true){
			portIPConnect.setText("");
			portIPConnect.setForeground(Color.BLACK);
		}
	}
	public void focusLost(FocusEvent evt){
		if(username.getText().equals("")){
			username.setForeground(Color.GRAY);
			username.setText("Username");
		}
		if(portIPConnect.getText().equals("")){
			portIPConnect.setForeground(Color.GRAY);
			portIPConnect.setText("Port #,IP Address");
		}
	}
	//returns array representative of requested view of board
	public static String[][] Board(boolean server){
		String strLine, strSplit[] = new String[9];
		String[][] serverBoard = new String[8][9], clientBoard = new String[8][9];// server = white, client = black
		//read csv to array
		try{	
			BufferedReader file = new BufferedReader(new FileReader("csv.csv"));	
			for(int row = 0; row < 8; row++){
				strLine = file.readLine();
				strSplit = strLine.split(",");
				for(int col = 0; col < 9; col++){
					clientBoard[row][col] = strSplit[col];
					serverBoard[7-row][8-col] = strSplit[col];
				}
			}
			file.close();
		}catch (IOException e){
			System.out.println("Error loading file");
		}
		if(server == true){
			return serverBoard;
		}else{
			return clientBoard;
		}
	}
	public static void nextDetect(String[][] strBoard, int nextRow, int nextCol, int curRow, int curCol, boolean blnServer){
		String[] strPieceID = new String[3], strNextID = new String[3];// get id of selected piece and next piece
		String strOppSide = "";// initialize opponent side
		strPieceID = strBoard[curRow][curCol].split("/");// find id of selected piece
		strNextID = strBoard[nextRow][nextCol].split("/");// find id of cell you want to move to
		String strTemp = "";// store current cell id for moving
		System.out.println(strNextID[0]+strNextID[1]+strNextID[2]);
		if(blnServer == true) strOppSide = "B";// if you're server, your opponent's colour is black
		else strOppSide = "W";// if client, your opponent's colour is white
		
		if(!strNextID[2].equals(strPieceID[2]) && !strNextID[0].equals("N")){// if next cell contains enemy
			int nextRank = Integer.parseInt(strNextID[0]);
			int ownRank = Integer.parseInt(strPieceID[0]);
			if(!strNextID[0].equals(strPieceID[0])){// if the pieces aren't equivalent
				if(nextRank == 0 && ownRank != 0){// if enemy is flag
					animation.blnwin = true;
					animation.blnPieceSelected = false;
				}else if(ownRank == 0 && nextRank != 0){// if you're flag
					animation.blnloss = true;
					animation.blnPieceSelected = false;
				}else if(nextRank == 1 && ownRank == 2){// if enemy is spy and you're private
					strTemp = strBoard[curRow][curCol];
					strBoard[curRow][curCol] = "N/N/N";
					strBoard[nextRow][nextCol] = strTemp;
					animation.blnPieceSelected = false;
				}else if(nextRank == 1 && ownRank != 2){// if enemy is spy and you're not private
					strTemp = strBoard[nextRow][nextCol];
					strBoard[nextRow][nextCol] = "N/N/N";
					strBoard[curRow][curCol] = strTemp;
					animation.blnPieceSelected = false;
				}else if(ownRank == 1 && nextRank == 2){// if you're spy and enemy is private
					strTemp = strBoard[nextRow][nextCol];
					strBoard[nextRow][nextCol] = "N/N/N";
					strBoard[curRow][curCol] = strTemp;
					animation.blnPieceSelected = false;
				}else if(ownRank == 1 && nextRank != 2){// if you're spy and enemy isn't private
					strTemp = strBoard[curRow][curCol];
					strBoard[curRow][curCol] = "N/N/N";
					strBoard[nextRow][nextCol] = strTemp;
					animation.blnPieceSelected = false;
				}else if(ownRank - nextRank > 0){// if your piece stronger than next piece
					strBoard[nextRow][nextCol] = "N/N/N";
				}else if(ownRank - nextRank < 0){// if your piece weaker than next piece
					strBoard[curRow][curCol] = "N/N/N";
				}
				animation.blnPieceSelected = false;

			}else if(strNextID[0].equals(strPieceID[0])){// if pieces are equal in rank
				if(nextRank == 0 && ownRank == 0){
					animation.blnloss = true;
					animation.blnPieceSelected = false;
				}else{
					strBoard[nextRow][nextCol] = "N/N/N";
					strBoard[curRow][curCol] = "N/N/N";
					animation.blnPieceSelected = false;
				}
			}
		}else if(strNextID[0].equals("N")){// if next cell is empty
			strTemp = strBoard[curRow][curCol];
			strBoard[nextRow][nextCol] = strTemp;
			animation.blnPieceSelected = false;
		}else if(strNextID[2].equals(strPieceID[2])){// if next cell contains self
			animation.blnPieceSelected = false;//deselect curr piect
			// select next clicked piece
			animation.blnPieceSelected = true;// select clicked piece
		}
	}
    //CONSTRUCTOR
	public main(){
		// panel
		thepanel.setLayout(null);
		thepanel.setPreferredSize(new Dimension(1280, 720));
		thepanel.addMouseListener(this);
		thepanel.addMouseMotionListener(this);
		
		// button to lock pieces in 
		lockIn.setBounds(830, 20, 430, 325);
		lockIn.addActionListener(this);
		
		// the chat
		thescroll.setBounds(830, 30, 430, 255);
		chat.setBounds(830, 295, 320, 50);
		send.setBounds(1160, 295, 100, 50);
		send.addActionListener(this);
		darkON.setBounds(705, 10, 55, 25);
		darkON.addActionListener(this);
		darkOFF.setBounds(755, 10, 55, 25);
		darkOFF.addActionListener(this);
		darkOFF.setEnabled(false);
		
		// settings screen
		portNumber.setBounds(247, 135, 250, 25);
		portNumber.addActionListener(this);
		serverIP.setBounds(192, 198, 250, 25);
		serverIP.addActionListener(this);
		
		// connection screen
		username.setBounds(450, 292, 450, 25);
		username.addActionListener(this);
		username.addFocusListener(this);
		portIPConnect.setBounds(450, 351, 450, 25);
		portIPConnect.addActionListener(this);
		portIPConnect.addFocusListener(this);
		
		// frame
		theframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theframe.setContentPane(thepanel);
		theframe.addKeyListener(this);
		theframe.pack();
		theframe.setResizable(false);
		theframe.setVisible(true);
		
		// super socket master
		if(thepanel.blnServer == true){ //for server
			ssm = new SuperSocketMaster(intPort, this);
		}else{ //for client
			ssm = new SuperSocketMaster(strIP, intPort, this);
		}
		ssm.connect();
		System.out.println(ssm.getMyAddress());
		
		// timer
		thetimer.start();
    }
  
    //MAIN METHOD
    public static void main(String[] args){
      new main();
    }   
}
