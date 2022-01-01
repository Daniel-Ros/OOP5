package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * About window to display some information about the project
 */
public class AboutWindow extends JPanel{
    JLabel about;
    JLabel gitHubLink;
    public AboutWindow(){
        setLayout(new GridBagLayout());
        GridBagConstraints layout= new GridBagConstraints();
        layout.insets = new Insets(2,2,2,2);
        about = new JLabel("<html>\n" +
                "<b>About</b>\n" +
                "<br><b>Menu Bar:</b></br>\n" +"<br><br>"+
                "<br><b>File: </b><br>\n" +
                "<li type=disc>Load: Import supported graph JSON file from Directory.\n" +
                "<br><li type=disc> Save: Export JSON file.</br>\n" +
                "<li type=disc>Exit: close program.\n" +
                "<br></br>\n" +
                "    <br><b>Algo: </b></br>\n" +
                "<li type=disc> tsp:\n" +
                "<br><li type=disc>Shortest Path Distance: calculates the shortest distance between two Vertex </br>\n" +
                "<li type=disc>Shortest Path: Mark the shortest path from Source to Destination\n" +
                "<br><li type=disc>Connect: creates new weighted edge from Source to Destination </br>\n" +
                "<li type=disc> Is Connected: check if the graph is connected\n" +
                "<br><li type=disc>Center: The center point of the graph</br>\n" +
                "<br><br>\n"+
                "    <br><b>Graph: </b></br>\n" +
                "<li type=square>Vertex: Edit/ Delete Vertex\n" +
                "<li type=square>Edge: Add/ Delete/ Edit Edge\n" +
                "<br><br>\n" +
                "<b>Side Panel (Same functions as Algo)</b>\n" +
                "<br><br>\n" +
                "    <b>Graph Panel:</b>\n" +
                "<li type=disc> Mouse wheel: Zoom In/Out.\n" +
                "    <br><li type=disc>Double Click on Vertex open edit vertex window<br>\n" +
                "<li type=disc>Double Click on Graph Panel creates new Vertex\n" +
                "    <br><li type=disc>Click hold moves graph panel<br>\n" +
                "<br><br>\n" +
                "  Made By: Daniel Rosenberg and Daniel Zinn\n" +
                "</html>");
        gitHubLink= new JLabel("<html><A HREF=\"https://github.com/Daniel-Ros/OOP3\">GitHub Link</a><html>\n");
        about.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        gitHubLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        String url= "https://github.com/Daniel-Ros/OOP3";
        gitHubLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Desktop desktop= Desktop.getDesktop();
                try{
                    desktop.browse(new URI(url));
                }catch (IOException | URISyntaxException er){
                    er.printStackTrace();
                }
            }
        });
        layout.gridx= 1;
        layout.ipadx=40;
        layout.ipady=40;
        super.add(about,layout);
        layout.gridx= 1;
        layout.ipadx=20;
        layout.ipady=20;
        super.add(gitHubLink,layout);
    }
}
