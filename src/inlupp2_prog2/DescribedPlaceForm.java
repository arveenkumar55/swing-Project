package inlupp2_prog2;

import javax.swing.*;

public class DescribedPlaceForm {

    private JTextField nameField = new JTextField(10);
    private JTextField descriptionField = new JTextField(30);
    private int answer;

    public DescribedPlaceForm() {
        JPanel DescribedPlaceForm = new JPanel();
        DescribedPlaceForm.setLayout(new BoxLayout(DescribedPlaceForm, BoxLayout.Y_AXIS));

        JPanel row1 = new JPanel();
        row1.add(new JLabel("Name of location: "));
        row1.add(nameField);
        DescribedPlaceForm.add(row1);

        JPanel row2 = new JPanel();
        row2.add(new JLabel("Description of location: "));
        row2.add(descriptionField);
        DescribedPlaceForm.add(row2);

        answer = JOptionPane.showConfirmDialog(null, DescribedPlaceForm, "Described Place Form", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

    }

    public int getAnswer() {
        return answer;
    }

    public String getName() {
        return nameField.getText();
    }

    public String getDescription() {
        return descriptionField.getText();
    }

}
