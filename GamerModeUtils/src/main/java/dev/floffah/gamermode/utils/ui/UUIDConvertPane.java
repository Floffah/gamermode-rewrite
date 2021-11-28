package dev.floffah.gamermode.utils.ui;

import dev.floffah.gamermode.utils.ui.component.Input;

import javax.swing.*;

public class UUIDConvertPane extends JPanel {
    Input uuidArea;

    public UUIDConvertPane() {
        super();

        this.setLayout(null);

        this.uuidArea = new Input();
        this.uuidArea.setPlaceholder("UUID");


        this.uuidArea.setBounds(5, 5, 200, 25);
        this.add(this.uuidArea);
    }
}
