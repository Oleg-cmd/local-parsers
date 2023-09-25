ackage fileManager;

import collectionWorker.Command;
import model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.time.ZonedDateTime;
import java.util.HashSet;

/**
 The XmlToJava class provides a method to parse an XML file and convert its contents to a HashSet of Movie objects.
 */
public class XmlToJava implements Command {
    /**
     * A HashSet of Movie objects to store parsed XML data.
     */
    public static HashSet<Movie> movies = new HashSet<>();

    /**
     * Parses an XML file and returns a HashSet of Movie objects.
     *
     * @param filename the name of the XML file to be parsed.
     * @return a HashSet of Movie objects containing data from the specified XML file.
     */
    public static HashSet<Movie> parseXml(String filename) {
        try {
            // Open the XML file and create a reader
            File xmlFile = new File(filename);
            Reader fileReader = new FileReader(xmlFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Read the contents of the file into a StringBuilder
            StringBuilder xmlString = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                xmlString.append(line);
            }
            bufferedReader.close();

            // Parse the XML file using a DocumentBuilder
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            // Normalize the document
            doc.getDocumentElement().normalize();

            // Get all nodes in the document
            NodeList nodeList = doc.getElementsByTagName("*");

            // Loop through all nodes and extract data for Movie objects
            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Node node = nodeList.item(temp);
                if (!node.getNodeName().equals("root") && node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    NodeList childNodes = element.getChildNodes();
                    if(childNodes.getLength() != 1){
                        Element elem = (Element) childNodes;
//                        System.out.println(elem.getNodeName());
                        if(elem.getNodeName().equals("Movie")){
                            Movie movie = new Movie();
                            Coordinates coordinates = new Coordinates();
                            Person director = new Person();
                            for (int i = 0; i < childNodes.getLength(); i++) {
                                Node childNode = childNodes.item(i);
                                if (childNode != null && childNode.getNodeType() == Node.ELEMENT_NODE) {
                                    String content = childNode.getTextContent();
                                    if (content != null) {
                                        switch(childNode.getNodeName()) {
                                            case "id":
                                                try{
                                                    movie.setId(Integer.parseInt(content));
                                                }catch (NumberFormatException e){
                                                    System.out.println("id is not valid, i generate new");
                                                    movie.setId(collectionManager.getRandomID());
                                                }
                                                break;
                                            case "name":
                                                movie.setName(content);
                                                break;
                                            case "coordinates":
                                                Element coordinatesElement = (Element) childNode;
                                                NodeList xList = coordinatesElement.getElementsByTagName("x");
                                                NodeList yList = coordinatesElement.getElementsByTagName("y");
                                                if (xList.getLength() > 0 && yList.getLength() > 0) {
                                                    try{
                                                        coordinates.setX(Float.parseFloat(xList.item(0).getTextContent()));
                                                        coordinates.setY(Float.parseFloat(yList.item(0).getTextContent()));
                                                    }catch (NumberFormatException e){
                                                        System.out.println("incorrect coordinates");
                                                        coordinates.setX(0);
                                                        coordinates.setY(0);
                                                    }
                                                    movie.setCoordinates(coordinates);
                                                }
                                                break;
                                            case "creationDate":
                                                try{
                                                    movie.setCreationDate(ZonedDateTime.parse(content));
                                                }catch (IllegalArgumentException e){
                                                    System.out.println("illegal creation date");
                                                    movie.setCreationDate(ZonedDateTime.now());
                                                }
                                                break;
                                            case "oscarsCount":
                                                try{
                                                    movie.setOscarsCount(Integer.parseInt(content));
                                                }catch (NumberFormatException e){
                                                    System.out.println("not valid field");
                                                    movie.setOscarsCount(0);
                                                }
                                                break;
                                            case "goldenPalmCount":
                                                try{
                                                    movie.setGoldenPalmCount(Integer.parseInt(content));
                                                }catch (NumberFormatException e){
                                                    System.out.println("not valid field");
                                                    movie.setGoldenPalmCount(0);
                                                }
                                                break;
                                            case "tagline":
                                                movie.setTagline(content);
                                                break;
                                            case "mpaaRating":
                                                try{
                                                    movie.setMpaaRating(MpaaRating.valueOf(content));
                                                }catch (IllegalArgumentException e){
                                                    System.out.println("illegal rating");
                                                    movie.setMpaaRating(MpaaRating.PG);
                                                }
                                                break;
                                            case "director":
                                                Element directorElement = (Element) childNode;
                                                NodeList nameElements = directorElement.getElementsByTagName("name");
                                                if (nameElements.getLength() > 0) {
                                                    director.setName(nameElements.item(0).getTextContent());
                                                }
                                                NodeList heightElements = directorElement.getElementsByTagName("height");
                                                if (heightElements.getLength() > 0) {
                                                    try {
                                                        director.setHeight(Double.parseDouble(heightElements.item(0).getTextContent()));
                                                    }catch (NumberFormatException e){
                                                        System.out.println("illegal height");
                                                        director.setHeight(1);
                                                    }

                                                }
                                                NodeList birth = directorElement.getElementsByTagName("birthday");
                                                try{
                                                    director.setBirthday(ZonedDateTime.parse(birth.item(0).getTextContent()));
                                                }catch (IllegalArgumentException e){
                                                    System.out.println("illegal birthday");
                                                    director.setBirthday(ZonedDateTime.now());
                                                }


                                                NodeList color = directorElement.getElementsByTagName("eyeColor");
                                                try{
                                                    director.setEyeColor(Color.valueOf(color.item(0).getTextContent()));
                                                }catch (IllegalArgumentException e){
                                                    System.out.println("illegal color");
                                                    director.setEyeColor(Color.GREEN);
                                                }


                                                NodeList location = directorElement.getElementsByTagName("location");
                                                if (location.getLength() > 0) {
                                                    Element locationElement = (Element) location.item(0);
                                                    NodeList xElements = locationElement.getElementsByTagName("x");
                                                    NodeList yElements = locationElement.getElementsByTagName("y");
                                                    NodeList name = locationElement.getElementsByTagName("name");
                                                    Double directorX = 0.0;
                                                    Double directorY = 0.0;
                                                    String directorLocationName = "";
                                                    if (xElements.getLength() > 0) {
                                                        try {
                                                            directorX = Double.parseDouble(xElements.item(0).getTextContent());
                                                        }catch (NumberFormatException e){
                                                            System.out.println("illegal directorX");
                                                            directorX = 1.0;
                                                        }

                                                    }
                                                    if (yElements.getLength() > 0) {
                                                        try {
                                                            directorY = Double.parseDouble(yElements.item(0).getTextContent());
                                                        }catch (NumberFormatException e){
                                                            System.out.println("illegal directorX");
                                                            directorY = 1.0;
                                                        }
                                                    }
                                                    if (name.getLength() > 0) {
                                                        directorLocationName = name.item(0).getTextContent();
                                                    }
                                                    Location directorLocation = new Location();
                                                    directorLocation.setLocation(directorX, directorY, directorLocationName);
                                                    director.setLocation(directorLocation);
                                                }
                                                movie.setDirector(director);
                                                break;
                                        }
                                    }else{
//                                    System.out.println("");
                                    }

                                }
                            }
                            movies.add(movie);
                        }
                    }
                }

            }
//            System.out.println(movies);
            return movies;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }

    @Override
    public void execute() {
    }
}
