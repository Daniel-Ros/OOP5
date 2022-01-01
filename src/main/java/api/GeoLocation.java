package api;
/**
 * This interface represents a geo location <x,y,z>, (aka Point3D data).
 *
 */
public interface GeoLocation {
    public double x();
    public double y();
    public double z();
    public double distance(GeoLocation g);
    public boolean inRect(GeoLocation p1, GeoLocation p2);
}
