package finalProject1.states;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import finalProject1.Assets;
import finalProject1.FinalProject;
import finalProject1.network.NetworkData;
import finalProject1.states.gameState.GameState;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * this is the state for when you are connecting to you opponent and waiting for them to get ready
 *
 */
public class ConnectionState extends State{
	private FinalProject project;//the project so it can add things to it
	
	private boolean ready=false, opponentReady=false;//whether you or your opponent are ready
	private Group startButton = new Group();//this is the button on the bottom that lets you say that you are ready
	
	//the text that lets you know what is happening
	private Text info = new Text(80*FinalProject.PIXEL_SCALE, 100*FinalProject.PIXEL_SCALE, 
			"trying to connect to your opponent. please wait");
	
	//internet things
	private boolean isServer;//whether you are the server or client
	private String ip;//you ip address
	private SocketGetter socketGetter;//a thing that lets you gets you opponents socket
	private NetworkData opponent;//the netword data that is used to send/receive data from your opponent
	
	//this throws an error if it cant create a socket for some reason
	public ConnectionState(FinalProject project, String ip) throws IOException {
		//assigning the variables we got as paramiters
		this.project=project;
		this.ip=ip;
		//doing server making things if they are making a server
		if(ip.equals("server")) {
			isServer=true;
			//connecting to opponent as a server
			socketGetter=new SocketGetter(new ServerSocket(69));
			//showing their ip address so that the opponent knows where to connect to
			info.setText("tell your oponent to join "+InetAddress.getLocalHost().getHostAddress());	
		//doing client connecty things if they are a client
		}else {
			isServer=false;
			//connecting to opponent as a client
			socketGetter=new SocketGetter(ip);
			info.setText("connecting to "+ip);	
		}
		socketGetter.start();
		System.out.println("isserver:"+isServer);

	}
	
	@Override
	public void start() {
		//the play button
		Text endTurnText=new Text(13*FinalProject.PIXEL_SCALE, 20*FinalProject.PIXEL_SCALE, "clickity click");
		endTurnText.setFill(Color.WHITE);
		endTurnText.setFont(Assets.boldfont);
		//moving the endturn button to the right spot and adding the graphics to it
		startButton.setTranslateX(175*FinalProject.PIXEL_SCALE);
		startButton.setTranslateY(150*FinalProject.PIXEL_SCALE);
		startButton.getChildren().add(new Rectangle(100*FinalProject.PIXEL_SCALE,
				30*FinalProject.PIXEL_SCALE,new Color(0.047, 0.047, 0.047, 1)));
		startButton.getChildren().add(endTurnText);
		
		//the instruction text
		info.setFill(Color.WHITE);
		info.setFont(Assets.font);
				
		
		project.add(startButton);
		project.add(info);
		
		
		
		
		
	}
	
	@Override
	public void update() {
		if(socketGetter.hasSocket()&&opponent==null) {
			
			opponent=new NetworkData(socketGetter.getSocket(),ip);
			info.setText("connected to "+socketGetter.getSocket().getInetAddress().getHostName());
		}
		
		if(opponent!=null&&startButton.isPressed()) {
			opponent.sendData("ready");
			ready=true;
			info.setText("ready and waiting for opponent start");
		}
		if(opponent!=null&&opponent.getData().equals("ready")) {
			info.setText("your opponent is ready to start");
			opponentReady=true;
		}
		
		if(ready&&opponentReady) {
			setCurrentState(new GameState(project, opponent,isServer));
		}
	}

	@Override
	public void end() {
		project.clear();
	}
}
/**
 * this is used to get a socket to communicate with the opponent in a sepperate thread. 
 * it needs to do this because connecting to a server or waiting for a client to connect hangs the code, 
 * and if it is in the same thread nothing else will run when it tries to connect. 
 *
 */
class SocketGetter extends Thread{//extinding thread so it can be a thread
	boolean isServer;//if it is a server of not
	ServerSocket server;//the server it uses if it is a server
	String ip;//the ip address that it should connect to if it is a client
	Socket socket;//the socket it will return
	
	/**
	 * a constructor for the server to use
	 * @param server - the server that the opponent will connect to
	 */
	public SocketGetter(ServerSocket server) {
		this.server=server;
		isServer=true;
	}
	/**
	 * a constructor for the client to use
	 * @param ipm - the ip the opponent will connenct to
	 */
	public SocketGetter(String ip) {
		this.ip=ip;
		isServer=false;
	}
	/**
	 * this is the part that updates in a seperate thread
	 */
	public void run() {
		//doing server connection things
		if(isServer) {
			//trying to accept any clients that connect
			try {
				socket=server.accept();
			} catch (IOException e) {
				System.out.println("a problem with your server happened");
				e.printStackTrace();
			}
			//doing client connection things
		}else {
			//trying to connect with the server
			try {
				socket=new Socket(InetAddress.getByName(ip),69);
			} catch (UnknownHostException e) {
				System.out.println("a server with ip '"+ip+"' couldnt be found");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//synchronized getters so hat other classes can get the socket from it
	public synchronized Socket getSocket() {
		return socket;
	}
	
	public synchronized boolean hasSocket() {
		if(socket==null) {
			return false;
		}
		return true;
	}
}
