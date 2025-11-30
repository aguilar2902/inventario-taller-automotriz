package com.taller.view.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarButton extends JButton {
    private Color normalBg = new Color(245, 245, 245);
    private Color hoverBg = new Color(230, 230, 230);
    private Color activeBg = new Color(200, 220, 255);

    private boolean active = false;

    public SidebarButton(String texto, Icon icono) {
        super(texto, icono);

        setHorizontalAlignment(SwingConstants.LEFT);
        setBackground(normalBg);
        setFocusPainted(false);
        setBorder(new EmptyBorder(12, 20, 12, 10));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!active) setBackground(hoverBg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!active) setBackground(normalBg);
            }
        });
    }

    public void setActive(boolean value) {
        this.active = value;
        setBackground(value ? activeBg : normalBg);
    }

    public boolean isActive() {
        return active;
    }
}
