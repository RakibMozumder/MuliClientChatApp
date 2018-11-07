package multiClientChatApp;

import javax.swing.*;
public class LoginAsWindow {

	
	public static void main(String [] args){
		
		Object[] selectionValues = { "Server","Client"};
		String initialSelection = "Server";
		
		Object selection = JOptionPane.showInputDialog(null, "Login as : ", "ChatApp", JOptionPane.QUESTION_MESSAGE, null, selectionValues, initialSelection);
		if(selection.equals("Server")){
                   String[] arguments = new String[] {};
			new Server().main(arguments);
		}else if(selection.equals("Client")){
			String IPServer = JOptionPane.showInputDialog("Enter the Server ip adress");
                        String[] arguments = new String[] {IPServer};
			new MultiClientChatWindow().main(arguments);
                }	
	}
}