package dev.floffah.gamermode.utils.ui;

import dev.floffah.gamermode.utils.ui.component.Input;
import java.awt.*;
import java.util.UUID;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import lombok.val;

public class UUIDConvertPane extends JPanel {

    JLabel uuidText;

    Input uuidArea;

    Input uuidMost;
    Input uuidLeast;

    long uuidMostVal = 0;
    long uuidLeastVal = 0;

    public UUIDConvertPane() {
        super();
        this.setLayout(null);

        val pane = this;

        this.uuidText = new JLabel("UUID");
        this.uuidText.setBounds(5, 5, 200, 25);
        this.add(this.uuidText);

        this.uuidArea = new Input();
        this.uuidArea.setPlaceholder("UUID");
        this.uuidArea.setBounds(5, 30, 400, 20);
        this.uuidArea.getDocument()
            .addDocumentListener(
                this.createUUIDInputDocumentListener(
                        this.uuidArea,
                        (Long value) -> this.uuidMostVal = value,
                        (Long value) -> this.uuidLeastVal = value
                    )
            );
        this.add(this.uuidArea);

        this.uuidMost = new Input();
        this.uuidMost.setPlaceholder("Most sig bits");
        this.uuidMost.setBounds(5, 55, 195, 20);
        this.uuidMost.getDocument()
            .addDocumentListener(
                this.createLongInputDocumentListener(
                        this.uuidMost,
                        (Long value) -> this.uuidMostVal = value
                    )
            );
        this.add(this.uuidMost);

        this.uuidLeast = new Input();
        this.uuidLeast.setPlaceholder("Least sig bits");
        this.uuidLeast.setBounds(210, 55, 195, 20);
        this.uuidMost.getDocument()
            .addDocumentListener(
                this.createLongInputDocumentListener(
                        this.uuidLeast,
                        (Long value) -> this.uuidLeastVal = value
                    )
            );
        this.add(this.uuidLeast);

        this.updateValues();
    }

    public void updateValues() {
        SwingUtilities.invokeLater(() -> {
            String newUUIDString = new UUID(this.uuidMostVal, this.uuidLeastVal)
                .toString();
            if (
                !this.uuidArea.getText().equals(newUUIDString)
            ) this.uuidArea.setText(newUUIDString);
            String newMostLong = Long.toString(this.uuidMostVal);
            if (
                !this.uuidMost.getText().equals(newMostLong)
            ) this.uuidMost.setText(newMostLong);
            String newLeastLong = Long.toString(this.uuidLeastVal);
            if (
                !this.uuidLeast.getText().equals(newLeastLong)
            ) this.uuidLeast.setText(newLeastLong);
        });
    }

    public DocumentListener createUUIDInputDocumentListener(
        Input input,
        Consumer<Long> onNewMostValue,
        Consumer<Long> onNewLeastValue
    ) {
        Color defaultColor = input.getBackground();
        val pane = this;
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }

            public void changed() {
                try {
                    val uuid = UUID.fromString(input.getText());
                    long most = uuid.getMostSignificantBits();
                    long least = uuid.getLeastSignificantBits();
                    SwingUtilities.invokeLater(() -> onNewMostValue.accept(most)
                    );
                    SwingUtilities.invokeLater(() ->
                        onNewLeastValue.accept(least)
                    );
                    if (input.getBackground() == Color.red) input.setBackground(
                        defaultColor
                    );
                    pane.updateValues();
                } catch (IllegalArgumentException e) {
                    input.setBackground(Color.red);
                }
            }
        };
    }

    public DocumentListener createLongInputDocumentListener(
        Input input,
        Consumer<Long> onNewValue
    ) {
        Color defaultColor = input.getBackground();
        val pane = this;
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }

            public void changed() {
                try {
                    Long newValue = Long.parseLong(input.getText());
                    SwingUtilities.invokeLater(() -> onNewValue.accept(newValue)
                    );
                    if (input.getBackground() == Color.red) input.setBackground(
                        defaultColor
                    );
                    pane.updateValues();
                } catch (NumberFormatException e) {
                    input.setBackground(Color.red);
                }
            }
        };
    }
}
