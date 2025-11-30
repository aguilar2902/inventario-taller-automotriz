package com.taller.view.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TopBar extends JPanel {
    public TopBar(String p_titulo){
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblTitulo = new JLabel(p_titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        add(lblTitulo, BorderLayout.WEST);
    }
}
