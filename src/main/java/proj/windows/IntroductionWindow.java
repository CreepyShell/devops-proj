package proj.windows;

import javax.swing.*;
import java.awt.*;

public class IntroductionWindow {
    private final JFrame introduction;
    private final WindowsManager windowsManager;

    public IntroductionWindow(WindowsManager windowsManager) {
        this.windowsManager = windowsManager;
        introduction = new JFrame("FunnyAirlines");
        JPanel panel = new JPanel();
        introduction.setResizable(false);
        panel.setLayout(null);

        JLabel label = new JLabel("Welcome to the funny airlines!");
        label.setBounds(30, 20, 400, 21);
        label.setFont(new Font("Verdana", Font.BOLD, 20));

        JLabel helpLabel = new JLabel("To buy tickets or to manage flights you have to register or login");
        helpLabel.setBounds(10, 50, 520, 45);
        helpLabel.setFont(new Font("Verdana", Font.ITALIC, 15));

        JButton login = new JButton("Login");
        login.setBounds(180, 100, 150, 50);
        login.setFont(new Font("Times new Roman", Font.PLAIN, 25));
        login.addActionListener(e -> {
            this.closeWindow();
            windowsManager.openAuthWindowLogin();
        });

        JButton register = new JButton();
        register.setBounds(180, 170, 150, 50);
        register.setFont(new Font("Times new Roman", Font.PLAIN, 25));
        register.setText("Register");
        register.addActionListener(l -> {
            this.closeWindow();
            windowsManager.openAuthWindowRegister();
        });

        panel.add(label);
        panel.add(helpLabel);
        panel.add(login);
        panel.add(register);

        introduction.add(panel);
        introduction.setSize(530, 400);
        introduction.setLocationRelativeTo(null);
        introduction.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        introduction.setVisible(true);
    }
    public void closeWindow(){
        introduction.setVisible(false);
        introduction.dispose();
    }
}
