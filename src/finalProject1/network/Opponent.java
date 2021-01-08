package finalProject1.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Opponent {
	DataInputStream inData;
	DataOutputStream outData;

	public Opponent(Socket socket) {
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
