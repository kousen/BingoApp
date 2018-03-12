package com.kousenit;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Bingo extends JPanel {
    private static boolean muted;
    private List<JButton> buttons = new ArrayList<>();
    private static List<String> terms = Arrays.asList(
            "Java", "Polymorphism", "Inheritance", "JVM", "JDK",
            "Encapsulation", "abstract class", "abstract method",
            "final class", "final method", "final reference",
            "static method", "public", "private", "protected",
            "static attribute", "Stream", "Block Lambda",
            "Expression Lambda", "Method Reference", "primitive",
            "concrete class", "interface");

    public Bingo() {
        createGameBoard();
    }

    private void createGameBoard() {
        Collections.shuffle(terms);
        List<String> firstNine = terms.subList(0, 9);

        for (String term : firstNine) {
            JButton b = new JButton(term);
            b.setBackground(Color.BLUE);
            b.setForeground(Color.WHITE);
            b.setFont(new Font("Sans serif", Font.BOLD, 20));
            b.setOpaque(true);
            b.addActionListener(this::clicked);
            buttons.add(b);
            add(b);
        }
    }

    private void clicked(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        button.setBackground(Color.YELLOW);
        button.setEnabled(false);
        if (checkForBingo()) {
            if (!isMuted()) {
                try {
                    AudioInputStream audioInputStream =
                            AudioSystem.getAudioInputStream(
                                    new BufferedInputStream(
                                            getClass().getResourceAsStream("/TADA.WAV")));
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
                    e1.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), "Bingo!", "Bingo!",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private boolean checkForBingo() {
        return
                // check rows
                !buttons.get(0).isEnabled() && !buttons.get(1).isEnabled() && !buttons.get(2).isEnabled() ||
                        !buttons.get(3).isEnabled() && !buttons.get(4).isEnabled() && !buttons.get(5).isEnabled() ||
                        !buttons.get(6).isEnabled() && !buttons.get(7).isEnabled() && !buttons.get(8).isEnabled() ||

                        // check columns
                        !buttons.get(0).isEnabled() && !buttons.get(3).isEnabled() && !buttons.get(6).isEnabled() ||
                        !buttons.get(1).isEnabled() && !buttons.get(4).isEnabled() && !buttons.get(7).isEnabled() ||
                        !buttons.get(2).isEnabled() && !buttons.get(5).isEnabled() && !buttons.get(8).isEnabled() ||

                        // check diagonals
                        !buttons.get(0).isEnabled() && !buttons.get(4).isEnabled() && !buttons.get(8).isEnabled() ||
                        !buttons.get(2).isEnabled() && !buttons.get(4).isEnabled() && !buttons.get(6).isEnabled();
    }

    public static void setMuted(boolean muted) {
        Bingo.muted = muted;
    }

    public static boolean isMuted() {
        return muted;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(Bingo::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Bingo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        refreshGameBoard(frame);

        JMenuBar menuBar = addMenuBar(frame);
        frame.setJMenuBar(menuBar);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension halfSize = new Dimension(screenSize.width / 2, screenSize.height / 2);
        frame.setSize(halfSize);
        frame.setVisible(true);
    }

    private static JMenuBar addMenuBar(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem reset = new JMenuItem("Restart");
        reset.addActionListener(e -> refreshGameBoard(frame));
        reset.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_MASK));
        menu.add(reset);

        JMenuItem mute = new JMenuItem("Mute");
        mute.addActionListener(e -> Bingo.setMuted(!Bingo.isMuted()));
        mute.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_MASK));
        menu.add(mute);

        menuBar.add(menu);
        return menuBar;
    }

    private static void refreshGameBoard(JFrame frame) {
        frame.getContentPane().removeAll();
        Bingo bingo = new Bingo();
        bingo.setOpaque(true);
        bingo.setLayout(new GridLayout(3, 3));
        frame.setContentPane(bingo);
        frame.revalidate();
        frame.repaint();
    }
}
