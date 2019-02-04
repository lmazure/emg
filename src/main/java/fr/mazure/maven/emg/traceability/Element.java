package fr.mazure.maven.emg.traceability;

public class Element {

    
    final private String _id; 
    final private String _location; 

    /**
     * @param id
     * @param location
     */
    public Element(final String id, final String location) {
        _id = id;
        _location = location;
    }

    /**
     * @return the id
     */
    public String getId() {
        return _id;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return _location;
    }
}
