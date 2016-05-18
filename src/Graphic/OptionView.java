package Graphic;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import Configuration.Configuration;

public class OptionView extends JDialog {
	private JPanel buttonPane;
	private JTextField tfConfig1;
	private JTextField tfConfig2;
	private JTextField tfConfig3;
	private JTextField tfConfig4;



	/**
	 * Create the dialog.
	 * @throws IOException 
	 */
	public OptionView() throws IOException {
		setBounds(100, 100, 450, 352);
		{
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							Configuration.setConfig(0, tfConfig1.getText());
							Configuration.setConfig(1, tfConfig2.getText());
							Configuration.setConfig(2, tfConfig3.getText());
							Configuration.setConfig(3, tfConfig4.getText());
							setVisible(false);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
			setResizable(false);
		}
		
		JLabel lbConfig1 = new JLabel("Chemin d'export des bases brutes");
		List<String> conf = Configuration.importConfig();
		tfConfig1 = new JTextField();
		tfConfig1.setColumns(10);
		tfConfig1.setText(conf.get(0));
		
		JLabel lbConfig2 = new JLabel("Chemin d'export des bases traitées");
		
		tfConfig2 = new JTextField();
		tfConfig2.setColumns(10);
		tfConfig2.setText(conf.get(1));
		
		JLabel lbConfig3 = new JLabel("Chemin d'accès aux prints studys");
		
		tfConfig3 = new JTextField();
		tfConfig3.setColumns(10);
		tfConfig3.setText(conf.get(2));
		
		JLabel lbConfig4 = new JLabel("Chemin d'export des bases libellées");
		
		tfConfig4 = new JTextField();
		tfConfig4.setColumns(10);
		tfConfig4.setText(conf.get(3));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(buttonPane, GroupLayout.PREFERRED_SIZE, 434, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(27)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(lbConfig4)
						.addComponent(lbConfig3)
						.addComponent(lbConfig2)
						.addComponent(tfConfig1, GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
						.addComponent(lbConfig1)
						.addComponent(tfConfig2)
						.addComponent(tfConfig3)
						.addComponent(tfConfig4))
					.addGap(58))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lbConfig1)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(tfConfig1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lbConfig2)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(tfConfig2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lbConfig3)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(tfConfig3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lbConfig4)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(tfConfig4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
					.addComponent(buttonPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
	}
}
