package finalProject1.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkData {
	//these are seperators in the data that determine what the data that is given means
	//this class does nothing with them but other classes do
	public static final char SEPERATOR=',',ATTACK='A',MOVE='M',STARTTURN='S',ENDTURN='E',CARDS='C';
	DataInputStream inData;
	DataOutputStream outData;

	public NetworkData(Socket socket) {
		try {
			inData = new DataInputStream(socket.getInputStream());
			outData = new DataOutputStream(socket.getOutputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getData() {
		try {
			if(inData.available()>0) {
				return inData.readUTF();
			}
		} catch (IOException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public void sendData(String data) {
		try {
			outData.writeUTF(data);
		} catch (IOException e) {
			System.out.println("couldn't send information to the opponent");
		}
		
		
	}
}
