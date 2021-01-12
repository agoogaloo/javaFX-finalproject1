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

public class ConnectionState extends State{
	private FinalProject project;
	
	private boolean ready=false, opponentReady=false;
	private Group startButton = new Group();//this is the button on the bottom that lets you end your turn
	
	private Text info = new Text(80*FinalProject.PIXEL_SCALE, 100*FinalProject.PIXEL_SCALE, 
			"trying to connect to your opponent. please wait");
	
	//internet things
	private boolean isServer;
	private String ip;
	private SocketGetter socketGetter;
	//Socket opponentSocket;
	private NetworkData opponent;
	
	public ConnectionState(FinalProject project, String ip) throws IOException {
		this.project=project;
		this.ip=ip;
		if(ip.equals("server")) {
			//do server making things
			isServer=true;
			socketGetter=new SocketGetter(new ServerSocket(69));
		
			info.setText("tell your oponent to join "+InetAddress.getLocalHost().getHostAddress());				
		}else {
			isServer=false;
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

class SocketGetter extends Thread{
	boolean isServer;
	ServerSocket server;
	String ip;
	Socket socket;
	
	public SocketGetter(ServerSocket server) {
		this.server=server;
		isServer=true;
	}
	public SocketGetter(String ip) {
		this.ip=ip;
		isServer=false;
	}
	
	public void run() {
		if(isServer) {
			try {
				socket=server.accept();
			} catch (IOException e) {
				System.out.println("a problem with your server happened");
				e.printStackTrace();
			}
		}else {
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
