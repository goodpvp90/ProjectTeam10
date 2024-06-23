package client.logic;

import java.io.IOException;

import common.logic.ClientRequest;
import common.logic.enums.ClientOperationEnum;
import common.logic.enums.ClientTypeEnum;

/**
 * This class constructs the UI for a chat client. It implements the chat
 * interface in order to activate the display() method. 
 */
public class ClientController implements ChatIF{
	// Instance variables **********************************************

		/**
		 * The instance of the client that created this ConsoleChat.
		 */
		GoNatureClient client;

		// Constructors ****************************************************

		/**
		 * Constructs an instance of the ClientConsole UI.
		 *
		 * @param host The host to connect to.
		 * @param port The port to connect on.
		 * @throws IOException If failed to connect to server
		 */
		public ClientController(String host, int port) throws IOException {
			
			try {
				client = new GoNatureClient(host, port, this);
			} catch (IOException e) {
				System.out.println("Error: Can't setup connection!" + " Terminating client.");
				throw e;
			}
		}

		// Instance methods ************************************************

		/**
		 * This method waits for input from the console. Once it is received, it sends
		 * it to the client's message handler.
		 * 
		 * @param str  the input from the client
		 */
		//public void accept(String str) {
		//	client.handleMessageFromClientUI(str);
		//}
		
		/**
		 * This method waits for input from the console. Once it is received, it sends
		 * it to the client's message handler.
		 * 
		 * @param message The message to send to the server
		 */
		public void accept(ClientRequest message) {
			client.handleMessageFromClientUI(message);
		}
		
		public void disconnect() {
			client.disconnect();
		}

		/**
		 * This method overrides the method in the ChatIF interface. It displays a
		 * message onto the screen.
		 *
		 * @param message The string to be displayed.
		 */
		public void display(String message) {
			System.out.println("> " + message);
		}
		
		public GoNatureClient getClient() {
			return client;
		}
}
