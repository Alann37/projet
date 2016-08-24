package Graphic;
/**
 * classe non utiliser a l'heure actuel, il s'agit d'un compteur pour montrer la progression du traitement
 */
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class LoadingScreen extends JDialog {
	private JProgressBar progressBar;
	public void progressUpdate(int value) {
		if(value != progressBar.getValue())
		{
			progressBar.setValue(value);;
		}
		progressBar.update(progressBar.getGraphics());
	}
	public int getValue(){
		return progressBar.getValue();
	}
	public void visibility(boolean b){
		this.progressBar.setVisible(b);
		this.setVisible(b);
	}
	/**
	 * Create the dialog.
	 */
	public LoadingScreen() {
		setResizable(false);
		setBounds(100, 100, 450, 160);
		this.setTitle("Ongoing Treatment");
		progressBar = new JProgressBar(0,1000);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(168)
							))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(60)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					
					.addContainerGap(22, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
	}

}
