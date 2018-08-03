package inlupp2_prog2;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.util.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.io.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.Iterator;

//för att testa så git fungerar som det ska... ta bort sen 

public class Window extends JFrame {

    private ImagePanel display;
    private JMenuItem newMap, loadPlaces, save, exit;
    private JPanel upperPanel, p1, eastPanel;
    private JButton newButton, searchButton, hideButton, removeButton, coordinatesButton, hideCategoriesButton;
    private JRadioButton namedRB, describedRB;
    private JList categoryList;
    private JScrollPane scroll;
    private JFileChooser fc = new JFileChooser();
    private Mouselyss mouseLyss = new Mouselyss();
    private JTextField searchField;
    private String[] categories = {"Bus", "Underground", "Train"};
    private String selectedCategory = "None";
    private boolean isSaved;
    private boolean loadedImg;

    private Place p;
   // private NamedPlace n;
   // private DescribedPlace d;

    Map<Position, Place> positionList = new HashMap<>();
    List<Place> places = new ArrayList<>();
    HashMap<String, List<Place>> nameList = new HashMap<>();
    Collection<Place> markedPlacesHashMap = new ArrayList<>();
    List<Place> placeByCategory = new ArrayList<>();
    HashMap<String, List<Place>> categoryMap = new HashMap<>();

    Window() {
        super("Inlupp2");

        JMenuBar menuBar = new JMenuBar();
        JMenu archiveMenu = new JMenu("Archive");
        menuBar.add(archiveMenu);
        newMap = archiveMenu.add("New Map");
        newMap.addActionListener(new ArchiveListener());
        loadPlaces = archiveMenu.add("Load Places");
        loadPlaces.addActionListener(new ArchiveListener());
        save = archiveMenu.add("Save");
        save.addActionListener(new ArchiveListener());
        exit = archiveMenu.add("Exit");
        exit.addActionListener(new ArchiveListener());

        upperPanel = new JPanel();
        add(upperPanel, BorderLayout.NORTH);
        newButton = new JButton("New");
        upperPanel.add(newButton);
        newButton.addActionListener(new NewListener());

        namedRB = new JRadioButton("Named", true);
        upperPanel.add(namedRB);
        describedRB = new JRadioButton("Described", false);
        upperPanel.add(describedRB);
        ButtonGroup bg = new ButtonGroup();
        bg.add(namedRB);
        bg.add(describedRB);

        p1 = new JPanel();
        upperPanel.add(p1);
        p1.setLayout(new GridLayout(2, 1));
        p1.add(namedRB);
        p1.add(describedRB);

        searchField = new JTextField(12);
        upperPanel.add(searchField);
        searchButton = new JButton("Search");
        upperPanel.add(searchButton);
        searchButton.addActionListener(new SearchListener());
        hideButton = new JButton("Hide");
        upperPanel.add(hideButton);
        hideButton.addActionListener(new OperationsListener());

        removeButton = new JButton("Remove");
        upperPanel.add(removeButton);
        removeButton.addActionListener(new OperationsListener());

        coordinatesButton = new JButton("Coordinates");
        coordinatesButton.addActionListener(new CoordinatesListener());
        upperPanel.add(coordinatesButton);

        eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

        eastPanel.add(new JLabel("Categories"));
        add(eastPanel, BorderLayout.EAST);
        categoryList = new JList(categories);
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryList.addListSelectionListener(new CategoriesListener());
        eastPanel.add(categoryList);
        hideCategoriesButton = new JButton("Hide category");
        hideCategoriesButton.addActionListener(new HideCategoryListener());
        eastPanel.add(hideCategoriesButton);

        display = new ImagePanel();
        display.setLayout(null);

        scroll = new JScrollPane(display);
        add(scroll, BorderLayout.CENTER);

        setJMenuBar(menuBar);
        setSize(800, 600);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!isSaved) {
                    int confirmMessage = JOptionPane.showConfirmDialog(null, "Do you want to exit?", "Warning!",
                            JOptionPane.YES_NO_OPTION);
                    if (confirmMessage == JOptionPane.YES_OPTION) {
                        dispose();
                    }
                } else {
                    System.exit(0);
                }
            }
        });

    }

    private void createNamedPlace(String name, Position pos) {

       NamedPlace namedPlace = new NamedPlace(selectedCategory, pos, name);
       
        display.add(namedPlace);
        p = namedPlace;

        addPlaceToList(p, name, pos);

        namedPlace.addMouseListener(new TriangleListener());
        repaintDisplay();
        clearCategorySelection();
    }

    private void createDescribedPlace(Position pos, String name, String description) {

        DescribedPlace describedPlace = new DescribedPlace(selectedCategory, pos, name, description);
        
        display.add(describedPlace);
        p = describedPlace;
        addPlaceToList(p, name, pos);
        describedPlace.addMouseListener(new TriangleListener());
        repaintDisplay();
        clearCategorySelection();
    }

    private void addPlaceToList(Place p, String name, Position pos) {
        places = new ArrayList<>();
        placeByCategory = new ArrayList<>();
        positionList.put(pos, p);
        
        

        if (categoryMap.containsKey(selectedCategory)) {
            categoryMap.get(selectedCategory).add(p);
            if (nameList.containsKey(name)) {
                nameList.get(name).add(p);
            } else {
                places.add(p);
                nameList.put(name, places);
            }
        } else {
            placeByCategory.add(p);
            categoryMap.put(selectedCategory, placeByCategory);
            if (nameList.containsKey(name)) {
                nameList.get(name).add(p);

            } else {
                places.add(p);
                nameList.put(name, places);

            }
        }
        return;
    }

    private void clearCategorySelection() {
        categoryList.clearSelection();
        selectedCategory = "None";
    }

    private void repaintDisplay() {

        display.validate();
        display.repaint();
        display.removeMouseListener(mouseLyss);
        display.setCursor(Cursor.getDefaultCursor());
        newButton.setEnabled(true);
        isSaved = false;
        return;

    }

    private void readFromFile(String line) {
        String[] tokens = line.split(",");
        String type = tokens[0];
        selectedCategory = tokens[1];
        int x = Integer.parseInt(tokens[2]);
        int y = Integer.parseInt(tokens[3]);
        String name = tokens[4];

        if (type.equalsIgnoreCase("Named")) {
            createNamedPlace(name, new Position(x, y));
        }
        if (type.equalsIgnoreCase("Described")) {
            String description = tokens[5];
            createDescribedPlace(new Position(x, y), name, description);
        }
    }

    private void loadPlace() {
        int result = fc.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath;
                File file = fc.getSelectedFile();
                filePath = file.getAbsolutePath();
                FileReader placesFile = new FileReader(filePath);
                BufferedReader in = new BufferedReader(placesFile);
                String line;

                while ((line = in.readLine()) != null) {
                    readFromFile(line);
                }
                placesFile.close();
                in.close();

            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Fil ej hittad" + ex.getMessage());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Fel: " + ex.getMessage());
            }
        } else {
            return;
        }
    }

    private void loadImage() {

        String filePath;
        int result = fc.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        } else {

            File file = fc.getSelectedFile();
            filePath = file.getAbsolutePath();
            display.setImage(filePath);
            validate();
            repaint();
            loadedImg = true;
        }

    }

    private void savePlace() {
        
        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {

                FileWriter outfile = new FileWriter(fc.getSelectedFile());
                PrintWriter out = new PrintWriter(outfile);
                for (Place p : positionList.values()) {
                    System.out.println(p + "place p");
                    out.println(p);
                }
                out.close();
                outfile.close();
                isSaved = true;
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Filen kan ej hittas");

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Fel" + e.getMessage());

            }
        } else {
            return;
        }

    }

    private void removePlace(Place p) {

      //!!  isSaved = false;  Oklart om denna behövs? !!//
        placeByCategory.remove(p);
        places.remove(p);
        markedPlacesHashMap.remove(p);

        if (nameList.containsKey(p.getName())) {
            nameList.remove(p.getName());
        }
        if (categoryMap.containsKey(p.getCategory())) {
            categoryMap.remove(p.getCategory());
        }
        if (positionList.containsKey(p.getPosition())) {
            positionList.remove(p.getPosition());
        }
           
    }

    private void hidePlace() {
        if (markedPlacesHashMap.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Marked places available", "Marked Place", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            Iterator<Place> iterator = markedPlacesHashMap.iterator();
            while (iterator.hasNext()) {
                Place entry = iterator.next();
                p = entry;
                String placeName = p.getName();
                p.setVisible(false);
                p.setMarked(false);
                markedPlacesHashMap.remove(placeName);
                p.repaint();
            }
            return;
        }
    }

    private void notSavedDialog() {
        int dialogButton = JOptionPane.showConfirmDialog(null, "Do you want to save before loading new ", "Warning!", JOptionPane.YES_NO_OPTION);
        if (dialogButton == JOptionPane.YES_OPTION) {
            savePlace();
            return;
        } else {
            return;
        }
    }

    private void disableNewPlaceCursor() {
        display.setCursor(Cursor.getDefaultCursor());
        display.removeMouseListener(mouseLyss);
        newButton.setEnabled(true);
    }

    public void setAllMarkedPlacesUnMarked() {

        Iterator<Place> iterator = markedPlacesHashMap.iterator();
        while (iterator.hasNext()) {
            Place pl = iterator.next();
            if (pl.getMarked() == true) {
                pl.setMarked(false);
                pl.repaint();
                System.out.println(pl.getName() + " place set unmarked with category " + pl.getCategory() + " with Coordinates(x,y)"
                        + "(" + pl.getPosition().getXCoordinate() + "," + pl.getPosition().getYCoordinate() + ")");

                iterator.remove();

            }
        }
        markedPlacesHashMap.clear();
        return;
    }

    class OperationsListener implements ActionListener {

        public void actionPerformed(ActionEvent ave) {

            if (ave.getSource() == hideButton) {
                hidePlace();
            }

            if (ave.getSource() == removeButton) {
                if (markedPlacesHashMap.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No marked places available", "Marked Place", JOptionPane.ERROR_MESSAGE);
                    return;

                } else {

                    Iterator<Place> iterator = markedPlacesHashMap.iterator();
                    while (iterator.hasNext()) {
                        p.setMarked(false);
                        p.setVisible(false);
                        removePlace(p);
                    }
                    p.repaint();
                    markedPlacesHashMap.clear();

                }

            }
        }
    }

    class ArchiveListener implements ActionListener {

        public void actionPerformed(ActionEvent ave) {

            if (ave.getSource() == newMap) {
                if (!isSaved) {
                    notSavedDialog();
                    loadImage();
                } else {
                    loadImage();
                }
            }
            if (ave.getSource() == loadPlaces) {
                if (!loadedImg) {
                    JOptionPane.showMessageDialog(null, "No map available", "Please load map before adding new places", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!isSaved) {
                    notSavedDialog();
                    loadPlace();
                } else {
                    loadPlace();
                }
            }

            if (ave.getSource() == save) {
                savePlace();
            }

            if (ave.getSource() == exit) {
                if (!isSaved) {
                    int dialogButton = JOptionPane.showConfirmDialog(null, "Do you want to save before exit?", "Warning!", JOptionPane.YES_NO_OPTION);
                    if (dialogButton == JOptionPane.NO_OPTION) {
                        System.exit(0);
                    } else {
                        savePlace();
                        return;
                    }
                }
            }

        }
    }

    class NewListener implements ActionListener {

        public void actionPerformed(ActionEvent ave) {

            display.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            display.addMouseListener(mouseLyss);
            newButton.setEnabled(false);
        }
    }

    class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ave) {

            setAllMarkedPlacesUnMarked();
            String searchTerm = searchField.getText();

            List<Place> temporary = nameList.get(searchTerm);
            if (temporary != null) {
                for (Iterator<Place> iterator = temporary.iterator(); iterator.hasNext();) {
                    Place p = iterator.next();
                    if (p.getMarked() == false) {
                        markedPlacesHashMap.add(p);
                        p.setMarked(true);
                        p.repaint();
                    }
                    if (!p.isVisible()) {
                        p.setVisible(true);
                    }

                }
            }else {
            JOptionPane.showMessageDialog(null, "No place with that name was found", "No place found", JOptionPane.ERROR_MESSAGE);}
            
        }

    }

    class TriangleListener extends MouseAdapter {

        public void mouseClicked(MouseEvent mev) {

            p = (Place) mev.getSource();

            if (mev.getModifiers() == InputEvent.BUTTON1_MASK) {
                if (!p.getMarked()) {
                    p.requestFocus();
                    p.setMarked(true);
                    p.addFocusListener(new MarkedListener());
                    markedPlacesHashMap.add(p);
                    p.repaint();
                    return;

                } else {
                    p.setMarked(false);
                    p.repaint();
                    markedPlacesHashMap.remove(p);
                    return;
                }

            }
            if (mev.getModifiers() == InputEvent.BUTTON3_MASK) {
                if (p instanceof NamedPlace) {

                    JOptionPane.showMessageDialog(null, p.getName() + " " + p.getPosition(), "Platsinfo: ", JOptionPane.INFORMATION_MESSAGE);
                }
                if (p instanceof DescribedPlace) {
                    JOptionPane describedPane = new JOptionPane();
                    describedPane.setMessage("Name: " + p.getName() + " " + p.getPosition() + "\n" + "Description: " + ((DescribedPlace) p).getDescription());
                    describedPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
                    JDialog dialog = describedPane.createDialog(p, "Platsinfo:");
                    dialog.setVisible(true);
                    ;

                }

            }

        }

    }

    class MarkedListener implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
           
            p.repaint();

        }

        @Override
        public void focusLost(FocusEvent e) {
            
            p.removeFocusListener(this);
            p.repaint();
        }

    }

    class CoordinatesListener implements ActionListener {

        public void actionPerformed(ActionEvent ave) {

            try {
                CoordinatesForm c = new CoordinatesForm();
                int answer = c.getAnswer();

                if (answer != JOptionPane.OK_OPTION) {
                    return;
                }
                if (c.getPosition() == null) {
                    JOptionPane.showMessageDialog(null, "Invalid Input", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                } else {
                    Position p = c.getPosition();
                    boolean flag = false;

                    if (positionList.containsKey(p)) {
                        Place place = positionList.get(p);
                        flag = true;
                        setAllMarkedPlacesUnMarked();
                        place.setMarked(true);
                        if (!place.isVisible()) {
                            place.setVisible(true);
                        }
                        markedPlacesHashMap.add(place);

                    }
                    if (!flag) {
                        JOptionPane.showMessageDialog(null, "The selected location is not available", "Select Location", JOptionPane.OK_OPTION);
                    }
                    repaint();

                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "The input values are numeric", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    class CategoriesListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!categoryList.isSelectionEmpty()) {
                selectedCategory = String.valueOf(categoryList.getSelectedValue());
                if (!categoryMap.isEmpty()) {
                    markedPlacesHashMap.clear();

                    try {
                        List<Place> temporary = categoryMap.get(selectedCategory);
                        for (Place p : temporary) {
                            markedPlacesHashMap.add(p);
                            p.setVisible(true);
                        }
                    } catch (NullPointerException np) {
                        
                    }
                } 

            }

        }
    }

    class Mouselyss extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent mev) {

            if (!loadedImg) {
                JOptionPane.showMessageDialog(null, "No map available", "Please load map before adding new places", JOptionPane.ERROR_MESSAGE);
                disableNewPlaceCursor();
                return;
            } else {

                int x = mev.getX();
                int y = mev.getY();

                Position posit = new Position(x, y);

                if (positionList.containsKey(posit)) {
                    JOptionPane.showMessageDialog(null, "already created place on this position", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (namedRB.isSelected()) {

                    try {
                        String answer = JOptionPane.showInputDialog(null, "Name of location", "Name of location",
                                JOptionPane.QUESTION_MESSAGE);
                        if (answer != null && !answer.isEmpty()) {
                            createNamedPlace(answer, posit);
                            System.out.println(x + " " + y);
                            return;
                        }
                        if (answer == null) {
                            disableNewPlaceCursor();
                            return;

                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid Input", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Invalid Input", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    }

                }

                if (describedRB.isSelected()) {
                    try {
                        DescribedPlaceForm d = new DescribedPlaceForm();
                        int answer = d.getAnswer();

                        if (answer != JOptionPane.OK_OPTION) {
                            disableNewPlaceCursor();
                            return;
                        }
                        if (!d.getName().isEmpty() && !d.getDescription().isEmpty()) {
                            createDescribedPlace(posit, d.getName(), d.getDescription());
                            return;

                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid Input", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Invalid Input", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
            display.validate();

            display.repaint();

            display.removeMouseListener(mouseLyss);

            display.setCursor(Cursor.getDefaultCursor());
            newButton.setEnabled(
                    true);
        }
    }

    class HideCategoryListener implements ActionListener {

        public void actionPerformed(ActionEvent ave) {

            try {
                String cat = categoryList.getSelectedValue().toString();
                List<Place> catList = categoryMap.get(cat);
                for (Place p : catList) {
                    p.setVisible(false);
                }
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, "No category selected or no places available in the chosen category "+ e, "Error", JOptionPane.ERROR_MESSAGE);

            }
        }
    }
}
