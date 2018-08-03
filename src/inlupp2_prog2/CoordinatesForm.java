package inlupp2_prog2;

import javax.swing.*;

public class CoordinatesForm {

    private JTextField x = new JTextField(3);
    private JTextField y = new JTextField(3);
    private int answer;

    public CoordinatesForm() {
        JPanel CoordinatesForm = new JPanel();
        CoordinatesForm.add(new JLabel("x:"));
        CoordinatesForm.add(x);
        CoordinatesForm.add(new JLabel("y:"));
        CoordinatesForm.add(y);
        answer = JOptionPane.showConfirmDialog(null, CoordinatesForm, "Input coordinates:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    public int getAnswer() {
        return answer;
    }

    public Position getPosition() {
        int newX = Integer.parseInt(x.getText());
        int newY = Integer.parseInt(y.getText());
        Position p = new Position(newX, newY);
        return p;
    }
}
