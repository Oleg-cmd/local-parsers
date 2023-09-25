package fileManager;

import model.Movie;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;

/**
 A utility class for converting a HashSet of Movie objects to an XML string representation.
 */
public class XmlConverter {

    /**
     * Converts a HashSet of Movie objects to an XML string representation.
     *
     * @param movies the HashSet of Movie objects to be converted
     * @param writer the BufferedWriter to write the resulting XML string to
     * @throws ParserConfigurationException if there is a problem with the XML parser configuration
     * @throws IOException if there is a problem with writing the XML string
     */
    public static void convertToXml(HashSet<Movie> movies, BufferedWriter writer) throws ParserConfigurationException, IOException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        Element root = document.createElement("root");
        document.appendChild(root);

        for (Movie movie : movies) {
            Element movieElem = document.createElement("Movie");
            root.appendChild(movieElem);

            Element idElem = document.createElement("id");
            Text idText = document.createTextNode(String.valueOf(movie.getId()));
            idElem.appendChild(idText);
            movieElem.appendChild(idElem);

            Element nameElem = document.createElement("name");
            Text nameText = document.createTextNode(movie.getName());
            nameElem.appendChild(nameText);
            movieElem.appendChild(nameElem);

            // Add Director details

            Element directorElem = document.createElement("director");
            movieElem.appendChild(directorElem);

            Element directorNameElem = document.createElement("name");
            Text directorNameText = document.createTextNode(movie.getDirector().getName());
            directorNameElem.appendChild(directorNameText);
            directorElem.appendChild(directorNameElem);

            Element directorBirthdayElem = document.createElement("birthday");
            Text directorBirthdayText = document.createTextNode(movie.getDirector().getBirthday().toString());
            directorBirthdayElem.appendChild(directorBirthdayText);
            directorElem.appendChild(directorBirthdayElem);

            Element directorHeightElem = document.createElement("height");
            Text directorHeightText = document.createTextNode(String.valueOf(movie.getDirector().getHeight()));
            directorHeightElem.appendChild(directorHeightText);
            directorElem.appendChild(directorHeightElem);

            Element directorEyeColorElem = document.createElement("eyeColor");
            Text directorEyeColorText = document.createTextNode(movie.getDirector().getEyeColor().toString());
            directorEyeColorElem.appendChild(directorEyeColorText);
            directorElem.appendChild(directorEyeColorElem);

            // Add Location details for Director
            Element directorLocationElem = document.createElement("location");
            directorElem.appendChild(directorLocationElem);

            Element locationNameElem = document.createElement("name");
            Text locationNameText = document.createTextNode(movie.getDirector().getLocation().getName());
            locationNameElem.appendChild(locationNameText);
            directorLocationElem.appendChild(locationNameElem);

            Element locationXElem = document.createElement("x");
            Text locationXText = document.createTextNode(String.valueOf(movie.getDirector().getLocation().getX()));
            locationXElem.appendChild(locationXText);
            directorLocationElem.appendChild(locationXElem);

            Element locationYElem = document.createElement("y");
            Text locationYText = document.createTextNode(String.valueOf(movie.getDirector().getLocation().getY()));
            locationYElem.appendChild(locationYText);
            directorLocationElem.appendChild(locationYElem);


            Element time = document.createElement("creationDate");
            Text creationText = document.createTextNode(movie.getCreationDate().toString());
            time.appendChild(creationText);
            movieElem.appendChild(time);

            Element rating = document.createElement("mpaaRating");
            Text ratingText = document.createTextNode(movie.getMpaaRating().toString());
            rating.appendChild(ratingText);
            movieElem.appendChild(rating);

            Element coordinatesElem = document.createElement("coordinates");


            Element xElem = document.createElement("x");
            Text xText = document.createTextNode(String.valueOf(movie.getCoordinates().getX()));
            xElem.appendChild(xText);
            coordinatesElem.appendChild(xElem);

            Element yElem = document.createElement("y");
            Text yText = document.createTextNode(String.valueOf(movie.getCoordinates().getY()));
            yElem.appendChild(yText);
            coordinatesElem.appendChild(yElem);

            movieElem.appendChild(coordinatesElem);

            Element oscarsCountElem = document.createElement("oscarsCount");
            Text oscarsCountText = document.createTextNode(String.valueOf(movie.getOscarsCount()));
            oscarsCountElem.appendChild(oscarsCountText);
            movieElem.appendChild(oscarsCountElem);

            Element goldenPalmCount = document.createElement("goldenPalmCount");
            Text palmText = document.createTextNode(movie.getGoldenPalmCount().toString());
            goldenPalmCount.appendChild(palmText);
            movieElem.appendChild(goldenPalmCount);

            Element taglineElem = document.createElement("tagline");
            Text taglineText = document.createTextNode(movie.getTagline());
            taglineElem.appendChild(taglineText);
            movieElem.appendChild(taglineElem);
        }

        writer.write(documentToString(document));
        writer.flush();
    }

    /**
     Converts the given XML Document to a string representation.
     @param document the XML Document to be converted to a string
     @return the string representation of the XML Document
     @throws IOException if an error occurs while converting the XML Document to a string
     */
    private static String documentToString(Document document) throws IOException {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(document);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);

            return writer.toString();
        } catch (TransformerException e) {
            throw new IOException("Error converting XML document to string", e);
        }
    }
}
