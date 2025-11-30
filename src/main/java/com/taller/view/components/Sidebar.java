package com.taller.view.components;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Sidebar extends JPanel {

    private final List<SidebarButton> botones = new ArrayList<>();

    public Sidebar() {
        setLayout(new GridLayout(0, 1, 0, 5));
        setBackground(new Color(245, 245, 245));
        setPreferredSize(new Dimension(220, 600));
    }

    public SidebarButton addButton(String texto, Icon icono) {
        SidebarButton btn = new SidebarButton(texto, icono);
        botones.add(btn);
        add(btn);
        return btn;
    }

    public void marcarActivo(SidebarButton boton) {
        for (SidebarButton b : botones)
            b.setActive(b == boton);
    }
}
