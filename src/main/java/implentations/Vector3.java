package implentations;

import api.GeoLocation;

public class Vector3 implements GeoLocation {
    double x,y,z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    public void normilze(){
        GeoLocation nor = new Vector3(0,0,0);

        x /= distance(nor);
        y /= distance(nor);
        z /= distance(nor);
    }

    @Override
    public double distance(GeoLocation g) {
        return Math.sqrt(Math.pow(x - g.x(),2) + Math.pow(y - g.y(),2) + Math.pow(z - g.z(),2));
    }

    public double distanceSqrt(GeoLocation g) {
        return Math.pow(x - g.x(),2) + Math.pow(y - g.y(),2);
    }

    public GeoLocation sum(GeoLocation g) {
        return new Vector3(x + g.x(),y + g.y(),z + g.z());
    }

    public Vector3 sub(GeoLocation g) {
        return new Vector3(x - g.x(),y - g.y(),z - g.z());
    }

    public boolean inRect(GeoLocation p1, GeoLocation p2){
        double min_x = Math.min(p1.x(), p2.x()) - 0.0001;
        double min_y = Math.min(p1.y(), p2.y()) - 0.0001;
        double max_x = Math.max(p1.x(), p2.x()) + 0.0001;
        double max_y = Math.max(p1.y(), p2.y()) + 0.0001;

        return x > min_x && x < max_x && y > min_y && y < max_y;
    }

    public static GeoLocation fromString(String pos)
    {
        String [] poss = pos.split(",");
        return new Vector3(Double.parseDouble(poss[0]),Double.parseDouble(poss[1]),Double.parseDouble(poss[2]));
    }
}
