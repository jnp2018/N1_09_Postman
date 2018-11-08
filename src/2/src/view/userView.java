package view;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import main.workFollow;

public class userView extends JFrame implements ActionListener{
	JLabel labelUsername;
	JLabel labelPassword;
	JTextField txtUsername;
	JPasswordField txtPassword;
	JButton btnTraCuuDiem;
	private void btnTraCuuDiemClick() {
		try {
			workFollow wf = new workFollow(txtUsername.getText(), txtPassword.getPassword());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		JButton btnClicked = (JButton) e.getSource();
		if(btnClicked.equals(btnTraCuuDiem)){
			btnTraCuuDiemClick();
			return;
		}
	}
	
	
	public userView() throws HeadlessException {
		super();
		this.setLayout(new GridLayout(2, 3));
		labelUsername = new JLabel("Username:");
		labelPassword = new JLabel("Password:");
        
		txtUsername = new JFormattedTextField();
		txtPassword = new JPasswordField("");
        
		btnTraCuuDiem = new JButton("Tra Cuu Diem");
		btnTraCuuDiem.addActionListener(this);
		this.add(labelUsername);
		this.add(txtUsername);
		this.add(btnTraCuuDiem);
		this.add(labelPassword);
		this.add(txtPassword);
		this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
	}
	public static void main(String[] args) {
		userView uv = new userView();
	}
}
