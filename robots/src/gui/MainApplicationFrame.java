package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

import gui.state.StatefulWindow;
import gui.state.WindowStateManager;
import log.Logger;

public class MainApplicationFrame extends StatefulWindow {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public static void main(String[] args) {
        Locale.setDefault(new Locale("ru", "RU"));
        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame();
            frame.setVisible(true);
        });
    }

    private final JDesktopPane desktopPane = new JDesktopPane();
    public MainApplicationFrame() {
        initLocalization();
        setupWindow();
        createAndAddWindows();
        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                confirmExit();
            }
        });
    }

    private void setupWindow() {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);
        setContentPane(desktopPane);
    }

    private void createAndAddWindows() {
        Map<String, Map<String, String>> savedStates = WindowStateManager.loadStates();

        if (savedStates.containsKey("mainWindow")) {
            restoreState(savedStates.get("mainWindow"));
        }

        LogWindow logWindow = createLogWindow();
        if (savedStates.containsKey("logWindow")) {
            logWindow.restoreState(savedStates.get("logWindow"));
        }
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        if (savedStates.containsKey("gameWindow")) {
            gameWindow.restoreState(savedStates.get("gameWindow"));
        }
        addWindow(gameWindow);
    }

    private void saveWindowStates() {
        WindowStateManager.saveStates(this);
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private void confirmExit() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Вы уверены, что хотите выйти?",
                "Подтверждение",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {
            saveWindowStates(); // Сохраняем состояния перед выходом
            System.exit(0);
        }
    }

    public JDesktopPane getDesktopPane() {
        return desktopPane;
    }

    private void initLocalization() {
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");
        UIManager.put("OptionPane.okButtonText", "ОК");
        UIManager.put("OptionPane.cancelButtonText", "Отмена");
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createFileMenu());
        return menuBar;
    }

    private JMenu createLookAndFeelMenu() {
        JMenu menu = new JMenu("Режим отображения");
        menu.setMnemonic(KeyEvent.VK_V);

        JMenuItem systemItem = new JMenuItem("Системная схема", KeyEvent.VK_S);
        systemItem.addActionListener(e -> setLookAndFeel(UIManager.getSystemLookAndFeelClassName()));

        JMenuItem crossItem = new JMenuItem("Универсальная схема", KeyEvent.VK_U);
        crossItem.addActionListener(e -> setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()));

        menu.add(systemItem);
        menu.add(crossItem);
        return menu;
    }

    private JMenu createTestMenu() {
        JMenu menu = new JMenu("Тесты");
        menu.setMnemonic(KeyEvent.VK_T);

        JMenuItem logItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        logItem.addActionListener(e -> Logger.debug("Новая строка"));

        menu.add(logItem);
        return menu;
    }

    private JMenu createFileMenu() {
        JMenu menu = new JMenu("Файл");
        menu.setMnemonic(KeyEvent.VK_F);

        JMenuItem exitItem = new JMenuItem("Выход", KeyEvent.VK_Q);
        exitItem.addActionListener(e -> confirmExit());

        menu.add(exitItem);
        return menu;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException |
                 IllegalAccessException | UnsupportedLookAndFeelException e) {
            Logger.error("Ошибка при смене стиля: " + e.getMessage());
        }
    }
}