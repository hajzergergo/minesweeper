package toplist;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Eredmények mentéséért felelős osztáy.
 */
public class DomParser {

    private static Logger logger = LoggerFactory.getLogger(DomParser.class);

    /**
     * A {@link DocumentBuilder}-t létrehozó factory.
     */
    private static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    /**
     * A {@link Document}-t létrehozó Builder.
     */
    private static DocumentBuilder db;
    /**
     * A gyökér elemet reprezentáló xml {@link Element}.
     */
    private static Element root;
    /**
     * Az xml documentum.
     */
    private static Document doc;
    /**
     * A kezelni kívánt fájl.
     */
    private static File input;

    /**
     * Kiírja a paraméterül kapott játékos adatatit(név, pontszám) a topmine.xml-be.
     * @param p játékos.
     */
    public static void writeToTopMineSweeper(Player p){
        try{

            db = dbf.newDocumentBuilder();

            //input = new File("topmine.xml");
            input = new File( System.getProperty("user.home") + "/topmine.xml");
            if (input.exists()){
                doc = db.parse(input);
            } else{
                doc = db.newDocument();
            }

            if (doc.getDocumentElement() == null){
                root = doc.createElement("players");
                doc.appendChild(root);
            } else {
                root = doc.getDocumentElement();
            }

            Element player = doc.createElement("player");

            Element name = doc.createElement("name");
            name.appendChild(doc.createTextNode(p.getUsername()));
            player.appendChild(name);

            Element score = doc.createElement("score");
            score.appendChild(doc.createTextNode(Integer.toString(p.getPoints())));
            player.appendChild(score);

            root.appendChild(player);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();

            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(input);

            t.transform(source,result);

            logger.info("Writing player(" + p.getUsername() + ") to topmine.xml");

        } catch (Exception e){
            e.printStackTrace();
            logger.error("Failed to write to topmine.xml");
        }
    }

    /**
     * Visszaadja a legeredményesebb játkosokat.
     *
     * Beolvassa a topmine.xml fájlból a játékosokat, rendezi
     * pontszám szerint csökkenő sorrendbe, majd kiválasztja az első tizet.
     *
     * @return Legtöbb ponttal rendelkező 10 {@link Player}.
     */
    public static List<Player> readFromTopMindeSweeper(){
        List<Player> players = new ArrayList<>();
        try{
            db = dbf.newDocumentBuilder();
            //input = new File("topmine.xml");
            input = new File( System.getProperty("user.home") + "/topmine.xml");
            doc = db.parse(input);

            Element e;
            NodeList nl = doc.getElementsByTagName("player");

            for (int i = 0; i < nl.getLength(); i++){
                e = (Element) nl.item(i);
                players.add(new Player(e.getElementsByTagName("name").item(0).getTextContent(),Integer.parseInt(e.getElementsByTagName("score").item(0).getTextContent())));
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to read from topmine.xml");
        }

        logger.info("Reading players from topmine.xml");
        return players.stream().sorted((x,y) -> y.getPoints()-x.getPoints()).limit(10).collect(Collectors.toList());
    }

}
