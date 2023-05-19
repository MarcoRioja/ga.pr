package Features;

import javax.swing.JOptionPane;

public class AlertMessage {

	public AlertMessage(final String alertMessage, final String alertSection) {
		Thread alertThread = new Thread(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(null, alertMessage, alertSection, JOptionPane.PLAIN_MESSAGE);
			}
		});
		alertThread.start();
	}
	
	
	
}
