package GUI;

import Game.Agent;
import Game.ClientData;
import Game.GameData;
import Game.Pokemon;
import api.*;
import implentations.DirectedWeightedGraphAlgorithmsImpl;
import implentations.Vector3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Main panel to draw the graph
 */
public class GrapPanel extends JPanel implements MouseListener, MouseWheelListener , MouseMotionListener , ActionListener{
    private DirectedWeightedGraphAlgorithms ga;
    private GeoLocation min,max;


    private Point2D pos;
    private Point2D prevPos;
    private Point2D nextPos;
    private double zoom;

    private HashMap<Shape,Integer> circles;

    GameData gd;
    ClientData cd;

    JButton stop;

    GrapPanel(DirectedWeightedGraphAlgorithms g, GeoLocation min, GeoLocation max, GameData gd, ClientData cd){
        this.gd = gd;
        this.cd = cd;

        ga = g;
        this.min = min;
        this.max = max;
        zoom =1;
        pos = new Point(0,0);
        prevPos = (Point2D) pos.clone();
        nextPos = (Point2D) pos.clone();
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);

        stop = new JButton("stop");
        stop.setSize(100,50);
        stop.addActionListener(this);
        stop.setLocation(0,50);

        add(stop);
    }

    /**
     * Draws the graph
     * @param graphics
     */
    public void paint(Graphics graphics){
        if(ga.getGraph() == null){
            return;
        }
        //stop.repaint();

        this.min =((DirectedWeightedGraphAlgorithmsImpl)ga).getMin();
        this.max = ((DirectedWeightedGraphAlgorithmsImpl)ga).getMax();
        circles = new HashMap<>();
        DirectedWeightedGraph graph = ga.getGraph();
        Graphics2D g = (Graphics2D)graphics;
        Iterator<NodeData> itNodes = graph.nodeIter();

        synchronized (cd){
            int moves = cd.getMoves();
            int time = cd.timeToEnd();
            g.drawString("moves: " + moves,5,10);
            g.drawString("grade: " + cd.getGrade(),5,24);
            g.drawString("time left: " + TimeUnit.MILLISECONDS.toSeconds(time),5,38);
        }

        //draw nodes
        while(itNodes.hasNext()){
            NodeData n = itNodes.next();
            GeoLocation posInScreen = getPoint2ScreenCord(n.getLocation().x(),n.getLocation().y());
            Ellipse2D c = new Ellipse2D.Double(posInScreen.x() - (int)(10*zoom),(int)posInScreen.y() - (int)(10*zoom),(int)(20*zoom),(int)(20*zoom));
            circles.put(c,n.getKey());
            g.setPaint(new Color(n.getTag()));
            g.draw(c);
            g.drawString(String.valueOf(n.getKey()),(int)posInScreen.x() - 15 ,(int)posInScreen.y() - 15);
        }
        List<EdgeData> specialEdges = new ArrayList<>();
        Iterator<EdgeData> itEdges = graph.edgeIter();
        //draw edges
        while(itEdges.hasNext()){
            EdgeData e = itEdges.next();
            GeoLocation p1 = getPoint2ScreenCord(graph.getNode(e.getSrc()).getLocation().x(),
                                                          graph.getNode(e.getSrc()).getLocation().y());
            GeoLocation p2 = getPoint2ScreenCord(graph.getNode(e.getDest()).getLocation().x(),
                                                    graph.getNode(e.getDest()).getLocation().y());
            g.setPaint(new Color(e.getTag()));
            if(e.getTag() != 0){
                specialEdges.add(e);
            }else{
                g.setStroke(new BasicStroke(1));
                drawArrow(g,p1,p2,10F*(float)zoom);
            }
        }

        //draw special edges
        for (EdgeData e : specialEdges){
            g.setStroke(new BasicStroke(2));
            GeoLocation p1 = getPoint2ScreenCord(graph.getNode(e.getSrc()).getLocation().x(),
                    graph.getNode(e.getSrc()).getLocation().y());
            GeoLocation p2 = getPoint2ScreenCord(graph.getNode(e.getDest()).getLocation().x(),
                    graph.getNode(e.getDest()).getLocation().y());
            g.setPaint(new Color(e.getTag()));
            drawArrow(g,p1,p2,15F);
        }

        synchronized (gd){
            for (Pokemon p : gd.getFreePokemons())
            {
                g.setStroke(new BasicStroke(5));
                if(p.getType() == 1){
                    g.setPaint(Color.GREEN);
                }else{
                    g.setPaint(Color.BLUE);
                }
                GeoLocation start = gd.getGa().getGraph().getNode(p.getEdge().getSrc()).getLocation();
                start = getPoint2ScreenCord(start.x(),start.y());
                GeoLocation point = getPoint2ScreenCord(p.getPos().x(),p.getPos().y());
                drawArrowHead(g,start,point,30);
            }


            for (Agent a : gd.getAgents()) {
                g.setPaint(Color.RED);
                GeoLocation point = getPoint2ScreenCord(a.getPos().x(), a.getPos().y());
                g.drawOval((int) point.x() - 7, (int)point.y() - 7, 15,15);
            }
        }
    }

    // Transform world position to screen sopition
    private GeoLocation getPoint2ScreenCord(double x,double y){
        double newx = (((max.x() - x) / (max.x() - min.x()))*this.getWidth()*0.9 + this.getWidth()*0.05 + pos.getX()) * zoom;
        double newy = (((max.y() - y) / (max.y() - min.y()))*this.getHeight()*0.9 + this.getHeight()*0.05 + pos.getY()) * zoom;
        return new Vector3(newx,newy,0);
    }

    // reverse to getPoint2ScreenCord
    // Transform screenPosition to world position
    private GeoLocation getScreenCord2Point(double x,double y){
        double newx = max.x() - (((x/zoom)-pos.getX()-getWidth()*0.05D)*(max.x() - min.x()))/(getWidth()*0.9D);
        double newy = max.y() - (((y/zoom)-pos.getY()-getHeight()*0.05D)*(max.y() - min.y()))/(getHeight()*0.9D);
        return new Vector3(newx,newy,0);
    }


    private void drawArrowHead(final Graphics2D gfx, final GeoLocation start, final GeoLocation end, final float arrowSize)
    {
        final double startx = start.x();
        final double starty = start.y();

        final double deltax = startx - end.x();
        final double result;
        if (deltax == 0.0d) {
            result = Math.PI / 2;
        }
        else {
            result = Math.atan((starty - end.y()) / deltax) + (startx < end.x() ? Math.PI : 0);
        }

        final double angle = result;

        final double arrowAngle = Math.PI / 12.0d;

        final double x1 = arrowSize * Math.cos(angle - arrowAngle);
        final double y1 = arrowSize * Math.sin(angle - arrowAngle);
        final double x2 = arrowSize * Math.cos(angle + arrowAngle);
        final double y2 = arrowSize * Math.sin(angle + arrowAngle);

        final double cx = (arrowSize / 2.0f) * Math.cos(angle);
        final double cy = (arrowSize / 2.0f) * Math.sin(angle);

        final GeneralPath polygon = new GeneralPath();
        polygon.moveTo(end.x(), end.y());
        polygon.lineTo(end.x() + x1, end.y() + y1);
        polygon.lineTo(end.x() + x2, end.y() + y2);
        polygon.closePath();
        gfx.fill(polygon);
    }

    //draws an arrow
    private void drawArrow (final Graphics2D gfx, final GeoLocation start, final GeoLocation end, final float arrowSize) {

        final double startx = start.x();
        final double starty = start.y();

        final double deltax = startx - end.x();
        final double result;
        if (deltax == 0.0d) {
            result = Math.PI / 2;
        }
        else {
            result = Math.atan((starty - end.y()) / deltax) + (startx < end.x() ? Math.PI : 0);
        }

        final double angle = result;

        final double arrowAngle = Math.PI / 12.0d;

        final double x1 = arrowSize * Math.cos(angle - arrowAngle);
        final double y1 = arrowSize * Math.sin(angle - arrowAngle);
        final double x2 = arrowSize * Math.cos(angle + arrowAngle);
        final double y2 = arrowSize * Math.sin(angle + arrowAngle);

        final double cx = (arrowSize / 2.0f) * Math.cos(angle);
        final double cy = (arrowSize / 2.0f) * Math.sin(angle);

        final GeneralPath polygon = new GeneralPath();
        polygon.moveTo(end.x(), end.y());
        polygon.lineTo(end.x() + x1, end.y() + y1);
        polygon.lineTo(end.x() + x2, end.y() + y2);
        polygon.closePath();
        gfx.fill(polygon);

        gfx.drawLine((int) startx, (int) starty, (int) (end.x() + cx), (int) (end.y() + cy));
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        nextPos = e.getPoint();
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        prevPos = (Point2D) pos.clone();
    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * Invoked when the mouse wheel is rotated.
     *
     * @param e the event to be processed
     * @see MouseWheelEvent
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(zoom + (double)(-e.getWheelRotation()) / 10 > 0.1)
            zoom += (double)(-e.getWheelRotation()) / 10;
        getTopLevelAncestor().repaint();
    }

    /**
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  {@code MOUSE_DRAGGED} events will continue to be
     * delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position
     * is within the bounds of the component).
     * <p>
     * Due to platform-dependent Drag&amp;Drop implementations,
     * {@code MOUSE_DRAGGED} events may not be delivered during a native
     * Drag&amp;Drop operation.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        pos.setLocation(prevPos.getX() + (e.getX() - nextPos.getX())/zoom ,prevPos.getY() + (e.getY() - nextPos.getY())/zoom);
        getTopLevelAncestor().repaint();
    }

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        synchronized (cd){
            cd.stop();
        }
    }
}
